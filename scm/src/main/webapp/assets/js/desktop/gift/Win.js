/**
 * 优惠活动管理
 * Created by HuaLei.Du on 13-12-24.
 */
Ext.define('EBDesktop.gift.Win', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'EBDesktop.gift.BrandActivity',
        'EBDesktop.gift.BrandSave',
        'EBDesktop.gift.BrandGoods',
        'EBDesktop.gift.BrandForm',
        'EBDesktop.gift.BrandGoodsCart',
        'EBDesktop.gift.GoodsActivity',
        'EBDesktop.gift.GoodsActivityAdd',
        'EBDesktop.gift.GoodsActivityAddForm',
        'EBDesktop.gift.GoodsList',
        'EBDesktop.gift.GoodsActivityEdit'
    ],

    id: 'gift-win',

    init: function () {
        this.launcher = {
            text: '优惠活动管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {

        var desktop = this.app.getDesktop(),
            win = desktop.getWindow('gift-win');

        if (!win) {
            win = desktop.createWindow({
                id: 'gift-win',
                title: '优惠活动管理',
                width: 740,
                height: 500,
                iconCls: 'icon-grid',
                animCollapse: false,
                constrainHeader: true,
                layout: 'fit',
                items: [
                    new Ext.TabPanel({
                        region: 'center',
                        activeTab: 0,
                        items: [
                            {
                                title: '根据品牌',
                                xtype: 'giftBrandActivity'
                            },
                            {
                                title: '根据商品',
                                xtype: 'giftGoodsActivity',
                                listeners: {
                                    afterrender: function (t, opt) {
                                        setTimeout(function () {
                                            Ext.getCmp('giftGoodsActivityGrid').getStore().load();
                                        }, 10);
                                    }
                                }
                            }
                        ]
                    })
                ]
            });
        }
        return win;

    },

    statics: {}
});