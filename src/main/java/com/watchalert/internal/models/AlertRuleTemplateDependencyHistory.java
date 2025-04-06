package com.watchalert.internal.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_template_dependency_history")
public class AlertRuleTemplateDependencyHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dependency_id", nullable = false)
    private Long dependencyId;
    
    @Column(name = "change_type", nullable = false, length = 50)
    private String changeType;
    
    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;
    
    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;
    
    @Column(name = "change_reason", length = 500)
    private String changeReason;
    
    @Column(name = "changed_by", nullable = false, length = 100)
    private String changedBy;
    
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