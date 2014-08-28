drop table if exists t_shop_product;

/*==============================================================*/
/* Table: t_shop_product                                        */
/*==============================================================*/
create table t_shop_product
(
  id                   int not null auto_increment,
  shop_id              int,
  prod_id              int,
  price                bigint comment '一口价，吊牌价',
  discount_price       bigint comment '促销价',
  storage_percent      int comment '库存占比，如果设置将覆写掉t_platform的占比值',
  storage_num          int comment '店铺库存',
  is_putaway           tinyint comment '是否上架',
  syn_status           tinyint comment '同步状态',
  platform_url         varchar(100)binary comment '产品链接',
  auto_putaway         tinyint comment '是否自动上架',
  create_time          datetime,
  operator_id          int,
  primary key (id)
);
