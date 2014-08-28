/*
* Created by king on 13-12-17
*/

Ext.define('Supplier.view.AddOrderChangeWin.AddOrderChangeInfo', {
    extend: 'Ext.form.Panel',
    id: 'AddOrderChangeInfo',
    region: 'north',
    alias: 'widget.AddOrderChangeInfo',
    bodyPadding: 10,
    layout:'anchor',
    split:'true',
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
    initComponent: function (){

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
        });


        this.items = [
            {
                items: [
                    {
                        xtype:'combo',
                        name: 'platformType',
                        id: 'platformType',
                        fieldLabel:'平台类型',
                        labelWidth: 60,
                        width: 240,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        value: 'TAO_BAO',
                        emptyText: '请选择',
                        store: [
                            ['TAO_BAO', '天猫'],
                            ['JING_DONG', '京东'],
                            ['EJS', '易居尚'],
                            ['TM_GYS', '天猫供应商'],
                            ['JIAN_HANG', '建行商城'],
                            ['QQ_WG', 'QQ网购'],
                            ['QQ_TG', 'QQ团购'],
                            ['WEI_XIN', '微信'],
                            ['WEI_BO', '微博'],
                        ],
                        listeners:{
                            select:function(){
                                var shopId=Ext.getCmp("shopGo");
                                shopId.reset();
                                goStore.proxy.url = '/order/shopList?platformType='+ this.getValue();
                                goStore.load();
                            }
                        }
                    },{
                        xtype:'textfield',
                        value: '售后换货订单',
                        disabled:true
                    },
                    {
                        xtype:'textfield',
                        id:'orderType',
                        name: 'orderType',
                        value: 'EXCHANGE',
                        hidden: true
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'buyerId',
                        fieldLabel: '买家Id',
                        allowBlank: false
                    },
                    {
                        name: 'receiverName',
                        width: 180,
                        fieldLabel:'收货人',
                        allowBlank: false
                    },
                    {
                        name: 'receiverPhone',
                        fieldLabel: '收货电话',
                        emptyText: '0000-0000000',
                        //vtype: 'Phone',
                        allowBlank: true

                    },
                    {
                        name: 'receiverMobile',
                        fieldLabel: '收货手机',
                        vtype: 'Mobile',
                        allowBlank: false
                    },
                    {
                        name: 'receiverZip',
                        width: 160,
                        fieldLabel:'邮政编码',
                        allowBlank: true
                    },
                    {
                        fieldLabel: '快递',
                        labelWidth: 40,
                        width:150,
                        name: 'shippingComp',
                        xtype: 'combo',
                        emptyText: '请选择',
                        allowBlank: false,
                        store: Espide.Common.expressStore()
                    },
//                    {
//                        name: 'orderType',
//                        fieldLabel:'订单类型',
//                        allowBlank: false,
//                        xtype: 'combo',
//                        value: 'NORMAL',
//                        store: Espide.Common.orderType.getStore()
//                    }
                ]
            },
            {
                items: [
                    {
                        name: 'receiverState',
                        itemId: 'receiverState',
                        fieldLabel:'收货省',
                        width: 200,
                        xtype: 'combo',
                        emptyText: "请选择",
                        allowBlank: false,
                        store: Espide.City.getProvinces()
                    },
                    {
                        name: 'receiverCity',
                        itemId: 'receiverCity',
                        fieldLabel:'收货市',
                        width: 180,
                        xtype: 'combo',
                        emptyText: "请选择",
                        allowBlank: false,
                        store: []
                    },
                    {
                        name: 'receiverDistrict',
                        itemId: 'receiverDistrict',
                        fieldLabel:'收货区',
                        width: 200,
                        xtype: 'combo',
                        emptyText: "请选择",
                        allowBlank: true,
                        store: []
                    },
                    {
                        name: 'receiverAddress',
                        fieldLabel:'收货地址',
                        width: 370,
                        allowBlank: false
                    },
                    {
                        fieldLabel: '店铺',
                        labelWidth: 40,
                        width: 175,
                        xtype: 'combo',
                        emptyText: '请选择',
                        queryMode: 'remote',
                        name: 'shopId',
                        id:'shopGo',
                        //itemId: 'shopId',
                        valueField: 'id',
                        displayField: 'nick',
                        allowBlank: false,
                        //store: 'Shop'
                        store:goStore
                    }
                ]
            },

            {
                items: [
//                    {
//                        name: 'totalFee',
//                        width: 160,
//                        xtype: 'numberfield',
//                        minValue: 0,
//                        fieldLabel: '成交金额',
//                        editable: true,
//                        disabled: true,
//                        value: 0
//                    },
                    {
                        name: 'receiptTitle',
                        width: 200,
                        fieldLabel: '发票抬头',
                        allowBlank: true
                    },
                    {
                        name: 'receiptContent',
                        width: 200,
                        margin: '0 10 0 0',
                        fieldLabel: '发票内容',
                        allowBlank: true
                    },
                    {
                        name: 'buyerMessage',
                        width: 300,
                        margin: '0 10 0 0',
                        fieldLabel: '买家留言',
                        allowBlank: true
                    },
                    {
                        name: 'remark',
                        width: 330,
                        fieldLabel: '备注说明',
                        allowBlank: true
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
})