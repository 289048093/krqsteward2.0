/*
 * Created by king on 13-12-19
 */

Ext.define('Customer.store.LabelStore', {
    //不要忘了继承
    extend: 'Ext.data.Store',

    fields: ['id', 'name'],
    proxy: {
        type: 'ajax',
        url:'/customerTag/list',
        reader: {
            type: 'json',
            root: 'data.obj.result',
            totalProperty: 'data.obj.totalCount',
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
    autoLoad: false

});