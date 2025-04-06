package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.NotificationChannel;
import com.watchalert.internal.services.NotificationChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@Component
public class WeChatWorkNotificationChannel implements NotificationChannelService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void send(NotificationChannel channel, String title, String content) {
        log.debug("Sending WeChat Work notification: {}", title);
        
        try {
            String webhookUrl = channel.getConfig().get("webhook_url");
            if (webhookUrl == null || webhookUrl.isEmpty()) {
                throw new IllegalArgumentException("WeChat Work webhook URL is not configured");
            }

            Map<String, Object> message = new HashMap<>();
            message.put("msgtype", "markdown");
            
            Map<String, String> markdown = new HashMap<>();
            markdown.put("content", String.format("### %s\n\n%s", title, content));
            message.put("markdown", markdown);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);
            
            log.debug("Sending request to WeChat Work webhook: {}", webhookUrl);
            Map<String, Object> response = restTemplate.postForObject(webhookUrl, request, Map.class);
            
            if (response == null) {
                throw new RuntimeException("Empty response from WeChat Work webhook");
            }

            Integer errcode = (Integer) response.get("errcode");
            if (errcode != null && errcode != 0) {
                String errmsg = (String) response.get("errmsg");
                throw new RuntimeException("Failed to send WeChat Work notification: " + errmsg);
            }
        } catch (Exception e) {
            log.error("Failed to send WeChat Work notification: {}", title, e);
            throw new RuntimeException("Failed to send WeChat Work notification", e);
        }
    }
} 