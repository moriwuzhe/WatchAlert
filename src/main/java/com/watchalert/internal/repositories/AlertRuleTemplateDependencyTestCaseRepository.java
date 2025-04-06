package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRuleTemplateDependencyTestCaseRepository extends JpaRepository<AlertRuleTemplateDependencyTestCase, Long> {
    
    /**
     * 根据依赖ID查找测试用例
     */
    List<AlertRuleTemplateDependencyTestCase> findByDependencyId(Long dependencyId);
    
    /**
     * 根据依赖ID和测试类型查找测试用例
     */
    List<AlertRuleTemplateDependencyTestCase> findByDependencyIdAndTestType(Long dependencyId, String testType);
    
    /**
     * 根据依赖ID和状态查找测试用例
     */
    List<AlertRuleTemplateDependencyTestCase> findByDependencyIdAndStatus(Long dependencyId, String status);
    
    /**
     * 根据依赖ID和优先级查找测试用例
     */
    List<AlertRuleTemplateDependencyTestCase> findByDependencyIdAndPriority(Long dependencyId, int priority);
    
    /**
     * 删除依赖的所有测试用例
     */
    void deleteByDependencyId(Long dependencyId);
    
    /**
     * 统计依赖的测试用例数量
     */
    long countByDependencyId(Long dependencyId);
    
    /**
     * 统计依赖的测试用例状态数量
     */
    long countByDependencyIdAndStatus(Long dependencyId, String status);
} 