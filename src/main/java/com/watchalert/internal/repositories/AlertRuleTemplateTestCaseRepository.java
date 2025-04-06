package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTemplateTestCaseRepository extends JpaRepository<AlertRuleTemplateTestCase, Long> {
    
    /**
     * 根据模板ID查找测试用例
     *
     * @param templateId 模板ID
     * @return 测试用例列表
     */
    List<AlertRuleTemplateTestCase> findByTemplateId(Long templateId);
    
    /**
     * 根据模板ID和测试类型查找测试用例
     *
     * @param templateId 模板ID
     * @param testType 测试类型
     * @return 测试用例列表
     */
    List<AlertRuleTemplateTestCase> findByTemplateIdAndTestType(Long templateId, String testType);
    
    /**
     * 根据模板ID和状态查找测试用例
     *
     * @param templateId 模板ID
     * @param status 状态
     * @return 测试用例列表
     */
    List<AlertRuleTemplateTestCase> findByTemplateIdAndStatus(Long templateId, String status);
    
    /**
     * 根据模板ID和优先级查找测试用例
     *
     * @param templateId 模板ID
     * @param priority 优先级
     * @return 测试用例列表
     */
    List<AlertRuleTemplateTestCase> findByTemplateIdAndPriority(Long templateId, int priority);
    
    /**
     * 删除模板的所有测试用例
     *
     * @param templateId 模板ID
     */
    void deleteByTemplateId(Long templateId);
    
    /**
     * 统计模板的测试用例数量
     *
     * @param templateId 模板ID
     * @return 测试用例数量
     */
    long countByTemplateId(Long templateId);
    
    /**
     * 统计模板的测试用例状态
     *
     * @param templateId 模板ID
     * @param status 状态
     * @return 测试用例数量
     */
    long countByTemplateIdAndStatus(Long templateId, String status);
    
    /**
     * 根据模板ID和名称查找测试用例
     *
     * @param templateId 模板ID
     * @param name 测试用例名称
     * @return 测试用例
     */
    AlertRuleTemplateTestCase findByTemplateIdAndName(Long templateId, String name);
    
    /**
     * 检查测试用例名称是否存在
     *
     * @param templateId 模板ID
     * @param name 测试用例名称
     * @return 是否存在
     */
    boolean existsByTemplateIdAndName(Long templateId, String name);
    
    /**
     * 根据模板ID和执行结果查找测试用例
     *
     * @param templateId 模板ID
     * @param actualResult 实际执行结果
     * @return 测试用例列表
     */
    List<AlertRuleTemplateTestCase> findByTemplateIdAndActualResult(Long templateId, Boolean actualResult);
    
    /**
     * 根据模板ID查找最近执行的测试用例
     *
     * @param templateId 模板ID
     * @param limit 限制数量
     * @return 测试用例列表
     */
    List<AlertRuleTemplateTestCase> findTopByTemplateIdOrderByLastExecutedAtDesc(Long templateId, int limit);
} 