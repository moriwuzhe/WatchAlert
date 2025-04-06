package com.watchalert.api;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertHistory;
import com.watchalert.internal.services.AlertService;
import com.watchalert.internal.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/alert/rules")
public class AlertRuleController {

    @Autowired
    private AlertService alertService;
    
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<AlertRule>> getAllRules() {
        return ResponseEntity.ok(alertService.getEnabledRules());
    }

    @PostMapping
    public ResponseEntity<AlertRule> createRule(@RequestBody AlertRule rule) {
        return ResponseEntity.ok(alertService.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRule(@PathVariable Long id, @RequestBody AlertRule rule) {
        rule.setId(id);
        alertService.updateRule(rule);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        alertService.deleteRule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<AlertHistory>> getAlertHistory(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.getAlertHistory(id));
    }

    @PostMapping("/{id}/test")
    public ResponseEntity<Void> testRule(@PathVariable Long id) {
        AlertRule rule = alertService.getEnabledRules().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Rule not found"));
        
        alertService.evaluateRule(rule);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test-notification")
    public ResponseEntity<Void> testNotification(@RequestParam String channel, @RequestParam String message) {
        notificationService.sendTestNotification(channel, message);
        return ResponseEntity.ok().build();
    }
} 