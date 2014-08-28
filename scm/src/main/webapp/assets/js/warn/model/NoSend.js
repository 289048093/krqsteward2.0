/**
 * 需要确认发货的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.model.NoSend', {
    extend: 'Ext.data.Model',
    fields: ['id', 'orderNo', 'payTime', 'orderStatus'],
    idProperty: 'orderNo'
});