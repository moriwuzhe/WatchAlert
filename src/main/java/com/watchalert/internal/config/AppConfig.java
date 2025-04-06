package com.watchalert.internal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private Ldap ldap = new Ldap();
    private Ai ai = new Ai();

    @Data
    public static class Ldap {
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
    public static class Ai {
        private boolean enabled = false;
        private String apiKey;
        private String model;
        private String endpoint;
        private int maxTokens = 1000;
        private double temperature = 0.7;
    }
} 