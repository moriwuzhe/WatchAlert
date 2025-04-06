package com.watchalert.internal.services.impl;

import com.watchalert.internal.services.ConditionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ConditionEvaluatorImpl implements ConditionEvaluator {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
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
            return false;
        }
    }
} 