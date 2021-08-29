/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : monitor

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 28/12/2020 19:05:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for addr_rule
-- ----------------------------
DROP TABLE IF EXISTS `addr_rule`;
CREATE TABLE `addr_rule` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '地址监控规则表',
  `event_name` varchar(50) DEFAULT NULL COMMENT '事件名称',
  `coin_kind` varchar(20) NOT NULL COMMENT '币种',
  `address` varchar(255) NOT NULL COMMENT '地址信息',
  `event_add_time` datetime NOT NULL COMMENT '事件添加时间',
  `event_update_time` datetime NOT NULL COMMENT '事件修改时间',
  `notice_way` tinyint(4) DEFAULT NULL COMMENT '通知方式',
  `monitor_min_val` varchar(255) DEFAULT NULL COMMENT '监控阈值',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `state` tinyint(4) NOT NULL COMMENT '地址监控规则状态',
  `address_mark` varchar(255) DEFAULT NULL COMMENT '地址标注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for coin_kind
-- ----------------------------
DROP TABLE IF EXISTS `coin_kind`;
CREATE TABLE `coin_kind` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '币种管理表',
  `main_chain` varchar(20) NOT NULL COMMENT '主链名称',
  `coin_name` varchar(50) DEFAULT NULL COMMENT '币种名称',
  `contract_addr` varchar(255) DEFAULT NULL COMMENT '合约地址',
  `point` int(11) DEFAULT NULL COMMENT '小数位',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for monitor_addr
-- ----------------------------
DROP TABLE IF EXISTS `monitor_addr`;
CREATE TABLE `monitor_addr` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '地址异动监控表',
  `trans_hash` varchar(255) NOT NULL COMMENT '交易哈希',
  `unusual_count` varchar(255) NOT NULL COMMENT '异动额度',
  `unusual_time` datetime NOT NULL COMMENT '异动时间',
  `notice_time` datetime DEFAULT NULL COMMENT '通知时间',
  `addr_rule_id` int(11) NOT NULL COMMENT '地址监控规则id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for monitor_trans
-- ----------------------------
DROP TABLE IF EXISTS `monitor_trans`;
CREATE TABLE `monitor_trans` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '交易监控表',
  `trans_hash` varchar(255) NOT NULL COMMENT '交易哈希',
  `from_address` varchar(255) NOT NULL COMMENT '交易发送地址',
  `to_address` varchar(255) NOT NULL COMMENT '交易接收地址',
  `unusual_count` varchar(255) NOT NULL COMMENT '异动额度',
  `unusual_time` datetime NOT NULL COMMENT '异动时间',
  `notice_time` datetime DEFAULT NULL COMMENT '通知时间',
  `trans_rule_id` int(11) NOT NULL COMMENT '交易监控规则id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53618 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for trans_rule
-- ----------------------------
DROP TABLE IF EXISTS `trans_rule`;
CREATE TABLE `trans_rule` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '交易监控规则表',
  `coin_kind` varchar(255) NOT NULL COMMENT '币种',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `event_add_time` datetime NOT NULL COMMENT '事件添加时间',
  `event_update_time` datetime NOT NULL COMMENT '事件修改时间',
  `notice_way` tinyint(4) DEFAULT NULL COMMENT '通知方式',
  `monitor_min_val` varchar(255) NOT NULL COMMENT '监控阈值',
  `state` tinyint(4) NOT NULL COMMENT '交易监控规则状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户表',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `phone` varchar(20) NOT NULL COMMENT '电话',
  `email` varchar(50) NOT NULL COMMENT '电子邮箱',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户信息修改时间',
  `access_prmision` tinyint(4) DEFAULT NULL COMMENT '访问权限',
  `app_id` int(11) DEFAULT NULL COMMENT '微服务appID',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `state` tinyint(4) NOT NULL COMMENT '用户状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
