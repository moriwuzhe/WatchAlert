package com.watchalert.internal.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateDependencyHistoryService {
    
    /**
     * 记录依赖变更
     */
    Long recordChange(Long dependencyId, String changeType, String oldValue, String newValue, 
            String changeReason, String changedBy);
    
    /**
     * 获取依赖变更历史
     */
    List<Map<String, Object>> getDependencyHistory(Long dependencyId);
    
    /**
     * 获取指定类型的变更历史
     */
    List<Map<String, Object>> getDependencyHistoryByType(Long dependencyId, String changeType);
    
    /**
     * 获取指定时间范围的变更历史
     */
    List<Map<String, Object>> getDependencyHistoryByTimeRange(Long dependencyId, 
            LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取指定用户的变更历史
     */
    List<Map<String, Object>> getDependencyHistoryByUser(String changedBy);
    
    /**
     * 获取变更统计信息
     */
    Map<String, Object> getDependencyHistoryStatistics(Long dependencyId);
    
    /**
     * 回滚到指定版本
     */
    Map<String, Object> rollbackToVersion(Long dependencyId, Long versionId, String changedBy);
    
    /**
     * 比较两个版本的差异
     */
    Map<String, Object> compareVersions(Long dependencyId, Long version1Id, Long version2Id);
    
    /**
     * 导出变更历史
     */
    List<Map<String, Object>> exportHistory(Long dependencyId);
    
    /**
     * 清理历史记录
     */
    void cleanupHistory(Long dependencyId, LocalDateTime beforeTime);
} 