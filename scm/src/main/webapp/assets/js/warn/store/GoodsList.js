/**
 * Created by HuaLei.Du on 14-2-19.
 */

Ext.define('Warn.store.GoodsList', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Warn.model.GoodsList',
    proxy: {
        type: 'ajax',
        limitParam: undefined,
        pageParam: undefined,
        startParam: undefined,
        api: {
            read: '/warn/listorderitems'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj',
            messageProperty: 'msg'
        }
    },
    autoLoad: false
});
