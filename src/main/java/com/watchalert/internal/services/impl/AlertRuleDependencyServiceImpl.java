package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertRuleDependency;
import com.watchalert.internal.repositories.AlertRuleRepository;
import com.watchalert.internal.repositories.AlertRuleDependencyRepository;
import com.watchalert.internal.services.AlertRuleDependencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

@Slf4j
@Service
public class AlertRuleDependencyServiceImpl implements AlertRuleDependencyService {

    @Autowired
    private AlertRuleRepository ruleRepository;

    @Autowired
    private AlertRuleDependencyRepository dependencyRepository;

    @Override
    @Transactional
    public AlertRuleDependency createDependency(Long ruleId, Long dependentRuleId, 
            String dependencyType, int priority, String createdBy, String comment) {
        log.debug("Creating dependency between rules {} and {}", ruleId, dependentRuleId);
        
        // 检查规则是否存在
        AlertRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found: " + ruleId));
        AlertRule dependentRule = ruleRepository.findById(dependentRuleId)
                .orElseThrow(() -> new RuntimeException("Dependent rule not found: " + dependentRuleId));
        
        // 检查是否已存在依赖关系
        List<AlertRuleDependency> existingDependencies = dependencyRepository
                .findByRuleIdAndDependentRuleId(ruleId, dependentRuleId);
        if (!existingDependencies.isEmpty()) {
            throw new RuntimeException("Dependency already exists");
        }
        
        // 检查循环依赖
        if (hasCircularDependency(ruleId, dependentRuleId)) {
            throw new RuntimeException("Circular dependency detected");
        }
        
        // 创建依赖关系
        AlertRuleDependency dependency = new AlertRuleDependency();
        dependency.setRule(rule);
        dependency.setDependentRule(dependentRule);
        dependency.setDependencyType(dependencyType);
        dependency.setPriority(priority);
        dependency.setEnabled(true);
        dependency.setCreatedBy(createdBy);
        dependency.setComment(comment);
        
        return dependencyRepository.save(dependency);
    }

    @Override
    @Transactional
    public void updateDependency(AlertRuleDependency dependency) {
        log.debug("Updating dependency: {}", dependency.getId());
        
        AlertRuleDependency existingDependency = dependencyRepository.findById(dependency.getId())
                .orElseThrow(() -> new RuntimeException("Dependency not found: " + dependency.getId()));
        
        existingDependency.setDependencyType(dependency.getDependencyType());
        existingDependency.setPriority(dependency.getPriority());
        existingDependency.setEnabled(dependency.isEnabled());
        existingDependency.setComment(dependency.getComment());
        
        dependencyRepository.save(existingDependency);
    }

    @Override
    @Transactional
    public void deleteDependency(Long id) {
        log.debug("Deleting dependency: {}", id);
        dependencyRepository.deleteById(id);
    }

    @Override
    public List<AlertRuleDependency> getDependencies(Long ruleId) {
        log.debug("Getting dependencies for rule: {}", ruleId);
        return dependencyRepository.findByRuleIdOrderByPriorityAsc(ruleId);
    }

    @Override
    public List<AlertRuleDependency> getDependentRules(Long ruleId) {
        log.debug("Getting dependent rules for rule: {}", ruleId);
        return dependencyRepository.findByDependentRuleIdOrderByPriorityAsc(ruleId);
    }

    @Override
    public Map<String, Object> getDependencyGraph(Long ruleId) {
        log.debug("Getting dependency graph for rule: {}", ruleId);
        
        Map<String, Object> graph = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        Map<Long, Map<String, Object>> nodes = new HashMap<>();
        List<Map<String, Object>> edges = new ArrayList<>();
        
        // 使用BFS遍历依赖图
        Queue<Long> queue = new LinkedList<>();
        queue.offer(ruleId);
        visited.add(ruleId);
        
        while (!queue.isEmpty()) {
            Long currentRuleId = queue.poll();
            
            // 获取当前规则的所有依赖
            List<AlertRuleDependency> dependencies = getDependencies(currentRuleId);
            for (AlertRuleDependency dependency : dependencies) {
                Long dependentRuleId = dependency.getDependentRule().getId();
                
                // 添加节点
                if (!nodes.containsKey(dependentRuleId)) {
                    AlertRule rule = dependency.getDependentRule();
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", dependentRuleId);
                    node.put("name", rule.getName());
                    node.put("severity", rule.getSeverity());
                    nodes.put(dependentRuleId, node);
                }
                
                // 添加边
                Map<String, Object> edge = new HashMap<>();
                edge.put("from", currentRuleId);
                edge.put("to", dependentRuleId);
                edge.put("type", dependency.getDependencyType());
                edge.put("priority", dependency.getPriority());
                edges.add(edge);
                
                // 继续遍历
                if (!visited.contains(dependentRuleId)) {
                    queue.offer(dependentRuleId);
                    visited.add(dependentRuleId);
                }
            }
        }
        
        graph.put("nodes", nodes.values());
        graph.put("edges", edges);
        return graph;
    }

    @Override
    public boolean hasCircularDependency(Long ruleId, Long dependentRuleId) {
        log.debug("Checking circular dependency between rules {} and {}", ruleId, dependentRuleId);
        
        Set<Long> visited = new HashSet<>();
        Set<Long> recursionStack = new HashSet<>();
        
        return hasCircularDependencyUtil(dependentRuleId, visited, recursionStack);
    }

    private boolean hasCircularDependencyUtil(Long ruleId, Set<Long> visited, Set<Long> recursionStack) {
        if (recursionStack.contains(ruleId)) {
            return true;
        }
        
        if (visited.contains(ruleId)) {
            return false;
        }
        
        visited.add(ruleId);
        recursionStack.add(ruleId);
        
        List<AlertRuleDependency> dependencies = getDependencies(ruleId);
        for (AlertRuleDependency dependency : dependencies) {
            Long dependentRuleId = dependency.getDependentRule().getId();
            if (hasCircularDependencyUtil(dependentRuleId, visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack.remove(ruleId);
        return false;
    }

    @Override
    @Transactional
    public void setDependencyEnabled(Long id, boolean enabled) {
        log.debug("Setting dependency {} enabled to {}", id, enabled);
        
        AlertRuleDependency dependency = dependencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependency not found: " + id));
        
        dependency.setEnabled(enabled);
        dependencyRepository.save(dependency);
    }
} 