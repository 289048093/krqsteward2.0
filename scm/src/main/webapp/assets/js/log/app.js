
Ext.Loader.setConfig({enabled: true});
Ext.application({
    name: 'Log',
    appFolder: 'assets/js/log',
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