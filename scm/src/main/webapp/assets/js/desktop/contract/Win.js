/**
 * 供应商合同管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.contract.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.contract.contractList'
    ],

    id: 'contract-win',

    init: function () {
        this.launcher = {
            text: '供应商合同管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('contract-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'contract-win',
                title: '供应商合同管理',
                width: 900,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'contractList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});