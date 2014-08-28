/**
 * 互斥管理
 * Created by HuaLei.Du on 13-12-18.
 */
Ext.define('EBDesktop.mutex.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.mutex.List',
        'EBDesktop.mutex.Add'
    ],

    id: 'mutex-win',

    init: function () {
        this.launcher = {
            text: '打包互斥',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {

        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('mutex-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'mutex-win',
                title: '打包互斥管理',
                width: 740,
                height: 480,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'mutexList'
                    }
                ]
            });
        }
        return win;

    },

    statics: {}
});