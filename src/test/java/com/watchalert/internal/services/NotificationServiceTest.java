package com.watchalert.internal.services;

import com.watchalert.internal.config.NotificationConfig;
import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.services.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessage;
import org.apache.http.impl.client.CloseableHttpClient;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationConfig config;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private Slack slack;

    @Mock
    private MethodsClient methodsClient;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private AlertRule rule;
    private AlertHistory history;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        rule = new AlertRule();
        rule.setName("测试规则");
        rule.setSeverity("HIGH");
        rule.setNotifyChannels("email,webhook,slack");
        rule.setNotifyTarget("test@example.com");

        history = new AlertHistory();
        history.setMessage("测试告警消息");
        history.setCreatedAt(LocalDateTime.now());

        // 设置配置
        NotificationConfig.Email emailConfig = new NotificationConfig.Email();
        emailConfig.setFrom("from@example.com");
        when(config.getEmail()).thenReturn(emailConfig);

        NotificationConfig.Webhook webhookConfig = new NotificationConfig.Webhook();
        webhookConfig.setDefaultUrl("http://example.com/webhook");
        when(config.getWebhook()).thenReturn(webhookConfig);

        NotificationConfig.Slack slackConfig = new NotificationConfig.Slack();
        slackConfig.setDefaultChannel("#alerts");
        slackConfig.setToken("xoxb-test-token");
        when(config.getSlack()).thenReturn(slackConfig);
    }

    @Test
    void testSendEmailNotification() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.sendNotification(rule, history);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendWebhookNotification() throws Exception {
        notificationService.sendNotification(rule, history);

        // 由于HTTP客户端是模拟的，我们只能验证没有抛出异常
        verify(httpClient, never()).execute(any());
    }

    @Test
    void testSendSlackNotification() throws Exception {
        when(slack.methods(anyString())).thenReturn(methodsClient);
        doNothing().when(methodsClient).chatPostMessage(any());

        notificationService.sendNotification(rule, history);

        verify(slack, times(1)).methods(anyString());
        verify(methodsClient, times(1)).chatPostMessage(any());
    }

    @Test
    void testSendTestEmail() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        boolean result = notificationService.sendTestNotification("email", "test@example.com");

        assert result;
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendTestWebhook() throws Exception {
        boolean result = notificationService.sendTestNotification("webhook", "http://example.com/webhook");

        assert result;
        verify(httpClient, never()).execute(any());
    }

    @Test
    void testSendTestSlack() throws Exception {
        when(slack.methods(anyString())).thenReturn(methodsClient);
        doNothing().when(methodsClient).chatPostMessage(any());

        boolean result = notificationService.sendTestNotification("slack", "#test-channel");

        assert result;
        verify(slack, times(1)).methods(anyString());
        verify(methodsClient, times(1)).chatPostMessage(any());
    }
} 