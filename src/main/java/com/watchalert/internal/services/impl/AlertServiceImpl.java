package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.models.Datasource;
import com.watchalert.internal.repo.AlertRuleRepository;
import com.watchalert.internal.repo.AlertHistoryRepository;
import com.watchalert.internal.services.AlertService;
import com.watchalert.internal.services.DatasourceService;
import com.watchalert.internal.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private AlertRuleRepository ruleRepository;

    @Autowired
    private AlertHistoryRepository historyRepository;

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private ConditionEvaluator conditionEvaluator;

    @Autowired
    private AlertAggregator alertAggregator;

    @Override
    public void initialize() {
        log.info("Initializing AlertService...");
        // 可以在这里添加初始化逻辑，比如加载默认规则等
    }

    @Override
    @Transactional
    public AlertRule createRule(AlertRule rule) {
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        return ruleRepository.save(rule);
    }

    @Override
    @Transactional
    public void updateRule(AlertRule rule) {
        AlertRule existingRule = ruleRepository.findById(rule.getId())
            .orElseThrow(() -> new RuntimeException("Alert rule not found: " + rule.getId()));
        
        rule.setCreatedAt(existingRule.getCreatedAt());
        rule.setUpdatedAt(LocalDateTime.now());
        ruleRepository.save(rule);
    }

    @Override
    @Transactional
    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }

    @Override
    public List<AlertRule> getEnabledRules() {
        return ruleRepository.findByEnabled(true);
    }

    @Override
    @Transactional
    public void evaluateRule(AlertRule rule) {
        log.debug("Evaluating rule: {}", rule.getName());
        
        try {
            // 执行查询
            Map<String, Object> result = queryExecutor.executeQuery(rule);
            
            // 评估条件
            boolean triggered = conditionEvaluator.evaluateCondition(result, rule.getCondition());
            
            if (triggered) {
                // 创建告警历史
                AlertHistory history = new AlertHistory();
                history.setRule(rule);
                history.setSeverity(rule.getSeverity());
                history.setStatus("ACTIVE");
                history.setSource(rule.getDatasourceType());
                history.setTriggerTime(LocalDateTime.now());
                history.setMessage(String.format("Rule '%s' triggered", rule.getName()));
                
                // 保存告警历史
                historyRepository.save(history);
                
                // 发送通知
                notificationService.sendNotification(rule, history);
                
                log.info("Rule '{}' triggered and notification sent", rule.getName());
            } else {
                // 检查是否有未恢复的告警
                List<AlertHistory> activeAlerts = historyRepository.findByRuleIdAndStatus(rule.getId(), "ACTIVE");
                for (AlertHistory alert : activeAlerts) {
                    if (!alert.getRecoveryNotified()) {
                        // 发送恢复通知
                        alert.setStatus("RESOLVED");
                        alert.setResolvedAt(LocalDateTime.now());
                        alert.setRecoveryNotified(true);
                        alert.setResolution("Auto-resolved: Condition no longer met");
                        historyRepository.save(alert);
                        
                        notificationService.sendNotification(rule, alert);
                        log.info("Alert {} auto-resolved and recovery notification sent", alert.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to evaluate rule: {}", rule.getName(), e);
            throw new RuntimeException("Failed to evaluate rule", e);
        }
    }

    @Override
    public List<AlertHistory> getAggregatedAlerts(String[] groupBy, int timeWindow) {
        log.debug("Getting aggregated alerts with groupBy: {}, timeWindow: {} minutes", 
                groupBy, timeWindow);

        // 获取时间窗口内的告警
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMinutes(timeWindow);
        List<AlertHistory> alerts = historyRepository.findByTriggerTimeBetween(startTime, endTime);

        // 聚合告警
        return alertAggregator.aggregateAlerts(alerts, groupBy, timeWindow);
    }

    @Override
    @Transactional
    public void resolveAlert(Long alertId) {
        log.debug("Resolving alert: {}", alertId);
        
        AlertHistory history = historyRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + alertId));
        
        history.setStatus("RESOLVED");
        history.setResolvedAt(LocalDateTime.now());
        history.setResolvedBy("manual");
        history.setResolution("Manually resolved");
        historyRepository.save(history);
        
        // 发送恢复通知
        if (!history.getRecoveryNotified()) {
            history.setRecoveryNotified(true);
            historyRepository.save(history);
            notificationService.sendNotification(history.getRule(), history);
        }
        
        log.info("Alert {} resolved and recovery notification sent", alertId);
    }

    @Override
    public List<AlertHistory> getAlertHistory(Long ruleId) {
        return historyRepository.findByRuleId(ruleId);
    }

    @Scheduled(fixedDelayString = "${alert.evaluation.interval:60000}")
    public void evaluateAllRules() {
        log.debug("Starting scheduled rule evaluation");
        List<AlertRule> enabledRules = getEnabledRules();
        
        for (AlertRule rule : enabledRules) {
            try {
                evaluateRule(rule);
            } catch (Exception e) {
                log.error("Failed to evaluate rule: {}", rule.getName(), e);
            }
        }
    }
} 