/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.AddOrderChangeWin.AddOrderChangeWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.addOrderChange',
    title: '加订单(售后换货订单)',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addOrderChange',
    autoShow: false,
    modal: true,
    animateTarget:Ext.getBody(),
    constrain:true,
    layout: 'border',
    width: 1200,
    height: 650,
    initComponent: function(){
        this.items = [
            {xtype: 'AddOrderChangeInfo'},
            {xtype: 'AddOrderChangeSearch'},
            {xtype: 'AddOrderChangeList'},
            {xtype: 'AddOrderChangeCart'}
        ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'addOrderChangeComfirm'},
                {text: '取消', itemId: 'cancel'}
            ]
        }
        this.callParent(arguments);
    }
})