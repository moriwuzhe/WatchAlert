package com.watchalert.internal.services.impl;

import com.watchalert.internal.config.TestConfig;
import com.watchalert.internal.models.NotificationTemplate;
import com.watchalert.internal.repo.NotificationTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class NotificationTemplateServiceImplTest {

    @Mock
    private NotificationTemplateRepository templateRepository;

    @InjectMocks
    private NotificationTemplateServiceImpl templateService;

    private NotificationTemplate emailTemplate;
    private NotificationTemplate webhookTemplate;
    private NotificationTemplate slackTemplate;

    @BeforeEach
    void setUp() {
        // 创建邮件模板
        emailTemplate = new NotificationTemplate();
        emailTemplate.setId(1L);
        emailTemplate.setName("email_template");
        emailTemplate.setChannel("email");
        emailTemplate.setContent("尊敬的${user.name}：\n\n您的${rule.name}规则已触发告警。\n\n告警详情：\n- 严重程度：${rule.severity}\n- 告警消息：${history.message}\n- 触发时间：${history.createdAt}");
        emailTemplate.setEnabled(true);

        // 创建Webhook模板
        webhookTemplate = new NotificationTemplate();
        webhookTemplate.setId(2L);
        webhookTemplate.setName("webhook_template");
        webhookTemplate.setChannel("webhook");
        webhookTemplate.setContent("{\"rule\":\"${rule.name}\",\"severity\":\"${rule.severity}\",\"message\":\"${history.message}\",\"timestamp\":\"${history.createdAt}\"}");
        webhookTemplate.setEnabled(true);

        // 创建Slack模板
        slackTemplate = new NotificationTemplate();
        slackTemplate.setId(3L);
        slackTemplate.setName("slack_template");
        slackTemplate.setChannel("slack");
        slackTemplate.setContent("*告警通知*\n规则: ${rule.name}\n严重程度: ${rule.severity}\n消息: ${history.message}\n时间: ${history.createdAt}");
        slackTemplate.setEnabled(true);

        // 设置Mock行为
        when(templateRepository.findByName("email_template")).thenReturn(Optional.of(emailTemplate));
        when(templateRepository.findByName("webhook_template")).thenReturn(Optional.of(webhookTemplate));
        when(templateRepository.findByName("slack_template")).thenReturn(Optional.of(slackTemplate));
        when(templateRepository.findByChannelAndEnabled("email", true)).thenReturn(Arrays.asList(emailTemplate));
        when(templateRepository.findByChannelAndEnabled("webhook", true)).thenReturn(Arrays.asList(webhookTemplate));
        when(templateRepository.findByChannelAndEnabled("slack", true)).thenReturn(Arrays.asList(slackTemplate));
    }

    @Test
    void testCreateTemplate() {
        NotificationTemplate template = new NotificationTemplate();
        template.setName("test_template");
        template.setChannel("email");
        template.setContent("Test content");
        template.setEnabled(true);

        when(templateRepository.save(any(NotificationTemplate.class))).thenReturn(template);

        NotificationTemplate result = templateService.createTemplate(template);

        assertNotNull(result);
        assertEquals("test_template", result.getName());
        assertEquals("email", result.getChannel());
        assertEquals("Test content", result.getContent());
        assertTrue(result.isEnabled());

        verify(templateRepository).save(any(NotificationTemplate.class));
    }

    @Test
    void testUpdateTemplate() {
        NotificationTemplate template = new NotificationTemplate();
        template.setId(1L);
        template.setName("updated_template");
        template.setChannel("email");
        template.setContent("Updated content");
        template.setEnabled(true);

        templateService.updateTemplate(template);

        verify(templateRepository).save(template);
    }

    @Test
    void testDeleteTemplate() {
        templateService.deleteTemplate(1L);

        verify(templateRepository).deleteById(1L);
    }

    @Test
    void testGetEnabledTemplates() {
        String channel = "email";
        templateService.getEnabledTemplates(channel);

        verify(templateRepository).findByChannelAndEnabled(channel, true);
    }

    @Test
    void testGetTemplateByName() {
        String name = "email_template";
        NotificationTemplate result = templateService.getTemplateByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals("email", result.getChannel());

        verify(templateRepository).findByName(name);
    }

    @Test
    void testGetTemplateByNameNotFound() {
        String name = "non_existent_template";
        when(templateRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> templateService.getTemplateByName(name));

        verify(templateRepository).findByName(name);
    }

    @Test
    void testRenderTemplate() {
        // 准备测试数据
        Map<String, Object> data = new HashMap<>();
        
        Map<String, Object> user = new HashMap<>();
        user.put("name", "张三");
        data.put("user", user);
        
        Map<String, Object> rule = new HashMap<>();
        rule.put("name", "CPU使用率告警");
        rule.put("severity", "严重");
        data.put("rule", rule);
        
        Map<String, Object> history = new HashMap<>();
        history.put("message", "CPU使用率超过90%");
        history.put("createdAt", "2023-01-01 12:00:00");
        data.put("history", history);

        // 测试邮件模板渲染
        String emailResult = templateService.renderTemplate("email_template", data);
        assertNotNull(emailResult);
        assertTrue(emailResult.contains("张三"));
        assertTrue(emailResult.contains("CPU使用率告警"));
        assertTrue(emailResult.contains("严重"));
        assertTrue(emailResult.contains("CPU使用率超过90%"));
        assertTrue(emailResult.contains("2023-01-01 12:00:00"));

        // 测试Webhook模板渲染
        String webhookResult = templateService.renderTemplate("webhook_template", data);
        assertNotNull(webhookResult);
        assertTrue(webhookResult.contains("\"rule\":\"CPU使用率告警\""));
        assertTrue(webhookResult.contains("\"severity\":\"严重\""));
        assertTrue(webhookResult.contains("\"message\":\"CPU使用率超过90%\""));
        assertTrue(webhookResult.contains("\"timestamp\":\"2023-01-01 12:00:00\""));

        // 测试Slack模板渲染
        String slackResult = templateService.renderTemplate("slack_template", data);
        assertNotNull(slackResult);
        assertTrue(slackResult.contains("*告警通知*"));
        assertTrue(slackResult.contains("规则: CPU使用率告警"));
        assertTrue(slackResult.contains("严重程度: 严重"));
        assertTrue(slackResult.contains("消息: CPU使用率超过90%"));
        assertTrue(slackResult.contains("时间: 2023-01-01 12:00:00"));
    }

    @Test
    void testRenderTemplateWithNestedProperties() {
        // 准备测试数据
        Map<String, Object> data = new HashMap<>();
        
        Map<String, Object> user = new HashMap<>();
        user.put("name", "张三");
        Map<String, Object> profile = new HashMap<>();
        profile.put("department", "研发部");
        user.put("profile", profile);
        data.put("user", user);
        
        Map<String, Object> rule = new HashMap<>();
        rule.put("name", "CPU使用率告警");
        Map<String, Object> threshold = new HashMap<>();
        threshold.put("value", 90);
        threshold.put("unit", "%");
        rule.put("threshold", threshold);
        data.put("rule", rule);

        // 创建带有嵌套属性的模板
        NotificationTemplate template = new NotificationTemplate();
        template.setName("nested_template");
        template.setContent("用户 ${user.name} 在 ${user.profile.department} 部门的 ${rule.name} 规则阈值设置为 ${rule.threshold.value}${rule.threshold.unit}");
        template.setEnabled(true);
        
        when(templateRepository.findByName("nested_template")).thenReturn(Optional.of(template));

        // 测试模板渲染
        String result = templateService.renderTemplate("nested_template", data);
        assertNotNull(result);
        assertTrue(result.contains("用户 张三 在 研发部 部门的 CPU使用率告警 规则阈值设置为 90%"));
    }
} 