/**
 * 需要确认签收的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.model.NoSign', {
    extend: 'Ext.data.Model',
    fields: ['orderNo', 'expressNo', 'expressCompany', 'firstTime', 'sendTo'],
    idProperty: 'orderNo'
});

