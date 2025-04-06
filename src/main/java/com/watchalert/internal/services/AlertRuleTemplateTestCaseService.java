package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

/**
 * 告警规则模板测试用例服务
 */
public interface AlertRuleTemplateTestCaseService {
    
    /**
     * 创建测试用例
     *
     * @param templateId 模板ID
     * @param name 测试用例名称
     * @param description 测试用例描述
     * @param testData 测试数据
     * @param expectedResult 预期结果
     * @param testType 测试类型
     * @param priority 优先级
     * @param createdBy 创建人
     * @return 测试用例ID
     */
    Long createTestCase(Long templateId, String name, String description, String testData, 
            String expectedResult, String testType, int priority, String createdBy);
    
    /**
     * 更新测试用例
     *
     * @param testCaseId 测试用例ID
     * @param name 测试用例名称
     * @param description 测试用例描述
     * @param testData 测试数据
     * @param expectedResult 预期结果
     * @param testType 测试类型
     * @param priority 优先级
     */
    void updateTestCase(Long testCaseId, String name, String description, String testData, 
            String expectedResult, String testType, int priority);
    
    /**
     * 删除测试用例
     *
     * @param testCaseId 测试用例ID
     */
    void deleteTestCase(Long testCaseId);
    
    /**
     * 获取测试用例详情
     *
     * @param testCaseId 测试用例ID
     * @return 测试用例详情
     */
    Map<String, Object> getTestCase(Long testCaseId);
    
    /**
     * 获取模板的所有测试用例
     *
     * @param templateId 模板ID
     * @return 测试用例列表
     */
    List<Map<String, Object>> getTestCases(Long templateId);
    
    /**
     * 执行测试用例
     *
     * @param testCaseId 测试用例ID
     * @return 测试结果
     */
    Map<String, Object> executeTestCase(Long testCaseId);
    
    /**
     * 批量执行测试用例
     *
     * @param templateId 模板ID
     * @return 测试结果
     */
    Map<String, Object> executeTestCases(Long templateId);
    
    /**
     * 获取测试用例统计信息
     *
     * @param templateId 模板ID
     * @return 统计信息
     */
    Map<String, Object> getTestCaseStatistics(Long templateId);
    
    /**
     * 导入测试用例
     *
     * @param templateId 模板ID
     * @param testCases 测试用例数据
     * @param createdBy 创建人
     * @return 导入结果
     */
    Map<String, Object> importTestCases(Long templateId, List<Map<String, Object>> testCases, String createdBy);
    
    /**
     * 导出测试用例
     *
     * @param templateId 模板ID
     * @return 导出的测试用例数据
     */
    List<Map<String, Object>> exportTestCases(Long templateId);
    
    /**
     * 验证测试用例数据
     *
     * @param testCase 测试用例数据
     * @return 验证结果
     */
    Map<String, Object> validateTestCase(Map<String, Object> testCase);
} 