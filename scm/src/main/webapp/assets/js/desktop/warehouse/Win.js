/**
 * 仓库管理
 * Created by HuaLei.Du on 13-12-18.
 */
Ext.define('EBDesktop.warehouse.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.warehouse.List',
        'EBDesktop.warehouse.Save'
    ],

    id: 'warehouse-win',

    init: function () {
        this.launcher = {
            text: '仓库管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {

        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('warehouse-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'warehouse-win',
                title: '仓库管理',
                width: 930,
                height: 546,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'warehouseList'
                    }
                ]
            });
        }
        return win;

    },

    statics: {}
});