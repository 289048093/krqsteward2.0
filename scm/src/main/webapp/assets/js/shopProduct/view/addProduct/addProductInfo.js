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


        var platform = Espide.Common.createComboStore('/np/platform/list',false,['id','name','type']);




        this.items = [
            {
                items: [
                    {
                        xtype:'combo',
                        name: 'platformId',
                        id: 'addplatformType',
                        fieldLabel: '平台类型',
                        labelWidth: 60,
                        width: 160,
                        value: 'TAO_BAO',
                        valueField: 'id',
                        displayField: 'name',
                        editable: false,
                        emptyText: '请选择',
                        store: platform,
                        listeners:{
                            select:function(combo, records, eOpts){
                                console.log(records[0]);
                                console.log(combo);
                                var shopId=Ext.getCmp("addshopId");
                                shopId.enable();
                                //shopId.reset();
                                shopId.getStore().proxy.url = '/order/shopList?platformType='+ records[0].get('type');
                                shopId.getStore().load();
                            }
                        }
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
                    {
                        xtype:'numberfield',
                        minValue: 0,
                        maxValue:100,
                        allowDecimals:false,
                        editable:true,
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