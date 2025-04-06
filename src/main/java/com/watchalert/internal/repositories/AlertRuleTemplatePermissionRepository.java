package com.watchalert.internal.repositories;

import com.watchalert.internal.models.AlertRuleTemplatePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRuleTemplatePermissionRepository extends JpaRepository<AlertRuleTemplatePermission, Long> {
    
    /**
     * 根据模板ID查找权限
     *
     * @param templateId 模板ID
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> findByTemplateId(Long templateId);
    
    /**
     * 根据用户ID查找权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> findByUserId(String userId);
    
    /**
     * 根据模板ID和用户ID查找权限
     *
     * @param templateId 模板ID
     * @param userId 用户ID
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> findByTemplateIdAndUserId(Long templateId, String userId);
    
    /**
     * 根据模板ID和权限类型查找权限
     *
     * @param templateId 模板ID
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> findByTemplateIdAndPermissionType(Long templateId, String permissionType);
    
    /**
     * 根据模板ID和用户ID和权限类型查找权限
     *
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> findByTemplateIdAndUserIdAndPermissionType(Long templateId, String userId, String permissionType);
    
    /**
     * 检查权限是否存在
     *
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permissionType 权限类型
     * @return 是否存在
     */
    boolean existsByTemplateIdAndUserIdAndPermissionType(Long templateId, String userId, String permissionType);
    
    /**
     * 统计指定模板ID的权限数量
     *
     * @param templateId 模板ID
     * @return 权限数量
     */
    long countByTemplateId(Long templateId);
    
    /**
     * 统计指定模板ID和状态的权限数量
     *
     * @param templateId 模板ID
     * @param active 是否激活
     * @return 权限数量
     */
    long countByTemplateIdAndActive(Long templateId, boolean active);
} 