package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.AlertRuleTemplatePermission;
import com.watchalert.internal.repositories.AlertRuleTemplatePermissionRepository;
import com.watchalert.internal.services.AlertRuleTemplatePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplatePermissionServiceImpl implements AlertRuleTemplatePermissionService {

    @Autowired
    private AlertRuleTemplatePermissionRepository permissionRepository;

    @Override
    @Transactional
    public AlertRuleTemplatePermission createPermission(Long templateId, String userId, String permissionType, String createdBy) {
        log.debug("Creating permission for template: {}, user: {}", templateId, userId);
        
        AlertRuleTemplatePermission permission = new AlertRuleTemplatePermission();
        permission.setTemplateId(templateId);
        permission.setUserId(userId);
        permission.setPermissionType(permissionType);
        permission.setActive(true);
        permission.setCreatedBy(createdBy);
        
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void updatePermission(AlertRuleTemplatePermission permission) {
        log.debug("Updating permission: {}", permission.getId());
        permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        log.debug("Deleting permission: {}", id);
        permissionRepository.deleteById(id);
    }

    @Override
    public AlertRuleTemplatePermission getPermission(Long id) {
        log.debug("Getting permission: {}", id);
        return permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + id));
    }

    @Override
    public List<AlertRuleTemplatePermission> getPermissionsByTemplate(Long templateId) {
        log.debug("Getting permissions for template: {}", templateId);
        return permissionRepository.findByTemplateId(templateId);
    }

    @Override
    public List<AlertRuleTemplatePermission> getPermissionsByUser(String userId) {
        log.debug("Getting permissions for user: {}", userId);
        return permissionRepository.findByUserId(userId);
    }

    @Override
    public boolean hasPermission(Long templateId, String userId, String permissionType) {
        log.debug("Checking permission for template: {}, user: {}, type: {}", 
                templateId, userId, permissionType);
        
        // 检查权限是否存在
        if (!permissionRepository.existsByTemplateIdAndUserIdAndPermissionType(
                templateId, userId, permissionType)) {
            return false;
        }
        
        // 获取权限详情
        List<AlertRuleTemplatePermission> permissions = permissionRepository
                .findByTemplateIdAndUserIdAndPermissionType(templateId, userId, permissionType);
        
        // 检查是否有有效的权限
        LocalDateTime now = LocalDateTime.now();
        return permissions.stream().anyMatch(permission -> 
                permission.isActive() && 
                (permission.getExpiresAt() == null || permission.getExpiresAt().isAfter(now)));
    }

    @Override
    @Transactional
    public void setPermissionEnabled(Long id, boolean enabled) {
        log.debug("Setting permission enabled: {}, enabled: {}", id, enabled);
        
        AlertRuleTemplatePermission permission = getPermission(id);
        permission.setActive(enabled);
        permissionRepository.save(permission);
    }

    @Override
    public Map<String, Object> getPermissionStatistics(Long templateId) {
        log.debug("Getting permission statistics for template: {}", templateId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 统计权限数量
        long totalCount = permissionRepository.countByTemplateId(templateId);
        result.put("totalCount", totalCount);
        
        // 按权限类型统计
        List<AlertRuleTemplatePermission> permissions = permissionRepository.findByTemplateId(templateId);
        Map<String, Long> typeCount = permissions.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplatePermission::getPermissionType,
                        Collectors.counting()
                ));
        result.put("typeCount", typeCount);
        
        // 按状态统计
        long activeCount = permissions.stream()
                .filter(AlertRuleTemplatePermission::isActive)
                .count();
        result.put("activeCount", activeCount);
        result.put("inactiveCount", totalCount - activeCount);
        
        // 统计过期权限
        LocalDateTime now = LocalDateTime.now();
        long expiredCount = permissions.stream()
                .filter(p -> p.getExpiresAt() != null && p.getExpiresAt().isBefore(now))
                .count();
        result.put("expiredCount", expiredCount);
        
        return result;
    }

    @Override
    @Transactional
    public void batchUpdatePermissions(Long templateId, List<AlertRuleTemplatePermission> permissions) {
        log.debug("Batch updating permissions for template: {}", templateId);
        
        for (AlertRuleTemplatePermission permission : permissions) {
            permission.setTemplateId(templateId);
            permissionRepository.save(permission);
        }
    }
} 