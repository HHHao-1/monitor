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

 Date: 28/12/2020 19:06:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Records of coin_kind
-- ----------------------------
BEGIN;
INSERT INTO `coin_kind` VALUES (1, 'ETH', 'ETH', '0xETH', 18, '2020-12-22 15:30:00', '2020-12-22 15:30:00');
INSERT INTO `coin_kind` VALUES (2, 'BTC', 'BTC', '0xBTC', 8, '2020-12-22 15:30:00', '2020-12-22 15:30:00');
INSERT INTO `coin_kind` VALUES (3, 'BCH', 'BCH', '0xBCH', 8, '2020-12-22 15:30:00', '2020-12-22 15:30:00');
INSERT INTO `coin_kind` VALUES (4, 'LTC', 'LTC', '0xLTC', 8, '2020-12-22 15:30:00', '2020-12-22 15:30:00');
INSERT INTO `coin_kind` VALUES (5, 'USDT', 'USDT', '0xUSDT', 0, '2020-12-22 15:30:00', '2020-12-22 15:30:00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
