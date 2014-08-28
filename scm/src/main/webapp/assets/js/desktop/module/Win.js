/**
 * 模块管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.module.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.module.moduleList'
    ],

    id: 'module-win',

    init: function () {
        this.launcher = {
            text: '模块管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('module-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'module-win',
                title: '模块管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'moduleList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});