/**
 * 平台管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.platform.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.platform.platformList'
    ],

    id: 'platform-win',

    init: function () {
        this.launcher = {
            text: '平台管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('platform-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'platform-win',
                title: '平台管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'platformList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});