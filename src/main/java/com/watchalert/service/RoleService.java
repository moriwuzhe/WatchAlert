package com.watchalert.service;

import com.watchalert.entity.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Role role);
    void deleteRole(String id);
    Role getRole(String id);
    List<Role> getAllRoles();
} 