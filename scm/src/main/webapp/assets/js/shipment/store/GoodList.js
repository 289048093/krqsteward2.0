Ext.define('Supplier.store.GoodList', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Supplier.model.GoodList',
    proxy: {
        type: 'ajax',
        url: '/order/ItemList',
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.list',
            messageProperty: 'message'
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
    autoLoad: false
});