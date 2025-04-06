package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertHistory;
import java.util.List;

public interface AlertService {
    void initialize();
    AlertRule createRule(AlertRule rule);
    void updateRule(AlertRule rule);
    void deleteRule(Long id);
    List<AlertRule> getEnabledRules();
    void evaluateRule(AlertRule rule);
    List<AlertHistory> getAlertHistory(Long ruleId);
    List<AlertHistory> getAggregatedAlerts(String[] groupBy, int timeWindow);
    void resolveAlert(Long alertId);
} 