/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.ReplenishmentWin.ReplenishmentWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.ReplenishmentWin',
    title: '加订单(补货订单)',
    collapsible: true,
    closeAction: 'destroy',
    id: 'ReplenishmentWin',
    autoShow: false,
    modal: true,
    animateTarget:Ext.getBody(),
    constrain:true,
    layout: 'border',
    width: 1200,
    height: 600,

    initComponent: function(){
        this.items = [
            {xtype: 'ReplenishmentOrderInfo'},
            {xtype: 'ReplenishmentAddGoodList'},
            {xtype: 'ReplenishmentAddGoodCart'}
        ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'Replenishmentcomfirm'},
                {text: '取消', itemId: 'cancel'}
            ]
        }
        this.callParent(arguments);
    }
})