package com.watchalert.internal.services.impl;

import com.watchalert.internal.config.NotificationConfig;
import com.watchalert.internal.config.NotificationRetryConfig;
import com.watchalert.internal.config.TestConfig;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.services.NotificationRateLimitService;
import com.watchalert.internal.services.NotificationTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessage;
import org.springframework.test.context.ContextConfiguration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class NotificationServiceImplTest {

    @Mock
    private NotificationConfig config;

    @Mock
    private NotificationRetryConfig retryConfig;

    @Mock
    private NotificationTemplateService templateService;

    @Mock
    private NotificationRateLimitService rateLimitService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private AlertRule rule;
    private AlertHistory history;

    @BeforeEach
    void setUp() throws MessagingException {
        // 创建测试规则
        rule = new AlertRule();
        rule.setId(1L);
        rule.setName("测试规则");
        rule.setEnabled(true);
        rule.setNotifyChannels("email,webhook,slack");
        rule.setNotifyTarget("test@example.com");
        rule.setNotifyTemplate("test_template");

        // 创建测试历史记录
        history = new AlertHistory();
        history.setId(1L);
        history.setRuleId(1L);
        history.setMessage("测试告警消息");
        history.setCreatedAt(LocalDateTime.now());

        // 设置默认配置
        when(config.getEmail()).thenReturn(new NotificationConfig.Email());
        when(config.getWebhook()).thenReturn(new NotificationConfig.Webhook());
        when(config.getSlack()).thenReturn(new NotificationConfig.Slack());
        when(retryConfig.isEnabled()).thenReturn(true);
        when(retryConfig.getMaxAttempts()).thenReturn(3);
        when(retryConfig.getInitialDelay()).thenReturn(1000L);
        when(retryConfig.getMultiplier()).thenReturn(2.0);
        when(retryConfig.getMaxDelay()).thenReturn(10000L);
        when(rateLimitService.canSendNotification(any(AlertRule.class))).thenReturn(true);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendNotificationWhenRateLimitExceeded() {
        when(rateLimitService.canSendNotification(any(AlertRule.class))).thenReturn(false);

        notificationService.sendNotification(rule, history);

        verify(rateLimitService).canSendNotification(rule);
        verify(rateLimitService, never()).recordNotificationSent(any(AlertRule.class));
    }

    @Test
    void testSendNotificationWithEmail() throws MessagingException {
        notificationService.sendNotification(rule, history);

        verify(rateLimitService).canSendNotification(rule);
        verify(rateLimitService).recordNotificationSent(rule);
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendNotificationWithWebhook() throws Exception {
        rule.setNotifyChannels("webhook");

        notificationService.sendNotification(rule, history);

        verify(rateLimitService).canSendNotification(rule);
        verify(rateLimitService).recordNotificationSent(rule);
    }

    @Test
    void testSendNotificationWithSlack() throws Exception {
        rule.setNotifyChannels("slack");

        notificationService.sendNotification(rule, history);

        verify(rateLimitService).canSendNotification(rule);
        verify(rateLimitService).recordNotificationSent(rule);
    }

    @Test
    void testSendNotificationWithRetry() throws MessagingException {
        when(mailSender.send(any(MimeMessage.class)))
                .thenThrow(new MessagingException("测试异常"))
                .thenThrow(new MessagingException("测试异常"))
                .thenReturn(null);

        notificationService.sendNotification(rule, history);

        verify(rateLimitService).canSendNotification(rule);
        verify(rateLimitService).recordNotificationSent(rule);
        verify(mailSender, times(3)).send(any(MimeMessage.class));
    }

    @Test
    void testSendNotificationWithRetryDisabled() throws MessagingException {
        when(retryConfig.isEnabled()).thenReturn(false);
        when(mailSender.send(any(MimeMessage.class)))
                .thenThrow(new MessagingException("测试异常"));

        assertThrows(RuntimeException.class, () -> notificationService.sendNotification(rule, history));

        verify(rateLimitService).canSendNotification(rule);
        verify(rateLimitService, never()).recordNotificationSent(any(AlertRule.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendTestNotification() throws MessagingException {
        boolean result = notificationService.sendTestNotification("email", "test@example.com");

        assertTrue(result);
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendTestNotificationWithInvalidChannel() {
        boolean result = notificationService.sendTestNotification("invalid", "test@example.com");

        assertFalse(result);
        verify(mailSender, never()).send(any(MimeMessage.class));
    }
} 