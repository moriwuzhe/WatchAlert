package com.watchalert.internal.cache;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class CacheManager {
    private final ProviderPools providerPools = new ProviderPools();

    @Data
    public static class ProviderPools {
        private final ConcurrentHashMap<String, Object> clients = new ConcurrentHashMap<>();

        public void setClient(String key, Object client) {
            clients.put(key, client);
        }

        public Object getClient(String key) {
            return clients.get(key);
        }
    }
} 