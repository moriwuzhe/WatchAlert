package com.watchalert.repository;

import com.watchalert.model.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, String> {
    List<AlertHistory> findByAlertRuleIdOrderByTriggeredAtDesc(String alertRuleId);
    List<AlertHistory> findByStatusOrderByTriggeredAtDesc(String status);
    List<AlertHistory> findByNotificationSentFalse();
} 