package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertSuppression;
import com.watchalert.internal.models.AlertHistory;
import java.util.List;

public interface AlertSuppressionService {
    AlertSuppression createSuppression(AlertSuppression suppression);
    void updateSuppression(AlertSuppression suppression);
    void deleteSuppression(Long id);
    AlertSuppression getSuppression(Long id);
    List<AlertSuppression> getSuppressionsByRule(Long ruleId);
    List<AlertSuppression> getEnabledSuppressions();
    boolean shouldSuppressAlert(AlertHistory alert);
    void checkAndApplySuppression(AlertHistory alert);
} 