/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2019-11-04 12:01:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES ('1');

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(2048) NOT NULL,
  `create_time` varchar(255) NOT NULL,
  `last_modify_time` varchar(255) DEFAULT NULL,
  `rule_key` varchar(255) NOT NULL,
  `version` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9yepjak9olg92holwkr8p3l0f` (`rule_key`),
  UNIQUE KEY `UK_ilmbp99kyt6gy10224pc9bl6n` (`version`),
  UNIQUE KEY `UK_ei48upwykmhx9r5p7p4ndxvgn` (`last_modify_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES ('1', 'package plausibcheck.adress\r\n\r\nimport com.curefun.drools.model.Address;\r\nimport com.curefun.drools.model.fact.AddressCheckResult;\r\n\r\nrule \"Postcode 6 numbers\"\r\n\r\n    when\r\n        address : Address(postcode != null, postcode matches \"([0-9]{3})\")\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        checkResult.setPostCodeResult(true);\r\n		System.out.println(\"规则6中打印日志：校验通过!\");\r\nend', '111', '111', 'address', '1');
