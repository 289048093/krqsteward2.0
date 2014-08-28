/**
 * 产品列表
 * Created by Lein.xu
 */

Ext.define('EBDesktop.Financial', {
    extend: 'Ext.ux.desktop.Module',

    id:'financial-win',

    init : function(){
        this.launcher = {
            text: '财务模块',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('financial.html');
    },

    statics: {
    }
});

