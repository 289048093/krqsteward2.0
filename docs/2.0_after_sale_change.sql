drop table if exists t_after_sales;

drop table if exists t_after_sales_attachment;

drop table if exists t_after_sales_flowlog;

drop table if exists t_after_sales_item;

drop table if exists t_after_sales_patch;

drop table if exists t_after_sales_reason;

drop table if exists t_after_sales_refund;

drop table if exists t_after_sales_refund_alloc;

drop table if exists t_after_sales_refund_goods;

drop table if exists t_after_sales_send;

drop table if exists t_refund_alloc;

/*==============================================================*/
/* Table: t_after_sales                                         */
/*==============================================================*/
create table t_after_sales
(
   id                   int(11) not null auto_increment,
   code                 varchar(128) comment '售后工单编号',
   source               varchar(20) comment 'AfterSalesSource:售后来源;ORDER:普通订单,VISIT:回访单;',
   order_id             int(11) not null comment '订单ID',
   revisit_id           int(11) comment '回访单ID',
   service_user_id      int(11) comment '客服人员Id',
   service_user_name    varchar(128) comment '客服人员名称',
   reason_code          varchar(2000) comment '售后原因码',
   reason               text comment '售后原因',
   remark               text comment '售后备注',
   status               varchar(20) comment 'AfterSalesStatus:售后单状态;SAVE:处理中,CHECK:待审批,ACCEPT:审批通过,REJECT:审批驳回,FINISH:已结束,CANCEL:已作废;',
   status_before        varchar(20) comment '!AfterSalesStatus:修改前状态;',
   status_remark        varchar(2000) comment '最后一次状态流转原因',
   send                 tinyint(1) comment '是否发货',
   build_order          tinyint(1) comment '是否已生成发货单',
   brand_id             int(11) comment '品牌id',
   brand_name           varchar(50) not null comment '品牌名',
   platform_type        varchar(10) not null comment '!PlatformType:外部平台类型(天猫还是京东);',
   platform_order_no    varchar(100) comment '外部订单号',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales comment '售后工单表';

/*==============================================================*/
/* Table: t_after_sales_attachment                              */
/*==============================================================*/
create table t_after_sales_attachment
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '所属售后工单ID',
   name                 varchar(128) comment '附件名',
   path                 varchar(2000) comment '附件路径',
   type                 varchar(20) comment 'AttachmentType:附件类型;IMAGE:图片,DOC:word,EXL:excel,OTH:other;',
   operator_id          int(11) comment '操作人员ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   primary key (id)
);

alter table t_after_sales_attachment comment '售后附件表';

/*==============================================================*/
/* Table: t_after_sales_flowlog                                 */
/*==============================================================*/
create table t_after_sales_flowlog
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) not null comment '售后工单ID',
   status               varchar(20) not null comment '!AfterSalesStatus:当前状态（修改后状态）;',
   status_before        varchar(20) comment '!AfterSalesStatus:修改前状态;',
   remark               varchar(2000) comment '流程转向原因备注',
   operator_id          int(11) comment '操作人员ID',
   operator_name        varchar(128) comment '操作人员姓名',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   primary key (id)
);

alter table t_after_sales_flowlog comment '售后流程日志表
';

/*==============================================================*/
/* Table: t_after_sales_item                                    */
/*==============================================================*/
create table t_after_sales_item
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '售后工单ID',
   order_item_id        int(11) comment '订单项ID',
   type                 varchar(20) comment 'AfterSalesType:售后类型;REFUND:退款,SWAP:换货,REFUND_GOODS:退货,PATCH:补货;',
   refund               tinyint(1) comment '是否退款',
   refund_goods         tinyint(1) comment '是否退货',
   patch                tinyint(1) comment '是否补货',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales_item comment '售后工单项表';

/*==============================================================*/
/* Table: t_after_sales_patch                                   */
/*==============================================================*/
create table t_after_sales_patch
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '所属售后工单ID',
   after_sales_item_id  int(11) comment '售后工单项ID',
   product_id           int(11) comment '商品id',
   count                int(11) comment '补货数量（用来支持1*N，N*N，N*1）',
   order_item_type      varchar(20) comment '!OrderItemType:订单类型方便生成发货单;',
   payment_service_fee  bigint(20) comment '服务补差金额',
   payment_shipping_fee bigint(20) comment '邮费补差金额',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales_patch comment '补货表';

/*==============================================================*/
/* Table: t_after_sales_reason                                  */
/*==============================================================*/
create table t_after_sales_reason
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '售后工单ID',
   reason_id            int(11) comment '原因码ID',
   reason_code          varchar(128) comment '原因码',
   reason_text          text comment '原因描述',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales_reason comment '售后原因表';

/*==============================================================*/
/* Table: t_after_sales_refund                                  */
/*==============================================================*/
create table t_after_sales_refund
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '所属售后工单ID',
   after_sales_item_id  int(11) comment '订单项ID',
   fee                  bigint(20) default 0 comment '总金额',
   online_refund_id     int(11) comment '线上退款ID',
   online_fee           bigint(20) default 0 comment '线上退款总金额',
   offline_refund_id    int(11) comment '线下退款ID',
   offline_fee          bigint(20) default 0 comment '线下退款总金额',
   payment              tinyint(1) comment '退款是否已支付',
   payment_time         datetime comment '确认支付时间',
   refund_method        varchar(20) comment 'RefundMethod:退款方式;ALIPAY:支付宝,BANK:银行;',
   alipay_no            varchar(128) comment '退款买家支付宝账号（淘宝）',
   bank                 varchar(128) comment '退款银行',
   bank_accout          varchar(128) comment '退款银行账号',
   bank_user            varchar(128) comment '退款银行收款人姓名',
   refund_time          datetime default '0000-00-00 00:00:00' comment '退款时间',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales_refund comment '售后退款中间表';

/*==============================================================*/
/* Table: t_after_sales_refund_alloc                            */
/*==============================================================*/
create table t_after_sales_refund_alloc
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '所属售后工单ID',
   after_sales_item_id  int(11) comment '订单项ID',
   after_sales_refund_id int(11) comment '售后工单退款ID',
   type                 varchar(20) comment 'RefundClass:退款类型;GOODS:货款,POST:运费,REFUND_POST:退款运费;',
   fee                  bigint(20) default 0 comment '总金额',
   platform_fee         bigint(20) comment '平台承担金额',
   supplier_fee         bigint(20) comment '品牌商家承担金额',
   online               tinyint(1) comment '是否在线退款',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

/*==============================================================*/
/* Table: t_after_sales_refund_goods                            */
/*==============================================================*/
create table t_after_sales_refund_goods
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '所属售后工单ID',
   after_sales_item_id  int(11) comment '订单项ID',
   count                int(11) comment '退货数量',
   shipping_no          varchar(128) comment '物流编号',
   shipping_comp        varchar(64) comment '物流公司',
   received_count       int(11) comment '收货数量',
   returned             tinyint(1) comment '是否已退货（客户已发货）',
   received             tinyint(1) comment '是否已收货（仓库已收货）',
   pack                 varchar(20) comment 'RefundGoodsPack:退货产品包装;NEW:新,OLD:有/非新,NVL:无;',
   func                 varchar(20) comment 'RefundGoodsFunc:退货产品功能;GOOD:好,BAD:坏,CHECK:待检测;',
   face                 varchar(20) comment 'RefundGoodsFace:退货产品外观;NEW:新,SMALL_DAMAGE:轻微损:DAMAGE:严重损;',
   remark               varchar(2000) comment '退货产品收货备注',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales_refund_goods comment '换货补货关联表';

/*==============================================================*/
/* Table: t_after_sales_send                                    */
/*==============================================================*/
create table t_after_sales_send
(
   id                   int(11) not null auto_increment,
   after_sales_id       int(11) comment '售后工单ID',
   order_id             int(11) comment '订单ID',
   shipping_no          varchar(128) comment '物流编号',
   shipping_comp        varchar(64) comment '物流公司',
   receiver_name        varchar(128) comment '收货人姓名',
   receiver_phone       varchar(128) comment '收货人手机号(与收货人电话在业务上必须存在一个)',
   receiver_mobile      varchar(128) comment '收货人电话(与收货人手机号在业务上必须存在一个)',
   receiver_zip         varchar(128) comment '收货人邮编',
   receiver_state       varchar(64) comment '收货人省份',
   receiver_city        varchar(64) comment '收货人城市',
   receiver_district    varchar(64) comment '收货人地区',
   receiver_address     varchar(256) comment '不包含省市区的详细地址',
   receiver_remark      varchar(2000) comment '备注信息（卖家留言）',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   version              int(11) default 0 comment '版本号',
   primary key (id)
);

alter table t_after_sales_send comment '发货表';

/*==============================================================*/
/* Table: t_refund_alloc                                        */
/*==============================================================*/
create table t_refund_alloc
(
   id                   int(11) not null auto_increment,
   refund_id            int(11) comment '原退款单ID',
   type                 varchar(20) comment '!RefundClass:退款类型;',
   fee                  bigint(20) default 0 comment '总金额',
   platform_fee         bigint(20) comment '平台承担金额',
   supplier_fee         bigint(20) comment '品牌商家承担金额',
   online               tinyint(1) comment '是否在线退款',
   refund_time          datetime default '0000-00-00 00:00:00' comment '退款时间',
   operator_id          int(11) comment '操作者ID',
   create_time          datetime default '0000-00-00 00:00:00' comment '创建时间',
   update_time          datetime default '0000-00-00 00:00:00' comment '更新时间',
   primary key (id)
);
