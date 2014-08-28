/**
 * 权限管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.permission.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
       'EBDesktop.permission.accountList',//用户列表
        'EBDesktop.permission.roleList',//角色列表
        'EBDesktop.permission.moduleList' //模块管理
    ],

    id: 'promission-win',

    init: function () {
        this.launcher = {
            text: '权限管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('promission-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'promission-win',
                title: '权限管理',
                width: 600,
                height: document.body.clientHeight * 0.85,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items:[
                    new Ext.TabPanel({
                    region: 'center',
                    activeTab: 0,
                    items: [
                        {xtype: 'accountList' },
                        {xtype: 'roleList' },
                        {xtype: 'moduleList' }
                    ]
                })
                ]
            });
        }

        return win;
    },
    statics: {}
});