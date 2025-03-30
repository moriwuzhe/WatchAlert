package com.watchalert.service.impl;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import com.watchalert.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendNotification(Alert alert) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(alert.getAlertRule().getNotificationChannels().get(0)); // 假设第一个是邮箱地址
            helper.setSubject("告警通知: " + alert.getAlertRule().getName());
            helper.setText(buildEmailContent(alert), true);

            mailSender.send(message);
            log.info("告警通知发送成功: {}", alert.getId());
        } catch (MessagingException e) {
            log.error("告警通知发送失败: {}", alert.getId(), e);
            throw new RuntimeException("告警通知发送失败", e);
        }
    }

    protected String buildEmailContent(Alert alert) {
        AlertRule rule = alert.getAlertRule();
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>告警通知</h2>");
        content.append("<p><strong>告警规则：</strong>").append(rule.getName()).append("</p>");
        content.append("<p><strong>指标名称：</strong>").append(rule.getMetricName()).append("</p>");
        content.append("<p><strong>当前值：</strong>").append(alert.getValue()).append("</p>");
        content.append("<p><strong>阈值：</strong>").append(rule.getThreshold()).append("</p>");
        content.append("<p><strong>操作符：</strong>").append(rule.getOperator()).append("</p>");
        content.append("<p><strong>严重程度：</strong>").append(rule.getSeverity()).append("</p>");
        content.append("<p><strong>触发时间：</strong>").append(alert.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>");
        content.append("</body></html>");
        return content.toString();
    }
} 