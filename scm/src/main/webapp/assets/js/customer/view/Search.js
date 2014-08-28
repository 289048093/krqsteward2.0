/*
 * Created by king on 13-12-17
 */

Ext.define('Customer.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.CustomerSearch',
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
                        width: 940,
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
                                name: 'platformType',
                                id: 'platformType',
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
                                store:Ext.create('Ext.data.Store', {
                                    fields: ['id', 'nick'],
                                    proxy: {
                                        type: 'ajax',
                                        url: '/order/shopList',
                                        reader: {
                                            type: 'json',
                                            root: 'data.list'
                                        },
                                        listeners: {
                                            exception: function(proxy, response, operation){
                                                var data = Ext.decode(response.responseText);
                                                Ext.MessageBox.show({
                                                    title: '警告',
                                                    msg: data.msg,
                                                    icon: Ext.MessageBox.ERROR,
                                                    buttons: Ext.Msg.OK
                                                });
                                            }
                                        }
                                    },
                                    autoLoad: false,
                                    listeners: {
                                        load: function (){
                                            this.insert(0, { nick: '全部店铺', id: null });
                                        }
                                    }
                                }),
                            },
                            {

                                xtype:'numberfield',
                                fieldLabel: '会员等级',
                                labelWidth: 65,
                                width: 160,
                                editable:true,
                                allowDecimals:false,
                                minValue:0,
                                name: 'grade',
                            },
                            {
                                xtype:'textfield',
                                fieldLabel: '地址',
                                emptyText: '请选择',
                                labelWidth: 35,
                                width: 160,
                                name: 'address',
                            },
                            {
                                xtype:'numberfield',
                                fieldLabel: '交易次数',
                                labelWidth: 65,
                                editable:true,
                                allowDecimals:false,
                                minValue:0,
                                width: 160,
                                name: 'tradeCount',
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
                        width: 940,
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
                                xtype: 'textfield',
                                width: 160,
                                labelWidth: 60,
                                id: 'prodNo',
                                fieldLabel: '商品编号',
                                name: 'prodNo',
                            },

                            {
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: 'sku',
                                width: 215,
                                name: 'sku',
                            },
                            {
                                xtype:'textfield',
                                fieldLabel: '会员名称',
                                labelWidth: 65,
                                width: 160,
                                name: 'buyerId',
                            },
                            {
                                fieldLabel: '标签',
                                emptyText: '请选择',
                                labelWidth: 35,
                                width: 160,
                                name: 'tags',
                                valueField: 'id',
                                displayField: 'name',
                                queryMode: 'remote',
                                store:Ext.create('Ext.data.Store', {
                                    fields: ['id', 'name'],
                                    proxy: {
                                        type: 'ajax',
                                        url: '/customerTag/list',
                                        reader: {
                                            type: 'json',
                                            root: 'data.obj.result'
                                        },
                                        listeners: {
                                            exception: function(proxy, response, operation){
                                                var data = Ext.decode(response.responseText);
                                                Ext.MessageBox.show({
                                                    title: '警告',
                                                    msg: data.msg,
                                                    icon: Ext.MessageBox.ERROR,
                                                    buttons: Ext.Msg.OK
                                                });
                                            }
                                        }
                                    },
                                    autoLoad: false,
                                    pageSize: 200000,
                                    listeners: {
                                        load: function (){
                                            this.insert(0, { name: '全部', id: null });
                                        }
                                    }
                                }),
                            }
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        id: 'row2',
                        combineErrors: true,
                        msgTarget: 'side',
                        labelWidth: 60,
                        width: 960,
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
                                fieldLabel: '最近交易时间',
                                value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                                name: 'startDate',
                                id: 'startDate',
                                itemId: 'startDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 85,
                                width: 215
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '至',
                                value: new Date(new Date().getTime()),
                                name: 'endDate',
                                id: 'endDate',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 10,
                                width: 160
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '生日',
                                //value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                                name: 'birthdayStartDate',
                                id: 'birthdayStartDate',
                                itemId: 'birthdayStartDate',
                                format: 'Y-m-d',
                                margin: '0 5 0 0',
                                labelWidth: 65,
                                width: 170
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '至',
                                //value: new Date(new Date().getTime()),
                                //disabled: true,
                                name: 'birthdayEndDate',
                                id: 'birthdayEndDate',
                                format: 'Y-m-d',
                                margin: '0 5 0 0',
                                labelWidth: 10,
                                width: 120
                            }),
                            {
                                xtype:'numberfield',
                                fieldLabel: '累计购买金额',
                                editable:true,
                                labelWidth: 85,
                                allowDecimals:false,
                                minValue:0,
                                width: 160,
                                name: 'totalTradeFeeStart',
                            },
                            {
                                xtype:'numberfield',
                                fieldLabel: '至',
                                allowDecimals:false,
                                editable:true,
                                minValue:0,
                                labelWidth: 15,
                                width: 110,
                                name: 'totalTradeFeeEnd',
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

                ]
            }
        ];

        this.callParent(arguments);
    }
})