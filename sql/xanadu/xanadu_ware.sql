/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : xanadu_ware

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 14/06/2023 11:42:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ware_center_input
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_input`;
CREATE TABLE `ware_center_input`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入库记录ID',
  `input_id` bigint(20) NULL DEFAULT NULL COMMENT '对应的追溯单号,购货单或者退货单',
  `input_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库类型，购货或是退单',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `input_num` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `input_time` datetime NULL DEFAULT NULL COMMENT '入库日期',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库单状态(未入库、已入库)',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  `product_price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `suplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_center_output
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_output`;
CREATE TABLE `ware_center_output`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '中心仓库出库记录ID',
  `output_id` bigint(20) NULL DEFAULT NULL COMMENT '退货出库或调拨出库的ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `ouput_num` int(10) NULL DEFAULT NULL COMMENT '出库数量',
  `output_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库类型',
  `output_time` datetime NULL DEFAULT NULL COMMENT '出库时间',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库状态(未出库，已出库)',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  `require_time` datetime NULL DEFAULT NULL COMMENT '预计出库时间',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_center_storage_record
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_storage_record`;
CREATE TABLE `ware_center_storage_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `allocate_able_num` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '可分配商品数量',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `allocated_num` int(11) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '已分配的商品数量',
  `refund_num` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '退货的商品数量',
  `total_num` int(11) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '总计商品数量',
  `lock_num` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '下单锁定的商品数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_centerware
-- ----------------------------
DROP TABLE IF EXISTS `ware_centerware`;
CREATE TABLE `ware_centerware`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT COMMENT '中心仓库id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中心仓库地址',
  `x` double(20, 4) NULL DEFAULT NULL COMMENT '中心仓库经度',
  `y` double(20, 4) NULL DEFAULT NULL COMMENT '中心仓库纬度',
  `warn_number` int(10) NULL DEFAULT NULL COMMENT '仓库预警值',
  `max_number` int(10) NULL DEFAULT NULL COMMENT '仓库最高值',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中心仓库城市地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_sub_input
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_input`;
CREATE TABLE `ware_sub_input`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入库记录ID',
  `input_id` bigint(20) NULL DEFAULT NULL COMMENT '入库单号ID,可能为退货或调拨',
  `input_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库类型，购货或是退单',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `input_num` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `input_time` datetime NULL DEFAULT NULL COMMENT '入库日期',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '子仓库ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_sub_output
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_output`;
CREATE TABLE `ware_sub_output`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '中心仓库出库记录ID',
  `output_id` bigint(20) NULL DEFAULT NULL COMMENT '退货出库或调拨出库的ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `ouput_num` int(10) NULL DEFAULT NULL COMMENT '出库数量',
  `output_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库类型(退货或者领货)',
  `output_time` datetime NULL DEFAULT NULL COMMENT '出库时间',
  `subware_id` bigint(10) NULL DEFAULT NULL COMMENT '出库分站ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_sub_storage_record
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_storage_record`;
CREATE TABLE `ware_sub_storage_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '子站ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `allocate_able_num` int(10) NULL DEFAULT NULL COMMENT '可分配数量',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `allocated_num` int(10) NULL DEFAULT NULL COMMENT '已分配数量',
  `refund_num` int(10) NULL DEFAULT NULL COMMENT '退货产品数量',
  `total_num` int(10) NULL DEFAULT NULL COMMENT '总计产品数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_subware
-- ----------------------------
DROP TABLE IF EXISTS `ware_subware`;
CREATE TABLE `ware_subware`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分库房ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库房地址',
  `x` double(20, 4) NULL DEFAULT NULL COMMENT '经度',
  `y` double(20, 4) NULL DEFAULT NULL COMMENT '纬度',
  `master` bigint(20) NULL DEFAULT NULL COMMENT '库管员',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分库城市地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
