package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertStatistics;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AlertStatisticsService {
    AlertStatistics generateStatistics(Long ruleId, LocalDateTime startTime, LocalDateTime endTime);
    List<AlertStatistics> getRuleStatistics(Long ruleId, LocalDateTime startTime, LocalDateTime endTime);
    List<AlertStatistics> getSeverityStatistics(String severity, LocalDateTime startTime, LocalDateTime endTime);
    List<AlertStatistics> getAllStatistics(LocalDateTime startTime, LocalDateTime endTime);
    Map<String, Object> getStatisticsSummary(LocalDateTime startTime, LocalDateTime endTime);
    void generateDailyStatistics();
    void generateHourlyStatistics();
} 