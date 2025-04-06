package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateDependencyVersionService {
    
    /**
     * 创建依赖版本
     */
    Long createVersion(Long dependencyId, String versionNumber, String versionType, 
            String versionConstraint, String description, String createdBy);
    
    /**
     * 更新依赖版本
     */
    void updateVersion(Long versionId, String versionNumber, String versionType, 
            String versionConstraint, String description);
    
    /**
     * 删除依赖版本
     */
    void deleteVersion(Long versionId);
    
    /**
     * 获取依赖版本详情
     */
    Map<String, Object> getVersion(Long versionId);
    
    /**
     * 获取依赖的所有版本
     */
    List<Map<String, Object>> getDependencyVersions(Long dependencyId);
    
    /**
     * 检查版本兼容性
     */
    boolean isVersionCompatible(Long dependencyId, String versionNumber);
    
    /**
     * 获取版本升级建议
     */
    Map<String, Object> getVersionUpgradeSuggestions(Long dependencyId);
    
    /**
     * 获取版本变更历史
     */
    List<Map<String, Object>> getVersionHistory(Long dependencyId);
    
    /**
     * 获取版本统计信息
     */
    Map<String, Object> getVersionStatistics(Long dependencyId);
    
    /**
     * 导入依赖版本
     */
    Map<String, Object> importVersions(Long dependencyId, List<Map<String, Object>> versions, String createdBy);
    
    /**
     * 导出依赖版本
     */
    List<Map<String, Object>> exportVersions(Long dependencyId);
    
    /**
     * 验证版本数据
     */
    Map<String, Object> validateVersion(Map<String, Object> version);
} 