/**
 * 物流信息长时间未更新
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.model.StayTimeout', {
    extend: 'Ext.data.Model',
    fields: ['orderNo', 'expressNo', 'expressCompany', 'latestTime'],
    idProperty: 'orderNo'
});
