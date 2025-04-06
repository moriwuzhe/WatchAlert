package com.watchalert.alert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertInitializer {
    public void initialize() {
        // TODO: 实现告警系统初始化
        log.info("Alert system initialized");
    }
} 