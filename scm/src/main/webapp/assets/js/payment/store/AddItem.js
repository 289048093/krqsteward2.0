Ext.define('Payment.store.AddItem', {

    extend: 'Ext.data.Store',
    model: 'Payment.model.AddItem',
    proxy: {
//        extraParams:{
//            repositoryId: "",
//            param: "",
//            minDate: "",
//            maxDate: ""
//        },
        type: 'ajax',
        extraParams: {},
        api: {
            read:"payment/orderItem",
            create: 'order/addGift',
            update: 'payment/save',
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj',
            messageProperty: 'message'
        },
        writer: {
            type: 'json',
            encode: true,
            writeAllFields: true,
            root: 'orderItems'
        },
        listeners: {
            exception: function (proxy, response, operation) {
                var data = Ext.decode(response.responseText);
                Ext.MessageBox.show({
                    title: '警告',
                    msg: data.msg,
                    icon: Ext.MessageBox.ERROR,
                    button: Ext.Msg.OK
                });
            }
        },
    },
    listeners: {
        write: function(proxy, operation) {
            var com = Espide.Common,
                data = Ext.decode(operation.response.responseText);
            var paymentListStore = Ext.getCmp('List').getStore();

            if (data.success) {
                com.tipMsg('操作成功', data.msg);
                if (Ext.getCmp('addGoodWin')){
                    Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('addGoodWin'), 'List', true);
                }

                //paymentListStore.reload();
                paymentListStore.getProxy().extraParams = Ext.getCmp('search').getValues();

                //com.reLoadGird('OrderList', 'search', false);
                if (data.data.list && data.data.list.length>0){
                    Ext.Msg.show({
                        title: '订单生成成功',
                        msg: '生成的订单编号是:'+data.data.list.join(','),
                        buttons: Ext.Msg.YES,
                        fn: function (){
                            Espide.Common.reLoadGird('OrderList', 'search', false);
                        }
                    });
                }
            } else {

                Ext.Msg.show({
                    title: '错误',
                    msg: data.msg,
                    buttons: Ext.Msg.YES,
                    icon: Ext.Msg.WARNING
                });
            }
        }
    },
    autoLoad: false,
    autoSync: false

});
