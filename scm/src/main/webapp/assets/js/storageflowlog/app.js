Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/order/dateExtend/'
    }
});

Ext.application({
    name: 'StorageFlowLog',
    appFolder: 'assets/js/storageflowlog',
    controllers: ['LogList'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'LogList'}
            ]
        });
    }
});

