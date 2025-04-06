package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCaseVersion;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyTestCaseVersionRepository;
import com.watchalert.internal.services.AlertRuleTemplateDependencyTestCaseVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateDependencyTestCaseVersionServiceImpl implements AlertRuleTemplateDependencyTestCaseVersionService {

    @Autowired
    private AlertRuleTemplateDependencyTestCaseVersionRepository versionRepository;

    @Override
    @Transactional
    public Long createVersion(Long testCaseId, String versionNumber, String versionType, String testData, 
            String expectedResult, String testType, Integer priority, String changeDescription, String createdBy) {
        log.debug("Creating version for test case: {}, version: {}", testCaseId, versionNumber);
        
        AlertRuleTemplateDependencyTestCaseVersion version = new AlertRuleTemplateDependencyTestCaseVersion();
        version.setTestCaseId(testCaseId);
        version.setVersionNumber(versionNumber);
        version.setVersionType(versionType);
        version.setTestData(testData);
        version.setExpectedResult(expectedResult);
        version.setTestType(testType);
        version.setPriority(priority);
        version.setChangeDescription(changeDescription);
        version.setCreatedBy(createdBy);
        
        return versionRepository.save(version).getId();
    }

    @Override
    public List<Map<String, Object>> getVersionHistory(Long testCaseId) {
        log.debug("Getting version history for test case: {}", testCaseId);
        
        return versionRepository.findByTestCaseIdOrderByCreatedAtDesc(testCaseId).stream()
                .map(this::convertVersionToMap)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getVersion(Long versionId) {
        log.debug("Getting version: {}", versionId);
        return convertVersionToMap(getVersionEntity(versionId));
    }

    @Override
    public Map<String, Object> getVersionByNumber(Long testCaseId, String versionNumber) {
        log.debug("Getting version by number: {} for test case: {}", versionNumber, testCaseId);
        
        AlertRuleTemplateDependencyTestCaseVersion version = versionRepository
                .findByTestCaseIdAndVersionNumber(testCaseId, versionNumber);
        
        if (version == null) {
            throw new IllegalArgumentException("Version not found: " + versionNumber);
        }
        
        return convertVersionToMap(version);
    }

    @Override
    public Map<String, Object> compareVersions(Long versionId1, Long versionId2) {
        log.debug("Comparing versions: {} and {}", versionId1, versionId2);
        
        AlertRuleTemplateDependencyTestCaseVersion version1 = getVersionEntity(versionId1);
        AlertRuleTemplateDependencyTestCaseVersion version2 = getVersionEntity(versionId2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("version1", convertVersionToMap(version1));
        result.put("version2", convertVersionToMap(version2));
        
        // 比较版本号
        result.put("versionNumberDiff", !version1.getVersionNumber().equals(version2.getVersionNumber()));
        
        // 比较版本类型
        result.put("versionTypeDiff", !version1.getVersionType().equals(version2.getVersionType()));
        
        // 比较测试数据
        result.put("testDataDiff", !Objects.equals(version1.getTestData(), version2.getTestData()));
        
        // 比较预期结果
        result.put("expectedResultDiff", !Objects.equals(version1.getExpectedResult(), version2.getExpectedResult()));
        
        // 比较测试类型
        result.put("testTypeDiff", !Objects.equals(version1.getTestType(), version2.getTestType()));
        
        // 比较优先级
        result.put("priorityDiff", !Objects.equals(version1.getPriority(), version2.getPriority()));
        
        // 计算变更摘要
        List<String> changes = new ArrayList<>();
        
        if (!version1.getVersionNumber().equals(version2.getVersionNumber())) {
            changes.add("版本号从 " + version1.getVersionNumber() + " 变更为 " + version2.getVersionNumber());
        }
        
        if (!version1.getVersionType().equals(version2.getVersionType())) {
            changes.add("版本类型从 " + version1.getVersionType() + " 变更为 " + version2.getVersionType());
        }
        
        if (!Objects.equals(version1.getTestData(), version2.getTestData())) {
            changes.add("测试数据已变更");
        }
        
        if (!Objects.equals(version1.getExpectedResult(), version2.getExpectedResult())) {
            changes.add("预期结果已变更");
        }
        
        if (!Objects.equals(version1.getTestType(), version2.getTestType())) {
            changes.add("测试类型从 " + version1.getTestType() + " 变更为 " + version2.getTestType());
        }
        
        if (!Objects.equals(version1.getPriority(), version2.getPriority())) {
            changes.add("优先级从 " + version1.getPriority() + " 变更为 " + version2.getPriority());
        }
        
        result.put("changes", changes);
        
        return result;
    }

    @Override
    @Transactional
    public Long rollbackToVersion(Long testCaseId, Long versionId, String createdBy) {
        log.debug("Rolling back test case: {} to version: {}", testCaseId, versionId);
        
        AlertRuleTemplateDependencyTestCaseVersion version = getVersionEntity(versionId);
        
        // 获取当前版本号并计算新版本号
        String currentVersionNumber = version.getVersionNumber();
        String newVersionNumber = incrementVersionNumber(currentVersionNumber);
        
        // 创建新版本
        return createVersion(
                testCaseId,
                newVersionNumber,
                "PATCH", // 回滚通常作为补丁版本
                version.getTestData(),
                version.getExpectedResult(),
                version.getTestType(),
                version.getPriority(),
                "回滚到版本 " + currentVersionNumber,
                createdBy
        );
    }

    @Override
    public Map<String, Object> getVersionStatistics(Long testCaseId) {
        log.debug("Getting version statistics for test case: {}", testCaseId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 统计版本数量
        long totalVersions = versionRepository.countByTestCaseId(testCaseId);
        result.put("totalVersions", totalVersions);
        
        // 按版本类型统计
        long majorVersions = versionRepository.countByTestCaseIdAndVersionType(testCaseId, "MAJOR");
        long minorVersions = versionRepository.countByTestCaseIdAndVersionType(testCaseId, "MINOR");
        long patchVersions = versionRepository.countByTestCaseIdAndVersionType(testCaseId, "PATCH");
        
        result.put("majorVersions", majorVersions);
        result.put("minorVersions", minorVersions);
        result.put("patchVersions", patchVersions);
        
        // 获取版本历史
        List<AlertRuleTemplateDependencyTestCaseVersion> versions = versionRepository
                .findByTestCaseIdOrderByCreatedAtDesc(testCaseId);
        
        // 计算版本变更频率
        if (versions.size() >= 2) {
            LocalDateTime firstVersionTime = versions.get(versions.size() - 1).getCreatedAt();
            LocalDateTime lastVersionTime = versions.get(0).getCreatedAt();
            
            long daysBetween = java.time.Duration.between(firstVersionTime, lastVersionTime).toDays();
            if (daysBetween > 0) {
                double versionsPerDay = (double) versions.size() / daysBetween;
                result.put("versionsPerDay", versionsPerDay);
            }
        }
        
        // 获取最新版本
        if (!versions.isEmpty()) {
            result.put("latestVersion", convertVersionToMap(versions.get(0)));
        }
        
        return result;
    }

    @Override
    public Map<String, Object> generateVersionChangeReport(Long testCaseId) {
        log.debug("Generating version change report for test case: {}", testCaseId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取版本历史
        List<AlertRuleTemplateDependencyTestCaseVersion> versions = versionRepository
                .findByTestCaseIdOrderByCreatedAtDesc(testCaseId);
        
        if (versions.isEmpty()) {
            result.put("message", "No version history found");
            return result;
        }
        
        // 基本信息
        result.put("testCaseId", testCaseId);
        result.put("totalVersions", versions.size());
        result.put("firstVersionDate", versions.get(versions.size() - 1).getCreatedAt());
        result.put("latestVersionDate", versions.get(0).getCreatedAt());
        
        // 版本变更历史
        List<Map<String, Object>> changeHistory = new ArrayList<>();
        
        for (int i = 0; i < versions.size() - 1; i++) {
            AlertRuleTemplateDependencyTestCaseVersion currentVersion = versions.get(i);
            AlertRuleTemplateDependencyTestCaseVersion previousVersion = versions.get(i + 1);
            
            Map<String, Object> change = new HashMap<>();
            change.put("fromVersion", previousVersion.getVersionNumber());
            change.put("toVersion", currentVersion.getVersionNumber());
            change.put("versionType", currentVersion.getVersionType());
            change.put("changeDescription", currentVersion.getChangeDescription());
            change.put("changedAt", currentVersion.getCreatedAt());
            change.put("changedBy", currentVersion.getCreatedBy());
            
            // 计算变更内容
            List<String> changes = new ArrayList<>();
            
            if (!Objects.equals(previousVersion.getTestData(), currentVersion.getTestData())) {
                changes.add("测试数据已变更");
            }
            
            if (!Objects.equals(previousVersion.getExpectedResult(), currentVersion.getExpectedResult())) {
                changes.add("预期结果已变更");
            }
            
            if (!Objects.equals(previousVersion.getTestType(), currentVersion.getTestType())) {
                changes.add("测试类型从 " + previousVersion.getTestType() + " 变更为 " + currentVersion.getTestType());
            }
            
            if (!Objects.equals(previousVersion.getPriority(), currentVersion.getPriority())) {
                changes.add("优先级从 " + previousVersion.getPriority() + " 变更为 " + currentVersion.getPriority());
            }
            
            change.put("changes", changes);
            changeHistory.add(change);
        }
        
        result.put("changeHistory", changeHistory);
        
        // 版本类型分布
        Map<String, Long> versionTypeDistribution = versions.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyTestCaseVersion::getVersionType,
                        Collectors.counting()
                ));
        
        result.put("versionTypeDistribution", versionTypeDistribution);
        
        // 变更频率分析
        if (versions.size() >= 2) {
            LocalDateTime firstVersionTime = versions.get(versions.size() - 1).getCreatedAt();
            LocalDateTime lastVersionTime = versions.get(0).getCreatedAt();
            
            long daysBetween = java.time.Duration.between(firstVersionTime, lastVersionTime).toDays();
            if (daysBetween > 0) {
                double versionsPerDay = (double) versions.size() / daysBetween;
                result.put("versionsPerDay", versionsPerDay);
                
                // 计算平均变更间隔（天）
                double averageDaysBetweenVersions = (double) daysBetween / (versions.size() - 1);
                result.put("averageDaysBetweenVersions", averageDaysBetweenVersions);
            }
        }
        
        return result;
    }

    @Override
    public Map<String, Object> validateVersion(Map<String, Object> versionData) {
        log.debug("Validating version data");
        
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (!versionData.containsKey("testCaseId") || versionData.get("testCaseId") == null) {
            errors.add("测试用例ID是必填项");
        }
        
        if (!versionData.containsKey("versionNumber") || versionData.get("versionNumber") == null) {
            errors.add("版本号是必填项");
        }
        
        if (!versionData.containsKey("versionType") || versionData.get("versionType") == null) {
            errors.add("版本类型是必填项");
        }
        
        // 验证版本类型
        if (versionData.containsKey("versionType") && versionData.get("versionType") != null) {
            String versionType = (String) versionData.get("versionType");
            if (!Arrays.asList("MAJOR", "MINOR", "PATCH").contains(versionType.toUpperCase())) {
                errors.add("无效的版本类型: " + versionType);
            }
        }
        
        // 验证版本号格式
        if (versionData.containsKey("versionNumber") && versionData.get("versionNumber") != null) {
            String versionNumber = (String) versionData.get("versionNumber");
            if (!versionNumber.matches("^\\d+\\.\\d+\\.\\d+$")) {
                errors.add("版本号格式无效: " + versionNumber + "，应为 x.y.z 格式");
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        
        return result;
    }

    private AlertRuleTemplateDependencyTestCaseVersion getVersionEntity(Long versionId) {
        return versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));
    }

    private Map<String, Object> convertVersionToMap(AlertRuleTemplateDependencyTestCaseVersion version) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", version.getId());
        result.put("testCaseId", version.getTestCaseId());
        result.put("versionNumber", version.getVersionNumber());
        result.put("versionType", version.getVersionType());
        result.put("testData", version.getTestData());
        result.put("expectedResult", version.getExpectedResult());
        result.put("testType", version.getTestType());
        result.put("priority", version.getPriority());
        result.put("changeDescription", version.getChangeDescription());
        result.put("createdBy", version.getCreatedBy());
        result.put("createdAt", version.getCreatedAt());
        result.put("updatedAt", version.getUpdatedAt());
        return result;
    }

    private String incrementVersionNumber(String currentVersion) {
        String[] parts = currentVersion.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid version number format: " + currentVersion);
        }
        
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);
        
        // 增加补丁版本号
        patch++;
        
        return major + "." + minor + "." + patch;
    }
} 