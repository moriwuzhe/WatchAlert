package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_dependency")
public class AlertRuleDependency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private AlertRule rule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_rule_id", nullable = false)
    private AlertRule dependentRule;
    
    @Column(nullable = false)
    private String dependencyType; // 依赖类型：AND/OR
    
    @Column(nullable = false)
    private int priority; // 优先级：数字越小优先级越高
    
    @Column(nullable = false)
    private boolean enabled;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "comment")
    private String comment;
    
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