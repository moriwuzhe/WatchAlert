package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCaseRelation;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyTestCaseRelationRepository;
import com.watchalert.internal.services.AlertRuleTemplateDependencyTestCaseRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateDependencyTestCaseRelationServiceImpl implements AlertRuleTemplateDependencyTestCaseRelationService {

    @Autowired
    private AlertRuleTemplateDependencyTestCaseRelationRepository relationRepository;

    @Override
    @Transactional
    public Long createRelation(Long sourceTestCaseId, Long targetTestCaseId, String dependencyType, String description, String createdBy) {
        log.debug("Creating dependency relation: {} -> {}", sourceTestCaseId, targetTestCaseId);
        
        // 检查是否已存在相同的依赖关系
        AlertRuleTemplateDependencyTestCaseRelation existingRelation = relationRepository
                .findBySourceTestCaseIdAndTargetTestCaseId(sourceTestCaseId, targetTestCaseId);
        
        if (existingRelation != null) {
            throw new IllegalArgumentException("Dependency relation already exists");
        }
        
        // 检查是否形成循环依赖
        if (wouldCreateCircularDependency(sourceTestCaseId, targetTestCaseId)) {
            throw new IllegalArgumentException("Circular dependency detected");
        }
        
        AlertRuleTemplateDependencyTestCaseRelation relation = new AlertRuleTemplateDependencyTestCaseRelation();
        relation.setSourceTestCaseId(sourceTestCaseId);
        relation.setTargetTestCaseId(targetTestCaseId);
        relation.setDependencyType(dependencyType);
        relation.setDescription(description);
        relation.setCreatedBy(createdBy);
        
        return relationRepository.save(relation).getId();
    }

    @Override
    @Transactional
    public Map<String, Object> updateRelation(Long relationId, String dependencyType, String description) {
        log.debug("Updating dependency relation: {}", relationId);
        
        AlertRuleTemplateDependencyTestCaseRelation relation = getRelationEntity(relationId);
        relation.setDependencyType(dependencyType);
        relation.setDescription(description);
        
        return convertRelationToMap(relationRepository.save(relation));
    }

    @Override
    @Transactional
    public void deleteRelation(Long relationId) {
        log.debug("Deleting dependency relation: {}", relationId);
        relationRepository.deleteById(relationId);
    }

    @Override
    public Map<String, Object> getRelation(Long relationId) {
        log.debug("Getting dependency relation: {}", relationId);
        return convertRelationToMap(getRelationEntity(relationId));
    }

    @Override
    public List<Map<String, Object>> getSourceRelations(Long sourceTestCaseId) {
        log.debug("Getting source relations for test case: {}", sourceTestCaseId);
        
        return relationRepository.findBySourceTestCaseId(sourceTestCaseId).stream()
                .map(this::convertRelationToMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTargetRelations(Long targetTestCaseId) {
        log.debug("Getting target relations for test case: {}", targetTestCaseId);
        
        return relationRepository.findByTargetTestCaseId(targetTestCaseId).stream()
                .map(this::convertRelationToMap)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasCircularDependency(Long testCaseId) {
        log.debug("Checking circular dependency for test case: {}", testCaseId);
        
        Set<Long> visited = new HashSet<>();
        Set<Long> recursionStack = new HashSet<>();
        
        return hasCircularDependencyUtil(testCaseId, visited, recursionStack);
    }

    @Override
    public Map<String, Object> getDependencyGraphData(Long testCaseId) {
        log.debug("Getting dependency graph data for test case: {}", testCaseId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有相关的依赖关系
        List<AlertRuleTemplateDependencyTestCaseRelation> sourceRelations = relationRepository.findBySourceTestCaseId(testCaseId);
        List<AlertRuleTemplateDependencyTestCaseRelation> targetRelations = relationRepository.findByTargetTestCaseId(testCaseId);
        
        // 构建节点数据
        Set<Long> nodes = new HashSet<>();
        nodes.add(testCaseId);
        sourceRelations.forEach(relation -> nodes.add(relation.getTargetTestCaseId()));
        targetRelations.forEach(relation -> nodes.add(relation.getSourceTestCaseId()));
        
        // 构建边数据
        List<Map<String, Object>> edges = new ArrayList<>();
        
        sourceRelations.forEach(relation -> {
            Map<String, Object> edge = new HashMap<>();
            edge.put("source", relation.getSourceTestCaseId());
            edge.put("target", relation.getTargetTestCaseId());
            edge.put("type", relation.getDependencyType());
            edges.add(edge);
        });
        
        targetRelations.forEach(relation -> {
            Map<String, Object> edge = new HashMap<>();
            edge.put("source", relation.getSourceTestCaseId());
            edge.put("target", relation.getTargetTestCaseId());
            edge.put("type", relation.getDependencyType());
            edges.add(edge);
        });
        
        result.put("nodes", nodes);
        result.put("edges", edges);
        
        return result;
    }

    @Override
    public Map<String, Object> getDependencyStatistics(Long testCaseId) {
        log.debug("Getting dependency statistics for test case: {}", testCaseId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 统计依赖数量
        long sourceCount = relationRepository.countBySourceTestCaseId(testCaseId);
        long targetCount = relationRepository.countByTargetTestCaseId(testCaseId);
        
        result.put("totalDependencies", sourceCount + targetCount);
        result.put("sourceDependencies", sourceCount);
        result.put("targetDependencies", targetCount);
        
        // 按依赖类型统计
        List<AlertRuleTemplateDependencyTestCaseRelation> allRelations = new ArrayList<>();
        allRelations.addAll(relationRepository.findBySourceTestCaseId(testCaseId));
        allRelations.addAll(relationRepository.findByTargetTestCaseId(testCaseId));
        
        Map<String, Long> typeDistribution = allRelations.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyTestCaseRelation::getDependencyType,
                        Collectors.counting()
                ));
        
        result.put("typeDistribution", typeDistribution);
        
        return result;
    }

    @Override
    public Map<String, Object> analyzeDependencyImpact(Long testCaseId) {
        log.debug("Analyzing dependency impact for test case: {}", testCaseId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有相关的依赖关系
        List<AlertRuleTemplateDependencyTestCaseRelation> sourceRelations = relationRepository.findBySourceTestCaseId(testCaseId);
        List<AlertRuleTemplateDependencyTestCaseRelation> targetRelations = relationRepository.findByTargetTestCaseId(testCaseId);
        
        // 分析直接依赖
        result.put("directDependencies", sourceRelations.size());
        result.put("directDependents", targetRelations.size());
        
        // 分析间接依赖
        Set<Long> indirectDependencies = new HashSet<>();
        Set<Long> indirectDependents = new HashSet<>();
        
        // 递归查找间接依赖
        sourceRelations.forEach(relation -> 
            findIndirectDependencies(relation.getTargetTestCaseId(), indirectDependencies));
        
        // 递归查找间接依赖者
        targetRelations.forEach(relation -> 
            findIndirectDependents(relation.getSourceTestCaseId(), indirectDependents));
        
        result.put("indirectDependencies", indirectDependencies.size());
        result.put("indirectDependents", indirectDependents.size());
        
        // 分析依赖链
        List<List<Long>> dependencyChains = findDependencyChains(testCaseId);
        result.put("dependencyChains", dependencyChains);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getDependencySuggestions(Long testCaseId) {
        log.debug("Getting dependency suggestions for test case: {}", testCaseId);
        
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        // 获取所有相关的依赖关系
        List<AlertRuleTemplateDependencyTestCaseRelation> sourceRelations = relationRepository.findBySourceTestCaseId(testCaseId);
        List<AlertRuleTemplateDependencyTestCaseRelation> targetRelations = relationRepository.findByTargetTestCaseId(testCaseId);
        
        // 分析依赖类型分布
        Map<String, Long> typeDistribution = new HashMap<>();
        sourceRelations.forEach(relation -> 
            typeDistribution.merge(relation.getDependencyType(), 1L, Long::sum));
        
        // 根据依赖类型分布提供建议
        if (typeDistribution.getOrDefault("PREREQUISITE", 0L) == 0) {
            Map<String, Object> suggestion = new HashMap<>();
            suggestion.put("type", "MISSING_PREREQUISITE");
            suggestion.put("description", "建议添加前置依赖测试用例");
            suggestion.put("priority", "HIGH");
            suggestions.add(suggestion);
        }
        
        // 检查是否存在重复依赖
        Set<Long> targetIds = new HashSet<>();
        for (AlertRuleTemplateDependencyTestCaseRelation relation : sourceRelations) {
            if (!targetIds.add(relation.getTargetTestCaseId())) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("type", "DUPLICATE_DEPENDENCY");
                suggestion.put("description", "发现重复的依赖关系");
                suggestion.put("priority", "MEDIUM");
                suggestions.add(suggestion);
                break;
            }
        }
        
        // 检查依赖链长度
        List<List<Long>> dependencyChains = findDependencyChains(testCaseId);
        if (dependencyChains.stream().anyMatch(chain -> chain.size() > 5)) {
            Map<String, Object> suggestion = new HashMap<>();
            suggestion.put("type", "LONG_DEPENDENCY_CHAIN");
            suggestion.put("description", "依赖链过长，建议优化依赖结构");
            suggestion.put("priority", "MEDIUM");
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }

    private AlertRuleTemplateDependencyTestCaseRelation getRelationEntity(Long relationId) {
        return relationRepository.findById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("Relation not found: " + relationId));
    }

    private Map<String, Object> convertRelationToMap(AlertRuleTemplateDependencyTestCaseRelation relation) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", relation.getId());
        result.put("sourceTestCaseId", relation.getSourceTestCaseId());
        result.put("targetTestCaseId", relation.getTargetTestCaseId());
        result.put("dependencyType", relation.getDependencyType());
        result.put("description", relation.getDescription());
        result.put("createdBy", relation.getCreatedBy());
        result.put("createdAt", relation.getCreatedAt());
        result.put("updatedAt", relation.getUpdatedAt());
        return result;
    }

    private boolean wouldCreateCircularDependency(Long sourceId, Long targetId) {
        Set<Long> visited = new HashSet<>();
        Set<Long> recursionStack = new HashSet<>();
        
        // 从目标节点开始检查是否会形成循环
        return hasCircularDependencyUtil(targetId, visited, recursionStack);
    }

    private boolean hasCircularDependencyUtil(Long testCaseId, Set<Long> visited, Set<Long> recursionStack) {
        if (recursionStack.contains(testCaseId)) {
            return true;
        }
        
        if (visited.contains(testCaseId)) {
            return false;
        }
        
        visited.add(testCaseId);
        recursionStack.add(testCaseId);
        
        List<AlertRuleTemplateDependencyTestCaseRelation> relations = relationRepository.findBySourceTestCaseId(testCaseId);
        
        for (AlertRuleTemplateDependencyTestCaseRelation relation : relations) {
            if (hasCircularDependencyUtil(relation.getTargetTestCaseId(), visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack.remove(testCaseId);
        return false;
    }

    private void findIndirectDependencies(Long testCaseId, Set<Long> dependencies) {
        List<AlertRuleTemplateDependencyTestCaseRelation> relations = relationRepository.findBySourceTestCaseId(testCaseId);
        
        for (AlertRuleTemplateDependencyTestCaseRelation relation : relations) {
            Long targetId = relation.getTargetTestCaseId();
            if (dependencies.add(targetId)) {
                findIndirectDependencies(targetId, dependencies);
            }
        }
    }

    private void findIndirectDependents(Long testCaseId, Set<Long> dependents) {
        List<AlertRuleTemplateDependencyTestCaseRelation> relations = relationRepository.findByTargetTestCaseId(testCaseId);
        
        for (AlertRuleTemplateDependencyTestCaseRelation relation : relations) {
            Long sourceId = relation.getSourceTestCaseId();
            if (dependents.add(sourceId)) {
                findIndirectDependents(sourceId, dependents);
            }
        }
    }

    private List<List<Long>> findDependencyChains(Long testCaseId) {
        List<List<Long>> chains = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        List<Long> currentChain = new ArrayList<>();
        
        findDependencyChainsUtil(testCaseId, visited, currentChain, chains);
        
        return chains;
    }

    private void findDependencyChainsUtil(Long testCaseId, Set<Long> visited, List<Long> currentChain, List<List<Long>> chains) {
        currentChain.add(testCaseId);
        visited.add(testCaseId);
        
        List<AlertRuleTemplateDependencyTestCaseRelation> relations = relationRepository.findBySourceTestCaseId(testCaseId);
        
        if (relations.isEmpty()) {
            chains.add(new ArrayList<>(currentChain));
        } else {
            for (AlertRuleTemplateDependencyTestCaseRelation relation : relations) {
                Long targetId = relation.getTargetTestCaseId();
                if (!visited.contains(targetId)) {
                    findDependencyChainsUtil(targetId, visited, currentChain, chains);
                }
            }
        }
        
        currentChain.remove(currentChain.size() - 1);
        visited.remove(testCaseId);
    }
} 