/**
 * Created by king on 13-12-23
 */

Ext.define('Customer.view.backList.backListManage', {
    extend: 'Ext.window.Window',
    alias: 'widget.backListManage',
    title: '黑名单管理',
    collapsible: true,
    closeAction: 'destroy',
    id: 'backListManage',
    itemId: 'backListManage',
    autoShow: false,
    modal: true,
    layout: 'border',
    width: 830,
    height: 470,
    initComponent: function(){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'backList'},
        ];

        this.callParent(arguments);
    }
})