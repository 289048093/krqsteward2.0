-- ----------------------------
-- Table structure for `t_member_info_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_member_info_log`;
CREATE TABLE `t_member_info_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `order_log_type` varchar(15) NOT NULL COMMENT '日志类型',
  `is_offline` tinyint(1)  NULL COMMENT '是否线下订单',
  `platform_order_no` varchar(20) DEFAULT NULL COMMENT '外部平台编号',
  `refund_id` int(11) DEFAULT NULL,
  `platform_type` varchar(15) DEFAULT NULL COMMENT '外部平台类型',
  `buyer_id` varchar(128) DEFAULT NULL COMMENT '买家ID',
  `payment_fee` bigint(20) DEFAULT NULL  COMMENT '预收款',
  `actual_fee` bigint(20) DEFAULT NULL  COMMENT '实付金额',
  `refund_payment_fee` bigint(20) DEFAULT NULL COMMENT '订单预收款退款',
  `refund_order_fee` bigint(20) DEFAULT NULL COMMENT '订单退款',
  `receiver_name` varchar(128) DEFAULT NULL COMMENT '收货人',
  `receiver_phone` varchar(128) DEFAULT NULL COMMENT '收货电话',
  `receiver_mobile` varchar(128) DEFAULT NULL COMMENT '收货手机',
  `receiver_zip` varchar(128) DEFAULT NULL COMMENT '收货邮编',
  `receiver_state` varchar(128) DEFAULT NULL COMMENT '收货省',
  `receiver_city` varchar(128) DEFAULT NULL COMMENT '收货市',
  `receiver_district` varchar(128) DEFAULT NULL COMMENT '收货区',
  `receiver_address` varchar(128) DEFAULT NULL COMMENT '收货地址',
  `shipping_no` varchar(60) DEFAULT NULL COMMENT '快递编号',
  `shipping_comp` varchar(60) DEFAULT NULL COMMENT '快递公司',
  `buy_count` int(11) DEFAULT NULL COMMENT '购买次数',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
   `processed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否处理过',
   `shop_id` int(11)  NULL  COMMENT '店铺id',
  PRIMARY KEY (`id`)
) ;
