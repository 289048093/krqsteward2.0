/**
 * Created by Lein xu
 */
Ext.define('Payment.controller.PaymentList', {
    extend: 'Ext.app.Controller',
    views: ['PaymentList', 'Search', 'List', 'Item'],
    stores: ['PaymentList', 'Item', 'ShopAll', 'AddItem'],
    models: ['PaymentList', 'Item'],
    init: function () {
        var start = "", end = "" , type;

        this.control({
            '#List': {
                //初始化渲染表格
                afterrender: function (grid) {
                    var root = this,
                        com = Espide.Common,
                        store = grid.getStore();
                    store.getProxy().extraParams = Ext.getCmp('search').getValues();
                    Ext.Function.defer(function () {
                        //grid.getStore().getProxy().extraParams = {};
                    }, 700)

                },
                //paymentList 选择时显示 子项
                selectionchange: function (sm, records) {
                    var record = records[records.length - 1];
                    if (record) {
                        var store = Ext.getCmp('paymentItem').getStore();
                        store.load({
                            params: {
                                id: record.get('id')
                            }
                        });

                        Ext.getCmp('paymentItem').show();
                    } else {
                        Ext.getCmp('paymentItem').hide();
                    }

                },

                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.reLoadGird('List', 'search', true);
                                }
                            }
                        ]
                    });
                }
            },

            '#confirmBtn': {
                'click': function (btn) {
                    var addListGridStore = Ext.getCmp('addList').getStore();
                    addListGridStore.load({
                        params: Ext.getCmp('createForm').getValues()
                    });
                }
            },
            //导出数据
            '#outOrder': {
                click: function () {
                    var parms = Ext.getCmp('search').getForm().getValues();
                    window.open('/payment/extract2excel?'+ Ext.Object.toQueryString(parms));
                }
            },
            //查询数据
            '#search': {
                click: function (btn) {

                    var paymentListGridStore = Ext.getCmp('List').getStore();
                    paymentListGridStore.load({
                        params: Ext.getCmp('search').getValues()
                    });

                }
            },
            //添加预收款分配
            '#addPayment': {
                click: function (btn) {
                    //获取选中的分配预收款数据
                    var paymentListSlcRecords = btn.up('grid').getSelectionModel().getSelection(),
                        createForm = Ext.getCmp('createForm');

                    if (paymentListSlcRecords.length == 0) {
                        Ext.Msg.alert('警告', '请至少选择一条数据!');
                        return;
                    }

                    //添加预收款窗口
                    Ext.widget('addPayment').show(this, function () {//弹窗回调
                        //清空前面数据
                        Ext.getCmp('addList').getStore().removeAll();


                        var paymentItemStore = Ext.getCmp('paymentItem').getStore(),
                            createListStore = Ext.getCmp('createList').getStore(),
                            addListStore = Ext.getCmp('addList').getStore(),
                            paymentData = paymentListSlcRecords[0].data;


                        Ext.getCmp('type').setValue(paymentData.type.value);
                        Ext.getCmp('allocateStatus').setValue(paymentData.allocateStatus.value);
                        Ext.getCmp('platformType').setValue(paymentData.platformType.value);
                        Ext.getCmp('shopName').setValue(paymentData.shopName);
                        Ext.getCmp('paymentFee').setValue(paymentData.paymentFee);
                        Ext.getCmp('refundFee').setValue(paymentData.refundFee);
                        Ext.getCmp('orderId').setValue(paymentData.id);


                        if (paymentData.type.value == '订单邮费') {
                            Ext.getCmp('conditionValue').setValue(paymentData.platformOrderNo);
                            //触发按钮点击

                            Ext.getCmp('searchOrder').getEl().dom.click();
                            Ext.getCmp('searchOrder').disable();

                            addListStore.loadData(Espide.Common.storeToJson(paymentItemStore));
                        }

                        createListStore.loadData(Espide.Common.storeToJson(paymentItemStore));

                    });
                }
            },
            //提交保存分配预付款
            '#submit': {
                click: function (btn) {

                    var createListStore = Ext.getCmp('createList').getStore();
                    var paymentListSlcRecords = Ext.getCmp('List').getSelectionModel().getSelection();
                    if (createListStore.count() == 0) {
                        //判断是否已经分配
                        if (Ext.getCmp('allocateStatus').value == '已分配') {
                            Ext.Ajax.request({
                                params: {
                                    id: paymentListSlcRecords[0].data.id,
                                    orderItems: "null"
                                },
                                url: "payment/save",
                                async: false,
                                success: function (response, options) {
                                    var data = Ext.JSON.decode(response.responseText);
                                    if (data.success) {
                                        Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('addGoodWin'), 'List', true);
                                        Espide.Common.removeGridSel('List');
                                    }
                                }
                            });
                        } else {
                            //直接关掉窗口
                            Ext.getCmp('addGoodWin').close();
                        }

                    } else {
                        //同步store
                        createListStore.getProxy().api.create = "payment/save";
                        createListStore.getProxy().extraParams = Ext.getCmp('goodSearch').getForm().getValues();
                        createListStore.sync();
                    }
                }
            },
            '#cancel': {
                click: function (btn) {
                    if (Ext.getCmp('addGoodWin')) {
                        Ext.getCmp('addGoodWin').destroy();
                    }
                }
            },

        });
    }



});