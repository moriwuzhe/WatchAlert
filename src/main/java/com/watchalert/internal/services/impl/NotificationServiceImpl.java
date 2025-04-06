package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.NotificationChannel;
import com.watchalert.internal.services.NotificationService;
import com.watchalert.internal.services.NotificationChannelService;
import com.watchalert.internal.services.NotificationRateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationRateLimitService rateLimitService;

    @Autowired
    private List<NotificationChannelService> notificationChannels;

    @Override
    public void sendNotification(NotificationChannel channel, String title, String content) {
        log.debug("Sending notification via channel: {}", channel.getType());
        
        // 检查通知频率限制
        if (!rateLimitService.shouldSendNotification(channel)) {
            log.info("Notification rate limit exceeded for channel: {}", channel.getType());
            return;
        }

        // 根据渠道类型选择发送方式
        switch (channel.getType().toLowerCase()) {
            case "email":
                sendEmail(channel, title, content);
                break;
            case "webhook":
                sendWebhook(channel, title, content);
                break;
            case "slack":
                sendSlack(channel, title, content);
                break;
            case "dingtalk":
            case "wechatwork":
                // 使用对应的通知渠道实现
                for (NotificationChannelService notificationChannel : notificationChannels) {
                    if (notificationChannel.getClass().getSimpleName().toLowerCase()
                            .contains(channel.getType().toLowerCase())) {
                        notificationChannel.send(channel, title, content);
                        break;
                    }
                }
                break;
            default:
                log.error("Unsupported notification channel type: {}", channel.getType());
                throw new IllegalArgumentException("Unsupported notification channel type: " + channel.getType());
        }
    }

    @Override
    public void sendTestNotification(NotificationChannel channel) {
        String title = "Test Notification";
        String content = "This is a test notification from WatchAlert system.";
        sendNotification(channel, title, content);
    }

    private void sendEmail(NotificationChannel channel, String title, String content) {
        String to = channel.getConfig().get("to");
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Email recipient is not configured");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    private void sendWebhook(NotificationChannel channel, String title, String content) {
        String webhookUrl = channel.getConfig().get("webhook_url");
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            throw new IllegalArgumentException("Webhook URL is not configured");
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("content", content);
        restTemplate.postForObject(webhookUrl, payload, String.class);
    }

    private void sendSlack(NotificationChannel channel, String title, String content) {
        String webhookUrl = channel.getConfig().get("webhook_url");
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            throw new IllegalArgumentException("Slack webhook URL is not configured");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", String.format("*%s*\n%s", title, content));
        restTemplate.postForObject(webhookUrl, payload, String.class);
    }
} 