/**
 * 商品优惠活动 - 添加表单
 * Created by HuaLei.Du on 14-1-7.
 */


Ext.define('EBDesktop.GiftBrandGoodsCartModel', {
    extend: 'Ext.data.Model',
    fields: ['id','product', 'sku', 'amount',

        {name:'prodId',type:'auto',mapping:'product.id'},
        {name:'sku',type:'auto',mapping:'product.sku'},
        {name:'brandName',type:'auto',mapping:'product.brand.name'},
        {name:'name',type:'auto',mapping:'product.name'},
        {name:'productNo',type:'auto',mapping:'product.productNo'},

    ],
    idProperty: 'id'
});


Ext.define('EBDesktop.gift.GoodsActivityAddForm', {
    alias: 'widget.GoodsCart',
    id: 'giftGoodsActivityAddForm',
    extend: 'Ext.container.Container',
    height: 280,
    initComponent: function () {

        Ext.tip.QuickTipManager.init();

        var brandGoodsCartStore,
            goodListGridCart;

        // 商品列表store
        brandGoodsCartStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.GiftBrandGoodsCartModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: 'activity/detail'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.activityItems',
                    messageProperty: 'msg'
                }
            },
            autoLoad: false
        });

        //动态生成店铺checkbox
        ///order/shopList
        var shopArray = [];
        Ext.Ajax.request({
            url: "np/findOnlineShop",
            async:false,
            success: function (response, options) {
                var  shopList = Ext.JSON.decode(response.responseText).data.list;
                Ext.each(shopList, function (shopList) {
                    shopArray.push({
                        boxLabel: shopList.platformType['value']+':'+shopList.nick,
                        name: 'shopIds',
                        inputValue: shopList.id,
                        checked: false,
                        width:210,
                        id: "check" + shopList.id,
                    });
                })
            },
            failure: function (form, action) {
                var data = Ext.JSON.decode(action.response.responseText);
                Ext.MessageBox.show({
                    title: '提示',
                    msg: data.msg,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-warning'
                });

            }
        });



        // 商品列表Grid
        goodListGridCart = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: brandGoodsCartStore,
            height: 280,
            forceFit: true,
            id: 'giftGoodsCartGrid',
            tbar:[

                Ext.create('Ext.form.Panel', {
                    layout: 'hbox',
                    border: false,
                    itemId: 'search',
                    id: 'search',
                    layout:{
                        type: 'vbox',
                    },
                    labelWidth:50,
                    msgTarget: 'side',
                    defaults:{
                        msgTarget: 'side',
                        margin:'0 0 10 0'
                    },
                    items: [
                        {
                            labelWidth:60,
                            xtype: 'fieldcontainer',
                            layout: 'hbox',
                            defaultType: 'radiofield',
                            fieldLabel: '是否启用',
                            items: [
                                {
                                    boxLabel: '是',
                                    name: 'inUse',
                                    inputValue: 1,
                                    itemId: 'inUseYes',
                                    checked: true
                                },
                                {
                                    boxLabel: '否',
                                    name: 'inUse',
                                    itemId: 'inUseNo',
                                    inputValue: 0
                                }
                            ]
                        },
                        {
                            labelWidth:60,
                            xtype: 'textfield',
                            hidden:true,
                            name: 'id',
                            id: 'activityId',
                        },
                        {
                            labelWidth:60,
                            xtype: 'textfield',
                            margin:'0 0 0 0',
                            fieldLabel: '活动名称',
                            name: 'remark',
                            id: 'remark',
                            emptyText: '活动名称',
                            allowBlank: false,
                        },
                        {
                            xtype: 'checkboxgroup',
                            labelWidth:60,
                            fieldLabel: '选择店铺',
                            allowBlank:false,
                            blankText:'至少选择一个店铺',
                            columns: 3,
                            vertical: true,
                            items:shopArray
                        }
                    ]
                })
            ],
            viewConfig: {
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>'
            },
            plugins: new Ext.grid.plugin.CellEditing({
                pluginId: 'cellEdit',
                clicksToEdit: 2
            }),
            columns: [
                {
                    text: '品牌名称',
                    dataIndex: 'brandName',
                    width: 120
                },
                {
                    text: '产品名称',
                    dataIndex: 'name',
                    width: 160
                },
                {
                    text: '商品编号',
                    dataIndex: 'productNo',
                    width: 100
                },
                {
                    header: '数量',
                    dataIndex: 'amount',
                    editor: {
                        xtype: "textfield",
                        allowBlank: false,
                        vtype: 'Number',
                        minValue: 1
                    }
                },
                {
                    xtype: 'actioncolumn',
                    text: '删除',
                    menuDisabled: true,
                    width: 50,
                    items: [
                        {
                            iconCls: 'icon-remove'
                        }
                    ],
                    handler: function (btn, rowIndex, colIndex, item, e, record) {
                        btn.up('grid').getStore().remove(record);
                    }
                }
            ]
        });

        this.items = [goodListGridCart];



        this.callParent(arguments);
    }
});