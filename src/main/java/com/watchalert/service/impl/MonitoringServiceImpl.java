package com.watchalert.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.GetMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricDataResult;
import com.amazonaws.services.cloudwatch.model.MetricDataQuery;
import com.amazonaws.services.cloudwatch.model.MetricStat;
import com.watchalert.config.AWSConfig;
import com.watchalert.config.AliyunConfig;
import com.watchalert.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private final AWSConfig awsConfig;
    private final AliyunConfig aliyunConfig;

    @Override
    public Double getMetricValue(String source, String target, String metric) {
        log.debug("获取指标值 - source: {}, target: {}, metric: {}", source, target, metric);
        switch (source.toUpperCase()) {
            case "AWS":
                return getAWSMetricValue(target, metric);
            case "ALIYUN":
                return getAliyunMetricValue(target, metric);
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + source);
        }
    }

    @Override
    public Map<String, Object> getMetricData(String source, String target, Map<String, Object> params, String metric) {
        log.debug("获取指标数据 - source: {}, target: {}, params: {}, metric: {}", source, target, params, metric);
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials(
                awsConfig.getAccessKey(),
                awsConfig.getSecretKey()
            );

            AmazonCloudWatch cloudWatch = AmazonCloudWatchClientBuilder.standard()
                .withRegion(awsConfig.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

            Date endTime = new Date();
            Date startTime = Date.from(Instant.now().minus(5, ChronoUnit.MINUTES));

            List<com.amazonaws.services.cloudwatch.model.Dimension> awsDimensions = params.entrySet().stream()
                .map(entry -> new com.amazonaws.services.cloudwatch.model.Dimension()
                    .withName(entry.getKey())
                    .withValue(entry.getValue().toString()))
                .collect(Collectors.toList());

            MetricDataQuery query = new MetricDataQuery()
                .withId("m1")
                .withMetricStat(new MetricStat()
                    .withMetric(new com.amazonaws.services.cloudwatch.model.Metric()
                        .withNamespace(source)
                        .withMetricName(metric)
                        .withDimensions(awsDimensions))
                    .withPeriod(Integer.parseInt(params.get("period").toString()))
                    .withStat("Average"));

            GetMetricDataRequest request = new GetMetricDataRequest()
                .withStartTime(startTime)
                .withEndTime(endTime)
                .withMetricDataQueries(Collections.singletonList(query));

            GetMetricDataResult result = cloudWatch.getMetricData(request);
            if (result.getMetricDataResults().isEmpty()) {
                return null;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("values", result.getMetricDataResults().get(0).getValues());
            response.put("timestamps", result.getMetricDataResults().get(0).getTimestamps());
            return response;
        } catch (Exception e) {
            log.error("获取指标数据失败: source={}, target={}, params={}, metric={}", source, target, params, metric, e);
            return null;
        }
    }

    private Double getAWSMetricValue(String target, String metric) {
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials(
                awsConfig.getAccessKey(),
                awsConfig.getSecretKey()
            );

            AmazonCloudWatch cloudWatch = AmazonCloudWatchClientBuilder.standard()
                .withRegion(awsConfig.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

            Date endTime = new Date();
            Date startTime = Date.from(Instant.now().minus(5, ChronoUnit.MINUTES));

            MetricDataQuery query = new MetricDataQuery()
                .withId("m1")
                .withMetricStat(new MetricStat()
                    .withMetric(new com.amazonaws.services.cloudwatch.model.Metric()
                        .withNamespace("AWS/EC2")
                        .withMetricName(metric)
                        .withDimensions(new com.amazonaws.services.cloudwatch.model.Dimension()
                            .withName("InstanceId")
                            .withValue(target)))
                    .withPeriod(60)
                    .withStat("Average"));

            GetMetricDataRequest request = new GetMetricDataRequest()
                .withStartTime(startTime)
                .withEndTime(endTime)
                .withMetricDataQueries(Collections.singletonList(query));

            GetMetricDataResult result = cloudWatch.getMetricData(request);
            if (result.getMetricDataResults().isEmpty()) {
                return null;
            }

            return result.getMetricDataResults().get(0).getValues().get(0);
        } catch (Exception e) {
            log.error("获取AWS指标值失败: target={}, metric={}", target, metric, e);
            return null;
        }
    }

    private Double getAliyunMetricValue(String target, String metric) {
        try {
            // TODO: 实现阿里云指标获取逻辑
            log.warn("阿里云指标获取功能尚未实现");
            return null;
        } catch (Exception e) {
            log.error("获取阿里云指标值失败: target={}, metric={}", target, metric, e);
            return null;
        }
    }
}