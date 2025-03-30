package com.watchalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WatchAlertApplication {
    public static void main(String[] args) {
        SpringApplication.run(WatchAlertApplication.class, args);
    }
} 