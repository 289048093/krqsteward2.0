use steward;

alter table t_order add index t_order_index1 (platform_order_no);
alter table t_order add index t_order_index2 (order_no);

alter table t_order_item add index t_order_item_index1 (order_id);

alter table t_original_order add index t_order_index1 (platform_order_no);

alter table t_original_order_item add index t_original_order_item_index1 (original_order_id);


/* =============== 2014.7.9 ============= */

alter table t_product add unique(sku);
alter table t_mealset add unique(sku);


/* =============== 2014.7.18 ============= */

alter table t_order add index t_order_index3 (invoice_id);
alter table t_order_approve add index t_order_approve_index1 (order_id);
alter table t_order_handle_log add index t_order_handle_log_index1 (order_id);

alter table t_storage add index t_storage_index1 (product_id);
alter table t_storage add index t_storage_index2 (repository_id);

alter table t_storage_flow add index t_storage_flow_index1 (storage_id);