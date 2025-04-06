package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplateVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTemplateVersionRepository extends JpaRepository<AlertRuleTemplateVersion, Long> {
    
    /**
     * 根据模板ID查找版本列表，按创建时间降序排序
     *
     * @param templateId 模板ID
     * @return 版本列表
     */
    List<AlertRuleTemplateVersion> findByTemplateIdOrderByCreatedAtDesc(Long templateId);
    
    /**
     * 根据模板ID和版本类型查找版本列表
     *
     * @param templateId 模板ID
     * @param versionType 版本类型
     * @return 版本列表
     */
    List<AlertRuleTemplateVersion> findByTemplateIdAndVersionType(Long templateId, String versionType);
    
    /**
     * 根据模板ID和版本号查找版本
     *
     * @param templateId 模板ID
     * @param versionNumber 版本号
     * @return 版本
     */
    AlertRuleTemplateVersion findByTemplateIdAndVersionNumber(Long templateId, String versionNumber);
    
    /**
     * 统计指定模板的版本数量
     *
     * @param templateId 模板ID
     * @return 版本数量
     */
    long countByTemplateId(Long templateId);
    
    /**
     * 统计指定模板和版本类型的版本数量
     *
     * @param templateId 模板ID
     * @param versionType 版本类型
     * @return 版本数量
     */
    long countByTemplateIdAndVersionType(Long templateId, String versionType);
} 