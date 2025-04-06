package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "alert_rule_templates")
public class AlertRuleTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String category;
    
    @Column(name = "query", nullable = false, length = 4000)
    private String query;
    
    @Column(name = "condition", nullable = false, length = 1000)
    private String condition;
    
    @Column(nullable = false)
    private String severity;
    
    @Column(name = "notification_config", length = 1000)
    private String notificationConfig;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<AlertRule> rules;
    
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