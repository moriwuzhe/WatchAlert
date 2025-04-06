package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCaseVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 告警规则模板依赖测试用例版本仓库接口
 */
@Repository
public interface AlertRuleTemplateDependencyTestCaseVersionRepository extends JpaRepository<AlertRuleTemplateDependencyTestCaseVersion, Long> {

    /**
     * 根据测试用例ID查询版本历史，按创建时间降序排序
     *
     * @param testCaseId 测试用例ID
     * @return 版本历史列表
     */
    List<AlertRuleTemplateDependencyTestCaseVersion> findByTestCaseIdOrderByCreatedAtDesc(Long testCaseId);

    /**
     * 根据测试用例ID和版本类型查询版本历史
     *
     * @param testCaseId 测试用例ID
     * @param versionType 版本类型
     * @return 版本历史列表
     */
    List<AlertRuleTemplateDependencyTestCaseVersion> findByTestCaseIdAndVersionType(Long testCaseId, String versionType);

    /**
     * 根据测试用例ID和版本号查询特定版本
     *
     * @param testCaseId 测试用例ID
     * @param versionNumber 版本号
     * @return 特定版本
     */
    AlertRuleTemplateDependencyTestCaseVersion findByTestCaseIdAndVersionNumber(Long testCaseId, String versionNumber);

    /**
     * 统计测试用例的版本数量
     *
     * @param testCaseId 测试用例ID
     * @return 版本数量
     */
    long countByTestCaseId(Long testCaseId);

    /**
     * 统计测试用例特定版本类型的版本数量
     *
     * @param testCaseId 测试用例ID
     * @param versionType 版本类型
     * @return 版本数量
     */
    long countByTestCaseIdAndVersionType(Long testCaseId, String versionType);
} 