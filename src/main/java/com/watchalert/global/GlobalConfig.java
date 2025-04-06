package com.watchalert.global;

import com.watchalert.internal.config.AppConfig;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GlobalConfig {
    private static String version;
    private static AppConfig appConfig;

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        GlobalConfig.version = version;
    }

    public static AppConfig getAppConfig() {
        return appConfig;
    }

    public static void setAppConfig(AppConfig appConfig) {
        GlobalConfig.appConfig = appConfig;
    }
} 