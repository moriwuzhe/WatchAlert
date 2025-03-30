package com.watchalert.validator;

import com.watchalert.model.AlertRule;
import org.springframework.stereotype.Component;

@Component
public class AlertRuleValidator {

    public void validate(AlertRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("告警规则不能为空");
        }

        if (rule.getName() == null || rule.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("告警规则名称不能为空");
        }

        if (rule.getMetricName() == null || rule.getMetricName().trim().isEmpty()) {
            throw new IllegalArgumentException("指标名称不能为空");
        }

        if (rule.getThreshold() == null) {
            throw new IllegalArgumentException("阈值不能为空");
        }

        if (rule.getOperator() == null) {
            throw new IllegalArgumentException("操作符不能为空");
        }

        if (rule.getDuration() == null || rule.getDuration() <= 0) {
            throw new IllegalArgumentException("持续时间必须大于0");
        }

        if (rule.getSeverity() == null) {
            throw new IllegalArgumentException("严重程度不能为空");
        }

        if (rule.getNotificationChannels() == null || rule.getNotificationChannels().isEmpty()) {
            throw new IllegalArgumentException("通知渠道不能为空");
        }

        // 验证阈值和操作符的组合
        validateThresholdAndOperator(rule.getThreshold(), rule.getOperator());
    }

    private void validateThresholdAndOperator(Double threshold, String operator) {
        switch (operator) {
            case ">":
            case ">=":
            case "<":
            case "<=":
            case "==":
                break;
            default:
                throw new IllegalArgumentException("不支持的操作符: " + operator);
        }
    }
} 