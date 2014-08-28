Ext.define('Refund.store.RefundList', {

    extend: 'Ext.data.Store',
    model: 'Refund.model.RefundList',
    remoteSort:false,
    proxy: {
        type: 'ajax',
        api: {
            read: 'refund/list'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.result',
            messageProperty: 'message',
            totalProperty: 'data.obj.totalCount'
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
    autoLoad: true,
    pageSize: 25

});
