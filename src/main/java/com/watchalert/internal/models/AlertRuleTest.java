package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_test")
public class AlertRuleTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private AlertRule rule;
    
    @Column(nullable = false)
    private String testData;
    
    @Column(nullable = false)
    private boolean expectedResult;
    
    @Column
    private String actualResult;
    
    @Column
    private String errorMessage;
    
    @Column(nullable = false)
    private LocalDateTime executedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        executedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 