/**
 * Created by king on 13-12-23
 */

Ext.define('Customer.view.labelWin.labelManage', {
    extend: 'Ext.window.Window',
    alias: 'widget.labelManage',
    title: '标签管理',
    collapsible: true,
    closeAction: 'destroy',
    id: 'labelManage',
    itemId: 'labelManage',
    autoShow: false,
    modal: true,
    layout: 'border',
    width: 830,
    height: 470,
    initComponent: function(){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'labelList'},
        ];

        this.callParent(arguments);
    }
})