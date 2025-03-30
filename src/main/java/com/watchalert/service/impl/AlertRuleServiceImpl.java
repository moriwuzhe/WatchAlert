package com.watchalert.service.impl;

import com.watchalert.model.AlertRule;
import com.watchalert.repository.AlertRuleRepository;
import com.watchalert.service.AlertRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertRuleServiceImpl implements AlertRuleService {

    private final AlertRuleRepository alertRuleRepository;

    @Override
    @Transactional
    public AlertRule createAlertRule(AlertRule rule) {
        rule.setId(UUID.randomUUID().toString());
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        return alertRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public AlertRule updateAlertRule(AlertRule rule) {
        AlertRule existingRule = getAlertRule(rule.getId());
        if (existingRule == null) {
            throw new IllegalArgumentException("告警规则不存在: " + rule.getId());
        }
        
        rule.setUpdatedAt(LocalDateTime.now());
        return alertRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public void deleteAlertRule(String id) {
        alertRuleRepository.deleteById(id);
    }

    @Override
    public AlertRule getAlertRule(String id) {
        return alertRuleRepository.findById(id).orElse(null);
    }

    @Override
    public List<AlertRule> getAllAlertRules() {
        return alertRuleRepository.findAll();
    }

    @Override
    public List<AlertRule> getEnabledAlertRules() {
        return alertRuleRepository.findByEnabledTrue();
    }
} 