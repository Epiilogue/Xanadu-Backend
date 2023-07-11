/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_dpc

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 11/07/2023 14:46:36
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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpc_dispatch
-- ----------------------------
INSERT INTO `dpc_dispatch` VALUES (1, 1, 7, 2, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', '2023-07-13 00:00:00', '未出库', '电脑', 1, 0, 7);
INSERT INTO `dpc_dispatch` VALUES (2, 1, 9, 2, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', '2023-07-13 00:00:00', '未出库', '玩具', 1, 0, 7);

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
-- Records of dpc_product
-- ----------------------------
INSERT INTO `dpc_product` VALUES (37, 20, 1, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 2, 405.01, '电脑', NULL);
INSERT INTO `dpc_product` VALUES (38, 20, 1, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 2, 800.02, '玩具', NULL);

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
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpc_task
-- ----------------------------
INSERT INTO `dpc_task` VALUES (1, 20, 7, '已完成', 0, '送货收款', NULL);

SET FOREIGN_KEY_CHECKS = 1;
