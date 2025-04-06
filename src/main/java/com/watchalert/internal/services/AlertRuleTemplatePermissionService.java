package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTemplatePermission;
import java.util.List;
import java.util.Map;

public interface AlertRuleTemplatePermissionService {
    
    /**
     * 创建模板权限
     *
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permissionType 权限类型
     * @param createdBy 创建人
     * @return 创建的权限
     */
    AlertRuleTemplatePermission createPermission(Long templateId, String userId, String permissionType, String createdBy);
    
    /**
     * 更新模板权限
     *
     * @param permission 权限对象
     */
    void updatePermission(AlertRuleTemplatePermission permission);
    
    /**
     * 删除模板权限
     *
     * @param id 权限ID
     */
    void deletePermission(Long id);
    
    /**
     * 获取模板权限
     *
     * @param id 权限ID
     * @return 权限对象
     */
    AlertRuleTemplatePermission getPermission(Long id);
    
    /**
     * 获取模板的所有权限
     *
     * @param templateId 模板ID
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> getPermissionsByTemplate(Long templateId);
    
    /**
     * 获取用户的所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<AlertRuleTemplatePermission> getPermissionsByUser(String userId);
    
    /**
     * 检查用户是否有指定权限
     *
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permissionType 权限类型
     * @return 是否有权限
     */
    boolean hasPermission(Long templateId, String userId, String permissionType);
    
    /**
     * 启用/禁用权限
     *
     * @param id 权限ID
     * @param enabled 是否启用
     */
    void setPermissionEnabled(Long id, boolean enabled);
    
    /**
     * 获取权限统计信息
     *
     * @param templateId 模板ID
     * @return 统计信息
     */
    Map<String, Object> getPermissionStatistics(Long templateId);
    
    /**
     * 批量更新权限
     *
     * @param templateId 模板ID
     * @param permissions 权限列表
     */
    void batchUpdatePermissions(Long templateId, List<AlertRuleTemplatePermission> permissions);
} 