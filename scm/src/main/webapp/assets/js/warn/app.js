/**
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.Loader.setConfig({enabled: true});
Ext.application({
    name: 'Warn',
    appFolder: 'assets/js/warn',
    controllers: ['Warn'],
    launch: function () {
        Ext.create('Ext.container.Viewport', {
            id: 'mainViewPort',
            layout: 'border',
            items: [
                {
                    //title: '需要确认发货的订单',
                    bodyPadding: 5,
                    region: 'west',
                    width: '50%',
                    height: 500,
                    minWidth: 400,
                    split: true,
                    collapsible: false,
                    xtype: 'noSend'
                },
                {
                    //title: '已经发了货却还没有物流扫描信息的订单',
                    bodyPadding: 5,
                    region: 'center',
                    minWidth: 400,
                    split: true,
                    collapsible: false,
                    xtype: 'noLogistics'
                },
                {
                    region: 'south',
                    height: '50%',
                    collapsible: false,
                    split: true,
                    layout: 'border',
                    border: 0,
                    items: [
                        {
                            //title: '物流信息长时间未更新',
                            region: 'west',
                            flex: 1,
                            minWidth: 400,
                            split: true,
                            collapsible: false,
                            xtype: 'stayTimeout'
                        },
                        {
                            //title: '需要确认签收的订单',
                            region: 'center',
                            minWidth: 400,
                            xtype: 'noSign'
                        }
                    ]
                }
            ]
        });
    }
});