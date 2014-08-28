Ext.define('Supplier.store.AddOrderChangeWin.GoodCart', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Supplier.model.AddOrderChangeWin.GoodCart',
    proxy: {
        type: 'ajax',
        extraParams: {},
        api: {
            create: '/order/addGift'
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
            root: 'queryProdVos'
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
                if (Ext.getCmp('addOrderWin')){
                    Ext.getCmp('addOrderWin').destroy();
                }
                if (Ext.getCmp('replenishmentWin')){
                    Ext.getCmp('replenishmentWin').destroy();
                }
                if (Ext.getCmp('addOrderChange')){
                    Ext.getCmp('addOrderChange').destroy();
                }
                //com.reLoadGird('OrderList', 'search', false);
                if (data.data.list && data.data.list.length>0){
                    Ext.Msg.show({
                        title: '订单生成成功',
                        msg: '生成的订单编号是:'+data.data.list.join(','),
                        buttons: Ext.Msg.YES,
                        fn: function (){
                            Espide.Common.reLoadGird('OrderList', 'search', false);
                        }
                    });
                }
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