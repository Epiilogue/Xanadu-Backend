/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : xanadu_dbc

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 15/06/2023 16:17:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dbc_categary
-- ----------------------------
DROP TABLE IF EXISTS `dbc_categary`;
CREATE TABLE `dbc_categary`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '大类ID',
  `parent_id` int(10) NULL DEFAULT 0 COMMENT '父节点ID',
  `level` int(8) NULL DEFAULT NULL COMMENT '节点层级',
  `category` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dbc_lack_record
-- ----------------------------
DROP TABLE IF EXISTS `dbc_lack_record`;
CREATE TABLE `dbc_lack_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缺货单ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `categary_id` int(10) NULL DEFAULT NULL COMMENT '分类ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `now_count` int(10) NULL DEFAULT NULL COMMENT '当前库存数量',
  `safe_count` int(10) NULL DEFAULT NULL COMMENT '安全库存数量',
  `need_count` int(10) NULL DEFAULT NULL COMMENT '缺货数量',
  `input_count` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '进货数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `source` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺货来源',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dbc_product
-- ----------------------------
DROP TABLE IF EXISTS `dbc_product`;
CREATE TABLE `dbc_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `first_categray` int(10) NULL DEFAULT NULL COMMENT '一级分类ID',
  `second_categray` int(10) NULL DEFAULT NULL COMMENT '二级分类ID',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `cost` double(10, 2) NULL DEFAULT NULL COMMENT '商品成本',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供销商ID',
  `refund_able` tinyint(1) NULL DEFAULT NULL COMMENT '能否退货',
  `change_able` tinyint(1) NULL DEFAULT NULL COMMENT '能否换货',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否已被删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `safe_stock` int(10) NULL DEFAULT NULL COMMENT '安全库存量',
  `max_count` int(10) NULL DEFAULT NULL COMMENT '货物最大库存量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dbc_purchase_record
-- ----------------------------
DROP TABLE IF EXISTS `dbc_purchase_record`;
CREATE TABLE `dbc_purchase_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '进货单ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供销商ID',
  `number` int(10) NULL DEFAULT NULL COMMENT '进货数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进货状态',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '软删除标记',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `total_cost` double(20, 2) NULL DEFAULT NULL COMMENT '进货单总消费',
  `lack_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺货单的ID列表字符串',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dbc_refund
-- ----------------------------
DROP TABLE IF EXISTS `dbc_refund`;
CREATE TABLE `dbc_refund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退货安排ID',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `input_num` int(10) NULL DEFAULT NULL COMMENT '进货数量',
  `now_count` int(10) NULL DEFAULT NULL COMMENT '现库存量',
  `refund_count` int(10) NULL DEFAULT NULL COMMENT '退货数量',
  `status` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货状态',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '是否删除',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '退货时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dbc_supplier
-- ----------------------------
DROP TABLE IF EXISTS `dbc_supplier`;
CREATE TABLE `dbc_supplier`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供销商ID',
  `name` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商名字',
  `address` char(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT 'NULL' COMMENT '供销商地址',
  `contact_person` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商联系人',
  `phone` char(11) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商电话号码',
  `bank_account` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商银行账户',
  `remarks` char(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT 'NULL' COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
