package com.watchalert.service.impl;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Alert testAlert;
    private AlertRule testAlertRule;

    @BeforeEach
    void setUp() {
        testAlertRule = new AlertRule();
        testAlertRule.setName("Test Rule");
        testAlertRule.setMetricName("CPU_Usage");
        testAlertRule.setThreshold(80.0);
        testAlertRule.setOperator(">");
        testAlertRule.setSeverity("WARNING");

        testAlert = new Alert();
        testAlert.setId("1");
        testAlert.setAlertRule(testAlertRule);
        testAlert.setStatus("OPEN");
        testAlert.setValue(85.0);
        testAlert.setTriggeredAt(LocalDateTime.now());
    }

    @Test
    void sendNotification_Success() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.sendNotification(testAlert);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_Failure() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("发送失败")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> notificationService.sendNotification(testAlert));
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void buildEmailContent_Success() {
        String content = notificationService.buildEmailContent(testAlert);

        assertNotNull(content);
        assertTrue(content.contains(testAlertRule.getName()));
        assertTrue(content.contains(testAlertRule.getMetricName()));
        assertTrue(content.contains(String.valueOf(testAlert.getValue())));
        assertTrue(content.contains(testAlertRule.getThreshold().toString()));
        assertTrue(content.contains(testAlertRule.getOperator()));
        assertTrue(content.contains(testAlertRule.getSeverity()));
    }
} 