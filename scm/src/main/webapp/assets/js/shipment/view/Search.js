/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.orderSearch',
    region: 'north',
    border: 0,
    //bodyPadding: 10,
    layout: {
        type: 'hbox',
        align: 'left'
    },
    height: 'auto',
    bodyPadding: 10,
    defaultType: 'fieldcontainer',
    defaults: {
        margin: '0 10 0 0',
        defaults: {
            xtype: 'combo',
            labelWidth: 40,
            width: 100,
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
                    {
                        xtype: 'combo',
                        width: 160,
                        labelWidth: 60,
                        id: 'shippingCp',
                        fieldLabel: '物流公司',
                        name: 'shippingComp',
                        emptyText: '请选择',
                        store: Espide.Common.expressStore(true)
                    },
                    {
                        name: 'dateType',
                        labelWidth: 60,
                        width: 160,
                        fieldLabel: '日期类型',
                        itemId: 'dateType',
                        id: 'dateType',
                        value: 'confirmedDate',
                        store: [
                            ['all', '全部'],
                            ['payDate', '付款日期'],
                            ['confirmedDate', '审单日期'],
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
                                    case '审单日期':
                                        Ext.getCmp('appOrderStatus').setValue("CONFIRMED");
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
                            ['CONFIRMED', '审单日期'],
                            ['INVOICED', '发货日期'],
                            ['EXAMINED', '验货日期'],
                            ['SIGNED', '签收日期'],
                        ]
                    },
                    {
                        name: 'outPlatformType',
                        id: 'outPlatformType',
                        fieldLabel: '平台类型',
                        value: 'null',
                        labelWidth: 60,
                        width: 160,
                        valueField: 'type',
                        displayField: 'name',
                        editable: false,
                        emptyText: '请选择',
                        store: platform,
                    },

                ]
            },
            {
                items: [
                    {
                        xtype: 'combo',
                        labelWidth: 100,
                        fieldLabel: '显示订单条数',
                        editable: false,
                        value: 50,
                        width: 215,
                        name: 'limit',
                        id: 'limit',
                        store: [
                            [50, 50],
                            [100, 100],
                            [200, 200],
                            [300, 300],
                        ]
                    },
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '开始日期',
                        value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                        //disabled: true,
                        name: 'startDate',
                        id: 'startDate',
                        itemId: 'startDate',
                        format: 'Y-m-d H:i:s',
                        margin: '0 5 0 0',
                        labelWidth: 60,
                        width: 215
                    }),


                ]
            },

            {
                items: [

                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        combineErrors: true,
                        msgTarget: 'side',
                        fieldLabel: '条件1',
                        labelWidth: 40,
                        width: 390,
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
                                value: 'platformOrderNo',
                                valueField: 'id',
                                displayField: 'value',
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
                            {xtype: 'textfield', name: 'conditionValue', emptyText: '输入关键字', width: 135, margin: '0 10 0 0'}
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'col4',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 390,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
                            margin: '0 5 5 0'
                        },
                        items: [
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '结束日期',
                                name: 'endDate',
                                //disabled: true,
                                itemId: 'endDate',
                                id: 'endDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 215
                            }),
                            {
                                fieldLabel: '库房',
                                emptyText: '请选择',
                                labelWidth: 35,
                                width: 160,
                                name: 'repoId',
                                valueField: 'id',
                                displayField: 'name',
                                queryMode: 'remote',
                                store: 'StorageAll'
                            }
                        ]

                    },
/*                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'col5',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 390,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
                            margin: '0 5 5 0'
                        },
                        items: [

                        ]

                    },*/
                ]

            },
            {
                fieldLabel: '订单状态', value: 'CONFIRMED', name: 'orderStatus', id: 'orderState', hidden: true,
                queryMode: 'local',
                triggerAction: 'all',
                forceSelection: true,
                xtype: 'combo',
                ///store: Espide.Common.orderState.getStore(true)
                store: [
                    ["all", "全部"],
                    ["WAIT_APPROVE", "待审核"],
                    [ "INVALID", "已作废"],
                    ["WAIT_PROCESS", "待处理"],
                    ["CONFIRMED", "已确认"],
                    [ "PRINTED", "已打印"],
                    [ "EXAMINED", "已验货"],
                    ["INVOICED", "已发货"],
                    ["SIGNED", "已签收"],
                ]
            },
            {
                items: [
                    {
                        xtype: 'button',
                        text: '查询',
                        itemId: 'confirmBtn',
                        margin: '8 0 0 0',
                        width: 55,
                        height: 35,
                    }
                ]
            }
        ];
        this.tbar = {
            ui: 'footer',
            items: [
                { text: '已作废', itemId: 'INVALID', belong: 'mainBtn'},
                '--》',
                { text: '待处理', itemId: 'CONFIRMED', belong: 'mainBtn', disabled: true},
                '--》',
                { text: '已打印', itemId: 'PRINTED', belong: 'mainBtn'},
                '--》',
                { text: '已验货', itemId: 'EXAMINED', belong: 'mainBtn'},
                '--》',
                { text: '已发货', itemId: 'INVOICED', belong: 'mainBtn'},
                '--》',
                { text: '签收', itemId: 'SIGNED', belong: 'mainBtn'},
                '--》',
                { text: '全部', itemId: 'all', belong: 'mainBtn'}
            ]
        };

        this.callParent(arguments);
    }
})