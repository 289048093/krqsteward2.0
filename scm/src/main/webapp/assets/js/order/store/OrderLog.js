/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.store.OrderLog', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    fields: [
        'log'
    ],
    //自动加载设为true

    autoLoad: false,
    proxy: {
        type: 'ajax',
        url: 'order/approveLogs',
        reader: {
            type: 'json',
            root: 'data.obj.orderHandleLogs'
        },

//        listeners: {
//            exception: function (proxy, response, operation) {
//                var data = Ext.decode(response.responseText);
//                Ext.MessageBox.show({
//                    title: '警告',
//                    msg: data.msg,
//                    icon: Ext.MessageBox.ERROR,
//                    buttons: Ext.Msg.OK
//                });
//            }
//        }
    },

    pageSize: 30
});