package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.services.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AlertRuleScheduler {

    @Autowired
    private AlertService alertService;

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void scheduleEvaluation() {
        log.info("Starting scheduled alert rule evaluation");
        List<AlertRule> enabledRules = alertService.getEnabledRules();
        
        for (AlertRule rule : enabledRules) {
            alertService.evaluateRule(rule);
        }
        
        log.info("Completed scheduled alert rule evaluation");
    }
} 