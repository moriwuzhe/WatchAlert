package com.watchalert.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20190101.DescribeMetricDataRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricDataResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.watchalert.service.AliyunMonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AliyunMonitoringServiceImpl implements AliyunMonitoringService {

    @Value("${aliyun.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.region}")
    private String region;

    private IAcsClient getClient() {
        DefaultProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    @Override
    public Map<String, Object> getMetricData(String namespace, String metricName, Map<String, String> dimensions, String period) {
        try {
            DescribeMetricDataRequest request = new DescribeMetricDataRequest();
            request.setNamespace(namespace);
            request.setMetricName(metricName);
            request.setPeriod(period);
            
            // 设置维度
            StringBuilder dimensionStr = new StringBuilder();
            for (Map.Entry<String, String> entry : dimensions.entrySet()) {
                if (dimensionStr.length() > 0) {
                    dimensionStr.append(",");
                }
                dimensionStr.append(entry.getKey()).append("=").append(entry.getValue());
            }
            request.setDimensions(dimensionStr.toString());

            DescribeMetricDataResponse response = getClient().getAcsResponse(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("datapoints", response.getDatapoints());
            result.put("metricName", metricName);
            result.put("period", response.getPeriod());
            
            return result;
        } catch (ClientException e) {
            log.error("获取阿里云监控指标数据失败", e);
            throw new RuntimeException("获取阿里云监控指标数据失败: " + e.getMessage());
        }
    }
} 