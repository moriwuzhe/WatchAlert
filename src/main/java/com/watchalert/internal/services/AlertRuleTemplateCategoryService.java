package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTemplateCategory;
import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateCategoryService {
    
    /**
     * 创建模板分类
     *
     * @param category 分类信息
     * @return 创建的分类
     */
    AlertRuleTemplateCategory createCategory(AlertRuleTemplateCategory category);
    
    /**
     * 更新模板分类
     *
     * @param category 分类信息
     * @return 更新后的分类
     */
    AlertRuleTemplateCategory updateCategory(AlertRuleTemplateCategory category);
    
    /**
     * 删除模板分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);
    
    /**
     * 获取分类详情
     *
     * @param id 分类ID
     * @return 分类信息
     */
    AlertRuleTemplateCategory getCategory(Long id);
    
    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    List<AlertRuleTemplateCategory> getAllCategories();
    
    /**
     * 获取子分类
     *
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<AlertRuleTemplateCategory> getChildCategories(Long parentId);
    
    /**
     * 移动分类
     *
     * @param id 分类ID
     * @param newParentId 新的父级ID
     * @param newSort 新的排序号
     * @return 更新后的分类
     */
    AlertRuleTemplateCategory moveCategory(Long id, Long newParentId, int newSort);
    
    /**
     * 获取分类树
     *
     * @return 分类树结构
     */
    List<Map<String, Object>> getCategoryTree();
    
    /**
     * 验证分类名称是否可用
     *
     * @param name 分类名称
     * @param excludeId 排除的分类ID
     * @return 是否可用
     */
    boolean isNameAvailable(String name, Long excludeId);
    
    /**
     * 获取分类路径
     *
     * @param id 分类ID
     * @return 分类路径列表
     */
    List<AlertRuleTemplateCategory> getCategoryPath(Long id);
} 