package com.watchalert.service;

import com.watchalert.model.AlertHistory;
import java.util.List;

public interface AlertHistoryService {
    AlertHistory createAlertHistory(AlertHistory alertHistory);
    AlertHistory updateAlertHistory(AlertHistory alertHistory);
    AlertHistory getAlertHistory(String id);
    List<AlertHistory> getAlertHistoryByRuleId(String alertRuleId);
    List<AlertHistory> getAlertHistoryByStatus(String status);
    List<AlertHistory> getUnsentNotifications();
    void markNotificationAsSent(String id);
    void acknowledgeAlert(String id, String acknowledgedBy);
    void resolveAlert(String id);
} 