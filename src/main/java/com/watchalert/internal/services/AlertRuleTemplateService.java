package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTemplate;
import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateService {
    AlertRuleTemplate createTemplate(AlertRuleTemplate template);
    void updateTemplate(AlertRuleTemplate template);
    void deleteTemplate(Long id);
    AlertRuleTemplate getTemplate(Long id);
    List<AlertRuleTemplate> getAllTemplates();
    List<AlertRuleTemplate> getTemplatesByCategory(String category);
    List<AlertRuleTemplate> getEnabledTemplates();
    List<AlertRuleTemplate> searchTemplates(String keyword);
    Map<String, Object> getTemplateStatistics();
    AlertRuleTemplate cloneTemplate(Long id, String newName);
} 