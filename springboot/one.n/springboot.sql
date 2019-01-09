/*
MySQL Data Transfer
Source Host: localhost
Source Database: springboot
Target Host: localhost
Target Database: springboot
Date: 2019/1/9 10:58:10
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_name` varchar(66) DEFAULT NULL,
  `name` varchar(55) DEFAULT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for shiro_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_permission`;
CREATE TABLE `shiro_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(55) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5h26fq18y6wcrsec1eh8h2yyx` (`role_id`),
  CONSTRAINT `FK5h26fq18y6wcrsec1eh8h2yyx` FOREIGN KEY (`role_id`) REFERENCES `shiro_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(55) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lw6fmfwdi0t4yj2lhitnqwg7b` (`name`),
  KEY `FKm78yn9djdg9dl87cajk0h9v5l` (`user_id`),
  CONSTRAINT `FKm78yn9djdg9dl87cajk0h9v5l` FOREIGN KEY (`user_id`) REFERENCES `shiro_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for shiro_role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role_permissions`;
CREATE TABLE `shiro_role_permissions` (
  `shiro_role_bean_id` int(11) NOT NULL,
  `permissions_id` int(11) NOT NULL,
  UNIQUE KEY `UK_2107foawty0r1gwcjiefdhxqu` (`permissions_id`),
  KEY `FKqh4er5fbkpkmhuc3dha6yb8yb` (`shiro_role_bean_id`),
  CONSTRAINT `FKanis2sfppyh2q1vl7orbe7044` FOREIGN KEY (`permissions_id`) REFERENCES `shiro_permission` (`id`),
  CONSTRAINT `FKqh4er5fbkpkmhuc3dha6yb8yb` FOREIGN KEY (`shiro_role_bean_id`) REFERENCES `shiro_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for shiro_user
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user`;
CREATE TABLE `shiro_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(55) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8v05bunr41dvns69nuy9nrowd` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for shiro_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_roles`;
CREATE TABLE `shiro_user_roles` (
  `shiro_user_bean_id` int(11) NOT NULL,
  `roles_id` int(11) NOT NULL,
  UNIQUE KEY `UK_i84rmrqndspan1i7syqlmowjb` (`roles_id`),
  KEY `FKin030gufae8xa1vsgd1rtf713` (`shiro_user_bean_id`),
  CONSTRAINT `FKin030gufae8xa1vsgd1rtf713` FOREIGN KEY (`shiro_user_bean_id`) REFERENCES `shiro_user` (`id`),
  CONSTRAINT `FKpss09xq6jjhl60rtch72m9pjw` FOREIGN KEY (`roles_id`) REFERENCES `shiro_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `shiro_user` VALUES ('1', 'admin', '123');
INSERT INTO `user` VALUES ('3', '测试ddd', '123232');
INSERT INTO `user` VALUES ('4', 'ss1', '1232ds');
