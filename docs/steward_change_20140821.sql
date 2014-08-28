drop table if exists t_original_traderate;

/*==============================================================*/
/* Table: original_traderate                                    */
/*==============================================================*/
create table t_original_traderate
(
   id                   int not null auto_increment,
   oid                  int(11) comment '交易ID',
   tid                  int(11) comment '子订单ID',
   role                 varchar(12) comment '评价者角色.可选值:seller(卖家),buyer(买家)',
   nick                 varchar(50) comment '评价者昵称',
   result               varchar(12) comment '评价结果,可选值:good(好评),neutral(中评),bad(差评)',
   created              date comment '评价创建时间,格式:yyyy-MM-dd HH:mm:ss',
   rated_nick           varchar(50) comment '被评价者昵称',
   item_title           varchar(128) comment '商品标题',
   item_price           bigint comment '商品价格,精确到2位小数;单位:元.如:200.07，表示:200元7分',
   content              varchar(250) comment '评价内容,最大长度:500个汉字',
   reply                varchar(250) comment '评价解释,最大长度:500个汉字',
   num_iid              int comment '商品的数字ID',
   valid_score          bool comment '评价信息是否用于记分， 可取值：true(参与记分)和false(不参与记分)',
   primary key (id)
);
drop table if exists t_rate_tag;

/*==============================================================*/
/* Table: t_rate_tag                                            */
/*==============================================================*/
create table t_rate_tag
(
   id                   int(11) not null auto_increment,
   tag_name             varchar(50) comment '标签的名称',
   posi                 bool comment '标签的极性，正极true，负极false',
   tag_type             int(11) comment '评价类型（1、原始评价2、追加评价）',
   primary key (id)
);


alter table t_rate_tag comment '评价标签对象';

drop table if exists t_rate_info;

/*==============================================================*/
/* Table: t_rate_info                                           */
/*==============================================================*/
create table t_rate_info
(
   id                   int(11) not null auto_increment,
   user_nick            varchar(50) comment '评价者的昵称',
   content              varchar(500) comment '评价内容',
   comment_time         date comment '评价时间',
   rate_tag_id          int(11),
   has_negtv            bool comment '原始评价是否含有负向标签',
   append_content       varchar(500) comment '追加评价内容',
   append_time          date comment '追加评价时间',
   append_has_negtv     bool comment '追评中是否含有负向标签',
   primary key (id)
);

alter table t_rate_info comment '评价信息包括标签信息';

alter table t_customer_tag add create_time datetime comment '创建时间';
alter table t_customer_tag add update_time datetime comment '更新时间';
alter table t_customer_tag add operator_id int comment '操作人';

alter table t_blacklist add create_time datetime comment '创建时间';
alter table t_blacklist add update_time datetime comment '更新时间';
alter table t_blacklist add operator_id int comment '操作人';

alter table t_comment add platform_order_no varchar(256) comment '外部平台订单编号';
alter table t_comment add platform_sub_order_no varchar(256) comment '外部平台子订单编号';
alter table t_comment add platform_type varchar(32) comment '平台';
alter table t_comment add is_category tinyint comment '是否已归类';

alter table t_comment add shop_name varchar(32) comment '店铺名';
