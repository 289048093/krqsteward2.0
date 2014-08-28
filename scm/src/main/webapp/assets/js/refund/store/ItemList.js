Ext.define('Refund.store.ItemList', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Refund.model.ItemList',
    proxy: {
        type: 'ajax',
        api: {
            create  : '/order/ItemList',
            read    : '/order/ItemList',
            update  : '/order/updateOrderItemByOrderItem',
            //destroy : '/order/deleteItemList'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.list',
            messageProperty: 'msg'
        },
        writer: {
            type: 'json',
            encode: true,
            writeAllFields: false,
            root: 'data'
        },
        listeners: {
            exception: function(proxy, response, operation){
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
        write: function(proxy, operation) {
            var com = Espide.Common,
                data = Ext.decode(operation.response.responseText);
            if (data.success) {
                if (operation.action == 'destroy') {
                    com.tipMsg('操作成功', '商品删除成功');
                }else{
                    com.tipMsg('操作成功', '商品修改成功');
                }
                com.reLoadGird('OrderList', 'search', false);
                com.reLoadGird('orderItem');
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
    autoSync: true
});