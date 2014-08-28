/**
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.define('Stock.store.List', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Stock.model.List',
    proxy: {
        type: 'ajax',
        api: {
            read: '/storage/list'
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.result',
            totalProperty: 'data.obj.totalCount',
            messageProperty: 'msg'
        }
    },
    autoLoad: true,
    pageSize: 50
});