/*
* Created by king on 13-12-17
*/

Ext.define('ShopProduct.view.ShopProduct', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.ShopProduct',
    title: '产品同步',
    id: 'ShopProduct',
    fixed: true,
    layout: 'border',
    initComponent: function (){
        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');
        this.items = [
            {xtype: 'ShopProductSearch'},
            {xtype: 'shopProductList'},
        ];
        this.callParent(arguments);
    }
});