package com.watchalert.internal.services;

import com.watchalert.internal.models.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    void initialize();
    Role findById(Long id);
    Role createRole(Role role);
    void updateRole(Role role);
    void deleteRole(Long id);
} 