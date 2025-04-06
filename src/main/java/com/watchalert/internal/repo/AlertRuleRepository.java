package com.watchalert.internal.repo;

import com.watchalert.internal.models.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {
    List<AlertRule> findByEnabled(boolean enabled);
    List<AlertRule> findByDatasourceType(String datasourceType);
} 