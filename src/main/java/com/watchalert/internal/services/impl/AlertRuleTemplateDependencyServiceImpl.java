package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateDependency;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyRepository;
import com.watchalert.internal.services.AlertRuleTemplateDependencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateDependencyServiceImpl implements AlertRuleTemplateDependencyService {

    @Autowired
    private AlertRuleTemplateDependencyRepository dependencyRepository;

    @Override
    @Transactional
    public Long createDependency(Long templateId, Long dependencyId, String dependencyType, String description, String createdBy) {
        log.debug("Creating dependency for template: {}, dependency: {}", templateId, dependencyId);
        
        // 检查是否存在循环依赖
        if (wouldCreateCircularDependency(templateId, dependencyId)) {
            throw new IllegalArgumentException("Circular dependency detected");
        }
        
        AlertRuleTemplateDependency dependency = new AlertRuleTemplateDependency();
        dependency.setTemplateId(templateId);
        dependency.setDependencyId(dependencyId);
        dependency.setDependencyType(dependencyType);
        dependency.setDescription(description);
        dependency.setCreatedBy(createdBy);
        
        return dependencyRepository.save(dependency).getId();
    }

    @Override
    @Transactional
    public void updateDependency(Long dependencyId, String dependencyType, String description) {
        log.debug("Updating dependency: {}", dependencyId);
        
        AlertRuleTemplateDependency dependency = getDependencyEntity(dependencyId);
        dependency.setDependencyType(dependencyType);
        dependency.setDescription(description);
        
        dependencyRepository.save(dependency);
    }

    @Override
    @Transactional
    public void deleteDependency(Long dependencyId) {
        log.debug("Deleting dependency: {}", dependencyId);
        dependencyRepository.deleteById(dependencyId);
    }

    @Override
    public Map<String, Object> getDependency(Long dependencyId) {
        log.debug("Getting dependency: {}", dependencyId);
        return convertDependencyToMap(getDependencyEntity(dependencyId));
    }

    @Override
    public List<Map<String, Object>> getTemplateDependencies(Long templateId) {
        log.debug("Getting dependencies for template: {}", templateId);
        
        return dependencyRepository.findByTemplateId(templateId).stream()
                .map(this::convertDependencyToMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDependentTemplates(Long templateId) {
        log.debug("Getting dependent templates for template: {}", templateId);
        
        return dependencyRepository.findByDependencyId(templateId).stream()
                .map(this::convertDependencyToMap)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasCircularDependency(Long templateId) {
        log.debug("Checking circular dependency for template: {}", templateId);
        return wouldCreateCircularDependency(templateId, templateId);
    }

    @Override
    public Map<String, Object> getDependencyGraphData(Long templateId) {
        log.debug("Getting dependency graph data for template: {}", templateId);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();
        
        // 获取所有相关依赖
        Set<Long> processedTemplates = new HashSet<>();
        Queue<Long> templateQueue = new LinkedList<>();
        templateQueue.add(templateId);
        
        while (!templateQueue.isEmpty()) {
            Long currentTemplateId = templateQueue.poll();
            if (processedTemplates.contains(currentTemplateId)) {
                continue;
            }
            processedTemplates.add(currentTemplateId);
            
            // 添加节点
            Map<String, Object> node = new HashMap<>();
            node.put("id", currentTemplateId);
            node.put("label", "Template " + currentTemplateId);
            nodes.add(node);
            
            // 处理依赖
            List<AlertRuleTemplateDependency> dependencies = dependencyRepository.findByTemplateId(currentTemplateId);
            for (AlertRuleTemplateDependency dependency : dependencies) {
                Long dependencyId = dependency.getDependencyId();
                templateQueue.add(dependencyId);
                
                // 添加边
                Map<String, Object> edge = new HashMap<>();
                edge.put("from", currentTemplateId);
                edge.put("to", dependencyId);
                edge.put("label", dependency.getDependencyType());
                edges.add(edge);
            }
        }
        
        result.put("nodes", nodes);
        result.put("edges", edges);
        
        return result;
    }

    @Override
    public Map<String, Object> getDependencyStatistics(Long templateId) {
        log.debug("Getting dependency statistics for template: {}", templateId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 统计依赖数量
        long totalDependencies = dependencyRepository.countByTemplateId(templateId);
        result.put("totalDependencies", totalDependencies);
        
        // 按依赖类型统计
        List<AlertRuleTemplateDependency> dependencies = dependencyRepository.findByTemplateId(templateId);
        Map<String, Long> typeCount = dependencies.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependency::getDependencyType,
                        Collectors.counting()
                ));
        result.put("typeCount", typeCount);
        
        // 统计被依赖数量
        long totalDependents = dependencyRepository.countByDependencyId(templateId);
        result.put("totalDependents", totalDependents);
        
        return result;
    }

    @Override
    public Map<String, Object> analyzeDependencyImpact(Long templateId) {
        log.debug("Analyzing dependency impact for template: {}", templateId);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> impacts = new ArrayList<>();
        
        // 获取所有依赖
        List<AlertRuleTemplateDependency> dependencies = dependencyRepository.findByTemplateId(templateId);
        
        for (AlertRuleTemplateDependency dependency : dependencies) {
            Map<String, Object> impact = new HashMap<>();
            impact.put("dependencyId", dependency.getDependencyId());
            impact.put("dependencyType", dependency.getDependencyType());
            
            // 分析依赖影响
            switch (dependency.getDependencyType().toUpperCase()) {
                case "REQUIRED":
                    impact.put("impactLevel", "HIGH");
                    impact.put("description", "必需依赖，变更可能影响模板功能");
                    break;
                case "OPTIONAL":
                    impact.put("impactLevel", "MEDIUM");
                    impact.put("description", "可选依赖，变更影响较小");
                    break;
                case "REFERENCE":
                    impact.put("impactLevel", "LOW");
                    impact.put("description", "参考依赖，变更影响最小");
                    break;
                default:
                    impact.put("impactLevel", "UNKNOWN");
                    impact.put("description", "未知依赖类型");
            }
            
            impacts.add(impact);
        }
        
        result.put("impacts", impacts);
        return result;
    }

    @Override
    public Map<String, Object> getDependencySuggestions(Long templateId) {
        log.debug("Getting dependency suggestions for template: {}", templateId);
        
        Map<String, Object> result = new HashMap<>();
        List<String> suggestions = new ArrayList<>();
        
        // 获取依赖统计
        Map<String, Object> statistics = getDependencyStatistics(templateId);
        long totalDependencies = (Long) statistics.get("totalDependencies");
        
        // 基于依赖数量提供建议
        if (totalDependencies == 0) {
            suggestions.add("建议添加必要的依赖关系");
        } else if (totalDependencies > 5) {
            suggestions.add("依赖数量较多，建议检查是否可以简化依赖关系");
        }
        
        // 检查是否存在循环依赖
        if (hasCircularDependency(templateId)) {
            suggestions.add("检测到循环依赖，建议重构依赖关系");
        }
        
        result.put("suggestions", suggestions);
        return result;
    }

    private AlertRuleTemplateDependency getDependencyEntity(Long dependencyId) {
        return dependencyRepository.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependency not found: " + dependencyId));
    }

    private Map<String, Object> convertDependencyToMap(AlertRuleTemplateDependency dependency) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", dependency.getId());
        result.put("templateId", dependency.getTemplateId());
        result.put("dependencyId", dependency.getDependencyId());
        result.put("dependencyType", dependency.getDependencyType());
        result.put("description", dependency.getDescription());
        result.put("createdBy", dependency.getCreatedBy());
        result.put("createdAt", dependency.getCreatedAt());
        result.put("updatedAt", dependency.getUpdatedAt());
        return result;
    }

    private boolean wouldCreateCircularDependency(Long templateId, Long dependencyId) {
        if (templateId.equals(dependencyId)) {
            return true;
        }
        
        Set<Long> visited = new HashSet<>();
        Queue<Long> queue = new LinkedList<>();
        queue.add(dependencyId);
        
        while (!queue.isEmpty()) {
            Long currentId = queue.poll();
            if (currentId.equals(templateId)) {
                return true;
            }
            
            if (visited.contains(currentId)) {
                continue;
            }
            visited.add(currentId);
            
            List<AlertRuleTemplateDependency> dependencies = dependencyRepository.findByTemplateId(currentId);
            for (AlertRuleTemplateDependency dependency : dependencies) {
                queue.add(dependency.getDependencyId());
            }
        }
        
        return false;
    }
} 