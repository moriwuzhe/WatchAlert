package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTest;
import java.util.List;
import java.util.Map;

public interface AlertRuleTestService {
    AlertRuleTest createTest(AlertRuleTest test);
    void updateTest(AlertRuleTest test);
    void deleteTest(Long id);
    AlertRuleTest getTest(Long id);
    List<AlertRuleTest> getTestsByRule(Long ruleId);
    AlertRuleTest executeTest(Long testId);
    List<AlertRuleTest> executeAllTests(Long ruleId);
    Map<String, Object> getTestSummary(Long ruleId);
} 