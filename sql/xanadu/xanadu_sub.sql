/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : xanadu_sub

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 01/06/2023 23:37:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sub_delivery
-- ----------------------------
DROP TABLE IF EXISTS `sub_delivery`;
CREATE TABLE `sub_delivery`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `courier_id` bigint(20) NULL DEFAULT NULL COMMENT '投递员ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `num` int(11) NULL DEFAULT NULL COMMENT '投递数量',
  `salary` double(10, 2) NULL DEFAULT NULL COMMENT '投递费',
  `time` datetime NOT NULL COMMENT '日期(天)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '投递费结算' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sub_finance
-- ----------------------------
DROP TABLE IF EXISTS `sub_finance`;
CREATE TABLE `sub_finance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `delivery_num` int(11) NULL DEFAULT NULL COMMENT '送货数量',
  `return_num` int(11) NULL DEFAULT NULL COMMENT '退回数量',
  `refund` double(10, 2) NULL DEFAULT NULL COMMENT '退款额',
  `receive` double(10, 2) NULL DEFAULT NULL COMMENT '应收额',
  `actual` double(10, 2) NULL DEFAULT NULL COMMENT '实收额',
  `pay` double(10, 2) NULL DEFAULT NULL COMMENT '应缴额',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品收款' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sub_receipt
-- ----------------------------
DROP TABLE IF EXISTS `sub_receipt`;
CREATE TABLE `sub_receipt`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回执ID',
  `sign_id` bigint(20) NULL DEFAULT NULL COMMENT '签收单ID',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员联系电话',
  `substation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分站名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务类型',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回执任务状态',
  `feedback` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户满意度',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备注',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货/退货地址',
  `num` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `receive_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款金额',
  `return_amount` double(10, 0) NULL DEFAULT NULL COMMENT '退款金额',
  `invoice_num` bigint(20) NULL DEFAULT NULL COMMENT '发票号',
  `time` datetime NULL DEFAULT NULL COMMENT '日期',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回执单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sub_sign
-- ----------------------------
DROP TABLE IF EXISTS `sub_sign`;
CREATE TABLE `sub_sign`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签收ID',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `courier_id` bigint(20) NULL DEFAULT NULL COMMENT '快递员ID',
  `station_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `finish_date` datetime NULL DEFAULT NULL COMMENT '签收日期',
  `type` int(10) NULL DEFAULT NULL COMMENT '任务类型',
  `feedback` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户反馈',
  `receive_num` int(11) NULL DEFAULT NULL COMMENT '签收数量',
  `return_num` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `print` int(11) NULL DEFAULT NULL COMMENT '打印次数',
  `amount` int(11) NULL DEFAULT NULL COMMENT '收款',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否被删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '签收单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sub_sign_product
-- ----------------------------
DROP TABLE IF EXISTS `sub_sign_product`;
CREATE TABLE `sub_sign_product`  (
  `sign_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `sign_num` int(11) NULL DEFAULT NULL COMMENT '签收数量',
  `return_num` int(11) NULL DEFAULT NULL COMMENT '退货数量'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单商品的签收情况' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sub_substation
-- ----------------------------
DROP TABLE IF EXISTS `sub_substation`;
CREATE TABLE `sub_substation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分站ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分站名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分站地址',
  `phone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '分站电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '分站' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sub_task
-- ----------------------------
DROP TABLE IF EXISTS `sub_task`;
CREATE TABLE `sub_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务单ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `courier_id` bigint(20) NULL DEFAULT NULL COMMENT '快递员ID',
  `product_numbers` int(10) NOT NULL COMMENT '商品总数',
  `total_amount` double(20, 2) NOT NULL COMMENT '商品总价',
  `finish_need` datetime NOT NULL COMMENT '要求完成日期',
  `finish_real` datetime NULL DEFAULT NULL COMMENT '实际完成日期',
  `type` int(10) NULL DEFAULT NULL COMMENT '任务类型',
  `state` int(10) NULL DEFAULT NULL COMMENT '任务状态',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否被删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务单' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
