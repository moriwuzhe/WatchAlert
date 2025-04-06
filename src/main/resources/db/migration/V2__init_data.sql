-- 插入管理员角色
INSERT INTO `role` (`name`, `description`, `enabled`) 
VALUES ('ROLE_ADMIN', '系统管理员', true);

-- 插入普通用户角色
INSERT INTO `role` (`name`, `description`, `enabled`) 
VALUES ('ROLE_USER', '普通用户', true);

-- 插入管理员用户 (密码: admin123)
INSERT INTO `user` (`username`, `password`, `email`, `enabled`, `display_name`) 
VALUES ('admin', '$2a$10$rDkPvvAFV6GgJzKYQ5wNYOYzJqzqxX5Y5.5.5.5.5.5.5.5.5.5', 'admin@watchalert.com', true, '系统管理员');

-- 关联管理员用户和角色
INSERT INTO `user_role` (`user_id`, `role_id`) 
VALUES (1, 1);

-- 插入基本权限
INSERT INTO `permission` (`name`, `description`, `resource`, `action`, `enabled`) VALUES
('user:read', '查看用户', 'user', 'read', true),
('user:write', '修改用户', 'user', 'write', true),
('role:read', '查看角色', 'role', 'read', true),
('role:write', '修改角色', 'role', 'write', true),
('permission:read', '查看权限', 'permission', 'read', true),
('permission:write', '修改权限', 'permission', 'write', true),
('datasource:read', '查看数据源', 'datasource', 'read', true),
('datasource:write', '修改数据源', 'datasource', 'write', true); 