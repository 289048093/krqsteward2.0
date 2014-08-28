/*
* Created by king on 13-12-17
*/

Ext.define('Supplier.view.Item', {
    extend: 'Ext.grid.Panel',
    region: 'south',
    alias: 'widget.orderItem',
    id: 'orderItem',
    itemId: 'item',
    hidden: true,
    height: 200,
    split: true,
    store: 'ItemList',
    forceFit: true,
    viewConfig: {
        enableTextSelection: true
    },
    selType: 'checkboxmodel',
    columns: [
        {text: '订单项类型', dataIndex: 'type', sortable: true, width: 100,
//            renderer: function (value) {
//                return value['value'];
//            }
        },
        { text: '商品id', dataIndex: 'id', width: 55, hidden: true},
        { text: '商品编号', dataIndex: 'productCode', width: 55},
        { text: '商品名称', dataIndex: 'productName', width: 120},
        { text: '规格', dataIndex: 'specInfo', width: 120},
        { text: '条形码', dataIndex: 'productSku', width: 60},
        { text: '类别', dataIndex: 'cateName', width: 45,},
        { text: '原价', dataIndex: 'price', width: 45},
        { text: '数量', dataIndex: 'buyCount', width: 40},
        { text: '库存', dataIndex: 'repoNum', width: 40},
        { text: '品牌', dataIndex: 'brandName', width: 70},
        { text: '订单项分摊邮费', dataIndex: 'sharedPostFee', width: 70},
        { text: '成交金额', dataIndex: 'userActualPrice', width: 70},
        {text: '货款', dataIndex: 'goodsFee', sortable: true, width: 60, xtype: 'numbercolumn', format: '0.00', },
    ]
})