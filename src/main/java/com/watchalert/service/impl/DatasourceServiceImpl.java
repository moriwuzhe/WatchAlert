package com.watchalert.service.impl;

import com.watchalert.model.Datasource;
import com.watchalert.repository.DatasourceRepository;
import com.watchalert.service.DatasourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatasourceServiceImpl implements DatasourceService {

    private final DatasourceRepository datasourceRepository;

    @Override
    public List<Datasource> getAllEnabledDatasources() {
        return datasourceRepository.findByEnabledTrue();
    }

    @Override
    public void addClientToProviderPools(Datasource datasource) {
        try {
            switch (datasource.getType().toUpperCase()) {
                case "AWS":
                    // 初始化AWS客户端
                    break;
                case "ALIYUN":
                    // 初始化阿里云客户端
                    break;
                default:
                    log.warn("不支持的数据源类型: {}", datasource.getType());
            }
        } catch (Exception e) {
            log.error("初始化数据源客户端失败: {}", datasource.getId(), e);
            throw new RuntimeException("初始化数据源客户端失败", e);
        }
    }
} 