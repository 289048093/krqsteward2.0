Ext.define('Returnvisit.store.EmployeeCart', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Returnvisit.model.EmployeeCart',
    proxy: {
        type: 'ajax',
//        extraParams: {
//            roleName:'回访专员'
//        },
        api: {
            create: 'return'
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
            writeAllFields: true,
            root: 'apportionPersonVos'
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
                com.tipMsg('操作成功', data.msg);
                Ext.getCmp('AllocationWin').destroy();
               Espide.Common.reLoadGird('List', 'search', true);
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
    autoSync: false
});