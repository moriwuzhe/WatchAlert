package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateDependencyTestCaseService {
    
    /**
     * 创建测试用例
     */
    Long createTestCase(Long dependencyId, String name, String description, String testData, 
            String expectedResult, String testType, int priority, String createdBy);
    
    /**
     * 更新测试用例
     */
    void updateTestCase(Long testCaseId, String name, String description, String testData, 
            String expectedResult, String testType, int priority);
    
    /**
     * 删除测试用例
     */
    void deleteTestCase(Long testCaseId);
    
    /**
     * 获取测试用例详情
     */
    Map<String, Object> getTestCase(Long testCaseId);
    
    /**
     * 获取依赖的所有测试用例
     */
    List<Map<String, Object>> getDependencyTestCases(Long dependencyId);
    
    /**
     * 执行测试用例
     */
    Map<String, Object> executeTestCase(Long testCaseId);
    
    /**
     * 执行依赖的所有测试用例
     */
    Map<String, Object> executeDependencyTestCases(Long dependencyId);
    
    /**
     * 获取测试用例统计信息
     */
    Map<String, Object> getTestCaseStatistics(Long dependencyId);
    
    /**
     * 导入测试用例
     */
    Map<String, Object> importTestCases(Long dependencyId, List<Map<String, Object>> testCases, String createdBy);
    
    /**
     * 导出测试用例
     */
    List<Map<String, Object>> exportTestCases(Long dependencyId);
    
    /**
     * 验证测试用例数据
     */
    Map<String, Object> validateTestCase(Map<String, Object> testCase);
} 