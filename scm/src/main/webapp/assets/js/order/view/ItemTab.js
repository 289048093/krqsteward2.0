/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.ItemTab', {
    extend: 'Ext.tab.Panel',
    region: 'south',
    alias: 'widget.ItemTab',
    id: 'ItemTab',
    itemId: 'ItemTab',
    hidden: true,
    height: 250,
    //autoHeight: true,
    split: true,
    forceFit: false,
    width: 'auto',
    defaults: {
        bodyPadding: 0,
    },
    items: [
        {
            title: '订单项信息',
            layout: 'fit',
            items:[
                {
                    xtype: 'orderItem',
                }
            ]

        },
        {
            title:'审核信息',
            layout: 'fit',
            items:[
                {
                    xtype: 'ExamineList',
                    autoScroll :true,
                }
            ]

        },
        {
            title:'订单日志',
            layout: 'fit',
            items:[
                {
                    xtype: 'OrderLog',
                    autoScroll :true,
                }
            ]

        }
    ]




});