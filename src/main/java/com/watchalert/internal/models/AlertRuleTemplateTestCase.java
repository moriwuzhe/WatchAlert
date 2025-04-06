package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_template_test_cases")
public class AlertRuleTemplateTestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "test_data", columnDefinition = "TEXT", nullable = false)
    private String testData;

    @Column(name = "expected_result", columnDefinition = "TEXT", nullable = false)
    private String expectedResult;

    @Column(name = "test_type", nullable = false)
    private String testType; // POSITIVE, NEGATIVE, EDGE_CASE

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "status", nullable = false)
    private String status; // PASSED, FAILED, PENDING

    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;

    @Column(name = "last_run_result", columnDefinition = "TEXT")
    private String lastRunResult;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 