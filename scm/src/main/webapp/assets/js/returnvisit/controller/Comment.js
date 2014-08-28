/**
 * Created by king on 13-12-23
 */

Ext.define('Comment.controller.Comment', {
        extend: 'Ext.app.Controller',
        views: [
            'Comment',
            'Search',
            'List',
        ],
        models: ['List'],
        stores: ['List'],
        init: function () {
            this.control({
                '#List': {
                    'itemdblclick': function (view, record, item, index, event) {

                        var shopProductUpdateForm = Ext.create('Ext.form.Panel', {
                            baseCls: 'x-plain',
                            labelWidth: 80,
                            border: 0,
                            layout: {
                                type: 'hbox',
                                align: 'left'
                            },
                            height: 'auto',
                            bodyPadding: 10,
                            defaultType: 'fieldcontainer',
                            items: [
                                {
                                    items: [
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            id: 'up2',
                                            combineErrors: true,
                                            msgTarget: 'side',
                                            labelWidth: 60,
                                            width: 1240,
                                            defaults: {
                                                xtype: 'textfield',
                                                disabled: true,
                                            },
                                            items: [
                                                {
                                                    name: 'platformName',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('platformName'),
                                                    id: 'platformName',
                                                    fieldLabel: '平台',
                                                },
                                                {
                                                    name: 'shopName',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('shopName'),
                                                    id: 'shopName',
                                                    fieldLabel: '平台',
                                                },
                                            ]
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            id: 'up1',
                                            combineErrors: true,
                                            msgTarget: 'side',
                                            labelWidth: 60,
                                            width: 1240,
                                            defaults: {
                                                xtype: 'textfield',
                                                disabled: true,
                                            },
                                            items: [
                                                {
                                                    name: 'id',
                                                    hidden:true,
                                                    value: record.get('name'),

                                                },
                                                {
                                                    name: 'name',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('name'),
                                                    id: 'name',
                                                    fieldLabel: '商品名称',
                                                },
                                                {
                                                    name: 'sku',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('sku'),
                                                    fieldLabel: 'sku',
                                                },
                                                {
                                                    name: 'allNum',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('allNum'),
                                                    id: 'allNum',
                                                    fieldLabel: '总库存',
                                                },
                                            ]
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            id: 'up3',
                                            combineErrors: true,
                                            msgTarget: 'side',
                                            labelWidth: 60,
                                            width: 1240,
                                            defaults: {
                                                xtype: 'textfield',
                                            },
                                            items: [
                                                {
                                                    name: 'storageNum',
                                                    id: 'storageNum',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('storageNum'),
                                                    fieldLabel: '店铺库存',
                                                    listeners: {
                                                        'change': function (textfield, newValue, oldValue, eOpts) {
                                                                    //获取总库存数量
                                                                    var allNum = Ext.getCmp('allNum').getValue();

                                                                    //库存占比
                                                                   // var synProportion = Ext.getCmp('synProportion').getValue();

                                                            Ext.getCmp('synProportion').setValue(Math.ceil(newValue*100/allNum));
                                                        }
                                                    }
                                                },
                                                {
                                                    name: 'synProportion',
                                                    labelWidth: 60,
                                                    width: 200,
                                                    value: record.get('synProportion'),
                                                    id: 'synProportion',
                                                    disabled:true,
                                                    fieldLabel: '库存占比',
//                                                    listeners: {
//                                                        'change': function (textfield, newValue, oldValue, eOpts) {
//                                                            //获取总库存数量
//                                                            var allNum = Ext.getCmp('allNum').getValue();
//
//                                                            Ext.getCmp('storageNum').setValue(Math.ceil(allNum*newValue/100));
//                                                        }
//                                                    }
                                                },
                                            ]
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            id: 'up4',
                                            combineErrors: true,
                                            msgTarget: 'side',
                                            labelWidth: 60,
                                            width: 1240,
                                            defaults: {
                                                xtype: 'textfield',
                                            },
                                            items: [
                                                {
                                                    xtype: 'checkbox',
                                                    id: 'synStatus',
                                                    name: 'synStatus',
                                                    fieldLabel: '自动同步库存',
                                                    labelAlign: 'left',
                                                    wdith: 50,
                                                    checked: record.get('synStatus'),
                                                    labelWidth: 85,
                                                    inputValue: true,
                                                },
                                                {
                                                    xtype: 'checkbox',
                                                    name: 'autoPutaway',
                                                    wdith: 200,
                                                    labelWidth: 140,
                                                    itemId: 'autoPutaway',
                                                    id: 'autoPutaway',
                                                    labelAlign: 'left',
                                                    checked: record.get('autoPutaway'),
                                                    inputValue: true,
                                                    fieldLabel: '有库存时是否自动上架'
                                                }
                                            ]
                                        },
                                    ]
                                }
                            ]

                        });



                        var shopProductUpdateWin = Ext.create("Ext.window.Window", {
                            title: "修改商品同步",
                            width: 650,
                            height: 230,
                            items: [shopProductUpdateForm],
                            buttonAlign: "center",
                            modal: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        var data = shopProductUpdateForm.getValues();
                                        data.storagePercent = Ext.getCmp('synProportion').getValue();
                                        data.synStatus = Ext.getCmp('synStatus').getValue();
                                        data.autoPutaway = Ext.getCmp('autoPutaway').getValue();
                                        data.id = record.get("id");

                                        console.log(data);
                                        if (shopProductUpdateForm.isValid()) {

                                            shopProductUpdateForm.submit({
                                                submitEmptyText: false,
                                                url: "/shopProduct/update",
                                                params: data,
                                                success: function (form, action) {
                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    if (data.success) {
                                                        Espide.Common.tipMsgIsCloseWindow(data, shopProductUpdateWin, 'List', true);
                                                    }

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
                                        }

                                    }
                                },
                                {
                                    text: '取消',
                                    itemId: 'cancelBtn',
                                }
                            ]
                        });
                        //更新窗口
                        shopProductUpdateWin.show();
                    }
                },
                '#ShopProductSearch #confirmBtn': {
                    'click': function (btn) {
                        var com = Espide.Common;

                        com.doFormCheck(Ext.getCmp('search').getForm(), function () {
                            com.reLoadGird('List', 'search', true);
                        }, '请正确输入要搜索的信息!')
                    }
                },
                '#addProduct': {
                    'click': function (btn) {
                        Ext.widget('addProductWin').show(this, function () {
                        });
                    }
                },
                //查询按钮
                '#searchBtn': {
                    'click': function () {
                        Espide.Common.doSearch('addProductList', 'addProductInfo', true);
                    }
                },
                '#Addcomfirm': {
                    'click': function (btn) {
                        var data = Ext.getCmp('addProductInfo').getValues(),
                            synStatus = Ext.getCmp('synStatus').getValue(),
                            autoPutaway = Ext.getCmp('autoPutaway').getValue(),
                            ProductId = Espide.Common.getGridSelsId('addProductList');

                        if (!data.synStatus) {
                            data.synStatus = synStatus;
                        }
                        ;
                        if (!data.autoPutaway) {
                            data.autoPutaway = autoPutaway;
                        }

                        data.prodId = ProductId;

                        Espide.Common.doAction({
                            url: '/shopProduct/add',
                            params: data,
                            successCall: function () {
                                Ext.getCmp('addProductWin').close();
                                Ext.getCmp('List').getStore().loadPage(1);
                            },
                            failureCall: function () {
                            },
                            successTipMsg: '产品添加成功'
                        })('yes');


                    }
                },
                '#remove': {
                    click: function (btn) {
                        var url = '/shopProduct/delete',
                            ids = Espide.Common.getGridSels('List', 'id');

                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('请先选择要删除的品牌');
                            return;
                        }

                        Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                            Espide.Common.doAction({
                                url: url,
                                params: {
                                    shopProductIds: ids.join()
                                },
                                successCall: function () {
                                    Ext.getCmp('List').getStore().loadPage(1);
                                },
                                successTipMsg: '删除成功'
                            })(optional);
                        });
                    }
                },
                '#batUp': {
                    click: function (btn) {
                        var url = '/shopProduct/listing',
                            ids = Espide.Common.getGridSels('List', 'id');

                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('请先选择要批量上架产品');
                            return;
                        }

                        // Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                        Espide.Common.doAction({
                            url: url,
                            params: {
                                shopProductIds: ids.join()
                            },
                            successCall: function () {
                                Ext.getCmp('List').getStore().loadPage(1);
                            },
                            successTipMsg: '上架成功'
                        })('yes');
                        // });
                    }
                },
                '#batDown': {
                    click: function (btn) {
                        var url = '/shopProduct/delisting',
                            ids = Espide.Common.getGridSels('List', 'id');

                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('请先选择要批量下架产品');
                            return;
                        }

                        // Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                        Espide.Common.doAction({
                            url: url,
                            params: {
                                shopProductIds: ids.join()
                            },
                            successCall: function () {
                                Ext.getCmp('List').getStore().loadPage(1);
                            },
                            successTipMsg: '下架成功'
                        })('yes');
                        // });
                    }
                }
            });
        }
    }
);