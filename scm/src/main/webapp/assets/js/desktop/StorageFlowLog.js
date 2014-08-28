/**
 * 产品列表
 * Created by Lein.xu
 */

Ext.define('EBDesktop.StorageFlowLog', {
    extend: 'Ext.ux.desktop.Module',

    id:'storageflowlog-win',

    init : function(){
        this.launcher = {
            text: '库存日志',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('storageflowlog.html');
    },

    statics: {
    }
});

