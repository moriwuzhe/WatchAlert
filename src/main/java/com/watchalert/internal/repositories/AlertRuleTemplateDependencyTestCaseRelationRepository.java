package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 告警规则模板依赖测试用例关系仓库接口
 */
@Repository
public interface AlertRuleTemplateDependencyTestCaseRelationRepository extends JpaRepository<AlertRuleTemplateDependencyTestCaseRelation, Long> {

    /**
     * 根据源测试用例ID查询依赖关系
     *
     * @param sourceTestCaseId 源测试用例ID
     * @return 依赖关系列表
     */
    List<AlertRuleTemplateDependencyTestCaseRelation> findBySourceTestCaseId(Long sourceTestCaseId);

    /**
     * 根据目标测试用例ID查询依赖关系
     *
     * @param targetTestCaseId 目标测试用例ID
     * @return 依赖关系列表
     */
    List<AlertRuleTemplateDependencyTestCaseRelation> findByTargetTestCaseId(Long targetTestCaseId);

    /**
     * 根据源测试用例ID和依赖类型查询依赖关系
     *
     * @param sourceTestCaseId 源测试用例ID
     * @param dependencyType 依赖类型
     * @return 依赖关系列表
     */
    List<AlertRuleTemplateDependencyTestCaseRelation> findBySourceTestCaseIdAndDependencyType(Long sourceTestCaseId, String dependencyType);

    /**
     * 根据目标测试用例ID和依赖类型查询依赖关系
     *
     * @param targetTestCaseId 目标测试用例ID
     * @param dependencyType 依赖类型
     * @return 依赖关系列表
     */
    List<AlertRuleTemplateDependencyTestCaseRelation> findByTargetTestCaseIdAndDependencyType(Long targetTestCaseId, String dependencyType);

    /**
     * 根据源测试用例ID和目标测试用例ID查询依赖关系
     *
     * @param sourceTestCaseId 源测试用例ID
     * @param targetTestCaseId 目标测试用例ID
     * @return 依赖关系
     */
    AlertRuleTemplateDependencyTestCaseRelation findBySourceTestCaseIdAndTargetTestCaseId(Long sourceTestCaseId, Long targetTestCaseId);

    /**
     * 统计源测试用例的依赖数量
     *
     * @param sourceTestCaseId 源测试用例ID
     * @return 依赖数量
     */
    long countBySourceTestCaseId(Long sourceTestCaseId);

    /**
     * 统计目标测试用例的依赖数量
     *
     * @param targetTestCaseId 目标测试用例ID
     * @return 依赖数量
     */
    long countByTargetTestCaseId(Long targetTestCaseId);

    /**
     * 删除源测试用例的所有依赖关系
     *
     * @param sourceTestCaseId 源测试用例ID
     */
    void deleteBySourceTestCaseId(Long sourceTestCaseId);

    /**
     * 删除目标测试用例的所有依赖关系
     *
     * @param targetTestCaseId 目标测试用例ID
     */
    void deleteByTargetTestCaseId(Long targetTestCaseId);
} 