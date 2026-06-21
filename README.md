# AI轻量级ERP系统（基础版）

基于 **Java 17 + Spring Boot 3.2 + Spring Cloud 2023** 的三层微服务架构，面向中小企业的轻量化ERP系统，覆盖采购、销售、库存、财务核心进销存业务闭环，并集成轻度AI赋能。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 基础框架 | Spring Boot | 3.2.0 |
| 微服务 | Spring Cloud Gateway + Nacos | 2023.0 / 2023.0.1 |
| ORM | MyBatis-Plus | 3.5.5 |
| 安全认证 | Spring Security + JWT (jjwt) | 0.12.3 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis + Redisson | 3.24.3 |
| 工具集 | Hutool | 5.8.24 |
| API文档 | Knife4j (Swagger) | 4.4.0 |
| Excel | EasyExcel | 3.3.3 |
| 构建 | Maven 多模块 | 3.8+ |

## 项目结构

```
tiny-erp/
├── pom.xml                     # 父工程，统一依赖管理
├── docs/sql/init.sql           # 数据库初始化脚本
│
├── erp-common/                 # 公共模块（实体基类、工具类、异常、安全）
├── erp-api/                    # Feign 远程调用接口
├── erp-gateway/                # API 网关（8080）
│
├── erp-auth/                   # 认证授权服务（8001）
├── erp-system/                 # 系统配置服务（8002）
├── erp-purchase/               # 采购管理服务（8003）
├── erp-sales/                  # 销售管理服务（8004）
├── erp-inventory/              # 库存管理服务（8005）
├── erp-finance/                # 财务管理服务（8006）
└── erp-ai/                     # AI智能服务（8007）
```

## 功能模块

### 模块一：基础中台与权限体系
- 多账号创建与角色权限管理（管理员/销售/仓管/财务）
- 操作日志全程追溯（新增/修改/删除/导出/登录）
- BCrypt 密码加密 + JWT 无状态认证
- 系统可视化配置

### 模块二：核心业务进销存
- **采购管理**：供应商档案、采购订单、入库单、采购对账
- **销售管理**：客户档案、销售订单、出库单、回款记录
- **库存管理**：商品档案、实时库存、出入库、盘点、库存预警
- **财务联动**：应收/应付自动生成、收支流水、财务看板

### 模块三：轻量AI赋能
- 库存智能预警（低库存/积压自动提醒）
- 智能报表生成
- 单据OCR识别（预留接口，可对接百度/阿里/腾讯云OCR）
- 智能单据校验

### 模块四：终端与运维
- PC浏览器端 + 移动端H5适配
- SaaS云端架构
- 数据备份配置

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.x（可选，本地开发可跳过）
- Maven 3.8+

### 1. 初始化数据库

```bash
mysql -u root -p < docs/sql/init.sql
```

### 2. 启动依赖服务

```bash
# 启动 Redis（默认端口 6379）
# 启动 Nacos（默认端口 8848，可选）
```

### 3. 编译项目

```bash
cd tiny-erp
mvn clean install -DskipTests
```

### 4. 启动服务

按顺序启动各服务：

```bash
# 网关
mvn spring-boot:run -pl erp-gateway

# 认证服务
mvn spring-boot:run -pl erp-auth

# 其他业务服务
mvn spring-boot:run -pl erp-system
mvn spring-boot:run -pl erp-purchase
mvn spring-boot:run -pl erp-sales
mvn spring-boot:run -pl erp-inventory
mvn spring-boot:run -pl erp-finance
mvn spring-boot:run -pl erp-ai
```

### 5. 访问

- API 网关：`http://localhost:8080`
- API 文档：`http://localhost:8080/doc.html`
- 默认管理员：`admin / 123456`

## 数据库表

| 表名 | 说明 |
|------|------|
| sys_user | 系统用户 |
| sys_role | 系统角色 |
| sys_user_role | 用户角色关联 |
| sys_permission | 权限 |
| sys_role_permission | 角色权限关联 |
| sys_operation_log | 操作日志 |
| sys_config | 系统配置 |
| biz_supplier | 供应商档案 |
| biz_customer | 客户档案 |
| biz_product | 商品档案 |
| biz_purchase_order | 采购订单 |
| biz_purchase_order_item | 采购订单明细 |
| biz_purchase_receipt | 采购入库单 |
| biz_purchase_receipt_item | 采购入库单明细 |
| biz_sales_order | 销售订单 |
| biz_sales_order_item | 销售订单明细 |
| biz_sales_delivery | 销售出库单 |
| biz_sales_delivery_item | 销售出库单明细 |
| biz_inventory | 实时库存 |
| biz_inventory_log | 库存变动记录 |
| biz_stock_check | 库存盘点单 |
| biz_stock_check_item | 盘点单明细 |
| fin_receivable | 应收记录 |
| fin_payable | 应付记录 |
| fin_transaction | 收支记录 |

## API 接口概览

| 服务 | 前缀 | 主要接口 |
|------|------|----------|
| erp-auth | `/api/auth` | 登录、获取用户信息 |
| erp-system | `/api/system` | 系统配置CRUD |
| erp-purchase | `/api/purchase` | 供应商、采购订单、入库单 |
| erp-sales | `/api/sales` | 客户、销售订单、出库单 |
| erp-inventory | `/api/inventory` | 商品、库存查询、出入库、盘点 |
| erp-finance | `/api/finance` | 应收、应付、收支、财务看板 |
| erp-ai | `/api/ai` | 库存预警、智能报表、OCR识别 |

## 配置文件

各服务配置文件位于 `src/main/resources/application.yml`，主要配置项：

```yaml
server:
  port: 8001                          # 各服务端口不同

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/tiny_erp
    username: root
    password: root
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848   # Nacos 地址
```

## 联系方式：
WX: wjianwei666
QQ：294223811
Email：support@crosskit.top

## 如果觉得本项目对您有任何一点帮助，请点右上角 "Star" 支持一下， 并向您的基友、同事们宣传一下吧，谢谢！
