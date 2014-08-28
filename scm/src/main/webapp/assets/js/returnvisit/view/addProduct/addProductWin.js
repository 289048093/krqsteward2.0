/**
 * Created by king on 13-12-23
 */

Ext.define('ShopProduct.view.addProduct.addProductWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.addProductWin',
    title: '新增商品同步模块',
    collapsible: true,
    closeAction: 'destroy',
    id: 'addProductWin',
    autoShow: false,
    modal: true,
    animateTarget:Ext.getBody(),
    constrain:true,
    layout: 'border',
    width: 1200,
    height: 600,
    initComponent: function(){
        this.items = [
            //{xtype: 'addOrderGoWin'},
            {xtype: 'addProductInfo'},
            {xtype: 'addProductList'},
        ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {text: '确定', itemId: 'Addcomfirm'},
                {text: '取消', itemId: 'cancel'}
            ]
        }
        this.callParent(arguments);
    }
})