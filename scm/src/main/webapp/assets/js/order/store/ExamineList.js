/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.store.ExamineList', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    fields: [
        'orderStatus',
        'operatorName',
        {name:'updateTime',type: 'date', dateFormat: 'time'}

    ],
    //自动加载设为true

    autoLoad: false,

    proxy: {
        type: 'ajax',
        url: 'order/approveLogs',
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.orderApproves',
            messageProperty: 'msg'
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