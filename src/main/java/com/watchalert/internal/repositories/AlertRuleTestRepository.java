package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTestRepository extends JpaRepository<AlertRuleTest, Long> {
    List<AlertRuleTest> findByRuleId(Long ruleId);
    List<AlertRuleTest> findByRuleIdOrderByExecutedAtDesc(Long ruleId);
} 