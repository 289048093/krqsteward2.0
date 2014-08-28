/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.store.StorageAll', {
    //不要忘了继承
    extend: 'Ext.data.Store',

    fields: ['id', 'name'],
    proxy: {
        type: 'ajax',
        url: '/repository/find_all',
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
    autoLoad: false,
    listeners: {
        load: function (){
            Espide.Common.comInsertNull(this);
        }
    }
});