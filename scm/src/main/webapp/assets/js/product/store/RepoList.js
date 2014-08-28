Ext.define('Product.store.RepoList', {

    extend: 'Ext.data.Store',
    model: 'Product.model.RepoList',
    proxy: {
        type: 'ajax',
        api: {
            read: '/repository/list'
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
    autoLoad: true,
    pageSize: 30000

});
