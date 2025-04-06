package com.watchalert.initialization;

import com.watchalert.alert.AlertInitializer;
import com.watchalert.config.AppConfig;
import com.watchalert.global.GlobalConfig;
import com.watchalert.internal.cache.CacheManager;
import com.watchalert.internal.repo.RepositoryManager;
import com.watchalert.internal.services.ServiceManager;
import com.watchalert.pkg.ai.AiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class Initializer {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private RepositoryManager repositoryManager;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private AlertInitializer alertInitializer;

    @PostConstruct
    public void init() {
        // 设置全局配置
        GlobalConfig.setAppConfig(appConfig);

        // 初始化服务
        serviceManager.initialize();

        // 初始化告警系统
        alertInitializer.initialize();

        // 初始化权限数据
        initPermissions();

        // 初始化用户数据
        initUsers();

        // 初始化角色数据
        initRoles();

        // 初始化用户角色数据
        initUserRoles();

        // 导入数据源Client到存储池
        importClientPools();

        // 如果启用LDAP，启动同步任务
        if (appConfig.getLdap().isEnabled()) {
            serviceManager.getLdapService().startSyncUsersCronjob();
        }

        // 初始化AI客户端
        initAiClient();
    }

    private void initPermissions() {
        // TODO: 实现权限初始化
    }

    private void initUsers() {
        // TODO: 实现用户初始化
    }

    private void initRoles() {
        // TODO: 实现角色初始化
    }

    private void initUserRoles() {
        // TODO: 实现用户角色初始化
    }

    private void importClientPools() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        repositoryManager.getDatasourceRepository().findAll().stream()
                .filter(datasource -> datasource.getEnabled())
                .forEach(datasource -> {
                    CompletableFuture.runAsync(() -> {
                        try {
                            serviceManager.getDatasourceService().addClientToProviderPools(datasource);
                        } catch (Exception e) {
                            log.error("Failed to add client to provider pools: {}", e.getMessage());
                        }
                    }, executor);
                });
        executor.shutdown();
    }

    private void initAiClient() {
        if (appConfig.getAi().isEnable()) {
            try {
                AiClient aiClient = new AiClient(appConfig.getAi());
                cacheManager.getProviderPools().setClient("AiClient", aiClient);
            } catch (Exception e) {
                log.error("Failed to create AI client: {}", e.getMessage());
            }
        }
    }
} 