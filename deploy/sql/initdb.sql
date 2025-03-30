-- MySQL dump 10.13  Distrib 8.0.41, for Linux (aarch64)
--
-- Host: localhost    Database: watchalert
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alert_cur_events`
--

DROP TABLE IF EXISTS `alert_cur_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_cur_events` (
  `tenant_id` longtext,
  `rule_id` longtext,
  `rule_name` longtext,
  `datasource_type` longtext,
  `datasource_id` longtext,
  `fingerprint` longtext,
  `severity` longtext,
  `metric` longtext,
  `labels` longtext,
  `eval_interval` bigint DEFAULT NULL,
  `for_duration` bigint DEFAULT NULL,
  `first_trigger_time` bigint DEFAULT NULL,
  `repeat_notice_interval` bigint DEFAULT NULL,
  `effective_time` longtext,
  `fault_center_id` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_cur_events`
--

LOCK TABLES `alert_cur_events` WRITE;
/*!40000 ALTER TABLE `alert_cur_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_cur_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_data_sources`
--

DROP TABLE IF EXISTS `alert_data_sources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_data_sources` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `labels` longtext,
  `type` longtext,
  `http` longtext,
  `auth` longtext,
  `ds_ali_cloud_config` longtext,
  `aws_cloud_watch` longtext,
  `description` longtext,
  `kube_config` longtext,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_data_sources`
--

LOCK TABLES `alert_data_sources` WRITE;
/*!40000 ALTER TABLE `alert_data_sources` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_data_sources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_his_events`
--

DROP TABLE IF EXISTS `alert_his_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_his_events` (
  `tenant_id` longtext,
  `datasource_id` longtext,
  `datasource_type` longtext,
  `fingerprint` longtext,
  `rule_id` longtext,
  `rule_name` longtext,
  `severity` longtext,
  `metric` longtext,
  `eval_interval` bigint DEFAULT NULL,
  `annotations` longtext,
  `first_trigger_time` bigint DEFAULT NULL,
  `last_eval_time` bigint DEFAULT NULL,
  `last_send_time` bigint DEFAULT NULL,
  `recover_time` bigint DEFAULT NULL,
  `fault_center_id` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_his_events`
--

LOCK TABLES `alert_his_events` WRITE;
/*!40000 ALTER TABLE `alert_his_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_his_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_notices`
--

DROP TABLE IF EXISTS `alert_notices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_notices` (
  `tenant_id` longtext,
  `uuid` longtext,
  `name` longtext,
  `duty_id` longtext,
  `notice_type` longtext,
  `notice_tmpl_id` longtext,
  `hook` longtext,
  `email` longtext,
  `sign` longtext,
  `phone_number` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_notices`
--

LOCK TABLES `alert_notices` WRITE;
/*!40000 ALTER TABLE `alert_notices` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_notices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_rules`
--

DROP TABLE IF EXISTS `alert_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_rules` (
  `tenant_id` longtext,
  `rule_id` longtext,
  `rule_group_id` longtext,
  `datasource_type` longtext,
  `datasource_id_list` longtext,
  `rule_name` longtext,
  `eval_interval` bigint DEFAULT NULL,
  `eval_time_type` longtext,
  `repeat_notice_interval` bigint DEFAULT NULL,
  `description` longtext,
  `effective_time` longtext,
  `severity` longtext,
  `prometheus_config` longtext,
  `ali_cloud_sls_config` longtext,
  `loki_config` longtext,
  `jaeger_config` longtext,
  `cloud_watch_config` longtext,
  `kubernetes_config` longtext,
  `elastic_search_config` longtext,
  `log_eval_condition` longtext,
  `fault_center_id` longtext,
  `enabled` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_rules`
--

LOCK TABLES `alert_rules` WRITE;
/*!40000 ALTER TABLE `alert_rules` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_silences`
--

DROP TABLE IF EXISTS `alert_silences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_silences` (
  `tenant_id` longtext,
  `name` longtext,
  `id` varchar(191) NOT NULL,
  `labels` longtext,
  `starts_at` bigint DEFAULT NULL,
  `update_by` longtext,
  `ends_at` bigint DEFAULT NULL,
  `update_at` bigint DEFAULT NULL,
  `fault_center_id` longtext,
  `comment` longtext,
  `status` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_silences`
--

LOCK TABLES `alert_silences` WRITE;
/*!40000 ALTER TABLE `alert_silences` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_silences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_subscribes`
--

DROP TABLE IF EXISTS `alert_subscribes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_subscribes` (
  `s_id` longtext,
  `s_tenant_id` longtext,
  `s_user_id` longtext,
  `s_user_email` longtext,
  `s_rule_id` longtext,
  `s_rule_name` longtext,
  `s_rule_type` longtext,
  `s_rule_severity` longtext,
  `s_notice_subject` longtext,
  `s_notice_template_id` longtext,
  `s_filter` longtext,
  `s_create_at` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_subscribes`
--

LOCK TABLES `alert_subscribes` WRITE;
/*!40000 ALTER TABLE `alert_subscribes` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_subscribes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `username` longtext,
  `ip_address` longtext,
  `method` longtext,
  `path` longtext,
  `created_at` bigint DEFAULT NULL,
  `status_code` bigint DEFAULT NULL,
  `body` longtext,
  `audit_type` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dashboard_folders`
--

DROP TABLE IF EXISTS `dashboard_folders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dashboard_folders` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `theme` longtext,
  `grafana_version` longtext,
  `grafana_host` longtext,
  `grafana_folder_id` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dashboard_folders`
--

LOCK TABLES `dashboard_folders` WRITE;
/*!40000 ALTER TABLE `dashboard_folders` DISABLE KEYS */;
/*!40000 ALTER TABLE `dashboard_folders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dashboards`
--

DROP TABLE IF EXISTS `dashboards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dashboards` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `name` varchar(191) DEFAULT NULL,
  `url` longtext,
  `folder_id` longtext,
  `description` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_dashboards_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dashboards`
--

LOCK TABLES `dashboards` WRITE;
/*!40000 ALTER TABLE `dashboards` DISABLE KEYS */;
/*!40000 ALTER TABLE `dashboards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `duty_managements`
--

DROP TABLE IF EXISTS `duty_managements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `duty_managements` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `manager` longtext,
  `description` longtext,
  `cur_duty_user` longtext,
  `create_by` longtext,
  `create_at` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `duty_managements`
--

LOCK TABLES `duty_managements` WRITE;
/*!40000 ALTER TABLE `duty_managements` DISABLE KEYS */;
/*!40000 ALTER TABLE `duty_managements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `duty_schedules`
--

DROP TABLE IF EXISTS `duty_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `duty_schedules` (
  `tenant_id` longtext,
  `duty_id` longtext,
  `time` longtext,
  `user_id` longtext,
  `username` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `duty_schedules`
--

LOCK TABLES `duty_schedules` WRITE;
/*!40000 ALTER TABLE `duty_schedules` DISABLE KEYS */;
/*!40000 ALTER TABLE `duty_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `user_id` longtext,
  `user_name` longtext,
  `email` longtext,
  `phone` longtext,
  `password` longtext,
  `role` longtext,
  `create_by` longtext,
  `create_at` bigint DEFAULT NULL,
  `join_duty` longtext,
  `duty_user_id` longtext,
  `tenants` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice_records`
--

DROP TABLE IF EXISTS `notice_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice_records` (
  `date` longtext,
  `create_at` bigint DEFAULT NULL,
  `tenant_id` longtext,
  `rule_name` longtext,
  `n_type` longtext,
  `n_obj` longtext,
  `severity` longtext,
  `status` bigint DEFAULT NULL,
  `alarm_msg` longtext,
  `err_msg` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_records`
--

LOCK TABLES `notice_records` WRITE;
/*!40000 ALTER TABLE `notice_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice_template_examples`
--

DROP TABLE IF EXISTS `notice_template_examples`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice_template_examples` (
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `notice_type` longtext,
  `description` longtext,
  `template` longtext,
  `template_firing` longtext,
  `template_recover` longtext,
  `enable_fei_shu_json_card` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_template_examples`
--

LOCK TABLES `notice_template_examples` WRITE;
/*!40000 ALTER TABLE `notice_template_examples` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice_template_examples` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_groups`
--

DROP TABLE IF EXISTS `rule_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_groups` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `number` bigint DEFAULT NULL,
  `description` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_groups`
--

LOCK TABLES `rule_groups` WRITE;
/*!40000 ALTER TABLE `rule_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_template_groups`
--

DROP TABLE IF EXISTS `rule_template_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_template_groups` (
  `name` varchar(255) NOT NULL,
  `number` bigint DEFAULT NULL,
  `type` longtext,
  `description` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_template_groups`
--

LOCK TABLES `rule_template_groups` WRITE;
/*!40000 ALTER TABLE `rule_template_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_template_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_templates`
--

DROP TABLE IF EXISTS `rule_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_templates` (
  `type` longtext,
  `rule_group_name` longtext,
  `rule_name` varchar(255) NOT NULL,
  `datasource_type` longtext,
  `eval_interval` bigint DEFAULT NULL,
  `for_duration` bigint DEFAULT NULL,
  `repeat_notice_interval` bigint DEFAULT NULL,
  `description` longtext,
  `effective_time` longtext,
  `prometheus_config` longtext,
  `ali_cloud_sls_config` longtext,
  `loki_config` longtext,
  `jaeger_config` longtext,
  `kubernetes_config` longtext,
  `elastic_search_config` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_templates`
--

LOCK TABLES `rule_templates` WRITE;
/*!40000 ALTER TABLE `rule_templates` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `is_init` bigint DEFAULT NULL,
  `email_config` longtext,
  `phone_call_config` longtext,
  `ai_config` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_linked_users`
--

DROP TABLE IF EXISTS `tenant_linked_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_linked_users` (
  `id` varchar(191) NOT NULL,
  `users` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_linked_users`
--

LOCK TABLES `tenant_linked_users` WRITE;
/*!40000 ALTER TABLE `tenant_linked_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_linked_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenants`
--

DROP TABLE IF EXISTS `tenants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenants` (
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `create_at` bigint DEFAULT NULL,
  `create_by` longtext,
  `manager` longtext,
  `description` longtext,
  `user_number` bigint DEFAULT NULL,
  `rule_number` bigint DEFAULT NULL,
  `duty_number` bigint DEFAULT NULL,
  `notice_number` bigint DEFAULT NULL,
  `remove_protection` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenants`
--

LOCK TABLES `tenants` WRITE;
/*!40000 ALTER TABLE `tenants` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_permissions`
--

DROP TABLE IF EXISTS `user_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_permissions` (
  `key` longtext,
  `api` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_permissions`
--

LOCK TABLES `user_permissions` WRITE;
/*!40000 ALTER TABLE `user_permissions` DISABLE KEYS */;
INSERT INTO `user_permissions` VALUES ('更新告警规则组','/api/w8t/ruleGroup/ruleGroupUpdate'),('查看值班表','/api/w8t/dutyManage/dutyManageList'),('查看通知对象','/api/w8t/notice/noticeList'),('查看用户角色','/api/w8t/role/roleList'),('删除告警规则','/api/w8t/rule/ruleDelete'),('更新规则模版','/api/w8t/ruleTmpl/ruleTmplUpdate'),('查看用户列表','/api/w8t/user/userList'),('搜索告警规则','/api/w8t/rule/ruleSearch'),('查看数据源','/api/w8t/datasource/dataSourceList'),('获取仪表盘目录详情','/api/w8t/dashboard/getFolder'),('创建仪表盘目录','/api/w8t/dashboard/createFolder'),('获取通知记录列表','/api/w8t/notice/noticeRecordList'),('获取通知记录指标','/api/w8t/notice/noticeRecordMetric'),('搜索数据源','/api/w8t/datasource/dataSourceSearch'),('查看静默规则','/api/w8t/silence/silenceList'),('查询故障中心','/api/w8t/faultCenter/faultCenterSearch'),('创建数据源','/api/w8t/datasource/dataSourceCreate'),('创建规则模版组','/api/w8t/ruleTmplGroup/ruleTmplGroupCreate'),('更新通知模版','/api/w8t/noticeTemplate/noticeTemplateUpdate'),('删除故障中心','/api/w8t/faultCenter/faultCenterDelete'),('获取租户详细信息','/api/w8t/tenant/getTenant'),('获取拨测规则列表','/api/w8t/probing/listProbing'),('一次性拨测任务','/api/w8t/probing/onceProbing'),('获取值班表用户列表','/api/w8t/calendar/getCalendarUsers'),('查看规则模版','/api/w8t/ruleTmpl/ruleTmplList'),('搜索通知对象','/api/w8t/notice/noticeSearch'),('查看用户权限','/api/w8t/permissions/permsList'),('数据源连接测试','/api/w8t/datasource/dataSourcePing'),('更新故障中心','/api/w8t/faultCenter/faultCenterUpdate'),('创建租户','/api/w8t/tenant/createTenant'),('更新值班表','/api/w8t/dutyManage/dutyManageUpdate'),('获取Kubernetes资源列表','/api/w8t/kubernetes/getResourceList'),('获取Kubernetes事件类型列表','/api/w8t/kubernetes/getReasonList'),('更新规则模版组','/api/w8t/ruleTmplGroup/ruleTmplGroupUpdate'),('获取系统配置','/api/w8t/setting/getSystemSetting'),('删除规则模版','/api/w8t/ruleTmpl/ruleTmplDelete'),('创建故障中心','/api/w8t/faultCenter/faultCenterCreate'),('删除仪表盘','/api/w8t/dashboard/deleteDashboard'),('查看通知模版','/api/w8t/noticeTemplate/noticeTemplateList'),('搜索告警订阅','/api/w8t/subscribe/getSubscribe'),('搜索值班用户','/api/w8t/user/searchDutyUser'),('删除租户成员','/api/w8t/tenant/delUsersOfTenant'),('创建静默规则','/api/w8t/silence/silenceCreate'),('删除静默规则','/api/w8t/silence/silenceDelete'),('Prometheus指标查询','/api/w8t/datasource/promQuery'),('更新用户角色','/api/w8t/role/roleUpdate'),('删除规则模版组','/api/w8t/ruleTmplGroup/ruleTmplGroupDelete'),('删除数据源','/api/w8t/datasource/dataSourceDelete'),('更新数据源','/api/w8t/datasource/dataSourceUpdate'),('删除仪表盘目录','/api/w8t/dashboard/deleteFolder'),('ElasticSearch搜索','/api/w8t/datasource/esSearch'),('创建规则模版','/api/w8t/ruleTmpl/ruleTmplCreate'),('获取故障中心列表','/api/w8t/faultCenter/faultCenterList'),('更新租户信息','/api/w8t/tenant/updateTenant'),('删除告警订阅','/api/w8t/subscribe/deleteSubscribe'),('更新仪表盘目录','/api/w8t/dashboard/updateFolder'),('删除告警规则组','/api/w8t/ruleGroup/ruleGroupDelete'),('获取租户成员列表','/api/w8t/tenant/getUsersForTenant'),('编辑系统配置','/api/w8t/setting/saveSystemSetting'),('获取仪表盘图表列表','/api/w8t/dashboard/listGrafanaDashboards'),('更新静默规则','/api/w8t/silence/silenceUpdate'),('更新仪表盘','/api/w8t/dashboard/updateDashboard'),('修改用户密码','/api/w8t/user/userChangePass'),('获取Jaeger服务列表','/api/w8t/c/getJaegerService'),('搜索用户','/api/w8t/user/searchUser'),('搜索值班表','/api/w8t/dutyManage/dutyManageSearch'),('创建用户角色','/api/w8t/role/roleCreate'),('获取仪表盘','/api/w8t/dashboard/getDashboard'),('删除拨测规则','/api/w8t/probing/deleteProbing'),('搜索日历表','/api/w8t/calendar/calendarSearch'),('查看当前告警事件','/api/w8t/event/curEvent'),('更新告警规则','/api/w8t/rule/ruleUpdate'),('获取仪表盘目录列表','/api/w8t/dashboard/listFolder'),('删除通知模版','/api/w8t/noticeTemplate/noticeTemplateDelete'),('创建告警规则','/api/w8t/rule/ruleCreate'),('用户注册','/api/system/register'),('删除通知对象','/api/w8t/notice/noticeDelete'),('修改故障中心基本信息','/api/w8t/faultCenter/faultCenterReset'),('更新值班表','/api/w8t/dutyManage/dutyManageDelete'),('查看仪表盘','/api/w8t/dashboard/listDashboard'),('搜索仪表盘','/api/w8t/dashboard/searchDashboard'),('创建拨测规则','/api/w8t/probing/createProbing'),('获取拨测规则信息','/api/w8t/probing/searchProbing'),('获取告警订阅','/api/w8t/subscribe/listSubscribe'),('发布日历表','/api/w8t/calendar/calendarCreate'),('创建仪表盘','/api/w8t/dashboard/createDashboard'),('查看规则模版组','/api/w8t/ruleTmplGroup/ruleTmplGroupList'),('查看告警规则组','/api/w8t/ruleGroup/ruleGroupList'),('查看告警规则','/api/w8t/rule/ruleList'),('创建通知模版','/api/w8t/noticeTemplate/noticeTemplateCreate'),('更新日历表','/api/w8t/calendar/calendarUpdate'),('获取数据源','/api/w8t/datasource/dataSourceGet'),('更新用户信息','/api/w8t/user/userUpdate'),('搜索通知模版','/api/w8t/noticeTemplate/searchNoticeTmpl'),('修改租户成员角色','/api/w8t/tenant/changeTenantUserRole'),('更新拨测规则','/api/w8t/probing/updateProbing'),('获取仪表盘完整URL','/api/w8t/dashboard/getDashboardFullUrl'),('创建告警订阅','/api/w8t/subscribe/createSubscribe'),('更新通知对象','/api/w8t/notice/noticeUpdate'),('删除用户','/api/w8t/user/userDelete'),('查看租户','/api/w8t/tenant/getTenantList'),('查看历史告警','/api/w8t/event/hisEvent'),('向租户添加成员','/api/w8t/tenant/addUsersToTenant'),('删除租户','/api/w8t/tenant/deleteTenant'),('创建值班表','/api/w8t/dutyManage/dutyManageCreate'),('创建告警规则组','/api/w8t/ruleGroup/ruleGroupCreate'),('创建通知对象','/api/w8t/notice/noticeCreate'),('删除用户角色','/api/w8t/role/roleDelete');
/*!40000 ALTER TABLE `user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `description` longtext,
  `permissions` longtext,
  `create_at` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES ('admin','admin','system','[{\"key\":\"更新告警规则组\",\"api\":\"/api/w8t/ruleGroup/ruleGroupUpdate\"},{\"key\":\"查看值班表\",\"api\":\"/api/w8t/dutyManage/dutyManageList\"},{\"key\":\"查看通知对象\",\"api\":\"/api/w8t/notice/noticeList\"},{\"key\":\"查看用户角色\",\"api\":\"/api/w8t/role/roleList\"},{\"key\":\"删除告警规则\",\"api\":\"/api/w8t/rule/ruleDelete\"},{\"key\":\"更新规则模版\",\"api\":\"/api/w8t/ruleTmpl/ruleTmplUpdate\"},{\"key\":\"查看用户列表\",\"api\":\"/api/w8t/user/userList\"},{\"key\":\"搜索告警规则\",\"api\":\"/api/w8t/rule/ruleSearch\"},{\"key\":\"查看数据源\",\"api\":\"/api/w8t/datasource/dataSourceList\"},{\"key\":\"获取仪表盘目录详情\",\"api\":\"/api/w8t/dashboard/getFolder\"},{\"key\":\"创建仪表盘目录\",\"api\":\"/api/w8t/dashboard/createFolder\"},{\"key\":\"获取通知记录列表\",\"api\":\"/api/w8t/notice/noticeRecordList\"},{\"key\":\"获取通知记录指标\",\"api\":\"/api/w8t/notice/noticeRecordMetric\"},{\"key\":\"搜索数据源\",\"api\":\"/api/w8t/datasource/dataSourceSearch\"},{\"key\":\"查看静默规则\",\"api\":\"/api/w8t/silence/silenceList\"},{\"key\":\"查询故障中心\",\"api\":\"/api/w8t/faultCenter/faultCenterSearch\"},{\"key\":\"创建数据源\",\"api\":\"/api/w8t/datasource/dataSourceCreate\"},{\"key\":\"创建规则模版组\",\"api\":\"/api/w8t/ruleTmplGroup/ruleTmplGroupCreate\"},{\"key\":\"更新通知模版\",\"api\":\"/api/w8t/noticeTemplate/noticeTemplateUpdate\"},{\"key\":\"删除故障中心\",\"api\":\"/api/w8t/faultCenter/faultCenterDelete\"},{\"key\":\"获取租户详细信息\",\"api\":\"/api/w8t/tenant/getTenant\"},{\"key\":\"获取拨测规则列表\",\"api\":\"/api/w8t/probing/listProbing\"},{\"key\":\"一次性拨测任务\",\"api\":\"/api/w8t/probing/onceProbing\"},{\"key\":\"获取值班表用户列表\",\"api\":\"/api/w8t/calendar/getCalendarUsers\"},{\"key\":\"查看规则模版\",\"api\":\"/api/w8t/ruleTmpl/ruleTmplList\"},{\"key\":\"搜索通知对象\",\"api\":\"/api/w8t/notice/noticeSearch\"},{\"key\":\"查看用户权限\",\"api\":\"/api/w8t/permissions/permsList\"},{\"key\":\"数据源连接测试\",\"api\":\"/api/w8t/datasource/dataSourcePing\"},{\"key\":\"更新故障中心\",\"api\":\"/api/w8t/faultCenter/faultCenterUpdate\"},{\"key\":\"创建租户\",\"api\":\"/api/w8t/tenant/createTenant\"},{\"key\":\"更新值班表\",\"api\":\"/api/w8t/dutyManage/dutyManageUpdate\"},{\"key\":\"获取Kubernetes资源列表\",\"api\":\"/api/w8t/kubernetes/getResourceList\"},{\"key\":\"获取Kubernetes事件类型列表\",\"api\":\"/api/w8t/kubernetes/getReasonList\"},{\"key\":\"更新规则模版组\",\"api\":\"/api/w8t/ruleTmplGroup/ruleTmplGroupUpdate\"},{\"key\":\"获取系统配置\",\"api\":\"/api/w8t/setting/getSystemSetting\"},{\"key\":\"删除规则模版\",\"api\":\"/api/w8t/ruleTmpl/ruleTmplDelete\"},{\"key\":\"创建故障中心\",\"api\":\"/api/w8t/faultCenter/faultCenterCreate\"},{\"key\":\"删除仪表盘\",\"api\":\"/api/w8t/dashboard/deleteDashboard\"},{\"key\":\"查看通知模版\",\"api\":\"/api/w8t/noticeTemplate/noticeTemplateList\"},{\"key\":\"搜索告警订阅\",\"api\":\"/api/w8t/subscribe/getSubscribe\"},{\"key\":\"搜索值班用户\",\"api\":\"/api/w8t/user/searchDutyUser\"},{\"key\":\"删除租户成员\",\"api\":\"/api/w8t/tenant/delUsersOfTenant\"},{\"key\":\"创建静默规则\",\"api\":\"/api/w8t/silence/silenceCreate\"},{\"key\":\"删除静默规则\",\"api\":\"/api/w8t/silence/silenceDelete\"},{\"key\":\"Prometheus指标查询\",\"api\":\"/api/w8t/datasource/promQuery\"},{\"key\":\"更新用户角色\",\"api\":\"/api/w8t/role/roleUpdate\"},{\"key\":\"删除规则模版组\",\"api\":\"/api/w8t/ruleTmplGroup/ruleTmplGroupDelete\"},{\"key\":\"删除数据源\",\"api\":\"/api/w8t/datasource/dataSourceDelete\"},{\"key\":\"更新数据源\",\"api\":\"/api/w8t/datasource/dataSourceUpdate\"},{\"key\":\"删除仪表盘目录\",\"api\":\"/api/w8t/dashboard/deleteFolder\"},{\"key\":\"ElasticSearch搜索\",\"api\":\"/api/w8t/datasource/esSearch\"},{\"key\":\"创建规则模版\",\"api\":\"/api/w8t/ruleTmpl/ruleTmplCreate\"},{\"key\":\"获取故障中心列表\",\"api\":\"/api/w8t/faultCenter/faultCenterList\"},{\"key\":\"更新租户信息\",\"api\":\"/api/w8t/tenant/updateTenant\"},{\"key\":\"删除告警订阅\",\"api\":\"/api/w8t/subscribe/deleteSubscribe\"},{\"key\":\"更新仪表盘目录\",\"api\":\"/api/w8t/dashboard/updateFolder\"},{\"key\":\"删除告警规则组\",\"api\":\"/api/w8t/ruleGroup/ruleGroupDelete\"},{\"key\":\"获取租户成员列表\",\"api\":\"/api/w8t/tenant/getUsersForTenant\"},{\"key\":\"编辑系统配置\",\"api\":\"/api/w8t/setting/saveSystemSetting\"},{\"key\":\"获取仪表盘图表列表\",\"api\":\"/api/w8t/dashboard/listGrafanaDashboards\"},{\"key\":\"更新静默规则\",\"api\":\"/api/w8t/silence/silenceUpdate\"},{\"key\":\"更新仪表盘\",\"api\":\"/api/w8t/dashboard/updateDashboard\"},{\"key\":\"修改用户密码\",\"api\":\"/api/w8t/user/userChangePass\"},{\"key\":\"获取Jaeger服务列表\",\"api\":\"/api/w8t/c/getJaegerService\"},{\"key\":\"搜索用户\",\"api\":\"/api/w8t/user/searchUser\"},{\"key\":\"搜索值班表\",\"api\":\"/api/w8t/dutyManage/dutyManageSearch\"},{\"key\":\"创建用户角色\",\"api\":\"/api/w8t/role/roleCreate\"},{\"key\":\"获取仪表盘\",\"api\":\"/api/w8t/dashboard/getDashboard\"},{\"key\":\"删除拨测规则\",\"api\":\"/api/w8t/probing/deleteProbing\"},{\"key\":\"搜索日历表\",\"api\":\"/api/w8t/calendar/calendarSearch\"},{\"key\":\"查看当前告警事件\",\"api\":\"/api/w8t/event/curEvent\"},{\"key\":\"更新告警规则\",\"api\":\"/api/w8t/rule/ruleUpdate\"},{\"key\":\"获取仪表盘目录列表\",\"api\":\"/api/w8t/dashboard/listFolder\"},{\"key\":\"删除通知模版\",\"api\":\"/api/w8t/noticeTemplate/noticeTemplateDelete\"},{\"key\":\"创建告警规则\",\"api\":\"/api/w8t/rule/ruleCreate\"},{\"key\":\"用户注册\",\"api\":\"/api/system/register\"},{\"key\":\"删除通知对象\",\"api\":\"/api/w8t/notice/noticeDelete\"},{\"key\":\"修改故障中心基本信息\",\"api\":\"/api/w8t/faultCenter/faultCenterReset\"},{\"key\":\"更新值班表\",\"api\":\"/api/w8t/dutyManage/dutyManageDelete\"},{\"key\":\"查看仪表盘\",\"api\":\"/api/w8t/dashboard/listDashboard\"},{\"key\":\"搜索仪表盘\",\"api\":\"/api/w8t/dashboard/searchDashboard\"},{\"key\":\"创建拨测规则\",\"api\":\"/api/w8t/probing/createProbing\"},{\"key\":\"获取拨测规则信息\",\"api\":\"/api/w8t/probing/searchProbing\"},{\"key\":\"获取告警订阅\",\"api\":\"/api/w8t/subscribe/listSubscribe\"},{\"key\":\"发布日历表\",\"api\":\"/api/w8t/calendar/calendarCreate\"},{\"key\":\"创建仪表盘\",\"api\":\"/api/w8t/dashboard/createDashboard\"},{\"key\":\"查看规则模版组\",\"api\":\"/api/w8t/ruleTmplGroup/ruleTmplGroupList\"},{\"key\":\"查看告警规则组\",\"api\":\"/api/w8t/ruleGroup/ruleGroupList\"},{\"key\":\"查看告警规则\",\"api\":\"/api/w8t/rule/ruleList\"},{\"key\":\"创建通知模版\",\"api\":\"/api/w8t/noticeTemplate/noticeTemplateCreate\"},{\"key\":\"更新日历表\",\"api\":\"/api/w8t/calendar/calendarUpdate\"},{\"key\":\"获取数据源\",\"api\":\"/api/w8t/datasource/dataSourceGet\"},{\"key\":\"更新用户信息\",\"api\":\"/api/w8t/user/userUpdate\"},{\"key\":\"搜索通知模版\",\"api\":\"/api/w8t/noticeTemplate/searchNoticeTmpl\"},{\"key\":\"修改租户成员角色\",\"api\":\"/api/w8t/tenant/changeTenantUserRole\"},{\"key\":\"更新拨测规则\",\"api\":\"/api/w8t/probing/updateProbing\"},{\"key\":\"获取仪表盘完整URL\",\"api\":\"/api/w8t/dashboard/getDashboardFullUrl\"},{\"key\":\"创建告警订阅\",\"api\":\"/api/w8t/subscribe/createSubscribe\"},{\"key\":\"更新通知对象\",\"api\":\"/api/w8t/notice/noticeUpdate\"},{\"key\":\"删除用户\",\"api\":\"/api/w8t/user/userDelete\"},{\"key\":\"查看租户\",\"api\":\"/api/w8t/tenant/getTenantList\"},{\"key\":\"查看历史告警\",\"api\":\"/api/w8t/event/hisEvent\"},{\"key\":\"向租户添加成员\",\"api\":\"/api/w8t/tenant/addUsersToTenant\"},{\"key\":\"删除租户\",\"api\":\"/api/w8t/tenant/deleteTenant\"},{\"key\":\"创建值班表\",\"api\":\"/api/w8t/dutyManage/dutyManageCreate\"},{\"key\":\"创建告警规则组\",\"api\":\"/api/w8t/ruleGroup/ruleGroupCreate\"},{\"key\":\"创建通知对象\",\"api\":\"/api/w8t/notice/noticeCreate\"},{\"key\":\"删除用户角色\",\"api\":\"/api/w8t/role/roleDelete\"}]',1742908378);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w8t_ai_content_record`
--

DROP TABLE IF EXISTS `w8t_ai_content_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w8t_ai_content_record` (
  `rule_id` longtext,
  `content` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w8t_ai_content_record`
--

LOCK TABLES `w8t_ai_content_record` WRITE;
/*!40000 ALTER TABLE `w8t_ai_content_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `w8t_ai_content_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w8t_fault_center`
--

DROP TABLE IF EXISTS `w8t_fault_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w8t_fault_center` (
  `tenant_id` longtext,
  `id` varchar(191) NOT NULL,
  `name` longtext,
  `description` longtext,
  `notice_id` longtext,
  `notice_routes` longtext,
  `repeat_notice_interval` bigint DEFAULT NULL,
  `recover_notify` tinyint(1) DEFAULT NULL,
  `aggregation_type` longtext,
  `create_at` bigint DEFAULT NULL,
  `recover_wait_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w8t_fault_center`
--

LOCK TABLES `w8t_fault_center` WRITE;
/*!40000 ALTER TABLE `w8t_fault_center` DISABLE KEYS */;
/*!40000 ALTER TABLE `w8t_fault_center` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `w8t_probing_rule`
--

DROP TABLE IF EXISTS `w8t_probing_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w8t_probing_rule` (
  `tenant_id` longtext,
  `rule_name` longtext,
  `rule_id` longtext,
  `rule_type` longtext,
  `repeat_notice_interval` bigint DEFAULT NULL,
  `severity` longtext,
  `probing_endpoint_config` longtext,
  `notice_id` longtext,
  `annotations` longtext,
  `recover_notify` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `w8t_probing_rule`
--

LOCK TABLES `w8t_probing_rule` WRITE;
/*!40000 ALTER TABLE `w8t_probing_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `w8t_probing_rule` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-26 11:55:04
