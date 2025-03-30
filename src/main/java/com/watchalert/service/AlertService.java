package com.watchalert.service;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import java.util.List;

public interface AlertService {
    Alert createAlert(Alert alert);
    Alert updateAlert(Alert alert);
    void deleteAlert(String id);
    Alert getAlert(String id);
    List<Alert> getAllAlerts();
    List<Alert> getAlertsByRule(AlertRule rule);
    List<Alert> getActiveAlerts();
    void acknowledgeAlert(String id, String username);
    void resolveAlert(String id);
    void checkAlerts();
    void sendAlert(Alert alert);
    void createAlertRule(AlertRule rule);
    void updateAlertRule(AlertRule rule);
    void deleteAlertRule(String ruleId);
    List<AlertRule> getAllAlertRules();
} 