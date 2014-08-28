/**
 * author     : 梦龙
 * createTime : 14-5-9 上午9:01
 * description:
 */


Ext.define('Payment.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.paymentSearch',
    region: 'north',
    border: 0,
    split: true,
    bodyPadding: 10,
    layout: {
        type: 'hbox',
        align: 'left'
    },
    height: 'auto',
    defaultType: 'fieldcontainer',
    defaults: {
        margin: '0 10 10 0',
        layout: {
            type: 'vbox',
            align: 'left'
        },
        defaults: {
            xtype: 'combo',
            //margin: '0 0 10 0',
            labelWidth: 60,
            width: 150,
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
                        name: 'dateType',
                        fieldLabel: '日期类型',
                        itemId: 'dateType',
                        id: 'dateType',
                        value: 'payDate',
                        store: [
                            ['all', '全部'],
                            ['payDate', '付款日期'],
                            ['createDate', '创建日期'],
                            ['orderDate', '下单日期'],
                        ],
                        listeners: {
                            select: function (combo, record, index) {
                                switch (combo.rawValue) {
                                    case '打印日期':
                                        Ext.getCmp('appOrderStatus').setValue("PRINTED");
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
                        fieldLabel: '拆分状态',
                        itemId: 'appOrderStatus',
                        value: 'all',
                        hidden: true,
                        store: [
                            ['all', '全部'],
                            ['PRINTED', '打印日期'],
                            ['INVOICED', '发货日期'],
                            ['SIGNED', '签收日期'],
                        ]
                    },
                    {
                        name: 'type',
                        labelWidth: 70,
                        fieldLabel: '预收款类型',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store:Espide.Common.paymentType.getStore(true)
                    },
                ]
            },
            {
                items: [
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '开始日期',
                        value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                        name: 'startDate',
                        itemId: 'startDate',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        labelWidth: 60,
                        width: 220
                    }),
                    {
                        name: 'allocateStatus',
                        labelWidth: 65,
                        width: 220,
                        fieldLabel: '分配状态',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store:Espide.Common.allocateStatus.getStore(true)
                    },
                ]
            },
            {
                items: [
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '结束日期',
                        name: 'endDate',
                        itemId: 'endDate',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        labelWidth: 60,
                        width: 220
                    }),
                    {
                        name: 'platformType',
                        labelWidth: 60,
                        width: 220,
                        fieldLabel: '平台类型',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        valueField: 'type',
                        displayField: 'name',
                        emptyText: '请选择',
                        store: platform,
                    },
                ]
            },
            {
                items: [
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        combineErrors: true,
                        msgTarget: 'side',
                        //fieldLabel: '条件1',
                        labelWidth: 60,
                        width:430,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
                            //hideLabel: true,
                            margin: '0 5 0 0'
                        },
                        items: [
//                            {
//
//                                name: 'brandId',
//                                fieldLabel: '品牌',
//                                value: 30,
//                                labelWidth: 60,
//                                emptyText: '请选择',
//                                valueField: 'id',
//                                displayField: 'name',
//                                width: 200,
//                                queryMode: 'remote',
//                                store: 'Brand'
//
//                            },
                            {
                                name: 'shopId',
                                labelWidth: 65,
                                width: 220,
                                fieldLabel: '店铺名称',
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                emptyText: '请选择',
                                valueField: 'id',
                                displayField: 'nick',
                                store:'ShopAll'
                            },
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        combineErrors: true,
                        msgTarget: 'side',
                        fieldLabel: '条件1',
                        labelWidth: 60,
                        width:430,
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
                                width: 135,
                                name: 'conditionQuery',
                                itemId: 'querySelect',
                                value: 'buyerId',
                                valueField: 'id',
                                displayField: 'value',
                                store: Ext.create('Ext.data.Store', {
                                    fields: ['id', 'value', 'type'],
                                    data: [
                                        {id: 'buyerId', value: '买家id', type: 'string'},
                                        {id: 'platformOrderNo', value: '外部平台订单编号', type: 'string'},
                                        {id: 'platformSubOrderNo', value: '外部平台订单项编号', type: 'string'},
//                                        {id: 'remark', value: '卖家备注', type: 'string'},
//                                        {id: 'orderNo', value: '订单编号', type: 'string'},
//                                        {id: 'outOrderNo', value: '原订单号', type: 'string'},
//                                        {id: 'prodCode', value: '产品编码', type: 'string'},
//                                        {id: 'shippingNo', value: '快递单号', type: 'string'},
//                                        {id: 'totalFee', value: '成交金额', type: 'number'},
//                                        {id: 'receiverMobile', value: '收货手机', type: 'string'},
//                                        {id: 'prodName', value: '商品名称', type: 'string'},
//                                        {id: 'brandName', value: '品牌名称', type: 'string'}
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
                            {xtype: 'textfield', name: 'conditionValue', width: 145, margin: '0 10 0 0'}
                        ]
                    }

                ]
            },
            {
                xtype: 'button',
                text: '查询',
                itemId: 'search',
                width: 55,
                height: 55
            }

        ]
        this.callParent(arguments);
    }
});