package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule_version")
public class AlertRuleVersion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private AlertRule rule;
    
    @Column(nullable = false)
    private String version;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String datasourceType;
    
    @Column(nullable = false)
    private String query;
    
    @Column(nullable = false)
    private String condition;
    
    @Column(nullable = false)
    private String severity;
    
    @Column(nullable = false)
    private String notifyChannels;
    
    @Column(nullable = false)
    private String notifyUsers;
    
    @Column(nullable = false)
    private String notifyGroups;
    
    @Column(nullable = false)
    private String notifyTemplate;
    
    @Column(nullable = false)
    private String tags;
    
    @Column(nullable = false)
    private boolean enabled;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "comment")
    private String comment;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 