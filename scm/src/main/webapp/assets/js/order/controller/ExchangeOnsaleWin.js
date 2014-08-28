/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.ExchangeOnsaleWin', {
        extend: 'Ext.app.Controller',
        views: [

            'ExchangeOnsaleWin.ExchangeOnsaleWin',
            'ExchangeOnsaleWin.ExchangeList',
            'ExchangeOnsaleWin.ExchangeItem',
        ],
        stores: ['GoodCart', 'GoodList'],
        models: ['GoodCart', 'GoodList'],
        init: function () {
            this.control({

                //线上换货搜索产品
                '#ExchangeSearchBtn': {
                    click: function (btn) {
                        var params = btn.up('form').getValues();
                        btn.up('grid').getStore().load({
                            params: params
                        });
                    }
                },

                //售前换货提交
                '#Exchangesubmit': {
                    click: function (btn) {

                        var ExchangeAddListSlc = Ext.getCmp('ExchangeAddList').getSelectionModel().getSelection();

                        //判断是否选择了数据
                        if (!Espide.Common.checkGridSel('ExchangeAddList', '请选择一条订单项数据！')) {
                            return false;
                        }

                        //判断是否选择了数据
                        if (!Espide.Common.checkGridSel('ExchangeItem', '请选择一条产品数据！')) {
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

                //售前换货点击
                '#exchangeOnsale': {
                    click: function (btn) {

                        var OrderListSlc = Ext.getCmp('OrderList').getSelectionModel().getSelection();


                        if (!Espide.Common.checkGridSel('OrderList', '请选择一条订单数据！')) {
                            return false;
                        }

                        //判断是否符合条件
                        if (OrderListSlc[0].data.orderStatus.name != "WAIT_PROCESS") {
                            Ext.Msg.alert('警告', '只有订单在待处理才能售前换货！');
                            return;
                        }


                        if (OrderListSlc[0].data.outPlatformType.name != "TAO_BAO") {
                            Ext.Msg.alert('警告', '只有天猫才能售前换货！');
                            return;
                        }

                        //显示售前窗口显示

                        Ext.widget('exchangeOnsale').show(this, function () {

                            var ExchangeAddListStore = Ext.getCmp('ExchangeAddList').getStore();
                            var ExchangeItemStore = Ext.getCmp('ExchangeItem').getStore();

                            //清空上一次数据
                            if (ExchangeAddListStore.count() >= 0 || ExchangeItemStore.count() >= 0) {
                                //ExchangeAddListStore.removeAll();
                                ExchangeItemStore.removeAll();

                            }
                            //加载订单项
                            Ext.Ajax.request({
                                params: {
                                    orderIds: OrderListSlc[0].data.id
                                },
                                url: "order/ItemList",
                                success: function (response, options) {
                                    var data = Ext.JSON.decode(response.responseText);
                                    //载入数据
                                    ExchangeAddListStore.loadData(data.data.list);

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
                        });
                    }
                },

            });
        },

    }
);