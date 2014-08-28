/**
 * Created by HuaLei.Du on 14-2-19.
 */

Ext.define('Warn.model.GoodsList', {
    extend: 'Ext.data.Model',
    fields: ['id', 'prodName', 'prodCount', 'prodPrice', 'totalFee'],
    idProperty: 'id'
});
