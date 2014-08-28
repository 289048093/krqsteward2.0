/*
 * Created by king on 13-12-17
 */

Ext.define('Returnvisit.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.SmsSearch',
    itemId: 'SmsSearch',
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

                /***  搜索表单第一行    ***/
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'row0',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 1040,
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
                                xtype: 'combo',
                                name: 'status',
                                fieldLabel: '回访状态',
                                width: 150,
                                labelWidth: 60,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: '',
                                emptyText: '请选择',
                                store: [
                                    ['', '全部'],
                                    ['UNASSIGNED', '待分配'],
                                    ['ASSIGNED', '待回访'],
                                    ['SUCCEED', '回访成功'],
                                    ['FAILED', '回访失败'],
                                    ['COMPLETED', '回访结束'],
                                    ['APPOINTMENT', '已预约'],
                                    ['REJECTED', '拒访'],
                                ],
                                listeners:{
                                    select: function (combo, record, index) {
                                        switch (combo.rawValue) {
                                            case '待分配':
                                                Ext.getCmp('lastVisitEndTime').disable();
                                                Ext.getCmp('lastVisitBeginTime').disable();
                                                break;
                                            case '待回访':
                                                Ext.getCmp('lastVisitEndTime').disable();
                                                Ext.getCmp('lastVisitBeginTime').disable();
                                                break;
                                            default :
                                                Ext.getCmp('lastVisitEndTime').enable();
                                                Ext.getCmp('lastVisitBeginTime').enable();
                                        }
                                    }
                                }
                            },
                            {
                                xtype:'textfield',
                                name: 'visitorRealname',
                                id: 'visitorRealname',
                                labelWidth: 60,
                                width:150,
                                fieldLabel: '回访人',
                            },
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '最近回访时间',
                                value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                                name: 'lastVisitBeginTime',
                                id: 'lastVisitBeginTime',
                                itemId: 'lastVisitBeginTime',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 90,
                                width: 170
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '至',
                                name: 'lastVisitEndTime',
                                id: 'lastVisitEndTime',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 10,
                                width: 120
                            }),
                            {
                                xtype: 'combo',
                                name: 'used',
                                fieldLabel: '商品是否使用',
                                width: 150,
                                labelWidth: 90,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: '',
                                emptyText: '请选择',
                                store: [
                                    ['', '全部'],
                                    ['true', '是'],
                                    ['false', '否'],
                                ],
                            },
                            {
                                xtype: 'combo',
                                name: 'redirectAfterSale',
                                fieldLabel: '是否转入售后',
                                width: 150,
                                labelWidth: 90,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: '',
                                emptyText: '请选择',
                                store: [
                                    ['', '全部'],
                                    ['true', '是'],
                                    ['false', '否'],
                                ],
                            },



                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'row1',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 1040,
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
                                name: 'brandId',
                                fieldLabel: '品牌',
                                value: 30,
                                labelWidth: 60,
                                emptyText: '请选择',
                                valueField: 'id',
                                displayField: 'name',
                                width: 200,
                                queryMode: 'remote',
                                //store: 'Brand'
                            },
                            {
                                fieldLabel: '店铺',
                                emptyText: '请选择',
                                valueField: 'id',
                                displayField: 'nick',
                                labelWidth: 60,
                                width: 215,
                                id: 'shopId',
                                queryMode: 'remote',
                                name: 'shopId',
                                //store: 'ShopAll'
                            },
                            {
                                name: 'platformType',
                                id: 'platformType',
                                fieldLabel: '平台类型',
                                value: 'null',
                                labelWidth: 60,
                                width: 215,
                                valueField: 'type',
                                displayField: 'name',
                                editable: false,
                                emptyText: '请选择',
                                //store: platform,
                            },
                            {
                                xtype:'textfield',
                                name: 'receiverMobile',
                                id: 'receiverMobile',
                                labelWidth: 60,
                                width:150,
                                fieldLabel: '收货手机',
                            },
                            {
                                xtype: 'combo',
                                name: 'type',
                                fieldLabel: '回访单类型',
                                width: 150,
                                labelWidth: 90,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: '',
                                emptyText: '请选择',
                                store: [
                                    ['', '全部'],
                                    ['SIGNED', '签收回访'],
                                    ['AFTER_SALE', '售后回访'],
                                    ['NEGATIVE_COMMENT', '差评回访'],
                                ],
                            },
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'row2',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 1040,
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
                                xtype:'textfield',
                                name: 'buyerId',
                                id: 'buyerId',
                                labelWidth: 80,
                                width:150,
                                fieldLabel: '会员名',
                            },
                            {
                                xtype:'textfield',
                                name: 'receiverName',
                                id: 'receiverName',
                                labelWidth: 80,
                                width:150,
                                fieldLabel: '收货人',
                            },
                            {
                                xtype:'textfield',
                                name: 'receiverPhone',
                                id: 'receiverPhone',
                                labelWidth: 80,
                                width:150,
                                fieldLabel: '收货电话',
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