package com.watchalert.internal.services;

import java.util.List;
import java.util.Map;

/**
 * 告警规则模板依赖测试用例关系服务接口
 */
public interface AlertRuleTemplateDependencyTestCaseRelationService {

    /**
     * 创建依赖关系
     *
     * @param sourceTestCaseId 源测试用例ID
     * @param targetTestCaseId 目标测试用例ID
     * @param dependencyType 依赖类型
     * @param description 依赖描述
     * @param createdBy 创建人
     * @return 依赖关系ID
     */
    Long createRelation(Long sourceTestCaseId, Long targetTestCaseId, String dependencyType, String description, String createdBy);

    /**
     * 更新依赖关系
     *
     * @param relationId 依赖关系ID
     * @param dependencyType 依赖类型
     * @param description 依赖描述
     * @return 更新后的依赖关系
     */
    Map<String, Object> updateRelation(Long relationId, String dependencyType, String description);

    /**
     * 删除依赖关系
     *
     * @param relationId 依赖关系ID
     */
    void deleteRelation(Long relationId);

    /**
     * 获取依赖关系
     *
     * @param relationId 依赖关系ID
     * @return 依赖关系详情
     */
    Map<String, Object> getRelation(Long relationId);

    /**
     * 获取源测试用例的依赖关系
     *
     * @param sourceTestCaseId 源测试用例ID
     * @return 依赖关系列表
     */
    List<Map<String, Object>> getSourceRelations(Long sourceTestCaseId);

    /**
     * 获取目标测试用例的依赖关系
     *
     * @param targetTestCaseId 目标测试用例ID
     * @return 依赖关系列表
     */
    List<Map<String, Object>> getTargetRelations(Long targetTestCaseId);

    /**
     * 检查是否存在循环依赖
     *
     * @param testCaseId 测试用例ID
     * @return 是否存在循环依赖
     */
    boolean hasCircularDependency(Long testCaseId);

    /**
     * 获取依赖关系图数据
     *
     * @param testCaseId 测试用例ID
     * @return 依赖关系图数据
     */
    Map<String, Object> getDependencyGraphData(Long testCaseId);

    /**
     * 获取依赖关系统计信息
     *
     * @param testCaseId 测试用例ID
     * @return 统计信息
     */
    Map<String, Object> getDependencyStatistics(Long testCaseId);

    /**
     * 分析依赖影响
     *
     * @param testCaseId 测试用例ID
     * @return 依赖影响分析结果
     */
    Map<String, Object> analyzeDependencyImpact(Long testCaseId);

    /**
     * 获取依赖建议
     *
     * @param testCaseId 测试用例ID
     * @return 依赖建议
     */
    List<Map<String, Object>> getDependencySuggestions(Long testCaseId);
} 