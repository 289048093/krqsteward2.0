/**
 * 库存
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.Loader.setConfig({enabled: true});
Ext.application({
    name: 'Stock',
    appFolder: 'assets/js/stock',
    controllers: ['Stock'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'stockList'}
            ]
        });
    }
});