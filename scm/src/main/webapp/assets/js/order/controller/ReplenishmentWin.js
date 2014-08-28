/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.ReplenishmentWin', {
        extend: 'Ext.app.Controller',
        views: [

            'ReplenishmentWin.ReplenishmentWin',
            'ReplenishmentWin.ReplenishmentOrderInfo',
            'ReplenishmentWin.ReplenishmentAddGoodList',
            'ReplenishmentWin.ReplenishmentAddGoodCart',

        ],
        stores: ['GoodCart', 'GoodList'],
        models: ['GoodCart', 'GoodList'],
        init: function () {
           this.control({

                //铺货订单提交
                '#ReplenishmentWin #Replenishmentcomfirm': {
                    click: function (btn) {
                        var com = Espide.Common,
                            params = {},
                            replenishmentOrderInfo = Ext.getCmp('ReplenishmentOrderInfo').getForm(),
                            goodCartStore = Ext.getCmp('ReplenishmentAddGoodCart').getStore();


                        if (replenishmentOrderInfo.isValid()) {

                            if (goodCartStore.count() === 0) {
                                Ext.Msg.alert('警告', '请至少加入一个商品!');
                                return;
                            }
                            goodCartStore.getProxy().api.create = "/order/addOrder";
                            goodCartStore.getProxy().extraParams = replenishmentOrderInfo.getValues();
                            //goodCartStore.getProxy().extraParams['orderItemType'] = Ext.getCmp('goodList').down("#searchType").getValue();
                            goodCartStore.sync();
                            Ext.getCmp('ReplenishmentWin').destroy();
                        } else {
                            Ext.Msg.alert('警告', '请把订单信息补全!');
                        }


                    }

                },


                '#ReplenishmentAddGoodList #addRow': {
                    //多个添加到商品车
                    click: this.readyAddGood
                },

                '#ReplenishmentAddGoodCart #removeBtn': {
                    click: this.removeGoodCart  //移除购物车中商品
                },
                //加产品提交订单
//                "#addGoodWin #submit": {
//                    click: function (btn) {
//                        var com = Espide.Common,
//                            goodCartStore = Ext.getCmp('goodCart').getStore();
//
//                        goodCartStore.getProxy().api.create = "/order/addGift";
//
////                        goodCartStore.getProxy().extraParams = {
////                            orderNos: com.getGridSels('OrderList', 'orderNo').join(','),
////                            orderItemType: Ext.getCmp('goodList').down("#searchType").getValue()
////                        };
//
//
//                        goodCartStore.getProxy().extraParams['orderIds'] = Ext.getCmp('hideOrderId').getValue();
//
//                        goodCartStore.sync();
//
//                        Ext.getCmp('ReplenishmentWin').destroy();
//
//                    }
//                },


                "#ReplenishmentOrderInfo #receiverState": {
                    change: function (combo, newValue) {
                        var state = Espide.City.getCities(newValue),
                            cityEle = combo.up('form').down('#receiverCity'),
                            districtEle = combo.up('form').down('#receiverDistrict');
                        cityEle.getStore().loadData(state);
                        cityEle.reset();

                        districtEle.getStore().loadData([]);
                        districtEle.reset();
                    }
                },
                "#ReplenishmentOrderInfo #receiverCity": {
                    change: function (combo, newValue) {
                        if (newValue) {
                            var province = combo.up('form').down('#receiverState').getValue(),
                                areas = Espide.City.getAreas(province, newValue);
                            combo.up('form').down('#receiverDistrict').getStore().loadData(areas);
                            combo.up('form').down('#receiverDistrict').reset();
                        }
                    }
                },

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
                addNum = Ext.getCmp('ReplenishmentAddGoodList').down('#addNum').getValue() || 1;


            Ext.each(sels, function (record, index, records) {
                if (root.isGoodAdded(record.get('sku'))) {
                    Ext.Msg.alert('警告', '暂存仓已有选中商品，请先移除已选再添加');
                    return flag = false;
                }

                var newgood = Ext.create('Supplier.model.addGoodWin.GoodCart', {
                    num: addNum,
                    autoId: null,
                    productNo: record.get('productNo'),
                    itemType: record.get('itemType'),
                    brandName: record.get('brandName'),
                    prodCategoryName: record.get('prodCategoryName'),
                    sku: record.get('sku'),
                    repositoryNum: record.get('repositoryNum'),
                    marketPrice: 0,
                    discountPrice: 0,
                    //repositoryNum: addNum*record.get('prodPrice'),
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
                goodCartItems = Ext.getCmp('ReplenishmentAddGoodCart').getStore().data.items;
            Ext.each(goodCartItems, function (record, index, root) {
                if (record.get('sku') == goodId) {
                    return flag = true;
                }
            });
            return flag;
        },
        //过滤完后，真正加入购物车
        addToGoodCart: function (goods) {
            Ext.getCmp('ReplenishmentAddGoodCart').getStore().add(goods);
        },
        //移除购物车中商品
        removeGoodCart: function (button) {
            var goodCart = button.up('window').down("#ReplenishmentAddGoodCart"),
                records = goodCart.getSelectionModel().getSelection();
            if (records.length > 0) {
                goodCart.getStore().remove(records);
            }
        }
    }
);