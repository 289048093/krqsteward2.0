/*
 * Created by king on 13-12-17
 */

Ext.define('ShopProduct.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'List',
    itemId: 'List',
    alias: 'widget.shopProductList',
    store: 'List',
    foreFit: false,
    split: true,
    //selType: 'checkboxmodel',
    selModel: {
        selType: 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'MULTI',
        showHeaderCheckbox: true
    },
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {

        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 2
            })
        ];


        this.tbar = {
            items: [

                {
                    xtype: 'button',
                    text: '添加',
                    iconCls: 'icon-add',
                    itemId: 'addProduct'
                },
                {
                    xtype: 'button',
                    text: '删除',
                    style: 'margin: 0 0 0 15px',
                    iconCls: 'icon-remove',
                    itemId: 'remove'
                },
                { text: '批量上架', iconCls: 'icon-batch-edit', itemId: 'batUp', style: 'margin: 0 0 0 15px', },
                { text: '批量下架', iconCls: 'icon-batch-delete', itemId: 'batDown', style: 'margin: 0 0 0 15px', },

            ]
        };

        this.columns = [
            { text: 'id', dataIndex: 'id', width: 50},
            { text: '产品名称', dataIndex: 'name', width: 80,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            { text: 'sku', dataIndex: 'sku', width: 150},
            { text: '商家编码', dataIndex: 'productNo', width: 150},
            { text: '平台名称', dataIndex: 'platformName', width: 150},
            {text: '一口价', dataIndex: 'price', sortable: true, width: 120, },
            {text: '店铺名称', dataIndex: 'shopName', sortable: true, width: 120,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {xtype: 'checkcolumn', text: '是否上架', width: 150, dataIndex: 'putaway', sortable: true,disabled:true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {text: '总库存数量', width: 150, dataIndex: 'allNum', sortable: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {text: '店铺库存数量', width: 150, dataIndex: 'storageNum', sortable: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },

            {text: '同步店铺库存占比（%）', dataIndex: 'synProportion', sortable: true, width: 250,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },

            {xtype: 'checkcolumn', text: '是否自动同步库存', disabled:true, width: 150, dataIndex: 'synStatus', sortable: true},
            {xtype: 'checkcolumn', text: '有库存是否自动上架', disabled:true, width: 150, dataIndex: 'autoPutaway', sortable: true},
            {
                text: "操作样式1",
                xtype: 'actioncolumn',
                items: [
                    {
                        test: "手动同步",
                        tooltip: '手动同步',
                        text: '手动同步',
                        iconCls: 'icon-refresh',
                        renderer: function (value) {
                            return Ext.String.format('<button>{1}<span>{0}<span></button>', '手动同步', value);
                        },
                        handler: function (btn, rowIndex, colIndex, item, e, record) {
                            var url = '/shopProduct/updateShopStorage';

                            Espide.Common.doAction({
                                url: url,
                                params: {
                                    id: record.get('id')
                                },
                                successCall: function () {
                                    Ext.getCmp('List').getStore().loadPage(1);
                                },
                                successTipMsg: '手动同步成功'
                            })('yes');

                        }
                    },
                    {
                        test: "浏览商品详情",
                        tooltip: "浏览商品详情",
                        iconCls: 'icon-batch-edit',
                        style: 'margin: 0 0 0 15px',
                        renderer: function (value) {
                            return Ext.String.format('<button>{1}<span>{0}<span></button>', '浏览商品详情', value);
                        },
                        handler: function (btn, rowIndex, colIndex, item, e, record) {
                            var url = '/shopProduct/searchDetail', root = btn , id =record.get('id');

                            Ext.Ajax.request({
                                url: url,
                                params: {
                                    id: id
                                },
                                success: function (response) {
                                    var data = Ext.decode(response.responseText);
                                    if (data.success) {
                                        var dataObj = data.data.obj,goodArr = [];

                                        var newRecord = Ext.create('ShopProduct.model.GoodList', {
                                            brandName:dataObj.brandName,
                                            name: dataObj.name,
                                            productNo: dataObj.productNo,
                                            sku: dataObj.sku,
                                            prodCategoryName: dataObj.prodCategoryName,
                                            description: dataObj.description,
                                            marketPrice: dataObj.marketPrice,
                                            minimumPrice: dataObj.minimumPrice,
                                            color: dataObj.color,
                                            weight: dataObj.weight,
                                            boxSize: dataObj.boxSize,
                                            speci: dataObj.speci,
                                            orgin: dataObj.orgin,
                                            style: dataObj.style.value,
                                            location: dataObj.location.value,
                                            repositoryNum: dataObj.repositoryNum,
                                            repositoryName: dataObj.repositoryName,
                                            outerProductNo: dataObj.outerProductNo,
                                        });



                                        var goodRecord = Ext.create('ShopProduct.model.GoodList', {
                                            id: dataObj.id,
                                            productNo: dataObj.productNo,
                                            name: dataObj.productName,
                                            sku: dataObj.sku,
                                            color: dataObj.color,
                                            repositoryNum: dataObj.allAmount,
                                            brandName: dataObj.brand,
                                            speci: dataObj.specification,
                                        });

                                        goodArr.push(goodRecord);

                                        Ext.widget('showDetailsWin').show(root, function () {
                                            Ext.getCmp('showProductInfo').getForm().loadRecord(newRecord);
                                            Ext.getCmp('showProductList').getStore().loadRecords(goodArr);
                                        });

                                    } else {
                                        Ext.Msg.show({
                                            title: '错误',
                                            msg: data.msg,
                                            buttons: Ext.Msg.YES,
                                            icon: Ext.Msg.WARNING
                                        });
                                    }
                                }
                            });

//                            Espide.Common.doAction({
//                                url: url,
//                                params: {
//                                    id: record.get('id')
//                                },
//                                successCall: function (data) {
//                                    //console.log(data.data.obj);
//                                    var dataObj = data.data.obj,goodArr = [];
//
//                                    var newRecord = Ext.create('ShopProduct.model.List', {
//                                        storagePercent: dataObj.storagePercent,
//                                        platformName: dataObj.platform,
//                                        shopName: dataObj.shop,
//                                        synStatus: dataObj.synStatus,
//                                        autoPutaway: dataObj.autoPutaway,
//                                    });
//
//                                    var goodRecord = Ext.create('ShopProduct.model.GoodList', {
//                                        id: dataObj.id,
//                                        productNo: dataObj.productNo,
//                                        name: dataObj.productName,
//                                        sku: dataObj.sku,
//                                        color: dataObj.color,
//                                        repositoryNum: dataObj.allAmount,
//                                        brandName: dataObj.brand,
//                                        speci: dataObj.specification,
//                                    });
//
//
//
//
//                                    Ext.widget('showDetailsWin').show(root, function () {
//                                        Ext.getCmp('showProductInfo').getForm().loadRecord(newRecord);
//                                        Ext.getCmp('showProductList').getStore().loadRecords(goodArr);
//                                    });
//
//
//
//                                },
//                                successTipMsg: ''
//                            })('yes');

                        }
                    }
                ]
            }

        ];

        this.callParent(arguments);

    }
});


