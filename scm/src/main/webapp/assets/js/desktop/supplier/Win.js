/**
 * 供应商管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.supplier.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.supplier.supplierList'
    ],

    id: 'supplier-win',

    init: function () {
        this.launcher = {
            text: '供应商管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('supplier-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'supplier-win',
                title: '供应商管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'supplierList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});