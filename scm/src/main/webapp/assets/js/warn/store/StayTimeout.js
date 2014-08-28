/**
 * 物流信息长时间未更新
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.store.StayTimeout', {
    //不要忘了继承
    extend: 'Ext.data.Store',
    //记得设置model
    model: 'Warn.model.StayTimeout',
    proxy: {
        type: 'ajax',
        limitParam: undefined,
        pageParam: undefined,
        startParam: undefined,
        api: {
            read: '/warn/staytimeout'
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
