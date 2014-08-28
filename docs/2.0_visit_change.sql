drop table if exists t_customer;

/*==============================================================*/
/* Table: t_customer                                            */
/*==============================================================*/
create table t_customer
(
   id                   int(64) not null auto_increment,
   real_name            varchar(32) comment '会员姓名',
   phone                varchar(16) comment '电话',
   birthday             datetime comment '生日',
   state                varchar(16) comment '省份',
   city                 varchar(64) comment '城市',
   district             varchar(64) comment '区',
   address              varchar(64) comment '地址',
   bonus_point          int comment '会员积分',
   grade                tinyint comment '会员等级',
   total_trade_fee      bigint comment '累计消费金额',
   trade_count          int(10) comment '累计交易次数',
   last_trade_time      datetime comment '最近交易时间',
   create_time          datetime comment '生成时间',
   update_time          datetime comment '数据更新时间',
   operator_id          int comment '操作人id',
   email                varchar(32) comment '邮箱',
   primary key (id)
);

alter table t_customer comment '会员表';

drop table if exists t_customer_customertag;

/*==============================================================*/
/* Table: t_customer_customertag                                */
/*==============================================================*/
create table t_customer_customertag
(
   id                   int(16) not null auto_increment,
   customer_id          int(8),
   tag_id               int(8),
   primary key (id)
);

alter table t_customer_customertag comment '会员标签表';

drop index index_mobile on t_customer_mobile;

drop table if exists t_customer_mobile;

/*==============================================================*/
/* Table: t_customer_mobile                                     */
/*==============================================================*/
create table t_customer_mobile
(
   id                   int not null auto_increment,
   customer_id          int,
   mobile               varchar(16),
   primary key (id)
);

alter table t_customer_mobile comment '会员手机号';

/*==============================================================*/
/* Index: index_mobile                                          */
/*==============================================================*/
create unique index index_mobile on t_customer_mobile
(
   mobile
);
drop table if exists t_customer_shop;

/*==============================================================*/
/* Table: t_customer_shop                                       */
/*==============================================================*/
create table t_customer_shop
(
   id                   int not null auto_increment,
   customer_id          int,
   shop_id              int(8),
   primary key (id)
);

alter table t_customer_shop comment '会员商店关联表';
drop table if exists t_customer_tag;

/*==============================================================*/
/* Table: t_customer_tag                                        */
/*==============================================================*/
create table t_customer_tag
(
   id                   int(8) not null auto_increment,
   name                 varchar(64) comment '标签名称',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   operator_id          int comment '操作人',
   primary key (id)
);

alter table t_customer_tag comment '标签表';
drop table if exists t_return_visit_log;

/*==============================================================*/
/* Table: t_return_visit_log                                    */
/*==============================================================*/
create table t_return_visit_log
(
   id                   int not null auto_increment,
   task_id              int comment 't_return_visit_task外键',
   order_id             int comment 't_order外键',
   visitor_name         varchar(32) comment '回访专员用户名',
   visitor_realname     varchar(32) comment '回访专员真实姓名',
   status               varchar(12) comment '本次回访状态',
   create_time          datetime comment '生成时间',
   dial_status          varchar(12) comment '电话接通情况',
   appointment_time     datetime comment '预约时间',
   used                 tinyint comment '是否已使用',
   problem_accepted_soon tinyint comment '问题解决满意度',
   is_problem_solved    tinyint comment '问题受理是否及时',
   service_satisfaction varchar(12) comment '服务满意度',
   remark               varchar(1024) comment '说明',
   redirect_after_sale  tinyint comment '是否转售后',
   add_to_blacklist     tinyint,
   primary key (id)
);

drop index index_visitor on t_return_visit_task;

drop index index_status on t_return_visit_task;

drop table if exists t_return_visit_task;

/*==============================================================*/
/* Table: t_return_visit_task                                   */
/*==============================================================*/
create table t_return_visit_task
(
   id                   int not null auto_increment,
   type                 varchar(20) comment '回访类型',
   order_id             int comment '订单id',
   status               varchar(12) comment '回访状态',
   visitor_name         varchar(32) comment '访问专员用户名',
   visitor_realname     varchar(32) comment '访问专员真实姓名',
   last_visit_time      datetime comment '最近回访时间',
   appointment_time     datetime comment '预约时间',
   create_time          datetime comment '生成时间',
   update_time          datetime comment '更新时间',
   mobile               varchar(16) comment '收货人手机号',
   phone                varchar(16) comment '收货人电话',
   display_time         date comment '允许显示在列表的最早时间',
   platform_order_no    varchar(100) comment '外部平台订单编号',
   used                 tinyint comment '商品是否已使用',
   return_visit_no      varchar(20) comment '回访单编号',
   redirect_after_sale  tinyint comment '是否转售后',
   after_sale_status    varchar(12) comment '售后处理状态',
   primary key (id)
);

/*==============================================================*/
/* Index: index_status                                          */
/*==============================================================*/
create index index_status on t_return_visit_task
(
   status
);

/*==============================================================*/
/* Index: index_visitor                                         */
/*==============================================================*/
create index index_visitor on t_return_visit_task
(
   visitor_name
);

drop table if exists t_returnvisit_aftersales_log;

/*==============================================================*/
/* Table: t_returnvisit_aftersales_log                          */
/*==============================================================*/
create table t_returnvisit_aftersales_log
(
   id                   int not null auto_increment,
   remark               varchar(1024) comment '售后处理备注',
   operator             varchar(32) comment '操作者姓名',
   create_time          datetime comment '创建时间',
   return_visit_id      int comment '回访任务id',
   operator_id          int comment '操作者id',
   solution_type        varchar(20) comment '售后解决方案',
   after_sales_no       varchar(32) comment '售后单编号',
   primary key (id)
);

alter table t_returnvisit_aftersales_log comment '回访转售后处理日志';

drop table if exists t_returnvisitlog_reasoncode;

/*==============================================================*/
/* Table: t_returnvisitlog_reasoncode                           */
/*==============================================================*/
create table t_returnvisitlog_reasoncode
(
   id                   int not null auto_increment,
   log_id               int comment 't_return_visit_log外键',
   reason_id            int comment 't_reason_code外键',
   primary key (id)
);

alter table t_returnvisitlog_reasoncode comment '回访日志与原因码关联表';

drop table if exists t_reason_code_category;

/*==============================================================*/
/* Table: t_reason_code_category                                */
/*==============================================================*/
create table t_reason_code_category
(
   id                   int not null auto_increment,
   name                 varchar(64) comment '类别名称',
   level                tinyint comment '层级',
   parent_id            int comment '父id',
   create_time          datetime,
   update_time          datetime,
   operator_id          int,
   primary key (id)
);

alter table t_reason_code_category comment '原因码类目';


drop table if exists t_reason_code;

/*==============================================================*/
/* Table: t_reason_code                                         */
/*==============================================================*/
create table t_reason_code
(
   id                   int not null auto_increment,
   name                 varchar(64) comment '类别名称',
   code                 varchar(16) comment '原因码',
   remark               varchar(125) comment '备注',
   first_level_category_id int comment '第一层级类别',
   second_level_category_id int comment '第二层级类别',
   deleted              tinyint comment '删除标记',
   create_time          datetime,
   update_time          datetime,
   operator_id          operator_id,
   primary key (id)
);

alter table t_reason_code comment '原因码';



drop table if exists t_order_signed_log;

/*==============================================================*/
/* Table: t_order_signed_log                                    */
/*==============================================================*/
create table t_order_signed_log
(
   id                   int not null auto_increment,
   order_id             int comment '订单id',
   processed            tinyint comment '签收回访是否已处理',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (id)
);

alter table t_order_signed_log comment '订单签收日志';

drop table if exists t_blacklist;

/*==============================================================*/
/* Table: t_blacklist                                           */
/*==============================================================*/
create table t_blacklist
(
   id                   int not null auto_increment,
   type                 tinyint comment '黑名单类型',
   value                varchar(64) comment '邮箱/电话',
   create_time          datetime comment '添加时间',
   update_time          datetime comment '更新时间',
   operator_id          int comment '操作员id',
   primary key (id)
);

alter table t_blacklist comment '黑名单';
