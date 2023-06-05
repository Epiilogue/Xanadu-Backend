/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : xanadu_cc

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 05/06/2023 10:44:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cc_customer
-- ----------------------------
DROP TABLE IF EXISTS `cc_customer`;
CREATE TABLE `cc_customer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `identity_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `telephone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `organization` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作单位',
  `postcode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否被删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cc_new_order
-- ----------------------------
DROP TABLE IF EXISTS `cc_new_order`;
CREATE TABLE `cc_new_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对应的order主表ID',
  `telephone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `delivery_time` date NULL DEFAULT NULL COMMENT '预计送货日期',
  `invoice_need` int(1) NULL DEFAULT 0 COMMENT '是否要发票',
  `delivery_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `receiver_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人姓名',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cc_operation
-- ----------------------------
DROP TABLE IF EXISTS `cc_operation`;
CREATE TABLE `cc_operation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作客服ID',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `operator_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `numbers` int(10) NULL DEFAULT NULL COMMENT '操作商品数量',
  `total_amount` double(20, 2) NULL DEFAULT NULL COMMENT '操作金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cc_order
-- ----------------------------
DROP TABLE IF EXISTS `cc_order`;
CREATE TABLE `cc_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `order_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单类型',
  `deadline` date NULL DEFAULT NULL COMMENT '要求完成日期',
  `numbers` int(10) NULL DEFAULT NULL COMMENT '涉及的商品数量',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `total_amout` double(20, 2) NULL DEFAULT NULL COMMENT '涉及的金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cc_product
-- ----------------------------
DROP TABLE IF EXISTS `cc_product`;
CREATE TABLE `cc_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_id` bigint(20) NOT NULL COMMENT '对应的订单ID号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `number` int(10) NULL DEFAULT NULL COMMENT '数量',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `product_categary` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品大类',
  `islack` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否缺货',
  `refund_able` tinyint(1) NULL DEFAULT NULL COMMENT '是否可以退货',
  `change_able` tinyint(1) NULL DEFAULT NULL COMMENT '是否可以换货',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cc_refund
-- ----------------------------
DROP TABLE IF EXISTS `cc_refund`;
CREATE TABLE `cc_refund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对应的order表记录ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '操作订单ID',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因',
  `operation_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型(退货，换货，退订)',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '退换货状态',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cc_stockout
-- ----------------------------
DROP TABLE IF EXISTS `cc_stockout`;
CREATE TABLE `cc_stockout`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缺货记录ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '缺货订单号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '缺货商品号',
  `need_numbers` int(10) NULL DEFAULT NULL COMMENT '缺货商品数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建客服',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺货状态',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
