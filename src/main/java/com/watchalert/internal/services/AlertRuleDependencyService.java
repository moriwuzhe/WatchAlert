package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleDependency;
import java.util.List;
import java.util.Map;

public interface AlertRuleDependencyService {
    /**
     * 创建告警规则依赖关系
     *
     * @param ruleId 告警规则ID
     * @param dependentRuleId 依赖的告警规则ID
     * @param dependencyType 依赖类型（AND/OR）
     * @param priority 优先级
     * @param createdBy 创建人
     * @param comment 说明
     * @return 创建的依赖关系
     */
    AlertRuleDependency createDependency(Long ruleId, Long dependentRuleId, 
            String dependencyType, int priority, String createdBy, String comment);

    /**
     * 更新告警规则依赖关系
     *
     * @param dependency 依赖关系
     */
    void updateDependency(AlertRuleDependency dependency);

    /**
     * 删除告警规则依赖关系
     *
     * @param id 依赖关系ID
     */
    void deleteDependency(Long id);

    /**
     * 获取告警规则的所有依赖关系
     *
     * @param ruleId 告警规则ID
     * @return 依赖关系列表
     */
    List<AlertRuleDependency> getDependencies(Long ruleId);

    /**
     * 获取依赖该告警规则的所有规则
     *
     * @param ruleId 告警规则ID
     * @return 依赖关系列表
     */
    List<AlertRuleDependency> getDependentRules(Long ruleId);

    /**
     * 获取告警规则的依赖图
     *
     * @param ruleId 告警规则ID
     * @return 依赖图信息
     */
    Map<String, Object> getDependencyGraph(Long ruleId);

    /**
     * 检查是否存在循环依赖
     *
     * @param ruleId 告警规则ID
     * @param dependentRuleId 依赖的告警规则ID
     * @return 是否存在循环依赖
     */
    boolean hasCircularDependency(Long ruleId, Long dependentRuleId);

    /**
     * 启用/禁用依赖关系
     *
     * @param id 依赖关系ID
     * @param enabled 是否启用
     */
    void setDependencyEnabled(Long id, boolean enabled);
} 