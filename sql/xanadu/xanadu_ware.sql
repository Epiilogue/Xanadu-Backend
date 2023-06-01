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

 Date: 01/06/2023 23:37:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ware_center
-- ----------------------------
DROP TABLE IF EXISTS `ware_center`;
CREATE TABLE `ware_center`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中心仓库地址',
  `x` double(20, 4) NULL DEFAULT NULL COMMENT '中心仓库经度',
  `y` double(20, 4) NULL DEFAULT NULL COMMENT '中心仓库纬度',
  `warn_number` int(10) NULL DEFAULT NULL COMMENT '仓库预警值',
  `max_number` int(10) NULL DEFAULT NULL COMMENT '仓库最高值'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_center_input
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_input`;
CREATE TABLE `ware_center_input`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入库记录ID',
  `input_id` bigint(20) NULL DEFAULT NULL COMMENT '入库单号ID',
  `input_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库类型，购货或是退单',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `input_num` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `input_time` datetime NULL DEFAULT NULL COMMENT '入库日期',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_center_product
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_product`;
CREATE TABLE `ware_center_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_num` int(10) NULL DEFAULT NULL COMMENT '库存数量',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware_sub
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub`;
CREATE TABLE `ware_sub`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分库房ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库房地址',
  `x` double(20, 4) NULL DEFAULT NULL COMMENT '经度',
  `y` double(20, 4) NULL DEFAULT NULL COMMENT '纬度',
  `master` bigint(20) NULL DEFAULT NULL COMMENT '库管员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
-- Table structure for ware_sub_product
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_product`;
CREATE TABLE `ware_sub_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '子站ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_num` int(10) NULL DEFAULT NULL COMMENT '库存数量',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
