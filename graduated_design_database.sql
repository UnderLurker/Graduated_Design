/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : localhost:3306
 Source Schema         : graduated_design_database

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 16/02/2022 17:30:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_info
-- ----------------------------
DROP TABLE IF EXISTS `chat_info`;
CREATE TABLE `chat_info`  (
  `chat_no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(0) NOT NULL,
  `contact_id` int(0) NOT NULL,
  `read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否未读 0未读 1已读',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天信息',
  `time` datetime(0) NOT NULL COMMENT '发送信息的时间',
  `belong` tinyint(1) NOT NULL COMMENT '聊天归属，0为user，1为contact',
  PRIMARY KEY (`chat_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `contact_id`(`contact_id`) USING BTREE,
  CONSTRAINT `chat_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chat_info_ibfk_2` FOREIGN KEY (`contact_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`  (
  `no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` int(0) NOT NULL COMMENT '用户id',
  `contactid` int(0) NOT NULL COMMENT '联系人id此项不唯一',
  `folder` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人分类',
  `unread` int(0) NULL DEFAULT NULL COMMENT '未读条数',
  `headportrait` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像uuid',
  `do_not_disturb` tinyint(1) NOT NULL COMMENT '是否开启免打扰',
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `userid`(`userid`) USING BTREE,
  INDEX `contactid`(`contactid`) USING BTREE,
  INDEX `folder`(`folder`) USING BTREE,
  INDEX `headportrait`(`headportrait`) USING BTREE,
  CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_ibfk_2` FOREIGN KEY (`contactid`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_ibfk_3` FOREIGN KEY (`folder`) REFERENCES `folder_table` (`folder`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_ibfk_4` FOREIGN KEY (`headportrait`) REFERENCES `file_storage` (`uuid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
DROP TABLE IF EXISTS `file_storage`;
CREATE TABLE `file_storage`  (
  `no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id` int(0) NOT NULL COMMENT '发送者的id',
  `uuid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一的id',
  `originname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件的原名',
  `datetime` datetime(0) NOT NULL COMMENT '文件存储时间',
  `type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `folder` int(0) NOT NULL COMMENT '所属文件夹',
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `Id`(`Id`) USING BTREE,
  INDEX `uuid`(`uuid`) USING BTREE,
  CONSTRAINT `file_storage_ibfk_1` FOREIGN KEY (`Id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for folder_table
-- ----------------------------
DROP TABLE IF EXISTS `folder_table`;
CREATE TABLE `folder_table`  (
  `no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL,
  `folder` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `folder`(`folder`) USING BTREE,
  CONSTRAINT `folder_table_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `Id` int(0) NOT NULL COMMENT '用户的唯一id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `gender` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `active` tinyint(1) NOT NULL DEFAULT 0 COMMENT '账户是否激活',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
