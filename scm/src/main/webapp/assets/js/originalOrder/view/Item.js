/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.Item', {
    extend: 'Ext.grid.Panel',
    region: 'south',
    alias: 'widget.orderItem',
    id: 'orderItem',
    itemId: 'item',
    //hidden: true,
    height: 200,
    split: true,
    autoScroll :true,
    store: 'ItemList',
    forceFit: true,
    viewConfig: {
        enableTextSelection: true
    },
    //selType: 'checkboxmodel',
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 2
        })
    ],
    columns: [

        { text: '外部平台订单项编号', dataIndex: 'platformSubOrderNo', width: 170},
        { text: '商品名称', dataIndex: 'title', width: 100},
        { text: '品牌', dataIndex: 'brandName', width: 70},
        { text: 'sku', dataIndex: 'sku', width: 90,
            editor: {
                xtype: 'textfield',
                allowBlank: false
            }
        },
        { text: '外部平台sku', dataIndex: 'outSku', width: 90},
        { text: '原价（一口价）', dataIndex: 'price', width: 170},
        { text: '订货数量', dataIndex: 'buyCount', width: 90},
        { text: '实付金额', dataIndex: 'actualFee', width: 120},
        { text: '订单项优惠金额', dataIndex: 'discountFee', width: 170},
    ]
})