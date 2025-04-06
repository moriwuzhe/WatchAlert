package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertStatistics;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.repositories.AlertStatisticsRepository;
import com.watchalert.internal.repositories.AlertHistoryRepository;
import com.watchalert.internal.repositories.AlertRuleRepository;
import com.watchalert.internal.services.AlertStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertStatisticsServiceImpl implements AlertStatisticsService {

    @Autowired
    private AlertStatisticsRepository statisticsRepository;

    @Autowired
    private AlertHistoryRepository historyRepository;

    @Autowired
    private AlertRuleRepository ruleRepository;

    @Override
    @Transactional
    public AlertStatistics generateStatistics(Long ruleId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Generating statistics for rule: {} between {} and {}", ruleId, startTime, endTime);
        
        AlertRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found: " + ruleId));
        
        List<AlertHistory> alerts = historyRepository.findByRuleIdAndTriggerTimeBetween(ruleId, startTime, endTime);
        
        AlertStatistics statistics = new AlertStatistics();
        statistics.setRuleId(ruleId);
        statistics.setRuleName(rule.getName());
        statistics.setSeverity(rule.getSeverity());
        statistics.setStartTime(startTime);
        statistics.setEndTime(endTime);
        
        // 计算告警数量
        statistics.setTotalAlerts(alerts.size());
        statistics.setActiveAlerts((int) alerts.stream().filter(a -> "ACTIVE".equals(a.getStatus())).count());
        statistics.setResolvedAlerts((int) alerts.stream().filter(a -> "RESOLVED".equals(a.getStatus())).count());
        statistics.setEscalatedAlerts((int) alerts.stream().filter(a -> a.getEscalationLevel() != null && a.getEscalationLevel() > 0).count());
        
        // 计算平均解决时间
        List<AlertHistory> resolvedAlerts = alerts.stream()
                .filter(a -> "RESOLVED".equals(a.getStatus()) && a.getResolvedAt() != null)
                .collect(Collectors.toList());
        
        if (!resolvedAlerts.isEmpty()) {
            double avgResolutionTime = resolvedAlerts.stream()
                    .mapToLong(a -> Duration.between(a.getTriggerTime(), a.getResolvedAt()).toMinutes())
                    .average()
                    .orElse(0.0);
            statistics.setAverageResolutionTime(avgResolutionTime);
        }
        
        // 计算平均升级时间
        List<AlertHistory> escalatedAlerts = alerts.stream()
                .filter(a -> a.getEscalationLevel() != null && a.getEscalationLevel() > 0 && a.getLastEscalationTime() != null)
                .collect(Collectors.toList());
        
        if (!escalatedAlerts.isEmpty()) {
            double avgEscalationTime = escalatedAlerts.stream()
                    .mapToLong(a -> Duration.between(a.getTriggerTime(), a.getLastEscalationTime()).toMinutes())
                    .average()
                    .orElse(0.0);
            statistics.setAverageEscalationTime(avgEscalationTime);
        }
        
        // 计算通知统计
        statistics.setNotificationCount(alerts.stream().mapToInt(a -> "SUCCESS".equals(a.getNotifyStatus()) ? 1 : 0).sum());
        statistics.setNotificationSuccessCount(alerts.stream().mapToInt(a -> "SUCCESS".equals(a.getNotifyStatus()) ? 1 : 0).sum());
        statistics.setNotificationFailureCount(alerts.stream().mapToInt(a -> "FAILED".equals(a.getNotifyStatus()) ? 1 : 0).sum());
        
        return statisticsRepository.save(statistics);
    }

    @Override
    public List<AlertStatistics> getRuleStatistics(Long ruleId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting statistics for rule: {} between {} and {}", ruleId, startTime, endTime);
        return statisticsRepository.findRuleStatisticsInTimeRange(ruleId, startTime, endTime);
    }

    @Override
    public List<AlertStatistics> getSeverityStatistics(String severity, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting statistics for severity: {} between {} and {}", severity, startTime, endTime);
        return statisticsRepository.findSeverityStatisticsInTimeRange(severity, startTime, endTime);
    }

    @Override
    public List<AlertStatistics> getAllStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting all statistics between {} and {}", startTime, endTime);
        return statisticsRepository.findStatisticsInTimeRange(startTime, endTime);
    }

    @Override
    public Map<String, Object> getStatisticsSummary(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting statistics summary between {} and {}", startTime, endTime);
        
        List<AlertStatistics> statistics = getAllStatistics(startTime, endTime);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAlerts", statistics.stream().mapToInt(AlertStatistics::getTotalAlerts).sum());
        summary.put("activeAlerts", statistics.stream().mapToInt(AlertStatistics::getActiveAlerts).sum());
        summary.put("resolvedAlerts", statistics.stream().mapToInt(AlertStatistics::getResolvedAlerts).sum());
        summary.put("escalatedAlerts", statistics.stream().mapToInt(AlertStatistics::getEscalatedAlerts).sum());
        
        double avgResolutionTime = statistics.stream()
                .mapToDouble(AlertStatistics::getAverageResolutionTime)
                .average()
                .orElse(0.0);
        summary.put("averageResolutionTime", avgResolutionTime);
        
        double avgEscalationTime = statistics.stream()
                .mapToDouble(AlertStatistics::getAverageEscalationTime)
                .average()
                .orElse(0.0);
        summary.put("averageEscalationTime", avgEscalationTime);
        
        summary.put("notificationCount", statistics.stream().mapToInt(AlertStatistics::getNotificationCount).sum());
        summary.put("notificationSuccessCount", statistics.stream().mapToInt(AlertStatistics::getNotificationSuccessCount).sum());
        summary.put("notificationFailureCount", statistics.stream().mapToInt(AlertStatistics::getNotificationFailureCount).sum());
        
        return summary;
    }

    @Override
    @Transactional
    public void generateDailyStatistics() {
        log.debug("Generating daily statistics");
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        
        List<AlertRule> rules = ruleRepository.findByEnabled(true);
        for (AlertRule rule : rules) {
            generateStatistics(rule.getId(), startTime, endTime);
        }
    }

    @Override
    @Transactional
    public void generateHourlyStatistics() {
        log.debug("Generating hourly statistics");
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(1);
        
        List<AlertRule> rules = ruleRepository.findByEnabled(true);
        for (AlertRule rule : rules) {
            generateStatistics(rule.getId(), startTime, endTime);
        }
    }
} 