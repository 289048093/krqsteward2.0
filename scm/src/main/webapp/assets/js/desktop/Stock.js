/**
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.define('EBDesktop.Stock', {
    extend: 'Ext.ux.desktop.Module',

    id:'stock-win',

    init : function(){
        this.launcher = {
            text: '商品库存',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('stock.html');
    },

    statics: {
    }
});
