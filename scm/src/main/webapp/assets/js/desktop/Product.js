/**
 * 产品列表
 * Created by Lein.xu
 */

Ext.define('EBDesktop.Product', {
    extend: 'Ext.ux.desktop.Module',

    id:'product-win',

    init : function(){
        this.launcher = {
            text: '产品管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('product.html');
    },

    statics: {
    }
});

