package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertEscalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertEscalationRepository extends JpaRepository<AlertEscalation, Long> {
    List<AlertEscalation> findByRuleIdOrderByLevelAsc(Long ruleId);
    List<AlertEscalation> findByRuleIdAndLevelGreaterThanOrderByLevelAsc(Long ruleId, Integer level);
} 