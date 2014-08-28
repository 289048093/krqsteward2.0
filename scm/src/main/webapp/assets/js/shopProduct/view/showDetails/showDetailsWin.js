/**
 * Created by king on 13-12-23
 */

Ext.define('ShopProduct.view.showDetails.showDetailsWin', {
    extend: 'Ext.window.Window',
    alias: 'widget.showDetailsWin',
    title: '商品详情',
    collapsible: true,
    closeAction: 'destroy',
    id: 'showDetailsWin',
    autoShow: false,
    modal: false,
    animateTarget:Ext.getBody(),
    constrain:true,
    layout: 'border',
    width: 1200,
    height: 600,
    initComponent: function(){
        this.items = [
            {xtype: 'showProductInfo'},
           //{xtype: 'showProductList'},
        ];
        this.buttons = {
            layout: {
                pack: 'center'
            },
//            items: [
//                {text: '确定', itemId: ''},
//                {text: '取消', itemId: ''}
//            ]
        }
        this.callParent(arguments);
    }
})