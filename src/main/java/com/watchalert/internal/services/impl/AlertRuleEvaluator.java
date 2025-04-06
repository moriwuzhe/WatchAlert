package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class AlertRuleEvaluator {

    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private ConditionEvaluator conditionEvaluator;

    @Autowired
    private NotificationService notificationService;

    public AlertHistory evaluate(AlertRule rule) {
        try {
            // 执行查询
            Map<String, Object> result = queryExecutor.executeQuery(rule);
            
            // 检查条件
            boolean isTriggered = conditionEvaluator.evaluateCondition(result, rule.getCondition());
            
            if (isTriggered) {
                // 创建告警历史
                AlertHistory history = new AlertHistory();
                history.setRule(rule);
                history.setStatus("ACTIVE");
                history.setSeverity(rule.getSeverity());
                history.setMessage("告警规则触发: " + rule.getName());
                history.setCreatedAt(LocalDateTime.now());
                
                // 发送通知
                try {
                    notificationService.sendNotification(rule, history);
                    history.setNotifyStatus("SENT");
                } catch (Exception e) {
                    log.error("Failed to send notification for rule: {}", rule.getName(), e);
                    history.setNotifyStatus("FAILED");
                    history.setNotifyMessage(e.getMessage());
                }
                
                return history;
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to evaluate rule: {}", rule.getName(), e);
            throw new RuntimeException("Failed to evaluate rule", e);
        }
    }
} 