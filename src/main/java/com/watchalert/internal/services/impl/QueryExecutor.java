package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.Datasource;
import com.watchalert.internal.services.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class QueryExecutor {

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private PrometheusClient prometheusClient;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * 执行查询
     *
     * @param datasource 数据源
     * @param query 查询语句
     * @return 查询结果
     */
    public Map<String, Object> executeQuery(Datasource datasource, String query) {
        log.debug("Executing query for datasource: {}, type: {}", datasource.getName(), datasource.getType());
        
        switch (datasource.getType().toLowerCase()) {
            case "prometheus":
                return prometheusClient.executeQuery(query);
            case "elasticsearch":
                return elasticsearchClient.executeQuery(query);
            default:
                log.error("Unsupported datasource type: {}", datasource.getType());
                throw new IllegalArgumentException("Unsupported datasource type: " + datasource.getType());
        }
    }
} 