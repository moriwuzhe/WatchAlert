package com.watchalert.service;

import com.watchalert.entity.Permission;
import java.util.List;

public interface PermissionService {
    Permission createPermission(Permission permission);
    Permission updatePermission(Permission permission);
    void deletePermission(String id);
    Permission getPermission(String id);
    List<Permission> getAllPermissions();
} 