package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRule;
import java.util.Map;

public interface QueryExecutor {
    Map<String, Object> executeQuery(AlertRule rule);
} 