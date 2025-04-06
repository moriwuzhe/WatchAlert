package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertStatisticsRepository extends JpaRepository<AlertStatistics, Long> {
    List<AlertStatistics> findByRuleId(Long ruleId);
    List<AlertStatistics> findBySeverity(String severity);
    List<AlertStatistics> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT s FROM AlertStatistics s WHERE s.startTime >= ?1 AND s.endTime <= ?2")
    List<AlertStatistics> findStatisticsInTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT s FROM AlertStatistics s WHERE s.ruleId = ?1 AND s.startTime >= ?2 AND s.endTime <= ?3")
    List<AlertStatistics> findRuleStatisticsInTimeRange(Long ruleId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT s FROM AlertStatistics s WHERE s.severity = ?1 AND s.startTime >= ?2 AND s.endTime <= ?3")
    List<AlertStatistics> findSeverityStatisticsInTimeRange(String severity, LocalDateTime startTime, LocalDateTime endTime);
} 