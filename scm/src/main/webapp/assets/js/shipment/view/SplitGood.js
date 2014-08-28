/*
* Created by king on 13-12-20
*/

Ext.define('Supplier.view.SplitGood', {
    extend: 'Ext.window.Window',
    title: '拆分订单',
    collapsible: true,
    closeAction: 'destroy',
    alias: 'widget.splitGood',
    id: 'splitGoodWin',
    autoShow: false,
    layout: 'border',
    width: 700,
    height: 450,
    html: 'content'
});