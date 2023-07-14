/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19-log)
 Source Host           : localhost:3306
 Source Schema         : xanadu_cc

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19-log)
 File Encoding         : 65001

 Date: 14/07/2023 09:30:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cc_customer
-- ----------------------------
DROP TABLE IF EXISTS `cc_customer`;
CREATE TABLE `cc_customer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `identity_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `telephone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `organization` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作单位',
  `postcode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '是否被删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cc_customer
-- ----------------------------
INSERT INTO `cc_customer` VALUES (8, '2023-07-07 15:41:44', '2023-07-07 15:41:44', '徐高松', '133464648466', '东北大学', '15824035076', '东北大学', '324557', 0);
INSERT INTO `cc_customer` VALUES (9, '2018-08-10 19:06:58', '2023-07-07 16:05:58', 'Arimura', 'juoyuoyuiyou', '720 Shanhu Rd', '158-8542-1291111', 'Cj5DCALiNF', 'Z3ah8DvP72', 1);
INSERT INTO `cc_customer` VALUES (10, '2015-08-23 07:49:28', '2023-07-07 15:44:35', 'Todd Evans', 'hvjhhCKoAB', '598 Wooster Street', '212-551-8637', '1ftf1QfNVN', 'F3HFUZzAv1', 1);
INSERT INTO `cc_customer` VALUES (11, '2002-04-28 20:54:40', '2023-07-09 11:29:08', 'Takagi Mai', 'c50rkFkoNp', '419 Cyril St, Braunstone Town', '(116) 130 1142', 'QQ04m9VFBm', 'jRgg6BFoDW', 1);
INSERT INTO `cc_customer` VALUES (12, '2001-09-24 20:59:19', '2023-07-09 11:29:11', 'Chung Wing Kuen', 'AuyDjmbYh3', '337 Earle Rd', '7384 240991', 'rCoahzP971', 'eiD7yoHGKP', 1);
INSERT INTO `cc_customer` VALUES (13, '2022-04-23 22:43:36', '2023-07-09 11:29:12', 'Alice Lee', 'P7cUJ6AbNT', '403 4th Section  Renmin South Road, Jinjiang District', '168-0510-1580', '7Kb5yjj5DT', 'Z37pmBqPye', 1);
INSERT INTO `cc_customer` VALUES (14, '2007-09-30 23:52:04', '2010-11-15 03:50:11', 'Bonnie Mcdonald', 'N2jQIBpl3r', '308 Alameda Street', '213-134-5669', 'AWtEYhSUn8', 'ADNJRHut4H', 0);
INSERT INTO `cc_customer` VALUES (15, '2001-08-24 13:34:01', '2005-03-07 01:56:28', 'Ota Ayano', 'LZojcm4qJI', '889 Ridgewood Road', '330-051-3963', 'gfIJ1NDTEY', 'yz6yVHfPKb', 0);
INSERT INTO `cc_customer` VALUES (16, '2006-02-28 11:32:56', '2007-01-23 10:52:37', 'Stephanie Nelson', 'MedKp7ldE1', '13-3-7 Toyohira 3 Jo, Toyohira Ward', '70-1258-9492', '1EMdFGftmW', '6If30OS8lU', 0);
INSERT INTO `cc_customer` VALUES (17, '2021-04-03 10:14:42', '2002-08-16 04:16:47', 'Jiang Xiaoming', '6nzvkh0oL3', '48 Wall Street', '213-948-6557', 'oLmAnpvMDj', 'anccEFGbs8', 0);
INSERT INTO `cc_customer` VALUES (18, '2019-09-24 01:24:01', '2021-10-23 21:10:20', 'Nishimura Seiko', 'aFI4cnjp67', '1-5-16, Higashi-Shimbashi, Minato-ku', '80-0267-8761', 'JqknkuXojA', '4gt0503mtX', 0);
INSERT INTO `cc_customer` VALUES (19, '2017-12-24 12:22:14', '2006-08-08 14:16:23', 'Kudo Daisuke', 'l0eabfddj3', '720 Tremont Road', '614-726-8008', 'Pfr2YPZn5y', '1UT9I7IrPl', 0);
INSERT INTO `cc_customer` VALUES (20, '2014-03-10 22:07:25', '2006-12-25 08:37:21', 'Fujita Kaito', 'O3BUUEmKrG', '6-1-11, Miyanomori 4 Jō, Chuo Ward', '11-999-1691', '3FOHnMUfYZ', 'dQyLq62G0f', 0);
INSERT INTO `cc_customer` VALUES (21, '2017-05-20 08:23:14', '2016-07-10 08:01:39', 'Yu Lan', 'ADQS9fCHR9', '261 East Wangfujing Street, Dongcheng District ', '197-6698-9030', 'RVIWYP790j', 'pxlXY5o8Bc', 0);
INSERT INTO `cc_customer` VALUES (22, '2001-12-28 02:21:09', '2005-10-18 19:40:03', 'Ueda Ikki', 'EHqHBfTVkb', '3-15-1 Ginza, Chuo-ku', '3-4651-7947', 'riUgPWcPPq', 'hzrBd7COl9', 0);
INSERT INTO `cc_customer` VALUES (23, '2014-05-17 07:49:43', '2017-06-22 02:13:33', 'Shi Jiehong', 'ImwUMxJCna', '980 Papworth Rd, Trumpington', '5567 395199', 'JTmtxIguL7', 'uLMhJ04zXU', 0);
INSERT INTO `cc_customer` VALUES (24, '2016-11-07 22:23:10', '2017-06-14 01:44:27', 'Cheung Wai Yee', 'xzq8XZlkA0', '426 Sky Way', '213-578-4091', 'XFGECyZIrW', 'czcU84lkP2', 0);
INSERT INTO `cc_customer` VALUES (25, '2001-02-05 22:55:41', '2014-04-22 05:18:59', 'Nishimura Mai', '104D3zhr0j', '360 Jingtian East 1st St, Futian District', '158-0958-2899', 'QRTRWyonfK', '9sKUSenomL', 0);
INSERT INTO `cc_customer` VALUES (26, '2019-10-31 05:35:11', '2004-04-30 04:53:56', 'Frances Butler', 'XYzRJ3RuXO', '882 Columbia St', '718-040-2249', 'GpiPpEdioA', '0aAwC9OH0i', 0);
INSERT INTO `cc_customer` VALUES (27, '2020-06-13 14:51:50', '2021-08-17 10:54:49', 'Fujiwara Kazuma', '8gTBPVQnY9', '831 Silver St, Newnham', '(1223) 99 9919', 'A89kGUSQFd', 'xIh0RZXlap', 0);
INSERT INTO `cc_customer` VALUES (28, '2021-11-15 20:16:30', '2020-08-28 12:00:54', 'Chow Ming', 'WeUmJthWmm', '5-2-7 Higashi Gotanda, Shinagawa-ku ', '80-0368-3265', 'HSDwo4hx4i', 'enJElatC4O', 0);
INSERT INTO `cc_customer` VALUES (29, '2013-06-18 10:29:42', '2011-06-28 06:10:41', 'Yamashita Ayano', '5y6ldjTcin', '261 Columbia St', '718-333-5050', 'CkzeK6tM25', 'WGrlYE4k3s', 0);
INSERT INTO `cc_customer` VALUES (30, '2015-06-07 14:29:40', '2007-08-03 18:08:30', 'Janice Johnson', 'iU9HZuOAhZ', '67 028 County Rd, Yanqing District', '196-9847-3608', 'EYgUESoAaI', 'K0der9V9cM', 0);
INSERT INTO `cc_customer` VALUES (31, '2009-10-19 21:03:22', '2010-09-12 11:17:33', 'Herbert Alvarez', 'dbMWgsdZFA', '99 1st Ave', '718-126-6257', 'AS1GJrH002', 'IopfzEXyFk', 0);
INSERT INTO `cc_customer` VALUES (32, '2020-02-21 11:04:08', '2017-04-01 03:54:03', 'Tsui Hok Yau', 'jymEDMAzV1', '151 Middle Huaihai Road, Huangpu District', '140-1808-4675', 'kCVr6UDdHb', '4esKLfICBu', 0);
INSERT INTO `cc_customer` VALUES (33, '2009-10-31 21:37:18', '2005-12-12 08:49:57', 'Mario Martin', 'xe6LG10TPx', '121 Trafalgar Square, Charing Cross', '(151) 140 0267', '6fffJNUqQi', '9A4uYo59xH', 0);
INSERT INTO `cc_customer` VALUES (34, '2004-07-31 23:20:23', '2015-07-19 18:13:40', 'Ito Rena', 'UghJRyF1CX', '6 1-1715 Sekohigashi, Moriyama Ward', '90-3397-3477', 'VnLtGmi4ka', 'nBA2oAACXY', 0);
INSERT INTO `cc_customer` VALUES (35, '2016-01-25 13:43:06', '2014-08-08 01:46:40', 'Chu Wai Man', 'KaDZqJm5iP', '916 Qingshuihe 1st Rd, Luohu District', '755-347-0080', 'XaoJTru7PT', '3Roj6Eyc3N', 0);
INSERT INTO `cc_customer` VALUES (36, '2021-04-16 13:53:59', '2013-11-02 12:15:20', 'Kobayashi Takuya', '226dbIkoik', '643 Abingdon Rd, Cumnor', '5800 883538', 'exinrWX97B', '1kXRUc1fC8', 0);
INSERT INTO `cc_customer` VALUES (37, '2011-05-31 04:41:43', '2013-03-10 07:43:51', 'Goto Mio', 'N6fOaTloH0', '450 Columbia St', '718-661-9495', 'iB5hRkdYaI', 'AdgIhejFyf', 0);
INSERT INTO `cc_customer` VALUES (38, '2010-02-24 10:11:35', '2000-01-16 00:33:59', 'Jiang Jialun', 'yg9JmJIEw3', '687 Fifth Avenue', '212-637-8473', '0Ktoed1Jn4', 'fOhE0iulHj', 0);
INSERT INTO `cc_customer` VALUES (39, '2017-02-17 19:14:14', '2007-12-06 10:18:35', 'Sit Wai Man', 'ZbNHU38JPp', '935 Rush Street', '312-637-5819', 'Xv58hTUO86', '6kr335150U', 0);
INSERT INTO `cc_customer` VALUES (40, '2012-07-30 15:07:34', '2008-09-06 12:42:52', 'Stephen Hughes', 'mV6HZe1mLZ', '641 Yueliu Rd, Fangshan District', '10-928-9721', 'bCLTZijAyf', 'hD2fz4MaFv', 0);
INSERT INTO `cc_customer` VALUES (41, '2020-03-22 04:07:53', '2004-05-23 11:06:55', 'Fu Yuning', 'g21tFwOJn6', '2-5-14 Chitose, Atsuta Ward', '90-1379-2016', 'kmdNMsQbP2', '6Bd43WdwyQ', 0);
INSERT INTO `cc_customer` VALUES (42, '2016-05-15 21:01:07', '2014-04-08 21:16:48', 'Nakamori Nanami', 'OilE8WJcvP', '1-5-10, Higashi-Shimbashi, Minato-ku', '90-0967-5394', 'YFeIV02dBo', 'NuNbNsPTOS', 0);
INSERT INTO `cc_customer` VALUES (43, '2002-05-17 15:35:37', '2007-04-21 04:42:29', 'Ichikawa Aoshi', 'Du7PUBQa5I', '440 Ridgewood Road', '330-696-2252', 'Yf768EiPDq', '9owAIJNbPI', 0);
INSERT INTO `cc_customer` VALUES (44, '2019-03-11 01:10:34', '2011-10-25 05:49:13', 'Arimura Momoka', 'JpF71kv2SY', '7 Fern Street', '330-366-1079', 'tfTeWbaOaT', 'rnc7WSJQ0H', 0);
INSERT INTO `cc_customer` VALUES (45, '2018-03-04 20:12:55', '2003-05-25 16:02:42', 'Travis Stephens', 'INxEAvI72l', '934 Hanover St', '7293 730477', '35aEWxnc1f', '3lRvhXnQJm', 0);
INSERT INTO `cc_customer` VALUES (46, '2007-05-04 02:59:49', '2016-08-29 11:36:30', 'Chan Hui Mei', 'DETR0WLLTP', '2-1-9 Tenjinnomori, Nishinari Ward', '66-369-3324', 'sLe1amKFwm', 'Ahem7ubNE3', 0);
INSERT INTO `cc_customer` VALUES (47, '2017-11-27 10:47:23', '2007-07-09 07:35:48', 'Hara Ren', 'rwdV79foQe', '1-7-15 Saidaiji Akodacho', '74-078-6465', '2uqxJEX0Jk', 'iu5Z2pipCw', 0);
INSERT INTO `cc_customer` VALUES (48, '2021-07-02 10:16:22', '2006-11-03 22:28:47', 'Ku Chi Yuen', 'sqH1yykfFK', '959 Bank Street', '212-136-8142', 'cofi37L6OW', 'sRnBtpxHsx', 0);
INSERT INTO `cc_customer` VALUES (49, '2014-03-12 19:44:18', '2008-04-22 23:43:45', 'Qin Xiuying', 'sVUgizFK6l', '436 Osney Mead', '(1865) 96 6285', 'KllwriOrAE', 'oGDpSwMDoh', 0);
INSERT INTO `cc_customer` VALUES (50, '2022-10-19 00:19:57', '2008-01-30 10:07:39', 'Shen Lu', 'YDqGHzZkSC', '209 Bank Street', '212-745-2604', 'mY23rCYhmx', 'KDCvvp6ZWW', 0);
INSERT INTO `cc_customer` VALUES (51, '2005-06-11 19:17:18', '2000-09-27 03:02:52', 'Koon Fat', 'bsBI1I3LsY', '85 Grape Street', '213-436-5200', 'BQCNhEWDIs', 'pGGeqT9xFX', 0);
INSERT INTO `cc_customer` VALUES (52, '2002-06-19 22:16:10', '2016-10-18 05:55:14', 'Liu Yuning', '6mDPT1uyvC', '1-5-1, Higashi-Shimbashi, Minato-ku', '3-5128-0950', 'CnxIWhXA4Z', 'U4xsP57HFa', 0);
INSERT INTO `cc_customer` VALUES (53, '2008-07-03 16:59:08', '2006-02-22 03:44:08', 'Huang Rui', 'zy0G0etmeH', '189 Wicklow Road', '614-690-4605', 'ZY6GkDBXIR', 'TioEj3JRWE', 0);
INSERT INTO `cc_customer` VALUES (54, '2008-02-19 12:52:24', '2017-03-07 06:12:32', 'Liang Ziyi', 'GbYn93Fodg', '486 Collier Road', '330-576-9215', '50x8KMbbkF', '7X4aZWUDBo', 0);
INSERT INTO `cc_customer` VALUES (55, '2010-05-26 00:02:21', '2007-12-03 21:35:54', 'Yamashita Yuito', 'DlGFbziWSY', '23 Bergen St', '718-980-9853', '4QVaFXaLCI', 'aveVBV28Pi', 0);
INSERT INTO `cc_customer` VALUES (56, '2015-06-05 08:50:41', '2005-07-23 08:54:17', 'Nakayama Ren', 'iayEQZS7ig', '3-19-2 Shimizu, Kita Ward', '52-606-3859', 'Mb155MNxyG', 'bJNqQwqLxb', 0);
INSERT INTO `cc_customer` VALUES (57, '2014-09-13 09:18:07', '2017-06-07 07:45:34', 'Yeung Kwok Yin', 'Hg9wwbTced', '2-5-16 Chitose, Atsuta Ward', '80-8238-8934', 'LMpuWAjBZK', 'xiFR3a6EBL', 0);
INSERT INTO `cc_customer` VALUES (58, '2007-04-09 20:06:09', '2021-06-20 23:43:08', 'Shing Hui Mei', 'EWcj1mtk7q', '6-1-10, Miyanomori 4 Jō, Chuo Ward', '11-945-6601', 'mhLFGROxTt', 'mJhnKk8qKj', 0);

-- ----------------------------
-- Table structure for cc_new_order
-- ----------------------------
DROP TABLE IF EXISTS `cc_new_order`;
CREATE TABLE `cc_new_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对应的order主表ID',
  `telephone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `delivery_time` date NULL DEFAULT NULL COMMENT '预计送货日期',
  `invoice_need` int(1) NULL DEFAULT 0 COMMENT '是否要发票',
  `delivery_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `receiver_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人姓名',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '子站ID',
  `new_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订购类型（先付款后送货，先送货后付款）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cc_new_order
-- ----------------------------
INSERT INTO `cc_new_order` VALUES (3, '15824035076', '', '2023-07-08', 1, '东北大学', '徐高松', 1, 2, '货到付款');
INSERT INTO `cc_new_order` VALUES (5, '15824035076', '', '2023-07-13', 0, '东北大学', '徐高松', 0, 7, '货到付款');
INSERT INTO `cc_new_order` VALUES (6, '15824035076', '', '2023-06-27', 0, '东北大学', '徐高松', 0, 6, '货到付款');
INSERT INTO `cc_new_order` VALUES (18, '15824035076', '', '2023-07-06', 0, '东北大学', '徐高松', 0, 8, '货到付款');
INSERT INTO `cc_new_order` VALUES (19, '15824035076', '', '2023-07-09', 0, '东北大学', '徐高松', 0, 6, '货到付款');
INSERT INTO `cc_new_order` VALUES (20, '15824035076', '电脑玩具', '2023-07-08', 1, '东北大学', '徐高松', 0, 7, '货到付款');
INSERT INTO `cc_new_order` VALUES (21, '15824035076', '', '2023-07-13', 1, '东北大学', '徐高松', 0, 7, '货到付款');
INSERT INTO `cc_new_order` VALUES (22, '15824035076', '', '2023-07-12', 1, '东北大学', '徐高松', 0, 6, '货到付款');
INSERT INTO `cc_new_order` VALUES (23, '15824035076', '', '2023-07-14', 1, '东北大学', '徐高松', 0, 6, '货到付款');
INSERT INTO `cc_new_order` VALUES (25, '15824035076', '易碎物品', '2023-07-21', 1, '东北大学', '徐高松', 0, 6, '付款送货');

-- ----------------------------
-- Table structure for cc_operation
-- ----------------------------
DROP TABLE IF EXISTS `cc_operation`;
CREATE TABLE `cc_operation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作客服ID',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `operator_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `numbers` int(10) NULL DEFAULT NULL COMMENT '操作商品数量',
  `total_amount` double(20, 2) NULL DEFAULT NULL COMMENT '操作金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cc_operation
-- ----------------------------
INSERT INTO `cc_operation` VALUES (5, '2023-07-09 16:22:47', 1, 8, 5, '新订', 5, 292.00);
INSERT INTO `cc_operation` VALUES (6, '2023-07-09 16:41:08', 1, 8, 6, '新订', 5, 305.00);
INSERT INTO `cc_operation` VALUES (8, '2023-07-10 13:57:28', 1, 8, 6, '新订', 5, 305.00);
INSERT INTO `cc_operation` VALUES (13, '2023-07-10 13:57:28', 1, 8, 5, '新订', 4, 292.00);
INSERT INTO `cc_operation` VALUES (16, '2023-07-10 13:57:28', 1, 8, 19, '新订', 6, 438.21);
INSERT INTO `cc_operation` VALUES (17, '2023-07-10 19:49:11', 1, 8, 20, '新订', 4, 2410.06);
INSERT INTO `cc_operation` VALUES (18, '2023-07-10 21:47:04', 1, 8, 19, '撤销', 6, 438.21);
INSERT INTO `cc_operation` VALUES (19, '2023-07-10 21:47:53', 1, 8, 5, '撤销', 5, 292.00);
INSERT INTO `cc_operation` VALUES (20, '2023-07-10 21:47:54', 1, 8, 6, '撤销', 3, 159.00);
INSERT INTO `cc_operation` VALUES (21, '2023-07-11 09:31:52', 1, 8, 5, '撤销', 5, 292.00);
INSERT INTO `cc_operation` VALUES (22, '2023-07-11 09:31:52', 1, 8, 6, '撤销', 3, 159.00);
INSERT INTO `cc_operation` VALUES (23, '2023-07-11 09:31:52', 1, 8, 19, '撤销', 6, 438.21);
INSERT INTO `cc_operation` VALUES (24, '2023-07-11 09:31:52', 1, 8, 20, '新订', 4, 2410.06);
INSERT INTO `cc_operation` VALUES (25, '2023-07-11 14:57:06', 1, 8, 21, '新订', 13, 4449.19);
INSERT INTO `cc_operation` VALUES (26, '2023-07-11 15:15:36', 1, 8, 22, '新订', 550, 420260.50);
INSERT INTO `cc_operation` VALUES (27, '2023-07-11 17:13:57', 1, 8, 21, '新订', 6, 3615.09);
INSERT INTO `cc_operation` VALUES (28, '2023-07-11 20:11:47', 1, 8, 21, '新订', 6, 3615.09);
INSERT INTO `cc_operation` VALUES (29, '2023-07-11 20:11:47', 1, 8, 22, '新订', 550, 420260.50);
INSERT INTO `cc_operation` VALUES (30, '2023-07-11 20:11:47', 1, 8, 21, '新订', 13, 4449.19);
INSERT INTO `cc_operation` VALUES (31, '2023-07-11 22:28:34', 1, 8, 22, '新订', 6, 3615.09);
INSERT INTO `cc_operation` VALUES (32, '2023-07-12 09:45:51', 1, 8, 22, '新订', 6, 3615.09);
INSERT INTO `cc_operation` VALUES (33, '2023-07-13 10:38:18', 1, 14, 21, '新订', 30, 5510.80);
INSERT INTO `cc_operation` VALUES (34, '2023-07-13 10:41:42', 1, 14, 21, '新订', 30, 5510.80);
INSERT INTO `cc_operation` VALUES (35, '2023-07-13 10:42:52', 1, 14, 22, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (36, '2023-07-13 10:45:09', 1, 14, 23, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (37, '2023-07-13 10:49:16', 1, 14, 24, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (38, '2023-07-13 10:49:25', 1, 14, 25, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (39, '2023-07-13 10:50:07', 1, 14, 26, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (40, '2023-07-13 10:50:18', 1, 14, 27, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (41, '2023-07-13 10:55:14', 1, 14, 28, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (42, '2023-07-13 10:57:44', 1, 14, 29, '新订', 500, 66515.00);
INSERT INTO `cc_operation` VALUES (43, '2023-07-13 10:59:26', 1, 14, 30, '新订', 20, 1360.80);
INSERT INTO `cc_operation` VALUES (44, '2023-07-13 11:09:31', 1, 14, 31, '新订', 20, 1360.80);
INSERT INTO `cc_operation` VALUES (45, '2023-07-13 13:02:33', 1, 14, 26, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (46, '2023-07-13 13:02:33', 1, 14, 23, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (47, '2023-07-13 13:02:33', 1, 14, 24, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (48, '2023-07-13 13:02:33', 1, 14, 27, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (49, '2023-07-13 13:02:33', 1, 14, 25, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (50, '2023-07-13 13:02:33', 1, 14, 29, '新订', 500, 66515.00);
INSERT INTO `cc_operation` VALUES (51, '2023-07-13 13:02:33', 1, 14, 22, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (52, '2023-07-13 13:02:33', 1, 14, 31, '新订', 20, 1360.80);
INSERT INTO `cc_operation` VALUES (53, '2023-07-13 13:02:33', 1, 14, 28, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (54, '2023-07-13 13:02:33', 1, 14, 30, '新订', 20, 1360.80);
INSERT INTO `cc_operation` VALUES (55, '2023-07-13 19:39:57', 1, 14, 29, '新订', 500, 66515.00);
INSERT INTO `cc_operation` VALUES (56, '2023-07-13 19:39:57', 1, 14, 23, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (57, '2023-07-13 19:39:57', 1, 14, 28, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (58, '2023-07-13 19:39:57', 1, 14, 26, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (59, '2023-07-13 19:39:57', 1, 14, 25, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (60, '2023-07-13 19:39:57', 1, 14, 27, '新订', 30, 42670.80);
INSERT INTO `cc_operation` VALUES (61, '2023-07-13 19:39:57', 1, 14, 22, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (62, '2023-07-13 19:39:57', 1, 14, 31, '新订', 20, 1360.80);
INSERT INTO `cc_operation` VALUES (63, '2023-07-13 19:39:57', 1, 14, 21, '新订', 30, 5510.80);
INSERT INTO `cc_operation` VALUES (64, '2023-07-13 19:39:57', 1, 14, 30, '新订', 20, 1360.80);
INSERT INTO `cc_operation` VALUES (65, '2023-07-13 19:39:57', 1, 14, 24, '新订', 30, 37520.80);
INSERT INTO `cc_operation` VALUES (66, '2023-07-13 20:44:40', 1, 8, 23, '新订', 20, 8070.50);
INSERT INTO `cc_operation` VALUES (67, '2023-07-13 20:46:16', 1, 8, 24, '退货', 2, 1205.03);
INSERT INTO `cc_operation` VALUES (68, '2023-07-13 20:52:52', 1, 8, 25, '新订', 2, 1205.03);

-- ----------------------------
-- Table structure for cc_order
-- ----------------------------
DROP TABLE IF EXISTS `cc_order`;
CREATE TABLE `cc_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `order_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单类型',
  `deadline` date NULL DEFAULT NULL COMMENT '要求完成日期',
  `numbers` int(20) NULL DEFAULT NULL COMMENT '涉及的商品数量',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `total_amount` double(20, 2) NULL DEFAULT NULL COMMENT '涉及的金额',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员工ID',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cc_order
-- ----------------------------
INSERT INTO `cc_order` VALUES (5, 8, '2023-07-09 16:22:47', '新订', '2023-07-14', 5, '已调度', 292.00, 1, 0);
INSERT INTO `cc_order` VALUES (6, 8, '2023-07-09 16:41:08', '新订', '2023-06-28', 3, '已调度', 159.00, 1, 0);
INSERT INTO `cc_order` VALUES (17, 8, '2023-07-09 17:09:29', '退订', NULL, 2, '无效', 146.00, 1, 0);
INSERT INTO `cc_order` VALUES (18, 8, '2023-07-10 13:28:43', '新订', '2023-07-08', 6, '已调度', 3615.09, 1, 0);
INSERT INTO `cc_order` VALUES (19, 8, '2023-07-10 13:51:24', '新订', '2023-07-10', 6, '已调度', 438.21, 1, 0);
INSERT INTO `cc_order` VALUES (20, 8, '2023-07-10 19:49:09', '新订', '2023-07-10', 4, '已分配', 2410.06, 1, 0);
INSERT INTO `cc_order` VALUES (21, 8, '2023-07-11 17:13:56', '新订', '2023-07-14', 6, '部分完成', 3615.09, 1, 0);
INSERT INTO `cc_order` VALUES (22, 8, '2023-07-11 22:28:34', '新订', '2023-07-13', 6, '配送站到货', 3615.09, 1, 0);
INSERT INTO `cc_order` VALUES (23, 8, '2023-07-13 20:44:40', '新订', '2023-07-15', 20, '已调度', 8070.50, 1, 0);
INSERT INTO `cc_order` VALUES (24, 8, '2023-07-10 19:49:09', '退货', '2023-07-10', 2, '可分配', 1205.03, 1, 0);
INSERT INTO `cc_order` VALUES (25, 8, '2023-07-13 20:52:52', '新订', '2023-07-29', 2, '已调度', 1205.03, 1, 0);

-- ----------------------------
-- Table structure for cc_product
-- ----------------------------
DROP TABLE IF EXISTS `cc_product`;
CREATE TABLE `cc_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_id` bigint(20) NOT NULL COMMENT '对应的订单ID号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `number` int(10) NULL DEFAULT NULL COMMENT '数量',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `product_categary` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品大类',
  `islack` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否缺货',
  `refund_able` tinyint(1) NULL DEFAULT NULL COMMENT '是否可以退货',
  `change_able` tinyint(1) NULL DEFAULT NULL COMMENT '是否可以换货',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cc_product
-- ----------------------------
INSERT INTO `cc_product` VALUES (1, 1, 3, '手机', 12, 20.30, '电子产品', 1, 1, 1);
INSERT INTO `cc_product` VALUES (2, 1, 4, '电脑', 12, 20.30, '电子产品', 1, 1, 1);
INSERT INTO `cc_product` VALUES (9, 5, 1, 'dddd', 2, 133.00, '华为手机', 1, 0, 0);
INSERT INTO `cc_product` VALUES (10, 5, 2, 'sss', 2, 13.00, '华为手机', 1, 1, 1);
INSERT INTO `cc_product` VALUES (11, 6, 1, 'dddd', 1, 133.00, '华为手机', 1, 0, 0);
INSERT INTO `cc_product` VALUES (12, 6, 2, 'sss', 2, 13.00, '华为手机', 1, 1, 1);
INSERT INTO `cc_product` VALUES (31, 17, 1, 'dddd', 1, 133.00, '华为手机', NULL, NULL, NULL);
INSERT INTO `cc_product` VALUES (32, 17, 2, 'sss', 1, 13.00, '华为手机', NULL, NULL, NULL);
INSERT INTO `cc_product` VALUES (33, 18, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 3, 405.01, '电脑', 1, 1, 1);
INSERT INTO `cc_product` VALUES (34, 18, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 3, 800.02, '玩具', 1, 1, 1);
INSERT INTO `cc_product` VALUES (35, 19, 1, 'dddd', 3, 133.03, '手机', NULL, 0, 0);
INSERT INTO `cc_product` VALUES (36, 19, 2, 'sss', 3, 13.04, '手机', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (37, 20, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 2, 405.01, '电脑', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (38, 20, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 2, 800.02, '玩具', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (39, 21, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 3, 405.01, '电脑', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (40, 21, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 3, 800.02, '玩具', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (41, 22, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 3, 405.01, '电脑', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (42, 22, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 3, 800.02, '玩具', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (43, 23, 6, '华为智选 鼎桥 TDTech M40 5G手机 8GB+256GB', 10, 402.04, '手机', NULL, 0, 0);
INSERT INTO `cc_product` VALUES (44, 23, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 10, 405.01, '电脑', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (45, 24, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 1, 405.01, NULL, NULL, 1, 1);
INSERT INTO `cc_product` VALUES (46, 24, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 1, 800.02, NULL, NULL, 1, 1);
INSERT INTO `cc_product` VALUES (47, 25, 7, 'Lenovo联想电脑音响蓝牙音箱台式机笔记本手机通用家用低音炮超重低音长条多媒体迷你有线', 1, 405.01, '电脑', NULL, 1, 1);
INSERT INTO `cc_product` VALUES (48, 25, 9, 'LEGO乐高积木玩具 超级赛车系列 76916 保时捷 963 男孩女孩生日礼物', 1, 800.02, '玩具', NULL, 1, 1);

-- ----------------------------
-- Table structure for cc_refund
-- ----------------------------
DROP TABLE IF EXISTS `cc_refund`;
CREATE TABLE `cc_refund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对应的order表记录ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '操作订单ID',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因',
  `operation_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型(退货，换货，退订)',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否删除',
  `substation_id` bigint(20) NULL DEFAULT NULL COMMENT '子站ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cc_refund
-- ----------------------------
INSERT INTO `cc_refund` VALUES (17, 6, '', '退订', NULL, NULL);
INSERT INTO `cc_refund` VALUES (24, 20, '', '退货', NULL, 7);

-- ----------------------------
-- Table structure for cc_stockout
-- ----------------------------
DROP TABLE IF EXISTS `cc_stockout`;
CREATE TABLE `cc_stockout`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缺货记录ID',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '缺货订单号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '缺货商品号',
  `need_numbers` int(10) NULL DEFAULT NULL COMMENT '缺货商品数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建客服',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺货状态',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cc_stockout
-- ----------------------------
INSERT INTO `cc_stockout` VALUES (1, 3, 5, 2, '2023-07-08 17:05:01', 1, '已到货', 0);
INSERT INTO `cc_stockout` VALUES (2, 3, 4, 1, '2023-07-08 17:05:01', 1, '未提交', 1);
INSERT INTO `cc_stockout` VALUES (3, 4, 1, 2, '2023-07-09 15:44:32', 1, '未提交', 1);
INSERT INTO `cc_stockout` VALUES (4, 4, 2, 2, '2023-07-09 15:44:32', 1, '未提交', 1);
INSERT INTO `cc_stockout` VALUES (5, 5, 1, 2, '2023-07-09 16:22:47', 1, '已到货', 0);
INSERT INTO `cc_stockout` VALUES (6, 5, 2, 2, '2023-07-09 16:22:47', 1, '已到货', 0);
INSERT INTO `cc_stockout` VALUES (7, 6, 1, 2, '2023-07-09 16:41:08', 1, '已到货', 0);
INSERT INTO `cc_stockout` VALUES (8, 6, 2, 3, '2023-07-09 16:41:08', 1, '已到货', 0);
INSERT INTO `cc_stockout` VALUES (9, 6, 1, 1, '2023-07-09 17:09:30', NULL, '未提交', 1);
INSERT INTO `cc_stockout` VALUES (10, 6, 2, 2, '2023-07-09 17:09:30', NULL, '未提交', 1);
INSERT INTO `cc_stockout` VALUES (11, 18, 7, 3, '2023-07-10 13:28:44', 1, '已到货', 0);
INSERT INTO `cc_stockout` VALUES (12, 18, 9, 3, '2023-07-10 13:28:44', 1, '已到货', 0);

SET FOREIGN_KEY_CHECKS = 1;
