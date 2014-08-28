/**
 * 商品管理模块
 *
 */
Ext.define('EBDesktop.goods.Goods', {
    extend: 'Ext.ux.desktop.Module',
    //依赖js
    requires: [
        'EBDesktop.goods.brandList',//品牌列表
        'EBDesktop.goods.productCategoryList'//品牌列表
    ],

    id: 'goods-win',

    init: function () {
        this.launcher = {
            text: '商品分类管理',
            iconCls: 'icon-grid'
        };
    },


    //创建窗口
    createWindow: function () {
        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('goods-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'goods-win',
                title: '商品分类管理',
                width: 600,
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
                        {xtype: 'brandList' },
                        {xtype: 'productCategoryList' }
                    ]
                })
            });
        }
        return win;

    },

    statics: {}
});