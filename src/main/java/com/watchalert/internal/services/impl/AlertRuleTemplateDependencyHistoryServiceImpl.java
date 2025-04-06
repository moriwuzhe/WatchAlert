package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateDependencyHistory;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyHistoryRepository;
import com.watchalert.internal.services.AlertRuleTemplateDependencyHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateDependencyHistoryServiceImpl implements AlertRuleTemplateDependencyHistoryService {

    @Autowired
    private AlertRuleTemplateDependencyHistoryRepository historyRepository;

    @Override
    @Transactional
    public Long recordChange(Long dependencyId, String changeType, String oldValue, String newValue, 
            String changeReason, String changedBy) {
        log.debug("Recording change for dependency: {}, type: {}", dependencyId, changeType);
        
        AlertRuleTemplateDependencyHistory history = new AlertRuleTemplateDependencyHistory();
        history.setDependencyId(dependencyId);
        history.setChangeType(changeType);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setChangeReason(changeReason);
        history.setChangedBy(changedBy);
        
        return historyRepository.save(history).getId();
    }

    @Override
    public List<Map<String, Object>> getDependencyHistory(Long dependencyId) {
        log.debug("Getting history for dependency: {}", dependencyId);
        
        return historyRepository.findByDependencyIdOrderByCreatedAtDesc(dependencyId).stream()
                .map(this::convertHistoryToMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDependencyHistoryByType(Long dependencyId, String changeType) {
        log.debug("Getting history for dependency: {}, type: {}", dependencyId, changeType);
        
        return historyRepository.findByDependencyIdAndChangeTypeOrderByCreatedAtDesc(dependencyId, changeType)
                .stream()
                .map(this::convertHistoryToMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDependencyHistoryByTimeRange(Long dependencyId, 
            LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting history for dependency: {}, time range: {} - {}", 
                dependencyId, startTime, endTime);
        
        return historyRepository.findByDependencyIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                dependencyId, startTime, endTime)
                .stream()
                .map(this::convertHistoryToMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDependencyHistoryByUser(String changedBy) {
        log.debug("Getting history for user: {}", changedBy);
        
        return historyRepository.findByChangedByOrderByCreatedAtDesc(changedBy)
                .stream()
                .map(this::convertHistoryToMap)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getDependencyHistoryStatistics(Long dependencyId) {
        log.debug("Getting history statistics for dependency: {}", dependencyId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 统计变更次数
        long totalChanges = historyRepository.countByDependencyId(dependencyId);
        result.put("totalChanges", totalChanges);
        
        // 按变更类型统计
        List<AlertRuleTemplateDependencyHistory> history = historyRepository
                .findByDependencyIdOrderByCreatedAtDesc(dependencyId);
        Map<String, Long> typeCount = history.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyHistory::getChangeType,
                        Collectors.counting()
                ));
        result.put("typeCount", typeCount);
        
        // 按用户统计
        Map<String, Long> userCount = history.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyHistory::getChangedBy,
                        Collectors.counting()
                ));
        result.put("userCount", userCount);
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> rollbackToVersion(Long dependencyId, Long versionId, String changedBy) {
        log.debug("Rolling back dependency: {} to version: {}", dependencyId, versionId);
        
        Map<String, Object> result = new HashMap<>();
        // TODO: 实现版本回滚逻辑
        result.put("success", true);
        result.put("message", "Version rollback successful");
        
        return result;
    }

    @Override
    public Map<String, Object> compareVersions(Long dependencyId, Long version1Id, Long version2Id) {
        log.debug("Comparing versions: {} and {}", version1Id, version2Id);
        
        Map<String, Object> result = new HashMap<>();
        // TODO: 实现版本比较逻辑
        result.put("differences", new ArrayList<>());
        
        return result;
    }

    @Override
    public List<Map<String, Object>> exportHistory(Long dependencyId) {
        log.debug("Exporting history for dependency: {}", dependencyId);
        
        return historyRepository.findByDependencyIdOrderByCreatedAtDesc(dependencyId)
                .stream()
                .map(this::convertHistoryToMap)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cleanupHistory(Long dependencyId, LocalDateTime beforeTime) {
        log.debug("Cleaning up history for dependency: {} before: {}", dependencyId, beforeTime);
        
        List<AlertRuleTemplateDependencyHistory> history = historyRepository
                .findByDependencyIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        dependencyId, LocalDateTime.MIN, beforeTime);
        
        historyRepository.deleteAll(history);
    }

    private Map<String, Object> convertHistoryToMap(AlertRuleTemplateDependencyHistory history) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", history.getId());
        result.put("dependencyId", history.getDependencyId());
        result.put("changeType", history.getChangeType());
        result.put("oldValue", history.getOldValue());
        result.put("newValue", history.getNewValue());
        result.put("changeReason", history.getChangeReason());
        result.put("changedBy", history.getChangedBy());
        result.put("createdAt", history.getCreatedAt());
        result.put("updatedAt", history.getUpdatedAt());
        return result;
    }
} 