package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 告警规则实体类
 */
@Data
@Entity
@Table(name = "alert_rule")
public class AlertRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 规则名称
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * 规则描述
     */
    @Column(nullable = false)
    private String description;
    
    /**
     * 数据源类型
     */
    @Column(nullable = false)
    private String datasourceType;
    
    /**
     * 查询语句
     */
    @Column(nullable = false)
    private String query;
    
    /**
     * 告警条件
     */
    @Column(nullable = false)
    private String condition;
    
    /**
     * 告警级别
     */
    @Column(nullable = false)
    private String severity;
    
    /**
     * 通知渠道（多个渠道用逗号分隔）
     */
    @Column
    private String notifyChannels;
    
    /**
     * 通知目标
     */
    @Column
    private String notifyUsers;
    
    /**
     * 通知组
     */
    @Column
    private String notifyGroups;
    
    /**
     * 通知模板
     */
    @Column
    private String notifyTemplate;
    
    /**
     * 标签
     */
    @Column
    private String tags;
    
    /**
     * 是否启用
     */
    @Column(nullable = false)
    private boolean enabled;
    
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        enabled = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 