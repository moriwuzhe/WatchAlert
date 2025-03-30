package com.watchalert.controller;

import com.watchalert.model.AlertRule;
import com.watchalert.service.AlertRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alert-rules")
@RequiredArgsConstructor
public class AlertRuleController {

    private final AlertRuleService alertRuleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertRule> createAlertRule(@RequestBody AlertRule rule) {
        return ResponseEntity.ok(alertRuleService.createAlertRule(rule));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertRule> updateAlertRule(@PathVariable String id, @RequestBody AlertRule rule) {
        rule.setId(id);
        return ResponseEntity.ok(alertRuleService.updateAlertRule(rule));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAlertRule(@PathVariable String id) {
        alertRuleService.deleteAlertRule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertRule> getAlertRule(@PathVariable String id) {
        AlertRule rule = alertRuleService.getAlertRule(id);
        return rule != null ? ResponseEntity.ok(rule) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AlertRule>> getAllAlertRules() {
        return ResponseEntity.ok(alertRuleService.getAllAlertRules());
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<AlertRule>> getEnabledAlertRules() {
        return ResponseEntity.ok(alertRuleService.getEnabledAlertRules());
    }
} 