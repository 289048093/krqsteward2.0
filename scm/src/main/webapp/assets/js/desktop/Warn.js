/**
 * Created by HuaLei.Du on 14-2-6.
 */
Ext.define('EBDesktop.Warn', {
    extend: 'Ext.ux.desktop.Module',

    id: 'warn-win',

    init: function () {
        this.launcher = {
            text: '订单预警',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        window.open('warn.html');
    },

    statics: {
    }
});