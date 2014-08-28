/**
 * 订单详情
 * Created by HuaLei.Du on 14-2-19.
 */

Ext.define('Warn.view.OrderDetail', {
    extend: 'Ext.window.Window',
    alias: 'widget.orderDetail',
    title: '查看订单详情',
    closeAction: 'destroy',
    id: 'orderDetail',
    autoShow: false,
    modal: true,
    layout: 'border',
    width: 700,
    height: 480,
    initComponent: function () {

        this.items = [
            Ext.create('Ext.panel.Panel', {
                region: 'center',
                autoScroll: true,
                bodyPadding: 10,
                items: [
                    {
                        itemId: 'orderInfo',
                        id: 'orderInfo',
                        xtype: 'container',
                        html: '>'
                    },
                    {
                        xtype: 'container',
                        style: {
                            height: '30px',
                            lineHeight: '30px',
                            color: '#666'
                        },
                        html: '<strong>商品清单：</strong>'
                    },
                    {
                        xtype: 'goodsList',
                        width: 648
                    },
                    {
                        id: 'receiverInfo',
                        xtype: 'container',
                        html: ''
                    }
                ]
            })
        ];

        this.callParent(arguments);
    }
});
