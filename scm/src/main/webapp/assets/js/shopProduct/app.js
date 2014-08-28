Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/customer/dateExtend/'
    }
});

Ext.application({
    name: 'ShopProduct',
    appFolder: 'assets/js/shopProduct',
    controllers: ['shopProduct'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'ShopProduct'}
            ]
        });
    }
});