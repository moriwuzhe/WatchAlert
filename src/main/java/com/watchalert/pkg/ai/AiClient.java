package com.watchalert.pkg.ai;

import com.watchalert.global.AppConfig.AiConfig;
import lombok.Data;

@Data
public class AiClient {
    private final AiConfig config;

    public AiClient(AiConfig config) {
        this.config = config;
    }

    public String generateResponse(String prompt) {
        // TODO: 实现AI响应生成
        return "";
    }
} 