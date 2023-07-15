/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_sub

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 14/07/2023 09:31:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sub_daily_report
-- ----------------------------
DROP TABLE IF EXISTS `sub_daily_report`;
CREATE TABLE `sub_daily_report`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `substation_id` bigint(20) NULL DEFAULT NULL,
  `substation_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `statistic_time` datetime NULL DEFAULT NULL,
  `is_settled` tinyint(1) NULL DEFAULT NULL,
  `courier_num` int(11) NULL DEFAULT NULL,
  `delivery_receive_task_num` int(11) NULL DEFAULT NULL,
  `receive_task_num` int(11) NULL DEFAULT NULL,
  `return_task_num` int(11) NULL DEFAULT NULL,
  `exchange_task_num` int(11) NULL DEFAULT NULL,
  `delivery_task_num` int(11) NULL DEFAULT NULL,
  `finish_task_num` int(11) NULL DEFAULT NULL,
  `fail_task_num` int(11) NULL DEFAULT NULL,
  `part_finish_task_num` int(11) NULL DEFAULT NULL,
  `sign_num` int(11) NULL DEFAULT NULL,
  `receive` double NULL DEFAULT NULL,
  `return_num` int(11) NULL DEFAULT NULL,
  `refund` double(20, 2) NULL DEFAULT NULL,
  `delivery_fee` double(20, 2) NULL DEFAULT NULL,
  `to_pay` double(20, 2) NULL DEFAULT NULL,
  `feedback` double(20, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sub_daily_report
-- ----------------------------

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
-- Records of sub_delivery
-- ----------------------------

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
-- Records of sub_finance
-- ----------------------------

-- ----------------------------
-- Table structure for sub_master_sub
-- ----------------------------
DROP TABLE IF EXISTS `sub_master_sub`;
CREATE TABLE `sub_master_sub`  (
  `master_id` bigint(20) NOT NULL COMMENT '分站管理员ID',
  `sub_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sub_master_sub
-- ----------------------------

-- ----------------------------
-- Table structure for sub_pending_product
-- ----------------------------
DROP TABLE IF EXISTS `sub_pending_product`;
CREATE TABLE `sub_pending_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` double(20, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `deal_number` int(20) NULL DEFAULT NULL COMMENT '待处理数量',
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分库ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sub_pending_product
-- ----------------------------

-- ----------------------------
-- Table structure for sub_receipt
-- ----------------------------
DROP TABLE IF EXISTS `sub_receipt`;
CREATE TABLE `sub_receipt`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回执ID',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收人姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户联系电话',
  `sub_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `task_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回执任务状态',
  `courier_id` bigint(20) NULL DEFAULT NULL COMMENT '配送员ID',
  `feedback` int(12) NULL DEFAULT NULL COMMENT '客户满意度',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `delivery_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货/退货地址',
  `sign_time` datetime NULL DEFAULT NULL COMMENT '签收时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '回执录入日期',
  `plan_num` int(20) NULL DEFAULT NULL COMMENT '计划商品数量',
  `plan_receipt` double(20, 2) NULL DEFAULT NULL COMMENT '计划金额',
  `sign_num` int(20) NULL DEFAULT NULL COMMENT '总签收数量',
  `refund_num` int(20) NULL DEFAULT NULL COMMENT '总退回数量',
  `input_money` double(20, 2) NULL DEFAULT NULL COMMENT '总收款',
  `output_money` double(20, 2) NULL DEFAULT NULL COMMENT '总退款',
  `invoice_number` bigint(20) NULL DEFAULT NULL COMMENT '发票号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回执单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sub_receipt
-- ----------------------------
INSERT INTO `sub_receipt` VALUES (2, 1, '徐高松', '15824035076', 7, '送货收款', '成功', 3, 9, '成功完成任务', '东北大学', '2023-07-09 00:00:00', '2023-07-10 23:04:03', 4, 2410.06, 4, 0, 2410.06, 0.00, NULL);
INSERT INTO `sub_receipt` VALUES (3, 7, '徐高松', '15824035076', 7, '送货收款', '部分完成', 3, 9, '', '东北大学', '2023-07-12 00:00:00', '2023-07-13 21:08:25', 6, 3615.09, 4, 2, 2410.06, 0.00, NULL);

-- ----------------------------
-- Table structure for sub_receipt_product
-- ----------------------------
DROP TABLE IF EXISTS `sub_receipt_product`;
CREATE TABLE `sub_receipt_product`  (
  `receipt_id` bigint(20) NOT NULL COMMENT '回执ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `all_num` int(20) NULL DEFAULT NULL COMMENT '总数量',
  `sign_num` int(20) NULL DEFAULT NULL COMMENT '签收数量',
  `return_num` int(20) NULL DEFAULT NULL COMMENT '退货数量',
  `input_money` double(20, 2) NULL DEFAULT NULL COMMENT '收来的钱',
  `output_money` double(20, 2) NULL DEFAULT NULL COMMENT '退款的钱'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单商品的签收情况' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sub_receipt_product
-- ----------------------------
INSERT INTO `sub_receipt_product` VALUES (2, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 405.01, 2, 2, 0, 810.02, 0.00);
INSERT INTO `sub_receipt_product` VALUES (2, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 800.02, 2, 2, 0, 1600.04, 0.00);
INSERT INTO `sub_receipt_product` VALUES (3, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 405.01, 3, 2, 1, 810.02, 0.00);
INSERT INTO `sub_receipt_product` VALUES (3, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 800.02, 3, 2, 1, 1600.04, 0.00);

-- ----------------------------
-- Table structure for sub_substation
-- ----------------------------
DROP TABLE IF EXISTS `sub_substation`;
CREATE TABLE `sub_substation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分站ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分站名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分站地址',
  `phone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '分站电话',
  `subware_id` bigint(20) NULL DEFAULT NULL COMMENT '分站对应的分仓库的ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '分站' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sub_substation
-- ----------------------------
INSERT INTO `sub_substation` VALUES (6, '浙江站', '浙江省台州市临海市', '154587954', 2);
INSERT INTO `sub_substation` VALUES (7, '海南站', '海南省海口市琼山区', '545634654', 1);
INSERT INTO `sub_substation` VALUES (8, '河北站', '河北省石家庄市长安区范光胡同8号', '546245621', 3);

-- ----------------------------
-- Table structure for sub_task
-- ----------------------------
DROP TABLE IF EXISTS `sub_task`;
CREATE TABLE `sub_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务单ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人姓名',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人电话',
  `delivery_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `courier_id` bigint(20) NULL DEFAULT NULL COMMENT '快递员ID',
  `numbers` int(20) NOT NULL COMMENT '商品总数',
  `total_amount` double(20, 2) NOT NULL COMMENT '商品总价',
  `deadline` datetime NOT NULL COMMENT '要求完成日期',
  `create_time` datetime NULL DEFAULT NULL COMMENT '生成日期',
  `task_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型',
  `task_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `products_json` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品列表json字符串',
  `sub_id` bigint(20) NULL DEFAULT NULL COMMENT '分站ID',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  `need_invoice` tinyint(1) NULL DEFAULT NULL COMMENT '是否需要发票',
  `receipt_id` bigint(20) NULL DEFAULT NULL COMMENT '回执ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sub_task
-- ----------------------------
INSERT INTO `sub_task` VALUES (1, 8, '徐高松', '15824035076', '东北大学', 20, 3, 4, 2410.06, '2023-07-10 00:00:00', '2023-07-10 22:06:34', '送货收款', '已完成', '[{\"number\":2,\"price\":405.01,\"productCategary\":\"电脑\",\"productId\":7,\"productName\":\"Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线\",\"refundAble\":true},{\"number\":2,\"price\":800.02,\"productCategary\":\"玩具\",\"productId\":9,\"productName\":\"LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物\",\"refundAble\":true}]', 7, 0, 1, 2);
INSERT INTO `sub_task` VALUES (7, 8, '徐高松', '15824035076', '东北大学', 21, 3, 6, 3615.09, '2023-07-14 00:00:00', '2023-07-13 13:05:46', '送货收款', '已完成', '[{\"number\":3,\"price\":405.01,\"productCategary\":\"电脑\",\"productId\":7,\"productName\":\"Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线\",\"refundAble\":true},{\"number\":3,\"price\":800.02,\"productCategary\":\"玩具\",\"productId\":9,\"productName\":\"LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物\",\"refundAble\":true}]', 7, 0, 1, 3);
INSERT INTO `sub_task` VALUES (12, 8, '徐高松', '15824035076', '东北大学', 20, 3, 4, 2410.06, '2023-07-10 00:00:00', '2023-07-13 20:51:52', '退货', '已分配', '[{\"number\":2,\"price\":405.01,\"productCategary\":\"电脑\",\"productId\":7,\"productName\":\"Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线\",\"refundAble\":true},{\"number\":2,\"price\":800.02,\"productCategary\":\"玩具\",\"productId\":9,\"productName\":\"LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物\",\"refundAble\":true}]', 7, 0, 1, NULL);

-- ----------------------------
-- Table structure for sub_user_sub
-- ----------------------------
DROP TABLE IF EXISTS `sub_user_sub`;
CREATE TABLE `sub_user_sub`  (
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '快递员ID',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '子站ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sub_user_sub
-- ----------------------------
INSERT INTO `sub_user_sub` VALUES (3, 7);

SET FOREIGN_KEY_CHECKS = 1;
