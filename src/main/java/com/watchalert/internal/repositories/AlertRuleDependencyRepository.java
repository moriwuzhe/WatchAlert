package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleDependencyRepository extends JpaRepository<AlertRuleDependency, Long> {
    List<AlertRuleDependency> findByRuleIdOrderByPriorityAsc(Long ruleId);
    List<AlertRuleDependency> findByDependentRuleIdOrderByPriorityAsc(Long dependentRuleId);
    List<AlertRuleDependency> findByRuleIdAndEnabled(Long ruleId, boolean enabled);
    List<AlertRuleDependency> findByDependentRuleIdAndEnabled(Long dependentRuleId, boolean enabled);
    List<AlertRuleDependency> findByRuleIdAndDependentRuleId(Long ruleId, Long dependentRuleId);
} 