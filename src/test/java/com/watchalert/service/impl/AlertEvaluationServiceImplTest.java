package com.watchalert.service.impl;

import com.watchalert.model.Alert;
import com.watchalert.model.AlertRule;
import com.watchalert.service.AlertService;
import com.watchalert.service.MonitoringService;
import com.watchalert.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertEvaluationServiceImplTest {

    @Mock
    private MonitoringService monitoringService;

    @Mock
    private AlertService alertService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AlertEvaluationServiceImpl alertEvaluationService;

    private AlertRule testAlertRule;
    private Map<String, Object> testMetricData;

    @BeforeEach
    void setUp() {
        testAlertRule = new AlertRule();
        testAlertRule.setId("1");
        testAlertRule.setName("Test Rule");
        testAlertRule.setMetricName("CPU_Usage");
        testAlertRule.setThreshold(80.0);
        testAlertRule.setOperator(">");
        testAlertRule.setDuration(300);
        testAlertRule.setSeverity("WARNING");
        testAlertRule.setNotificationChannels(Arrays.asList("EMAIL"));
        testAlertRule.setEnabled(true);

        testMetricData = new HashMap<>();
        testMetricData.put("value", 85.0);
        testMetricData.put("timestamp", LocalDateTime.now());
    }

    @Test
    void evaluateAlertRule_ThresholdExceeded() {
        when(monitoringService.getMetricData(anyString(), anyString(), anyMap(), anyString()))
            .thenReturn(testMetricData);
        when(alertService.createAlert(any(Alert.class)))
            .thenReturn(new Alert());
        doNothing().when(notificationService).sendNotification(any(Alert.class));

        alertEvaluationService.evaluateAlertRule(testAlertRule);

        verify(alertService).createAlert(any(Alert.class));
        verify(notificationService).sendNotification(any(Alert.class));
    }

    @Test
    void evaluateAlertRule_ThresholdNotExceeded() {
        testMetricData.put("value", 75.0);
        when(monitoringService.getMetricData(anyString(), anyString(), anyMap(), anyString()))
            .thenReturn(testMetricData);

        alertEvaluationService.evaluateAlertRule(testAlertRule);

        verify(alertService, never()).createAlert(any(Alert.class));
        verify(notificationService, never()).sendNotification(any(Alert.class));
    }

    @Test
    void evaluateAlertRule_Disabled() {
        testAlertRule.setEnabled(false);

        alertEvaluationService.evaluateAlertRule(testAlertRule);

        verify(monitoringService, never()).getMetricData(anyString(), anyString(), anyMap(), anyString());
        verify(alertService, never()).createAlert(any(Alert.class));
        verify(notificationService, never()).sendNotification(any(Alert.class));
    }

    @Test
    void evaluateAlertRule_InvalidOperator() {
        testAlertRule.setOperator("INVALID");

        assertThrows(IllegalArgumentException.class, () -> alertEvaluationService.evaluateAlertRule(testAlertRule));
        verify(monitoringService, never()).getMetricData(anyString(), anyString(), anyMap(), anyString());
        verify(alertService, never()).createAlert(any(Alert.class));
        verify(notificationService, never()).sendNotification(any(Alert.class));
    }
} 