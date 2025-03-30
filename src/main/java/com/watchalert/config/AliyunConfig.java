package com.watchalert.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun")
public class AliyunConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String regionId;
    private String endpoint;
    private String project;
    private String logstore;
} 