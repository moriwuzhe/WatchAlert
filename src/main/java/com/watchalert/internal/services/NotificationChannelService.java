package com.watchalert.internal.services;

import com.watchalert.internal.models.NotificationChannel;

/**
 * 通知渠道接口
 */
public interface NotificationChannelService {
    /**
     * 发送通知
     *
     * @param channel 通知渠道配置
     * @param title 通知标题
     * @param content 通知内容
     */
    void send(NotificationChannel channel, String title, String content);
} 