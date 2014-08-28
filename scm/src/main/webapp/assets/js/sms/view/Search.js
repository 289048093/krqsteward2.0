/*
 * Created by king on 13-12-17
 */

Ext.define('Sms.view.Search', {
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
                        width: 640,
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
                                name: 'type',
                                fieldLabel: '类别',
                                width: 150,
                                labelWidth: 45,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: 'true',
                                emptyText: '请选择',
                                store: [
                                    ['0', '手动营销短信'],
                                    ['1', '手动其他短信'],
                                    ['2', '付款关怀短信'],
                                    ['3', '拆分发货提醒短信'],
                                    ['4', '发货提醒短信'],
                                    ['5', '到达提醒短信'],
                                    ['6', '签收提醒短信'],
                                    ['7', '会员升级提醒短信'],
                                ],
                            },
                            {
                                xtype: 'combo',
                                name: 'status',
                                fieldLabel: '发送状态',
                                width: 150,
                                labelWidth: 60,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: 'true',
                                emptyText: '请选择',
                                store: [
                                    ['0', '等待发送'],
                                    ['1', '已发送'],
                                    ['2', '发送失败'],
                                    ['3', '已取消'],
                                    ['4', '已发送'],
                                ],
                            },
                            {
                                xtype: 'combo',
                                name: 'result',
                                fieldLabel: '接收状态',
                                width: 150,
                                labelWidth: 60,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: 'true',
                                emptyText: '请选择',
                                store: [
                                    ['0', '接收成功'],
                                    ['1', '正在接收中'],
                                    ['2', '接收失败'],
                                ],
                            },
                            {
                                xtype:'textfield',
                                name: 'module',
                                id: 'module',
                                labelWidth: 60,
                                width:150,
                                fieldLabel: '模块名称',
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
                        width: 640,
                        defaults: {
                            xtype: 'combo',
                            queryMode: 'local',
                            triggerAction: 'all',
                            forceSelection: true,
                            editable: false,
                            margin: '0 5 0 0'
                        },
                        items: [

                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '发送时间',
                                name: 'birthdayStartDate',
                                id: 'birthdayStartDate',
                                itemId: 'birthdayStartDate',
                                format: 'Y-m-d',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 170
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '至',
                                name: 'birthdayEndDate',
                                id: 'birthdayEndDate',
                                format: 'Y-m-d',
                                margin: '0 5 0 0',
                                labelWidth: 10,
                                width: 120
                            }),


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