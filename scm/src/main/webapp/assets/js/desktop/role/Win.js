/**
 * 用户管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.role.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.role.roleList'
    ],

    id: 'role-win',

    init: function () {
        this.launcher = {
            text: '角色管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('role-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'role-win',
                title: '角色管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'roleList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});