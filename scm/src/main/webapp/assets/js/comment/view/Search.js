/*
 * Created by king on 13-12-17
 */

Ext.define('Comment.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.CommentSearch',
    itemId: 'CommentSearch',
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
                        width: 1100,
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
                                labelWidth: 60,
                                width:150,
                                id: 'buyerId',
                                fieldLabel: '会员名称',
                            },

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
                                width: 160,
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
                                xtype:'textfield',
                                name: 'sku',
                                id: 'sku',
                                labelWidth: 30,
                                width:150,
                                fieldLabel: 'sku',
                            },
                            {
                                xtype: 'combo',
                                name: 'isCategory',
                                fieldLabel: '是否已分类',
                                width: 150,
                                labelWidth: 80,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: '',
                                emptyText: '请选择',
                                store: [
                                    ['', '全部'],
                                    ['true', '是'],
                                    ['false', '否']
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
                        width: 840,
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
                                name: 'categoryId',
                                fieldLabel: '评价类型',
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
                                    ['1', '物流'],
                                    ['2', '商品'],
                                    ['3', '售前'],
                                    ['4', '售后'],
                                    ['5', '使用问题'],
                                    ['6', '其他'],
                                ],
                            },
                            {
                                xtype: 'combo',
                                name: 'result',
                                fieldLabel: '评价结果',
                                width: 150,
                                labelWidth: 60,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: 'true',
                                emptyText: '请选择',
                                store: [
                                    ['', '全部'],
                                    ['GOOD', '好评'],
                                    ['NEUTRAL', '中评'],
                                    ['BAD', '差评'],
                                ],
                            },
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '评价时间',
                                //value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                                name: 'commentTimeStart',
                                id: 'commentTimeStart',
                                itemId: 'commentTimeStart',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 60,
                                width: 170
                            }),
                            Ext.create('Go.form.field.DateTime', {
                                fieldLabel: '至',
                                //value: new Date(new Date().getTime()),
                                //disabled: true,
                                name: 'commentTimeEnd',
                                id: 'commentTimeEnd',
                                format: 'Y-m-d H:i:s',
                                margin: '0 5 0 0',
                                labelWidth: 10,
                                width: 120
                            }),
                            {
                                xtype:'textfield',
                                name: 'content',
                                id: 'content',
                                labelWidth: 85,
                                width:150,
                                fieldLabel: '评价内容',
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