package com.watchalert.internal.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServiceManager {
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private LdapService ldapService;

    public void initialize() {
        // 初始化各个服务
        datasourceService.initialize();
        userService.initialize();
        roleService.initialize();
        permissionService.initialize();
    }
} 