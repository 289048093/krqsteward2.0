Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/comment/dateExtend/'
    }
});

Ext.application({
    name: 'Returnvisit',
    appFolder: 'assets/js/returnvisit',
    controllers: ['Returnvisit'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'Returnvisit'}
            ]
        });
    }
});