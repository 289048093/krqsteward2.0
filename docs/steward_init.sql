insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('天猫',50,'http://detail.tmall.com','TAO_BAO');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('淘宝',50,'http://item.taobao.com/item.htm','TAO_BAO_2');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('京东',50,'http://item.jd.com','JING_DONG');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('易居尚',50,'http://buy.ccb.com','EJS');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('天猫供应商',50,'http://buy.ccb.com','TM_GYS');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('建行商城',50,'http://buy.ccb.com','JIAN_HANG');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('QQ网购',50,'http://buy.ccb.com','QQ_WG');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('QQ团购',50,'http://buy.ccb.com','QQ_TG');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('微信',50,'http://buy.ccb.com','WEI_XIN');
insert into  t_platform(name,storage_percent,prod_link_prefix,type)values('微博',50,'http://buy.ccb.com','WEI_BO');

INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('易居尚平台','易居尚平台店铺','EJS','SHOP',now(),now());
INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('天猫供应商平台','天猫供应商平台店铺','TM_GYS','VENDOR',now(),now());
INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('建行商城平台','建行商城平台店铺','JIAN_HANG','SHOP',now(),now());
INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('QQ网购平台','QQ网购平台店铺','QQ_WG','SHOP',now(),now());
INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('QQ团购平台','QQ团购平台店铺','QQ_TG','SHOP',now(),now());
INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('微信平台','微信平台店铺','WEI_XIN','SHOP',now(),now());
INSERT INTO t_shop(nick,title,platform_type,shop_type,create_time,update_time) VALUES ('微博平台','微博平台店铺','WEI_BO','SHOP',now(),now());


insert into t_conf values(null,'next_supplier_no',3280,'商家编号',now(),now(), null);
insert into t_conf values(null,'next_order_no',111111,'订单编号',now(),now(), null);
insert into t_conf values(null,'next_meal_set_no',1,'套餐编号',now(),now(), null);
insert into t_conf values(null,'next_order_item_no',111111,'京东自生成订单项编号',now(),now(), null);


insert into t_conf values(null,'postage_product_sku', 'shunfeng008', '邮费补差商品的sku',now(),now(),null);
insert into t_conf values(null,'service_product_sku','9999', '服务补差商品的sku',now(),now(),null);

insert into t_conf values(null,'message_grab_interval', 60,'抓取消息的最小时间间隔,单位秒',now(),now(),null);
insert into t_conf values(null,'message_analyze_interval', 60,'分析消息的最小时间间隔,单位秒',now(),now(),null);
