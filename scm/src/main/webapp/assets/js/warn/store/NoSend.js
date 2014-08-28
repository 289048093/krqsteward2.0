/**
 * 需要确认发货的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.store.NoSend', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Warn.model.NoSend',
    proxy: {
        type: 'ajax',
        limitParam: undefined,
        pageParam: undefined,
        startParam: undefined,
        api: {
            read: '/warn/nosend'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.list',
            messageProperty: 'msg'
        }
    },
    autoLoad: true
});