package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    /**
     * 查找指定时间范围内的告警
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 告警列表
     */
    List<AlertHistory> findByTriggerTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查找指定规则的告警
     *
     * @param ruleId 规则ID
     * @return 告警列表
     */
    List<AlertHistory> findByRuleId(Long ruleId);

    /**
     * 查找指定状态的告警
     *
     * @param status 状态
     * @return 告警列表
     */
    List<AlertHistory> findByStatus(String status);

    /**
     * 查找指定严重程度的告警
     *
     * @param severity 严重程度
     * @return 告警列表
     */
    List<AlertHistory> findBySeverity(String severity);

    List<AlertHistory> findByRuleIdAndStatus(Long ruleId, String status);
} 