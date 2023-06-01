/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : xanadu_dpc

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 01/06/2023 23:37:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ac_invoice
-- ----------------------------
DROP TABLE IF EXISTS `ac_invoice`;
CREATE TABLE `ac_invoice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `invoice_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票号',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票状态',
  `batch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单号',
  `amount` double(20, 2) NULL DEFAULT NULL COMMENT '金额',
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用人姓名',
  `employee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '领用人姓名',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ac_invoices
-- ----------------------------
DROP TABLE IF EXISTS `ac_invoices`;
CREATE TABLE `ac_invoices`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `start` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始号码',
  `end` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束号码',
  `batch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `available` int(11) NULL DEFAULT NULL COMMENT '可用数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分站发票管理' ROW_FORMAT = DYNAMIC;

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
  `amount` double(10, 2) NULL DEFAULT NULL COMMENT '总计金额',
  `time` datetime NULL DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for dpc_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `dpc_dispatch`;
CREATE TABLE `dpc_dispatch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '调拨单号',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '入库分库ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_num` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `plan_time` datetime NULL DEFAULT NULL COMMENT '计划出库时间',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单号',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dpc_task
-- ----------------------------
DROP TABLE IF EXISTS `dpc_task`;
CREATE TABLE `dpc_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务单号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `sub_id` bigint(20) NULL DEFAULT NULL COMMENT '分站id',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分站库房id',
  `task_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务单状态',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  `task_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
