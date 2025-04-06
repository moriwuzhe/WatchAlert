package com.watchalert.internal.services;

import com.watchalert.internal.models.Permission;
import com.watchalert.internal.models.Role;
import com.watchalert.internal.models.User;

import java.util.List;

public interface PermissionService {
    List<Permission> getUserPermissions(User user);
    List<Role> getUserRoles(User user);
    boolean hasPermission(User user, String resource, String action);
    boolean hasRole(User user, String roleName);
} 