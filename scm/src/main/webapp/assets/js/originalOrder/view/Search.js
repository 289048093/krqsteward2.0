/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.orderSearch',
    region: 'north',
    border: 0,
    bodyPadding: 4,
    layout: {
        type: 'hbox',
        align: 'left'
    },
    height: 'auto',
    defaultType: 'fieldcontainer',
    defaults: {
        margin: '0 5 0 0',
        defaults: {
            xtype: 'combo',
            labelWidth: 60,
            width: 200,
            queryMode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            editable: false
        }
    },
    initComponent: function () {
        var platform = Espide.Common.createComboStore('/np/platform/list',true,['id','name','type']);
        this.items = [
            {
                items: [
                    /***  搜索表单第一行    ***/
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'row0',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 1200,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
                            margin: '0 5 0 0'
                        },
                        items: [
                            {
                                name: 'dateType',
                                labelWidth: 60,
                                width: 190,
                                fieldLabel: '日期类型',
                                itemId: 'dateType',
                                id: 'dateType',
                                value: 'all',
                                store: [
                                    ['all', '全部'],
                                    ['payDate', '付款日期'],
                                   // ['printDate', '打印日期'],
                                   // ['examinedDate', '验货日期'],
                                   // ['deliveryDate', '发货日期'],
                                    ['orderDate', '下单日期'],
                                    //['receiptDate', '签收日期']
                                ],
                                listeners: {
                                    select: function (combo, record, index) {
                                        switch (combo.rawValue) {
                                            case '打印日期':
                                                Ext.getCmp('appOrderStatus').setValue("PRINTED");
                                                break;
                                            case '验货日期':
                                                Ext.getCmp('appOrderStatus').setValue("EXAMINED");
                                                break;
                                            case '发货日期':
                                                Ext.getCmp('appOrderStatus').setValue("INVOICED");
                                                break;
                                            case '签收日期':
                                                Ext.getCmp('appOrderStatus').setValue("SIGNED");
                                                break;
                                            default :
                                                Ext.getCmp('appOrderStatus').setValue("all");
                                        }
                                    }
                                }
                            },
                            {
                                name: 'appOrderStatus',
                                id: 'appOrderStatus',
                                labelWidth: 60,
                                width: 200,
                                fieldLabel: '拆分状态',
                                itemId: 'appOrderStatus',
                                value: 'all',
                                hidden: true,
                                store: [
                                    ['all', '全部'],
                                    ['PRINTED', '打印日期'],
                                    ['INVOICED', '发货日期'],
                                    ['EXAMINED', '验货日期'],
                                    ['SIGNED', '签收日期'],
                                ]
                            },

                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '开始日期',
                                value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                                name: 'startDate',
                                id: 'startDate',
                                disabled:true,
                                itemId: 'startDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 215
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '结束日期',
                                name: 'endDate',
                                disabled:true,
                                itemId: 'endDate',
                                id: 'endDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 215
                            }),
                            {
                                name: 'processed',
                                labelWidth:60,
                                width:200,
                                id: 'processed',
                                fieldLabel: '解析状态',
                                value: 'false',
                                store: [
                                    ['null','全部'],
                                    ['true', '已被解析'],
                                    ['false', '未被解析'],
                                ]
                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'hbox',
                                combineErrors: true,
                                msgTarget: 'side',
                                fieldLabel: '条件1',
                                labelWidth: 40,
                                width: 370,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    hideLabel: true,
                                    margin: '0 5 0 0'
                                },
                                items: [
                                    {
                                        width: 120,
                                        name: 'conditionQuery',
                                        itemId: 'querySelect',
                                        valueField: 'id',
                                        displayField: 'value',
                                        value: 'platformOrderNo',
                                        store: Ext.create('Ext.data.Store', {
                                            fields: ['id', 'value', 'type'],
                                            data: [
//                                                {id: 'buyerId', value: '买家id', type: 'string'},
//                                                {id: 'buyerAlipayNo', value: '支付宝账号', type: 'string'},
//                                                {id: 'receiverName', value: '收货人', type: 'string'},
//                                                {id: 'buyerMessage', value: '买家留言', type: 'string'},
//                                                {id: 'remark', value: '卖家备注', type: 'string'},
//                                                {id: 'orderNo', value: '智库城订单编号', type: 'string'},
                                                {id: 'platformOrderNo', value: '外部平台订单编号', type: 'string'},
                                                {id: 'sku', value: 'sku', type: 'string'},
                                                {id: 'receiverAddress', value: '收货地址', type: 'string'},
                                                {id: 'offlineRemark', value: '线下备注', type: 'string'},
//                                                {id: 'prodCode', value: '商品编号', type: 'string'},
//                                                {id: 'shippingNo', value: '快递单号', type: 'string'},
//                                                {id: 'actualFee', value: '成交金额', type: 'number'},
//                                                {id: 'receiverMobile', value: '收货手机', type: 'string'},
//                                                {id: 'prodName', value: '商品名称', type: 'string'},
                                            ]
                                        })
                                    },
                                    {
                                        name: 'conditionType',
                                        value: 'has',
                                        itemId: 'queryType',
                                        valueField: 'id',
                                        displayField: 'value',
                                        width: 70,
                                        store: Ext.create('Ext.data.Store', {
                                            fields: ['id', 'value', 'type'],
                                            data: [
                                                {id: 'has', value: '包含', type: 'string'},
                                                {id: '!', value: '不包含', type: 'string'},
                                                {id: '=', value: '等于', type: 'all'},
                                                {id: '!=', value: '不等于', type: 'number'},
                                                {id: '>=', value: '大于等于', type: 'number'},
                                                {id: '<=', value: '小于等于', type: 'number'}

                                            ]
                                        })
                                    },
                                    {xtype: 'textfield', name: 'conditionValue', width: 105, margin: '0 5 0 0'}
                                ]
                            },
                        ]
                    },
                    /***  搜索表单第二行    ***/
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'row1',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 1200,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
                            margin: '0 5 0 0'
                        },
                        items: [
//                            {
//                                fieldLabel: '库房',
//                                emptyText: '请选择',
//                                labelWidth: 35,
//                                width: 190,
//                                name: 'repoId',
//                                valueField: 'id',
//                                displayField: 'name',
//                                queryMode: 'remote',
//                                store: 'StorageAll'
//                            },
                            {
                                name: 'outPlatformType',
                                id: 'outPlatformType',
                                fieldLabel: '平台类型',
                                value: 'null',
                                labelWidth: 60,
                                width: 190,
                                valueField: 'type',
                                displayField: 'name',
                                editable: false,
                                emptyText: '请选择',
                                store: platform,
                            },
                            {
                                fieldLabel: '店铺名称',
                                emptyText: '请选择',
                                valueField: 'id',
                                displayField: 'nick',
                                labelWidth: 60,
                                width: 215,
                                id: 'shopId',
                                queryMode: 'remote',
                                name: 'shopId',
                                store: 'ShopAll'
                            },
                            {

                                name: 'brandId',
                                fieldLabel: '品牌',
                                value: 30,
                                labelWidth: 60,
                                emptyText: '请选择',
                                valueField: 'id',
                                displayField: 'name',
                                width: 215,
                                queryMode: 'remote',
                                store: 'Brand'

                            },
                            {
                                labelWidth: 100,
                                fieldLabel: '显示订单条数',
                                editable: false,
                                value: 30,
                                width: 200,
                                name: 'limit',
                                id: 'limit',
                                store: [
                                    [30, 30],
                                    [50, 50],
                                    [100, 100],
                                    [200, 200],
                                    [300, 300],
                                ]
                            },
                            {
                                name: 'discard',
                                labelWidth:60,
                                width:200,
                                id: 'discard',
                                fieldLabel: '是否有效',
                                value: 'false',
                                store: [
                                    ['null','全部'],
                                    ['true', '失效'],
                                    ['false', '正常'],
                                ]
                            },
                        ]
                    },
                ]
            },
            /***  搜索栏提交 重置 按钮   ***/
            {
                items: [
                    {
                        xtype: 'button',
                        text: '查询',
                        itemId: 'confirmBtn',
                        margin: '0 0 0 0',
                        width: 55,
                        height: 35
                    },
                    {
                        xtype: 'button',
                        text: '重置',
                        itemId: 'resetBtn',
                        margin: '0 0 0 8',
                        width: 55,
                        height: 35,
                        handler: function (btn) {
                            Ext.getCmp('search').getForm().reset();

                        }
                    },

                ]
            }
        ];
        this.callParent(arguments);
    }
})