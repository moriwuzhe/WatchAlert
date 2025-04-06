package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateVersionCompareService {
    
    /**
     * 比较两个版本的差异
     *
     * @param version1Id 版本1 ID
     * @param version2Id 版本2 ID
     * @return 比较结果
     */
    Map<String, Object> compareVersions(Long version1Id, Long version2Id);
    
    /**
     * 获取两个版本之间的具体差异
     *
     * @param version1Id 版本1 ID
     * @param version2Id 版本2 ID
     * @return 差异列表
     */
    List<Map<String, Object>> getVersionDifferences(Long version1Id, Long version2Id);
    
    /**
     * 获取模板的版本历史
     *
     * @param templateId 模板ID
     * @return 版本历史列表
     */
    List<Map<String, Object>> getVersionHistory(Long templateId);
    
    /**
     * 获取版本变更统计信息
     *
     * @param templateId 模板ID
     * @return 统计信息
     */
    Map<String, Object> getVersionChangeStatistics(Long templateId);
    
    /**
     * 获取版本变更图表数据
     *
     * @param templateId 模板ID
     * @return 图表数据
     */
    Map<String, Object> getVersionChangeChartData(Long templateId);
    
    /**
     * 获取版本变更详情
     *
     * @param versionId 版本ID
     * @return 变更详情
     */
    Map<String, Object> getVersionChangeDetails(Long versionId);
    
    /**
     * 获取版本变更影响分析
     *
     * @param version1Id 版本1 ID
     * @param version2Id 版本2 ID
     * @return 影响分析结果
     */
    Map<String, Object> getVersionChangeImpactAnalysis(Long version1Id, Long version2Id);
    
    /**
     * 获取版本变更建议
     *
     * @param version1Id 版本1 ID
     * @param version2Id 版本2 ID
     * @return 变更建议
     */
    Map<String, Object> getVersionChangeSuggestions(Long version1Id, Long version2Id);
} 