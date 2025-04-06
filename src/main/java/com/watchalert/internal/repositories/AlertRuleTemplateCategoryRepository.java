package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTemplateCategoryRepository extends JpaRepository<AlertRuleTemplateCategory, Long> {
    
    /**
     * 根据父级ID查找分类
     *
     * @param parentId 父级ID
     * @return 分类列表
     */
    List<AlertRuleTemplateCategory> findByParentId(Long parentId);
    
    /**
     * 根据层级查找分类
     *
     * @param level 层级
     * @return 分类列表
     */
    List<AlertRuleTemplateCategory> findByLevel(int level);
    
    /**
     * 根据名称查找分类
     *
     * @param name 分类名称
     * @return 分类
     */
    AlertRuleTemplateCategory findByName(String name);
    
    /**
     * 检查分类名称是否存在
     *
     * @param name 分类名称
     * @return 是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 根据父级ID和排序号查找分类
     *
     * @param parentId 父级ID
     * @param sort 排序号
     * @return 分类列表
     */
    List<AlertRuleTemplateCategory> findByParentIdAndSortGreaterThan(Long parentId, int sort);
} 