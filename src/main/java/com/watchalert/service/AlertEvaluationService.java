package com.watchalert.service;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import java.util.List;

public interface AlertEvaluationService {
    void evaluateAlertRule(AlertRule rule);
    void evaluateAllEnabledRules();
    List<Alert> getActiveAlerts();
    void acknowledgeAlert(String alertId);
    void resolveAlert(String alertId);
} 