package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateVersion;
import com.watchalert.internal.models.AlertRuleTemplate;
import com.watchalert.internal.repositories.AlertRuleTemplateVersionRepository;
import com.watchalert.internal.repositories.AlertRuleTemplateRepository;
import com.watchalert.internal.services.AlertRuleTemplateVersionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateVersionServiceImpl implements AlertRuleTemplateVersionService {

    @Autowired
    private AlertRuleTemplateVersionRepository versionRepository;
    
    @Autowired
    private AlertRuleTemplateRepository templateRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public AlertRuleTemplateVersion createVersion(Long templateId, String versionType, String changeLog, String createdBy) {
        log.debug("Creating version for template: {}", templateId);
        
        AlertRuleTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));
        
        // 生成新版本号
        String version = generateNextVersion(templateId, versionType);
        
        // 创建版本对象
        AlertRuleTemplateVersion templateVersion = new AlertRuleTemplateVersion();
        templateVersion.setTemplate(template);
        templateVersion.setVersion(version);
        templateVersion.setVersionType(versionType);
        templateVersion.setChangeLog(changeLog);
        templateVersion.setCreatedBy(createdBy);
        
        try {
            // 序列化模板数据
            templateVersion.setTemplateData(objectMapper.writeValueAsString(template));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize template data: " + e.getMessage());
        }
        
        return versionRepository.save(templateVersion);
    }

    @Override
    public AlertRuleTemplateVersion getVersion(Long id) {
        log.debug("Getting version: {}", id);
        return versionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + id));
    }

    @Override
    public List<AlertRuleTemplateVersion> getVersionsByTemplate(Long templateId) {
        log.debug("Getting versions for template: {}", templateId);
        return versionRepository.findByTemplateId(templateId);
    }

    @Override
    public AlertRuleTemplateVersion getLatestVersion(Long templateId) {
        log.debug("Getting latest version for template: {}", templateId);
        return versionRepository.findTopByTemplateIdOrderByCreatedAtDesc(templateId);
    }

    @Override
    @Transactional
    public AlertRuleTemplateVersion rollbackToVersion(Long id, String createdBy) {
        log.debug("Rolling back to version: {}", id);
        
        AlertRuleTemplateVersion version = getVersion(id);
        AlertRuleTemplate template = version.getTemplate();
        
        try {
            // 反序列化模板数据
            AlertRuleTemplate oldTemplate = objectMapper.readValue(version.getTemplateData(), AlertRuleTemplate.class);
            
            // 更新模板
            template.setName(oldTemplate.getName());
            template.setDescription(oldTemplate.getDescription());
            template.setDataSource(oldTemplate.getDataSource());
            template.setQuery(oldTemplate.getQuery());
            template.setCondition(oldTemplate.getCondition());
            template.setMessageTemplate(oldTemplate.getMessageTemplate());
            template.setSeverity(oldTemplate.getSeverity());
            template.setCategory(oldTemplate.getCategory());
            
            templateRepository.save(template);
            
            // 创建回滚版本
            return createVersion(template.getId(), "PATCH", 
                    "Rolled back to version " + version.getVersion(), createdBy);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to rollback version: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> compareVersions(Long version1Id, Long version2Id) {
        log.debug("Comparing versions: {} and {}", version1Id, version2Id);
        
        AlertRuleTemplateVersion version1 = getVersion(version1Id);
        AlertRuleTemplateVersion version2 = getVersion(version2Id);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> changes = new ArrayList<>();
        
        try {
            AlertRuleTemplate template1 = objectMapper.readValue(version1.getTemplateData(), AlertRuleTemplate.class);
            AlertRuleTemplate template2 = objectMapper.readValue(version2.getTemplateData(), AlertRuleTemplate.class);
            
            // 比较基本信息
            compareField("name", template1.getName(), template2.getName(), changes);
            compareField("description", template1.getDescription(), template2.getDescription(), changes);
            compareField("dataSource", template1.getDataSource(), template2.getDataSource(), changes);
            compareField("query", template1.getQuery(), template2.getQuery(), changes);
            compareField("condition", template1.getCondition(), template2.getCondition(), changes);
            compareField("messageTemplate", template1.getMessageTemplate(), template2.getMessageTemplate(), changes);
            compareField("severity", template1.getSeverity(), template2.getSeverity(), changes);
            compareField("category", template1.getCategory(), template2.getCategory(), changes);
            
            result.put("version1", version1.getVersion());
            result.put("version2", version2.getVersion());
            result.put("changes", changes);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to compare versions: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getVersionHistory(Long templateId) {
        log.debug("Getting version history for template: {}", templateId);
        
        List<AlertRuleTemplateVersion> versions = getVersionsByTemplate(templateId);
        return versions.stream()
                .map(version -> {
                    Map<String, Object> history = new HashMap<>();
                    history.put("id", version.getId());
                    history.put("version", version.getVersion());
                    history.put("versionType", version.getVersionType());
                    history.put("changeLog", version.getChangeLog());
                    history.put("createdBy", version.getCreatedBy());
                    history.put("createdAt", version.getCreatedAt());
                    return history;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isVersionAvailable(Long templateId, String version, Long excludeId) {
        log.debug("Checking if version is available: {}", version);
        
        AlertRuleTemplateVersion existing = versionRepository.findByTemplateIdAndVersion(templateId, version);
        if (existing == null) {
            return true;
        }
        
        return excludeId != null && existing.getId().equals(excludeId);
    }

    @Override
    public String generateNextVersion(Long templateId, String versionType) {
        log.debug("Generating next version for template: {}", templateId);
        
        AlertRuleTemplateVersion latest = getLatestVersion(templateId);
        if (latest == null) {
            return "1.0.0";
        }
        
        String[] parts = latest.getVersion().split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);
        
        switch (versionType.toUpperCase()) {
            case "MAJOR":
                return (major + 1) + ".0.0";
            case "MINOR":
                return major + "." + (minor + 1) + ".0";
            case "PATCH":
                return major + "." + minor + "." + (patch + 1);
            default:
                throw new IllegalArgumentException("Invalid version type: " + versionType);
        }
    }

    @Override
    public Map<String, Object> getVersionStatistics(Long templateId) {
        log.debug("Getting version statistics for template: {}", templateId);
        
        List<AlertRuleTemplateVersion> versions = getVersionsByTemplate(templateId);
        Map<String, Object> result = new HashMap<>();
        
        int total = versions.size();
        int major = 0;
        int minor = 0;
        int patch = 0;
        
        for (AlertRuleTemplateVersion version : versions) {
            switch (version.getVersionType().toUpperCase()) {
                case "MAJOR":
                    major++;
                    break;
                case "MINOR":
                    minor++;
                    break;
                case "PATCH":
                    patch++;
                    break;
            }
        }
        
        result.put("total", total);
        result.put("major", major);
        result.put("minor", minor);
        result.put("patch", patch);
        result.put("latestVersion", getLatestVersion(templateId).getVersion());
        
        return result;
    }

    private void compareField(String field, Object value1, Object value2, List<Map<String, Object>> changes) {
        if (!Objects.equals(value1, value2)) {
            Map<String, Object> change = new HashMap<>();
            change.put("field", field);
            change.put("oldValue", value1);
            change.put("newValue", value2);
            changes.add(change);
        }
    }
} 