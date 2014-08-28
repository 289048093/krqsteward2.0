/**
 * 品牌优惠活动 - 活动列表
 * Created by HuaLei.Du on 13-12-30.
 */
Ext.define('EBDesktop.GiftBrandActivityModel', {
    extend: 'Ext.data.Model',
    fields: ['id','actualFeeBegin', 'actualFeeEnd', 'inUse','remark','shopIds',
        {name:'brandName',type:'auto',mapping:'brand.name'},
        {name:'brandId',type:'auto',mapping:'brand.id'},
    ],
    idProperty: 'id'
});

Ext.define('EBDesktop.gift.BrandActivity', {
    extend: 'Ext.container.Container',
    alias: 'widget.giftBrandActivity',
    id: 'giftBrandActivity',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var brandGridId = 'giftBrandActivityGrid',
            giftListStore,
            searchForm,
            giftBrandActivityGrid;

        // 显示添加窗口
        function showAddWin() {
            var saveWin = Ext.getCmp('giftBrandSave') || Ext.widget('giftBrandSave', {title: '添加品牌活动'}),
                saveForm = Ext.getCmp('giftBrandSaveForm');

            saveWin.down('[itemId=action]').setValue('activity/save');
            saveWin.show();
        }

        // 显示修改窗口
        function showEditWin(t, record) {
            console.log(record);
            var saveWin = Ext.getCmp('giftBrandSave') || Ext.widget('giftBrandSave', {title: '修改品牌活动'}),
                saveForm = Ext.getCmp('giftBrandForm'),
                giftBrandId = record.get('id');

            saveForm.getForm().loadRecord(record);
            saveForm.down('[itemId=action]').setValue('activity/update');
            saveForm.down('[itemId=brandId]').setDisabled(true);
            if (!record.data.inUse) {
                saveForm.down('[itemId=inUseYes]').setValue(false);
                saveForm.down('[itemId=inUseNo]').setValue(true);
            }
            saveWin.show();
            saveWin.setLoading(true);
            Ext.getCmp('giftBrandGoodsCartGrid').getStore().load({
                params: {
                    id: giftBrandId
                },
                callback: function () {
                    saveWin.setLoading(false);
                }
            });
        }

        // 移除品牌活动
        function removeGift() {
            var url = '/activity/delete',
                ids = Espide.Common.getGridSels(brandGridId, 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的品牌活动');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        id: ids.join()
                    },
                    successCall: function () {
                        Espide.Common.reLoadGird(brandGridId, false, true);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });
        }

        giftListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.GiftBrandActivityModel',
            proxy: {
                extraParams: {
                    type: 'BRAND',
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
            autoLoad: true
        });

        // 品牌活动搜索
        function doGiftBrandActivitySearch() {
            Espide.Common.doSearch(brandGridId, 'giftBrandSearch', true);
        }

        searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'giftBrandSearch',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'searchType',
                    value:'brand.name',
                    width: 220,
                    hidden:true
                },
                {
                    xtype: 'textfield',
                    name: 'type',
                    value:'BRAND',
                    width: 220,
                    hidden:true
                },
                {
                    xtype: 'textfield',
                    name: 'searchValue',
                    width: 220,
                    fieldLabel: '品牌名称',
                    emptyText: '关键字',
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {
                                doGiftBrandActivitySearch();
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
                        doGiftBrandActivitySearch();
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

        giftBrandActivityGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: giftListStore,
            forceFit: true,
            id: brandGridId,
            selType: 'checkboxmodel',
            viewConfig: {
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>'
            },
            columns: [
                {
                    text: '活动名称',
                    dataIndex: 'remark',
                    width: 180
                },
                {
                    text: '品牌',
                    dataIndex: 'brandName',
                    width: 180
                },
                {
                    text: '起始价格',
                    dataIndex: 'actualFeeBegin'
                },
                {
                    text: '结束价格',
                    dataIndex: 'actualFeeEnd'
                },
                {
                    text: '是否启用',
                    dataIndex: 'inUse',
                    width: 70,
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

        this.items = [searchForm, giftBrandActivityGrid];

        this.callParent(arguments);
    }
});