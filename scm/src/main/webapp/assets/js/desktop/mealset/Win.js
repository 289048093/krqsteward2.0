/**
 * 套餐管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.mealset.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.mealset.mealsetList'
    ],

    id: 'mealset-win',

    init: function () {
        this.launcher = {
            text: '套餐管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('mealset-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'mealset-win',
                title: '套餐管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'mealsetList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});