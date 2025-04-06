package com.watchalert.internal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 通知速率限制配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "notification.rate-limit")
public class NotificationRateLimitConfig {
    
    /**
     * 是否启用速率限制
     */
    private boolean enabled = true;
    
    /**
     * 最小通知间隔（秒）
     */
    private int minIntervalSeconds = 60;
    
    /**
     * 每小时最大通知次数
     */
    private int maxNotificationsPerHour = 60;
    
    /**
     * 全局每小时最大通知次数
     */
    private int globalMaxNotificationsPerHour = 1000;
} 