package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 告警历史实体类
 */
@Data
@Entity
@Table(name = "alert_history")
public class AlertHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 关联的告警规则ID
     */
    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private AlertRule rule;
    
    /**
     * 告警级别
     */
    @Column(nullable = false)
    private String severity;
    
    /**
     * 告警状态
     */
    @Column(nullable = false)
    private String status;
    
    /**
     * 告警来源
     */
    @Column(nullable = false)
    private String source;
    
    /**
     * 告警触发时间
     */
    @Column(nullable = false)
    private LocalDateTime triggerTime;
    
    /**
     * 告警消息
     */
    @Column
    private String message;
    
    /**
     * 告警次数
     */
    @Column
    private Integer count;
    
    /**
     * 首次发生时间
     */
    @Column
    private LocalDateTime firstOccurrence;
    
    /**
     * 最后发生时间
     */
    @Column
    private LocalDateTime lastOccurrence;
    
    /**
     * 通知状态
     */
    @Column
    private String notifyStatus;
    
    /**
     * 通知消息
     */
    @Column
    private String notifyMessage;
    
    /**
     * 通知错误信息
     */
    @Column
    private String notifyError;
    
    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime resolvedAt;
    
    @Column
    private String resolvedBy;
    
    @Column
    private String resolution;
    
    @Column
    private Boolean recoveryNotified;
    
    @Column
    private Integer escalationLevel;
    
    @Column
    private LocalDateTime lastEscalationTime;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        recoveryNotified = false;
        escalationLevel = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 