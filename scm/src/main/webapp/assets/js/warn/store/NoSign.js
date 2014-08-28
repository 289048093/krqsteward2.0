/**
 * 需要确认签收的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.store.NoSign', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Warn.model.NoSign',
    proxy: {
        type: 'ajax',
        limitParam: undefined,
        pageParam: undefined,
        startParam: undefined,
        api: {
            read: '/warn/nosign'
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
