package com.watchalert.internal.services.impl;

import com.watchalert.internal.config.NotificationRateLimitConfig;
import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.services.NotificationRateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class NotificationRateLimitServiceImpl implements NotificationRateLimitService {

    @Autowired
    private NotificationRateLimitConfig config;

    private final Map<Long, LocalDateTime> lastNotificationTimes = new ConcurrentHashMap<>();
    private final Map<Long, Integer> notificationCounts = new ConcurrentHashMap<>();

    @Override
    public boolean canSendNotification(AlertRule rule) {
        Long ruleId = rule.getId();
        LocalDateTime now = LocalDateTime.now();
        
        // 获取上次通知时间
        LocalDateTime lastTime = lastNotificationTimes.get(ruleId);
        if (lastTime == null) {
            return true;
        }

        // 检查最小间隔
        if (now.isBefore(lastTime.plusSeconds(config.getMinIntervalSeconds()))) {
            log.debug("Rate limit: minimum interval not reached for rule {}", ruleId);
            return false;
        }

        // 检查最大频率
        Integer count = notificationCounts.getOrDefault(ruleId, 0);
        if (count >= config.getMaxNotificationsPerHour() && 
            now.isBefore(lastTime.plusHours(1))) {
            log.debug("Rate limit: maximum notifications per hour reached for rule {}", ruleId);
            return false;
        }

        return true;
    }

    @Override
    public void recordNotificationSent(AlertRule rule) {
        Long ruleId = rule.getId();
        LocalDateTime now = LocalDateTime.now();
        
        // 更新上次通知时间
        lastNotificationTimes.put(ruleId, now);
        
        // 更新通知计数
        LocalDateTime lastTime = lastNotificationTimes.get(ruleId);
        if (lastTime != null && now.isBefore(lastTime.plusHours(1))) {
            notificationCounts.merge(ruleId, 1, Integer::sum);
        } else {
            notificationCounts.put(ruleId, 1);
        }
        
        log.debug("Recorded notification sent for rule {}, count: {}", 
            ruleId, notificationCounts.get(ruleId));
    }
} 