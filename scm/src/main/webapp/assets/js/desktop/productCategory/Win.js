/**
 * 产品分类管理
 * Created by Lein.xu
 */

Ext.define('EBDesktop.productCategory.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.productCategory.productCategoryList'
    ],

    id: 'productCategory-win',

    init: function () {
        this.launcher = {
            text: '产品分类管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('productCategory-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'productCategory-win',
                title: '产品分类管理',
                width: 580,
                height: 480,
                collapsible: true,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'productCategoryList'
                    }
                ]
            });
        }

        return win;
    },
    statics: {}
});