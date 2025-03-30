-- 修改 alert_rules 表的 rule_id 列类型
ALTER TABLE alert_rules
MODIFY COLUMN rule_id VARCHAR(191) NOT NULL;

-- 修改 alerts 表的 alert_rule_id 列类型
ALTER TABLE alerts
MODIFY COLUMN alert_rule_id VARCHAR(191) NOT NULL;

-- 修改 alert_history 表的 alert_rule_id 列类型
ALTER TABLE alert_history
MODIFY COLUMN alert_rule_id VARCHAR(191) NOT NULL;

-- 修改 alert_rule_notification_channels 表的 alert_rule_id 列类型
ALTER TABLE alert_rule_notification_channels
MODIFY COLUMN alert_rule_id VARCHAR(191) NOT NULL;

-- 为 alerts 表添加外键约束
ALTER TABLE alerts
ADD CONSTRAINT fk_alert_rule
FOREIGN KEY (alert_rule_id) REFERENCES alert_rules(rule_id);

-- 为 alert_history 表添加外键约束
ALTER TABLE alert_history
ADD CONSTRAINT fk_alert_history_rule
FOREIGN KEY (alert_rule_id) REFERENCES alert_rules(rule_id);

-- 为 alert_rule_notification_channels 表添加外键约束
ALTER TABLE alert_rule_notification_channels
ADD CONSTRAINT fk_notification_channel_rule
FOREIGN KEY (alert_rule_id) REFERENCES alert_rules(rule_id); 