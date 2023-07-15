/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_ac

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 14/07/2023 09:30:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ac_invoice
-- ----------------------------
DROP TABLE IF EXISTS `ac_invoice`;
CREATE TABLE `ac_invoice`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `start_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开始号码',
  `end_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结束号码',
  `registration` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '未登记' COMMENT '登记状态',
  `batch` int(255) NOT NULL COMMENT '批次',
  `total` int(30) NOT NULL COMMENT '本数',
  `time` datetime NOT NULL COMMENT '发票生成日期',
  `substation_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '暂无信息' COMMENT '分站id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ac_invoice
-- ----------------------------
INSERT INTO `ac_invoice` VALUES (1, 'RE20230606002', 'RE20230606053', '已登记', 2, 51, '2023-06-06 10:29:47', '1');
INSERT INTO `ac_invoice` VALUES (2, 'UE20230609019', 'UE20230609060', '已登记', 1, 41, '2023-06-10 03:45:21', '2');
INSERT INTO `ac_invoice` VALUES (3, 'RE20230606002', 'RE20230606053', '已登记', 2, 41, '2023-06-10 03:45:21', '7');
INSERT INTO `ac_invoice` VALUES (4, 'CY20230606001', 'CY20230606080', '已登记', 2, 79, '2023-06-17 03:45:21', '3');
INSERT INTO `ac_invoice` VALUES (5, 'RE20230606002', 'RE20230606053', '已登记', 2, 41, '2023-06-10 03:45:21', '暂无信息');
INSERT INTO `ac_invoice` VALUES (6, 'RE20230606002', 'RE20230606053', '已登记', 2, 41, '2023-06-10 03:45:21', '暂无信息');
INSERT INTO `ac_invoice` VALUES (7, 'RE20230606002', 'RE20230606053', '已登记', 2, 41, '2023-06-10 03:45:21', '暂无信息');
INSERT INTO `ac_invoice` VALUES (8, 'UE20230609019', 'UE20230609060', '未登记', 3, 41, '2023-06-10 03:45:21', '暂无信息');
INSERT INTO `ac_invoice` VALUES (50, 'WH20230626001', 'WH20230626005', '已登记', 3, 5, '2023-06-27 16:07:51', '7');
INSERT INTO `ac_invoice` VALUES (51, 'UY20230606001', 'UY20230606080', '已登记', 4, 80, '2023-06-28 00:30:58', '暂无信息');
INSERT INTO `ac_invoice` VALUES (52, 'UJ20230705010', 'UJ20230705020', '未登记', 4, 10, '2023-07-05 18:33:06', '暂无信息');

-- ----------------------------
-- Table structure for ac_invoices
-- ----------------------------
DROP TABLE IF EXISTS `ac_invoices`;
CREATE TABLE `ac_invoices`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `totalid` bigint(20) NOT NULL COMMENT '发票批次序号',
  `number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票号码',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '未领用' COMMENT '发票领用状态',
  `batch` int(255) NOT NULL COMMENT '批次',
  `substation_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '暂无信息' COMMENT '分站id',
  `employee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '暂无信息' COMMENT '领用人',
  `dstate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '生效中' COMMENT '失效状态',
  `details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '无' COMMENT '失效原因',
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '无' COMMENT '订单号',
  `printtime` datetime NULL DEFAULT NULL COMMENT '打印时间',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '无' COMMENT '商品名称',
  `productNum` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '无' COMMENT '商品数量',
  PRIMARY KEY (`id`, `employee`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分站发票管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ac_invoices
-- ----------------------------
INSERT INTO `ac_invoices` VALUES (1, 1, 'RE20230606002', '已领用', 1, '1', '某个快递员', '生效中', '无', '95', NULL, '420', '60');
INSERT INTO `ac_invoices` VALUES (2, 1, 'RE20230606003', '未领用', 1, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (3, 1, 'RE20230606004', '已领用', 2, '7', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (4, 1, 'RE20230606005', '未领用', 1, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (5, 2, 'UE20230609019', '未领用', 2, '2', '暂无信息', '生效中', '发票丢失', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (41, 50, 'WH2023062001', '未领用', 3, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (42, 50, 'WH2023062002', '未领用', 3, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (43, 50, 'WH2023062003', '未领用', 3, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (44, 50, 'WH2023062004', '未领用', 3, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (45, 50, 'WH2023062005', '未领用', 3, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (46, 51, 'UY2023060001', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (47, 51, 'UY2023060002', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (48, 51, 'UY2023060003', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (49, 51, 'UY2023060004', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (50, 51, 'UY2023060005', '未领用', 3, '7', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (51, 51, 'UY2023060006', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (52, 51, 'UY2023060007', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (53, 51, 'UY2023060008', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (54, 51, 'UY2023060009', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (55, 51, 'UY2023060010', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (56, 51, 'UY2023060011', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (57, 51, 'UY2023060012', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (58, 51, 'UY2023060013', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (59, 51, 'UY2023060014', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (60, 51, 'UY2023060015', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (61, 51, 'UY2023060016', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (62, 51, 'UY2023060017', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (63, 51, 'UY2023060018', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (64, 51, 'UY2023060019', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (65, 51, 'UY2023060020', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (66, 51, 'UY2023060021', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (67, 51, 'UY2023060022', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (68, 51, 'UY2023060023', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (69, 51, 'UY2023060024', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (70, 51, 'UY2023060025', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (71, 51, 'UY2023060026', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (72, 51, 'UY2023060027', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (73, 51, 'UY2023060028', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (74, 51, 'UY2023060029', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (75, 51, 'UY2023060030', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (76, 51, 'UY2023060031', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (77, 51, 'UY2023060032', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (78, 51, 'UY2023060033', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (79, 51, 'UY2023060034', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (80, 51, 'UY2023060035', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (81, 51, 'UY2023060036', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (82, 51, 'UY2023060037', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (83, 51, 'UY2023060038', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (84, 51, 'UY2023060039', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (85, 51, 'UY2023060040', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (86, 51, 'UY2023060041', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (87, 51, 'UY2023060042', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (88, 51, 'UY2023060043', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (89, 51, 'UY2023060044', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (90, 51, 'UY2023060045', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (91, 51, 'UY2023060046', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (92, 51, 'UY2023060047', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (93, 51, 'UY2023060048', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (94, 51, 'UY2023060049', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (95, 51, 'UY2023060050', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (96, 51, 'UY2023060051', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (97, 51, 'UY2023060052', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (98, 51, 'UY2023060053', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (99, 51, 'UY2023060054', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (100, 51, 'UY2023060055', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (101, 51, 'UY2023060056', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (102, 51, 'UY2023060057', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (103, 51, 'UY2023060058', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (104, 51, 'UY2023060059', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (105, 51, 'UY2023060060', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (106, 51, 'UY2023060061', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (107, 51, 'UY2023060062', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (108, 51, 'UY2023060063', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (109, 51, 'UY2023060064', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (110, 51, 'UY2023060065', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (111, 51, 'UY2023060066', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (112, 51, 'UY2023060067', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (113, 51, 'UY2023060068', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (114, 51, 'UY2023060069', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (115, 51, 'UY2023060070', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (116, 51, 'UY2023060071', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (117, 51, 'UY2023060072', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (118, 51, 'UY2023060073', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (119, 51, 'UY2023060074', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (120, 51, 'UY2023060075', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (121, 51, 'UY2023060076', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (122, 51, 'UY2023060077', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (123, 51, 'UY2023060078', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (124, 51, 'UY2023060079', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (125, 51, 'UY2023060080', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (126, 52, 'UJ2023070002', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (127, 52, 'UJ2023070003', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (128, 52, 'UJ2023070004', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (129, 52, 'UJ2023070005', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (130, 52, 'UJ2023070006', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (131, 52, 'UJ2023070007', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (132, 52, 'UJ2023070008', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (133, 52, 'UJ2023070009', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (134, 52, 'UJ2023070010', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');
INSERT INTO `ac_invoices` VALUES (135, 52, 'UJ2023070011', '未领用', 4, '暂无信息', '暂无信息', '生效中', '无', '无', NULL, '无', '无');

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
  `time` datetime NULL DEFAULT NULL COMMENT '日期',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否结算',
  `settleType` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '退款' COMMENT '退款还是支出',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ac_supply
-- ----------------------------
INSERT INTO `ac_supply` VALUES (17, 1, '笔记本电脑', 9000.00, 100, 150, -50, 450000.00, '2023-06-30 10:02:23', 0, '退款');
INSERT INTO `ac_supply` VALUES (18, 1, '笔记本电脑', 9000.00, 100, 0, 100, 900000.00, '2023-06-30 11:16:56', 0, '支出');
INSERT INTO `ac_supply` VALUES (19, 1, '华为手机', 2499.00, 0, 150, -150, 374850.00, '2023-06-30 11:18:02', 0, '退款');
INSERT INTO `ac_supply` VALUES (20, 1, '索尼相机', 16999.00, 0, 10, -10, 169990.00, '2023-06-30 11:48:23', 0, '退款');

SET FOREIGN_KEY_CHECKS = 1;
