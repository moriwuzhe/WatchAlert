package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertHistory;

/**
 * 通知服务接口
 */
public interface NotificationService {
    /**
     * 发送通知
     *
     * @param rule 通知规则
     * @param history 通知历史记录
     */
    void sendNotification(AlertRule rule, AlertHistory history);

    /**
     * 发送测试通知
     *
     * @param channel 通知渠道
     * @param recipient 接收者
     * @param message 消息内容
     */
    void sendTestNotification(String channel, String recipient, String message);
} 