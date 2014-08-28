/**
 * Created by king on 13-12-23
 */

Ext.define('Payment.view.win.AddPaymentWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.addPayment',
    title: '添加分配预收款',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addGoodWin',
    autoShow: false,
    modal: true,
    layout: 'border',
    animateTarget:Ext.getBody(),
    width: 1200,
    height: 450,
    initComponent: function () {
        this.items = [
            {xtype: 'addPaymentForm'},
            {xtype: 'addList'},
            {xtype: 'addItem'},
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