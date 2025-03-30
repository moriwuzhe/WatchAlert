package com.watchalert.service;

import java.util.Map;

public interface MonitoringService {
    /**
     * 获取指定指标的数据
     *
     * @param source 监控源（如 AWS、Aliyun 等）
     * @param target 监控目标（如实例ID、数据库ID等）
     * @param metric 指标名称
     * @return 指标数据
     */
    Double getMetricValue(String source, String target, String metric);

    /**
     * 获取指定指标的详细数据
     *
     * @param source 监控源
     * @param target 监控目标
     * @param params 额外参数
     * @param metric 指标名称
     * @return 指标详细数据
     */
    Map<String, Object> getMetricData(String source, String target, Map<String, Object> params, String metric);
} 