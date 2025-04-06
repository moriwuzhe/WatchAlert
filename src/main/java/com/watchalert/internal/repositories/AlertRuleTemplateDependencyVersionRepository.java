package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateDependencyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRuleTemplateDependencyVersionRepository extends JpaRepository<AlertRuleTemplateDependencyVersion, Long> {
    
    /**
     * 根据依赖ID查找版本
     */
    List<AlertRuleTemplateDependencyVersion> findByDependencyId(Long dependencyId);
    
    /**
     * 根据依赖ID和版本类型查找版本
     */
    List<AlertRuleTemplateDependencyVersion> findByDependencyIdAndVersionType(Long dependencyId, String versionType);
    
    /**
     * 根据依赖ID和版本号查找版本
     */
    AlertRuleTemplateDependencyVersion findByDependencyIdAndVersionNumber(Long dependencyId, String versionNumber);
    
    /**
     * 统计依赖的版本数量
     */
    long countByDependencyId(Long dependencyId);
    
    /**
     * 删除依赖的所有版本
     */
    void deleteByDependencyId(Long dependencyId);
} 