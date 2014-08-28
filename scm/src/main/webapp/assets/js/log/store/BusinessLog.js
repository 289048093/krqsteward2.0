Ext.define('Log.store.BusinessLog', {

    extend: 'Ext.data.Store',
    model: 'Log.model.BusinessLog',
    proxy: {
        type: 'ajax',
        api: {
            read: '/log/itemList'
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
    autoLoad: false

});
