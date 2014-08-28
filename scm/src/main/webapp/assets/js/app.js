Ext.Loader.setConfig({enabled: true});
Ext.application({
    name: 'Supplier',
    appFolder: 'assets/js',
    controllers: ['Main'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'frame'}
            ]
        });
    }
});