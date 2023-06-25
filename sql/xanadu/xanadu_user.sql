/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.7.28-log : Database - xanadu_user
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xanadu_user` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `xanadu_user`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `nickname` varchar(255) NOT NULL COMMENT '昵称',
  `headimage` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(255) NOT NULL COMMENT '手机号',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `job` varchar(255) NOT NULL COMMENT '职务',
  `auth` varchar(255) NOT NULL DEFAULT '1' COMMENT '权限',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`nickname`,`headimage`,`phone`,`email`,`job`,`auth`,`createtime`) values 
(1,'admin','admin','super-admin',NULL,'15866668888','20208888@neu.edu.cn','Super-Admin','0','2023-06-25 11:49:27'),
(2,'xiaoMing','xiaoMing','Mr.MIng',NULL,'16966668888','20206699@neu.edu.cn','Courier','4','2023-06-23 11:51:13');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
