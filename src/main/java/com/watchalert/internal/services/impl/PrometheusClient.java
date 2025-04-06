package com.watchalert.internal.services.impl;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Component
public class PrometheusClient {
    private final CollectorRegistry registry;
    private final PushGateway pushGateway;
    private final HTTPServer server;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${prometheus.api.url:http://localhost:9090}")
    private String prometheusApiUrl;

    public PrometheusClient(String host, int port) throws IOException {
        this.registry = new CollectorRegistry();
        this.pushGateway = new PushGateway(host + ":" + port);
        this.server = new HTTPServer(new InetSocketAddress(port), registry);
    }

    public CollectorRegistry getRegistry() {
        return registry;
    }

    public PushGateway getPushGateway() {
        return pushGateway;
    }

    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error("Failed to stop Prometheus HTTP server", e);
        }
    }

    /**
     * 执行Prometheus查询
     *
     * @param query PromQL查询语句
     * @return 查询结果
     */
    public Map<String, Object> executeQuery(String query) {
        log.debug("Executing Prometheus query: {}", query);
        
        try {
            String url = UriComponentsBuilder
                .fromHttpUrl(prometheusApiUrl)
                .path("/api/v1/query")
                .queryParam("query", query)
                .build()
                .toUriString();

            log.debug("Sending request to Prometheus API: {}", url);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response == null) {
                throw new RuntimeException("Empty response from Prometheus API");
            }

            String status = (String) response.get("status");
            if (!"success".equals(status)) {
                String error = (String) response.get("error");
                throw new RuntimeException("Prometheus query failed: " + error);
            }

            return response;
        } catch (Exception e) {
            log.error("Failed to execute Prometheus query: {}", query, e);
            throw new RuntimeException("Failed to execute Prometheus query", e);
        }
    }
} 