/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.6.34-log : Database - easyweb-shiro
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`easyweb-shiro` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `easyweb-shiro`;

/*Table structure for table `sys_authorities` */

DROP TABLE IF EXISTS `sys_authorities`;

CREATE TABLE `sys_authorities` (
  `authority_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `authority_name` varchar(20) NOT NULL COMMENT '权限名称',
  `authority` varchar(40) DEFAULT NULL COMMENT '授权标识',
  `menu_url` varchar(40) DEFAULT NULL COMMENT '菜单url',
  `parent_id` int(11) NOT NULL DEFAULT '-1' COMMENT '父id,-1表示无父级',
  `is_menu` int(1) NOT NULL DEFAULT '0' COMMENT '权限类型,0菜单,1按钮',
  `order_number` int(3) NOT NULL DEFAULT '0' COMMENT '排序号',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`authority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='权限表';

/*Data for the table `sys_authorities` */

insert  into `sys_authorities`(`authority_id`,`authority_name`,`authority`,`menu_url`,`parent_id`,`is_menu`,`order_number`,`menu_icon`,`create_time`,`update_time`) values (1,'系统管理',NULL,NULL,-1,0,1,'layui-icon layui-icon-set','2018-06-29 11:05:41','2019-02-13 13:36:40'),(2,'用户管理',NULL,'system/user',1,0,2,NULL,'2018-06-29 11:05:41','2019-02-12 09:16:42'),(3,'查看用户','user:view',NULL,2,1,3,NULL,'2018-07-21 13:54:16','2019-02-12 09:17:57'),(4,'更新用户','user:update',NULL,2,1,4,NULL,'2018-06-29 11:05:41','2019-02-12 09:17:41'),(5,'角色管理',NULL,'system/role',1,0,5,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:10'),(6,'查看角色','role:view',NULL,5,1,6,NULL,'2018-07-21 13:54:59','2019-02-12 09:26:11'),(7,'更新角色','role:update',NULL,5,1,7,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:12'),(8,'权限管理',NULL,'system/authorities',1,0,8,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:13'),(9,'查看权限','authorities:view',NULL,8,1,9,NULL,'2018-07-21 13:55:57','2019-02-12 09:26:14'),(10,'更新权限','authorities:update',NULL,8,1,10,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:15'),(11,'登录日志','loginRecord:view','system/loginRecord',1,0,11,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:16'),(12,'系统监控','','',-1,0,12,'layui-icon layui-icon-engine','2019-02-13 14:32:17','2019-02-13 14:36:49'),(13,'Druid监控','','druid',12,0,13,'','2019-02-13 14:36:45','2019-02-13 14:36:45');

/*Table structure for table `sys_dictionary` */

DROP TABLE IF EXISTS `sys_dictionary`;

CREATE TABLE `sys_dictionary` (
  `dict_code` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(50) NOT NULL COMMENT '字典名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_code`,`create_time`,`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='字典';

/*Data for the table `sys_dictionary` */

insert  into `sys_dictionary`(`dict_code`,`dict_name`,`description`,`create_time`,`update_time`) values (1,'性别',NULL,'2019-02-11 08:49:43','2019-02-11 08:50:12');

/*Table structure for table `sys_dictionary_data` */

DROP TABLE IF EXISTS `sys_dictionary_data`;

CREATE TABLE `sys_dictionary_data` (
  `dictdata_code` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典项主键',
  `dict_code` int(11) NOT NULL COMMENT '字典主键',
  `dictdata_name` varchar(40) NOT NULL COMMENT '字典项值',
  `is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `sort_number` int(1) NOT NULL COMMENT '排序',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dictdata_code`),
  KEY `FK_Reference_36` (`dict_code`),
  CONSTRAINT `sys_dictionary_data_ibfk_1` FOREIGN KEY (`dict_code`) REFERENCES `sys_dictionary` (`dict_code`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='字典项';

/*Data for the table `sys_dictionary_data` */

insert  into `sys_dictionary_data`(`dictdata_code`,`dict_code`,`dictdata_name`,`is_delete`,`sort_number`,`description`,`create_time`,`update_time`) values (1,1,'男',0,0,NULL,'2019-02-11 08:50:38','2019-02-11 08:51:02'),(2,1,'女',0,1,NULL,'2019-02-11 08:50:38','2019-02-11 08:51:02');

/*Table structure for table `sys_login_record` */

DROP TABLE IF EXISTS `sys_login_record`;

CREATE TABLE `sys_login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `os_name` varchar(40) DEFAULT NULL COMMENT '操作系统',
  `device` varchar(40) DEFAULT NULL COMMENT '设备名',
  `browser_type` varchar(40) DEFAULT NULL COMMENT '浏览器类型',
  `ip_address` varchar(40) DEFAULT NULL COMMENT 'ip地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_login_record_user` (`user_id`),
  CONSTRAINT `sys_login_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=318 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_login_record` */

insert  into `sys_login_record`(`id`,`user_id`,`os_name`,`device`,`browser_type`,`ip_address`,`create_time`) values (1,2,'Windows 10','Windows 10','Chrome','192.168.2.145','2018-02-14 20:38:08'),(2,2,'Android Mobile','MI 6','Chrome Mobile','220.249.67.55','2018-02-18 09:29:51'),(3,2,'Windows 7','Windows 7','Chrome','14.155.89.210','2018-02-22 13:42:23'),(4,2,'Windows 7','Windows 7','Chrome','14.155.89.210','2018-02-22 13:44:58'),(5,2,'Windows 10','Windows 10','Firefox','223.64.149.65','2018-02-22 23:17:06'),(6,2,'Android Mobile','MI 6','Chrome Mobile','192.168.1.153','2018-02-23 12:37:24'),(7,2,'Android Mobile','MI 6','Chrome Mobile','192.168.1.153','2018-02-23 12:37:49'),(8,2,'Windows 7','Windows 7','Chrome','111.47.18.85','2018-02-23 13:11:21'),(9,2,'Windows 7','Windows 7','Chrome','220.248.58.114','2018-02-23 14:33:27'),(10,2,'Windows 10','Windows 10','Chrome','180.168.79.170','2018-02-23 14:42:18'),(11,2,'Windows 7','Windows 7','Internet Explorer 11','218.28.20.161','2018-02-23 15:17:32'),(12,2,'Windows 7','Windows 7','Chrome','14.155.89.225','2018-02-24 14:23:43'),(13,2,'Windows 7','Windows 7','Chrome','111.47.18.85','2018-02-24 16:44:27'),(14,2,'Android Mobile','MI 6','Chrome Mobile','59.174.230.113','2018-02-24 22:29:16'),(15,2,'Windows 10','Windows 10','Internet Explorer 11','222.170.66.226','2018-02-26 11:53:16'),(16,2,'Windows 10','Windows 10','Firefox','222.170.66.226','2018-02-26 11:56:46'),(17,2,'Mac OS X','Mac OS X','Chrome','113.88.14.103','2018-02-27 16:29:01'),(18,2,'Windows 7','Windows 7','Firefox','58.22.48.210','2018-02-27 16:55:36'),(19,2,'Windows 7','Windows 7','Internet Explorer 9','111.19.40.101','2018-02-27 20:25:32'),(20,2,'Mac OS X','Mac OS X','Safari','101.81.118.198','2018-02-28 17:22:42'),(21,2,'Mac OS X','Mac OS X','Chrome','101.81.118.198','2018-02-28 17:24:04'),(22,2,'Mac OS X','Mac OS X','Safari','101.81.118.198','2018-02-28 17:25:27'),(23,2,'Mac OS X','Mac OS X','Chrome','101.81.118.198','2018-02-28 17:29:32'),(24,2,'Android Mobile','wv','Chrome Mobile','42.198.174.26','2018-03-01 00:04:33'),(25,2,'Windows 7','Windows 7','Chrome','125.35.75.30','2018-03-01 09:29:39'),(26,2,'Windows 7','Windows 7','Chrome','111.47.18.85','2018-03-01 09:50:36'),(27,2,'Windows 7','Windows 7','Chrome','125.35.75.30','2018-03-01 10:01:56'),(28,2,'Windows 7','Windows 7','Chrome','111.47.18.85','2018-03-01 10:03:11'),(29,2,'Android Mobile','MI 6','Chrome Mobile','111.47.18.85','2018-03-01 10:25:42'),(30,2,'Windows 7','Windows 7','Chrome','125.35.75.30','2018-03-01 14:47:25'),(31,2,'Windows 10','Windows 10','Internet Explorer 11','183.67.54.25','2018-03-01 15:43:55'),(32,2,'Windows 7','Windows 7','Chrome','111.47.18.85','2018-03-02 08:48:38'),(33,2,'Windows 7','Windows 7','Firefox','183.45.77.183','2018-03-02 12:30:17'),(34,2,'Windows 7','Windows 7','Chrome','103.214.85.94','2018-03-02 12:40:08'),(35,2,'Windows 7','Windows 7','Internet Explorer 11','42.230.213.97','2018-03-02 12:42:14'),(36,2,'Windows 10','Windows 10','Chrome','182.149.198.73','2018-03-02 12:46:40'),(37,2,'Windows 7','Windows 7','Chrome','111.47.18.85','2018-03-02 13:02:39'),(38,2,'Android Mobile','MI 6','Chrome Mobile','111.47.18.85','2018-03-02 13:10:50'),(39,2,'Windows 7','Windows 7','Chrome','218.83.245.210','2018-03-02 13:13:47'),(40,2,'Windows 10','Windows 10','Firefox','14.106.246.152','2018-03-02 13:14:33'),(41,2,'Windows 8.1','Windows 8.1','Internet Explorer 11','116.226.130.159','2018-03-02 13:14:35'),(42,2,'Windows 10','Windows 10','Chrome','120.195.66.188','2018-03-02 13:14:49'),(43,2,'Windows 7','Windows 7','Firefox','1.80.206.196','2018-03-02 13:21:53'),(44,2,'Windows 7','Windows 7','Chrome','111.207.194.66','2018-03-02 13:36:24'),(45,2,'Windows 10','Windows 10','Chrome','61.132.96.18','2018-03-02 13:41:31'),(46,2,'Mac OS X','Mac OS X','Safari','59.42.24.240','2018-03-02 13:41:31'),(47,2,'Mac OS X (iPhone)','iphone 10_3_3','Mobile Safari','183.93.133.144','2018-03-02 13:41:51'),(48,2,'Android 6.x','MI NOTE LTE','Chrome Mobile','117.136.0.251','2018-03-02 13:44:20'),(49,2,'Android 6.x','MI NOTE LTE','Chrome Mobile','117.136.0.251','2018-03-02 13:46:13'),(50,2,'Windows 10','Windows 10','Chrome','58.247.69.153','2018-03-02 13:47:12'),(51,2,'Windows 7','Windows 7','Internet Explorer 11','124.133.239.162','2018-03-02 13:47:32'),(52,2,'Windows 7','Windows 7','Chrome','59.38.45.211','2018-03-02 13:49:19'),(53,2,'Windows 10','Windows 10','Chrome','39.77.120.140','2018-03-02 13:50:12'),(54,2,'Windows 7','Windows 7','Chrome','123.182.247.98','2018-03-02 13:50:40'),(55,2,'Windows 7','Windows 7','Chrome','59.38.45.211','2018-03-02 13:54:42'),(56,2,'Mac OS X','Mac OS X','Chrome','117.100.228.78','2018-03-02 13:55:15'),(57,2,'Mac OS X','Mac OS X','Chrome','43.246.231.103','2018-03-02 13:57:30'),(58,2,'Windows 7','Windows 7','Chrome','218.1.108.146','2018-03-02 14:02:05'),(59,2,'Windows 7','Windows 7','Internet Explorer 11','118.26.16.188','2018-03-02 14:02:27'),(60,2,'Windows 7','Windows 7','Chrome','113.235.114.32','2018-03-02 14:03:50'),(61,2,'Mac OS X','Mac OS X','Chrome','36.110.168.140','2018-03-02 14:04:57'),(62,2,'Windows 10','Windows 10','Chrome','220.191.253.104','2018-03-02 16:49:38'),(63,2,'Windows 10','Windows 10','Firefox','183.95.51.245','2018-03-02 16:50:41'),(64,2,'Windows 10','Windows 10','Internet Explorer 11','222.86.67.116','2018-03-02 16:53:51'),(65,2,'Windows 10','Windows 10','Chrome','115.195.48.50','2018-03-02 16:58:26'),(66,2,'Windows 8.1','Windows 8.1','Internet Explorer 11','112.36.88.161','2018-03-02 16:59:12'),(67,2,'Windows 10','Windows 10','Chrome','58.56.96.28','2018-03-02 16:59:27'),(68,2,'Windows 10','Windows 10','Chrome','117.23.80.106','2018-03-02 17:01:11'),(69,2,'Windows 7','Windows 7','Chrome','106.38.48.226','2018-03-02 17:02:20'),(70,2,'Windows 10','Windows 10','Chrome','180.162.111.36','2018-03-02 17:04:53'),(71,2,'Mac OS X','Mac OS X','Chrome','58.247.0.10','2018-03-02 17:09:33'),(72,2,'Windows 7','Windows 7','Chrome','106.37.209.14','2018-03-02 17:14:00'),(73,2,'Windows 10','Windows 10','Chrome','180.173.41.181','2018-03-02 17:16:50'),(74,2,'Windows 10','Windows 10','Chrome','183.185.20.235','2018-03-02 17:17:08'),(75,2,'Windows 10','Windows 10','Chrome','182.149.162.18','2018-03-02 17:26:02'),(76,2,'Windows 7','Windows 7','Chrome','222.44.81.23','2018-03-02 17:31:26'),(77,2,'Windows 10','Windows 10','Internet Explorer 11','117.73.76.122','2018-03-02 17:43:22'),(78,2,'Windows 10','Windows 10','Chrome','104.160.175.155','2018-03-02 17:45:02'),(79,2,'Windows 10','Windows 10','Chrome','220.165.247.66','2018-03-02 17:47:05'),(80,2,'Windows 10','Windows 10','Chrome 49','121.8.145.23','2018-03-02 17:48:32'),(81,2,'Windows 10','Windows 10','Internet Explorer 11','115.175.201.237','2018-03-02 17:54:23'),(82,2,'Windows 10','Windows 10','Internet Explorer 11','182.103.24.243','2018-03-02 17:58:40'),(83,2,'Windows 10','Windows 10','Chrome','140.243.165.90','2018-03-02 18:13:04'),(84,2,'Windows 10','Windows 10','Chrome','36.152.17.174','2018-03-02 18:15:56'),(85,2,'Windows 10','Windows 10','Chrome','183.129.201.153','2018-03-02 18:18:25'),(86,2,'Windows XP','Windows XP','Chrome 40','117.136.89.99','2018-03-02 18:19:52'),(87,2,'Windows 10','Windows 10','Chrome','124.65.129.74','2018-03-02 18:23:46'),(88,2,'Windows 10','Windows 10','Chrome','125.71.215.213','2018-03-02 18:39:37'),(89,2,'Windows 7','Windows 7','Chrome','113.214.204.140','2018-03-02 19:08:05'),(90,2,'Mac OS X','Mac OS X','Chrome','111.202.41.226','2018-03-02 19:09:29'),(91,2,'Windows 7','Windows 7','Chrome','113.214.204.140','2018-03-02 19:13:34'),(92,2,'Windows 7','Windows 7','Chrome','116.24.155.162','2018-03-02 19:43:31'),(93,2,'Windows 10','Windows 10','Firefox','221.213.71.68','2018-03-02 19:52:54'),(94,2,'Windows 10','Windows 10','Chrome','36.5.67.135','2018-03-02 20:07:36'),(95,2,'Windows 7','Windows 7','Chrome','183.95.48.56','2018-03-02 20:13:49'),(96,2,'Windows 10','Windows 10','Chrome','27.156.40.74','2018-03-02 20:49:37'),(97,2,'Windows 10','Windows 10','Chrome','111.199.187.65','2018-03-02 21:02:27'),(98,2,'Windows 10','Windows 10','Internet Explorer 11','175.154.179.75','2018-03-02 21:02:39'),(99,2,'Windows 10','Windows 10','Internet Explorer 11','121.227.251.218','2018-03-02 21:06:48'),(100,2,'Windows 10','Windows 10','Chrome','220.191.33.187','2018-03-02 21:17:08'),(101,2,'Windows 10','Windows 10','Firefox','14.197.26.55','2018-03-02 21:19:26'),(102,2,'Windows 10','Windows 10','Chrome','121.227.251.218','2018-03-02 21:21:20'),(103,2,'Windows 10','Windows 10','Chrome','112.247.254.204','2018-03-02 21:48:59'),(104,2,'Android Mobile','MI 6','Chrome Mobile','222.140.139.139','2018-03-02 21:53:53'),(105,2,'Windows 10','Windows 10','Chrome','223.72.71.135','2018-03-02 22:12:55'),(106,2,'Windows 10','Windows 10','Firefox','116.249.196.142','2018-03-02 22:39:11'),(107,2,'Windows 10','Windows 10','Chrome','113.66.254.139','2018-03-02 22:39:44'),(108,2,'Windows 10','Windows 10','Firefox','116.249.196.142','2018-03-02 22:42:39'),(109,2,'Windows 10','Windows 10','Chrome','123.233.78.118','2018-03-02 22:43:04'),(110,2,'Windows 10','Windows 10','Chrome','113.44.68.69','2018-03-02 22:45:19'),(111,2,'Windows 10','Windows 10','Chrome','111.196.80.95','2018-03-02 22:46:25'),(112,2,'Windows 10','Windows 10','Firefox','223.72.88.248','2018-03-02 23:10:48'),(113,2,'Windows 10','Windows 10','Chrome','114.216.37.229','2018-03-02 23:13:03'),(114,2,'Windows 10','Windows 10','Firefox','223.72.88.248','2018-03-02 23:13:11'),(115,2,'Windows 7','Windows 7','Chrome','117.136.88.75','2018-03-02 23:30:58'),(116,2,'Windows 10','Windows 10','Chrome','120.229.57.231','2018-03-02 23:35:00'),(117,2,'Windows 10','Windows 10','Chrome','222.213.56.145','2018-03-02 23:57:17'),(118,2,'Mac OS X','Mac OS X','Chrome','183.14.25.196','2018-03-03 00:07:11'),(119,2,'Windows 10','Windows 10','Chrome 49','110.52.211.135','2018-03-03 00:18:52'),(120,2,'Windows 10','Windows 10','Chrome','139.205.211.155','2018-03-03 00:27:19'),(121,2,'Windows 10','Windows 10','Chrome','211.97.11.245','2018-03-03 00:34:09'),(122,2,'Windows 10','Windows 10','Chrome','182.139.181.71','2018-03-03 00:50:13'),(123,2,'Android Mobile','PRA-AL00','Chrome Mobile','123.173.44.79','2018-03-03 02:24:50'),(124,2,'Android Mobile','OPPO A79','Chrome Mobile','222.64.85.189','2018-03-03 02:47:30'),(125,2,'Linux','Linux','Chrome','222.94.72.198','2018-03-03 06:54:43'),(126,2,'Windows 10','Windows 10','Chrome','180.142.18.212','2018-03-03 08:20:30'),(127,2,'Windows 7','Windows 7','Internet Explorer 10','60.12.27.18','2018-03-03 08:21:20'),(128,2,'Windows 7','Windows 7','Chrome','119.128.172.13','2018-03-03 08:21:32'),(129,2,'Windows 10','Windows 10','Chrome','125.82.22.43','2018-03-03 08:52:47'),(130,2,'Windows 7','Windows 7','Internet Explorer 7','114.91.117.22','2018-03-03 08:55:08'),(131,2,'Windows 10','Windows 10','Chrome','117.84.13.140','2018-03-03 09:03:22'),(132,2,'Windows 10','Windows 10','Chrome','116.236.134.86','2018-03-03 09:09:55'),(133,2,'Windows 7','Windows 7','Chrome','123.151.173.71','2018-03-03 09:10:19'),(134,2,'Mac OS X','Mac OS X','Chrome','222.173.59.6','2018-03-03 09:12:50'),(135,2,'Windows 10','Windows 10','Chrome','42.198.174.26','2018-03-03 09:18:04'),(136,2,'Windows 10','Windows 10','Chrome','101.81.28.197','2018-03-03 09:26:28'),(137,2,'Windows 7','Windows 7','Internet Explorer 10','124.131.13.105','2018-03-03 09:32:53'),(138,2,'Windows 10','Windows 10','Chrome','222.244.93.121','2018-03-03 09:35:49'),(139,2,'Windows 10','Windows 10','Chrome','171.43.199.159','2018-03-03 09:53:06'),(140,2,'Windows 10','Windows 10','Chrome','115.45.116.197','2018-03-03 10:13:02'),(141,2,'Mac OS X','Mac OS X','Safari','202.106.49.22','2018-03-03 10:26:57'),(142,2,'Mac OS X','Mac OS X','Safari','202.106.49.22','2018-03-03 10:31:21'),(143,2,'Windows 7','Windows 7','Chrome','117.141.118.45','2018-03-03 10:32:50'),(144,2,'Windows 10','Windows 10','Internet Explorer 11','183.225.9.101','2018-03-03 10:37:09'),(145,2,'Mac OS X (iPhone)','iphone 11_2_6','Mobile Safari','153.227.44.233','2018-03-03 10:43:34'),(146,2,'Windows 10','Windows 10','Chrome','183.225.9.101','2018-03-03 10:45:01'),(147,2,'Windows 10','Windows 10','Internet Explorer 11','223.73.120.19','2018-03-03 10:52:42'),(148,2,'Windows 7','Windows 7','Chrome','223.74.101.164','2018-03-03 10:55:03'),(149,2,'Windows 7','Windows 7','Chrome','125.69.73.242','2018-03-03 10:55:09'),(150,2,'Windows 8.1','Windows 8.1','Chrome','118.212.204.6','2018-03-03 10:56:18'),(151,2,'Windows 10','Windows 10','Internet Explorer 11','36.42.95.16','2018-03-03 10:56:21'),(152,2,'Windows 10','Windows 10','Chrome','223.73.120.19','2018-03-03 10:57:30'),(153,2,'Windows 10','Windows 10','Chrome','45.124.44.70','2018-03-03 11:04:11'),(154,2,'Mac OS X','Mac OS X','Chrome','222.64.105.87','2018-03-03 11:05:20'),(155,2,'Mac OS X','Mac OS X','Chrome','222.64.105.87','2018-03-03 11:08:43'),(156,2,'Mac OS X','Mac OS X','Chrome','101.93.84.127','2018-03-03 11:16:57'),(157,2,'Windows 10','Windows 10','Internet Explorer 11','222.90.74.70','2018-03-03 11:24:18'),(158,2,'Windows 10','Windows 10','Internet Explorer 11','42.231.171.170','2018-03-03 11:32:23'),(159,2,'Windows 10','Windows 10','Firefox','113.110.254.111','2018-03-03 11:45:12'),(160,2,'Windows 10','Windows 10','Chrome','111.121.195.109','2018-03-03 11:45:47'),(161,2,'Windows 10','Windows 10','Internet Explorer 11','183.234.51.122','2018-03-03 11:47:36'),(162,2,'Mac OS X','Mac OS X','Safari','140.240.30.111','2018-03-03 11:48:31'),(163,2,'Windows 10','Windows 10','Internet Explorer 11','183.234.51.122','2018-03-03 11:50:34'),(164,2,'Windows 10','Windows 10','Internet Explorer 11','114.246.70.116','2018-03-03 12:17:26'),(165,2,'Windows 10','Windows 10','Chrome','61.158.149.157','2018-03-03 12:17:43'),(166,2,'Windows 10','Windows 10','Chrome','114.246.70.116','2018-03-03 12:18:12'),(167,2,'Windows 10','Windows 10','Chrome','116.226.251.233','2018-03-03 12:26:01'),(168,2,'Windows 10','Windows 10','Chrome','106.120.15.178','2018-03-03 12:38:21'),(169,2,'Windows 10','Windows 10','Internet Explorer 11','112.101.98.190','2018-03-03 13:01:20'),(170,2,'Windows XP','Windows XP','Chrome','49.95.13.223','2018-03-03 13:03:28'),(171,2,'Android Mobile','HUAWEI NXT-AL10','Chrome Mobile','116.52.27.10','2018-03-03 13:11:36'),(172,2,'Windows 10','Windows 10','Internet Explorer 11','112.250.203.41','2018-03-03 13:12:41'),(173,2,'Windows 10','Windows 10','Chrome','117.90.186.212','2018-03-03 13:19:45'),(174,2,'Windows 7','Windows 7','Chrome','222.212.196.58','2018-03-03 13:22:41'),(175,2,'Windows 7','Windows 7','Chrome','123.232.38.75','2018-03-03 13:28:09'),(176,2,'Windows 10','Windows 10','Chrome','60.173.235.130','2018-03-03 13:29:53'),(177,2,'Windows 7','Windows 7','Chrome','222.212.196.58','2018-03-03 13:37:18'),(178,2,'Windows 7','Windows 7','Internet Explorer 11','220.166.212.149','2018-03-03 13:39:05'),(179,2,'Windows 7','Windows 7','Chrome','220.166.212.149','2018-03-03 13:40:18'),(180,2,'Windows 10','Windows 10','Chrome','222.76.204.10','2018-03-03 13:44:36'),(181,2,'Mac OS X','Mac OS X','Chrome','113.109.79.28','2018-03-03 14:09:05'),(182,2,'Windows 7','Windows 7','Internet Explorer 10','124.131.13.105','2018-03-03 14:11:46'),(183,2,'Windows 10','Windows 10','Chrome','116.18.229.164','2018-03-03 14:19:24'),(184,2,'Windows 10','Windows 10','Chrome','115.60.132.5','2018-03-03 14:26:35'),(185,2,'Windows 10','Windows 10','Internet Explorer 11','117.172.9.240','2018-03-03 14:33:33'),(186,2,'Windows 10','Windows 10','Chrome','117.136.39.82','2018-03-03 14:49:33'),(187,2,'Windows 10','Windows 10','Internet Explorer 11','223.88.25.36','2018-03-03 14:54:15'),(188,2,'Windows 7','Windows 7','Chrome','113.116.117.129','2018-03-03 15:11:37'),(189,2,'Windows 10','Windows 10','Firefox','115.236.172.148','2018-03-03 15:15:20'),(190,2,'Windows 8.1','Windows 8.1','Chrome','113.246.86.60','2018-03-03 15:38:51'),(191,2,'Windows 10','Windows 10','Internet Explorer 11','125.94.209.221','2018-03-03 15:43:45'),(192,2,'Windows 10','Windows 10','Chrome','183.16.207.53','2018-03-03 15:45:23'),(193,2,'Windows 7','Windows 7','Chrome','171.8.5.165','2018-03-03 15:45:52'),(194,2,'Windows 10','Windows 10','Chrome','59.173.231.102','2018-03-03 15:47:48'),(195,2,'Windows 7','Windows 7','Chrome','182.84.160.93','2018-03-03 15:55:03'),(196,2,'Mac OS X (iPhone)','iphone 11_1','Mobile Safari','139.207.86.184','2018-03-03 15:55:38'),(197,2,'Windows 7','Windows 7','Chrome 49','39.181.152.196','2018-03-03 16:16:43'),(198,2,'Windows 7','Windows 7','Chrome','120.85.60.142','2018-03-03 16:23:37'),(199,2,'Windows 10','Windows 10','Chrome 49','113.91.140.152','2018-03-03 16:36:07'),(200,2,'Windows 10','Windows 10','Internet Explorer 11','116.16.159.132','2018-03-03 16:46:13'),(201,2,'Windows 10','Windows 10','Chrome','116.16.159.132','2018-03-03 16:47:22'),(202,2,'Windows 7','Windows 7','Chrome','114.102.134.76','2018-03-03 16:57:29'),(203,2,'Windows 10','Windows 10','Chrome','123.233.244.218','2018-03-03 17:04:11'),(204,2,'Windows 7','Windows 7','Firefox','222.181.8.69','2018-03-03 17:11:24'),(205,2,'Mac OS X','Mac OS X','Firefox','182.148.56.26','2018-03-03 17:28:25'),(206,2,'Windows 10','Windows 10','Microsoft Edge','61.145.190.148','2018-03-03 17:29:32'),(207,2,'Windows 7','Windows 7','Firefox','121.35.2.151','2018-03-03 17:29:39'),(208,2,'Mac OS X','Mac OS X','Chrome','221.225.16.239','2018-03-03 17:34:18'),(209,2,'Windows 10','Windows 10','Firefox','113.57.176.190','2018-03-03 17:50:59'),(210,2,'Windows 10','Windows 10','Firefox','113.57.176.179','2018-03-03 18:01:48'),(211,2,'Windows 10','Windows 10','Chrome','42.199.57.251','2018-03-03 18:08:59'),(212,2,'Mac OS X','Mac OS X','Vivaldi','119.123.73.168','2018-03-03 18:23:09'),(213,2,'Windows 7','Windows 7','Chrome','140.206.148.78','2018-03-03 18:51:07'),(214,2,'Windows 10','Windows 10','Internet Explorer 7','223.104.25.2','2018-03-03 19:15:25'),(215,2,'Windows 10','Windows 10','Internet Explorer 11','111.201.224.118','2018-03-03 19:23:14'),(216,2,'Windows 10','Windows 10','Internet Explorer 11','115.210.26.236','2018-03-03 20:06:01'),(217,2,'Windows 10','Windows 10','Chrome','111.193.222.66','2018-03-03 20:09:10'),(218,2,'Windows 7','Windows 7','Chrome','222.209.47.106','2018-03-03 20:43:46'),(219,2,'Windows 10','Windows 10','Chrome','183.21.190.12','2018-03-03 20:47:27'),(220,2,'Windows 10','Windows 10','Chrome','115.210.26.236','2018-03-03 20:58:15'),(221,2,'Windows 7','Windows 7','Chrome','119.98.191.167','2018-03-03 21:00:21'),(222,2,'Android Mobile','wv','Chrome Mobile','124.42.162.78','2018-03-03 21:30:08'),(223,2,'Windows 10','Windows 10','Firefox','116.22.35.219','2018-03-03 21:49:35'),(224,2,'Windows 7','Windows 7','Chrome','115.35.222.143','2018-03-03 22:05:59'),(225,2,'Windows 7','Windows 7','Chrome','61.140.233.245','2018-03-03 22:18:45'),(226,2,'Windows 7','Windows 7','Chrome','60.176.197.8','2018-03-03 22:44:58'),(227,2,'Windows 7','Windows 7','Internet Explorer 11','39.149.10.86','2018-03-03 22:46:07'),(228,2,'Windows 10','Windows 10','Chrome','106.37.83.124','2018-03-03 22:47:06'),(229,2,'Windows 7','Windows 7','Internet Explorer 11','106.115.230.28','2018-03-03 22:48:02'),(230,2,'Windows 7','Windows 7','Chrome','183.160.32.205','2018-03-03 23:12:08'),(231,2,'Windows 10','Windows 10','Chrome 49','117.91.231.61','2018-03-03 23:12:12'),(232,2,'Windows 7','Windows 7','Chrome','113.116.118.88','2018-03-03 23:24:37'),(233,2,'Mac OS X','Mac OS X','Chrome','211.97.11.69','2018-03-04 00:06:07'),(234,2,'Windows 10','Windows 10','Chrome','117.60.176.35','2018-03-04 00:12:10'),(235,2,'Windows 10','Windows 10','Chrome','211.161.245.243','2018-03-04 00:36:37'),(236,2,'Mac OS X','Mac OS X','Chrome','103.95.95.7','2018-03-04 00:37:18'),(237,2,'Windows 10','Windows 10','Chrome','218.88.125.243','2018-03-04 00:52:57'),(238,2,'Windows 7','Windows 7','Firefox','222.92.62.12','2018-03-04 08:38:50'),(239,2,'Mac OS X','Mac OS X','Chrome','182.46.0.65','2018-03-04 08:49:08'),(240,2,'Windows 7','Windows 7','Chrome','116.224.196.190','2018-03-04 09:33:45'),(241,2,'Windows 7','Windows 7','Internet Explorer 9','221.213.54.41','2018-03-04 10:06:17'),(242,2,'Windows 10','Windows 10','Internet Explorer 11','117.81.40.32','2018-03-04 10:17:13'),(243,2,'Windows 10','Windows 10','Chrome','27.156.42.83','2018-03-04 10:19:54'),(244,2,'Windows 10','Windows 10','Chrome 49','61.28.109.19','2018-03-04 10:30:27'),(245,2,'Windows 10','Windows 10','Chrome 49','61.28.109.19','2018-03-04 10:31:48'),(246,2,'Windows 7','Windows 7','Chrome','119.131.106.110','2018-03-04 10:53:10'),(247,2,'Mac OS X','Mac OS X','Chrome','183.95.49.229','2018-03-04 10:59:48'),(248,2,'Windows 10','Windows 10','Chrome 49','61.28.109.19','2018-03-04 11:04:22'),(249,2,'Windows 7','Windows 7','Chrome','210.21.98.71','2018-03-04 11:08:32'),(250,2,'Windows 10','Windows 10','Chrome','111.147.212.54','2018-03-04 11:16:34'),(251,2,'Windows 10','Windows 10','Internet Explorer 11','139.204.112.146','2018-03-04 13:07:53'),(252,2,'Windows 7','Windows 7','Chrome','58.100.104.225','2018-03-04 13:14:02'),(253,2,'Windows 10','Windows 10','Chrome','112.230.99.16','2018-03-04 13:15:41'),(254,2,'Windows 7','Windows 7','Chrome','171.221.137.146','2018-03-04 13:58:03'),(255,2,'Windows 7','Windows 7','Chrome','117.73.67.212','2018-03-04 14:11:18'),(256,2,'Windows 10','Windows 10','Internet Explorer 11','59.46.169.162','2018-03-04 14:46:08'),(257,2,'Windows 10','Windows 10','Chrome','218.197.142.37','2018-03-04 15:06:43'),(258,2,'Windows 10','Windows 10','Firefox','171.221.136.129','2018-03-04 15:21:15'),(259,2,'Mac OS X','Mac OS X','Chrome','27.25.235.26','2018-03-04 15:33:14'),(260,2,'Windows 10','Windows 10','Chrome','120.86.104.116','2018-03-04 15:45:23'),(261,2,'Mac OS X','Mac OS X','Chrome','27.25.235.26','2018-03-04 15:47:32'),(262,2,'Mac OS X','Mac OS X','Chrome','27.25.235.26','2018-03-04 15:57:38'),(263,2,'Mac OS X','Mac OS X','Chrome','27.25.235.26','2018-03-04 16:03:43'),(264,2,'Windows 10','Windows 10','Microsoft Edge','120.239.96.51','2018-03-04 16:07:41'),(265,2,'Windows 10','Windows 10','Internet Explorer 11','36.22.237.52','2018-03-04 16:12:57'),(266,2,'Windows 7','Windows 7','Chrome','113.118.200.204','2018-03-04 16:23:41'),(267,2,'Windows 10','Windows 10','Chrome','61.190.213.226','2018-03-04 16:36:44'),(268,2,'Windows 10','Windows 10','Chrome','101.106.102.73','2018-03-04 17:09:01'),(269,2,'Windows 10','Windows 10','Chrome','113.205.97.115','2018-03-04 17:46:59'),(270,2,'Windows 7','Windows 7','Internet Explorer 11','117.73.147.193','2018-03-04 18:17:08'),(271,2,'Linux','Linux','Firefox','115.200.206.206','2018-03-04 20:26:22'),(272,2,'Windows 7','Windows 7','Chrome','36.149.2.183','2018-03-04 20:50:38'),(273,2,'Windows 7','Windows 7','Internet Explorer 10','59.58.139.98','2018-03-04 21:09:57'),(274,2,'Windows 7','Windows 7','Chrome','171.117.255.218','2018-03-04 21:13:17'),(275,2,'Windows 7','Windows 7','Internet Explorer 11','49.80.89.132','2018-03-04 21:15:47'),(276,2,'Windows 10','Windows 10','Internet Explorer 11','113.139.101.222','2018-03-04 21:23:59'),(277,2,'Windows 10','Windows 10','Chrome','180.175.158.201','2018-03-04 21:29:42'),(278,2,'Windows 10','Windows 10','Chrome','124.236.189.116','2018-03-04 21:29:51'),(279,2,'Windows 10','Windows 10','Internet Explorer 11','111.201.228.185','2018-03-04 21:46:12'),(280,2,'Windows 10','Windows 10','Chrome','101.83.187.180','2018-03-04 22:02:06'),(281,2,'Windows 10','Windows 10','Chrome','39.64.229.180','2018-03-04 22:14:07'),(282,2,'Windows 10','Windows 10','Firefox','183.159.126.46','2018-03-04 22:15:25'),(283,2,'Android Mobile','BND-AL10','Chrome Mobile','36.149.236.26','2018-03-04 22:53:09'),(284,2,'Windows 10','Windows 10','Internet Explorer 11','106.114.176.180','2018-03-04 23:26:30'),(285,2,'Windows 10','Windows 10','Internet Explorer 11','58.33.109.232','2018-03-04 23:55:14'),(286,2,'Mac OS X','Mac OS X','Safari','118.116.19.176','2018-03-05 00:16:28'),(287,2,'Windows 10','Windows 10','Firefox','115.196.191.239','2018-03-05 00:53:35'),(288,2,'Mac OS X','Mac OS X','Chrome','42.90.130.234','2018-03-05 00:55:35'),(289,2,'Mac OS X (iPad)','iPad','Mobile Safari','124.117.147.70','2018-03-05 01:40:13'),(290,2,'Windows 10','Windows 10','Chrome','115.221.41.79','2018-03-05 01:46:23'),(291,2,'Windows 7','Windows 7','Chrome','121.71.76.168','2018-03-05 02:37:38'),(292,2,'Windows 10','Windows 10','Chrome','113.77.144.162','2018-03-05 08:27:51'),(293,2,'Windows 10','Windows 10','Chrome','114.250.20.191','2018-03-05 08:40:44'),(294,2,'Windows 10','Windows 10','Chrome 39','58.210.52.237','2018-03-05 08:48:34'),(295,2,'Windows 7','Windows 7','Chrome','117.39.63.134','2018-03-05 08:51:57'),(296,2,'Windows 10','Windows 10','Chrome','123.149.1.136','2018-03-05 08:52:35'),(297,2,'Windows 10','Windows 10','Firefox','120.1.6.152','2018-03-05 08:57:04'),(298,2,'Mac OS X','Mac OS X','Chrome','42.91.6.216','2018-03-05 08:59:00'),(299,2,'Windows 10','Windows 10','Chrome','123.8.8.103','2018-03-05 08:59:24'),(300,2,'Windows 10','Windows 10','Chrome','27.223.92.86','2018-03-05 09:04:37'),(301,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-11 16:16:20'),(302,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-11 16:18:25'),(303,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-12 11:45:05'),(304,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-12 13:45:04'),(305,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-12 14:43:39'),(306,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 14:44:44'),(307,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 14:55:43'),(308,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 15:06:55'),(309,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 15:09:22'),(310,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 17:07:14'),(311,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 17:09:02'),(312,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 17:16:31'),(313,1,'Windows 7','Windows 7','Chrome','192.168.1.125','2019-02-12 17:20:25'),(314,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-13 11:49:02'),(315,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-13 11:57:18'),(316,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-13 13:02:15'),(317,1,'Windows 7','Windows 7','Chrome 63','192.168.1.125','2019-02-13 15:36:05');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(20) NOT NULL COMMENT '角色名称',
  `comments` varchar(100) DEFAULT NULL COMMENT '备注',
  `is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_id`,`role_name`,`comments`,`is_delete`,`create_time`,`update_time`) values (1,'管理员','系统管理员',0,'2018-07-21 09:58:31','2019-02-12 09:28:59'),(2,'普通用户','系统普通用户',0,'2018-07-21 09:58:35','2019-02-12 09:29:07'),(3,'游客','系统游客',0,'2018-07-21 09:58:39','2019-02-12 09:29:20');

/*Table structure for table `sys_role_authorities` */

DROP TABLE IF EXISTS `sys_role_authorities`;

CREATE TABLE `sys_role_authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `authority_id` int(11) NOT NULL COMMENT '权限id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_role_permission_pm` (`authority_id`),
  KEY `FK_sys_role_permission_role` (`role_id`),
  CONSTRAINT `sys_role_authorities_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_authorities_ibfk_2` FOREIGN KEY (`authority_id`) REFERENCES `sys_authorities` (`authority_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色权限关联表';

/*Data for the table `sys_role_authorities` */

insert  into `sys_role_authorities`(`id`,`role_id`,`authority_id`,`create_time`) values (1,1,1,'2019-02-13 14:36:56'),(2,1,2,'2019-02-13 14:36:56'),(3,1,3,'2019-02-13 14:36:56'),(4,1,4,'2019-02-13 14:36:56'),(5,1,5,'2019-02-13 14:36:56'),(6,1,6,'2019-02-13 14:36:56'),(7,1,7,'2019-02-13 14:36:56'),(8,1,8,'2019-02-13 14:36:56'),(9,1,9,'2019-02-13 14:36:56'),(10,1,10,'2019-02-13 14:36:56'),(11,1,11,'2019-02-13 14:36:56'),(12,1,12,'2019-02-13 14:36:56'),(13,1,13,'2019-02-13 14:36:56'),(14,2,1,'2019-02-13 14:36:59'),(15,2,2,'2019-02-13 14:36:59'),(16,2,3,'2019-02-13 14:36:59'),(17,2,5,'2019-02-13 14:36:59'),(18,2,6,'2019-02-13 14:36:59'),(19,2,8,'2019-02-13 14:36:59'),(20,2,9,'2019-02-13 14:36:59'),(21,2,11,'2019-02-13 14:36:59'),(22,2,12,'2019-02-13 14:36:59'),(23,2,13,'2019-02-13 14:36:59');

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) NOT NULL COMMENT '账号',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nick_name` varchar(20) NOT NULL COMMENT '昵称',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `sex` varchar(1) NOT NULL DEFAULT '男' COMMENT '性别',
  `phone` varchar(12) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `email_verified` int(1) DEFAULT '0' COMMENT '邮箱是否验证,0未验证,1已验证',
  `true_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `department_id` int(11) DEFAULT NULL COMMENT '部门id',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1冻结',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_account` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`username`,`password`,`nick_name`,`avatar`,`sex`,`phone`,`email`,`email_verified`,`true_name`,`id_card`,`birthday`,`department_id`,`state`,`create_time`,`update_time`) values (1,'admin','822f69df5fff8f8983e69a515744a9df','管理员',NULL,'男',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2018-07-21 10:03:40','2019-02-12 11:45:15'),(2,'user01','08b96cdc7dc85dc9e3511a991b6716f4','用户一',NULL,'男',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2018-07-21 10:50:18','2019-02-11 15:16:33'),(3,'user02','08b96cdc7dc85dc9e3511a991b6716f4','用户二',NULL,'女',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2018-07-21 10:50:27','2019-02-12 11:49:58'),(4,'user03','08b96cdc7dc85dc9e3511a991b6716f4','用户三',NULL,'男',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2018-07-21 10:56:07','2019-02-12 14:45:30');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_user_role` (`user_id`),
  KEY `FK_sys_user_role_role` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_id`,`role_id`,`create_time`) values (3,3,2,'2018-07-20 11:52:45'),(9,1,1,'2019-02-12 14:59:57'),(10,4,3,'2019-02-12 15:00:00'),(11,2,2,'2019-02-12 15:00:04');


/*Table structure for table `mv_rtsp` */

DROP TABLE IF EXISTS `mv_rtsp`;

CREATE TABLE `mv_rtsp` (
  `rtsp_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '摄像头视频id',
  `videoname` varchar(100) NOT NULL COMMENT '摄像头视频名称',
  `videourl` varchar(128) NOT NULL COMMENT '摄像头视频流地址',
  `videoaddress` varchar(200) NOT NULL COMMENT '摄像头安装地址',
  `rtmpurl` varchar(128) DEFAULT NULL COMMENT '推流服务器地址',
  `cmdpath` varchar(128) DEFAULT NULL COMMENT '命令路径',
  `lat` double(16,2) DEFAULT NULL COMMENT '经度',
  `lng` double(16,2) DEFAULT NULL COMMENT '纬度',
  `rtmp_id` int(11) DEFAULT NULL COMMENT '推流服务器地址',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1不可用',
  `pid` int(11) NOT NULL DEFAULT '-1' COMMENT '推流进程pid',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`rtsp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='摄像头视频表';


DROP TABLE IF EXISTS `light_lamp`;

CREATE TABLE `light_lamp` (
  `lamp_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '路灯id',
  `lampname` varchar(30) NOT NULL COMMENT '账号',
  `avatar` varchar(128) DEFAULT NULL COMMENT '开灯图像',
  `nick_name` varchar(30) NOT NULL COMMENT '昵称',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `unavatar` varchar(128) DEFAULT NULL COMMENT '关灯图像',
  `remark` varchar(100) DEFAULT NULL COMMENT '备用',
  `lamp_before` int(1) DEFAULT '0' COMMENT '路灯前,0关闭,1开启',
  `lamp_after` int(1) DEFAULT '0' COMMENT '路灯后,0关闭,1开启',
  `lamp_left` int(1) DEFAULT '0' COMMENT '路灯左,0关闭,1开启',
  `lamp_right` int(1) DEFAULT '0' COMMENT '路灯右,0关闭,1开启',
  `lamp_warn` int(1) DEFAULT '0' COMMENT '路灯警,0关闭,1开启',
  `lat` double(16,2) DEFAULT NULL COMMENT '经度',
  `lng` double(16,2) DEFAULT NULL COMMENT '纬度',
  `code` varchar(20) DEFAULT NULL COMMENT '命令',
  `grouping` varchar(20) DEFAULT NULL COMMENT '分组',
  `channel` varchar(20) DEFAULT NULL COMMENT '通道',
  `category` varchar(20) DEFAULT NULL COMMENT '类别 ',
  `secondlevel` varchar(20) DEFAULT NULL COMMENT '二级类别 ',
  `lamp_second` int(1) DEFAULT '0' COMMENT '二级路灯,0关闭,1开启',
  `power` int(11) DEFAULT '0' COMMENT '路灯功率',
  `secondpower` int(11) DEFAULT '0' COMMENT '二级功率',
  `secondchannel` varchar(20) DEFAULT NULL COMMENT '二级通道',
  `baud_rate` int(11) DEFAULT '9600' COMMENT '波特率',
  `checkout_bit` int(11) DEFAULT '0' COMMENT '校验位',
  `data_bit` int(11) DEFAULT '8' COMMENT '数据位',
  `stop_bit` int(11) DEFAULT '1' COMMENT '停止位',
  `other` varchar(128) DEFAULT NULL COMMENT '其他',
  `memo1` varchar(128) DEFAULT NULL COMMENT '其他',
  `memo2` varchar(128) DEFAULT NULL COMMENT '其他',
  `memo3` varchar(128) DEFAULT NULL COMMENT '其他',
  `remark1` int(11) DEFAULT '0' COMMENT '备注',
  `remark2` int(11) DEFAULT '0' COMMENT '备注',
  `latitude` double(16,4) DEFAULT NULL COMMENT '实际纬度',
  `longitude` double(16,4) DEFAULT NULL COMMENT '实际经度',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1冻结',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`lamp_id`),
  UNIQUE KEY `lamp_account` (`lampname`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='路灯表';

DROP TABLE IF EXISTS `light_task`;

CREATE TABLE `light_task` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '路灯定时任务id',
  `opencron` varchar(50) NOT NULL COMMENT '开灯表达式',
  `closecron` varchar(50) NOT NULL COMMENT '关灯表达式',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `remark` varchar(128) DEFAULT NULL COMMENT '备用',
  `other` varchar(128) DEFAULT NULL COMMENT '其他',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1不可用',
  `leixing` int(11) NOT NULL DEFAULT '-1' COMMENT '路灯类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='路灯定时开关表';

DROP TABLE IF EXISTS `light_energy`;

CREATE TABLE `light_energy` (
  `energy_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '路灯能耗id',
  `lamp_id` int(11) NOT NULL COMMENT '路灯id',
  `lampname` varchar(50) NOT NULL COMMENT '路灯名称',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `remark` varchar(128) DEFAULT NULL COMMENT '备用',
  `other` varchar(128) DEFAULT NULL COMMENT '其他',
  `power` int(11) NOT NULL DEFAULT '0' COMMENT '功率',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1不可用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`energy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='路灯能耗表';

DROP TABLE IF EXISTS `light_operate`;
CREATE TABLE `light_operate` (
  `operate_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '路灯操作id',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `operatecontent` varchar(200) DEFAULT NULL COMMENT '操作内容',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `remark` varchar(128) DEFAULT NULL COMMENT '备用',
  `other` varchar(128) DEFAULT NULL COMMENT '其他',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1不可用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`operate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='路灯操作记录表';
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
