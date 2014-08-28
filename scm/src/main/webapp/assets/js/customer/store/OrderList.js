/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.store.OrderList', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Supplier.model.OrderList',
    //自动加载设为true
    autoSync: true,
    autoLoad: true,
    remoteSort: false,
    proxy: {
        type: 'ajax',
       // limitParam: 'limitNum',
        api: {
            read: 'order/list',
            create: '/assets/js/order/data/orderList.json',
            update: '/order/updateByOrder',
            destroy: '/assets/js/order/data/orderList.json'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.result',
            messageProperty: 'msg',
            totalProperty: 'data.obj.totalCount'
        },
        writer: {
            type: 'json',
            encode: true,
            writeAllFields: false,
            //root: 'data.obj.result'
            root: 'data'
        },
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
    //sorters:[{property:"orderStatus",direction:"ASC", root: 'data.obj.result',}],
    listeners: {
        write: function (proxy, operation) {
            var com = Espide.Common,
                data = Ext.decode(operation.response.responseText);
            if (data.success) {

                com.tipMsg('操作成功', '订单修改成功');
                com.reLoadGird('OrderList', 'search', false);

            } else {
                Ext.Msg.show({
                    title: '错误',
                    msg: data.msg,
                    buttons: Ext.Msg.YES,
                    icon: Ext.Msg.WARNING
                });
            }
        },
        beforeLoad:function(){
            Ext.apply(this.proxy.extraParams, {limit:Ext.getCmp('limit').getValue()});
        },
        load: function (store, records, successful, eOpts) {
            if (store.getTotalCount()>=0){
                Ext.getCmp('OrderList').down("#orderConut").setValue(store.getTotalCount());
            }
        }
    },
    pageSize: 30
});