package com.watchalert.internal.services;

import com.watchalert.internal.models.User;
import java.util.List;

public interface LdapService {
    /**
     * 初始化LDAP服务
     */
    void initialize();

    /**
     * 从LDAP同步用户
     */
    void syncUsers();

    /**
     * 验证用户凭据
     */
    boolean authenticate(String username, String password);

    /**
     * 获取用户的所有组
     */
    List<String> getUserGroups(String username);

    /**
     * 从LDAP获取用户信息
     */
    User getUserFromLdap(String username);
} 