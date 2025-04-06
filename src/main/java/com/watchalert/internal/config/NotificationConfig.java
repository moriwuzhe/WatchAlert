package com.watchalert.internal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification")
public class NotificationConfig {
    private Email email = new Email();
    private Webhook webhook = new Webhook();
    private Slack slack = new Slack();

    @Data
    public static class Email {
        private boolean enabled = false;
        private String host;
        private int port = 587;
        private String username;
        private String password;
        private String from;
        private boolean ssl = true;
    }

    @Data
    public static class Webhook {
        private boolean enabled = false;
        private String url;
        private String method = "POST";
        private int timeout = 5000;
        private int connectTimeout = 5000;
        private int readTimeout = 5000;
        private String defaultUrl = "http://localhost:8080/webhook";
    }

    @Data
    public static class Slack {
        private boolean enabled = false;
        private String webhookUrl;
        private String channel;
        private String username;
        private String icon;
        private String token;
        private String defaultChannel = "#alerts";
    }
} 