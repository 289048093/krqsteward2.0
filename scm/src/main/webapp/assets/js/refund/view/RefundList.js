/*
 * Created by Lein on 13-12-17
 */
Ext.define('Refund.view.RefundList', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.RefundList',
    title: '退款处理',
    id: 'RefundList',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'refundSearch'},
            {xtype: 'refundList'}
        ];
        this.callParent(arguments);
    }
});