/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.AddOrderChangeWin', {
        extend: 'Ext.app.Controller',
        views: [
            'AddOrderChangeWin.AddOrderChangeWin',
            'AddOrderChangeWin.AddOrderChangeInfo',
            'AddOrderChangeWin.AddOrderChangeSearch',
            'AddOrderChangeWin.AddOrderChangeList',
            'AddOrderChangeWin.AddOrderChangeCart',
        ],
        stores: ['AddOrderChangeWin.GoodCart', 'GoodList'],
        models: ['AddOrderChangeWin.GoodCart', 'GoodList'],
        init: function () {
           this.control({
                //获取订单数据
                '#ChangeSearchBtn': {
                    click: function (btn) {
                        var addOrderChangeSearchStore = Ext.getCmp('AddOrderChangeSearch').getStore();

                        addOrderChangeSearchStore.load({
                            params: Ext.getCmp('orderSearchForm').getValues()
                        });

                    }
                },

                 //提交售后补货
                '#addOrderChangeComfirm': {
                    click: function (btn) {


                        //console.log(ProductListSlc.id);
                        //orderItemId
                        //console.log(btn);
                        if (!Espide.Common.checkGridSel('AddOrderChangeSearch', '请选择一条订单项数据！')) {
                            return false;
                        }


                        var ProductListSlc = Ext.getCmp('AddOrderChangeSearch').getSelectionModel().getSelection()[0].data;
                        //判断选择订单项是否已换货状态
                        if(ProductListSlc.exchanged){
                            Espide.Common.showGridSelErr('你选择订单项状态不能是已换货状态！');
                            return false;
                        }


                        var com = Espide.Common,
                            params = {},
                            addOrderChargeInfo = Ext.getCmp('AddOrderChangeInfo').getForm(),
                            goodCartStore = Ext.getCmp('AddOrderChangeCart').getStore();

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


                '#AddOrderChangeList #addRow': {
                    //多个添加到商品车
                    click: this.readyAddGood
                },

                '#AddOrderChangeCart #removeBtn': {
                    click: this.removeGoodCart  //移除购物车中商品
                },

                "#AddOrderChangeInfo #receiverState": {
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
                "#AddOrderChangeInfo #receiverCity": {
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
                addNum = Ext.getCmp('AddOrderChangeList').down('#addNum').getValue() || 1;


            Ext.each(sels, function (record, index, records) {
                if (root.isGoodAdded(record.get('sku'))) {
                    Ext.Msg.alert('警告', '暂存仓已有选中商品，请先移除已选再添加');
                    return flag = false;
                }

                var newgood = Ext.create('Supplier.model.AddOrderChangeWin.GoodCart', {
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
                goodCartItems = Ext.getCmp('AddOrderChangeCart').getStore().data.items;
            Ext.each(goodCartItems, function (record, index, root) {
                if (record.get('sku') == goodId) {
                    return flag = true;
                }
            });
            return flag;
        },
        //过滤完后，真正加入购物车
        addToGoodCart: function (goods) {
            Ext.getCmp('AddOrderChangeCart').getStore().add(goods);
        },
        //移除购物车中商品
        removeGoodCart: function (button) {
            var goodCart = button.up('window').down("#AddOrderChangeCart"),
                records = goodCart.getSelectionModel().getSelection();
            if (records.length > 0) {
                goodCart.getStore().remove(records);
            }
        }
    }
);