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
public class DingTalkNotificationChannel implements NotificationChannelService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void send(NotificationChannel channel, String title, String content) {
        log.debug("Sending DingTalk notification: {}", title);
        
        try {
            String webhookUrl = channel.getConfig().get("webhook_url");
            if (webhookUrl == null || webhookUrl.isEmpty()) {
                throw new IllegalArgumentException("DingTalk webhook URL is not configured");
            }

            Map<String, Object> message = new HashMap<>();
            message.put("msgtype", "markdown");
            
            Map<String, String> markdown = new HashMap<>();
            markdown.put("title", title);
            markdown.put("text", content);
            message.put("markdown", markdown);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);
            
            log.debug("Sending request to DingTalk webhook: {}", webhookUrl);
            Map<String, Object> response = restTemplate.postForObject(webhookUrl, request, Map.class);
            
            if (response == null) {
                throw new RuntimeException("Empty response from DingTalk webhook");
            }

            Integer errcode = (Integer) response.get("errcode");
            if (errcode != null && errcode != 0) {
                String errmsg = (String) response.get("errmsg");
                throw new RuntimeException("Failed to send DingTalk notification: " + errmsg);
            }
        } catch (Exception e) {
            log.error("Failed to send DingTalk notification: {}", title, e);
            throw new RuntimeException("Failed to send DingTalk notification", e);
        }
    }
} 