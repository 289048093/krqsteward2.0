/**
 * Created by king on 13-12-23
 */

Ext.define('Returnvisit.view.allocationWin.AllocationWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.AllocationWin',
    title: '分配',
    collapsible: true,
    closeAction: 'destroy',
    id: 'AllocationWin',
    autoShow: false,
    modal: true,
    layout: 'border',
    animateTarget:Ext.getBody(),
    constrain:true,
    width: 1000,
    height: 450,
    initComponent: function () {
        this.items = [
            {xtype: 'EmployeeList'},
            {xtype: 'EmployeeCart'}
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