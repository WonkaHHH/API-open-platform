
use xiapi_db;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for interface_info
-- ----------------------------
DROP TABLE IF EXISTS `interface_info`;
CREATE TABLE `interface_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接口名称',
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接口描述',
  `host` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接口地址主机',
  `url` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接口地址路径',
  `requestParams` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求参数',
  `requestHeader` text COLLATE utf8mb4_unicode_ci COMMENT '请求头',
  `responseHeader` text COLLATE utf8mb4_unicode_ci COMMENT '响应头',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '接口状态（0-关闭，1-开启）',
  `method` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求类型',
  `userId` bigint(20) NOT NULL COMMENT '创建人',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口信息';

-- ----------------------------
-- Records of interface_info
-- ----------------------------
INSERT INTO `interface_info` VALUES ('1', 'GetUserNameByPost', '传入名字，返回名字，是不是很无聊', '127.0.01:8123', '/api/name/user', '{userName=\"\"}', '{}', '{}', '1', 'POST', '2', '2023-10-03 19:05:17', '2023-10-21 21:33:36', '0');
INSERT INTO `interface_info` VALUES ('2', 'GetAnswerByQuestion', '让你的程序轻松拥有AI能力', '127.0.0.1:8123', '/api/ai/getanswer', '{question=\"\"}', '{}', '{}', '1', 'GET', '2', '2023-10-13 09:18:21', '2023-10-17 14:11:32', '0');

-- ----------------------------
-- Table structure for interface_info_order
-- ----------------------------
DROP TABLE IF EXISTS `interface_info_order`;
CREATE TABLE `interface_info_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `interfaceInfoId` bigint(20) NOT NULL COMMENT '接口id',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `interfaceInfoProductId` bigint(20) NOT NULL COMMENT '商品id',
  `price` int(11) NOT NULL COMMENT '价格',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '订单状态，1：未支付；2：已支付；3：已取消；4：退款中；6：已退款',
  `payTime` datetime DEFAULT NULL COMMENT '支付时间',
  `refundTime` datetime DEFAULT NULL COMMENT '退款时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=913220724318294017 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口商品订单';

-- ----------------------------
-- Records of interface_info_order
-- ----------------------------
INSERT INTO `interface_info_order` VALUES ('911977791917150208', '1', '2', '1', '29999', '3', null, null, '2023-10-18 11:15:14', '2023-10-20 10:33:51', '1');
INSERT INTO `interface_info_order` VALUES ('912482384589639680', '1', '2', '1', '29999', '3', null, null, '2023-10-19 20:40:19', '2023-10-20 10:35:20', '0');
INSERT INTO `interface_info_order` VALUES ('912492780700585984', '1', '2', '1', '29999', '3', null, null, '2023-10-19 21:21:37', '2023-10-20 10:35:23', '0');
INSERT INTO `interface_info_order` VALUES ('912493008321269760', '1', '2', '9', '222', '3', null, null, '2023-10-19 21:22:32', '2023-10-20 10:35:26', '0');
INSERT INTO `interface_info_order` VALUES ('912493690705170432', '2', '2', '4', '100', '3', null, null, '2023-10-19 21:25:14', '2023-10-20 10:35:30', '0');
INSERT INTO `interface_info_order` VALUES ('912494164451807232', '2', '2', '4', '100', '2', '2023-10-20 11:30:50', null, '2023-10-19 21:27:07', '2023-10-20 11:30:49', '0');
INSERT INTO `interface_info_order` VALUES ('912494345620574208', '2', '2', '4', '100', '2', '2023-10-20 11:31:56', null, '2023-10-19 21:27:50', '2023-10-20 11:31:56', '0');
INSERT INTO `interface_info_order` VALUES ('912495021725601792', '2', '2', '5', '200', '1', null, null, '2023-10-19 21:30:32', '2023-10-19 21:30:32', '0');
INSERT INTO `interface_info_order` VALUES ('912495375150239744', '2', '2', '6', '29999', '1', null, null, '2023-10-19 21:31:56', '2023-10-19 21:31:56', '0');
INSERT INTO `interface_info_order` VALUES ('912496068904898560', '2', '2', '5', '200', '1', null, null, '2023-10-19 21:34:41', '2023-10-19 21:34:41', '0');
INSERT INTO `interface_info_order` VALUES ('912496356483158016', '2', '2', '4', '100', '1', null, null, '2023-10-19 21:35:50', '2023-10-19 21:35:50', '0');
INSERT INTO `interface_info_order` VALUES ('912496387495841792', '2', '2', '6', '29999', '1', null, null, '2023-10-19 21:35:57', '2023-10-19 21:35:57', '0');
INSERT INTO `interface_info_order` VALUES ('912496485617389568', '2', '2', '6', '29999', '1', null, null, '2023-10-19 21:36:21', '2023-10-19 21:36:21', '0');
INSERT INTO `interface_info_order` VALUES ('912496986043994112', '2', '2', '7', '45', '1', null, null, '2023-10-19 21:38:20', '2023-10-19 21:38:20', '0');
INSERT INTO `interface_info_order` VALUES ('912497010526146560', '2', '2', '5', '200', '1', null, null, '2023-10-19 21:38:26', '2023-10-19 21:38:26', '0');
INSERT INTO `interface_info_order` VALUES ('912497350252187648', '2', '2', '4', '100', '1', null, null, '2023-10-19 21:39:47', '2023-10-19 21:39:47', '0');
INSERT INTO `interface_info_order` VALUES ('912497568909643776', '2', '2', '5', '200', '1', null, null, '2023-10-19 21:40:39', '2023-10-19 21:40:39', '0');
INSERT INTO `interface_info_order` VALUES ('912497749499596800', '2', '2', '5', '200', '1', null, null, '2023-10-19 21:41:22', '2023-10-19 21:41:22', '0');
INSERT INTO `interface_info_order` VALUES ('912498415638958080', '1', '2', '2', '21', '1', null, null, '2023-10-19 21:44:01', '2023-10-19 21:44:01', '0');
INSERT INTO `interface_info_order` VALUES ('912498846838575104', '1', '2', '3', '30', '2', null, null, '2023-10-19 21:45:44', '2023-10-19 21:45:45', '0');
INSERT INTO `interface_info_order` VALUES ('912499031933210624', '1', '2', '3', '30', '2', null, null, '2023-10-19 21:46:28', '2023-10-19 21:46:29', '0');
INSERT INTO `interface_info_order` VALUES ('912499901114634240', '2', '2', '5', '200', '2', null, null, '2023-10-19 21:49:55', '2023-10-19 21:49:56', '0');
INSERT INTO `interface_info_order` VALUES ('912500074729459712', '2', '2', '5', '200', '2', null, null, '2023-10-19 21:50:36', '2023-10-19 21:50:37', '0');
INSERT INTO `interface_info_order` VALUES ('912500237346820096', '1', '2', '2', '21', '2', null, null, '2023-10-19 21:51:15', '2023-10-19 21:51:18', '0');
INSERT INTO `interface_info_order` VALUES ('912500842941403136', '1', '2', '9', '222', '2', null, null, '2023-10-19 21:53:40', '2023-10-19 21:53:41', '0');
INSERT INTO `interface_info_order` VALUES ('913220724318294016', '1', '2', '1', '29999', '2', '2023-10-21 21:34:15', null, '2023-10-21 21:34:13', '2023-10-21 21:34:15', '0');

-- ----------------------------
-- Table structure for interface_info_product
-- ----------------------------
DROP TABLE IF EXISTS `interface_info_product`;
CREATE TABLE `interface_info_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `interfaceInfoId` bigint(20) NOT NULL COMMENT '接口 id',
  `specification` int(11) NOT NULL COMMENT '规格',
  `price` int(11) NOT NULL COMMENT '价格',
  `stock` int(20) NOT NULL DEFAULT '0' COMMENT '库存',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口商品';

-- ----------------------------
-- Records of interface_info_product
-- ----------------------------
INSERT INTO `interface_info_product` VALUES ('1', '1', '99992', '29999', '7', '2023-10-17 16:38:15', '2023-10-21 21:34:13', '0');
INSERT INTO `interface_info_product` VALUES ('2', '1', '200', '21', '8', '2023-10-17 16:38:31', '2023-10-19 21:51:15', '0');
INSERT INTO `interface_info_product` VALUES ('3', '1', '300', '30', '8', '2023-10-17 16:38:44', '2023-10-19 21:46:28', '0');
INSERT INTO `interface_info_product` VALUES ('4', '2', '100', '100', '5', '2023-10-17 16:38:57', '2023-10-19 21:39:47', '0');
INSERT INTO `interface_info_product` VALUES ('5', '2', '200', '200', '3', '2023-10-17 16:39:08', '2023-10-19 21:50:36', '0');
INSERT INTO `interface_info_product` VALUES ('6', '2', '20000', '29999', '7', '2023-10-17 16:59:35', '2023-10-19 21:36:21', '0');
INSERT INTO `interface_info_product` VALUES ('7', '2', '45', '45', '9', '2023-10-17 21:56:48', '2023-10-19 21:38:20', '0');
INSERT INTO `interface_info_product` VALUES ('8', '1', '5555', '55555', '10', '2023-10-17 21:57:02', '2023-10-19 21:25:07', '1');
INSERT INTO `interface_info_product` VALUES ('9', '1', '222', '222', '220', '2023-10-18 16:36:52', '2023-10-19 21:53:40', '0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `accessKey` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ak',
  `secretKey` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'sk',
  `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2', 'xiaoxi', '3769b7e2b5556c7e1e00049d500382d4', null, null, '小夕', 'http://127.0.0.1:8101/api/user_avatar/2/cQdF01J6-1-2c5d6cfd26d4f0c8016c531c8e9c2.jpg', '吃吃吃，睡睡睡，codecodecode', '665430b603fd5e0f20b6827fdbdb030e', 'ab2e23c4e9b4cbf05dc39d0439d0a79b', 'admin', '2023-09-27 09:00:35', '2023-10-21 21:36:02', '0');
INSERT INTO `user` VALUES ('3', 'xiaoxiya', '3769b7e2b5556c7e1e00049d500382d4', null, null, '小夕吖', 'http://127.0.0.1:8101/api/user_avatar/3/IuSzHimd-ikun.png', '小夕吃吃吃', '908f44fc2f332461fa7cdaf6a62e53b0', 'f766352473baf4a97dde85623f9c45cf', 'user', '2023-10-20 22:00:12', '2023-10-20 22:04:45', '0');

-- ----------------------------
-- Table structure for user_interface_info
-- ----------------------------
DROP TABLE IF EXISTS `user_interface_info`;
CREATE TABLE `user_interface_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '调用用户 id',
  `interfaceInfoId` bigint(20) NOT NULL COMMENT '接口 id',
  `totalNum` int(11) NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `leftNum` int(11) NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-正常，1-禁用',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户调用接口关系';

-- ----------------------------
-- Records of user_interface_info
-- ----------------------------
INSERT INTO `user_interface_info` VALUES ('1', '2', '1', '30', '102006', '0', '2023-10-10 16:25:25', '2023-10-22 18:38:40', '0');
INSERT INTO `user_interface_info` VALUES ('2', '2', '2', '40', '684', '0', '2023-10-13 09:20:57', '2023-10-22 19:44:56', '0');
