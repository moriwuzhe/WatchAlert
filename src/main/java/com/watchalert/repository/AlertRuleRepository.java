package com.watchalert.repository;

import com.watchalert.model.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, String> {
    List<AlertRule> findByEnabledTrue();
} 