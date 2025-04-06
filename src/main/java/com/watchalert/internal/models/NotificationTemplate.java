package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知模板实体类
 */
@Data
@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 模板描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 通知渠道
     */
    @Column(name = "channel", nullable = false)
    private String channel;

    /**
     * 模板内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 