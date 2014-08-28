/*
* Created by king on 13-12-17
*/

Ext.define('Comment.view.Comment', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.Comment',
    title: '评价管理',
    id: 'Comment',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'CommentSearch'},
            {xtype: 'CommentList'},
            {xtype: 'WidgetPanel'},
            {xtype: 'WidgetPanel2'},
        ];
        this.callParent(arguments);
    }
});