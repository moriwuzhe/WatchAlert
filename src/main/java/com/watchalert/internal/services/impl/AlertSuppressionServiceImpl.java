package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertSuppression;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.repositories.AlertSuppressionRepository;
import com.watchalert.internal.services.AlertSuppressionService;
import com.watchalert.internal.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AlertSuppressionServiceImpl implements AlertSuppressionService {

    @Autowired
    private AlertSuppressionRepository suppressionRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public AlertSuppression createSuppression(AlertSuppression suppression) {
        log.debug("Creating alert suppression: {}", suppression.getName());
        return suppressionRepository.save(suppression);
    }

    @Override
    @Transactional
    public void updateSuppression(AlertSuppression suppression) {
        log.debug("Updating alert suppression: {}", suppression.getId());
        AlertSuppression existingSuppression = suppressionRepository.findById(suppression.getId())
                .orElseThrow(() -> new RuntimeException("Suppression not found: " + suppression.getId()));
        
        existingSuppression.setName(suppression.getName());
        existingSuppression.setDescription(suppression.getDescription());
        existingSuppression.setCondition(suppression.getCondition());
        existingSuppression.setDurationMinutes(suppression.getDurationMinutes());
        existingSuppression.setEnabled(suppression.isEnabled());
        existingSuppression.setNotifyChannels(suppression.getNotifyChannels());
        existingSuppression.setNotifyUsers(suppression.getNotifyUsers());
        existingSuppression.setNotifyGroups(suppression.getNotifyGroups());
        existingSuppression.setNotifyTemplate(suppression.getNotifyTemplate());
        
        suppressionRepository.save(existingSuppression);
    }

    @Override
    @Transactional
    public void deleteSuppression(Long id) {
        log.debug("Deleting alert suppression: {}", id);
        suppressionRepository.deleteById(id);
    }

    @Override
    public AlertSuppression getSuppression(Long id) {
        log.debug("Getting alert suppression: {}", id);
        return suppressionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suppression not found: " + id));
    }

    @Override
    public List<AlertSuppression> getSuppressionsByRule(Long ruleId) {
        log.debug("Getting alert suppressions for rule: {}", ruleId);
        return suppressionRepository.findByRuleId(ruleId);
    }

    @Override
    public List<AlertSuppression> getEnabledSuppressions() {
        log.debug("Getting all enabled alert suppressions");
        return suppressionRepository.findByEnabled(true);
    }

    @Override
    public boolean shouldSuppressAlert(AlertHistory alert) {
        log.debug("Checking if alert should be suppressed: {}", alert.getId());
        
        List<AlertSuppression> suppressions = suppressionRepository.findByRuleIdAndEnabled(alert.getRule().getId(), true);
        
        for (AlertSuppression suppression : suppressions) {
            // 检查抑制条件
            if (evaluateSuppressionCondition(alert, suppression.getCondition())) {
                // 检查抑制时间
                LocalDateTime suppressionEndTime = alert.getTriggerTime().plusMinutes(suppression.getDurationMinutes());
                if (LocalDateTime.now().isBefore(suppressionEndTime)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    @Transactional
    public void checkAndApplySuppression(AlertHistory alert) {
        log.debug("Checking and applying suppression for alert: {}", alert.getId());
        
        if (shouldSuppressAlert(alert)) {
            // 更新告警状态
            alert.setStatus("SUPPRESSED");
            
            // 发送抑制通知
            if (alert.getNotifyChannels() != null && !alert.getNotifyChannels().isEmpty()) {
                notificationService.sendNotification(
                        alert.getNotifyChannels(),
                        alert.getNotifyUsers(),
                        alert.getNotifyGroups(),
                        "Alert has been suppressed",
                        alert
                );
            }
            
            log.info("Alert {} has been suppressed", alert.getId());
        }
    }

    private boolean evaluateSuppressionCondition(AlertHistory alert, String condition) {
        // TODO: 实现抑制条件评估逻辑
        // 这里需要根据具体的条件格式来实现评估逻辑
        return false;
    }
} 