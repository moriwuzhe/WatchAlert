package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTemplateRepository extends JpaRepository<AlertRuleTemplate, Long> {
    List<AlertRuleTemplate> findByCategory(String category);
    List<AlertRuleTemplate> findByEnabled(boolean enabled);
    List<AlertRuleTemplate> findByDataSource(String dataSource);
    List<AlertRuleTemplate> findByNameContainingIgnoreCase(String name);
} 