package com.watchalert.internal.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_template_dependency_versions")
public class AlertRuleTemplateDependencyVersion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dependency_id", nullable = false)
    private Long dependencyId;
    
    @Column(name = "version_number", nullable = false, length = 50)
    private String versionNumber;
    
    @Column(name = "version_type", nullable = false, length = 50)
    private String versionType;
    
    @Column(name = "version_constraint", nullable = false, length = 100)
    private String versionConstraint;
    
    @Column(name = "description", length = 500)
    private String description;
    
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