package com.watchalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WatchAlertApplication {
    public static void main(String[] args) {
        SpringApplication.run(WatchAlertApplication.class, args);
    }
} 