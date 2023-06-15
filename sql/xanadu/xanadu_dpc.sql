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

 Date: 14/06/2023 15:39:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调度单对应状态',
  `product_categary` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品分类',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '软删除',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dpc_product
-- ----------------------------
DROP TABLE IF EXISTS `dpc_product`;
CREATE TABLE `dpc_product`  (
  `id` bigint(20) NOT NULL COMMENT '记录ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `number` int(20) NULL DEFAULT NULL COMMENT '商品数量',
  `price` double(20, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `product_categary` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品大类',
  `actual_number` int(20) NULL DEFAULT NULL COMMENT '实际接收数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dpc_task
-- ----------------------------
DROP TABLE IF EXISTS `dpc_task`;
CREATE TABLE `dpc_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务单号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `sub_id` bigint(20) NULL DEFAULT NULL COMMENT '分站id',
  `task_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务单状态',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  `task_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
