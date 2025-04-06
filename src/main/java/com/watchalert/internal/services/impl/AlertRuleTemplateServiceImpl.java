package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplate;
import com.watchalert.internal.repositories.AlertRuleTemplateRepository;
import com.watchalert.internal.services.AlertRuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateServiceImpl implements AlertRuleTemplateService {

    @Autowired
    private AlertRuleTemplateRepository templateRepository;

    @Override
    @Transactional
    public AlertRuleTemplate createTemplate(AlertRuleTemplate template) {
        log.debug("Creating alert rule template: {}", template.getName());
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public void updateTemplate(AlertRuleTemplate template) {
        log.debug("Updating alert rule template: {}", template.getId());
        AlertRuleTemplate existingTemplate = templateRepository.findById(template.getId())
                .orElseThrow(() -> new RuntimeException("Template not found: " + template.getId()));
        
        existingTemplate.setName(template.getName());
        existingTemplate.setDescription(template.getDescription());
        existingTemplate.setDataSource(template.getDataSource());
        existingTemplate.setQuery(template.getQuery());
        existingTemplate.setCondition(template.getCondition());
        existingTemplate.setSeverity(template.getSeverity());
        existingTemplate.setMessageTemplate(template.getMessageTemplate());
        existingTemplate.setCategory(template.getCategory());
        existingTemplate.setTags(template.getTags());
        existingTemplate.setEnabled(template.isEnabled());
        
        templateRepository.save(existingTemplate);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        log.debug("Deleting alert rule template: {}", id);
        templateRepository.deleteById(id);
    }

    @Override
    public AlertRuleTemplate getTemplate(Long id) {
        log.debug("Getting alert rule template: {}", id);
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found: " + id));
    }

    @Override
    public List<AlertRuleTemplate> getAllTemplates() {
        log.debug("Getting all alert rule templates");
        return templateRepository.findAll();
    }

    @Override
    public List<AlertRuleTemplate> getTemplatesByCategory(String category) {
        log.debug("Getting alert rule templates by category: {}", category);
        return templateRepository.findByCategory(category);
    }

    @Override
    public List<AlertRuleTemplate> getEnabledTemplates() {
        log.debug("Getting enabled alert rule templates");
        return templateRepository.findByEnabled(true);
    }

    @Override
    public List<AlertRuleTemplate> searchTemplates(String keyword) {
        log.debug("Searching alert rule templates with keyword: {}", keyword);
        return templateRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Map<String, Object> getTemplateStatistics() {
        log.debug("Getting alert rule template statistics");
        
        List<AlertRuleTemplate> allTemplates = getAllTemplates();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalTemplates", allTemplates.size());
        statistics.put("enabledTemplates", allTemplates.stream()
                .filter(AlertRuleTemplate::isEnabled)
                .count());
        statistics.put("templatesByCategory", allTemplates.stream()
                .collect(Collectors.groupingBy(AlertRuleTemplate::getCategory, Collectors.counting())));
        statistics.put("templatesByDataSource", allTemplates.stream()
                .collect(Collectors.groupingBy(AlertRuleTemplate::getDataSource, Collectors.counting())));
        
        return statistics;
    }

    @Override
    @Transactional
    public AlertRuleTemplate cloneTemplate(Long id, String newName) {
        log.debug("Cloning alert rule template: {} with new name: {}", id, newName);
        
        AlertRuleTemplate sourceTemplate = getTemplate(id);
        
        AlertRuleTemplate clonedTemplate = new AlertRuleTemplate();
        clonedTemplate.setName(newName);
        clonedTemplate.setDescription(sourceTemplate.getDescription());
        clonedTemplate.setDataSource(sourceTemplate.getDataSource());
        clonedTemplate.setQuery(sourceTemplate.getQuery());
        clonedTemplate.setCondition(sourceTemplate.getCondition());
        clonedTemplate.setSeverity(sourceTemplate.getSeverity());
        clonedTemplate.setMessageTemplate(sourceTemplate.getMessageTemplate());
        clonedTemplate.setCategory(sourceTemplate.getCategory());
        clonedTemplate.setTags(sourceTemplate.getTags());
        clonedTemplate.setEnabled(false); // 默认禁用克隆的模板
        
        return templateRepository.save(clonedTemplate);
    }
} 