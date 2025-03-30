package com.watchalert.service.impl;

import com.watchalert.model.AlertHistory;
import com.watchalert.repository.AlertHistoryRepository;
import com.watchalert.service.AlertHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertHistoryServiceImpl implements AlertHistoryService {

    private final AlertHistoryRepository alertHistoryRepository;

    @Override
    @Transactional
    public AlertHistory createAlertHistory(AlertHistory alertHistory) {
        alertHistory.setCreatedAt(LocalDateTime.now());
        alertHistory.setUpdatedAt(LocalDateTime.now());
        return alertHistoryRepository.save(alertHistory);
    }

    @Override
    @Transactional
    public AlertHistory updateAlertHistory(AlertHistory alertHistory) {
        alertHistory.setUpdatedAt(LocalDateTime.now());
        return alertHistoryRepository.save(alertHistory);
    }

    @Override
    public AlertHistory getAlertHistory(String id) {
        return alertHistoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<AlertHistory> getAlertHistoryByRuleId(String alertRuleId) {
        return alertHistoryRepository.findByAlertRuleIdOrderByTriggeredAtDesc(alertRuleId);
    }

    @Override
    public List<AlertHistory> getAlertHistoryByStatus(String status) {
        return alertHistoryRepository.findByStatusOrderByTriggeredAtDesc(status);
    }

    @Override
    public List<AlertHistory> getUnsentNotifications() {
        return alertHistoryRepository.findByNotificationSentFalse();
    }

    @Override
    @Transactional
    public void markNotificationAsSent(String id) {
        AlertHistory alertHistory = getAlertHistory(id);
        if (alertHistory != null) {
            alertHistory.setNotificationSent(true);
            updateAlertHistory(alertHistory);
        }
    }

    @Override
    @Transactional
    public void acknowledgeAlert(String id, String acknowledgedBy) {
        AlertHistory alertHistory = getAlertHistory(id);
        if (alertHistory != null) {
            alertHistory.setStatus("ACKNOWLEDGED");
            alertHistory.setAcknowledgedAt(LocalDateTime.now());
            alertHistory.setAcknowledgedBy(acknowledgedBy);
            updateAlertHistory(alertHistory);
        }
    }

    @Override
    @Transactional
    public void resolveAlert(String id) {
        AlertHistory alertHistory = getAlertHistory(id);
        if (alertHistory != null) {
            alertHistory.setStatus("CLOSED");
            alertHistory.setResolvedAt(LocalDateTime.now());
            updateAlertHistory(alertHistory);
        }
    }
} 