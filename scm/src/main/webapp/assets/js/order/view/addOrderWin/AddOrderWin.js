/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.addOrderWin.AddOrderWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.addOrderWin',
    title: '加订单(正常订单)',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addOrderWin',
    autoShow: false,
    modal: true,
    animateTarget:Ext.getBody(),
    constrain:true,
    layout: 'border',
    width: 1200,
    height: 600,
    initComponent: function(){
        this.items = [
            //{xtype: 'addOrderGoWin'},
            {xtype: 'AddOrderInfo'},
            {xtype: 'AddOrderList'},
            {xtype: 'AddOrderCart'}
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