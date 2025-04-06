package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateTestCase;
import com.watchalert.internal.repositories.AlertRuleTemplateTestCaseRepository;
import com.watchalert.internal.services.AlertRuleTemplateTestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateTestCaseServiceImpl implements AlertRuleTemplateTestCaseService {

    @Autowired
    private AlertRuleTemplateTestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public Long createTestCase(Long templateId, String name, String description, String testData, 
            String expectedResult, String testType, int priority, String createdBy) {
        log.debug("Creating test case for template: {}", templateId);
        
        AlertRuleTemplateTestCase testCase = new AlertRuleTemplateTestCase();
        testCase.setTemplateId(templateId);
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
        
        AlertRuleTemplateTestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Test case not found: " + testCaseId));
        
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
        
        AlertRuleTemplateTestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Test case not found: " + testCaseId));
        
        return convertTestCaseToMap(testCase);
    }

    @Override
    public List<Map<String, Object>> getTestCases(Long templateId) {
        log.debug("Getting test cases for template: {}", templateId);
        
        return testCaseRepository.findByTemplateId(templateId).stream()
                .map(this::convertTestCaseToMap)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> executeTestCase(Long testCaseId) {
        log.debug("Executing test case: {}", testCaseId);
        
        AlertRuleTemplateTestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Test case not found: " + testCaseId));
        
        Map<String, Object> result = new HashMap<>();
        try {
            // TODO: 实现测试用例执行逻辑
            boolean passed = true; // 临时占位
            String actualResult = "Test passed"; // 临时占位
            
            testCase.setStatus(passed ? "PASSED" : "FAILED");
            testCase.setLastRunAt(LocalDateTime.now());
            testCase.setLastRunResult(actualResult);
            
            testCaseRepository.save(testCase);
            
            result.put("passed", passed);
            result.put("actualResult", actualResult);
        } catch (Exception e) {
            log.error("Error executing test case: {}", testCaseId, e);
            testCase.setStatus("FAILED");
            testCase.setLastRunAt(LocalDateTime.now());
            testCase.setLastRunResult("Error: " + e.getMessage());
            testCaseRepository.save(testCase);
            
            result.put("passed", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> executeTestCases(Long templateId) {
        log.debug("Executing test cases for template: {}", templateId);
        
        List<AlertRuleTemplateTestCase> testCases = testCaseRepository.findByTemplateId(templateId);
        Map<String, Object> result = new HashMap<>();
        
        int total = testCases.size();
        int passed = 0;
        int failed = 0;
        List<Map<String, Object>> details = new ArrayList<>();
        
        for (AlertRuleTemplateTestCase testCase : testCases) {
            Map<String, Object> testResult = executeTestCase(testCase.getId());
            details.add(testResult);
            
            if ((Boolean) testResult.get("passed")) {
                passed++;
            } else {
                failed++;
            }
        }
        
        result.put("total", total);
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("details", details);
        
        return result;
    }

    @Override
    public Map<String, Object> getTestCaseStatistics(Long templateId) {
        log.debug("Getting test case statistics for template: {}", templateId);
        
        Map<String, Object> result = new HashMap<>();
        
        long total = testCaseRepository.countByTemplateId(templateId);
        long passed = testCaseRepository.countByTemplateIdAndStatus(templateId, "PASSED");
        long failed = testCaseRepository.countByTemplateIdAndStatus(templateId, "FAILED");
        long pending = testCaseRepository.countByTemplateIdAndStatus(templateId, "PENDING");
        
        result.put("total", total);
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("pending", pending);
        
        // 按测试类型统计
        List<AlertRuleTemplateTestCase> testCases = testCaseRepository.findByTemplateId(templateId);
        Map<String, Long> typeCount = testCases.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateTestCase::getTestType,
                        Collectors.counting()
                ));
        result.put("typeCount", typeCount);
        
        // 按优先级统计
        Map<Integer, Long> priorityCount = testCases.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateTestCase::getPriority,
                        Collectors.counting()
                ));
        result.put("priorityCount", priorityCount);
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> importTestCases(Long templateId, List<Map<String, Object>> testCases, String createdBy) {
        log.debug("Importing test cases for template: {}", templateId);
        
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
                            templateId,
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
    public List<Map<String, Object>> exportTestCases(Long templateId) {
        log.debug("Exporting test cases for template: {}", templateId);
        
        return testCaseRepository.findByTemplateId(templateId).stream()
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
            errors.add("Name is required");
        }
        if (!testCase.containsKey("testData") || testCase.get("testData") == null) {
            errors.add("Test data is required");
        }
        if (!testCase.containsKey("expectedResult") || testCase.get("expectedResult") == null) {
            errors.add("Expected result is required");
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
            if (!Arrays.asList("POSITIVE", "NEGATIVE", "EDGE_CASE").contains(testType)) {
                errors.add("Invalid test type: " + testType);
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        
        return result;
    }

    private Map<String, Object> convertTestCaseToMap(AlertRuleTemplateTestCase testCase) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", testCase.getId());
        result.put("templateId", testCase.getTemplateId());
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
} 