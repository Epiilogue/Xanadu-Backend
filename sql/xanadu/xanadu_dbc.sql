/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_dbc

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 14/07/2023 09:30:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dbc_categary
-- ----------------------------
DROP TABLE IF EXISTS `dbc_categary`;
CREATE TABLE `dbc_categary`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '大类ID',
  `parent_id` int(20) NULL DEFAULT 0 COMMENT '父节点ID',
  `level` int(8) NULL DEFAULT NULL COMMENT '节点层级（0或者1）',
  `category` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dbc_categary
-- ----------------------------
INSERT INTO `dbc_categary` VALUES (1, 0, 1, '手机');
INSERT INTO `dbc_categary` VALUES (2, 1, 2, '华为手机');
INSERT INTO `dbc_categary` VALUES (3, 1, 2, '苹果手机');
INSERT INTO `dbc_categary` VALUES (4, 0, 1, '电脑');
INSERT INTO `dbc_categary` VALUES (5, 4, 2, '苹果电脑');
INSERT INTO `dbc_categary` VALUES (6, 4, 2, '联想电脑');
INSERT INTO `dbc_categary` VALUES (7, 0, 1, '衣服');
INSERT INTO `dbc_categary` VALUES (8, 7, 2, '爱国李宁');
INSERT INTO `dbc_categary` VALUES (9, 7, 2, '耐克');
INSERT INTO `dbc_categary` VALUES (10, 0, 1, '玩具');
INSERT INTO `dbc_categary` VALUES (11, 10, 2, '毛绒玩具');
INSERT INTO `dbc_categary` VALUES (12, 10, 2, '积木玩具');
INSERT INTO `dbc_categary` VALUES (13, 10, 2, '模型玩具');
INSERT INTO `dbc_categary` VALUES (14, 1, 2, 'vivo手机');
INSERT INTO `dbc_categary` VALUES (15, 4, 2, '戴尔电脑');
INSERT INTO `dbc_categary` VALUES (16, 7, 2, '阿迪达斯');
INSERT INTO `dbc_categary` VALUES (17, 7, 2, '安踏');

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
-- Records of dbc_lack_record
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dbc_product
-- ----------------------------
INSERT INTO `dbc_product` VALUES (1, 'dddd', 1, 2, 133.03, 111.00, 6, 0, 0, 'daaa', 0, '2023-07-08 11:21:14', '2023-07-08 11:21:14', 'https://xanadu-image.oss-cn-beijing.aliyuncs.com/2023-07-08/198ad0a2-6990-4668-863a-466b8dbaa83e_系统架构.jpg', 444, 5667);
INSERT INTO `dbc_product` VALUES (2, 'sss', 1, 2, 13.04, 4.00, 6, 1, 1, 'sss', 0, '2023-07-08 14:39:06', '2023-07-08 14:39:06', 'https://xanadu-image.oss-cn-beijing.aliyuncs.com/2023-07-09/b339b8c5-598d-4a7e-876f-d11f36b19117_17.png', 40, 5667);
INSERT INTO `dbc_product` VALUES (3, 'eqeq', 1, 2, 3334.03, 2222.00, 8, 0, 0, 'dddd', 0, '2023-07-08 14:43:24', '2023-07-08 14:43:24', '', 444, 5667);
INSERT INTO `dbc_product` VALUES (4, 'aaaa', 1, 2, 123.04, 23.00, 10, 1, 0, 'dffff', 0, '2023-07-08 14:50:04', '2023-07-08 14:50:04', '', 434, 5667);
INSERT INTO `dbc_product` VALUES (5, 'macbook', 4, 5, 2562.04, 1202.00, 11, 0, 1, '无', 0, '2023-07-08 15:38:52', '2023-07-09 22:45:49', '', 333, 5667);
INSERT INTO `dbc_product` VALUES (6, '华为智选 鼎桥 TDTech M40 5G手机 8GB+256GB', 1, 2, 402.04, 2299.00, 5, 0, 0, '华为', 0, '2023-07-09 21:28:00', '2023-07-09 21:36:44', 'https://img3m1.ddimg.cn/94/14/11181060391-1_l_1.jpg', 500, 5667);
INSERT INTO `dbc_product` VALUES (7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 4, 6, 405.01, 393.00, 10, 1, 1, 'dsdd', 0, '2023-07-09 21:55:19', '2023-07-09 21:55:19', 'https://img3m7.ddimg.cn/41/32/11129923967-1_l_1.jpg', 222, 5667);
INSERT INTO `dbc_product` VALUES (9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 10, 12, 800.02, 169.00, 6, 1, 1, '乐高记录', 0, '2023-07-10 10:08:21', '2023-07-10 10:08:21', 'https://img3m4.ddimg.cn/62/4/11401678394-1_l_3.jpg', 200, 5667);

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dbc_purchase_record
-- ----------------------------
INSERT INTO `dbc_purchase_record` VALUES (1, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 10, 222, '2023-07-10 13:07:02', '已到货', '2023-07-10 13:07:02', 0, 405.01, 89912.22, '-1,');
INSERT INTO `dbc_purchase_record` VALUES (2, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 10, 28, '2023-07-12 23:06:34', '已到货', '2023-07-12 23:06:34', 0, 405.01, 11340.28, '-1,');
INSERT INTO `dbc_purchase_record` VALUES (3, 1, 'dddd', 6, 5, '2023-07-13 09:47:02', '已到货', '2023-07-13 09:47:02', 0, 133.03, 665.15, '5,7,');
INSERT INTO `dbc_purchase_record` VALUES (4, 2, 'sss', 6, 5, '2023-07-13 09:51:47', '已到货', '2023-07-13 09:51:47', 0, 13.04, 65.20, '6,8,');
INSERT INTO `dbc_purchase_record` VALUES (5, 1, 'dddd', 6, 267, '2023-07-13 09:56:55', '已到货', '2023-07-13 09:56:55', 0, 133.03, 35519.01, '-1,');
INSERT INTO `dbc_purchase_record` VALUES (6, 5, 'macbook', 11, 2, '2023-07-13 09:57:10', '已到货', '2023-07-13 09:57:10', 0, 2562.04, 5124.08, '1,');
INSERT INTO `dbc_purchase_record` VALUES (7, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 10, 3, '2023-07-13 10:16:07', '已到货', '2023-07-13 10:16:07', 0, 405.01, 1215.03, '11,');
INSERT INTO `dbc_purchase_record` VALUES (8, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 6, 3, '2023-07-13 10:16:09', '已到货', '2023-07-13 10:16:09', 0, 800.02, 2400.06, '12,');

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dbc_refund
-- ----------------------------
INSERT INTO `dbc_refund` VALUES (1, 10, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 253, 444, 1, '已提交', 0, 405.01, '2023-07-13 21:25:58');

-- ----------------------------
-- Table structure for dbc_supplier
-- ----------------------------
DROP TABLE IF EXISTS `dbc_supplier`;
CREATE TABLE `dbc_supplier`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供销商ID',
  `name` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商名字',
  `address` char(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT 'NULL' COMMENT '供销商地址',
  `contact_person` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商联系人',
  `phone` char(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商电话号码',
  `bank_account` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '供销商银行账户',
  `remarks` char(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT 'NULL' COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dbc_supplier
-- ----------------------------
INSERT INTO `dbc_supplier` VALUES (4, 'Grace Rogers', '375 Huanqu South Street 2nd Alley', 'etevsPM9sg', '139-9671-9208', 'RnHcXgpix3', 'YJ9Cd223MF', '2019-07-30 00:09:58', '2022-12-30 02:28:19');
INSERT INTO `dbc_supplier` VALUES (5, 'Chin Sum Wing', '463 Flatbush Ave', 'ZyzHvIrzXf', '718-769-2127', '3ewMhTOFnW', 'Jx8ZjPPLQb', '2011-03-30 12:57:34', '2013-02-10 10:29:11');
INSERT INTO `dbc_supplier` VALUES (6, 'Wang Jiehong', '1-7-16 Saidaiji Akodacho', 'stvVfMzPlk', '80-1767-3292', 'k1PrlFhP01', 'Wk6b94Xkq6', '2007-06-26 22:31:39', '2011-10-08 00:51:26');
INSERT INTO `dbc_supplier` VALUES (8, 'Yamashita Takuya', '192 Lower Temple Street', 'sV5DsE3Ij8', '(121) 381 7384', 'OBeYvrjYTU', 'k7IaHlJ4iP', '2022-11-13 13:52:40', '2007-08-24 18:02:47');
INSERT INTO `dbc_supplier` VALUES (10, 'Kwong Sze Yu', '880 Wooster Street', 'vvAuuMmwsi', '212-496-8986', 'KvtylH5odj', 'valxxNA6kN', '2000-12-04 20:22:44', '2004-04-20 05:07:01');
INSERT INTO `dbc_supplier` VALUES (11, 'Ichikawa Ryota', '873 Portland St', '0oKPMmwxhf', '5274 118890', '4jfxcbKLPj', '2qBMe79JxE', '2011-08-02 06:02:59', '2023-06-26 03:56:20');

SET FOREIGN_KEY_CHECKS = 1;
