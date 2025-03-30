package com.watchalert.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "alert_rules")
public class AlertRule {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String target;

    @Column(nullable = false)
    private String metric;

    @Column(nullable = false)
    private Double threshold;

    @Column(nullable = false)
    private String operator;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String severity;

    @ElementCollection
    @CollectionTable(name = "alert_rule_notification_channels")
    @Column(name = "channel")
    private List<String> notificationChannels;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "enabled")
    private boolean enabled = true;
} 