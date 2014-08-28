Ext.define('Refund.view.win.EditRefund', {
    extend: 'Ext.window.Window',
    alias: 'widget.editRefund',
    title: '修改退款申请',
    collapsible: true,
    closeAction: 'destroy',
    id: 'editRefundWin',
    itemId: 'editRefundWin',
    autoShow: false,
    modal: true,
    //autoScroll: true,
    animateTarget:Ext.getBody(),
    layout: 'border',
    width: 1200,
    height: 600,
    initComponent: function(){
        this.items = [
              {xtype: 'editList'},
              {xtype: 'editForm'},
       ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'Editcomfirm'},
                {text: '取消', itemId: 'cancel'}
            ]
        }
        this.callParent(arguments);
    }
})