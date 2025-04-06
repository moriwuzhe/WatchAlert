package com.watchalert.internal.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_template_dependency_test_cases")
public class AlertRuleTemplateDependencyTestCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dependency_id", nullable = false)
    private Long dependencyId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "test_data", columnDefinition = "TEXT")
    private String testData;
    
    @Column(name = "expected_result", columnDefinition = "TEXT")
    private String expectedResult;
    
    @Column(name = "test_type", nullable = false, length = 50)
    private String testType;
    
    @Column(name = "priority", nullable = false)
    private int priority;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status;
    
    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;
    
    @Column(name = "last_run_result", length = 50)
    private String lastRunResult;
    
    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 