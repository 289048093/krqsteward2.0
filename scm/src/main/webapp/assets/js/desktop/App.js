/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('EBDesktop.App', {
    extend: 'Ext.ux.desktop.App',

    requires: [ 
        'Ext.window.MessageBox',
        'Ext.ux.desktop.ShortcutModel',
        'EBDesktop.Order',
        'EBDesktop.originalOrder',
        'EBDesktop.Customer',
        'EBDesktop.ShopProduct',
        'EBDesktop.Comment',
        'EBDesktop.Sms',
        'EBDesktop.Returnvisit',
        'EBDesktop.Shipment',
        'EBDesktop.Stock',
        'EBDesktop.Shop',
        'EBDesktop.Config',
        'EBDesktop.LogisticsQuery',
        'EBDesktop.OrderFetch',
        'EBDesktop.Product',
        'EBDesktop.Contract',
        //'EBDesktop.Brand',
        'EBDesktop.Log',
        'EBDesktop.StorageFlowLog',
        'EBDesktop.Financial',
        'EBDesktop.Payment',
        'EBDesktop.Refund',
        'EBDesktop.logistics.Win',
        'EBDesktop.warehouse.Win',
        'EBDesktop.gift.Win',
        //'EBDesktop.mutex.Win',
        //'EBDesktop.permission.Win',
        'EBDesktop.account.Win',
        'EBDesktop.role.Win',
        'EBDesktop.module.Win',
        //'EBDesktop.goods.Goods',
        'EBDesktop.brand.Win',
        'EBDesktop.supplier.Win',
        'EBDesktop.platform.Win',
        //'EBDesktop.contract.Win',
        'EBDesktop.productCategory.Win',
        'EBDesktop.mealset.Win',
        'EBDesktop.Warn'
    ],

    init: function () {
        // custom logic before getXYZ methods get called...

        this.callParent();

        // now ready...
    },

    getModules: function () {
        return [
            new EBDesktop.Order(),
            new EBDesktop.originalOrder(),
            new EBDesktop.Customer(),
            new EBDesktop.ShopProduct(),
            new EBDesktop.Comment(),
            new EBDesktop.Sms(),
            new EBDesktop.Returnvisit(),
            new EBDesktop.Shipment(),
            new EBDesktop.Stock(),
            new EBDesktop.Shop(),
            new EBDesktop.Product(),
            new EBDesktop.Contract(),
            //new EBDesktop.Brand(),
            new EBDesktop.Log(),
            new EBDesktop.StorageFlowLog(),
            new EBDesktop.Financial(),
            new EBDesktop.Payment(),
            new EBDesktop.Refund(),
            new EBDesktop.Config(),
            new EBDesktop.LogisticsQuery(),
            new EBDesktop.OrderFetch(),
            new EBDesktop.logistics.Win(),
            new EBDesktop.warehouse.Win(),
            new EBDesktop.gift.Win(),
            //new EBDesktop.mutex.Win(),
            //new EBDesktop.permission.Win(),
            new EBDesktop.account.Win(),
            new EBDesktop.role.Win(),
            new EBDesktop.module.Win(),
            //new EBDesktop.goods.Goods(),
            new EBDesktop.brand.Win(),
            //new EBDesktop.contract.Win(),
            new EBDesktop.supplier.Win(),
            new EBDesktop.platform.Win(),
            new EBDesktop.productCategory.Win(),
            new EBDesktop.mealset.Win(),
            new EBDesktop.Warn()
        ];

    },

    getDesktopConfig: function () {
        var me = this, ret = me.callParent();

        var moduleArray = [
            { name: '订单管理', iconCls: 'desktop-order', module: 'order-win'},
            { name: '发货管理', iconCls: 'desktop-shipment', module: 'shipment-win'},
            { name: '库存管理', iconCls: 'desktop-stock', module: 'stock-win' },
            { name: '物流管理', iconCls: 'desktop-logistics', module: 'logistics-win'},
            { name: '仓库管理', iconCls: 'desktop-warehouse', module: 'warehouse-win'},
            // { name: '优惠活动管理', iconCls: 'desktop-gift', module: 'gift-win' },
            { name: '品牌管理', iconCls: 'desktop-brand', module: 'brand-win' },
            { name: '供应商管理', iconCls: 'desktop-supplier', module: 'supplier-win' },
            { name: '平台管理', iconCls: 'desktop-platform', module: 'platform-win' },
            { name: '品牌商合同信息', iconCls: 'desktop-contract', module: 'contract-win' },
            { name: '产品分类管理', iconCls: 'desktop-productCategory', module: 'productCategory-win'},
            // { name: '用户管理', iconCls: 'desktop-account', module: 'account-win'},
            // { name: '角色管理', iconCls: 'desktop-role', module: 'role-win' },
            // { name: '模块管理', iconCls: 'desktop-module', module: 'module-win'},
            { name: '店铺管理', iconCls: 'desktop-shop', module: 'shop-win'},
            { name: '系统配置', iconCls: 'desktop-config', module: 'config-win'},
            { name: '产品管理', iconCls: 'desktop-product', module: 'product-win'},
            { name: '系统日志', iconCls: 'desktop-log', module: 'log-win'},
            { name: '库存日志', iconCls: 'desktop-storageflowlog', module: 'storageflowlog-win'},
            { name: '财务管理', iconCls: 'desktop-financial', module: 'financial-win'},
            { name: '预收款分配', iconCls: 'desktop-payment', module: 'payment-win'},
            { name: '退款管理', iconCls: 'desktop-refund', module: 'refund-win'},
            { name: '套餐管理', iconCls: 'desktop-mealset', module: 'mealset-win'},
            // { name: '订单预警', iconCls: 'desktop-warn', module: 'warn-win'},
            { name: '抓取订单查询', iconCls: 'desktop-orderfetch', module: 'orderfetch-win'},
            // { name: '快递单查询', iconCls: 'desktop-logisticsquery', module: 'logisticsquery-win'}
            { name: '异常订单查询', iconCls: 'desktop-OriginalOrder', module: 'OriginalOrder-win'},
            { name: '会员中心', iconCls: 'desktop-customer', module: 'customer-win'},
            { name: '商品同步', iconCls: 'desktop-shopProduct', module: 'shopProduct-win'},
            { name: '评价管理', iconCls: 'desktop-comment', module: 'comment-win'},
            { name: '营销中心', iconCls: 'desktop-sms', module: 'sms-win'},
            { name: '回访中心', iconCls: 'returnvisit-sms', module: 'returnvisit-win'},
        ];


        //假如GV.userId为空时 啥事都不做
        // if(GV.userId == ""){
        //     return;
        // }



//        var moduleArray =   [
//
//            { name: '异常订单查询', iconCls: 'desktop-originalOrder', module: 'originalOrder-win'},
//            {"id": 439, "name": "订单管理", "uniqueKey": "order", "iconCls": "desktop-order", "module": "order-win", "entryOperation": "查询订单", "hasEntryOperation": true, "operationList": [
//                        {"id": 3083, "url": "/order/ItemList", "name": "查询订单详细", "configable": true, "required": null},
//                        {"id": 3087, "url": "/order/shopList", "name": "查看所有的店铺", "configable": false, "required": null},
//                        {"id": 3088, "url": "/order", "name": "订单界面", "configable": false, "required": "查询所有仓库,查询品牌,查看所有的店铺"},
//                        {"id": 3089, "url": "/order/list", "name": "查询订单", "configable": true, "required": "查询订单详细,订单界面"},
//                        {"id": 3090, "url": "/order/approveLogs", "name": "查看订单审核信息", "configable": true, "required": null},
//                        {"id": 3091, "url": "/order/check", "name": "批量审核", "configable": true, "required": "查询订单"},
//                        {"id": 3092, "url": "/order/updateByOrder", "name": "编辑订单", "configable": true, "required": "查询订单"},
//                        {"id": 3094, "url": "/order/cancellation", "name": "订单作废", "configable": true, "required": "查询订单"},
//                        {"id": 3095, "url": "/order/recover", "name": "订单恢复", "configable": true, "required": "查询订单"},
//                        {"id": 3096, "url": "/order/confirm", "name": "导入进销存", "configable": true, "required": "查询订单"},
//                        {"id": 3099, "url": "/order/addGift", "name": "加产品", "configable": true, "required": "查询产品,查询订单"},
//                        {"id": 3100, "url": "/order/splitOrder", "name": "订单拆分", "configable": true, "required": "查询订单"},
//                        {"id": 3101, "url": "/order/exchangeGood", "name": "订单售前换货", "configable": true, "required": "查询订单,查询产品"},
//                        {"id": 3102, "url": "/product/list/addOrder", "name": "搜索产品", "configable": false, "required": null},
//                        {"id": 3104, "url": "/order/deleteItemList", "name": "删除订单详细", "configable": true, "required": "查询订单"},
//                        {"id": 3196, "url": "/order/updateStautsByOrder", "name": "批量改物流", "configable": true, "required": "查询订单,查询所有仓库,查看所有的店铺"},
//                        {"id": 3293, "url": "/order/addOrder", "name": "加订单", "configable": true, "required": "查询订单,搜索产品,查看所有的店铺"},
//                        {"id": 3294, "url": "/order/addExchangeOrder", "name": "加售后换货订单", "configable": true, "required": "查询订单,搜索产品,查看所有的店铺"},
//                        {"id": 3295, "url": "/order/extractExchangeOrder2Excel", "name": "补货换货订单导出", "configable": true, "required": "查询订单"},
//                        {"id": 3331, "url": "/order/reportOrders", "name": "订单管理订单导出", "configable": true, "required": null},
//                        {"id": 3343, "url": "/order/leadInOrders", "name": "订单管理订单导入", "configable": true, "required": "查询订单"},
//                        {"id": 3344, "url": "/order/leadInOrderTemplate", "name": "订单管理订单导入模板", "configable": true, "required": null}
//                    ]},
//                    {"id": 440, "name": "仓库管理", "uniqueKey": "repository", "iconCls": "desktop-warehouse", "module": "warehouse-win", "entryOperation": "查询仓库", "hasEntryOperation": true, "operationList": [
//                        {"id": 3084, "url": "/repository/find_all", "name": "查询所有仓库", "configable": false, "required": null},
//                        {"id": 3105, "url": "/repository/list", "name": "查询仓库", "configable": true, "required": null},
//                        {"id": 3106, "url": "/province/findAll", "name": "查询省份", "configable": false, "required": null},
//                        {"id": 3107, "url": "/employee/list", "name": "用户列表", "configable": false, "required": null},
//                        {"id": 3108, "url": "/repository/add", "name": "添加仓库", "configable": true, "required": "查询仓库,查询省份,用户列表"},
//                        {"id": 3109, "url": "/repository/delete", "name": "删除仓库", "configable": true, "required": "查询仓库"},
//                        {"id": 3110, "url": "/repository/save", "name": "编辑仓库", "configable": true, "required": "查询仓库,用户列表,查询省份"}
//                    ]},
//                    {"id": 441, "name": "产品管理", "uniqueKey": "product", "iconCls": "desktop-product", "module": "product-win", "entryOperation": "查询产品", "hasEntryOperation": true, "operationList": [
//                        {"id": 3097, "url": "/product/getHead", "name": "产品头标签", "configable": false, "required": null},
//                        {"id": 3098, "url": "/product/list", "name": "查询产品", "configable": true, "required": "产品头标签,查询品牌名称"},
//                        {"id": 3111, "url": "/product/detail", "name": "产品详细", "configable": false, "required": null},
//                        {"id": 3113, "url": "/product/save", "name": "添加产品", "configable": true, "required": "查询产品,产品分类查询,查询品牌,查询仓库"},
//                        {"id": 3114, "url": "/product/update", "name": "编辑产品", "configable": true, "required": "查询产品,产品分类查询,查询品牌,查询仓库,产品详细"},
//                        {"id": 3115, "url": "/templet/productExcelModel", "name": "下载excel样板", "configable": false, "required": null},
//                        {"id": 3116, "url": "/product/leadingIn", "name": "导入产品", "configable": true, "required": "查询产品,下载excel样板"},
//                        {"id": 3118, "url": "/product", "name": "产品列表", "configable": false, "required": "查询平台信息"},
//                        {"id": 3119, "url": "/product/delete", "name": "删除产品", "configable": true, "required": "查询产品"}
//                    ]},
//                    {"id": 442, "name": "发货管理", "uniqueKey": "invoice", "iconCls": "desktop-shipment", "module": "shipment-win", "entryOperation": "查询发货订单", "hasEntryOperation": true, "operationList": [
//                        {"id": 3120, "url": "/invoice/list_invoice_orders", "name": "查询发货订单", "configable": false, "required": null},
//                        {"id": 3121, "url": "/invoice/list_invoice_order_item", "name": "查询发货订单项", "configable": true, "required": "查询发货订单"},
//                        {"id": 3122, "url": "/invoice/back_to_wait_process", "name": "返回客服处理", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3123, "url": "/invoice/delivery_print", "name": "打印物流单", "configable": true, "required": "查询发货订单,查询发货订单项,获取物流单页面"},
//                        {"id": 3124, "url": "/invoice/order_print", "name": "打印发货单", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3125, "url": "/invoice/OrderShipping/update", "name": "联想物流单号", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3126, "url": "/order/updateShippingNo", "name": "修改订单物流单号", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3127, "url": "/invoice/affirm_print", "name": "确认打印", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3128, "url": "/invoice/back_to_confirm", "name": "返回待处理", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3129, "url": "/invoice/batch_examine", "name": "验货", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3130, "url": "/invoice/inspection", "name": "验货订单查询", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3131, "url": "/invoice/back_to_print", "name": "返回已打印", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3132, "url": "/invoice/invoice", "name": "确认发货", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3207, "url": "/invoice/signed", "name": "签收订单", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3314, "url": "/logisticsprint/get_print_html", "name": "获取物流单页面", "configable": false, "required": null},
//                        {"id": 3332, "url": "/invoice/list_invoice_report_orders", "name": "发货管理订单导出", "configable": true, "required": null},
//                        {"id": 3360, "url": "/order/invoiceUpdateStautsByOrder", "name": "批量改订单物流", "configable": true, "required": "查询发货订单,查询发货订单项"},
//                        {"id": 3361, "url": "/invoice/collect_invoice_order_excel", "name": "发货管理汇总导出", "configable": true, "required": null}
//                    ]},
//                    {"id": 443, "name": "库存管理", "uniqueKey": "storage", "iconCls": "desktop-stock", "module": "stock-win", "entryOperation": "查询库存", "hasEntryOperation": true, "operationList": [
//                        {"id": 3133, "url": "/storage/list", "name": "查询库存", "configable": false, "required": "查询品牌名称,查询仓库,产品分类查询"},
//                        {"id": 3134, "url": "/storage/storage_increment", "name": "入库", "configable": true, "required": "查询库存"},
//                        {"id": 3135, "url": "/storage/storage_reduce", "name": "出库", "configable": true, "required": "查询库存"},
//                        {"id": 3136, "url": "/storage/download_template", "name": "库存导入模版下载", "configable": true, "required": "查询库存"},
//                        {"id": 3137, "url": "/storage/preview_batch_update", "name": "预览库存导入文件", "configable": true, "required": "查询库存"},
//                        {"id": 3138, "url": "/storage/batch_update", "name": "批量导入库存", "configable": true, "required": "查询库存"},
//                        {"id": 3139, "url": "/storage/force_batch_update", "name": "强制批量导入库存", "configable": true, "required": "查询发货订单"}
//                    ]},
//                    {"id": 444, "name": "物流管理", "uniqueKey": "LogisticsInfo", "iconCls": "desktop-logistics", "module": "logistics-win", "entryOperation": "物流查询", "hasEntryOperation": true, "operationList": [
//                        {"id": 3140, "url": "/logisticsprint/list", "name": "物流查询", "configable": false, "required": null},
//                        {"id": 3141, "url": "/logisticsprint/save", "name": "物流保存或更新", "configable": true, "required": "物流查询"},
//                        {"id": 3142, "url": "/logistics_design", "name": "物流跳转", "configable": false, "required": null},
//                        {"id": 3143, "url": "/logisticsprint/updateDesign", "name": "物流打印信息设计", "configable": true, "required": "物流查询,物流跳转,获取物流设计信息"},
//                        {"id": 3315, "url": "/logisticsprint/detail", "name": "获取物流设计信息", "configable": false, "required": null}
//                    ]},
//                    {"id": 445, "name": "库存日志管理", "uniqueKey": "storageFlow", "iconCls": "desktop-storageflowlog", "module": "storageflowlog-win", "entryOperation": "查询出入库产品", "hasEntryOperation": true, "operationList": [
//                        {"id": 3144, "url": "/storageflow/list_product", "name": "查询出入库产品", "configable": false, "required": "查询仓库"},
//                        {"id": 3145, "url": "/storageflow/list", "name": "查询产品出入库日志", "configable": false, "required": "查询出入库产品"}
//                    ]},
//                    {"id": 446, "name": "供应商合同信息", "uniqueKey": "contract", "iconCls": "desktop-contract", "module": "contract-win", "entryOperation": "查询合同", "hasEntryOperation": true, "operationList": [
//                        {"id": 3146, "url": "/contract/list", "name": "查询合同", "configable": false, "required": "查询供应商"},
//                        {"id": 3147, "url": "/contract/save", "name": "添加合同", "configable": true, "required": "查询合同"},
//                        {"id": 3148, "url": "/contract/delete", "name": "删除合同", "configable": true, "required": "查询合同"},
//                        {"id": 3149, "url": "/contract/leadingIn", "name": "导入合同", "configable": true, "required": "查询合同"},
//                        {"id": 3296, "url": "/contract/update", "name": "修改合同", "configable": true, "required": "查询合同"}
//                    ]},
//                    {"id": 447, "name": "品牌管理", "uniqueKey": "brand", "iconCls": "desktop-brand", "module": "brand-win", "entryOperation": "查询品牌", "hasEntryOperation": true, "operationList": [
//                        {"id": 3086, "url": "/brand/list", "name": "查询品牌", "configable": false, "required": "查询供应商"},
//                        {"id": 3150, "url": "/brand/save", "name": "添加品牌", "configable": true, "required": "查询品牌"},
//                        {"id": 3151, "url": "/brand/update", "name": "修改品牌", "configable": true, "required": "查询品牌"},
//                        {"id": 3152, "url": "/brand/delete", "name": "删除品牌", "configable": true, "required": "查询品牌"},
//                        {"id": 3153, "url": "/brand/leadingIn", "name": "导入品牌", "configable": true, "required": "查询品牌"},
//                        {"id": 3320, "url": "/brand/list_brand_id_name", "name": "查询品牌名称", "configable": false, "required": null}
//                    ]},
//                    {"id": 448, "name": "退款管理", "uniqueKey": "refund", "iconCls": "desktop-refund", "module": "refund-win", "entryOperation": "查询退款", "hasEntryOperation": true, "operationList": [
//                        {"id": 3154, "url": "/refund/list", "name": "查询退款", "configable": true, "required": "查看所有的店铺"},
//                        {"id": 3155, "url": "/refund/delete", "name": "删除线下退款", "configable": true, "required": "查询退款"},
//                        {"id": 3156, "url": "/refund/update", "name": "修改线上退款信息", "configable": true, "required": "查询退款,删除订单详细"},
//                        {"id": 3157, "url": "/refund/add", "name": "添加线下退款信息", "configable": true, "required": "查询退款,查询订单详细"},
//                        {"id": 3272, "url": "/refund/extract2excel", "name": "导出退款信息", "configable": true, "required": "查询退款"}
//                    ]},
//                    {"id": 449, "name": "供应商管理", "uniqueKey": "supplier", "iconCls": "desktop-supplier", "module": "supplier-win", "entryOperation": "查询供应商", "hasEntryOperation": true, "operationList": [
//                        {"id": 3085, "url": "/supplier/list", "name": "查询供应商", "configable": false, "required": null},
//                        {"id": 3158, "url": "/supplier/delete", "name": "删除供应商", "configable": true, "required": "查询供应商"},
//                        {"id": 3159, "url": "/supplier/update", "name": "修改供应商", "configable": true, "required": "查询供应商"},
//                        {"id": 3160, "url": "/supplier/save", "name": "添加供应商", "configable": true, "required": "查询供应商"}
//                    ]},
//                    {"id": 450, "name": "平台管理", "uniqueKey": "platform", "iconCls": "desktop-platform", "module": "platform-win", "entryOperation": "查询平台信息", "hasEntryOperation": true, "operationList": [
//                        {"id": 3117, "url": "/platform/list", "name": "查询平台信息", "configable": false, "required": null},
//                        {"id": 3161, "url": "/platform/delete", "name": "删除平台信息", "configable": true, "required": "查询平台信息"},
//                        {"id": 3162, "url": "/platform/update", "name": "修改平台信息", "configable": true, "required": "查询平台信息"},
//                        {"id": 3163, "url": "/platform/save", "name": "添加平台信息", "configable": true, "required": "查询平台信息"}
//                    ]},
//                    {"id": 451, "name": "抓单记录", "uniqueKey": "orderfetch", "iconCls": "desktop-orderfetch", "module": "orderfetch-win", "entryOperation": "查询抓取记录", "hasEntryOperation": true, "operationList": [
//                        {"id": 3164, "url": "/orderFetch/list", "name": "查询抓取记录", "configable": false, "required": null}
//                    ]},
//                    {"id": 452, "name": "系统配置", "uniqueKey": "conf", "iconCls": "desktop-config", "module": "config-win", "entryOperation": "查询配置", "hasEntryOperation": true, "operationList": [
//                        {"id": 3165, "url": "/conf/list", "name": "查询配置", "configable": true, "required": null},
//                        {"id": 3166, "url": "/conf/addOrUpdate", "name": "添加修改配置", "configable": true, "required": "查询配置"}
//                    ]},
//                    {"id": 453, "name": "系统日志", "uniqueKey": "businessLog", "iconCls": "desktop-log", "module": "log-win", "entryOperation": "查询日志", "hasEntryOperation": true, "operationList": [
//                        {"id": 3167, "url": "/businessLog/list", "name": "查询日志", "configable": true, "required": null}
//                    ]},
//                    {"id": 454, "name": "预收款分配", "uniqueKey": "payment", "iconCls": "desktop-payment", "module": "payment-win", "entryOperation": "预收款查询", "hasEntryOperation": true, "operationList": [
//                        {"id": 3168, "url": "/payment/orderItem", "name": "预收款明细查询", "configable": false, "required": null},
//                        {"id": 3169, "url": "/payment/list", "name": "预收款查询", "configable": true, "required": "预收款明细查询,查看所有的店铺"},
//                        {"id": 3170, "url": "/payment/isOrderItemLegal", "name": "订单项分配前检查", "configable": true, "required": "预收款查询"},
//                        {"id": 3171, "url": "/payment/save", "name": "预收款分配", "configable": true, "required": "预收款查询,订单项分配前检查,查询订单详细"},
//                        {"id": 3273, "url": "/payment/extract2excel", "name": "导出预收款信息", "configable": true, "required": "预收款查询"}
//                    ]},
//                    {"id": 455, "name": "套餐管理", "uniqueKey": "mealset", "iconCls": "desktop-mealset", "module": "mealset-win", "entryOperation": "套餐查询", "hasEntryOperation": true, "operationList": [
//                        {"id": 3172, "url": "/mealSetItem/list", "name": "套餐项查询", "configable": false, "required": null},
//                        {"id": 3173, "url": "/mealset/list", "name": "套餐查询", "configable": true, "required": "套餐项查询"},
//                        {"id": 3174, "url": "/mealset/save", "name": "新增套餐", "configable": true, "required": "套餐查询,查询产品"},
//                        {"id": 3175, "url": "/mealset/delete", "name": "删除套餐", "configable": true, "required": "套餐查询"},
//                        {"id": 3297, "url": "/meal_set/update", "name": "修改套餐", "configable": true, "required": "套餐查询"}
//                    ]},
//                    {"id": 456, "name": "产品分类管理", "uniqueKey": "productCategory", "iconCls": "desktop-productCategory", "module": "productCategory-win", "entryOperation": "产品分类查询", "hasEntryOperation": true, "operationList": [
//                        {"id": 3112, "url": "/productCategory/list", "name": "产品分类查询", "configable": true, "required": null},
//                        {"id": 3176, "url": "/productCategory/save", "name": "新增产品分类", "configable": true, "required": "产品分类查询"},
//                        {"id": 3177, "url": "/productCategory/update", "name": "修改产品分类", "configable": true, "required": "产品分类查询"},
//                        {"id": 3178, "url": "/productCategory/delete", "name": "删除产品分类", "configable": true, "required": "产品分类查询"},
//                        {"id": 3179, "url": "/product_category/upload_excel", "name": "导入产品分类", "configable": true, "required": null}
//                    ]},
//                    {"id": 459, "name": "店铺管理", "uniqueKey": "shop", "iconCls": "desktop-shop", "module": "shop-win", "entryOperation": "店铺查询", "hasEntryOperation": true, "operationList": [
//                        {"id": 3197, "url": "/shop/detail", "name": "店铺明细查询", "configable": false, "required": null},
//                        {"id": 3198, "url": "/shop/list", "name": "店铺查询", "configable": true, "required": "店铺明细查询"},
//                        {"id": 3199, "url": "/shop/update", "name": "店铺更新", "configable": true, "required": "店铺查询"},
//                        {"id": 3200, "url": "/shop/delete", "name": "店铺删除", "configable": true, "required": "店铺查询"},
//                        {"id": 3201, "url": "/shop/dynamicGetScore", "name": "动态获取评分", "configable": true, "required": "店铺查询"}
//                    ]},
//                    {"id": 479, "name": "财务管理", "uniqueKey": "financial", "iconCls": "desktop-financial", "module": "financial-win", "entryOperation": "财务数据查询", "hasEntryOperation": true, "operationList": [
//                        {"id": 3298, "url": "/financial/list", "name": "财务数据查询", "configable": true, "required": null},
//                        {"id": 3299, "url": "/financial/extract2excel", "name": "导出财务数据", "configable": true, "required": "财务数据查询"},
//                        {"id": 3362, "url": "/financial/extractMerger2excel", "name": "导出财务汇总数据", "configable": true, "required": "财务数据查询"}
//                    ]},
//                    {"id": 482, "name": "优惠活动管理", "uniqueKey": "activity", "iconCls": "desktop-gift", "module": "gift-win", "entryOperation": "查询优惠活动", "hasEntryOperation": true, "operationList": [
//                        {"id": 3355, "url": "/activity/list", "name": "查询优惠活动", "configable": true, "required": null},
//                        {"id": 3356, "url": "/activity/detail", "name": "查询优惠活动明细", "configable": true, "required": "查询优惠活动,查询品牌"},
//                        {"id": 3357, "url": "/activity/save", "name": "增加优惠活动", "configable": true, "required": "查询优惠活动,查询品牌"},
//                        {"id": 3358, "url": "/activity/update", "name": "修改优惠活动", "configable": true, "required": "查询优惠活动,查询优惠活动明细,查询品牌"},
//                        {"id": 3359, "url": "/activity/delete", "name": "删除优惠活动", "configable": true, "required": "查询优惠活动"}
//                    ]}
//                ]



//        var moduleArray = [];
//        Ext.Ajax.request({
//            url: '/employee/resource',
//            params: {
//                employeeId: GV.employeeId
//            },
//            async: false,
//            success: function (response) {
//                var data = Ext.decode(response.responseText);
//                if (data.success) {
//                    moduleArray = data.data.list;
//
//                }
//            }
//        });

        return Ext.apply(ret, {
            //cls: 'ux-desktop-black',

            contextMenuItems: [
                { text: 'Change Settings', scope: me }
            ],
            shortcuts: Ext.create('Ext.data.Store', {
                model: 'Ext.ux.desktop.ShortcutModel',
                data: moduleArray

            }),

            wallpaper: 'assets/images/wallpapers/default.jpg',
            wallpaperStretch: false
        });

    },

    // config for the start menu
    getStartConfig: function () {
        var me = this, ret = me.callParent();

        return Ext.apply(ret, {
            title: GV.username,
            iconCls: 'user',
            height: 200,
            toolConfig: {
                width: 150,
                items: [
                    {
                        text: '修改密码',
                        iconCls: 'settings',
                        scope: me,
                        handler: function () {

                            var form = new Ext.form.FormPanel({
                                buttonAlign: 'center',
                                width: 220,
                                id: 'updatePass',
                                frame: false,
                                border: 0,
                                allowBlank: false,
                                style: 'padding: 15px 5px 0px 5px;',
                                defaultType: 'textfield',
                                fieldDefaults: {
                                    labelAlign: 'right',
                                    labelWidth: 70
                                },
                                items: [
                                    {
                                        name: 'employeeId',
                                        hidden: true,
                                        value: GV.employeeId
                                    },
                                    {
                                        fieldLabel: '原密码',
                                        inputType: 'password',
                                        emptyText: '原密码',
                                        allowBlank: false,

                                        name: 'oldPassword'
                                    },
                                    {
                                        fieldLabel: '新密码',
                                        emptyText: '6-32位密码',
                                        blankText: '新密码不能为空',
                                        allowBlank: false,
                                        inputType: 'password',
                                        minLength: 6,
                                        maxLength: 32,
                                        name: 'newPassword'
                                    },
                                    {
                                        fieldLabel: '确认新密码',
                                        emptyText: '6-32位密码',
                                        inputType: 'password',
                                        blankText: '新密码不能为空',
                                        allowBlank: false,
                                        minLength: 6,
                                        maxLength: 32,
                                        name: 'reNewPassword'
                                    }
                                ]
                            });

                            var win = new Ext.Window({
                                title: '修改密码',
                                width: 300,
                                height: 200,
                                animateTarget: Ext.getBody(),
                                colseAction: 'hide',
                                constrainHeader: true,
                                layout: 'fit',
                                items: form,
                                listeners: {
                                    'render': function (input) {
                                        var map = new Ext.util.KeyMap({
                                            target: 'updatePass',    //target可以是组建的id  加单引号
                                            binding: [
                                                {                       //绑定键盘事件
                                                    key: Ext.EventObject.ENTER,
                                                    fn: function () {
                                                        Ext.getCmp('addBtn').getEl().dom.click();
                                                    }
                                                }
                                            ]
                                        });
                                    },
                                },
                                buttons: [
                                    {
                                        text: '保存',
                                        itemId: 'addBtn',
                                        id: 'addBtn',
                                        handler: function (btn) {
                                            var updateForm = Ext.getCmp('updatePass').getForm();
                                            if (updateForm.isValid()) {
                                                var data = updateForm.getValues();
                                                if (data.newPassword === data.reNewPassword) {

                                                    Ext.Ajax.request({
                                                        params: {
                                                            employeeId: data.employeeId,
                                                            oldPassword: data.oldPassword,
                                                            newPassword: data.newPassword
                                                        },
                                                        url: "/employee/updatePassword",
                                                        success: function (response, options) {
                                                            var data = Ext.JSON.decode(response.responseText);
                                                            if (data.success) {
                                                                Ext.Msg.alert('提示', '密码更新成功', function () {
                                                                    location.href = '/logout.action';
                                                                })
                                                            } else {

                                                                Espide.Common.showGridSelErr(data.msg);
                                                            }
                                                        },
                                                        failure: function (form, action) {
                                                            var data = Ext.JSON.decode(action.response.responseText);
                                                            Ext.MessageBox.show({
                                                                title: '提示',
                                                                msg: data.msg,
                                                                buttons: Ext.MessageBox.OK,
                                                                icon: 'x-message-box-warning'
                                                            });
                                                        }
                                                    });


                                                } else {
                                                    Espide.Common.showGridSelErr('新密码与确认密码必须一致！');
                                                }
                                            }

                                        }
                                    }
                                ]


                            });

                            win.show();
                        }
                    },
                    '-',
                    {
                        text: '退出系统',
                        iconCls: 'logout',
                        scope: me,
                        handler: function () {
                            location.href = '/logout.action';
                        }
                    }
                ]
            }
        });
    },

    onLogout: function () {
        Ext.Msg.confirm('退出', '你确定要退出系统吗?');
    },

    onSettings: function () {
        var dlg = new EBDesktop.Settings({
            desktop: this.desktop
        });
        dlg.show();
    }
});
