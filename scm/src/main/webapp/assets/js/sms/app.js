Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/comment/dateExtend/'
    }
});

Ext.application({
    name: 'Sms',
    appFolder: 'assets/js/sms',
    controllers: ['Sms'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'Sms'}
            ]
        });
    }
});