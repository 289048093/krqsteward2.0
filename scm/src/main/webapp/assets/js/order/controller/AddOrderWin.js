/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.AddOrderWin', {
        extend: 'Ext.app.Controller',
        views: [
            'addOrderWin.AddOrderInfo',
            'addOrderWin.AddOrderList',
            'addOrderWin.AddOrderCart',
            'addOrderWin.AddOrderWin'

        ],
        stores: ['GoodCart', 'GoodList'],
        models: ['GoodCart', 'GoodList'],
        init: function () {
            this.control({
                '#AddOrderCart': {//产品编辑 orderItemType  条件判断
                    cellclick: function (grid, td, cellIndex, record, tr, rowIndex, e) {

                        var record = grid.getStore().getAt(rowIndex);
                        if (record.get('orderItemType') == 'GIFT' && cellIndex == 7) {
                            return false;
                        }
                        if (record.get('orderItemType') == 'GIFT' && cellIndex == 11) {
                            return false;
                        }
                        if (record.get('orderItemType') == 'GIFT' && cellIndex == 13) {
                            return false;
                        }

                        if (record.get('orderItemType') == 'REPLENISHMENT' && cellIndex == 7) {
                            return false;
                        }
                        if (record.get('orderItemType') == 'REPLENISHMENT' && cellIndex == 11) {
                            return false;
                        }
                        if (record.get('orderItemType') == 'REPLENISHMENT' && cellIndex == 13) {
                            return false;
                        }

                    }
                },
                '#addOrderSearchBtn': {
                    click: function (button) {
                        var params = button.up('form').getValues();
                        //添加平台参数
                        params['platform'] = Ext.getCmp('platformType').getValue();

                        Ext.Ajax.request({
                            url: "/product/list/addOrder",
                            params: params,
                            success: function (response, options) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    button.up('grid').getStore().loadData(data.data.obj.result);
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

                },
                '#addOrderWin': {
                    //增加订单窗口展开
                    show: function (win) {

                        //判断是否选择了订单数据
                        if (Espide.Common.isGridSel('OrderList')) {
                            var records = Ext.getCmp('OrderList').getSelectionModel().getSelection();
                            delete records[0].data['platformType'];
                            delete records[0].data['shopId'];
                            delete records[0].data['shopName'];
                            delete records[0].data['orderType'];
                            Ext.getCmp('AddOrderInfo').getForm().loadRecord(records[0]);

                            if (!records[0].data['platformType']) {
                                Ext.getCmp('platformType').setValue('EJS');
                            }
                            if (!records[0].data['orderType']) {
                                Ext.getCmp('orderType').setValue('NORMAL');
                            }
                        }

                        Ext.getCmp('AddOrderCart').getStore().removeAll();
                        Ext.getCmp('AddOrderList').getStore().removeAll();
                    }
                },
                "#addOrderWin #comfirm": {
                    click: function (btn) {

                        var com = Espide.Common,
                            params = {},
                            orderInfo = Ext.getCmp('AddOrderInfo').getForm(),
                            goodCartStore = Ext.getCmp('AddOrderCart').getStore();


                        if (orderInfo.isValid()) {

                            if (goodCartStore.count() === 0) {
                                Ext.Msg.alert('警告', '请至少加入一个商品!');
                                return;
                            }
                            goodCartStore.getProxy().api.create = "/order/addOrder";
                            goodCartStore.getProxy().extraParams = orderInfo.getValues();
                            //goodCartStore.getProxy().extraParams['orderItemType'] = Ext.getCmp('goodList').down("#searchType").getValue();
                            goodCartStore.sync();
                        } else {
                            Ext.Msg.alert('警告', '请把订单信息补全!');
                        }

//                        var orderGoWin =  Ext.getCmp('AddOrderGoWin').getForm();
//                        var platfromId = orderGoWin.getValues().platformType,orderId = orderGoWin.getValues().orderType;


                    }
                },
                "#addOrderWin #cancel": {
                    click: function () {
                        Ext.getCmp('AddOrderCart').getStore().removeAll();
                    }
                },
                '#AddOrderList #addRow': {
                    //多个添加到商品车(正常订单)
                    click: this.readyAddGood
                },

                '#AddOrderCart #removeBtn': {
                    click: this.removeGoodCart  //移除购物车中商品
                },
                "#AddOrderInfo #receiverState": {
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
                "#AddOrderInfo #receiverCity": {
                    change: function (combo, newValue) {
                        if (newValue) {
                            var province = combo.up('form').down('#receiverState').getValue(),
                                areas = Espide.City.getAreas(province, newValue);
                            combo.up('form').down('#receiverDistrict').getStore().loadData(areas);
                            combo.up('form').down('#receiverDistrict').reset();
                        }
                    }
                },
//                "#addOrderWin #shopId": {
//                    afterrender: function (combo) {
//                        var store = combo.getStore(),
//                            result = store.findRecord("shopId", 'null');
//                        result && store.remove(result);
//                    }
//                }
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
                addNum = Ext.getCmp('AddOrderList').down('#addNum').getValue() || 1;


            Ext.each(sels, function (record, index, records) {
                if (root.isGoodAdded(record.get('sku'))) {
                    Ext.Msg.alert('警告', '暂存仓已有选中商品，请先移除已选再添加');
                    return flag = false;
                }
                var newgood = Ext.create('Supplier.model.GoodCart', {
                    num: addNum,
                    //autoId: null,
                    productNo: record.get('productNo'),
                    itemType: record.get('itemType'),
                    brandName: record.get('brandName'),
                    prodCategoryName: record.get('prodCategoryName'),
                    sku: record.get('sku'),
                    repositoryNum: record.get('repositoryNum'),
                    marketPrice: record.get('marketPrice'),
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
                goodCartItems = Ext.getCmp('AddOrderCart').getStore().data.items;
            Ext.each(goodCartItems, function (record, index, root) {
                if (record.get('sku') == goodId) {
                    return flag = true;
                }
            });
            return flag;
        },
        //过滤完后，真正加入购物车
        addToGoodCart: function (goods) {
            Ext.getCmp('AddOrderCart').getStore().add(goods);
        },
        //移除购物车中商品
        removeGoodCart: function (button) {
            var goodCart = button.up('window').down("#AddOrderCart"),
                records = goodCart.getSelectionModel().getSelection();
            if (records.length > 0) {
                goodCart.getStore().remove(records);
            }
        }
    }
);