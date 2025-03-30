package com.watchalert.service;

import java.util.Map;

public interface AliyunMonitoringService {
    /**
     * 获取阿里云监控指标数据
     * @param namespace 命名空间
     * @param metricName 指标名称
     * @param dimensions 维度
     * @param period 统计周期
     * @return 指标数据
     */
    Map<String, Object> getMetricData(String namespace, String metricName, Map<String, String> dimensions, String period);
} 