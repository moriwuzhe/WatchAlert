package com.watchalert.service.impl;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import com.watchalert.repository.AlertRepository;
import com.watchalert.repository.AlertRuleRepository;
import com.watchalert.service.AlertService;
import com.watchalert.service.NotificationService;
import com.watchalert.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    
    private final NotificationService notificationService;
    private final MonitoringService monitoringService;
    private final AlertRepository alertRepository;
    private final AlertRuleRepository alertRuleRepository;

    @Override
    @Scheduled(fixedDelayString = "${alert.check-interval:300000}")
    public void checkAlerts() {
        log.info("开始检查告警规则...");
        List<AlertRule> rules = alertRuleRepository.findByEnabledTrue();
        
        for (AlertRule rule : rules) {
            try {
                Double currentValue = monitoringService.getMetricValue(rule.getSource(), rule.getTarget(), rule.getMetric());
                if (shouldTriggerAlert(rule, currentValue)) {
                    Alert alert = createAlert(rule, currentValue);
                    alertRepository.save(alert);
                    sendAlert(alert);
                }
            } catch (Exception e) {
                log.error("检查告警规则失败: {}", rule.getId(), e);
            }
        }
    }

    @Override
    public void sendAlert(Alert alert) {
        notificationService.sendNotification(alert);
    }

    @Override
    public List<Alert> getActiveAlerts() {
        return alertRepository.findByStatus("ACTIVE");
    }

    @Override
    public void createAlertRule(AlertRule rule) {
        rule.setId(UUID.randomUUID().toString());
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        alertRuleRepository.save(rule);
    }

    @Override
    public void updateAlertRule(AlertRule rule) {
        rule.setUpdatedAt(LocalDateTime.now());
        alertRuleRepository.save(rule);
    }

    @Override
    public void deleteAlertRule(String ruleId) {
        alertRuleRepository.deleteById(ruleId);
    }

    @Override
    public List<AlertRule> getAllAlertRules() {
        return alertRuleRepository.findAll();
    }

    @Override
    public Alert createAlert(Alert alert) {
        alert.setId(UUID.randomUUID().toString());
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    @Override
    public Alert updateAlert(Alert alert) {
        alert.setUpdatedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    @Override
    public void deleteAlert(String id) {
        alertRepository.deleteById(id);
    }

    @Override
    public Alert getAlert(String id) {
        return alertRepository.findById(id).orElse(null);
    }

    @Override
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    @Override
    public List<Alert> getAlertsByRule(AlertRule rule) {
        return alertRepository.findByAlertRule(rule);
    }

    @Override
    public void acknowledgeAlert(String id, String username) {
        Alert alert = getAlert(id);
        if (alert != null) {
            alert.setStatus("ACKNOWLEDGED");
            alert.setAcknowledgedAt(LocalDateTime.now());
            alert.setAcknowledgedBy(username);
            alert.setUpdatedAt(LocalDateTime.now());
            alertRepository.save(alert);
        }
    }

    @Override
    public void resolveAlert(String id) {
        Alert alert = getAlert(id);
        if (alert != null) {
            alert.setStatus("RESOLVED");
            alert.setResolvedAt(LocalDateTime.now());
            alert.setUpdatedAt(LocalDateTime.now());
            alertRepository.save(alert);
        }
    }

    private boolean shouldTriggerAlert(AlertRule rule, Double currentValue) {
        if (currentValue == null) {
            return false;
        }

        boolean thresholdExceeded;
        switch (rule.getOperator()) {
            case ">":
                thresholdExceeded = currentValue > rule.getThreshold();
                break;
            case "<":
                thresholdExceeded = currentValue < rule.getThreshold();
                break;
            case ">=":
                thresholdExceeded = currentValue >= rule.getThreshold();
                break;
            case "<=":
                thresholdExceeded = currentValue <= rule.getThreshold();
                break;
            case "==":
                thresholdExceeded = Math.abs(currentValue - rule.getThreshold()) < 0.0001;
                break;
            default:
                thresholdExceeded = false;
                break;
        }

        return thresholdExceeded;
    }

    private Alert createAlert(AlertRule rule, Double currentValue) {
        Alert alert = new Alert();
        alert.setId(UUID.randomUUID().toString());
        alert.setName(rule.getName());
        alert.setDescription(rule.getDescription());
        alert.setSeverity(rule.getSeverity());
        alert.setSource(rule.getSource());
        alert.setStatus("ACTIVE");
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        alert.setAlertRule(rule);
        alert.setRuleId(rule.getId());
        alert.setTarget(rule.getTarget());
        alert.setMetric(rule.getMetric());
        alert.setValue(currentValue);
        return alert;
    }
} 