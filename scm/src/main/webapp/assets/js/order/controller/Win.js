/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.Win', {
        extend: 'Ext.app.Controller',
        views: [
            'win.AddGoodWin',
            'win.OrderInfo',
            'win.GoodList',
            'win.GoodCart',
            'win.AddOrderWin',
            'win.ExchangeOnsaleWin',
            'win.ExchangeList',
            'win.ExchangeSearch',
            'win.ExchangeItem',
            'win.AddOrderGoWin',
            'win.AddOrderChangeWin',
            'win.AddOrderNormalWin',
            'win.AddOrderChangeForm',
            'win.AddOrderChangeItem',
            'win.AddOrderChangeSearch',
            'win.ReplenishmentWin',
            'win.ReplenishmentForm',
            'win.ReplenishmentOrderInfo',
            'win.AddOrderChargeInfo',
        ],
        stores: ['GoodCart', 'GoodList'],
        models: ['GoodCart', 'GoodList'],
        init: function () {
           this.control({
//                '#addGoodWin': {
//                    //增加赠品窗口展开
//                    show: function (win) {
//                        //清除grid数据
//                        var OrderListSlc = Ext.getCmp('OrderList').getSelectionModel().getSelection()[0].data;
//                        Ext.getCmp('hideOrderId').setValue(OrderListSlc.id);
//                        Ext.getCmp('goodCart').getStore().removeAll();
//                        Ext.getCmp('goodList').getStore().removeAll();
//                    },
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
//                },
//                '#addOrderWin': {
//                    //增加订单窗口展开
//                    show: function (win) {
//                        console.log('show addOrderWin');
//                        Ext.getCmp('goodCart').getStore().removeAll();
//                        Ext.getCmp('goodList').getStore().removeAll();
//                    }
//                },
//                "#addOrderWin #comfirm": {
//                    click: function (btn) {
//
//                        var com = Espide.Common,
//                            params = {},
//                            orderInfo = Ext.getCmp('orderInfo').getForm(),
//                            goodCartStore = Ext.getCmp('goodCart').getStore();
//
//
//                        if (orderInfo.isValid()) {
//
//                            if (goodCartStore.count() === 0) {
//                                Ext.Msg.alert('警告', '请至少加入一个商品!');
//                                return;
//                            }
//                            goodCartStore.getProxy().api.create = "/order/addOrder";
//                            goodCartStore.getProxy().extraParams = orderInfo.getValues();
//                            //goodCartStore.getProxy().extraParams['orderItemType'] = Ext.getCmp('goodList').down("#searchType").getValue();
//                            goodCartStore.sync();
//                        } else {
//                            Ext.Msg.alert('警告', '请把订单信息补全!');
//                        }
//
////                        var orderGoWin =  Ext.getCmp('AddOrderGoWin').getForm();
////                        var platfromId = orderGoWin.getValues().platformType,orderId = orderGoWin.getValues().orderType;
//
//
//                    }
//                },
//                "#addOrderWin #cancel": {
//                    click: function () {
//                        console.log(111);
//                        Ext.getCmp('goodCart').getStore().removeAll();
//                    }
//                },
//                '#searchBtn': {
//                    //搜索商品
//                    click: function (button) {
//                        var params = button.up('form').getValues();
//                        //params['repoId'] = Espide.Common.getGridSels('OrderList', 'repoId')[0];
//
////                        if (!!Ext.getCmp('addOrderWin')){
////                            params['repoId'] = '0';
////                        }
////                        if (button.up('form').isValid()) {
////                            button.up('grid').getStore().load({
////                                params: params
////                            });
////                        }
//
//                        button.up('grid').getStore().load({
//                            params: params
//                        });
//                    }
//                },

                '#ChangeSearchBtn': {
                    click: function (btn) {
                        var addOrderChangeSearchStore = Ext.getCmp('addOrderChangeSearch').getStore();

                        addOrderChangeSearchStore.load({
                            params: Ext.getCmp('orderSearchForm').getValues()
                        });

                    }
                },

                '#replenishmentWin #Replenishmentcomfirm': {
                    click: function (btn) {
                        var com = Espide.Common,
                            params = {},
                            replenishmentOrderInfo = Ext.getCmp('replenishmentOrderInfo').getForm(),
                            goodCartStore = Ext.getCmp('goodCart').getStore();


                        if (replenishmentOrderInfo.isValid()) {

                            if (goodCartStore.count() === 0) {
                                Ext.Msg.alert('警告', '请至少加入一个商品!');
                                return;
                            }
                            goodCartStore.getProxy().api.create = "/order/addOrder";
                            goodCartStore.getProxy().extraParams = replenishmentOrderInfo.getValues();
                            //goodCartStore.getProxy().extraParams['orderItemType'] = Ext.getCmp('goodList').down("#searchType").getValue();
                            goodCartStore.sync();
                        } else {
                            Ext.Msg.alert('警告', '请把订单信息补全!');
                        }


                    }

                },

                '#addOrderChangeComfirm': {
                    click: function (btn) {
                        var ProductListSlc = Ext.getCmp('addOrderChangeSearch').getSelectionModel().getSelection()[0].data;


                        var com = Espide.Common,
                            params = {},
                            addOrderChargeInfo = Ext.getCmp('addOrderChargeInfo').getForm(),
                            goodCartStore = Ext.getCmp('goodCart').getStore();

                        if (addOrderChargeInfo.isValid()) {

                            if (goodCartStore.count() === 0) {
                                Ext.Msg.alert('警告', '请至少加入一个商品!');
                                return;
                            }
                            goodCartStore.getProxy().api.create = "order/addExchangeOrder";
                            goodCartStore.getProxy().extraParams = addOrderChargeInfo.getValues();
                            goodCartStore.getProxy().extraParams['orderItemId'] = ProductListSlc.id;
                            goodCartStore.sync();
                        } else {
                            Ext.Msg.alert('警告', '请把订单信息补全!');
                        }
                    }
                },
                //线上换货提交
                '#Exchangesubmit': {
                    click: function (btn) {

                        var ExchangeAddListSlc = Ext.getCmp('ExchangeAddList').getSelectionModel().getSelection();

                        //判断是否选择了数据
                        if(!Espide.Common.checkGridSel('ExchangeAddList','请选择一条订单项数据！')){
                            return false;
                        }

                        //判断是否选择了数据
                        if(!Espide.Common.checkGridSel('ExchangeItem','请选择一条产品数据！')){
                            return false;
                        }



                        var ProductListSlc = Ext.getCmp('ExchangeItem').getSelectionModel().getSelection()[0].data;


                        Ext.Ajax.request({
                            params: {
                                oldOrderItemId: ExchangeAddListSlc[0].data.id,
                                productId: ProductListSlc.id,
                                count: ProductListSlc.buyCount
                            },
                            url: "order/exchangeGood",
                            success: function (response, options) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('ExchangeOnsaleWin'), 'OrderList', true);
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
                //线上换货搜索产品
                '#ExchangeSearchBtn': {
                    click: function (btn) {
                        var params = btn.up('form').getValues();


//                        if (!!Ext.getCmp('addOrderWin')){
//                            params['repoId'] = '0';
//                        }
//                        if (button.up('form').isValid()) {
//                            button.up('grid').getStore().load({
//                                params: params
//                            });
//                        }
//                        console.log(btn.up('grid').getStore());
//                        console.log(params);


                        btn.up('grid').getStore().load({
                            params: params
                        });
                    }
                },
                //线上换货点击
                '#exchangeOnsale': {
                    click: function (btn) {
                        var OrderListSlc = Ext.getCmp('OrderList').getSelectionModel().getSelection();


                        if(!Espide.Common.checkGridSel('OrderList','请选择一条订单数据！')){
                            return false;
                        }

                        Ext.widget('exchangeOnsale').show(this, function () {
                            var ExchangeAddListStore = Ext.getCmp('ExchangeAddList').getStore();
                            var ExchangeItemStore = Ext.getCmp('ExchangeItem').getStore();
                            //清空上一次数据
                            if (ExchangeAddListStore.count() >= 0 || ExchangeItemStore.count() >= 0) {
                                ExchangeAddListStore.removeAll();
                                ExchangeItemStore.removeAll();

                            }

                            Ext.Ajax.request({
                                params: {
                                    orderIds: OrderListSlc[0].data.id
                                },
                                url: "order/checkOrder",
                                success: function (response, options) {
                                    var data = Ext.JSON.decode(response.responseText);
                                    ExchangeAddListStore.loadData(data.data.list);

                                },
                                failure: function (form, action) {
                                }
                            });
                        });
                    }
                },
//                '#addBtn': {
//                    //多个添加到商品车
//                    click: this.readyAddGood
//                },
//                '#goodList actioncolumn[itemId=addRow]': {
//                    click: this.readyAddGood //单个添加到商品车
//                },
//                '#removeBtn': {
//                    click: this.removeGoodCart  //移除购物车中商品
//                },
//                //加产品提交订单
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
//                        Ext.getCmp('addGoodWin').destroy();
//
//                    }
//                },
//                "#addGoodWin #cancel": {
//                    click: function () {
//                        Ext.getCmp('goodCart').getStore().removeAll();
//                    }
//                },
                "#addOrderChargeInfo #receiverState": {
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
                "#addOrderChargeInfo #receiverCity": {
                    change: function (combo, newValue) {

                        if (newValue) {
                            var province = combo.up('form').down('#receiverState').getValue(),
                                areas = Espide.City.getAreas(province, newValue);
                            combo.up('form').down('#receiverDistrict').getStore().loadData(areas);
                            combo.up('form').down('#receiverDistrict').reset();
                        }
                    }
                },
                "#replenishmentOrderInfo #receiverState": {
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
                "#replenishmentOrderInfo #receiverCity": {
                    change: function (combo, newValue) {
                        if (newValue) {
                            var province = combo.up('form').down('#receiverState').getValue(),
                                areas = Espide.City.getAreas(province, newValue);
                            combo.up('form').down('#receiverDistrict').getStore().loadData(areas);
                            combo.up('form').down('#receiverDistrict').reset();
                        }
                    }
                },


//                "#orderInfo #receiverState": {
//                    change: function (combo, newValue) {
//                        var state = Espide.City.getCities(newValue),
//                            cityEle = combo.up('form').down('#receiverCity'),
//                            districtEle = combo.up('form').down('#receiverDistrict');
//                        cityEle.getStore().loadData(state);
//                        cityEle.reset();
//
//                        districtEle.getStore().loadData([]);
//                        districtEle.reset();
//                    }
//                },
//                "#orderInfo #receiverCity": {
//                    change: function (combo, newValue) {
//                        console.log(1111);
//                        if (newValue) {
//                            var province = combo.up('form').down('#receiverState').getValue(),
//                                areas = Espide.City.getAreas(province, newValue);
//                            combo.up('form').down('#receiverDistrict').getStore().loadData(areas);
//                            combo.up('form').down('#receiverDistrict').reset();
//                        }
//                    }
//                },
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
//        readyAddGood: function (button, rowIndex, colIndex, item, e, selected) {
//            var root = this,
//                selectgoods = [],
//                newgoods = null;
//
//            //分情况获取所选项数组
//            if (button.getXType() == 'button') {
//                selectgoods = button.up('grid').getSelectionModel().getSelection();
//            } else {
//                selectgoods.push(selected);
//            }
//
//            console.log(selectgoods);
//            //是否有加入项，有，则返回可加入数组
//            newgoods = root.canSelGoodAdd(selectgoods);
//
//            if (!newgoods) return false;
//
//            //条件校检好后真正加入购物车
//            root.addToGoodCart(newgoods);
//        },
//
//        //过滤所选项，判断是否符合加入条件，符合返回可加入的数组，否则返回false
//        canSelGoodAdd: function (sels) {
//            var root = this,
//                flag = true,
//                arr = [],
//                addNum = Ext.getCmp('goodList').down('#addNum').getValue() || 1;
//
//            console.log(addNum);
//
//            Ext.each(sels, function (record, index, records) {
//                if (root.isGoodAdded(record.get('sku'))) {
//                    Ext.Msg.alert('警告', '暂存仓已有选中商品，请先移除已选再添加');
//                    return flag = false;
//                }
//
//                var newgood = Ext.create('Supplier.model.GoodCart', {
//                    num: addNum,
////                    autoId: null,
//                    productNo: record.get('productNo'),
//                    itemType: record.get('itemType'),
//                    brandName: record.get('brandName'),
//                    prodCategoryName: record.get('prodCategoryName'),
//                    sku: record.get('sku'),
//                    repositoryNum: record.get('repositoryNum'),
//                    marketPrice: record.get('marketPrice'),
//                    minimumPrice: addNum * record.get('minimumPrice'),
//                    //repositoryNum: addNum*record.get('prodPrice'),
//                    name: record.get('name')
//                });
//
//                console.log(newgood);
//
//                arr.push(newgood);
//            });
//            if (!flag || arr.length === 0) {
//                return false;
//            }
//            return arr;
//        },
//        //判断购物车是否已有要添加的商品
//        isGoodAdded: function (goodId) {
//            var flag = false,
//                goodCartItems = Ext.getCmp('goodCart').getStore().data.items;
//            Ext.each(goodCartItems, function (record, index, root) {
//                if (record.get('sku') == goodId) {
//                    return flag = true;
//                }
//            });
//            return flag;
//        },
//        //过滤完后，真正加入购物车
//        addToGoodCart: function (goods) {
//            Ext.getCmp('goodCart').getStore().add(goods);
//        },
//        //移除购物车中商品
//        removeGoodCart: function (button) {
//            var goodCart = button.up('window').down("#goodCart"),
//                records = goodCart.getSelectionModel().getSelection();
//            if (records.length > 0) {
//                goodCart.getStore().remove(records);
//            }
//        }
    }
);