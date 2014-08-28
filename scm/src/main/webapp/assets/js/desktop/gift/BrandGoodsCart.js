/**
 * 品牌已经添加的赠品列表
 * Created by HuaLei.Du on 14-2-20.
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



Ext.define('EBDesktop.gift.BrandGoodsCart', {
    extend: 'Ext.container.Container',
    alias: 'widget.giftBrandGoodsCart',
    id: 'giftBrandGoodsCart',
    height: 180,
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

        // 商品列表Grid
        goodListGridCart = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: brandGoodsCartStore,
            height: 180,
            forceFit: true,
            id: 'giftBrandGoodsCartGrid',
            viewConfig: {
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                enableTextSelection:true
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
