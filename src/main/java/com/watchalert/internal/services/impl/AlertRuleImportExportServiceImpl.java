package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertRuleTemplate;
import com.watchalert.internal.services.AlertRuleImportExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

@Slf4j
@Service
public class AlertRuleImportExportServiceImpl implements AlertRuleImportExportService {

    @Autowired
    private ObjectMapper objectMapper;

    private static final Set<String> REQUIRED_RULE_FIELDS = new HashSet<>(List.of(
        "name", "description", "datasourceType", "query", "condition",
        "severity", "notifyChannels", "notifyUsers", "notifyGroups"
    ));

    private static final Set<String> REQUIRED_TEMPLATE_FIELDS = new HashSet<>(List.of(
        "name", "description", "dataSource", "query", "condition",
        "severity", "messageTemplate", "category"
    ));

    @Override
    public void exportRules(List<AlertRule> rules, OutputStream outputStream) {
        try {
            log.debug("Exporting {} alert rules", rules.size());
            objectMapper.writeValue(outputStream, rules);
        } catch (Exception e) {
            log.error("Failed to export alert rules", e);
            throw new RuntimeException("Failed to export alert rules", e);
        }
    }

    @Override
    public List<AlertRule> importRules(InputStream inputStream) {
        try {
            log.debug("Importing alert rules from input stream");
            JsonNode root = objectMapper.readTree(inputStream);
            List<AlertRule> rules = new ArrayList<>();
            
            if (root.isArray()) {
                for (JsonNode node : root) {
                    Map<String, Object> ruleData = objectMapper.convertValue(node, Map.class);
                    Map<String, Object> validationResult = validateRuleData(ruleData);
                    
                    if ((Boolean) validationResult.get("valid")) {
                        AlertRule rule = objectMapper.convertValue(ruleData, AlertRule.class);
                        rules.add(rule);
                    } else {
                        log.warn("Skipping invalid rule: {}", validationResult.get("errors"));
                    }
                }
            }
            
            log.debug("Successfully imported {} alert rules", rules.size());
            return rules;
        } catch (Exception e) {
            log.error("Failed to import alert rules", e);
            throw new RuntimeException("Failed to import alert rules", e);
        }
    }

    @Override
    public void exportTemplates(List<AlertRuleTemplate> templates, OutputStream outputStream) {
        try {
            log.debug("Exporting {} alert rule templates", templates.size());
            objectMapper.writeValue(outputStream, templates);
        } catch (Exception e) {
            log.error("Failed to export alert rule templates", e);
            throw new RuntimeException("Failed to export alert rule templates", e);
        }
    }

    @Override
    public List<AlertRuleTemplate> importTemplates(InputStream inputStream) {
        try {
            log.debug("Importing alert rule templates from input stream");
            JsonNode root = objectMapper.readTree(inputStream);
            List<AlertRuleTemplate> templates = new ArrayList<>();
            
            if (root.isArray()) {
                for (JsonNode node : root) {
                    Map<String, Object> templateData = objectMapper.convertValue(node, Map.class);
                    Map<String, Object> validationResult = validateTemplateData(templateData);
                    
                    if ((Boolean) validationResult.get("valid")) {
                        AlertRuleTemplate template = objectMapper.convertValue(templateData, AlertRuleTemplate.class);
                        templates.add(template);
                    } else {
                        log.warn("Skipping invalid template: {}", validationResult.get("errors"));
                    }
                }
            }
            
            log.debug("Successfully imported {} alert rule templates", templates.size());
            return templates;
        } catch (Exception e) {
            log.error("Failed to import alert rule templates", e);
            throw new RuntimeException("Failed to import alert rule templates", e);
        }
    }

    @Override
    public Map<String, Object> validateRuleData(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 检查必需字段
        for (String field : REQUIRED_RULE_FIELDS) {
            if (!data.containsKey(field) || data.get(field) == null) {
                errors.add("Missing required field: " + field);
            }
        }
        
        // 验证字段类型和值
        if (data.containsKey("severity")) {
            String severity = (String) data.get("severity");
            if (!List.of("critical", "warning", "info").contains(severity.toLowerCase())) {
                errors.add("Invalid severity value: " + severity);
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public Map<String, Object> validateTemplateData(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 检查必需字段
        for (String field : REQUIRED_TEMPLATE_FIELDS) {
            if (!data.containsKey(field) || data.get(field) == null) {
                errors.add("Missing required field: " + field);
            }
        }
        
        // 验证字段类型和值
        if (data.containsKey("severity")) {
            String severity = (String) data.get("severity");
            if (!List.of("critical", "warning", "info").contains(severity.toLowerCase())) {
                errors.add("Invalid severity value: " + severity);
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }
} 