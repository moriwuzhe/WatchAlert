package com.watchalert.internal.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 告警规则模板依赖测试用例关系实体类
 */
@Entity
@Table(name = "alert_rule_template_dependency_test_case_relation")
@Data
public class AlertRuleTemplateDependencyTestCaseRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 源测试用例ID
     */
    @Column(name = "source_test_case_id", nullable = false)
    private Long sourceTestCaseId;

    /**
     * 目标测试用例ID
     */
    @Column(name = "target_test_case_id", nullable = false)
    private Long targetTestCaseId;

    /**
     * 依赖类型（PREREQUISITE/RELATED/DUPLICATE）
     */
    @Column(name = "dependency_type", nullable = false)
    private String dependencyType;

    /**
     * 依赖描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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