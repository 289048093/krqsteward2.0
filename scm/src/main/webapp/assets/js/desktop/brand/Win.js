/**
 * 品牌管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.brand.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.brand.brandList'
    ],

    id: 'brand-win',

    init: function () {
        this.launcher = {
            text: '品牌管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('brand-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'brand-win',
                title: '品牌管理',
                width: 840,
                height: 500,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'brandList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});