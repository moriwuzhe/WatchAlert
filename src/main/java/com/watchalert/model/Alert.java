package com.watchalert.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alerts")
public class Alert {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String target;

    @Column(nullable = false)
    private String metric;

    @Column(name = "rule_id", nullable = false)
    private String ruleId;

    @ManyToOne
    @JoinColumn(name = "alert_rule_id", nullable = false)
    private AlertRule alertRule;

    @Column(nullable = false)
    private String status; // OPEN, CLOSED, ACKNOWLEDGED

    @Column(name = "metric_value", nullable = false)
    private Double value;

    @Column(nullable = false)
    private Double threshold;

    @Column(nullable = false)
    private String operator;

    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "acknowledged_by")
    private String acknowledgedBy;

    @Column(name = "notification_sent")
    private boolean notificationSent = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
} 