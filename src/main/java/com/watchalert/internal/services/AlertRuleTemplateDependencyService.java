package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

/**
 * 告警规则模板依赖关系服务
 */
public interface AlertRuleTemplateDependencyService {
    
    /**
     * 创建依赖关系
     */
    Long createDependency(Long templateId, Long dependencyId, String dependencyType, String description, String createdBy);
    
    /**
     * 更新依赖关系
     */
    void updateDependency(Long dependencyId, String dependencyType, String description);
    
    /**
     * 删除依赖关系
     */
    void deleteDependency(Long dependencyId);
    
    /**
     * 获取依赖详情
     */
    Map<String, Object> getDependency(Long dependencyId);
    
    /**
     * 获取模板的所有依赖
     */
    List<Map<String, Object>> getTemplateDependencies(Long templateId);
    
    /**
     * 获取依赖该模板的所有模板
     */
    List<Map<String, Object>> getDependentTemplates(Long templateId);
    
    /**
     * 检查是否存在循环依赖
     */
    boolean hasCircularDependency(Long templateId);
    
    /**
     * 获取依赖图数据
     */
    Map<String, Object> getDependencyGraphData(Long templateId);
    
    /**
     * 获取依赖统计
     */
    Map<String, Object> getDependencyStatistics(Long templateId);
    
    /**
     * 分析依赖影响
     */
    Map<String, Object> analyzeDependencyImpact(Long templateId);
    
    /**
     * 获取依赖建议
     */
    Map<String, Object> getDependencySuggestions(Long templateId);
} 