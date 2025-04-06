package com.watchalert.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private LdapConfig ldap = new LdapConfig();
    private AiConfig ai = new AiConfig();

    @Data
    public static class LdapConfig {
        private boolean enabled = false;
        private String url;
        private String baseDn;
        private String username;
        private String password;
        private String userSearchBase;
        private String userSearchFilter;
        private String groupSearchBase;
        private String groupSearchFilter;
    }

    @Data
    public static class AiConfig {
        private boolean enabled = false;
        private String apiKey;
        private String model;
        private String endpoint;
        private int maxTokens = 2048;
        private double temperature = 0.7;
    }
} 