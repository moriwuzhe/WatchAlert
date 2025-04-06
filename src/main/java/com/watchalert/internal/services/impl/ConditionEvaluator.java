package com.watchalert.internal.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ConditionEvaluator {

    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 评估条件
     *
     * @param data 数据
     * @param condition 条件表达式
     * @return 是否满足条件
     */
    public boolean evaluateCondition(Map<String, Object> data, String condition) {
        log.debug("Evaluating condition: {} with data: {}", condition, data);
        
        try {
            // 创建评估上下文
            EvaluationContext context = new StandardEvaluationContext();
            
            // 将数据添加到上下文中
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
            
            // 解析并评估表达式
            Expression expression = parser.parseExpression(condition);
            Boolean result = expression.getValue(context, Boolean.class);
            
            log.debug("Condition evaluation result: {}", result);
            return result != null && result;
        } catch (Exception e) {
            log.error("Failed to evaluate condition: {}", condition, e);
            throw new RuntimeException("Failed to evaluate condition", e);
        }
    }

    /**
     * 评估数值比较条件
     *
     * @param value 实际值
     * @param operator 操作符
     * @param threshold 阈值
     * @return 是否满足条件
     */
    public boolean evaluateNumericCondition(Number value, String operator, Number threshold) {
        if (value == null || threshold == null) {
            return false;
        }

        double v = value.doubleValue();
        double t = threshold.doubleValue();

        switch (operator.toLowerCase()) {
            case ">":
                return v > t;
            case ">=":
                return v >= t;
            case "<":
                return v < t;
            case "<=":
                return v <= t;
            case "==":
            case "=":
                return v == t;
            case "!=":
                return v != t;
            default:
                log.error("Unsupported operator: {}", operator);
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    /**
     * 评估字符串条件
     *
     * @param value 实际值
     * @param operator 操作符
     * @param pattern 模式
     * @return 是否满足条件
     */
    public boolean evaluateStringCondition(String value, String operator, String pattern) {
        if (value == null || pattern == null) {
            return false;
        }

        switch (operator.toLowerCase()) {
            case "contains":
                return value.contains(pattern);
            case "startswith":
                return value.startsWith(pattern);
            case "endswith":
                return value.endsWith(pattern);
            case "matches":
                return value.matches(pattern);
            case "==":
            case "=":
                return value.equals(pattern);
            case "!=":
                return !value.equals(pattern);
            default:
                log.error("Unsupported operator: {}", operator);
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }
} 