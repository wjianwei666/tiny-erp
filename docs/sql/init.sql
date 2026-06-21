-- =============================================
-- AI轻量级ERP系统（基础版）数据库初始化脚本
-- Database: MySQL 8.0+
-- =============================================

CREATE DATABASE IF NOT EXISTS tiny_erp DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tiny_erp;

-- =============================================
-- 模块一：基础中台与权限体系
-- =============================================

-- 系统用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(200) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：1已删除 0未删除',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 系统角色表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码：ADMIN/SALES/WAREHOUSE/FINANCE',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 用户角色关联表
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 系统权限表
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY COMMENT '权限ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    permission_type VARCHAR(20) COMMENT '权限类型：MENU/BUTTON/API',
    path VARCHAR(200) COMMENT '路由路径',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_permission_code (permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

-- 角色权限关联表
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 操作日志表
DROP TABLE IF EXISTS sys_operation_log;
CREATE TABLE sys_operation_log (
    id BIGINT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT COMMENT '操作人ID',
    username VARCHAR(50) COMMENT '操作人用户名',
    module VARCHAR(50) COMMENT '操作模块',
    operation VARCHAR(50) COMMENT '操作类型：ADD/UPDATE/DELETE/EXPORT/LOGIN',
    description VARCHAR(500) COMMENT '操作描述',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_url VARCHAR(200) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    cost_time BIGINT COMMENT '耗时(ms)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user_id (user_id),
    INDEX idx_module (module),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 系统配置表
DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config (
    id BIGINT PRIMARY KEY COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(50) COMMENT '配置类型',
    description VARCHAR(200) COMMENT '配置描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- =============================================
-- 模块二：核心业务进销存
-- =============================================

-- 供应商档案表
DROP TABLE IF EXISTS biz_supplier;
CREATE TABLE biz_supplier (
    id BIGINT PRIMARY KEY COMMENT '供应商ID',
    supplier_code VARCHAR(50) COMMENT '供应商编码',
    supplier_name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    address VARCHAR(200) COMMENT '地址',
    bank_name VARCHAR(100) COMMENT '开户银行',
    bank_account VARCHAR(50) COMMENT '银行账号',
    tax_number VARCHAR(50) COMMENT '税号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_supplier_code (supplier_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商档案表';

-- 客户档案表
DROP TABLE IF EXISTS biz_customer;
CREATE TABLE biz_customer (
    id BIGINT PRIMARY KEY COMMENT '客户ID',
    customer_code VARCHAR(50) COMMENT '客户编码',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    address VARCHAR(200) COMMENT '地址',
    customer_type VARCHAR(20) COMMENT '客户类型：RETAIL/WHOLESALE/ECOMMERCE',
    credit_limit DECIMAL(15,2) DEFAULT 0 COMMENT '信用额度',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_customer_code (customer_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户档案表';

-- 商品档案表
DROP TABLE IF EXISTS biz_product;
CREATE TABLE biz_product (
    id BIGINT PRIMARY KEY COMMENT '商品ID',
    product_code VARCHAR(50) COMMENT '商品编码',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    category_id BIGINT COMMENT '商品分类ID',
    category_name VARCHAR(50) COMMENT '商品分类名称',
    unit VARCHAR(20) COMMENT '计量单位',
    spec VARCHAR(100) COMMENT '规格型号',
    purchase_price DECIMAL(15,2) COMMENT '采购参考价',
    sale_price DECIMAL(15,2) COMMENT '销售参考价',
    min_stock INT DEFAULT 0 COMMENT '最低库存预警阈值',
    max_stock INT DEFAULT 0 COMMENT '最高库存预警阈值',
    image_url VARCHAR(200) COMMENT '商品图片',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_product_code (product_code),
    INDEX idx_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品档案表';

-- 采购订单表
DROP TABLE IF EXISTS biz_purchase_order;
CREATE TABLE biz_purchase_order (
    id BIGINT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    supplier_name VARCHAR(100) COMMENT '供应商名称',
    order_date DATE NOT NULL COMMENT '订单日期',
    expect_arrive_date DATE COMMENT '预计到货日期',
    total_amount DECIMAL(15,2) DEFAULT 0 COMMENT '订单总金额',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT/PENDING/APPROVED/PART_RECEIVED/RECEIVED/CANCELLED',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- 采购订单明细表
DROP TABLE IF EXISTS biz_purchase_order_item;
CREATE TABLE biz_purchase_order_item (
    id BIGINT PRIMARY KEY COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_code VARCHAR(50) COMMENT '商品编码',
    product_name VARCHAR(200) COMMENT '商品名称',
    unit VARCHAR(20) COMMENT '单位',
    quantity DECIMAL(15,2) NOT NULL COMMENT '采购数量',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    total_price DECIMAL(15,2) NOT NULL COMMENT '总价',
    received_quantity DECIMAL(15,2) DEFAULT 0 COMMENT '已入库数量',
    sort_order INT DEFAULT 0 COMMENT '排序',
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';

-- 采购入库单表
DROP TABLE IF EXISTS biz_purchase_receipt;
CREATE TABLE biz_purchase_receipt (
    id BIGINT PRIMARY KEY COMMENT '入库单ID',
    receipt_no VARCHAR(50) NOT NULL COMMENT '入库单编号',
    order_id BIGINT NOT NULL COMMENT '关联采购订单ID',
    order_no VARCHAR(50) COMMENT '关联采购订单编号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    supplier_name VARCHAR(100) COMMENT '供应商名称',
    receipt_date DATE NOT NULL COMMENT '入库日期',
    total_amount DECIMAL(15,2) DEFAULT 0 COMMENT '入库总金额',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT/CONFIRMED/CANCELLED',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_receipt_no (receipt_no),
    INDEX idx_order_id (order_id),
    INDEX idx_supplier (supplier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购入库单表';

-- 采购入库单明细表
DROP TABLE IF EXISTS biz_purchase_receipt_item;
CREATE TABLE biz_purchase_receipt_item (
    id BIGINT PRIMARY KEY COMMENT '明细ID',
    receipt_id BIGINT NOT NULL COMMENT '入库单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_code VARCHAR(50) COMMENT '商品编码',
    product_name VARCHAR(200) COMMENT '商品名称',
    unit VARCHAR(20) COMMENT '单位',
    quantity DECIMAL(15,2) NOT NULL COMMENT '入库数量',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    total_price DECIMAL(15,2) NOT NULL COMMENT '总价',
    INDEX idx_receipt_id (receipt_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购入库单明细表';

-- 销售订单表
DROP TABLE IF EXISTS biz_sales_order;
CREATE TABLE biz_sales_order (
    id BIGINT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) COMMENT '客户名称',
    order_date DATE NOT NULL COMMENT '订单日期',
    expect_deliver_date DATE COMMENT '预计交货日期',
    total_amount DECIMAL(15,2) DEFAULT 0 COMMENT '订单总金额',
    discount_amount DECIMAL(15,2) DEFAULT 0 COMMENT '折扣金额',
    receivable_amount DECIMAL(15,2) DEFAULT 0 COMMENT '应收金额',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT/PENDING/APPROVED/PART_DELIVERED/DELIVERED/CANCELLED',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_customer (customer_id),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- 销售订单明细表
DROP TABLE IF EXISTS biz_sales_order_item;
CREATE TABLE biz_sales_order_item (
    id BIGINT PRIMARY KEY COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_code VARCHAR(50) COMMENT '商品编码',
    product_name VARCHAR(200) COMMENT '商品名称',
    unit VARCHAR(20) COMMENT '单位',
    quantity DECIMAL(15,2) NOT NULL COMMENT '销售数量',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    total_price DECIMAL(15,2) NOT NULL COMMENT '总价',
    delivered_quantity DECIMAL(15,2) DEFAULT 0 COMMENT '已出库数量',
    sort_order INT DEFAULT 0 COMMENT '排序',
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单明细表';

-- 销售出库单表
DROP TABLE IF EXISTS biz_sales_delivery;
CREATE TABLE biz_sales_delivery (
    id BIGINT PRIMARY KEY COMMENT '出库单ID',
    delivery_no VARCHAR(50) NOT NULL COMMENT '出库单编号',
    order_id BIGINT NOT NULL COMMENT '关联销售订单ID',
    order_no VARCHAR(50) COMMENT '关联销售订单编号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) COMMENT '客户名称',
    delivery_date DATE NOT NULL COMMENT '出库日期',
    total_amount DECIMAL(15,2) DEFAULT 0 COMMENT '出库总金额',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT/CONFIRMED/CANCELLED',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_delivery_no (delivery_no),
    INDEX idx_order_id (order_id),
    INDEX idx_customer (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售出库单表';

-- 销售出库单明细表
DROP TABLE IF EXISTS biz_sales_delivery_item;
CREATE TABLE biz_sales_delivery_item (
    id BIGINT PRIMARY KEY COMMENT '明细ID',
    delivery_id BIGINT NOT NULL COMMENT '出库单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_code VARCHAR(50) COMMENT '商品编码',
    product_name VARCHAR(200) COMMENT '商品名称',
    unit VARCHAR(20) COMMENT '单位',
    quantity DECIMAL(15,2) NOT NULL COMMENT '出库数量',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    total_price DECIMAL(15,2) NOT NULL COMMENT '总价',
    INDEX idx_delivery_id (delivery_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售出库单明细表';

-- 库存表（实时库存）
DROP TABLE IF EXISTS biz_inventory;
CREATE TABLE biz_inventory (
    id BIGINT PRIMARY KEY COMMENT '库存ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_code VARCHAR(50) COMMENT '商品编码',
    product_name VARCHAR(200) COMMENT '商品名称',
    warehouse_id BIGINT DEFAULT 0 COMMENT '仓库ID',
    warehouse_name VARCHAR(50) DEFAULT '默认仓库' COMMENT '仓库名称',
    quantity DECIMAL(15,2) DEFAULT 0 COMMENT '当前库存数量',
    locked_quantity DECIMAL(15,2) DEFAULT 0 COMMENT '锁定库存数量',
    available_quantity DECIMAL(15,2) DEFAULT 0 COMMENT '可用库存数量',
    total_cost DECIMAL(15,2) DEFAULT 0 COMMENT '库存总成本',
    last_purchase_price DECIMAL(15,2) COMMENT '最近采购价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_product_warehouse (product_id, warehouse_id),
    INDEX idx_quantity (quantity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 库存变动记录表
DROP TABLE IF EXISTS biz_inventory_log;
CREATE TABLE biz_inventory_log (
    id BIGINT PRIMARY KEY COMMENT '记录ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) COMMENT '商品名称',
    change_type VARCHAR(20) NOT NULL COMMENT '变动类型：PURCHASE_IN/SALES_OUT/STOCK_CHECK/ADJUST/TRANSFER',
    change_quantity DECIMAL(15,2) NOT NULL COMMENT '变动数量（正数入库，负数出库）',
    before_quantity DECIMAL(15,2) COMMENT '变动前数量',
    after_quantity DECIMAL(15,2) COMMENT '变动后数量',
    reference_no VARCHAR(50) COMMENT '关联单号',
    reference_type VARCHAR(20) COMMENT '关联单据类型',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    create_by BIGINT COMMENT '操作人',
    INDEX idx_product_id (product_id),
    INDEX idx_change_type (change_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动记录表';

-- 库存盘点单表
DROP TABLE IF EXISTS biz_stock_check;
CREATE TABLE biz_stock_check (
    id BIGINT PRIMARY KEY COMMENT '盘点单ID',
    check_no VARCHAR(50) NOT NULL COMMENT '盘点单编号',
    check_date DATE NOT NULL COMMENT '盘点日期',
    check_type VARCHAR(20) COMMENT '盘点类型：FULL/PARTIAL',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT/CHECKING/CONFIRMED',
    total_products INT DEFAULT 0 COMMENT '盘点商品总数',
    diff_products INT DEFAULT 0 COMMENT '差异商品数',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_check_no (check_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点单表';

-- 盘点单明细表
DROP TABLE IF EXISTS biz_stock_check_item;
CREATE TABLE biz_stock_check_item (
    id BIGINT PRIMARY KEY COMMENT '明细ID',
    check_id BIGINT NOT NULL COMMENT '盘点单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) COMMENT '商品名称',
    book_quantity DECIMAL(15,2) DEFAULT 0 COMMENT '账面数量',
    actual_quantity DECIMAL(15,2) COMMENT '实际盘点数量',
    diff_quantity DECIMAL(15,2) COMMENT '差异数量',
    diff_reason VARCHAR(200) COMMENT '差异原因',
    INDEX idx_check_id (check_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单明细表';

-- =============================================
-- 模块三：基础财务联动
-- =============================================

-- 应收记录表
DROP TABLE IF EXISTS fin_receivable;
CREATE TABLE fin_receivable (
    id BIGINT PRIMARY KEY COMMENT '应收ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) COMMENT '客户名称',
    order_id BIGINT COMMENT '关联销售订单ID',
    order_no VARCHAR(50) COMMENT '关联销售订单编号',
    amount DECIMAL(15,2) NOT NULL COMMENT '应收金额',
    received_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已收金额',
    balance_amount DECIMAL(15,2) COMMENT '余额',
    due_date DATE COMMENT '到期日期',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING/PART_PAID/PAID/OVERDUE',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer (customer_id),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收记录表';

-- 应付记录表
DROP TABLE IF EXISTS fin_payable;
CREATE TABLE fin_payable (
    id BIGINT PRIMARY KEY COMMENT '应付ID',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    supplier_name VARCHAR(100) COMMENT '供应商名称',
    order_id BIGINT COMMENT '关联采购订单ID',
    order_no VARCHAR(50) COMMENT '关联采购订单编号',
    amount DECIMAL(15,2) NOT NULL COMMENT '应付金额',
    paid_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已付金额',
    balance_amount DECIMAL(15,2) COMMENT '余额',
    due_date DATE COMMENT '到期日期',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING/PART_PAID/PAID/OVERDUE',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应付记录表';

-- 收支记录表
DROP TABLE IF EXISTS fin_transaction;
CREATE TABLE fin_transaction (
    id BIGINT PRIMARY KEY COMMENT '收支ID',
    transaction_no VARCHAR(50) NOT NULL COMMENT '交易编号',
    transaction_type VARCHAR(20) NOT NULL COMMENT '交易类型：INCOME/EXPENSE',
    amount DECIMAL(15,2) NOT NULL COMMENT '金额',
    related_type VARCHAR(20) COMMENT '关联类型：SALES/PURCHASE/OTHER',
    related_id BIGINT COMMENT '关联单据ID',
    related_no VARCHAR(50) COMMENT '关联单据编号',
    counterparty_name VARCHAR(100) COMMENT '对方名称',
    payment_method VARCHAR(20) COMMENT '支付方式：CASH/BANK/WECHAT/ALIPAY',
    transaction_date DATE NOT NULL COMMENT '交易日期',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_transaction_no (transaction_no),
    INDEX idx_type (transaction_type),
    INDEX idx_transaction_date (transaction_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收支记录表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入默认角色
INSERT INTO sys_role (id, role_name, role_code, description) VALUES
(1, '管理员', 'ADMIN', '系统管理员，拥有全部权限'),
(2, '销售人员', 'SALES', '销售岗位，管理客户、销售订单'),
(3, '仓管人员', 'WAREHOUSE', '仓库管理，负责库存、出入库'),
(4, '财务人员', 'FINANCE', '财务管理，负责应收应付、收支');

-- 插入默认管理员用户（密码：123456，BCrypt加密）
INSERT INTO sys_user (id, username, password, real_name, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 1);

-- 分配管理员角色
INSERT INTO sys_user_role (id, user_id, role_id) VALUES (1, 1, 1);

-- 插入系统配置
INSERT INTO sys_config (id, config_key, config_value, config_type, description) VALUES
(1, 'company_name', 'XX有限公司', 'SYSTEM', '企业名称'),
(2, 'company_address', '', 'SYSTEM', '企业地址'),
(3, 'stock_warn_enabled', 'true', 'INVENTORY', '库存预警开关'),
(4, 'auto_backup_enabled', 'true', 'SYSTEM', '自动备份开关'),
(5, 'backup_frequency', 'DAILY', 'SYSTEM', '备份频率');