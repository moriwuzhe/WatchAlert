package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_statistics")
public class AlertStatistics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long ruleId;
    
    @Column(nullable = false)
    private String ruleName;
    
    @Column(nullable = false)
    private String severity;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column(nullable = false)
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private Integer totalAlerts;
    
    @Column(nullable = false)
    private Integer activeAlerts;
    
    @Column(nullable = false)
    private Integer resolvedAlerts;
    
    @Column(nullable = false)
    private Integer escalatedAlerts;
    
    @Column(nullable = false)
    private Double averageResolutionTime;
    
    @Column(nullable = false)
    private Double averageEscalationTime;
    
    @Column(nullable = false)
    private Integer notificationCount;
    
    @Column(nullable = false)
    private Integer notificationSuccessCount;
    
    @Column(nullable = false)
    private Integer notificationFailureCount;
    
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