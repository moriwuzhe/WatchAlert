-- 创建通知模板表
CREATE TABLE notification_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    channel VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    description VARCHAR(500),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- 插入默认邮件模板
INSERT INTO notification_templates (name, channel, content, description, enabled, created_at, updated_at)
VALUES (
    '默认邮件模板',
    'email',
    '告警规则: ${rule.name}\n严重程度: ${rule.severity}\n告警消息: ${history.message}\n触发时间: ${history.createdAt}',
    '默认的邮件通知模板',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 插入默认邮件主题模板
INSERT INTO notification_templates (name, channel, content, description, enabled, created_at, updated_at)
VALUES (
    '默认邮件模板_subject',
    'email',
    '告警通知: ${rule.name}',
    '默认的邮件通知主题模板',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 插入默认Webhook模板
INSERT INTO notification_templates (name, channel, content, description, enabled, created_at, updated_at)
VALUES (
    '默认Webhook模板',
    'webhook',
    '{\n    "rule": "${rule.name}",\n    "severity": "${rule.severity}",\n    "message": "${history.message}",\n    "timestamp": "${history.createdAt}"\n}',
    '默认的Webhook通知模板',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 插入默认Slack模板
INSERT INTO notification_templates (name, channel, content, description, enabled, created_at, updated_at)
VALUES (
    '默认Slack模板',
    'slack',
    '*告警通知*\n规则: ${rule.name}\n严重程度: ${rule.severity}\n消息: ${history.message}\n时间: ${history.createdAt}',
    '默认的Slack通知模板',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
); 