
Ext.Loader.setConfig({enabled: true});
Ext.application({
    name: 'Product',
    appFolder: 'assets/js/product',
    controllers: ['Product'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'productList'}
            ]
        });
    }
});