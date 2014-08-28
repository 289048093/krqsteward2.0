/*
 * Created by king on 13-12-19
 */

Ext.define('ShopProduct.store.List',{
    //不要忘了继承
    extend:'Ext.data.Store',
    //记得设置model
    model:'ShopProduct.model.List',
    //自动加载设为true
    //autoSync: true,
    autoLoad: true,
    proxy: {
        type: 'ajax',
        //batchActions: false,
        //limitParam: 'limitNum',
        extraParams: {
            //orderStatus: 'CONFIRMED'
        },
        api: {
            read: 'shopProduct/list',
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.result',
            totalProperty: 'data.obj.totalCount',
            messageProperty: 'msg'
        },
//        writer: {
//            type: 'json',
//            encode: true,
//            writeAllFields: false,
//            root: 'data'
//        },
        listeners: {
            exception: function (proxy, response, operation) {
                var data = Ext.decode(response.responseText);
                Ext.MessageBox.show({
                    title: '警告',
                    msg: data.msg,
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.Msg.OK
                });
            }
        }
    },
    listeners: {
        beforeload:function(store,options){

//

        },
        write: function(proxy, operation) {
            var com = Espide.Common,
                data = Ext.decode(operation.response.responseText);
            if (data.success) {
                com.tipMsg('操作成功', '订单修改成功');
                //com.reLoadGird('OrderList', 'search', false);
            } else {
                Ext.Msg.show({
                    title: '错误',
                    msg: data.msg,
                    buttons: Ext.Msg.YES,
                    icon: Ext.Msg.WARNING
                });
            }
        },
        load: function (store, records, successful, eOpts) {
//            if (store.getTotalCount()>=0){
//                Ext.getCmp('OrderList').down("#orderConut").setValue(store.getTotalCount());
//            }



        }
    },

    pageSize: 30
});