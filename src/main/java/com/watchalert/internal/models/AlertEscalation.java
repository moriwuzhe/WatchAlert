package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_escalation")
public class AlertEscalation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private AlertRule rule;
    
    @Column(nullable = false)
    private Integer level;
    
    @Column(nullable = false)
    private Integer delayMinutes;
    
    @Column(nullable = false)
    private String notifyChannels;
    
    @Column
    private String notifyUsers;
    
    @Column
    private String notifyGroups;
    
    @Column
    private String notifyTemplate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
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