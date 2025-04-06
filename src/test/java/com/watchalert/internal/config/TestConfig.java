package com.watchalert.internal.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(25);
        mailSender.setUsername("test");
        mailSender.setPassword("test");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    @Primary
    public NotificationConfig notificationConfig() {
        NotificationConfig config = new NotificationConfig();
        
        // 配置邮件
        NotificationConfig.Email email = new NotificationConfig.Email();
        email.setHost("localhost");
        email.setPort(25);
        email.setUsername("test");
        email.setPassword("test");
        email.setFrom("test@example.com");
        email.setSsl(false);
        config.setEmail(email);
        
        // 配置Webhook
        NotificationConfig.Webhook webhook = new NotificationConfig.Webhook();
        webhook.setDefaultUrl("http://localhost:8080/webhook");
        webhook.setConnectTimeout(5000);
        webhook.setReadTimeout(5000);
        config.setWebhook(webhook);
        
        // 配置Slack
        NotificationConfig.Slack slack = new NotificationConfig.Slack();
        slack.setToken("test-token");
        slack.setDefaultChannel("#test");
        config.setSlack(slack);
        
        return config;
    }

    @Bean
    @Primary
    public NotificationRetryConfig notificationRetryConfig() {
        NotificationRetryConfig config = new NotificationRetryConfig();
        config.setEnabled(true);
        config.setMaxAttempts(3);
        config.setInitialDelay(1000);
        config.setMultiplier(2.0);
        config.setMaxDelay(10000);
        return config;
    }

    @Bean
    @Primary
    public NotificationRateLimitConfig notificationRateLimitConfig() {
        NotificationRateLimitConfig config = new NotificationRateLimitConfig();
        config.setEnabled(true);
        config.setTimeWindow(60);
        config.setMaxNotifications(10);
        config.setRuleTimeWindow(300);
        config.setMaxNotificationsPerRule(5);
        return config;
    }
} 