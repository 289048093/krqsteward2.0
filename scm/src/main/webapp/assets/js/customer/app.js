Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/customer/dateExtend/'
    }
});

Ext.application({
    name: 'Customer',
    appFolder: 'assets/js/customer',
    controllers: ['Order', 'Win','labelWin','backList'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'Customer'}
            ]
        });
    }
});