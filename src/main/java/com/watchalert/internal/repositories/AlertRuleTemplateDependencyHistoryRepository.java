package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateDependencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRuleTemplateDependencyHistoryRepository extends JpaRepository<AlertRuleTemplateDependencyHistory, Long> {
    
    /**
     * 根据依赖ID查找变更历史
     */
    List<AlertRuleTemplateDependencyHistory> findByDependencyIdOrderByCreatedAtDesc(Long dependencyId);
    
    /**
     * 根据依赖ID和变更类型查找变更历史
     */
    List<AlertRuleTemplateDependencyHistory> findByDependencyIdAndChangeTypeOrderByCreatedAtDesc(
            Long dependencyId, String changeType);
    
    /**
     * 根据依赖ID和时间范围查找变更历史
     */
    List<AlertRuleTemplateDependencyHistory> findByDependencyIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long dependencyId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据变更者查找变更历史
     */
    List<AlertRuleTemplateDependencyHistory> findByChangedByOrderByCreatedAtDesc(String changedBy);
    
    /**
     * 统计依赖的变更次数
     */
    long countByDependencyId(Long dependencyId);
    
    /**
     * 删除依赖的所有变更历史
     */
    void deleteByDependencyId(Long dependencyId);
} 