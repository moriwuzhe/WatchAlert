package com.watchalert.controller;

import com.watchalert.model.Alert;
import com.watchalert.service.AlertEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertEvaluationService alertEvaluationService;

    @GetMapping("/active")
    public ResponseEntity<List<Alert>> getActiveAlerts() {
        return ResponseEntity.ok(alertEvaluationService.getActiveAlerts());
    }

    @PostMapping("/{id}/acknowledge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> acknowledgeAlert(@PathVariable String id) {
        alertEvaluationService.acknowledgeAlert(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resolveAlert(@PathVariable String id) {
        alertEvaluationService.resolveAlert(id);
        return ResponseEntity.ok().build();
    }
} 