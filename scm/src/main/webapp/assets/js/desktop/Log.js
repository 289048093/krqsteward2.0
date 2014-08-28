/**
 * 产品列表
 * Created by Lein.xu
 */

Ext.define('EBDesktop.Log', {
    extend: 'Ext.ux.desktop.Module',

    id:'log-win',

    init : function(){
        this.launcher = {
            text: '系统日志',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('log.html');
    },

    statics: {
    }
});

