/**
 * 商品优惠活动 - 活动列表
 * Created by HuaLei.Du on 13-12-30.
 */
Ext.define('EBDesktop.GiftGoodsActivityModel', {
    extend: 'Ext.data.Model',
    fields: ['id','remark', 'sellProdId', 'giftProdId', 'giftProdName', 'giftProdCount',
        'inUse',
        'shopIds',
        {name:'brandName',type:'auto',mapping:'product.brand.name'},
        {name:'sellProdName',type:'auto',mapping:'product.name'},
        {name:'sku',type:'auto',mapping:'product.sku'},
        {name:'brandId',type:'auto',mapping:'product.brand.id'},
        {name:'productNo',type:'auto',mapping:'product.productNo'},
        {name:'productId',type:'auto',mapping:'product.id'},

        'activityItems',
    ],
    idProperty: 'id'
});

Ext.define('EBDesktop.gift.GoodsActivity', {
    extend: 'Ext.container.Container',
    alias: 'widget.giftGoodsActivity',
    id: 'giftGoodsActivity',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var goodsGridId = 'giftGoodsActivityGrid',
            giftListStore,
            searchForm,
            giftGoodsActivityGrid;

        // 显示添加窗口
        function showAddWin() {

            var saveWin = Ext.getCmp('giftGoodsActivityAdd') || Ext.widget('giftGoodsActivityAdd');
            saveWin.show().setTitle('添加商品活动');

        }

        // 显示修改窗口
        function showEditWin(t, record) {
            var saveWin = Ext.getCmp('giftGoodsActivityEdit') || Ext.widget('giftGoodsActivityEdit');

            //转换inUse 状态
            if(record.get('inUse')){
                record.set('inUse',1);
            }else{
                record.set('inUse',0);
            }

            Ext.getCmp('search').getForm().loadRecord(record);

            //设置产品的store
            newRecord = Ext.create('EBDesktop.GiftGoodsListModel', {
                id: record.get('productId'),
                brandName: record.get('brandName'),
                name: record.get('sellProdName'),
                sku: record.get('sku'),
                productNo: record.get('productNo'),
                amount: 1
            });

            var grid = Ext.getCmp('giftGoodsListGrid');


            grid.getStore().insert(0, newRecord);

            //设置赠品store
            Ext.getCmp('giftGoodsCartGrid').getStore().loadRawData(record.get('activityItems'));

            saveWin.show();
        }

        // 移除商品活动
        function removeGift() {
            var url = '/activity/delete',
                ids = Espide.Common.getGridSels(goodsGridId, 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的商品活动');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        id: ids.join()
                    },
                    successCall: function () {
                        Espide.Common.reLoadGird(goodsGridId, false, true);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });
        }

        // 商品活动搜索
        function doGiftGoodsActivitySearch() {
            Espide.Common.doSearch(goodsGridId, 'giftGoodsActivitySearch', true);
        }

        giftListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.GiftGoodsActivityModel',

            proxy: {
                extraParams: {
                    type: 'PRODUCT',
                },
                type: 'ajax',
                api: {
                    read: '/activity/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    totalProperty: 'data.obj.totalCount',
                    messageProperty: 'msg'
                }
            },
            pageSize: 12,
            autoLoad: false
        });

        searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'giftGoodsActivitySearch',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'type',
                    value:'PRODUCT',
                    width: 220,
                    hidden:true
                },
                {
                    xtype: 'combo',
                    store: [
                        ['product.productNo', '商品编号'],
                        ['product.name', '商品名称'],
                        ['product.brand.name', '品牌名称']
                    ],
                    queryMode: 'local',
                    name: 'searchType',
                    value: 'product.productNo',
                    editable: false,
                    width: 100
                },
                {
                    xtype: 'textfield',
                    name: 'searchValue',
                    width: 160,
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {
                                doGiftGoodsActivitySearch();
                            }
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    width: 60,
                    itemId: 'searchBtn',
                    handler: function () {
                        doGiftGoodsActivitySearch();
                    }
                },
                {
                    xtype: 'button',
                    text: '添加活动',
                    width: 70,
                    itemId: 'addBtn',
                    handler: showAddWin
                },
                {
                    xtype: 'button',
                    text: '删除已选',
                    width: 70,
                    itemId: 'deleteBtn',
                    handler: removeGift
                }
            ]
        });

        giftGoodsActivityGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: giftListStore,
            forceFit: true,
            id: goodsGridId,
            selType: 'checkboxmodel',
            viewConfig: {
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                enableTextSelection:true

            },
            columns: [
                {
                    text: '活动名称',
                    dataIndex: 'remark',
                    width: 120
                },
                {
                    text: '品牌名称',
                    dataIndex: 'brandName',
                    width: 120
                },
                {
                    text: '购买商品',
                    dataIndex: 'sellProdName',
                    width: 120
                },
                {
                    text: '商品编号',
                    dataIndex: 'productNo',
                    width: 120
                },
                {
                    text: '是否启用',
                    dataIndex: 'inUse',
                    width: 60,
                    renderer: function (value) {
                        if (value) {
                            return '是';
                        }
                        return '否';
                    }
                }
            ],
            listeners: {
                'itemdblclick': showEditWin
            },
            bbar: Ext.create('Ext.PagingToolbar', {
                store: giftListStore,
                displayInfo: true
            })
        });

        this.items = [searchForm, giftGoodsActivityGrid];

        this.callParent(arguments);
    }
});