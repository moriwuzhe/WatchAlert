package com.watchalert.repository;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {
    List<Alert> findByStatus(String status);
    List<Alert> findByAlertRule(AlertRule alertRule);
} 