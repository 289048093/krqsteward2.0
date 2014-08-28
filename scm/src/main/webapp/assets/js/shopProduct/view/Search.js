/*
 * Created by king on 13-12-17
 */

Ext.define('ShopProduct.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.ShopProductSearch',
    itemId: 'ShopProductSearch',
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
        var platform = Espide.Common.createComboStore('/np/platform/list',false,['id','name','type']);
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
                                xtype:'textfield',
                                name: 'productName',
                                labelWidth: 60,
                                width:150,
                                id: 'productName',
                                fieldLabel: '商品名称',
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
                                xtype:'textfield',
                                name: 'productNo',
                                id: 'productNo',
                                labelWidth: 60,
                                width:150,
                                fieldLabel: '商家编码',
                            },
                            {
                                xtype: 'combo',
                                name: 'Putaway',
                                fieldLabel: '是否上架',
                                width: 130,
                                labelWidth: 60,
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
                            {
                                name: 'platformType',
                                id: 'platformType',
                                fieldLabel: '平台类型',
                                labelWidth: 60,
                                width: 160,
                                value: 'TAO_BAO',
                                valueField: 'type',
                                displayField: 'name',
                                editable: false,
                                emptyText: '请选择',
                                store: platform,
                                listeners:{
                                    select:function(){
                                        var shopId=Ext.getCmp("shopId");
                                        shopId.enable();
                                        //shopId.reset();
                                        shopId.getStore().proxy.url = '/order/shopList?platformType='+ this.getValue();
                                        shopId.getStore().load();
                                    }
                                }
                            },
                            {
                                fieldLabel: '店铺名称',
                                //disabled:true,
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
                                        url: '/order/shopList?platformType=TAO_BAO',
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
                                }),
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
                                xtype: 'combo',
                                name: 'synStatus',
                                fieldLabel: '是否自动同步',
                                width: 150,
                                labelWidth: 85,
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
                            {
                                xtype:'textfield',
                                name: 'allNumMin',
                                id: 'allNumMin',
                                labelWidth: 70,
                                width:150,
                                fieldLabel: '总库存数量',
                            },
                            {
                                xtype:'textfield',
                                name: 'allNumMax',
                                id: 'allNumMax',
                                labelWidth: 15,
                                width:150,
                                fieldLabel:'至',
                            },
                            {
                                xtype:'textfield',
                                name: 'storageNumMin',
                                id: 'storageNumMin',
                                labelWidth: 85,
                                width:150,
                                fieldLabel: '店铺库存数量',
                            },
                            {
                                xtype:'textfield',
                                name: 'storageNumMax',
                                id: 'storageNumMax',
                                labelWidth: 15,
                                width:150,
                                fieldLabel:'至',
                            }

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
//                    {
//                        xtype: 'checkbox',
//                        name: 'alsoReturn',
//                        labelWidth: 60,
//                        itemId: 'createChangeBtn',
//                        id: 'createChangeBtn',
//                        labelAlign: 'left',
//                        inputValue: true,
//                        fieldLabel: '高级查询'
//                    }
                ]
            }
        ];

        this.callParent(arguments);
    }
})