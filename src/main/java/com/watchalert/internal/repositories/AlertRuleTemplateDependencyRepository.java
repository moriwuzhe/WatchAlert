package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTemplateDependencyRepository extends JpaRepository<AlertRuleTemplateDependency, Long> {
    
    /**
     * 根据模板ID查找依赖
     */
    List<AlertRuleTemplateDependency> findByTemplateId(Long templateId);
    
    /**
     * 根据依赖ID查找被依赖的模板
     */
    List<AlertRuleTemplateDependency> findByDependencyId(Long dependencyId);
    
    /**
     * 根据模板ID和依赖类型查找依赖
     */
    List<AlertRuleTemplateDependency> findByTemplateIdAndDependencyType(Long templateId, String dependencyType);
    
    /**
     * 根据模板ID和是否必需查找依赖
     */
    List<AlertRuleTemplateDependency> findByTemplateIdAndRequired(Long templateId, boolean required);
    
    /**
     * 统计模板的依赖数量
     */
    long countByTemplateId(Long templateId);
    
    /**
     * 统计被依赖的数量
     */
    long countByDependencyId(Long dependencyId);
    
    /**
     * 删除模板的所有依赖
     */
    void deleteByTemplateId(Long templateId);
    
    /**
     * 删除依赖的所有被依赖关系
     */
    void deleteByDependencyId(Long dependencyId);
    
    /**
     * 检查是否存在循环依赖
     */
    boolean existsByTemplateIdAndDependencyId(Long templateId, Long dependencyId);
} 