/*
* Created by king on 13-12-17
*/

Ext.define('Sms.view.Sms', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.Sms',
    title: '营销中心',
    id: 'Sms',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'SmsSearch'},
            {xtype: 'SmsList'},
        ];
        this.callParent(arguments);
    }
});