-- 告警规则表
CREATE TABLE IF NOT EXISTS `alert_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500),
    `datasource_type` VARCHAR(50) NOT NULL,
    `query` TEXT NOT NULL,
    `condition` VARCHAR(500) NOT NULL,
    `severity` VARCHAR(20) NOT NULL,
    `enabled` BOOLEAN DEFAULT TRUE,
    `notify_channels` VARCHAR(200),
    `notify_template` TEXT,
    `notify_target` VARCHAR(200),
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 告警历史记录表
CREATE TABLE IF NOT EXISTS `alert_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `rule_id` BIGINT NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `message` TEXT,
    `severity` VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `resolved_at` DATETIME,
    `notify_status` VARCHAR(20),
    `notify_message` TEXT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`rule_id`) REFERENCES `alert_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 