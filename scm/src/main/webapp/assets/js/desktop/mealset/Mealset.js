/**
 * 套餐管理
 *
 */
Ext.define('EBDesktop.mealset.Mealset', {
    extend: 'Ext.ux.desktop.Module',
    //依赖js
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


    //创建窗口
    createWindow: function () {

        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('mealset-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'mealset-win',
                title: '套餐管理',
                width: 740,
                height: document.body.clientHeight * 0.85,
                border: false,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'border',
                items: new Ext.TabPanel({
                    region: 'center',
                    activeTab: 0,
                    items: [
                        {xtype: 'mealsetList' }

                    ]
                })
            });
        }
        return win;

    },

    statics: {}
});