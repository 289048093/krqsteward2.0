Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/shipment/dateExtend/'
    }
});

Ext.application({
    name: 'Supplier',
    appFolder: 'assets/js/shipment',
    controllers: ['Order', 'Win'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'order'}
            ]
        });
    }
});