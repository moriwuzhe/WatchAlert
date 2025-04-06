package com.watchalert.internal.repo;

import com.watchalert.internal.models.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    List<AlertHistory> findByRuleId(Long ruleId);
    List<AlertHistory> findByStatus(String status);
} 