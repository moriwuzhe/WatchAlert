package com.watchalert.service;

import com.watchalert.model.Alert;

public interface NotificationService {
    /**
     * 发送告警通知
     * @param alert 告警信息
     */
    void sendNotification(Alert alert);
} 