package com.watchalert.internal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据源实体类
 */
@Data
@Entity
@Table(name = "datasources")
public class Datasource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 数据源名称
     */
    @Column(name = "name", nullable = false)
    private String name;
    
    /**
     * 数据源类型
     */
    @Column(name = "type", nullable = false)
    private String type;
    
    /**
     * 数据源URL
     */
    @Column(name = "url", nullable = false)
    private String url;
    
    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;
    
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;
    
    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
    
    /**
     * 连接超时时间（毫秒）
     */
    @Column(name = "connect_timeout")
    private Integer connectTimeout = 5000;
    
    /**
     * 读取超时时间（毫秒）
     */
    @Column(name = "read_timeout")
    private Integer readTimeout = 5000;
    
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