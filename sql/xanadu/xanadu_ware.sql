/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_ware

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 11/07/2023 14:46:44
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
  `input_num` int(10) NULL DEFAULT NULL COMMENT '计划入库数量',
  `input_time` datetime NULL DEFAULT NULL COMMENT '入库日期',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库单状态(未入库、已入库)',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  `product_price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `actual_num` int(20) NULL DEFAULT NULL COMMENT '实际入库数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_center_input
-- ----------------------------
INSERT INTO `ware_center_input` VALUES (1, 1, '采购入库', 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 222, '2023-07-10 16:29:31', '未入库', NULL, 405.01, 10, NULL, NULL);

-- ----------------------------
-- Table structure for ware_center_output
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_output`;
CREATE TABLE `ware_center_output`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '中心仓库出库记录ID',
  `output_id` bigint(20) NULL DEFAULT NULL COMMENT '退货出库或调拨出库的ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `output_num` int(10) NULL DEFAULT NULL COMMENT '预期出库数量',
  `output_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库类型',
  `output_time` datetime NULL DEFAULT NULL COMMENT '出库时间',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库状态(未出库，已出库)',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  `require_time` datetime NULL DEFAULT NULL COMMENT '预计出库时间',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员ID',
  `actual_num` bigint(20) NULL DEFAULT NULL COMMENT '实际出库数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_center_output
-- ----------------------------
INSERT INTO `ware_center_output` VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-14 18:11:26', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ware_center_output` VALUES (2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-14 18:11:36', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ware_center_output` VALUES (3, 1, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 405.01, 2, '调拨出库', '2023-07-10 20:38:38', '分库已入库', 1, '2023-07-13 00:00:00', 1, NULL, 7, 1, 2);
INSERT INTO `ware_center_output` VALUES (4, 2, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 800.02, 2, '调拨出库', '2023-07-10 20:38:41', '分库已入库', 1, '2023-07-13 00:00:00', 1, NULL, 7, 1, 2);

-- ----------------------------
-- Table structure for ware_center_storage_record
-- ----------------------------
DROP TABLE IF EXISTS `ware_center_storage_record`;
CREATE TABLE `ware_center_storage_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `allocate_able_num` int(20) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '可分配商品数量',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `allocated_num` int(20) UNSIGNED ZEROFILL NULL DEFAULT 00000000000000000000 COMMENT '已分配的商品数量',
  `refund_num` int(20) UNSIGNED ZEROFILL NULL DEFAULT 00000000000000000000 COMMENT '退货的商品数量',
  `total_num` int(20) UNSIGNED ZEROFILL NULL DEFAULT 00000000000000000000 COMMENT '总计商品数量',
  `lock_num` int(20) UNSIGNED ZEROFILL NULL DEFAULT 00000000000000000000 COMMENT '下单锁定的商品数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_center_storage_record
-- ----------------------------
INSERT INTO `ware_center_storage_record` VALUES (66, 1, 00000000000000000100, 'dddd', 133.03, '2023-07-09 12:22:15', '2023-07-10 13:04:44', 00000000000000000100, 00000000000000000000, 00000000000000000200, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (67, 2, 00000000000000000100, 'sss', 13.04, '2023-07-09 12:22:42', '2023-07-10 13:04:46', 00000000000000000100, 00000000000000000000, 00000000000000000200, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (68, 3, 00000000000000000100, 'eqeq', 3334.03, '2023-07-09 12:22:59', '2023-07-10 13:04:47', 00000000000000000100, 00000000000000000000, 00000000000000000200, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (69, 4, 00000000000000000100, 'aaaa', 123.04, '2023-07-09 12:23:05', '2023-07-10 13:04:48', 00000000000000000100, 00000000000000000000, 00000000000000000200, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (70, 5, 00000000000000000100, 'macbook', 2562.04, '2023-07-09 12:23:12', '2023-07-10 13:04:50', 00000000000000000100, 00000000000000000000, 00000000000000000200, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (71, 6, 00000000000000000100, '华为智选 鼎桥 TDTech M40 5G手机 8GB+256GB', 402.04, '2023-07-09 22:09:10', '2023-07-10 13:04:52', 00000000000000000100, 00000000000000000000, 00000000000000000200, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (73, 7, 00000000000000000098, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 405.01, '2023-07-09 22:09:10', '2023-07-10 12:24:21', 00000000000000000100, 00000000000000000000, 00000000000000000198, 00000000000000000000);
INSERT INTO `ware_center_storage_record` VALUES (74, 9, 00000000000000000098, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 800.02, '2023-07-10 12:12:33', '2023-07-10 12:28:01', 00000000000000000100, 00000000000000000000, 00000000000000000198, 00000000000000000000);

-- ----------------------------
-- Table structure for ware_centerware
-- ----------------------------
DROP TABLE IF EXISTS `ware_centerware`;
CREATE TABLE `ware_centerware`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT COMMENT '中心仓库id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中心仓库地址',
  `y` double(20, 4) NULL DEFAULT NULL COMMENT '中心仓库纬度',
  `x` double(20, 4) NULL DEFAULT NULL COMMENT '中心仓库经度',
  `warn_number` int(10) NULL DEFAULT NULL COMMENT '仓库预警值',
  `max_number` int(10) NULL DEFAULT NULL COMMENT '仓库最高值',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中心仓库城市地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_centerware
-- ----------------------------
INSERT INTO `ware_centerware` VALUES (1, 'xanadu中西仓库', '河北省廊坊市安次区', 39.1685, 116.8596, 100, 300, '廊坊市');

-- ----------------------------
-- Table structure for ware_sub_input
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_input`;
CREATE TABLE `ware_sub_input`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入库记录ID',
  `dispatch_id` bigint(20) NULL DEFAULT NULL COMMENT '调拨单ID，如果是退货则不需要该字段',
  `input_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库类型，购货或是退单',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `input_num` int(10) NULL DEFAULT NULL COMMENT ' 预计数量',
  `input_time` datetime NULL DEFAULT NULL COMMENT '入库日期',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  `product_price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_sub_input
-- ----------------------------
INSERT INTO `ware_sub_input` VALUES (1, 3, '调拨入库', 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 2, '2023-07-10 20:38:52', 1, 405.01, NULL, 1);
INSERT INTO `ware_sub_input` VALUES (3, 3, '调拨入库', 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 2, '2023-07-10 20:44:02', 1, 405.01, NULL, 1);
INSERT INTO `ware_sub_input` VALUES (4, 4, '调拨入库', 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 2, '2023-07-10 20:44:42', 1, 800.02, NULL, 1);

-- ----------------------------
-- Table structure for ware_sub_master
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_master`;
CREATE TABLE `ware_sub_master`  (
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  `master_id` bigint(20) NULL DEFAULT NULL COMMENT '分库管理员ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_sub_master
-- ----------------------------
INSERT INTO `ware_sub_master` VALUES (2, 11);
INSERT INTO `ware_sub_master` VALUES (1, 2);
INSERT INTO `ware_sub_master` VALUES (3, 10);

-- ----------------------------
-- Table structure for ware_sub_output
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_output`;
CREATE TABLE `ware_sub_output`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分库出库记录ID',
  `output_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `output_num` int(10) NULL DEFAULT NULL COMMENT '出库数量',
  `output_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库类型(退货或者领货)',
  `output_time` datetime NULL DEFAULT NULL COMMENT '出库时间',
  `subware_id` bigint(10) NULL DEFAULT NULL COMMENT '出库分站ID',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '软删除标记',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `actual_num` int(20) NULL DEFAULT NULL COMMENT '实际的出库数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_sub_output
-- ----------------------------
INSERT INTO `ware_sub_output` VALUES (1, 1, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 2, '领货出库', '2023-07-10 22:11:54', 1, 0, '已出库', 2);
INSERT INTO `ware_sub_output` VALUES (2, 1, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 2, '领货出库', '2023-07-10 22:11:54', 1, 0, '已出库', 2);

-- ----------------------------
-- Table structure for ware_sub_storage_record
-- ----------------------------
DROP TABLE IF EXISTS `ware_sub_storage_record`;
CREATE TABLE `ware_sub_storage_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '子站ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `allocate_able_num` int(20) NULL DEFAULT NULL COMMENT '可分配数量',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `allocated_num` int(20) NULL DEFAULT 0 COMMENT '已分配数量',
  `refund_num` int(20) NULL DEFAULT 0 COMMENT '退货产品数量',
  `total_num` int(20) NULL DEFAULT 0 COMMENT '总计产品数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_sub_storage_record
-- ----------------------------
INSERT INTO `ware_sub_storage_record` VALUES (1, 1, 7, 0, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 405.01, '2023-07-10 20:38:52', NULL, 2, 0, 2);
INSERT INTO `ware_sub_storage_record` VALUES (2, 1, 9, 0, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 800.02, '2023-07-10 20:44:43', NULL, 0, 0, 0);

-- ----------------------------
-- Table structure for ware_subware
-- ----------------------------
DROP TABLE IF EXISTS `ware_subware`;
CREATE TABLE `ware_subware`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分库房ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库房地址',
  `y` double(20, 4) NULL DEFAULT NULL COMMENT '纬度',
  `x` double(20, 4) NULL DEFAULT NULL COMMENT '经度',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分库城市地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_subware
-- ----------------------------
INSERT INTO `ware_subware` VALUES (1, '海南分库', '海南省海口市琼山区', 20.0211, 110.3553, '海口市');
INSERT INTO `ware_subware` VALUES (2, '临海仓库', '浙江省台州市临海市东方大道99号', 28.8644, 121.1515, '台州市');
INSERT INTO `ware_subware` VALUES (3, '河北仓库', '河北省石家庄市长安区范光胡同8号', 38.0465, 114.5261, '石家庄市');

SET FOREIGN_KEY_CHECKS = 1;
