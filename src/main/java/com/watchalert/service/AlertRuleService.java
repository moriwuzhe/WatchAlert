package com.watchalert.service;

import com.watchalert.model.AlertRule;
import java.util.List;

public interface AlertRuleService {
    AlertRule createAlertRule(AlertRule rule);
    AlertRule updateAlertRule(AlertRule rule);
    void deleteAlertRule(String id);
    AlertRule getAlertRule(String id);
    List<AlertRule> getAllAlertRules();
    List<AlertRule> getEnabledAlertRules();
} 