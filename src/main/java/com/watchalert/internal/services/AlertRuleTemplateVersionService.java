package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTemplateVersion;
import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateVersionService {
    
    /**
     * 创建新版本
     *
     * @param templateId 模板ID
     * @param versionType 版本类型
     * @param changeLog 变更日志
     * @param createdBy 创建人
     * @return 创建的版本
     */
    AlertRuleTemplateVersion createVersion(Long templateId, String versionType, String changeLog, String createdBy);
    
    /**
     * 获取版本详情
     *
     * @param id 版本ID
     * @return 版本信息
     */
    AlertRuleTemplateVersion getVersion(Long id);
    
    /**
     * 获取模板的所有版本
     *
     * @param templateId 模板ID
     * @return 版本列表
     */
    List<AlertRuleTemplateVersion> getVersionsByTemplate(Long templateId);
    
    /**
     * 获取模板的最新版本
     *
     * @param templateId 模板ID
     * @return 最新版本
     */
    AlertRuleTemplateVersion getLatestVersion(Long templateId);
    
    /**
     * 回滚到指定版本
     *
     * @param id 版本ID
     * @param createdBy 创建人
     * @return 回滚后的新版本
     */
    AlertRuleTemplateVersion rollbackToVersion(Long id, String createdBy);
    
    /**
     * 比较两个版本的差异
     *
     * @param version1Id 版本1 ID
     * @param version2Id 版本2 ID
     * @return 差异信息
     */
    Map<String, Object> compareVersions(Long version1Id, Long version2Id);
    
    /**
     * 获取版本历史
     *
     * @param templateId 模板ID
     * @return 版本历史
     */
    List<Map<String, Object>> getVersionHistory(Long templateId);
    
    /**
     * 验证版本号是否可用
     *
     * @param templateId 模板ID
     * @param version 版本号
     * @param excludeId 排除的版本ID
     * @return 是否可用
     */
    boolean isVersionAvailable(Long templateId, String version, Long excludeId);
    
    /**
     * 生成下一个版本号
     *
     * @param templateId 模板ID
     * @param versionType 版本类型
     * @return 下一个版本号
     */
    String generateNextVersion(Long templateId, String versionType);
    
    /**
     * 获取版本变更统计
     *
     * @param templateId 模板ID
     * @return 统计信息
     */
    Map<String, Object> getVersionStatistics(Long templateId);
} 