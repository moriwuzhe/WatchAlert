package com.watchalert.service.impl;

import com.watchalert.entity.Permission;
import com.watchalert.repository.PermissionRepository;
import com.watchalert.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        log.info("Creating new permission: {}", permission.getName());
        permission.setCreatedAt(LocalDateTime.now());
        permission.setUpdatedAt(LocalDateTime.now());
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        log.info("Updating permission: {}", permission.getId());
        permission.setUpdatedAt(LocalDateTime.now());
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(String id) {
        log.info("Deleting permission: {}", id);
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission getPermission(String id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
} 