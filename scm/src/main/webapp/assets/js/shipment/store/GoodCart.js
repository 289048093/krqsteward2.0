Ext.define('Supplier.store.GoodCart', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Supplier.model.GoodCart',
    proxy: {
        type: 'ajax',
        extraParams: {
            orders: 0
        },
        api: {
            read: 'static/js/order/data/common.json',
            create: 'static/js/order/data/common.json',
            update: 'static/js/order/data/common.json',
            destroy: 'static/js/order/data/common.json'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.list',
            messageProperty: 'message'
        },
        writer: {
            type: 'json',
            encode: true,
            writeAllFields: true,
            root: 'data.list'
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
    autoLoad: false,
    autoSync: true
});