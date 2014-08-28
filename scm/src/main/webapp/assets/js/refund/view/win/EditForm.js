/**
 * author     : 梦龙
 * createTime : 14-5-8 下午2:55
 * description:
 */



Ext.define('Refund.view.win.EditForm', {
    extend: 'Ext.form.Panel',
    id: 'editForm',
    region: 'south',
    alias: 'widget.editForm',
    bodyPadding: 10,
    height: 260,
    layout: {
        type: 'vbox',
        align: 'left'
    },
    forceFit: false,
    height: 'auto',
    width:'auto',
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
            queryMode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            allowBlank:false,
            editable: false
        }
    },
    initComponent: function () {
        this.items = [
            {
                items: [
                    {
                        name: 'refundFee',
                        id: 'refundFee',
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
                        id: 'refundTime',
                        itemId: 'refundtime',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        labelWidth: 60,
                        width: 180
                    }),
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '回访时间',
                        //value: new Date(),
                        name: 'revisitTime',
                        id: 'revisitTime',
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
                            ['SUCCESS', '退款成功'],
                            ['CLOSED', '退款失败']
                        ],
                    },

                    {
                        name: 'reason',
                        id: 'reason',
                        fieldLabel: '退款原因',
                        labelWidth: 60,
                        width: 220
                    },
                    {
                        name: 'remark',
                        id: 'remark',
                        fieldLabel: '备注',
                        allowBlank:true,
                        labelWidth: 40,
                        width: 220
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'buyerAlipayNo',
                        id:'buyerAlipayNo',
                        fieldLabel: '支付宝账号',
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
                        id: 'alsoReturn',
                        itemId: 'alsoReturn',
                        labelWidth: 90,
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
                        id: 'actualRefundFee',
                        fieldLabel: '实际退款金额',
                        xtype: 'numberfield',
                        minValue:0,
                        editable: true,
                        value:0.00
                    },
                    {
                        name: 'postFee',
                        id: 'postFee',
                        fieldLabel: '退货邮费',
                        xtype: 'numberfield',
                        minValue:0,
                        editable: true,
                        value:0.00
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: '退货邮费承担方',
                        name: 'postPayer',
                        id: 'postPayer',
                        labelWidth: 130,
                        width: 240,
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
                        fieldLabel: '货运单号',
                        id:'shippingNo',
                        xtype: 'textfield',
                        minValue:0,
                        allowBlank:true,
                        editable:true,
                    },
                    {
                        fieldLabel: '快递公司',
                        xtype:'combo',
                        name: 'shippingComp',
                        id: 'shippingComp',
                        emptyText: '请选择',
                        valueField: 'name',
                        displayField: 'title',
                        labelWidth: 60,
                        allowBlank:true,
                        width: 180,
                        store: Espide.Common.expressStore()
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
})