Ext.define('Contract.store.ContractList', {

        extend: 'Ext.data.Store',
        model: 'Contract.model.ContractList',
        proxy: {
            type: 'ajax',
            extraParams: {
                orders: 0
            },
            api: {
                read: '/contract/list'
            },
            reader: {
                type: 'json',
                successProperty: 'success',
                root: 'data.obj.result',
                messageProperty: 'message',
                totalProperty: 'data.obj.totalCount'
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
        autoSync: true,
        autoLoad: {start: 0, limit: 30},
        pageSize: 30

});
