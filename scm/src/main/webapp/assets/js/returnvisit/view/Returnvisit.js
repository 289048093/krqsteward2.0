/*
* Created by king on 13-12-17
*/

Ext.define('Returnvisit.view.Returnvisit', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.Returnvisit',
    title: '回访中心',
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