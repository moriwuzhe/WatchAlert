package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCase;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyTestCaseRepository;
import com.watchalert.internal.services.AlertRuleTemplateDependencyTestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateDependencyTestCaseServiceImpl implements AlertRuleTemplateDependencyTestCaseService {

    @Autowired
    private AlertRuleTemplateDependencyTestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public Long createTestCase(Long dependencyId, String name, String description, String testData, 
            String expectedResult, String testType, int priority, String createdBy) {
        log.debug("Creating test case for dependency: {}, name: {}", dependencyId, name);
        
        AlertRuleTemplateDependencyTestCase testCase = new AlertRuleTemplateDependencyTestCase();
        testCase.setDependencyId(dependencyId);
        testCase.setName(name);
        testCase.setDescription(description);
        testCase.setTestData(testData);
        testCase.setExpectedResult(expectedResult);
        testCase.setTestType(testType);
        testCase.setPriority(priority);
        testCase.setStatus("PENDING");
        testCase.setCreatedBy(createdBy);
        
        return testCaseRepository.save(testCase).getId();
    }

    @Override
    @Transactional
    public void updateTestCase(Long testCaseId, String name, String description, String testData, 
            String expectedResult, String testType, int priority) {
        log.debug("Updating test case: {}", testCaseId);
        
        AlertRuleTemplateDependencyTestCase testCase = getTestCaseEntity(testCaseId);
        testCase.setName(name);
        testCase.setDescription(description);
        testCase.setTestData(testData);
        testCase.setExpectedResult(expectedResult);
        testCase.setTestType(testType);
        testCase.setPriority(priority);
        
        testCaseRepository.save(testCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(Long testCaseId) {
        log.debug("Deleting test case: {}", testCaseId);
        testCaseRepository.deleteById(testCaseId);
    }

    @Override
    public Map<String, Object> getTestCase(Long testCaseId) {
        log.debug("Getting test case: {}", testCaseId);
        return convertTestCaseToMap(getTestCaseEntity(testCaseId));
    }

    @Override
    public List<Map<String, Object>> getDependencyTestCases(Long dependencyId) {
        log.debug("Getting test cases for dependency: {}", dependencyId);
        
        return testCaseRepository.findByDependencyId(dependencyId).stream()
                .map(this::convertTestCaseToMap)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> executeTestCase(Long testCaseId) {
        log.debug("Executing test case: {}", testCaseId);
        
        AlertRuleTemplateDependencyTestCase testCase = getTestCaseEntity(testCaseId);
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 根据测试类型执行不同的测试逻辑
            boolean testPassed = false;
            String testResult = "";
            
            switch (testCase.getTestType().toUpperCase()) {
                case "UNIT":
                    testPassed = executeUnitTest(testCase);
                    break;
                case "INTEGRATION":
                    testPassed = executeIntegrationTest(testCase);
                    break;
                case "FUNCTIONAL":
                    testPassed = executeFunctionalTest(testCase);
                    break;
                case "PERFORMANCE":
                    testPassed = executePerformanceTest(testCase);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported test type: " + testCase.getTestType());
            }
            
            // 更新测试用例状态
            testCase.setLastRunAt(LocalDateTime.now());
            testCase.setLastRunResult(testPassed ? "SUCCESS" : "FAILED");
            testCase.setStatus(testPassed ? "PASSED" : "FAILED");
            
            result.put("success", true);
            result.put("testPassed", testPassed);
            result.put("message", "Test case executed successfully");
            result.put("executionTime", System.currentTimeMillis() - testCase.getLastRunAt().toInstant(java.time.ZoneOffset.UTC).toEpochMilli());
        } catch (Exception e) {
            log.error("Error executing test case: {}", testCaseId, e);
            testCase.setLastRunAt(LocalDateTime.now());
            testCase.setLastRunResult("FAILED");
            testCase.setStatus("FAILED");
            
            result.put("success", false);
            result.put("testPassed", false);
            result.put("message", "Test case execution failed: " + e.getMessage());
            result.put("error", e.getMessage());
        }
        
        testCaseRepository.save(testCase);
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> executeDependencyTestCases(Long dependencyId) {
        log.debug("Executing test cases for dependency: {}", dependencyId);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> testResults = new ArrayList<>();
        
        List<AlertRuleTemplateDependencyTestCase> testCases = testCaseRepository
                .findByDependencyId(dependencyId);
        
        // 按优先级排序测试用例
        testCases.sort(Comparator.comparingInt(AlertRuleTemplateDependencyTestCase::getPriority));
        
        // 使用并行流执行测试用例
        testResults = executeTestCasesInParallel(testCases);
        
        result.put("total", testCases.size());
        result.put("results", testResults);
        
        // 生成测试报告
        Map<String, Object> report = generateTestReport(testCases, testResults);
        result.put("report", report);
        
        return result;
    }

    @Override
    public Map<String, Object> getTestCaseStatistics(Long dependencyId) {
        log.debug("Getting test case statistics for dependency: {}", dependencyId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 统计测试用例数量
        long totalTestCases = testCaseRepository.countByDependencyId(dependencyId);
        result.put("totalTestCases", totalTestCases);
        
        // 按状态统计
        long passedCount = testCaseRepository.countByDependencyIdAndStatus(dependencyId, "PASSED");
        long failedCount = testCaseRepository.countByDependencyIdAndStatus(dependencyId, "FAILED");
        long pendingCount = testCaseRepository.countByDependencyIdAndStatus(dependencyId, "PENDING");
        
        result.put("passedCount", passedCount);
        result.put("failedCount", failedCount);
        result.put("pendingCount", pendingCount);
        
        // 按测试类型统计
        List<AlertRuleTemplateDependencyTestCase> testCases = testCaseRepository
                .findByDependencyId(dependencyId);
        Map<String, Long> typeCount = testCases.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyTestCase::getTestType,
                        Collectors.counting()
                ));
        result.put("typeCount", typeCount);
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> importTestCases(Long dependencyId, List<Map<String, Object>> testCases, 
            String createdBy) {
        log.debug("Importing test cases for dependency: {}", dependencyId);
        
        Map<String, Object> result = new HashMap<>();
        int total = testCases.size();
        int success = 0;
        int failed = 0;
        List<String> errors = new ArrayList<>();
        
        for (Map<String, Object> testCase : testCases) {
            try {
                Map<String, Object> validationResult = validateTestCase(testCase);
                if ((Boolean) validationResult.get("valid")) {
                    createTestCase(
                            dependencyId,
                            (String) testCase.get("name"),
                            (String) testCase.get("description"),
                            (String) testCase.get("testData"),
                            (String) testCase.get("expectedResult"),
                            (String) testCase.get("testType"),
                            ((Number) testCase.get("priority")).intValue(),
                            createdBy
                    );
                    success++;
                } else {
                    failed++;
                    errors.add((String) validationResult.get("message"));
                }
            } catch (Exception e) {
                failed++;
                errors.add("Error importing test case: " + e.getMessage());
            }
        }
        
        result.put("total", total);
        result.put("success", success);
        result.put("failed", failed);
        result.put("errors", errors);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> exportTestCases(Long dependencyId) {
        log.debug("Exporting test cases for dependency: {}", dependencyId);
        
        return testCaseRepository.findByDependencyId(dependencyId).stream()
                .map(this::convertTestCaseToMap)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> validateTestCase(Map<String, Object> testCase) {
        log.debug("Validating test case data");
        
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (!testCase.containsKey("name") || testCase.get("name") == null) {
            errors.add("Test case name is required");
        }
        if (!testCase.containsKey("testType") || testCase.get("testType") == null) {
            errors.add("Test type is required");
        }
        if (!testCase.containsKey("priority") || testCase.get("priority") == null) {
            errors.add("Priority is required");
        }
        
        // 验证字段类型
        if (testCase.containsKey("priority") && testCase.get("priority") != null) {
            try {
                ((Number) testCase.get("priority")).intValue();
            } catch (Exception e) {
                errors.add("Priority must be a number");
            }
        }
        
        // 验证测试类型
        if (testCase.containsKey("testType") && testCase.get("testType") != null) {
            String testType = (String) testCase.get("testType");
            if (!Arrays.asList("UNIT", "INTEGRATION", "FUNCTIONAL", "PERFORMANCE").contains(testType.toUpperCase())) {
                errors.add("Invalid test type: " + testType);
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        
        return result;
    }

    private AlertRuleTemplateDependencyTestCase getTestCaseEntity(Long testCaseId) {
        return testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Test case not found: " + testCaseId));
    }

    private Map<String, Object> convertTestCaseToMap(AlertRuleTemplateDependencyTestCase testCase) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", testCase.getId());
        result.put("dependencyId", testCase.getDependencyId());
        result.put("name", testCase.getName());
        result.put("description", testCase.getDescription());
        result.put("testData", testCase.getTestData());
        result.put("expectedResult", testCase.getExpectedResult());
        result.put("testType", testCase.getTestType());
        result.put("priority", testCase.getPriority());
        result.put("status", testCase.getStatus());
        result.put("lastRunAt", testCase.getLastRunAt());
        result.put("lastRunResult", testCase.getLastRunResult());
        result.put("createdBy", testCase.getCreatedBy());
        result.put("createdAt", testCase.getCreatedAt());
        result.put("updatedAt", testCase.getUpdatedAt());
        return result;
    }

    /**
     * 执行单元测试
     */
    private boolean executeUnitTest(AlertRuleTemplateDependencyTestCase testCase) {
        log.debug("Executing unit test: {}", testCase.getName());
        
        // 解析测试数据
        Map<String, Object> testData = parseTestData(testCase.getTestData());
        
        // 解析预期结果
        Map<String, Object> expectedResult = parseTestData(testCase.getExpectedResult());
        
        // 执行测试逻辑
        try {
            // 这里应该调用实际的测试执行引擎
            // 目前仅作为示例，实际实现需要根据具体的测试框架和需求
            boolean result = validateTestResult(testData, expectedResult);
            
            log.debug("Unit test result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error in unit test execution", e);
            return false;
        }
    }
    
    /**
     * 执行集成测试
     */
    private boolean executeIntegrationTest(AlertRuleTemplateDependencyTestCase testCase) {
        log.debug("Executing integration test: {}", testCase.getName());
        
        // 解析测试数据
        Map<String, Object> testData = parseTestData(testCase.getTestData());
        
        // 解析预期结果
        Map<String, Object> expectedResult = parseTestData(testCase.getExpectedResult());
        
        // 执行测试逻辑
        try {
            // 这里应该调用实际的集成测试执行引擎
            // 目前仅作为示例，实际实现需要根据具体的测试框架和需求
            boolean result = validateTestResult(testData, expectedResult);
            
            log.debug("Integration test result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error in integration test execution", e);
            return false;
        }
    }
    
    /**
     * 执行功能测试
     */
    private boolean executeFunctionalTest(AlertRuleTemplateDependencyTestCase testCase) {
        log.debug("Executing functional test: {}", testCase.getName());
        
        // 解析测试数据
        Map<String, Object> testData = parseTestData(testCase.getTestData());
        
        // 解析预期结果
        Map<String, Object> expectedResult = parseTestData(testCase.getExpectedResult());
        
        // 执行测试逻辑
        try {
            // 这里应该调用实际的功能测试执行引擎
            // 目前仅作为示例，实际实现需要根据具体的测试框架和需求
            boolean result = validateTestResult(testData, expectedResult);
            
            log.debug("Functional test result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error in functional test execution", e);
            return false;
        }
    }
    
    /**
     * 执行性能测试
     */
    private boolean executePerformanceTest(AlertRuleTemplateDependencyTestCase testCase) {
        log.debug("Executing performance test: {}", testCase.getName());
        
        // 解析测试数据
        Map<String, Object> testData = parseTestData(testCase.getTestData());
        
        // 解析预期结果
        Map<String, Object> expectedResult = parseTestData(testCase.getExpectedResult());
        
        // 执行测试逻辑
        try {
            // 这里应该调用实际的性能测试执行引擎
            // 目前仅作为示例，实际实现需要根据具体的测试框架和需求
            boolean result = validateTestResult(testData, expectedResult);
            
            log.debug("Performance test result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error in performance test execution", e);
            return false;
        }
    }
    
    /**
     * 解析测试数据
     */
    private Map<String, Object> parseTestData(String testData) {
        try {
            if (testData == null || testData.trim().isEmpty()) {
                return new HashMap<>();
            }
            
            // 尝试解析为JSON
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(testData, Map.class);
        } catch (Exception e) {
            log.warn("Failed to parse test data as JSON, treating as plain text", e);
            Map<String, Object> result = new HashMap<>();
            result.put("rawData", testData);
            return result;
        }
    }
    
    /**
     * 验证测试结果
     */
    private boolean validateTestResult(Map<String, Object> actualResult, Map<String, Object> expectedResult) {
        // 简单的结果验证逻辑
        // 实际实现可能需要更复杂的比较逻辑
        if (expectedResult == null || expectedResult.isEmpty()) {
            return true; // 如果没有预期结果，则认为测试通过
        }
        
        // 检查关键字段是否匹配
        for (Map.Entry<String, Object> entry : expectedResult.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            
            if (!actualResult.containsKey(key)) {
                log.debug("Missing key in actual result: {}", key);
                return false;
            }
            
            Object actualValue = actualResult.get(key);
            if (!Objects.equals(expectedValue, actualValue)) {
                log.debug("Value mismatch for key {}: expected {}, actual {}", 
                        key, expectedValue, actualValue);
                return false;
            }
        }
        
        return true;
    }

    /**
     * 生成测试报告
     */
    private Map<String, Object> generateTestReport(List<AlertRuleTemplateDependencyTestCase> testCases, 
            List<Map<String, Object>> testResults) {
        log.debug("Generating test report for {} test cases", testCases.size());
        
        Map<String, Object> report = new HashMap<>();
        
        // 基本信息
        report.put("generatedAt", LocalDateTime.now());
        report.put("totalTestCases", testCases.size());
        
        // 测试结果统计
        int passedCount = 0;
        int failedCount = 0;
        int errorCount = 0;
        long totalExecutionTime = 0;
        
        for (Map<String, Object> result : testResults) {
            boolean success = (Boolean) result.get("success");
            boolean testPassed = (Boolean) result.get("testPassed");
            
            if (success && testPassed) {
                passedCount++;
            } else if (success && !testPassed) {
                failedCount++;
            } else {
                errorCount++;
            }
            
            if (result.containsKey("executionTime")) {
                totalExecutionTime += ((Number) result.get("executionTime")).longValue();
            }
        }
        
        report.put("passedCount", passedCount);
        report.put("failedCount", failedCount);
        report.put("errorCount", errorCount);
        report.put("totalExecutionTime", totalExecutionTime);
        report.put("averageExecutionTime", testCases.isEmpty() ? 0 : totalExecutionTime / testCases.size());
        
        // 按测试类型统计
        Map<String, Map<String, Integer>> typeStatistics = new HashMap<>();
        for (int i = 0; i < testCases.size(); i++) {
            AlertRuleTemplateDependencyTestCase testCase = testCases.get(i);
            Map<String, Object> result = testResults.get(i);
            
            String testType = testCase.getTestType().toUpperCase();
            typeStatistics.putIfAbsent(testType, new HashMap<>());
            
            Map<String, Integer> typeCount = typeStatistics.get(testType);
            if ((Boolean) result.get("success") && (Boolean) result.get("testPassed")) {
                typeCount.put("passed", typeCount.getOrDefault("passed", 0) + 1);
            } else if ((Boolean) result.get("success") && !(Boolean) result.get("testPassed")) {
                typeCount.put("failed", typeCount.getOrDefault("failed", 0) + 1);
            } else {
                typeCount.put("error", typeCount.getOrDefault("error", 0) + 1);
            }
        }
        report.put("typeStatistics", typeStatistics);
        
        // 按优先级统计
        Map<Integer, Map<String, Integer>> priorityStatistics = new HashMap<>();
        for (int i = 0; i < testCases.size(); i++) {
            AlertRuleTemplateDependencyTestCase testCase = testCases.get(i);
            Map<String, Object> result = testResults.get(i);
            
            int priority = testCase.getPriority();
            priorityStatistics.putIfAbsent(priority, new HashMap<>());
            
            Map<String, Integer> priorityCount = priorityStatistics.get(priority);
            if ((Boolean) result.get("success") && (Boolean) result.get("testPassed")) {
                priorityCount.put("passed", priorityCount.getOrDefault("passed", 0) + 1);
            } else if ((Boolean) result.get("success") && !(Boolean) result.get("testPassed")) {
                priorityCount.put("failed", priorityCount.getOrDefault("failed", 0) + 1);
            } else {
                priorityCount.put("error", priorityCount.getOrDefault("error", 0) + 1);
            }
        }
        report.put("priorityStatistics", priorityStatistics);
        
        // 失败详情
        List<Map<String, Object>> failureDetails = new ArrayList<>();
        for (int i = 0; i < testCases.size(); i++) {
            AlertRuleTemplateDependencyTestCase testCase = testCases.get(i);
            Map<String, Object> result = testResults.get(i);
            
            if (!(Boolean) result.get("success") || !(Boolean) result.get("testPassed")) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("testCaseId", testCase.getId());
                detail.put("name", testCase.getName());
                detail.put("testType", testCase.getTestType());
                detail.put("priority", testCase.getPriority());
                detail.put("error", result.get("error"));
                detail.put("message", result.get("message"));
                failureDetails.add(detail);
            }
        }
        report.put("failureDetails", failureDetails);
        
        // 测试建议
        List<String> suggestions = generateTestSuggestions(testCases, testResults);
        report.put("suggestions", suggestions);
        
        return report;
    }
    
    /**
     * 生成测试建议
     */
    private List<String> generateTestSuggestions(List<AlertRuleTemplateDependencyTestCase> testCases, 
            List<Map<String, Object>> testResults) {
        List<String> suggestions = new ArrayList<>();
        
        // 检查是否有失败的测试用例
        boolean hasFailures = testResults.stream()
                .anyMatch(result -> !(Boolean) result.get("success") || !(Boolean) result.get("testPassed"));
        
        if (hasFailures) {
            suggestions.add("有测试用例失败，建议优先修复失败的测试用例");
        }
        
        // 检查测试类型覆盖
        Map<String, Long> typeCount = testCases.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyTestCase::getTestType,
                        Collectors.counting()
                ));
        
        if (!typeCount.containsKey("UNIT") || typeCount.get("UNIT") < 2) {
            suggestions.add("建议增加单元测试用例，确保基本功能测试覆盖");
        }
        
        if (!typeCount.containsKey("INTEGRATION") || typeCount.get("INTEGRATION") < 1) {
            suggestions.add("建议添加集成测试用例，确保组件间交互正常");
        }
        
        // 检查优先级分布
        Map<Integer, Long> priorityCount = testCases.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyTestCase::getPriority,
                        Collectors.counting()
                ));
        
        if (!priorityCount.containsKey(1) || priorityCount.get(1) < 1) {
            suggestions.add("建议添加高优先级测试用例，确保关键功能测试覆盖");
        }
        
        // 检查测试数据完整性
        long incompleteDataCount = testCases.stream()
                .filter(testCase -> testCase.getTestData() == null || testCase.getTestData().trim().isEmpty())
                .count();
        
        if (incompleteDataCount > 0) {
            suggestions.add("有" + incompleteDataCount + "个测试用例缺少测试数据，建议补充完整");
        }
        
        return suggestions;
    }

    /**
     * 并行执行测试用例
     */
    private List<Map<String, Object>> executeTestCasesInParallel(List<AlertRuleTemplateDependencyTestCase> testCases) {
        log.debug("Executing {} test cases in parallel", testCases.size());
        
        // 创建线程安全的列表存储结果
        List<Map<String, Object>> results = Collections.synchronizedList(new ArrayList<>());
        
        // 使用并行流处理测试用例
        testCases.parallelStream().forEach(testCase -> {
            try {
                Map<String, Object> result = executeTestCase(testCase.getId());
                results.add(result);
            } catch (Exception e) {
                log.error("Error executing test case in parallel: {}", testCase.getId(), e);
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("testCaseId", testCase.getId());
                errorResult.put("success", false);
                errorResult.put("testPassed", false);
                errorResult.put("message", "Error executing test case: " + e.getMessage());
                errorResult.put("error", e.getMessage());
                results.add(errorResult);
            }
        });
        
        // 按原始顺序排序结果
        return results.stream()
                .sorted(Comparator.comparing(result -> {
                    Long testCaseId = (Long) result.get("testCaseId");
                    return testCases.indexOf(testCases.stream()
                            .filter(tc -> tc.getId().equals(testCaseId))
                            .findFirst()
                            .orElse(null));
                }))
                .collect(Collectors.toList());
    }
    
    /**
     * 按优先级分组执行测试用例
     */
    private List<Map<String, Object>> executeTestCasesByPriority(List<AlertRuleTemplateDependencyTestCase> testCases) {
        log.debug("Executing test cases by priority");
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        // 按优先级分组
        Map<Integer, List<AlertRuleTemplateDependencyTestCase>> priorityGroups = testCases.stream()
                .collect(Collectors.groupingBy(AlertRuleTemplateDependencyTestCase::getPriority));
        
        // 按优先级顺序执行
        priorityGroups.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    log.debug("Executing test cases with priority: {}", entry.getKey());
                    List<Map<String, Object>> priorityResults = executeTestCasesInParallel(entry.getValue());
                    results.addAll(priorityResults);
                });
        
        return results;
    }
    
    /**
     * 按测试类型分组执行测试用例
     */
    private List<Map<String, Object>> executeTestCasesByType(List<AlertRuleTemplateDependencyTestCase> testCases) {
        log.debug("Executing test cases by type");
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        // 按测试类型分组
        Map<String, List<AlertRuleTemplateDependencyTestCase>> typeGroups = testCases.stream()
                .collect(Collectors.groupingBy(AlertRuleTemplateDependencyTestCase::getTestType));
        
        // 按测试类型顺序执行
        // 单元测试 -> 集成测试 -> 功能测试 -> 性能测试
        List<String> typeOrder = Arrays.asList("UNIT", "INTEGRATION", "FUNCTIONAL", "PERFORMANCE");
        
        typeOrder.forEach(type -> {
            if (typeGroups.containsKey(type)) {
                log.debug("Executing test cases of type: {}", type);
                List<Map<String, Object>> typeResults = executeTestCasesInParallel(typeGroups.get(type));
                results.addAll(typeResults);
            }
        });
        
        return results;
    }
} 