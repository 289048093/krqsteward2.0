/*
 * Created by Lein on 13-12-17
 */
Ext.define('Payment.view.PaymentList', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.PaymentList',
    title: '预收款分配',
    id: 'PaymentList',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        this.items = [
            {xtype: 'paymentSearch'},
            {xtype: 'List'},
            {xtype: 'paymentItem'},
        ];
        this.callParent(arguments);
    }
});