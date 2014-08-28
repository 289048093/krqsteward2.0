/*
* Created by king on 13-12-17
*/

Ext.define('Customer.view.Customer', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.Customer',
    title: '会员中心',
    id: 'Customer',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'CustomerSearch'},
            {xtype: 'customerList'},
        ];
        this.callParent(arguments);
    }
});