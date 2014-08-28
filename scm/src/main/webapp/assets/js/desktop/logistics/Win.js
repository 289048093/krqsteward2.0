/**
 * 物流公司
 * Created by HuaLei.Du on 13-12-18.
 */

Ext.define('EBDesktop.logistics.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.logistics.List',
        'EBDesktop.logistics.Save'
    ],

    id: 'logistics-win',

    init: function () {
        this.launcher = {
            text: '物流管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {

        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('logistics-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'logistics-win',
                title: '物流管理',
                width: 600,
                height: 480,
                iconCls: 'icon-grid',
                animCollapse: false,
                animateTarget:Ext.getBody(),
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'logisticsList'
                    }
                ]
            });
        }
        return win;

    },

    statics: {}
});