Ext.define('Refund.view.win.AddRefund', {
    extend: 'Ext.window.Window',
    alias: 'widget.addRefund',
    title: '新建线下退款申请',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addRefundWin',
    autoShow: false,
    modal: true,
    //autoScroll: true,
    animateTarget:Ext.getBody(),
    layout: 'border',
    width: 1200,
    height: 600,
    initComponent: function(){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');

        this.items = [
              {xtype: 'createList'},
              {xtype: 'createForm'},
       ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'Refundcomfirm'},
                {text: '取消', itemId: 'cancel'}
            ]
        }
        this.callParent(arguments);
    }
})