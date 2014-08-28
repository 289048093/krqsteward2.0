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
                        width: 1240,
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
                                value: 'payDate',
                                store: [
                                    ['all', '全部'],
                                    ['payDate', '付款日期'],
                                    ['printDate', '打印日期'],
                                    ['examinedDate', '验货日期'],
                                    ['deliveryDate', '发货日期'],
                                    ['orderDate', '下单日期'],
                                    ['receiptDate', '签收日期']
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
                                itemId: 'startDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 215
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '结束日期',
                                name: 'endDate',
                                itemId: 'endDate',
                                id: 'endDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 215
                            }),
                            {
                                name: 'orderStatus',
                                labelWidth:60,
                                width:200,
                                id: 'orderStatus',
                                fieldLabel: '订单状态',
                                value: 'null',
                                hidden: false,
                                store: Espide.Common.orderState.getStore()
                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'hbox',
                                combineErrors: true,
                                msgTarget: 'side',
                                fieldLabel: '条件1',
                                labelWidth: 40,
                                width: 400,
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
                                        value: 'buyerId',
                                        valueField: 'id',
                                        displayField: 'value',
                                        value: 'platformOrderNo',
                                        store: Ext.create('Ext.data.Store', {
                                            fields: ['id', 'value', 'type'],
                                            data: [
                                                {id: 'buyerId', value: '买家id', type: 'string'},
                                                {id: 'buyerAlipayNo', value: '支付宝账号', type: 'string'},
                                                {id: 'receiverName', value: '收货人', type: 'string'},
                                                {id: 'buyerMessage', value: '买家留言', type: 'string'},
                                                {id: 'remark', value: '卖家备注', type: 'string'},
                                                {id: 'orderNo', value: '智库城订单编号', type: 'string'},
                                                {id: 'platformOrderNo', value: '外部平台订单编号', type: 'string'},
                                                {id: 'prodCode', value: '商品编号', type: 'string'},
                                                {id: 'shippingNo', value: '快递单号', type: 'string'},
                                                {id: 'actualFee', value: '成交金额', type: 'number'},
                                                {id: 'receiverMobile', value: '收货手机', type: 'string'},
                                                {id: 'prodName', value: '商品名称', type: 'string'},
                                                {id: 'receiverAddress', value: '收货地址', type: 'string'},
                                                {id: 'offlineRemark', value: '线下备注', type: 'string'},
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
                                    {xtype: 'textfield', name: 'conditionValue', width: 145, margin: '0 5 0 0'}
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
                        width: 1240,
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
                                fieldLabel: '库房',
                                emptyText: '请选择',
                                labelWidth: 35,
                                width: 190,
                                name: 'repoId',
                                valueField: 'id',
                                displayField: 'name',
                                queryMode: 'remote',
                                store: 'StorageAll'
                            },
                            {
                                name: 'outPlatformType',
                                id: 'outPlatformType',
                                fieldLabel: '平台类型',
                                value: 'null',
                                labelWidth: 60,
                                width: 215,
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
                                width: 200,
                                queryMode: 'remote',
                                store: 'Brand'

                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'hbox',
                                combineErrors: true,
                                msgTarget: 'side',
                                fieldLabel: '条件2',
                                labelWidth: 40,
                                width: 400,
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
                                        name: 'conditionQuery2',
                                        itemId: 'querySelect2',
                                        value: 'buyerId',
                                        valueField: 'id',
                                        displayField: 'value',
                                        value: 'orderNo',
                                        store: Ext.create('Ext.data.Store', {
                                            fields: ['id', 'value', 'type'],
                                            data: [
                                                {id: 'buyerId', value: '买家id', type: 'string'},
                                                {id: 'buyerAlipayNo', value: '支付宝账号', type: 'string'},
                                                {id: 'receiverName', value: '收货人', type: 'string'},
                                                {id: 'buyerMessage', value: '买家留言', type: 'string'},
                                                {id: 'remark', value: '卖家备注', type: 'string'},
                                                {id: 'orderNo', value: '智库城订单编号', type: 'string'},
                                                {id: 'platformOrderNo', value: '外部平台订单编号', type: 'string'},
                                                {id: 'prodCode', value: '商品编号', type: 'string'},
                                                {id: 'shippingNo', value: '快递单号', type: 'string'},
                                                {id: 'actualFee', value: '成交金额', type: 'number'},
                                                {id: 'receiverMobile', value: '收货手机', type: 'string'},
                                                {id: 'prodName', value: '商品名称', type: 'string'},
                                                {id: 'receiverAddress', value: '收货地址', type: 'string'},
                                                {id: 'offlineRemark', value: '线下备注', type: 'string'},
                                            ]
                                        })
                                    },
                                    {
                                        name: 'conditionType2',
                                        value: 'has',
                                        itemId: 'queryType2',
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
                                    {xtype: 'textfield', name: 'conditionValue2', width: 145, margin: '0 5 0 0'}
                                ]
                            },
                        ]
                    },
                    /***  搜索表单第三，四行    ***/
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        disabled:true,
                        id: 'row2',
                        hidden: true,
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 1240,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
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
                        items: [
                            {
                                xtype: 'fieldcontainer',
                                layout: 'vbox',
                                id: 'col1',
                                combineErrors: true,
                                msgTarget: 'side',
                                labelWidth: 90,
                                width: 190,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    labelWidth: 90,
                                    width: 190,
                                    margin: '0 0 5 0'
                                },
                                items: [
                                    {
                                        name: 'orderReturnStatus',
                                        id: 'orderReturnStatus',
                                        fieldLabel: '订单退货状态',
                                        hidden: false,
                                        itemId: 'orderReturnStatus',
                                        value: 'null',
                                        store: [
                                            ['null', '全部'],
                                            ['NORMAL', '正常'],
                                            ['PART_RETURN', '部分退货'],
                                            ['RETURNED', '已退货'],
                                        ]
                                    },
                                    {
                                        name: 'returnPostPayer',
                                        id: 'returnPostPayer',
                                        fieldLabel: '线上退货邮费承担方',
                                        hidden: false,
                                        itemId: 'returnPostPayer',
                                        value: 'null',
                                        labelWidth: 125,
                                        store: [
                                            ['null', '全部'],
                                            ['SELLER', '商家'],
                                            ['BUYER', '顾客'],
                                            ['SUPPLIER', '供应商'],
                                        ]
                                    },
                                ]

                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'vbox',
                                id: 'col2',
                                combineErrors: true,
                                msgTarget: 'side',
                                labelWidth: 60,
                                width: 215,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    labelWidth: 60,
                                    width: 215,
                                    margin: '0 5 5 0'
                                },
                                items: [

                                    {
                                        fieldLabel: '快递公司',
                                        name: 'shippingComp',
                                        emptyText: '请选择',
                                        valueField: 'shopId',
                                        displayField: 'title',
                                        labelWidth: 60,
                                        width: 215,
                                        store: Espide.Common.expressStore(true)
                                    },
                                    {
                                        name: 'offlineReturnPostPayer',
                                        id: 'offlineReturnPostPayer',
                                        fieldLabel: '线下退货邮费承担方',
                                        itemId: 'offlineReturnPostPayer',
                                        id: 'offlineReturnPostPayer',
                                        value: 'null',
                                        hidden: false,
                                        labelWidth: 125,
                                        store: [
                                            ['null', '全部'],
                                            ['SELLER', '商家'],
                                            ['BUYER', '顾客'],
                                            ['SUPPLIER', '供应商'],
                                        ]
                                    },
                                ]

                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'vbox',
                                id: 'col3',
                                combineErrors: true,
                                msgTarget: 'side',
                                labelWidth: 60,
                                width: 215,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    margin: '0 5 5 0'
                                },
                                items: [
                                    {
                                        name: 'orderItemReturnStatus',
                                        id: 'orderItemReturnStatus',
                                        fieldLabel: '线上退货状态',
                                        value: 'null',
                                        hidden: false,
                                        labelWidth: 90,
                                        width: 215,
                                        store: [
                                            ['null', '全部'],
                                            ['NORMAL', '正常'],
                                            ['RETURNED', '已退货'],
                                        ]
                                    },
                                    {
                                        name: 'exchangePostPayer',
                                        id: 'exchangePostPayer',
                                        fieldLabel: '线下换货邮费承担方',
                                        itemId: 'exchangePostPayer',
                                        value: 'null',
                                        hidden: false,
                                        labelWidth: 125,
                                        width: 215,
                                        store: [
                                            ['null', '全部'],
                                            ['SELLER', '商家'],
                                            ['BUYER', '顾客'],
                                            ['SUPPLIER', '供应商'],
                                        ]
                                    },
                                ]

                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'vbox',
                                id: 'col4',
                                combineErrors: true,
                                msgTarget: 'side',
                                labelWidth: 60,
                                width: 200,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    margin: '0 5 5 0'
                                },
                                items: [
                                    {
                                        name: 'offlineOrderItemReturnStatus',
                                        id: 'offlineOrderItemReturnStatus',
                                        fieldLabel: '线下退货状态',
                                        value: 'null',
                                        hidden: false,
                                        labelWidth: 90,
                                        width: 200,
                                        store: [
                                            ['null', '全部'],
                                            ['NORMAL', '正常'],
                                            ['RETURNED', '已退货'],
                                        ]
                                    },
                                    {
                                        name: 'orderItemType',
                                        id: 'orderItemType',
                                        fieldLabel: '订单项类型',
                                        itemId: 'orderItemType',
                                        hidden: false,
                                        value: 'null',
                                        labelWidth: 70,
                                        width: 200,
                                        store: Espide.Common.orderItemType.getStore(true)
                                    },
                                ]

                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'vbox',
                                id: 'col5',
                                combineErrors: true,
                                msgTarget: 'side',
                                labelWidth: 60,
                                width: 165,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    margin: '0 5 5 0'
                                },
                                items: [
                                    {
                                        name: 'generateType',
                                        id: 'generateType',
                                        fieldLabel: '生成类型',
                                        itemId: 'generateType',
                                        labelWidth: 60,
                                        width: 165,
                                        value: 'null',
                                        store: [
                                            ['null', '全部'],
                                            ['MANUAL_CREATE', '手动创建'],
                                            ['MANUAL_MERGE', '手动合并'],
                                            ['AUTO_CREATE', '自动抓单'],
                                            ['AUTO_SPLIT', '自动拆分'],
                                            ['AUTO_MERGE', '自动合并'],
                                        ]
                                    },
                                    {
                                        name: 'orderType',
                                        fieldLabel: '订单类型',
                                        value: 'null',
                                        width: 165,
                                        labelWidth: 60,
                                        store: Espide.Common.orderType.getStore(true)
                                    },

                                ]

                            },
                            {
                                xtype: 'fieldcontainer',
                                layout: 'vbox',
                                id: 'col6',
                                combineErrors: true,
                                msgTarget: 'side',
                                labelWidth: 60,
                                width: 220,
                                defaults: {
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    margin: '0 5 5 0'
                                },
                                items: [
                                    {
                                        name: 'refunding',
                                        id: 'refunding',
                                        fieldLabel: '退款状态',
                                        itemId: 'refunding',
                                        hidden: false,
                                        value: 'null',
                                        labelWidth: 70,
                                        width: 220,
                                        store: [
                                            ['null', '全部'],
                                            ['true', '正在申请'],
                                            ['false', '正常'],
                                        ]
                                    },
                                    {
                                        labelWidth: 100,
                                        fieldLabel: '显示订单条数',
                                        editable: false,
                                        value: 30,
                                        width: 220,
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
                    {
                        xtype: 'checkbox',
                        name: 'alsoReturn',
                        labelWidth: 60,
                        itemId: 'createChangeBtn',
                        id: 'createChangeBtn',
                        labelAlign: 'left',
                        inputValue: true,
                        fieldLabel: '高级查询'
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
})