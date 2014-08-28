/**
 * author     : 梦龙
 * createTime : 14-5-8 下午2:55
 * description:
 */



Ext.define('Refund.view.win.CreateForm', {
    extend: 'Ext.form.Panel',
    id: 'createForm',
    region: 'south',
    alias: 'widget.createForm',
    bodyPadding: 10,
    height: 260,
    layout: {
        type: 'vbox',
        align: 'left'
    },
    forceFit: false,
    height: 'auto',
    width: 'auto',
    border: 0,
    defaultType: 'fieldcontainer',
    defaults: {
        margin: '0 0 10 0',
        layout: {
            type: 'hbox',
            align: 'left'
        },
        defaults: {
            xtype: 'textfield',
            margin: '0 10 0 0',
            labelWidth: 90,
            width: 200,
            allowBlank:false,
            queryMode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            editable: false
        }
    },
    initComponent: function () {
        this.items = [
            {
                items: [
                    {
                        name: 'refundFee',
                        fieldLabel: '账面退款金额',
                        xtype: 'numberfield',
                        minValue:0,
                        width:150,
                        editable: true,
                        value:0.00
                    },

                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '退款时间',
                        //value: new Date(),
                        name: 'refundTime',
                        itemId: 'refundTime',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        emptyText:'请选择',
                        labelWidth: 60,
                        width: 180
                    }),
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '回访时间',
                        //value: new Date(),
                        name: 'revisitTime',
                        emptyText:'请选择',
                        itemId: 'revisitTime',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        labelWidth: 60,
                        width: 180
                    }),
                    {
                        xtype: 'combo',
                        fieldLabel: '退款状态',
                        name: 'status',
                        id:'status',
                        labelWidth: 60,
                        width: 175,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        value: 'IN_PROCESS',
                        emptyText: '请选择',
                        store: [
                            ['IN_PROCESS', '正在申请退款'],
                            ['SUCCESS', '退款成功']
                        ],
                    },
                    {
                        name: 'reason',
                        fieldLabel: '退款原因',
                        emptyText:'请输入退款原因',
                        labelWidth: 60,
                        width: 220
                    },
                    {
                        name: 'remark',
                        fieldLabel: '备注',
                        allowBlank:true,
                        emptyText:'请输入备注',
                        labelWidth: 40,
                        width: 220
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'buyerAlipayNo',
                        id: 'buyerAlipayNo',
                        fieldLabel: '支付宝账号',
                        emptyText:'请输入支付宝账号',
                        allowBlank:true,
                        labelWidth: 80,
                        width: 220
                    }
                ]
            },
            {
                items: [
                    {
                        xtype: 'checkbox',
                        name: 'alsoReturn',
                        labelWidth: 90,
                        itemId:'createChangeBtn',
                        id:'createChangeBtn',
                        labelAlign: 'left',
                        inputValue:true,
                        fieldLabel: '是否同时退货'
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'actualRefundFee',
                        fieldLabel: '实际退款金额',
                        xtype: 'numberfield',
                        id:'ctreateActualRefundFee',
                        minValue:0,
                        disabled:true,
                        hidden:true,
                        editable:true,
                        value:0.00
                    },
                    {
                        name: 'postFee',
                        fieldLabel: '线下退货邮费',
                        id:'ctreatePostFee',
                        xtype: 'numberfield',
                        minValue:0,
                        disabled:true,
                        editable:true,
                        hidden:true,
                        value:0.00
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: '线下退货邮费承担方',
                        name: 'postPayer',
                        id:'createPostPayer',
                        labelWidth: 130,
                        width: 240,
                        disabled:true,
                        hidden:true,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        value: 'SUPPLIER',
                        emptyText: '请选择',
                        store: [
                            ['BUYER', '顾客'],
                            ['SELLER', '平台商'],
                            ['SUPPLIER', '品牌商']
                        ],
                    },
                    {
                        name: 'shippingNo',
                        labelWidth: 80,
                        width:250,
                        fieldLabel: '快递单号',
                        emptyText:'请输入快递单号',
                        id:'shippingNo',
                        xtype: 'textfield',
                        allowBlank:true,
                        minValue:0,
                        disabled:true,
                        hidden:true,
                        editable:true,
                    },
                    {
                        fieldLabel: '快递公司',
                        xtype:'combo',
                        name: 'shippingComp',
                        id: 'shippingComp',
                        allowBlank:true,
                        disabled:true,
                        hidden:true,
                        emptyText: '请选择',
                        valueField: 'name',
                        displayField: 'title',
                        labelWidth: 60,
                        width: 180,
                        store: Espide.Common.expressStore()
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
})