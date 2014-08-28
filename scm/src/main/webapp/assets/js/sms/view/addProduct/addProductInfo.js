/*
 * Created by king on 13-12-17
 */

Ext.define('ShopProduct.view.addProduct.addProductInfo', {
    extend: 'Ext.form.Panel',
    id: 'addProductInfo',
    region: 'north',
    alias: 'widget.addProductInfo',
    bodyPadding: 10,
    layout: 'anchor',
    split: 'true',
    height: 'auto',
    defaults: {
        margin: '0 0 10 0',
        layout: 'hbox',
        border: 0,
        defaults: {
            xtype: 'textfield',
            margin: '0 10 0 0',
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
        // 声明店铺Store
        var goStore = new Ext.data.Store( {
            autoLoad : false,
            fields: ['id', 'nick'],
            reader : {
                    type:'json',
                    root: 'data.list'
            },
            proxy: {
                type: 'ajax',
                url: '/order/shopList?platformType=EJS',
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
        });



        this.items = [
            {
                items: [
                    {
                        xtype:'combo',
                        name: 'platformId',
                        id: 'addplatformType',
                        fieldLabel: '平台类型',
                        value: 'null',
                        labelWidth: 60,
                        width: 160,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        margin: '0 5 0 0',
                        valueField: 'id',
                        displayField: 'name',
                        editable: false,
                        emptyText: '请选择',
                        store: platform,
                    },
                    {
                        xtype:'combo',
                        fieldLabel: '店铺名称',
                        emptyText: '请选择',
                        valueField: 'id',
                        displayField: 'nick',
                        labelWidth: 60,
                        width: 160,
                        id: 'addshopId',
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
                        id:'storagePercent',
                        name: 'storagePercent',
                        fieldLabel:'库存占比',
                    },
                    {
                        xtype: 'checkbox',
                        id:'synStatus',
                        name: 'synStatus',
                        fieldLabel:'自动同步库存',
                        labelAlign: 'left',
                        wdith:50,
                        labelWidth: 85,
                        inputValue: true,
                    },
                    {
                        xtype: 'checkbox',
                        name: 'autoPutaway',
                        wdith:200,
                        labelWidth: 140,
                        itemId: 'autoPutaway',
                        id: 'autoPutaway',
                        labelAlign: 'left',
                        value:false,
                        inputValue: true,
                        fieldLabel: '有库存时是否自动上架'
                    }
                ]
            },
        ];
        this.callParent(arguments);
    }
})