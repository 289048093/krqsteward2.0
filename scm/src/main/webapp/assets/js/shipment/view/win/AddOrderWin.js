/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.win.AddOrderWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.addOrder',
    title: '增加订单',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addOrderWin',
    autoShow: false,
    modal: true,
    layout: 'border',
    width: 705,
    height: 600,
    initComponent: function(){
        this.items = [
            {xtype: 'orderInfo'},
            {xtype: 'goodList'},
            {xtype: 'goodCart'}
        ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'comfirm'},
                {text: '取消', itemId: 'cancel'}
            ]
        }
        this.callParent(arguments);
    }
})