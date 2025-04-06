package com.watchalert.internal.services;

import java.util.Map;

/**
 * 告警条件评估器接口
 */
public interface ConditionEvaluator {
    
    /**
     * 评估告警条件
     *
     * @param data 评估数据
     * @param condition 条件表达式
     * @return 评估结果
     */
    boolean evaluateCondition(Map<String, Object> data, String condition);
} 