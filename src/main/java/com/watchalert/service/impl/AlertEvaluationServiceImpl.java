package com.watchalert.service.impl;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import com.watchalert.repository.AlertRepository;
import com.watchalert.service.AlertEvaluationService;
import com.watchalert.service.MonitoringService;
import com.watchalert.service.NotificationService;
import com.watchalert.service.AlertRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertEvaluationServiceImpl implements AlertEvaluationService {

    private final AlertRepository alertRepository;
    private final MonitoringService monitoringService;
    private final NotificationService notificationService;
    private final AlertRuleService alertRuleService;

    @Override
    @Scheduled(fixedDelayString = "${alert.check-interval:300000}") // 默认5分钟
    public void evaluateAllEnabledRules() {
        log.info("开始评估所有启用的告警规则...");
        List<AlertRule> rules = alertRuleService.getEnabledAlertRules();
        rules.forEach(this::evaluateAlertRule);
    }

    @Override
    public void evaluateAlertRule(AlertRule rule) {
        try {
            Double currentValue = monitoringService.getMetricValue(rule.getSource(), rule.getTarget(), rule.getMetric());
            if (shouldTriggerAlert(rule, currentValue)) {
                Alert alert = createAlert(rule, currentValue);
                alertRepository.save(alert);
                notificationService.sendNotification(alert);
            }
        } catch (Exception e) {
            log.error("评估告警规则失败: {}", rule.getId(), e);
        }
    }

    @Override
    public List<Alert> getActiveAlerts() {
        return alertRepository.findByStatus("ACTIVE");
    }

    @Override
    @Transactional
    public void acknowledgeAlert(String alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new IllegalArgumentException("告警不存在: " + alertId));
        alert.setStatus("ACKNOWLEDGED");
        alert.setUpdatedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }

    @Override
    @Transactional
    public void resolveAlert(String alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new IllegalArgumentException("告警不存在: " + alertId));
        alert.setStatus("RESOLVED");
        alert.setUpdatedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }

    private boolean shouldTriggerAlert(AlertRule rule, Double currentValue) {
        if (currentValue == null) {
            return false;
        }

        boolean thresholdExceeded = false;
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