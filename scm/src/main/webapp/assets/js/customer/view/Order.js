/*
* Created by king on 13-12-17
*/

Ext.define('Supplier.view.Order', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.order',
    title: '订单预处理',
    id: 'order',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'orderSearch'},
            {xtype: 'orderList'},
            //{xtype: 'orderItem'}
            {xtype: 'ItemTab'}
        ];
        this.callParent(arguments);
    }
});