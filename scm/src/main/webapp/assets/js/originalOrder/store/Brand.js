/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.store.Brand', {
    //不要忘了继承
    extend: 'Ext.data.Store',

    fields: ['id', 'name'],
    proxy: {
        type: 'ajax',
        url: 'brand/list',
        reader: {
            type: 'json',
            root: 'data.obj.result'
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
    pageSize:100000000,
    listeners: {
        load: function (){
            this.insert(0, { name: '全部品牌', id: null });
        }
    }
});