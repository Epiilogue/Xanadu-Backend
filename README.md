# 后端代码仓库

## 简介

本仓库包含了东北大学软件学院Xanadu小组在东软教育科技集团实训项目中的后端代码。

该项目采用微服务架构,包含6个服务模块,分别是:

- 客户服务中心模块:管理客户和订单信息
- 运输管理中心模块:管理商品和供应商信息
- 调度中心模块:管理订单调度
- 仓库模块:管理中心仓库和分站仓库
- 分站模块:管理分站、快递员、任务单等信息
- 财务模块:管理财务和发票信息

## 技术栈

后端主要采用以下技术:

- Spring Boot:简化Spring应用开发
- Spring Cloud:微服务框架
- Spring Cloud Alibaba:集成阿里巴巴微服务组件
- MyBatis Plus:持久层框架
- Nacos:服务注册和配置中心
- Sentinel:服务容错保护
- RocketMQ:消息队列
- Canal:数据库日志工具
- Redis:缓存
- Elasticsearch:搜索引擎
- Docker:容器化部署

## 模块介绍

### 客户服务中心

- 实现新建订单、退货、换货等功能
- 生成商品缺货记录,统计客服工作量
- 集成RocketMQ进行异步处理

### 运输管理中心

- 管理商品和供应商信息
- 与供应商交互,采购缺货商品
- 集成阿里云OSS存储商品图片
- 使用Canal同步商品信息变更到Elasticsearch

### 调度中心

- 检查订单缺货状态,调度订单
- 实现订单的自动调度
- 生成和管理任务单

### 仓库模块

- 实现中心仓库和分站仓库的库存管理
- 对库存操作加分布式锁保证数据一致性
- 使用Canal同步商品信息变更到仓库

### 分站模块

- 管理分站、快递员和任务单
- 生成打印配送单,统计分站数据
- 提供小程序接口

### 财务模块

- 使用Quartz实现每日数据统计
- 为分站生成结算报表


## 环境搭建

- JDK 8+
- Maven
- Docker

## 编译与运行

```bash
# 构建Docker镜像
docker build -t xanadu-image .

# 启动所有服务  
docker-compose up -d
```

## 项目地址
代码仓库:https://github.com/Epiilogue/Xanadu-Backend

## 贡献指南
欢迎提交Pull Request一起完善项目。

## 版权
该项目签署了MIT 授权许可,详情请参阅 LICENSE.txt。

## 致谢
感谢东软教育科技集团实训提供技术支持。