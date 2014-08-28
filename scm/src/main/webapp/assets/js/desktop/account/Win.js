/**
 * 用户管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.account.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.account.accountList'
    ],

    id: 'account-win',

    init: function () {
        this.launcher = {
            text: '用户管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('account-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'account-win',
                title: '用户管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'accountList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});