/**
 * Created by Lein xu
 */
Ext.define('Payment.controller.Win', {
    extend: 'Ext.app.Controller',
    views: ['win.AddPaymentWin', 'win.AddPaymentForm', 'win.AddList', 'win.AddItem'],
    stores: ['PaymentList', 'GoodList'],
    models: ['PaymentList', 'GoodList'],
    init: function () {
        this.control({
            //添加数据
            '#addList #addRow': {
                click: function (grid, addGrid, records, index, e, record) {
                    var createListStore = Ext.getCmp('createList').getStore(),
                        addListStore = Ext.getCmp('addList').getStore();

                    //检测添加数据时候 不能添加相同数据

                    function isPaymentAdded(orderId) { //判断是否增加数据是否重复
                        var flag = false,
                            createListItems = Ext.getCmp('createList').getStore().data.items;
                        Ext.each(createListItems, function (records, index, root) {
                            if (records.get('id') == orderId) {
                                return flag = true;
                            }
                        });
                        return flag;
                    }

                    if (isPaymentAdded(record.data.id)) {
                        Ext.Msg.alert('警告', '订单项已被分配,请先移除分配记录再添加');
                        return flag = false;
                    } else {

                        //后台检测是否能添加数据
                        Ext.Ajax.request({
                            params: {
                                paymentId: Ext.getCmp('orderId').getValue(),
                                orderItemId: record.get('id')
                            },
                            url: "payment/isOrderItemLegal",
                            async: false,
                            success: function (response, options) {

                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    data.data.obj['feesString'] = 0;
                                    data.data.obj['refundFeesString'] = 0;
                                    createListStore.insert(0, data.data.obj);
                                    addListStore.remove(record);
                                } else {
                                    Ext.Msg.alert('警告', data.msg);
                                }

                            }

                        });

                    }


                }
            },
            //删除数据
            '#createList #removeRow': {
                click: function (grid, addGrid, records, index, e, record) {
                    Ext.Msg.confirm('', '是否要删除这条分配记录？', function (btn) {
                        if (btn == 'yes') {
                            var createListStore = Ext.getCmp('createList').getStore();
                            createListStore.remove(record);
                        }
                    })
                }
            }

        });
    }



});