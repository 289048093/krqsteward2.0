/**
 * 已经发了货却还没有物流扫描信息的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.model.NoLogistics', {
    extend: 'Ext.data.Model',
    fields: ['orderNo', 'expressNo', 'expressCompany', 'deliveryTime'],
    idProperty: 'orderNo'
});