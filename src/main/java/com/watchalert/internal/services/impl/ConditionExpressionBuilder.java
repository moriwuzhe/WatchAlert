package com.watchalert.internal.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ConditionExpressionBuilder {

    /**
     * 构建数值比较表达式
     *
     * @param field 字段名
     * @param operator 操作符
     * @param value 值
     * @return SpEL表达式
     */
    public String buildNumericExpression(String field, String operator, Number value) {
        return String.format("%s %s %s", field, operator, value);
    }

    /**
     * 构建字符串比较表达式
     *
     * @param field 字段名
     * @param operator 操作符
     * @param value 值
     * @return SpEL表达式
     */
    public String buildStringExpression(String field, String operator, String value) {
        switch (operator.toLowerCase()) {
            case "contains":
                return String.format("%s.contains('%s')", field, value);
            case "startswith":
                return String.format("%s.startsWith('%s')", field, value);
            case "endswith":
                return String.format("%s.endsWith('%s')", field, value);
            case "matches":
                return String.format("%s.matches('%s')", field, value);
            case "==":
            case "=":
                return String.format("%s == '%s'", field, value);
            case "!=":
                return String.format("%s != '%s'", field, value);
            default:
                log.error("Unsupported operator: {}", operator);
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    /**
     * 构建复合条件表达式
     *
     * @param conditions 条件列表
     * @param operator 逻辑操作符（AND/OR）
     * @return SpEL表达式
     */
    public String buildCompoundExpression(List<String> conditions, String operator) {
        if (conditions == null || conditions.isEmpty()) {
            return "true";
        }

        if (conditions.size() == 1) {
            return conditions.get(0);
        }

        List<String> expressions = new ArrayList<>();
        for (String condition : conditions) {
            expressions.add("(" + condition + ")");
        }

        String logicalOperator = operator.toUpperCase().equals("AND") ? " and " : " or ";
        return String.join(logicalOperator, expressions);
    }

    /**
     * 构建时间范围表达式
     *
     * @param field 时间字段名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return SpEL表达式
     */
    public String buildTimeRangeExpression(String field, String startTime, String endTime) {
        return String.format("%s >= '%s' and %s <= '%s'", field, startTime, field, endTime);
    }

    /**
     * 构建数组包含表达式
     *
     * @param field 数组字段名
     * @param value 值
     * @return SpEL表达式
     */
    public String buildArrayContainsExpression(String field, String value) {
        return String.format("%s.?[contains(.,'%s')].size() > 0", field, value);
    }
} 