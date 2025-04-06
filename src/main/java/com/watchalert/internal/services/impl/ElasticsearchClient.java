package com.watchalert.internal.services.impl;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Component
public class ElasticsearchClient {
    private final RestHighLevelClient client;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${elasticsearch.api.url:http://localhost:9200}")
    private String elasticsearchApiUrl;

    public ElasticsearchClient(String host, int port, String username, String password) {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, "http"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        this.client = new RestHighLevelClient(builder);
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            log.error("Failed to close Elasticsearch client", e);
        }
    }

    /**
     * 执行Elasticsearch查询
     *
     * @param query Elasticsearch查询语句
     * @return 查询结果
     */
    public Map<String, Object> executeQuery(String query) {
        log.debug("Executing Elasticsearch query: {}", query);
        
        try {
            String url = elasticsearchApiUrl + "/_search";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(query, headers);
            
            log.debug("Sending request to Elasticsearch API: {}", url);
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            
            if (response == null) {
                throw new RuntimeException("Empty response from Elasticsearch API");
            }

            return response;
        } catch (Exception e) {
            log.error("Failed to execute Elasticsearch query: {}", query, e);
            throw new RuntimeException("Failed to execute Elasticsearch query", e);
        }
    }
} 