/**
 * 商品优惠活动 - 商品列表
 * Created by HuaLei.Du on 14-1-7.
 */
Ext.define('EBDesktop.GiftGoodsListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'brandName', 'name','sku', 'productNo', 'shopPrice'],
    idProperty: 'id'
});

Ext.define('EBDesktop.gift.GoodsList', {
    extend: 'Ext.container.Container',
    alias: 'widget.giftGoodsList',
    id: 'giftGoodsList',
    region: 'north',
    height: 250,
    forceFit: true,
    //layout: 'border',
    initComponent: function () {
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        var brandListStore,
            goodListStore,
            goodsSearchForm,
            goodListGrid;






        // 商品搜索
        function doGiftGoodsSearch() {
            Espide.Common.doSearch('giftGoodsListGrid', 'giftGoodsSearch', true);
        }

        // 品牌列表store
        brandListStore = Espide.Common.createComboStore('/brand/list', true);


        function addGoodsCart(btn, rowIndex, colIndex, item, e, record) {
            var brandGoodsCartStore = Ext.getCmp('giftGoodsCartGrid').getStore(),
                brandGoodsStore = Ext.getCmp('giftGoodsListGrid').getStore(),
                sku = record.get('sku'),
                flag = false,
                newRecord;


            // 是否为第一条
            if (brandGoodsCartStore.data.items.length > 0) {
                // 遍历判断是否已经存在
                brandGoodsCartStore.each(function (records, index) {
                    if (records.get('sku') == sku) {
                        flag = true;
                    }
                });

            }

            if (flag) {
                Ext.Msg.alert('警告', '此商品已经添加过了');
                return;
            }

            newRecord = Ext.create('EBDesktop.GiftBrandGoodsCartModel', {
                prodId: record.get('id'),
                brandName: record.get('brandName'),
                name: record.get('name'),
                sku: record.get('sku'),
                productNo: record.get('productNo'),
                amount: 1
            });

            brandGoodsCartStore.insert(0, newRecord);
            brandGoodsStore.remove(record);
        }


        // 商品列表store
        goodListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.GiftGoodsListModel',
            proxy: {
                type: 'ajax',
                api: {
                    //read: '/assets/js/desktop/gift/data/goodsList.json',
                    read: '/product/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'msg'
                }
            },
            listeners:{
                add:function(store,record,index){
                    //添加选中第一条数据
                    var task = new Ext.util.DelayedTask(function(){
                        Ext.getCmp('giftGoodsListGrid').selModel.select(record);
                    });
                    task.delay(1);
                }
            },
            //autoSync: true,
            autoLoad: false,
            pageSize: 50
        });

        // 商品搜索表单
        goodsSearchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'giftGoodsSearch',
            defaults: {
                xtype: 'combo',
                labelWidth: 62,
                width: 160,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'type',
                    hidden: true,
                    value: 'PRODUCT'
                },
                {
                    name: 'brandId',
                    itemId: 'brandId',
                    labelWidth: 40,
                    fieldLabel: '品牌',
                    editable: false,
                    queryMode: 'local',
                    valueField: 'id',
                    displayField: 'name',
                    emptyText: '不限',
                    store: brandListStore
                },
                {
                    xtype: 'combo',
                    name: 'searchType',
                    itemId: 'searchType',
                    editable: false,
                    queryMode: 'local',
                    value: 'productNo',
                    store: [
                        ["sku", "sku"],
                        ["name", "产品名称"],
                        ["productNo", "商品编号"]
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'searchValue',
                    emptyText:'请输入关键字',
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {
                                doGiftGoodsSearch();
                            }
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    width: 80,
                    itemId: 'searchBtn',
                    handler: function () {
                        doGiftGoodsSearch();
                    }
                },
                {
                    xtype: 'container',
                    html: '<span style="line-height:24px;color:#f00;font-weight:700">仅显示前50条</span>'
                }
            ]
        });

        // 商品列表Grid
        goodListGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: goodListStore,
            forceFit: true,
            height: 200,
            id: 'giftGoodsListGrid',
            //selType: 'checkboxmodel',
            selModel: {
                selType : 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
                mode: 'SINGLE',
                showHeaderCheckbox: false
            },
            viewConfig: {
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                enableTextSelection:true
            },
            columns: [
                {
                    text: '品牌名称',
                    dataIndex: 'brandName',
                    width: 120
                },
                {
                    text: 'sku',
                    dataIndex: 'sku',
                    width: 120
                },
                {
                    text: '商品名称',
                    dataIndex: 'name',
                    width: 160
                },
                {
                    text: '商品编号',
                    dataIndex: 'productNo',
                    width: 100
                },
                {
                    xtype: 'actioncolumn',
                    text: '添加',
                    itemId: 'addRow',
                    menuDisabled: true,
                    width: 50,
                    items: [
                        {
                            iconCls: 'icon-add'
                        }
                    ],
                    handler: addGoodsCart
                }
            ]
        });

        this.items = [goodsSearchForm, goodListGrid];

        this.callParent(arguments);
    }
});