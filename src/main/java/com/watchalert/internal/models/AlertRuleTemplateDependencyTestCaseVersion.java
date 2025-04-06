package com.watchalert.internal.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 告警规则模板依赖测试用例版本实体类
 */
@Entity
@Table(name = "alert_rule_template_dependency_test_case_version")
@Data
public class AlertRuleTemplateDependencyTestCaseVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 测试用例ID
     */
    @Column(name = "test_case_id", nullable = false)
    private Long testCaseId;

    /**
     * 版本号
     */
    @Column(name = "version_number", nullable = false)
    private String versionNumber;

    /**
     * 版本类型（MAJOR/MINOR/PATCH）
     */
    @Column(name = "version_type", nullable = false)
    private String versionType;

    /**
     * 测试数据
     */
    @Column(name = "test_data", columnDefinition = "TEXT")
    private String testData;

    /**
     * 预期结果
     */
    @Column(name = "expected_result", columnDefinition = "TEXT")
    private String expectedResult;

    /**
     * 测试类型
     */
    @Column(name = "test_type")
    private String testType;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 变更描述
     */
    @Column(name = "change_description", columnDefinition = "TEXT")
    private String changeDescription;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 创建时的回调方法
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 更新时的回调方法
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 