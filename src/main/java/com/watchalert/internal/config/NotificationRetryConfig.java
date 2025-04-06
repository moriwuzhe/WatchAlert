package com.watchalert.internal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification.retry")
public class NotificationRetryConfig {
    /**
     * 最大重试次数
     */
    private int maxAttempts = 3;

    /**
     * 初始重试延迟（毫秒）
     */
    private int initialDelay = 1000;

    /**
     * 重试延迟乘数
     */
    private double multiplier = 2.0;

    /**
     * 最大重试延迟（毫秒）
     */
    private int maxDelay = 10000;

    /**
     * 是否启用重试
     */
    private boolean enabled = true;

    /**
     * 时间窗口（秒）
     */
    private int timeWindow = 3600; // 1 hour in seconds
} 