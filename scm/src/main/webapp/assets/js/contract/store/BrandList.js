Ext.define('Product.store.BrandList', {

    extend: 'Ext.data.Store',
    model: 'Product.model.BrandList',
    proxy: {
        type: 'ajax',
        api: {
            read: '/brand/list'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.result',
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
    autoLoad: {start: 0, limit: 30000},
    pageSize: 30000

});
