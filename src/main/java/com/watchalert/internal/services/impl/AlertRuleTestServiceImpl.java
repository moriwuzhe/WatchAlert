package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTest;
import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.repositories.AlertRuleTestRepository;
import com.watchalert.internal.repositories.AlertRuleRepository;
import com.watchalert.internal.services.AlertRuleTestService;
import com.watchalert.internal.services.QueryExecutor;
import com.watchalert.internal.services.ConditionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class AlertRuleTestServiceImpl implements AlertRuleTestService {

    @Autowired
    private AlertRuleTestRepository testRepository;

    @Autowired
    private AlertRuleRepository ruleRepository;

    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private ConditionEvaluator conditionEvaluator;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public AlertRuleTest createTest(AlertRuleTest test) {
        log.debug("Creating alert rule test for rule: {}", test.getRule().getId());
        return testRepository.save(test);
    }

    @Override
    @Transactional
    public void updateTest(AlertRuleTest test) {
        log.debug("Updating alert rule test: {}", test.getId());
        AlertRuleTest existingTest = testRepository.findById(test.getId())
                .orElseThrow(() -> new RuntimeException("Test not found: " + test.getId()));
        
        existingTest.setTestData(test.getTestData());
        existingTest.setExpectedResult(test.isExpectedResult());
        
        testRepository.save(existingTest);
    }

    @Override
    @Transactional
    public void deleteTest(Long id) {
        log.debug("Deleting alert rule test: {}", id);
        testRepository.deleteById(id);
    }

    @Override
    public AlertRuleTest getTest(Long id) {
        log.debug("Getting alert rule test: {}", id);
        return testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found: " + id));
    }

    @Override
    public List<AlertRuleTest> getTestsByRule(Long ruleId) {
        log.debug("Getting alert rule tests for rule: {}", ruleId);
        return testRepository.findByRuleIdOrderByExecutedAtDesc(ruleId);
    }

    @Override
    @Transactional
    public AlertRuleTest executeTest(Long testId) {
        log.debug("Executing alert rule test: {}", testId);
        
        AlertRuleTest test = getTest(testId);
        AlertRule rule = test.getRule();
        
        try {
            // 解析测试数据
            Map<String, Object> testData = objectMapper.readValue(test.getTestData(), Map.class);
            
            // 执行查询
            Map<String, Object> queryResult = queryExecutor.executeQuery(rule);
            
            // 合并测试数据和查询结果
            Map<String, Object> mergedData = new HashMap<>(queryResult);
            mergedData.putAll(testData);
            
            // 评估条件
            boolean actualResult = conditionEvaluator.evaluateCondition(mergedData, rule.getCondition());
            
            // 更新测试结果
            test.setActualResult(String.valueOf(actualResult));
            test.setExecutedAt(LocalDateTime.now());
            
            if (actualResult != test.isExpectedResult()) {
                test.setErrorMessage("Test failed: Expected " + test.isExpectedResult() + 
                        " but got " + actualResult);
            }
            
            return testRepository.save(test);
        } catch (Exception e) {
            log.error("Failed to execute test: {}", testId, e);
            test.setErrorMessage("Test execution failed: " + e.getMessage());
            test.setExecutedAt(LocalDateTime.now());
            return testRepository.save(test);
        }
    }

    @Override
    @Transactional
    public List<AlertRuleTest> executeAllTests(Long ruleId) {
        log.debug("Executing all tests for rule: {}", ruleId);
        
        List<AlertRuleTest> tests = getTestsByRule(ruleId);
        for (AlertRuleTest test : tests) {
            executeTest(test.getId());
        }
        
        return tests;
    }

    @Override
    public Map<String, Object> getTestSummary(Long ruleId) {
        log.debug("Getting test summary for rule: {}", ruleId);
        
        List<AlertRuleTest> tests = getTestsByRule(ruleId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalTests", tests.size());
        summary.put("passedTests", tests.stream()
                .filter(t -> t.getActualResult() != null && 
                        Boolean.parseBoolean(t.getActualResult()) == t.isExpectedResult())
                .count());
        summary.put("failedTests", tests.stream()
                .filter(t -> t.getActualResult() != null && 
                        Boolean.parseBoolean(t.getActualResult()) != t.isExpectedResult())
                .count());
        summary.put("errorTests", tests.stream()
                .filter(t -> t.getErrorMessage() != null)
                .count());
        
        return summary;
    }
} 