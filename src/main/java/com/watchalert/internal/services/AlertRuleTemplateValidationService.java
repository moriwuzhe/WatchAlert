package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTemplate;
import java.util.Map;

public interface AlertRuleTemplateValidationService {
    /**
     * 验证告警规则模板
     *
     * @param template 要验证的模板
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateTemplate(AlertRuleTemplate template);

    /**
     * 验证模板的查询语句
     *
     * @param template 要验证的模板
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateQuery(AlertRuleTemplate template);

    /**
     * 验证模板的条件表达式
     *
     * @param template 要验证的模板
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateCondition(AlertRuleTemplate template);

    /**
     * 验证模板的通知配置
     *
     * @param template 要验证的模板
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateNotification(AlertRuleTemplate template);

    /**
     * 验证模板的变量引用
     *
     * @param template 要验证的模板
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateVariables(AlertRuleTemplate template);

    /**
     * 执行模板测试
     *
     * @param template 要测试的模板
     * @param testData 测试数据
     * @return 测试结果
     */
    Map<String, Object> executeTest(AlertRuleTemplate template, Map<String, Object> testData);
} 