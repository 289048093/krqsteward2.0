/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.addGoodWin.AddGoodWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.addGood',
    title: '加产品',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addGoodWin',
    autoShow: false,
    modal: true,
    layout: 'border',
    animateTarget:Ext.getBody(),
    constrain:true,
    width: 1000,
    height: 450,
    initComponent: function () {
        this.items = [
            {xtype: 'AddGoodList'},
            {xtype: 'AddGoodCart'}
        ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'submit'},
                {text: '取消', itemId: 'cancel'}
            ]
        };
        this.callParent(arguments);
    }
})