package com.watchalert.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "watch-alert")
public class GlobalConfig {
    private String version;
    private String environment;
    private String timezone;
    private LdapConfig ldap;
    private AiConfig ai;

    @Data
    public static class LdapConfig {
        private boolean enabled;
        private String url;
        private String baseDn;
        private String username;
        private String password;
    }

    @Data
    public static class AiConfig {
        private boolean enabled;
        private String apiKey;
        private String endpoint;
        private String model;
    }
} 