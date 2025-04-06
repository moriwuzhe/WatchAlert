package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.Datasource;
import com.watchalert.internal.repo.DatasourceRepository;
import com.watchalert.internal.services.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private DatasourceRepository datasourceRepository;

    @Autowired
    private PrometheusClient prometheusClient;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final Map<Long, Object> clientPool = new ConcurrentHashMap<>();

    @Override
    public void initialize() {
        log.info("Initializing DatasourceService...");
        List<Datasource> enabledDatasources = datasourceRepository.findByEnabled(true);
        
        for (Datasource datasource : enabledDatasources) {
            try {
                addClientToProviderPools(datasource);
            } catch (Exception e) {
                log.error("Failed to initialize datasource: {}", datasource.getName(), e);
            }
        }
    }

    @Override
    public void addClientToProviderPools(Datasource datasource) throws Exception {
        if (!datasource.isEnabled()) {
            log.warn("Datasource is disabled: {}", datasource.getName());
            return;
        }

        Object client;
        switch (datasource.getType().toLowerCase()) {
            case "prometheus":
                // 使用已注入的PrometheusClient
                client = prometheusClient;
                break;
            case "elasticsearch":
                // 使用已注入的ElasticsearchClient
                client = elasticsearchClient;
                break;
            default:
                throw new IllegalArgumentException("Unsupported datasource type: " + datasource.getType());
        }

        clientPool.put(datasource.getId(), client);
        log.info("Added client to pool for datasource: {}", datasource.getName());
    }

    @Override
    public List<Datasource> findByType(String type) {
        return datasourceRepository.findByType(type);
    }

    @Override
    public Object getClient(Datasource datasource) throws Exception {
        Object client = clientPool.get(datasource.getId());
        if (client == null) {
            log.debug("Client not found in pool for datasource: {}, creating new client", datasource.getName());
            addClientToProviderPools(datasource);
            client = clientPool.get(datasource.getId());
        }
        return client;
    }

    @Override
    public boolean testConnection(Datasource datasource) throws Exception {
        try {
            Object client = getClient(datasource);
            switch (datasource.getType().toLowerCase()) {
                case "prometheus":
                    // 使用PrometheusClient测试连接
                    ((PrometheusClient) client).executeQuery("up");
                    break;
                case "elasticsearch":
                    // 使用ElasticsearchClient测试连接
                    ((ElasticsearchClient) client).executeQuery("GET _cluster/health");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported datasource type: " + datasource.getType());
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to test connection for datasource: {}", datasource.getName(), e);
            return false;
        }
    }
} 