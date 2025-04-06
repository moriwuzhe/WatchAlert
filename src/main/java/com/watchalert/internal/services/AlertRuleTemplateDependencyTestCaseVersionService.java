package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

/**
 * 告警规则模板依赖测试用例版本服务接口
 */
public interface AlertRuleTemplateDependencyTestCaseVersionService {

    /**
     * 创建测试用例版本
     *
     * @param testCaseId 测试用例ID
     * @param versionNumber 版本号
     * @param versionType 版本类型
     * @param testData 测试数据
     * @param expectedResult 预期结果
     * @param testType 测试类型
     * @param priority 优先级
     * @param changeDescription 变更描述
     * @param createdBy 创建人
     * @return 版本ID
     */
    Long createVersion(Long testCaseId, String versionNumber, String versionType, String testData, 
            String expectedResult, String testType, Integer priority, String changeDescription, String createdBy);

    /**
     * 获取测试用例版本历史
     *
     * @param testCaseId 测试用例ID
     * @return 版本历史列表
     */
    List<Map<String, Object>> getVersionHistory(Long testCaseId);

    /**
     * 获取特定版本
     *
     * @param versionId 版本ID
     * @return 版本详情
     */
    Map<String, Object> getVersion(Long versionId);

    /**
     * 获取特定版本号
     *
     * @param testCaseId 测试用例ID
     * @param versionNumber 版本号
     * @return 版本详情
     */
    Map<String, Object> getVersionByNumber(Long testCaseId, String versionNumber);

    /**
     * 比较两个版本
     *
     * @param versionId1 版本1 ID
     * @param versionId2 版本2 ID
     * @return 比较结果
     */
    Map<String, Object> compareVersions(Long versionId1, Long versionId2);

    /**
     * 回滚到特定版本
     *
     * @param testCaseId 测试用例ID
     * @param versionId 版本ID
     * @param createdBy 创建人
     * @return 新版本ID
     */
    Long rollbackToVersion(Long testCaseId, Long versionId, String createdBy);

    /**
     * 获取版本统计信息
     *
     * @param testCaseId 测试用例ID
     * @return 统计信息
     */
    Map<String, Object> getVersionStatistics(Long testCaseId);

    /**
     * 生成版本变更报告
     *
     * @param testCaseId 测试用例ID
     * @return 变更报告
     */
    Map<String, Object> generateVersionChangeReport(Long testCaseId);

    /**
     * 验证版本数据
     *
     * @param versionData 版本数据
     * @return 验证结果
     */
    Map<String, Object> validateVersion(Map<String, Object> versionData);
} 