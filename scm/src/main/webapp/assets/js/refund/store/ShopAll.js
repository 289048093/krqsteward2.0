/*
 * Created by king on 13-12-19
 */

Ext.define('Refund.store.ShopAll', {
    //不要忘了继承
    extend: 'Ext.data.Store',

    fields: ['id', 'nick'],
    proxy: {
        type: 'ajax',
        url: '/order/shopList',
        reader: {
            type: 'json',
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
    autoLoad: true,
    listeners: {
        load: function (){
            this.insert(0, { nick: '全部店铺', id: null });
        }
    }
});