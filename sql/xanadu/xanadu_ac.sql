/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_ac

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 10/07/2023 11:10:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ac_invoice
-- ----------------------------
DROP TABLE IF EXISTS `ac_invoice`;
CREATE TABLE `ac_invoice`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `start_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开始号码',
  `end_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结束号码',
  `registration` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '未登记' COMMENT '登记状态',
  `batch` int(255) NOT NULL COMMENT '批次',
  `total` int(30) NOT NULL COMMENT '本数',
  `time` datetime NOT NULL COMMENT '发票生成日期',
  `substation_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '暂无信息' COMMENT '分站id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ac_invoices
-- ----------------------------
DROP TABLE IF EXISTS `ac_invoices`;
CREATE TABLE `ac_invoices`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `totalid` bigint(20) NOT NULL COMMENT '发票批次序号',
  `number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票号码',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '未领用' COMMENT '发票领用状态',
  `batch` int(255) NOT NULL COMMENT '批次',
  `substation_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '暂无信息' COMMENT '分站id',
  `employee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '暂无信息' COMMENT '领用人',
  `dstate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '生效中' COMMENT '失效状态',
  `details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '无' COMMENT '失效原因',
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '无' COMMENT '订单号',
  `printtime` datetime NULL DEFAULT NULL COMMENT '打印时间',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '无' COMMENT '商品名称',
  `productNum` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '无' COMMENT '商品数量',
  PRIMARY KEY (`id`, `employee`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分站发票管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ac_supply
-- ----------------------------
DROP TABLE IF EXISTS `ac_supply`;
CREATE TABLE `ac_supply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `supply_num` int(11) NULL DEFAULT NULL COMMENT '供货数量',
  `return_num` int(11) NULL DEFAULT NULL COMMENT '退回数量',
  `total_num` int(11) NULL DEFAULT NULL COMMENT '结算数量',
  `total_amount` double(10, 2) NULL DEFAULT NULL COMMENT '结算金额',
  `time` datetime NULL DEFAULT NULL COMMENT '日期',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否结算',
  `settleType` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '退款' COMMENT '退款还是支出',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
