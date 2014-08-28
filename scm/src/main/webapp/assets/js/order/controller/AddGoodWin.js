/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.AddGoodWin', {
        extend: 'Ext.app.Controller',
        views: [
            'addGoodWin.AddGoodWin',
            'addGoodWin.AddGoodList',
            'addGoodWin.AddGoodCart',
        ],
        stores: ['addGoodWin.GoodCart', 'GoodList'],
        models: ['addGoodWin.GoodCart','GoodList'],
        init: function () {
            this.control({
                '#addGoodWin': {
                    //增加赠品窗口展开
                    show: function (win) {
                        //清除grid数据
                        var OrderListSlc = Ext.getCmp('OrderList').getSelectionModel().getSelection()[0].data;
                        Ext.getCmp('hideOrderId').setValue(OrderListSlc.id);
                        Ext.getCmp('AddGoodCart').getStore().removeAll();
                        Ext.getCmp('AddGoodList').getStore().removeAll();
                    },
//                    destroy: function () {
//                        Espide.Common.reLoadGird('OrderList', 'search', false);
//                        var store = Ext.getCmp('orderItem').getStore(),
//                            sel = Ext.getCmp('OrderList').getSelectionModel().selected.items[0],
//                            fld = sel.get('id');
//                        store.load({
//                            params: {
//                                orderIds: fld
//                            }
//                        });
//                    }
                },
                //搜索产品
                '#searchBtn': {
                    //搜索商品
                    click: function (button) {
                        var params = button.up('form').getValues();

                        button.up('grid').getStore().load({
                            params: params
                        });
                    }
                },

                '#AddGoodList #addRow': {
                    //多个添加到商品车
                    click: this.readyAddGood
                },

                '#AddGoodCart #removeBtn': {
                    click: this.removeGoodCart  //移除购物车中商品
                },
                //加产品提交订单
                "#addGoodWin #submit": {
                    click: function (btn) {
                        var com = Espide.Common,
                            goodCartStore = Ext.getCmp('AddGoodCart').getStore();

                        goodCartStore.getProxy().api.create = "/order/addGift";

//                        goodCartStore.getProxy().extraParams = {
//                            orderNos: com.getGridSels('OrderList', 'orderNo').join(','),
//                            orderItemType: Ext.getCmp('goodList').down("#searchType").getValue()
//                        };


                        goodCartStore.getProxy().extraParams['orderIds'] = Ext.getCmp('hideOrderId').getValue();

                        goodCartStore.sync();

                        Ext.getCmp('addGoodWin').destroy();

                    }
                },
                "#addGoodWin #cancel": {
                    click: function () {
                        Ext.getCmp('AddGoodCart').getStore().removeAll();
                    }
                }

            });
        },
        //准备添加到商品车
        readyAddGood: function (button, rowIndex, colIndex, item, e, selected) {
            var root = this,
                selectgoods = [],
                newgoods = null;

            //分情况获取所选项数组
            if (button.getXType() == 'button') {
                selectgoods = button.up('grid').getSelectionModel().getSelection();
            } else {
                selectgoods.push(selected);
            }

            //是否有加入项，有，则返回可加入数组
            newgoods = root.canSelGoodAdd(selectgoods);

            if (!newgoods) return false;

            //条件校检好后真正加入购物车
            root.addToGoodCart(newgoods);
        },

        //过滤所选项，判断是否符合加入条件，符合返回可加入的数组，否则返回false
        canSelGoodAdd: function (sels) {
            var root = this,
                flag = true,
                arr = [],
                addNum = Ext.getCmp('AddGoodList').down('#addNum').getValue() || 1;


            Ext.each(sels, function (record, index, records) {
                if (root.isGoodAdded(record.get('sku'))) {
                    Ext.Msg.alert('警告', '暂存仓已有选中商品，请先移除已选再添加');
                    return flag = false;
                }

                var newgood = Ext.create('Supplier.model.addGoodWin.GoodCart', {
                    num: addNum,
                   // autoId: null,
                    productNo: record.get('productNo'),
                    itemType: record.get('itemType'),
                    brandName: record.get('brandName'),
                    prodCategoryName: record.get('prodCategoryName'),
                    sku: record.get('sku'),
                    repositoryNum: record.get('repositoryNum'),
                    marketPrice: 0,
                    discountPrice:  0,
                    name: record.get('name')
                });


                arr.push(newgood);
            });
            if (!flag || arr.length === 0) {
                return false;
            }
            return arr;
        },
        //判断购物车是否已有要添加的商品
        isGoodAdded: function (goodId) {
            var flag = false,
                goodCartItems = Ext.getCmp('AddGoodCart').getStore().data.items;
            Ext.each(goodCartItems, function (record, index, root) {
                if (record.get('sku') == goodId) {
                    return flag = true;
                }
            });
            return flag;
        },
        //过滤完后，真正加入购物车
        addToGoodCart: function (goods) {
            Ext.getCmp('AddGoodCart').getStore().add(goods);
        },
        //移除购物车中商品
        removeGoodCart: function (button) {
            var goodCart = button.up('window').down("#AddGoodCart"),
                records = goodCart.getSelectionModel().getSelection();
            if (records.length > 0) {
                goodCart.getStore().remove(records);
            }
        }
    }
);