package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AlertAggregator {

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    /**
     * 聚合告警
     *
     * @param alerts 告警列表
     * @param groupBy 分组字段
     * @param timeWindow 时间窗口（分钟）
     * @return 聚合后的告警
     */
    public List<AlertHistory> aggregateAlerts(List<AlertHistory> alerts, String[] groupBy, int timeWindow) {
        log.debug("Aggregating {} alerts with groupBy: {}, timeWindow: {} minutes", 
                alerts.size(), Arrays.toString(groupBy), timeWindow);

        // 按时间窗口和分组字段进行分组
        Map<String, List<AlertHistory>> groupedAlerts = alerts.stream()
                .collect(Collectors.groupingBy(alert -> buildGroupKey(alert, groupBy, timeWindow)));

        // 对每个分组进行聚合
        return groupedAlerts.values().stream()
                .map(this::aggregateGroup)
                .collect(Collectors.toList());
    }

    /**
     * 构建分组键
     */
    private String buildGroupKey(AlertHistory alert, String[] groupBy, int timeWindow) {
        // 计算时间窗口
        LocalDateTime windowStart = alert.getTriggerTime()
                .minusMinutes(timeWindow)
                .withSecond(0)
                .withNano(0);

        // 构建分组键
        StringBuilder key = new StringBuilder();
        key.append(windowStart.toString());

        for (String field : groupBy) {
            key.append("|").append(getFieldValue(alert, field));
        }

        return key.toString();
    }

    /**
     * 获取字段值
     */
    private String getFieldValue(AlertHistory alert, String field) {
        switch (field.toLowerCase()) {
            case "rule":
                return alert.getRule().getName();
            case "severity":
                return alert.getSeverity();
            case "status":
                return alert.getStatus();
            case "source":
                return alert.getSource();
            default:
                return "unknown";
        }
    }

    /**
     * 聚合告警组
     */
    private AlertHistory aggregateGroup(List<AlertHistory> alerts) {
        if (alerts.isEmpty()) {
            return null;
        }

        // 使用第一个告警作为基础
        AlertHistory baseAlert = alerts.get(0);
        AlertHistory aggregatedAlert = new AlertHistory();
        aggregatedAlert.setRule(baseAlert.getRule());
        aggregatedAlert.setSeverity(baseAlert.getSeverity());
        aggregatedAlert.setStatus(baseAlert.getStatus());
        aggregatedAlert.setSource(baseAlert.getSource());
        aggregatedAlert.setTriggerTime(baseAlert.getTriggerTime());

        // 设置聚合信息
        aggregatedAlert.setCount(alerts.size());
        aggregatedAlert.setFirstOccurrence(alerts.stream()
                .map(AlertHistory::getTriggerTime)
                .min(LocalDateTime::compareTo)
                .orElse(baseAlert.getTriggerTime()));
        aggregatedAlert.setLastOccurrence(alerts.stream()
                .map(AlertHistory::getTriggerTime)
                .max(LocalDateTime::compareTo)
                .orElse(baseAlert.getTriggerTime()));

        // 合并消息
        String message = String.format("Aggregated %d alerts from %s to %s",
                alerts.size(),
                aggregatedAlert.getFirstOccurrence(),
                aggregatedAlert.getLastOccurrence());
        aggregatedAlert.setMessage(message);

        return aggregatedAlert;
    }
} 