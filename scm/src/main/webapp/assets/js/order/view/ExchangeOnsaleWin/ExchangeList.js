/**
 * author     : 梦龙
 * createTime : 14-5-9 上午10:10
 * description:
 */



Ext.define('Supplier.view.ExchangeOnsaleWin.ExchangeList', {
    extend: 'Ext.grid.Panel',
    region: 'north',
    alias: 'widget.exchangeList',
    id: 'ExchangeAddList',
    height: 150,
    split: true,
    forceFit: false,
    store: 'ItemList',
    //selType: 'checkboxmodel',
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {

        this.columns = [
            {text: '智库城订单项编号', dataIndex: 'id', width:130},
            {text: '外部平台订单项编号', dataIndex: 'platformSubOrderNo', width:150},
            {text: '订单项类型',  dataIndex: 'type', width: 90,

            },
            {text: '订单项状态', dataIndex: 'status', width: 90,scope: Espide.Common.orderItemStatus,renderer: Espide.Common.orderState.getData

            },
            { text: '线上退货状态', dataIndex: 'returnStatus', width: 170,
                renderer:function(value){
                    return value['value'];
                }
            },
            { text: '线下退货状态', dataIndex: 'offlineReturnStatus', width: 170,
                renderer:function(value){
                    return value['value'];
                }
            },
            { text: '换货状态', dataIndex: 'exchangedGoods', width: 90,},
            { text: '商品编号', dataIndex: 'productCode', width: 90},
            { text: '商品名称', dataIndex: 'productName', width: 120, editor: {xtype: 'textfield', allowBlank: true}},
            { text: 'sku', dataIndex: 'productSku', width: 90},
            { text: '品牌', dataIndex: 'brandName', width: 70},
            { text: '类别', dataIndex: 'cateName', width: 90},
            { text: '原价（一口价）', dataIndex: 'price', width: 170},
            { text: '促销价', dataIndex: 'discountPrice', width: 90},
            { text: '订货数量', dataIndex: 'buyCount', width: 90},
            { text: '库存', dataIndex: 'repoNum', width: 90},
            {text: '成交金额', dataIndex: 'actualFee', xtype: 'numbercolumn', format:'0.00'},
            {text: '货款', dataIndex: 'orderItemGoodsFee', xtype: 'numbercolumn', format:'0.00'},
            {text: '价格描述', dataIndex: 'priceDescription', xtype: 'numbercolumn', format:'0.00'},
//            {
//                xtype: 'actioncolumn',
//                text: '操作',
//                itemId: 'addRow',
//                menuDisabled: true,
//                width: 50,
//                iconCls: 'icon-remove'
//            }
        ];
        this.callParent(arguments);
    }
});
