Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/order/dateExtend/'
    }
});
Ext.application({
    name: 'Contract',
    appFolder: 'assets/js/contract',
    controllers: ['Contract'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'contractList'}
            ]
        });
    }
});