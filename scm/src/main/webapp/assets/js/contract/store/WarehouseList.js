Ext.define('Product.store.WarehouseList', {

    extend: 'Ext.data.Store',
    model: 'Product.model.WarehouseList',
    proxy: {
        type: 'ajax',
        extraParams: {
            orders: 0
        },
        api: {
            read: '/repository/findAll'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.list',
            messageProperty: 'message'
        }
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
    autoSync: true,
    autoLoad: true
});
