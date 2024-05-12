# 数据库初始化


-- 创建库
create database if not exists xiapi_db;

-- 切换库
use xiapi_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    accessKey  varchar(512)                           null comment 'ak',
    secretKey  varchar(512)                           null comment 'sk',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 接口表
create table if not exists interface_info
(
    id           bigint auto_increment comment 'id' primary key,
    name         varchar(256)     not null  comment '接口名称',
    description  varcher(256)       comment '接口描述',
    host         varcher(512)       not null comment '接口地址主机',
    url         varcher(256)       not null comment '接口地址路径',
    requestParams text not null comment '请求参数',
    requestHeader text null comment '请求头',
    responseHeader text null comment '响应头',
    status int default 0 not null comment '接口状态（0-关闭，1-开启）',
    method varchar(256) not null comment '请求类型',
    userId bigint not null comment '创建人',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
    ) comment '接口信息' collate = utf8mb4_unicode_ci;

-- 用户调用接口关系表
create table if not exists user_interface_info
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';

-- 接口产品表
create table if not exists interface_info_product
(
    id           bigint auto_increment comment 'id' primary key,
    interfaceInfoId bigint not null comment '接口 id',
    specification  int    not null comment '规格',
    price         int     not null comment '价格',
    stock         int     not null default 0 comment '库存',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '接口商品' collate = utf8mb4_unicode_ci;

-- 订单表
create table if not exists interface_info_order
(
    id           bigint auto_increment comment 'id' primary key,
    interfaceInfoId bigint not null comment '接口id',
    userId bigint not null comment '用户id',
    interfaceInfoProductId  bigint    not null comment '产品id',
    price         int     not null comment '价格',
    status        int     not null default 1 comment '订单状态，1：未支付；2：已支付；3：已取消；4：退款中；6：已退款',
    payTime      datetime     comment '支付时间',
    refundTime   datetime     comment '退款时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '接口商品订单' collate = utf8mb4_unicode_ci;