package com.watchalert.service;

import java.util.List;

public interface LdapService {
    /**
     * 验证用户凭据
     * @param username 用户名
     * @param password 密码
     * @return 验证是否成功
     */
    boolean authenticate(String username, String password);

    /**
     * 获取用户DN
     * @param username 用户名
     * @return 用户DN
     */
    String getUserDn(String username);

    /**
     * 获取用户组
     * @param username 用户名
     * @return 用户组列表
     */
    List<String> getUserGroups(String username);

    /**
     * 检查用户是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean userExists(String username);

    void startSyncUsersCronjob();
    void syncUsers();
} 