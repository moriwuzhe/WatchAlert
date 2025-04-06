package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleVersion;
import java.util.List;
import java.util.Map;

public interface AlertRuleVersionService {
    /**
     * 创建告警规则版本
     *
     * @param ruleId 告警规则ID
     * @param comment 版本说明
     * @param createdBy 创建人
     * @return 创建的版本
     */
    AlertRuleVersion createVersion(Long ruleId, String comment, String createdBy);

    /**
     * 获取告警规则的所有版本
     *
     * @param ruleId 告警规则ID
     * @return 版本列表
     */
    List<AlertRuleVersion> getVersions(Long ruleId);

    /**
     * 获取告警规则的指定版本
     *
     * @param ruleId 告警规则ID
     * @param version 版本号
     * @return 版本信息
     */
    AlertRuleVersion getVersion(Long ruleId, String version);

    /**
     * 获取告警规则的最新版本
     *
     * @param ruleId 告警规则ID
     * @return 最新版本信息
     */
    AlertRuleVersion getLatestVersion(Long ruleId);

    /**
     * 回滚到指定版本
     *
     * @param ruleId 告警规则ID
     * @param version 版本号
     * @param createdBy 操作人
     * @return 回滚后的版本
     */
    AlertRuleVersion rollbackToVersion(Long ruleId, String version, String createdBy);

    /**
     * 比较两个版本的差异
     *
     * @param ruleId 告警规则ID
     * @param version1 版本1
     * @param version2 版本2
     * @return 差异信息
     */
    Map<String, Object> compareVersions(Long ruleId, String version1, String version2);
} 