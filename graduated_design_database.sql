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

 Date: 08/04/2022 18:22:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_info
-- ----------------------------
DROP TABLE IF EXISTS `chat_info`;
CREATE TABLE `chat_info`  (
  `chat_no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否未读 0未读 1已读',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天信息',
  `time` datetime(0) NOT NULL COMMENT '发送信息的时间',
  `dest` int(0) UNSIGNED NOT NULL COMMENT '目标用户',
  `origin` int(0) UNSIGNED NOT NULL COMMENT '发送用户',
  `file` tinyint(1) NOT NULL COMMENT '0聊天 1文件',
  `size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`chat_no`) USING BTREE,
  INDEX `dest`(`dest`) USING BTREE,
  INDEX `origin`(`origin`) USING BTREE,
  CONSTRAINT `chat_info_ibfk_1` FOREIGN KEY (`dest`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chat_info_ibfk_2` FOREIGN KEY (`origin`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 204 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`  (
  `contact_no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` int(0) UNSIGNED NOT NULL COMMENT '用户id',
  `contactid` int(0) UNSIGNED NOT NULL COMMENT '联系人id此项不唯一',
  `folder` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人分类',
  `headportrait` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像uuid',
  `do_not_disturb` tinyint(1) NOT NULL COMMENT '是否开启免打扰',
  `unread` int(0) NOT NULL DEFAULT 0 COMMENT '未读条数',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `black_list` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '黑名单 0正常 1user黑contact 2contact黑user 3互相黑',
  PRIMARY KEY (`contact_no`) USING BTREE,
  INDEX `folder`(`folder`) USING BTREE,
  INDEX `contact_ibfk_4`(`headportrait`) USING BTREE,
  INDEX `userid`(`userid`) USING BTREE,
  INDEX `contactid`(`contactid`) USING BTREE,
  CONSTRAINT `contact_ibfk_3` FOREIGN KEY (`folder`) REFERENCES `folder_table` (`folder`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_ibfk_5` FOREIGN KEY (`userid`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_ibfk_6` FOREIGN KEY (`contactid`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
DROP TABLE IF EXISTS `file_storage`;
CREATE TABLE `file_storage`  (
  `no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id` int(0) UNSIGNED NOT NULL COMMENT '发送者的id',
  `uuid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一的id',
  `originname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件的原名',
  `datetime` datetime(0) NOT NULL COMMENT '文件存储时间',
  `folder` int(0) NULL DEFAULT NULL COMMENT '所属文件夹',
  `receive_id` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '接收者id',
  `path` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '为了存储除了folder的文件的路径',
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `uuid`(`uuid`) USING BTREE,
  INDEX `Id`(`Id`) USING BTREE,
  INDEX `receive_id`(`receive_id`) USING BTREE,
  CONSTRAINT `file_storage_ibfk_1` FOREIGN KEY (`Id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `file_storage_ibfk_2` FOREIGN KEY (`receive_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for folder_table
-- ----------------------------
DROP TABLE IF EXISTS `folder_table`;
CREATE TABLE `folder_table`  (
  `no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(0) UNSIGNED NOT NULL,
  `folder` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `folder`(`folder`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `folder_table_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friend_request
-- ----------------------------
DROP TABLE IF EXISTS `friend_request`;
CREATE TABLE `friend_request`  (
  `no` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id` int(0) UNSIGNED NOT NULL COMMENT '发起请求的ID',
  `receive_id` int(0) UNSIGNED NOT NULL COMMENT '接收请求的ID',
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `request_id`(`request_id`) USING BTREE,
  INDEX `receive_id`(`receive_id`) USING BTREE,
  CONSTRAINT `friend_request_ibfk_1` FOREIGN KEY (`request_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `friend_request_ibfk_2` FOREIGN KEY (`receive_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for general_setting
-- ----------------------------
DROP TABLE IF EXISTS `general_setting`;
CREATE TABLE `general_setting`  (
  `id` int(0) UNSIGNED NOT NULL,
  `font_size` int(0) NOT NULL DEFAULT 16 COMMENT '字体大小',
  `send_style` tinyint(1) NOT NULL DEFAULT 1 COMMENT '发送信息方式1enter 0ctrl+enter',
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `general_setting_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `Id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户的唯一id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `gender` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `active` tinyint(1) NOT NULL DEFAULT 0 COMMENT '账户是否激活',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  `pre_login_time` datetime(0) NULL DEFAULT NULL COMMENT '上次登录时间',
  `face_image_uuid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储脸部数据的名称',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for video_thumbnail
-- ----------------------------
DROP TABLE IF EXISTS `video_thumbnail`;
CREATE TABLE `video_thumbnail`  (
  `chat_no` int(0) UNSIGNED NOT NULL COMMENT '外键',
  `uuid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频缩略图的uuid',
  `file_storage_no` int(0) UNSIGNED NOT NULL COMMENT '文件存储的主键',
  `type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`chat_no`) USING BTREE,
  INDEX `file_storage_no`(`file_storage_no`) USING BTREE,
  CONSTRAINT `video_thumbnail_ibfk_1` FOREIGN KEY (`chat_no`) REFERENCES `chat_info` (`chat_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `video_thumbnail_ibfk_2` FOREIGN KEY (`file_storage_no`) REFERENCES `file_storage` (`no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
