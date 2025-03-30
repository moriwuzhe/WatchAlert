package com.watchalert.service;

import com.watchalert.entity.Role;
import com.watchalert.entity.User;
import com.watchalert.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    UserRole assignRole(User user, Role role);
    void removeRole(User user, Role role);
    List<UserRole> getUserRoles(User user);
    List<UserRole> getRoleUsers(Role role);
} 