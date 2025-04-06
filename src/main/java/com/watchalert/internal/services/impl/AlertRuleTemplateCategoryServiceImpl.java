package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateCategory;
import com.watchalert.internal.repositories.AlertRuleTemplateCategoryRepository;
import com.watchalert.internal.services.AlertRuleTemplateCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateCategoryServiceImpl implements AlertRuleTemplateCategoryService {

    @Autowired
    private AlertRuleTemplateCategoryRepository categoryRepository;

    @Override
    @Transactional
    public AlertRuleTemplateCategory createCategory(AlertRuleTemplateCategory category) {
        log.debug("Creating category: {}", category.getName());
        
        // 验证名称是否可用
        if (!isNameAvailable(category.getName(), null)) {
            throw new IllegalArgumentException("Category name already exists: " + category.getName());
        }
        
        // 设置层级
        if (category.getParentId() != null) {
            AlertRuleTemplateCategory parent = getCategory(category.getParentId());
            category.setLevel(parent.getLevel() + 1);
        } else {
            category.setLevel(1);
        }
        
        // 设置排序号
        List<AlertRuleTemplateCategory> siblings = getChildCategories(category.getParentId());
        int maxSort = siblings.stream()
                .mapToInt(AlertRuleTemplateCategory::getSort)
                .max()
                .orElse(0);
        category.setSort(maxSort + 1);
        
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public AlertRuleTemplateCategory updateCategory(AlertRuleTemplateCategory category) {
        log.debug("Updating category: {}", category.getId());
        
        AlertRuleTemplateCategory existing = getCategory(category.getId());
        
        // 验证名称是否可用
        if (!category.getName().equals(existing.getName()) && 
            !isNameAvailable(category.getName(), category.getId())) {
            throw new IllegalArgumentException("Category name already exists: " + category.getName());
        }
        
        // 更新基本信息
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        
        return categoryRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.debug("Deleting category: {}", id);
        
        AlertRuleTemplateCategory category = getCategory(id);
        
        // 检查是否有子分类
        List<AlertRuleTemplateCategory> children = getChildCategories(id);
        if (!children.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with children: " + id);
        }
        
        // 检查是否有关联的模板
        if (!category.getTemplates().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with templates: " + id);
        }
        
        categoryRepository.delete(category);
    }

    @Override
    public AlertRuleTemplateCategory getCategory(Long id) {
        log.debug("Getting category: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    }

    @Override
    public List<AlertRuleTemplateCategory> getAllCategories() {
        log.debug("Getting all categories");
        return categoryRepository.findAll();
    }

    @Override
    public List<AlertRuleTemplateCategory> getChildCategories(Long parentId) {
        log.debug("Getting child categories for parent: {}", parentId);
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    @Transactional
    public AlertRuleTemplateCategory moveCategory(Long id, Long newParentId, int newSort) {
        log.debug("Moving category: {} to parent: {} with sort: {}", id, newParentId, newSort);
        
        AlertRuleTemplateCategory category = getCategory(id);
        Long oldParentId = category.getParentId();
        
        // 更新父级ID和层级
        category.setParentId(newParentId);
        if (newParentId != null) {
            AlertRuleTemplateCategory newParent = getCategory(newParentId);
            category.setLevel(newParent.getLevel() + 1);
        } else {
            category.setLevel(1);
        }
        
        // 更新排序号
        if (oldParentId != null && oldParentId.equals(newParentId)) {
            // 同一父级下移动
            List<AlertRuleTemplateCategory> siblings = getChildCategories(newParentId);
            siblings.stream()
                    .filter(c -> c.getSort() >= newSort)
                    .forEach(c -> {
                        c.setSort(c.getSort() + 1);
                        categoryRepository.save(c);
                    });
        } else {
            // 移动到新的父级
            List<AlertRuleTemplateCategory> newSiblings = getChildCategories(newParentId);
            newSiblings.stream()
                    .filter(c -> c.getSort() >= newSort)
                    .forEach(c -> {
                        c.setSort(c.getSort() + 1);
                        categoryRepository.save(c);
                    });
            
            if (oldParentId != null) {
                List<AlertRuleTemplateCategory> oldSiblings = getChildCategories(oldParentId);
                oldSiblings.stream()
                        .filter(c -> c.getSort() > category.getSort())
                        .forEach(c -> {
                            c.setSort(c.getSort() - 1);
                            categoryRepository.save(c);
                        });
            }
        }
        
        category.setSort(newSort);
        return categoryRepository.save(category);
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        log.debug("Getting category tree");
        
        List<AlertRuleTemplateCategory> allCategories = getAllCategories();
        Map<Long, List<Map<String, Object>>> childrenMap = new HashMap<>();
        
        // 构建树结构
        List<Map<String, Object>> rootNodes = new ArrayList<>();
        for (AlertRuleTemplateCategory category : allCategories) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", category.getId());
            node.put("name", category.getName());
            node.put("description", category.getDescription());
            node.put("level", category.getLevel());
            node.put("sort", category.getSort());
            
            if (category.getParentId() == null) {
                rootNodes.add(node);
            } else {
                childrenMap.computeIfAbsent(category.getParentId(), k -> new ArrayList<>())
                        .add(node);
            }
        }
        
        // 递归设置子节点
        rootNodes.forEach(node -> setChildren(node, childrenMap));
        
        return rootNodes;
    }

    @Override
    public boolean isNameAvailable(String name, Long excludeId) {
        log.debug("Checking if name is available: {}", name);
        
        AlertRuleTemplateCategory existing = categoryRepository.findByName(name);
        if (existing == null) {
            return true;
        }
        
        return excludeId != null && existing.getId().equals(excludeId);
    }

    @Override
    public List<AlertRuleTemplateCategory> getCategoryPath(Long id) {
        log.debug("Getting category path: {}", id);
        
        List<AlertRuleTemplateCategory> path = new ArrayList<>();
        AlertRuleTemplateCategory category = getCategory(id);
        
        while (category != null) {
            path.add(0, category);
            category = category.getParentId() != null ? 
                    getCategory(category.getParentId()) : null;
        }
        
        return path;
    }

    private void setChildren(Map<String, Object> node, Map<Long, List<Map<String, Object>>> childrenMap) {
        Long id = (Long) node.get("id");
        List<Map<String, Object>> children = childrenMap.get(id);
        
        if (children != null) {
            children.sort(Comparator.comparing(m -> (Integer) m.get("sort")));
            node.put("children", children);
            children.forEach(child -> setChildren(child, childrenMap));
        }
    }
} 