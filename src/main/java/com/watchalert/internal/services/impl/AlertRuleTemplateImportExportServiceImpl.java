package com.watchalert.internal.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watchalert.internal.models.AlertRuleTemplate;
import com.watchalert.internal.repositories.AlertRuleTemplateRepository;
import com.watchalert.internal.services.AlertRuleTemplateImportExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateImportExportServiceImpl implements AlertRuleTemplateImportExportService {

    @Autowired
    private AlertRuleTemplateRepository templateRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // 存储导入导出进度
    private final Map<String, Map<String, Object>> progressMap = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> exportTemplate(Long templateId) {
        log.debug("Exporting template: {}", templateId);
        
        AlertRuleTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));
        
        return convertTemplateToMap(template);
    }

    @Override
    public List<Map<String, Object>> exportTemplates(List<Long> templateIds) {
        log.debug("Exporting templates: {}", templateIds);
        
        String exportId = UUID.randomUUID().toString();
        Map<String, Object> progress = new HashMap<>();
        progress.put("total", templateIds.size());
        progress.put("completed", 0);
        progress.put("status", "IN_PROGRESS");
        progressMap.put(exportId, progress);
        
        List<Map<String, Object>> result = new ArrayList<>();
        int completed = 0;
        
        for (Long templateId : templateIds) {
            try {
                result.add(exportTemplate(templateId));
                completed++;
                progress.put("completed", completed);
            } catch (Exception e) {
                log.error("Error exporting template: {}", templateId, e);
            }
        }
        
        progress.put("status", "COMPLETED");
        return result;
    }

    @Override
    public String exportTemplateAsJson(Long templateId) {
        log.debug("Exporting template as JSON: {}", templateId);
        
        try {
            Map<String, Object> templateData = exportTemplate(templateId);
            return objectMapper.writeValueAsString(templateData);
        } catch (Exception e) {
            log.error("Error exporting template as JSON: {}", templateId, e);
            throw new RuntimeException("Error exporting template as JSON", e);
        }
    }

    @Override
    public String exportTemplatesAsJson(List<Long> templateIds) {
        log.debug("Exporting templates as JSON: {}", templateIds);
        
        try {
            List<Map<String, Object>> templatesData = exportTemplates(templateIds);
            return objectMapper.writeValueAsString(templatesData);
        } catch (Exception e) {
            log.error("Error exporting templates as JSON", e);
            throw new RuntimeException("Error exporting templates as JSON", e);
        }
    }

    @Override
    @Transactional
    public AlertRuleTemplate importTemplate(Map<String, Object> templateData, String createdBy) {
        log.debug("Importing template");
        
        // 验证模板数据
        Map<String, Object> validationResult = validateTemplateData(templateData);
        if (!(Boolean) validationResult.get("valid")) {
            throw new IllegalArgumentException("Invalid template data: " + validationResult.get("message"));
        }
        
        // 创建模板
        AlertRuleTemplate template = new AlertRuleTemplate();
        template.setName((String) templateData.get("name"));
        template.setDescription((String) templateData.get("description"));
        template.setCategory((String) templateData.get("category"));
        template.setQuery((String) templateData.get("query"));
        template.setCondition((String) templateData.get("condition"));
        template.setSeverity((String) templateData.get("severity"));
        template.setNotificationConfig((String) templateData.get("notificationConfig"));
        template.setCreatedBy(createdBy);
        
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public List<AlertRuleTemplate> importTemplates(List<Map<String, Object>> templatesData, String createdBy) {
        log.debug("Importing {} templates", templatesData.size());
        
        String importId = UUID.randomUUID().toString();
        Map<String, Object> progress = new HashMap<>();
        progress.put("total", templatesData.size());
        progress.put("completed", 0);
        progress.put("status", "IN_PROGRESS");
        progressMap.put(importId, progress);
        
        List<AlertRuleTemplate> result = new ArrayList<>();
        int completed = 0;
        
        for (Map<String, Object> templateData : templatesData) {
            try {
                result.add(importTemplate(templateData, createdBy));
                completed++;
                progress.put("completed", completed);
            } catch (Exception e) {
                log.error("Error importing template", e);
            }
        }
        
        progress.put("status", "COMPLETED");
        return result;
    }

    @Override
    @Transactional
    public AlertRuleTemplate importTemplateFromJson(String jsonData, String createdBy) {
        log.debug("Importing template from JSON");
        
        try {
            Map<String, Object> templateData = objectMapper.readValue(jsonData, Map.class);
            return importTemplate(templateData, createdBy);
        } catch (Exception e) {
            log.error("Error importing template from JSON", e);
            throw new RuntimeException("Error importing template from JSON", e);
        }
    }

    @Override
    @Transactional
    public List<AlertRuleTemplate> importTemplatesFromJson(String jsonData, String createdBy) {
        log.debug("Importing templates from JSON");
        
        try {
            List<Map<String, Object>> templatesData = objectMapper.readValue(jsonData, List.class);
            return importTemplates(templatesData, createdBy);
        } catch (Exception e) {
            log.error("Error importing templates from JSON", e);
            throw new RuntimeException("Error importing templates from JSON", e);
        }
    }

    @Override
    public Map<String, Object> validateTemplateData(Map<String, Object> templateData) {
        log.debug("Validating template data");
        
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 检查必填字段
        if (!templateData.containsKey("name") || templateData.get("name") == null) {
            errors.add("Name is required");
        }
        if (!templateData.containsKey("query") || templateData.get("query") == null) {
            errors.add("Query is required");
        }
        if (!templateData.containsKey("condition") || templateData.get("condition") == null) {
            errors.add("Condition is required");
        }
        if (!templateData.containsKey("severity") || templateData.get("severity") == null) {
            errors.add("Severity is required");
        }
        
        // 检查字段类型
        if (templateData.get("name") != null && !(templateData.get("name") instanceof String)) {
            errors.add("Name must be a string");
        }
        if (templateData.get("description") != null && !(templateData.get("description") instanceof String)) {
            errors.add("Description must be a string");
        }
        if (templateData.get("category") != null && !(templateData.get("category") instanceof String)) {
            errors.add("Category must be a string");
        }
        if (templateData.get("query") != null && !(templateData.get("query") instanceof String)) {
            errors.add("Query must be a string");
        }
        if (templateData.get("condition") != null && !(templateData.get("condition") instanceof String)) {
            errors.add("Condition must be a string");
        }
        if (templateData.get("severity") != null && !(templateData.get("severity") instanceof String)) {
            errors.add("Severity must be a string");
        }
        if (templateData.get("notificationConfig") != null && !(templateData.get("notificationConfig") instanceof String)) {
            errors.add("Notification config must be a string");
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        
        return result;
    }

    @Override
    public Map<String, Object> getImportProgress(String importId) {
        log.debug("Getting import progress: {}", importId);
        return progressMap.getOrDefault(importId, new HashMap<>());
    }

    @Override
    public Map<String, Object> getExportProgress(String exportId) {
        log.debug("Getting export progress: {}", exportId);
        return progressMap.getOrDefault(exportId, new HashMap<>());
    }

    private Map<String, Object> convertTemplateToMap(AlertRuleTemplate template) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", template.getName());
        result.put("description", template.getDescription());
        result.put("category", template.getCategory());
        result.put("query", template.getQuery());
        result.put("condition", template.getCondition());
        result.put("severity", template.getSeverity());
        result.put("notificationConfig", template.getNotificationConfig());
        result.put("createdBy", template.getCreatedBy());
        result.put("createdAt", template.getCreatedAt());
        result.put("updatedAt", template.getUpdatedAt());
        return result;
    }
} 