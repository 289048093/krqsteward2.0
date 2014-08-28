/**
 * 已经发了货却还没有物流扫描信息的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.store.NoLogistics', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Warn.model.NoLogistics',
    proxy: {
        type: 'ajax',
        limitParam: undefined,
        pageParam: undefined,
        startParam: undefined,
        api: {
            read: '/warn/nologistics'
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