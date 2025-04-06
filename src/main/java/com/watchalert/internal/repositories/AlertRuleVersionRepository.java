package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleVersionRepository extends JpaRepository<AlertRuleVersion, Long> {
    List<AlertRuleVersion> findByRuleIdOrderByCreatedAtDesc(Long ruleId);
    List<AlertRuleVersion> findByRuleIdAndVersion(Long ruleId, String version);
    AlertRuleVersion findTopByRuleIdOrderByCreatedAtDesc(Long ruleId);
} 