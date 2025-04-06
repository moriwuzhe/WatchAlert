package com.watchalert.global;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
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
        private boolean enable;
        private String apiKey;
        private String endpoint;
    }
} 