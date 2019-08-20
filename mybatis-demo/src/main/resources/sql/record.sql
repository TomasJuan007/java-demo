/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : record

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-11-12 11:07:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `record`
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `event` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `priority` int(5),
  `value` int(5),
  `interest` int(5),
  `status` int(5),
  `estimate` int(10),
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  `delete_time` date DEFAULT NULL,
  `lft` int(5),
  `rgt` int(5),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of record
-- ----------------------------