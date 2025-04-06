package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateVersion;
import com.watchalert.internal.repositories.AlertRuleTemplateVersionRepository;
import com.watchalert.internal.services.AlertRuleTemplateVersionCompareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateVersionCompareServiceImpl implements AlertRuleTemplateVersionCompareService {

    @Autowired
    private AlertRuleTemplateVersionRepository versionRepository;

    @Override
    public Map<String, Object> compareVersions(Long version1Id, Long version2Id) {
        log.debug("Comparing versions: {} and {}", version1Id, version2Id);
        
        AlertRuleTemplateVersion version1 = getVersion(version1Id);
        AlertRuleTemplateVersion version2 = getVersion(version2Id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("version1", convertVersionToMap(version1));
        result.put("version2", convertVersionToMap(version2));
        result.put("differences", getVersionDifferences(version1Id, version2Id));
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getVersionDifferences(Long version1Id, Long version2Id) {
        log.debug("Getting differences between versions: {} and {}", version1Id, version2Id);
        
        AlertRuleTemplateVersion version1 = getVersion(version1Id);
        AlertRuleTemplateVersion version2 = getVersion(version2Id);
        
        List<Map<String, Object>> differences = new ArrayList<>();
        
        // 比较版本号
        if (!version1.getVersionNumber().equals(version2.getVersionNumber())) {
            differences.add(createDifference("versionNumber", version1.getVersionNumber(), version2.getVersionNumber()));
        }
        
        // 比较版本类型
        if (!version1.getVersionType().equals(version2.getVersionType())) {
            differences.add(createDifference("versionType", version1.getVersionType(), version2.getVersionType()));
        }
        
        // 比较规则内容
        if (!version1.getRuleContent().equals(version2.getRuleContent())) {
            differences.add(createDifference("ruleContent", version1.getRuleContent(), version2.getRuleContent()));
        }
        
        // 比较描述
        if (!Objects.equals(version1.getDescription(), version2.getDescription())) {
            differences.add(createDifference("description", version1.getDescription(), version2.getDescription()));
        }
        
        return differences;
    }

    @Override
    public List<Map<String, Object>> getVersionHistory(Long templateId) {
        log.debug("Getting version history for template: {}", templateId);
        
        return versionRepository.findByTemplateIdOrderByCreatedAtDesc(templateId).stream()
                .map(this::convertVersionToMap)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getVersionChangeStatistics(Long templateId) {
        log.debug("Getting version change statistics for template: {}", templateId);
        
        List<AlertRuleTemplateVersion> versions = versionRepository.findByTemplateIdOrderByCreatedAtDesc(templateId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalVersions", versions.size());
        
        // 按版本类型统计
        Map<String, Long> typeCount = versions.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateVersion::getVersionType,
                        Collectors.counting()
                ));
        result.put("typeCount", typeCount);
        
        // 按创建者统计
        Map<String, Long> creatorCount = versions.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateVersion::getCreatedBy,
                        Collectors.counting()
                ));
        result.put("creatorCount", creatorCount);
        
        // 按时间统计
        Map<String, Long> timeCount = versions.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()
                ));
        result.put("timeCount", timeCount);
        
        return result;
    }

    @Override
    public Map<String, Object> getVersionChangeChartData(Long templateId) {
        log.debug("Getting version change chart data for template: {}", templateId);
        
        List<AlertRuleTemplateVersion> versions = versionRepository.findByTemplateIdOrderByCreatedAtDesc(templateId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 准备时间序列数据
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        // 按时间分组统计
        Map<LocalDateTime, Long> timeData = versions.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateVersion::getCreatedAt,
                        Collectors.counting()
                ));
        
        // 排序并添加到图表数据
        timeData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    labels.add(entry.getKey().toString());
                    data.add(entry.getValue());
                });
        
        result.put("labels", labels);
        result.put("data", data);
        
        return result;
    }

    @Override
    public Map<String, Object> getVersionChangeDetails(Long versionId) {
        log.debug("Getting version change details for version: {}", versionId);
        
        AlertRuleTemplateVersion version = getVersion(versionId);
        return convertVersionToMap(version);
    }

    @Override
    public Map<String, Object> getVersionChangeImpactAnalysis(Long version1Id, Long version2Id) {
        log.debug("Getting version change impact analysis for versions: {} and {}", version1Id, version2Id);
        
        AlertRuleTemplateVersion version1 = getVersion(version1Id);
        AlertRuleTemplateVersion version2 = getVersion(version2Id);
        
        Map<String, Object> result = new HashMap<>();
        
        // 分析规则内容变化
        Map<String, Object> ruleAnalysis = analyzeRuleChanges(version1.getRuleContent(), version2.getRuleContent());
        result.put("ruleAnalysis", ruleAnalysis);
        
        // 分析版本类型变化
        Map<String, Object> typeAnalysis = analyzeTypeChange(version1.getVersionType(), version2.getVersionType());
        result.put("typeAnalysis", typeAnalysis);
        
        return result;
    }

    @Override
    public Map<String, Object> getVersionChangeSuggestions(Long version1Id, Long version2Id) {
        log.debug("Getting version change suggestions for versions: {} and {}", version1Id, version2Id);
        
        AlertRuleTemplateVersion version1 = getVersion(version1Id);
        AlertRuleTemplateVersion version2 = getVersion(version2Id);
        
        Map<String, Object> result = new HashMap<>();
        List<String> suggestions = new ArrayList<>();
        
        // 基于版本类型提供建议
        if (!version1.getVersionType().equals(version2.getVersionType())) {
            suggestions.add("考虑是否需要更新相关的文档");
            suggestions.add("检查是否需要通知相关用户");
        }
        
        // 基于规则内容变化提供建议
        if (!version1.getRuleContent().equals(version2.getRuleContent())) {
            suggestions.add("建议进行规则测试");
            suggestions.add("考虑是否需要更新规则说明文档");
        }
        
        result.put("suggestions", suggestions);
        return result;
    }

    private AlertRuleTemplateVersion getVersion(Long versionId) {
        return versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));
    }

    private Map<String, Object> convertVersionToMap(AlertRuleTemplateVersion version) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", version.getId());
        result.put("templateId", version.getTemplateId());
        result.put("versionNumber", version.getVersionNumber());
        result.put("versionType", version.getVersionType());
        result.put("ruleContent", version.getRuleContent());
        result.put("description", version.getDescription());
        result.put("createdBy", version.getCreatedBy());
        result.put("createdAt", version.getCreatedAt());
        result.put("updatedAt", version.getUpdatedAt());
        return result;
    }

    private Map<String, Object> createDifference(String field, Object oldValue, Object newValue) {
        Map<String, Object> difference = new HashMap<>();
        difference.put("field", field);
        difference.put("oldValue", oldValue);
        difference.put("newValue", newValue);
        return difference;
    }

    private Map<String, Object> analyzeRuleChanges(String oldRule, String newRule) {
        Map<String, Object> analysis = new HashMap<>();
        // TODO: 实现规则内容变化分析
        return analysis;
    }

    private Map<String, Object> analyzeTypeChange(String oldType, String newType) {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("oldType", oldType);
        analysis.put("newType", newType);
        analysis.put("isMajorChange", "MAJOR".equals(newType));
        analysis.put("isMinorChange", "MINOR".equals(newType));
        analysis.put("isPatchChange", "PATCH".equals(newType));
        return analysis;
    }
} 