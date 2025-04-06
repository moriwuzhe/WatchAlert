package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertRuleVersion;
import com.watchalert.internal.repositories.AlertRuleRepository;
import com.watchalert.internal.repositories.AlertRuleVersionRepository;
import com.watchalert.internal.services.AlertRuleVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.lang.reflect.Field;

@Slf4j
@Service
public class AlertRuleVersionServiceImpl implements AlertRuleVersionService {

    @Autowired
    private AlertRuleRepository ruleRepository;

    @Autowired
    private AlertRuleVersionRepository versionRepository;

    @Override
    @Transactional
    public AlertRuleVersion createVersion(Long ruleId, String comment, String createdBy) {
        log.debug("Creating version for rule: {}", ruleId);
        
        AlertRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found: " + ruleId));
        
        // 获取最新版本号
        AlertRuleVersion latestVersion = getLatestVersion(ruleId);
        String newVersion = latestVersion == null ? "1.0.0" : incrementVersion(latestVersion.getVersion());
        
        // 创建新版本
        AlertRuleVersion version = new AlertRuleVersion();
        version.setRule(rule);
        version.setVersion(newVersion);
        version.setName(rule.getName());
        version.setDescription(rule.getDescription());
        version.setDatasourceType(rule.getDatasourceType());
        version.setQuery(rule.getQuery());
        version.setCondition(rule.getCondition());
        version.setSeverity(rule.getSeverity());
        version.setNotifyChannels(rule.getNotifyChannels());
        version.setNotifyUsers(rule.getNotifyUsers());
        version.setNotifyGroups(rule.getNotifyGroups());
        version.setNotifyTemplate(rule.getNotifyTemplate());
        version.setTags(rule.getTags());
        version.setEnabled(rule.isEnabled());
        version.setCreatedBy(createdBy);
        version.setComment(comment);
        
        return versionRepository.save(version);
    }

    @Override
    public List<AlertRuleVersion> getVersions(Long ruleId) {
        log.debug("Getting versions for rule: {}", ruleId);
        return versionRepository.findByRuleIdOrderByCreatedAtDesc(ruleId);
    }

    @Override
    public AlertRuleVersion getVersion(Long ruleId, String version) {
        log.debug("Getting version {} for rule: {}", version, ruleId);
        List<AlertRuleVersion> versions = versionRepository.findByRuleIdAndVersion(ruleId, version);
        return versions.isEmpty() ? null : versions.get(0);
    }

    @Override
    public AlertRuleVersion getLatestVersion(Long ruleId) {
        log.debug("Getting latest version for rule: {}", ruleId);
        return versionRepository.findTopByRuleIdOrderByCreatedAtDesc(ruleId);
    }

    @Override
    @Transactional
    public AlertRuleVersion rollbackToVersion(Long ruleId, String version, String createdBy) {
        log.debug("Rolling back rule {} to version: {}", ruleId, version);
        
        AlertRuleVersion targetVersion = getVersion(ruleId, version);
        if (targetVersion == null) {
            throw new RuntimeException("Version not found: " + version);
        }
        
        AlertRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found: " + ruleId));
        
        // 更新规则
        rule.setName(targetVersion.getName());
        rule.setDescription(targetVersion.getDescription());
        rule.setDatasourceType(targetVersion.getDatasourceType());
        rule.setQuery(targetVersion.getQuery());
        rule.setCondition(targetVersion.getCondition());
        rule.setSeverity(targetVersion.getSeverity());
        rule.setNotifyChannels(targetVersion.getNotifyChannels());
        rule.setNotifyUsers(targetVersion.getNotifyUsers());
        rule.setNotifyGroups(targetVersion.getNotifyGroups());
        rule.setNotifyTemplate(targetVersion.getNotifyTemplate());
        rule.setTags(targetVersion.getTags());
        rule.setEnabled(targetVersion.isEnabled());
        
        ruleRepository.save(rule);
        
        // 创建新版本
        return createVersion(ruleId, "Rollback to version " + version, createdBy);
    }

    @Override
    public Map<String, Object> compareVersions(Long ruleId, String version1, String version2) {
        log.debug("Comparing versions {} and {} for rule: {}", version1, version2, ruleId);
        
        AlertRuleVersion v1 = getVersion(ruleId, version1);
        AlertRuleVersion v2 = getVersion(ruleId, version2);
        
        if (v1 == null || v2 == null) {
            throw new RuntimeException("One or both versions not found");
        }
        
        Map<String, Object> differences = new HashMap<>();
        
        // 比较所有字段
        for (Field field : AlertRuleVersion.class.getDeclaredFields()) {
            if (!field.getName().equals("id") && !field.getName().equals("rule") && 
                !field.getName().equals("version") && !field.getName().equals("createdAt") && 
                !field.getName().equals("createdBy") && !field.getName().equals("comment")) {
                
                try {
                    field.setAccessible(true);
                    Object value1 = field.get(v1);
                    Object value2 = field.get(v2);
                    
                    if (!Objects.equals(value1, value2)) {
                        differences.put(field.getName(), Map.of(
                            "old", value1,
                            "new", value2
                        ));
                    }
                } catch (IllegalAccessException e) {
                    log.error("Failed to compare field: {}", field.getName(), e);
                }
            }
        }
        
        return differences;
    }

    private String incrementVersion(String currentVersion) {
        String[] parts = currentVersion.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);
        
        patch++;
        if (patch > 99) {
            patch = 0;
            minor++;
            if (minor > 99) {
                minor = 0;
                major++;
            }
        }
        
        return String.format("%d.%d.%d", major, minor, patch);
    }
} 