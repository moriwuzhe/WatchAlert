package com.watchalert.internal.services;

import com.watchalert.internal.models.NotificationChannel;

/**
 * 通知限流服务接口
 */
public interface NotificationRateLimitService {
    /**
     * 检查是否应该发送通知
     *
     * @param channel 通知渠道
     * @return 是否应该发送通知
     */
    boolean shouldSendNotification(NotificationChannel channel);
} 