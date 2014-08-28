Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'Go':'assets/js/comment/dateExtend/'
    }
});

Ext.application({
    name: 'Comment',
    appFolder: 'assets/js/comment',
    controllers: ['Comment'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'fit',
            items: [
                {xtype: 'Comment'}
            ]
        });
    }
});

var store = Ext.create('Ext.data.JsonStore', {
    fields: ['name', 'data'],
    data: [
        { 'name': 'metric one',   'data': 10 },
        { 'name': 'metric two',   'data':  7 },
        { 'name': 'metric three', 'data':  5 },
        { 'name': 'metric four',  'data':  2 },
        { 'name': 'metric five',  'data': 27 }
    ]
});

