ALTER TABLE `t_order`
ADD COLUMN `online`  tinyint NULL;

update t_order set status='BUYER_RECEIVE' where status='SIGNED';


