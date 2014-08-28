/**
 * author     : 梦龙
 * createTime : 14-5-9 上午10:10
 * description:
 */



Ext.define('Payment.view.win.AddList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    alias: 'widget.addList',
    id: 'addList',
    itemId:'addList',
    height: 150,
    split: true,
    forceFit: false,
    store: 'GoodList',
    //selType: 'checkboxmodel',
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {
        this.tbar = [
            Ext.create('Ext.form.Panel', {
                layout: 'hbox',
                border: false,
                itemId: 'goodSearch',
                id: 'goodSearch',
                defaults:{
                    disabled: true,
                    margin: '0 10 0 0',
                    labelWidth: 60,
                    width: 180
                },
                items: [
                    {
                        name: 'id',
                        fieldLabel:'订单项id',
                        id:'orderId',
                        disabled: false,
                        hidden:true,
                        xtype:'textfield',
                        labelWidth:75,
                    },
                    {
                        name: 'type',
                        fieldLabel:'预收款类型',
                        id:'type',
                        xtype:'textfield',
                        labelWidth:75,
                    },
                    {
                        name: 'allocateStatus',
                        fieldLabel:'分配状态',
                        id:'allocateStatus',
                        xtype:'textfield',
                    },
                    {
                        name: 'platformType',
                        fieldLabel:'平台类型',
                        id:'platformType',
                        xtype:'textfield',
                    },
                    {
                        name: 'shopName',
                        fieldLabel:'店铺名称',
                        id:'shopName',
                        xtype:'textfield',
                    },
                    {
                        name: 'paymentFee',
                        fieldLabel:'预收款金额',
                        labelWidth:75,
                        id:'paymentFee',
                        xtype:'textfield',
                    },
                    {
                        name: 'refundFee',
                        fieldLabel:'预收款退款金额',
                        labelWidth:95,
                        id:'refundFee',
                        xtype:'textfield',
                    }
                ]
            })
        ];
        this.columns = [
            { text: '商品id', dataIndex: 'id', width: 55, hidden: true},
            { text: '智库城订单编号', dataIndex: 'id', width: 170},
            { text: '外部平台订单项编号', dataIndex: 'platformSubOrderNo', width: 170},
            { text: '订单项类型', dataIndex: 'type', width: 90,},
            { text: '订单项状态', dataIndex: 'status', width: 90,scope: Espide.Common.orderItemStatus,renderer: Espide.Common.orderState.getData},
//            { text: '线上退货状态', dataIndex: 'returnStatus', width: 170,
//                renderer:function(value){
//                    return value['value'];
//                }
//            },
//            { text: '线下退货状态', dataIndex: 'offlineReturnStatus', width: 170,
//                renderer:function(value){
//                    return value['value'];
//                }
//            },
            { text: '换货状态', dataIndex: 'exchangedGoods', width: 90,renderer: function(value){if(value){return "已换货"}else{return '未换货'}}},
            { text: '商品编号', dataIndex: 'productCode', width: 90},
            { text: '商品名称', dataIndex: 'productName', width: 120, editor: {xtype: 'textfield', allowBlank: true}},
            { text: '商品规格', dataIndex: 'specInfo', width: 120,},
            { text: 'sku', dataIndex: 'productSku', width: 90},
            { text: '品牌', dataIndex: 'brandName', width: 70},
            { text: '类别', dataIndex: 'categoryName', width: 90},
            { text: '原价（一口价）', dataIndex: 'price', width: 170},
            { text: '促销价', dataIndex: 'discountPrice', width: 90},
            { text: '订货数量', dataIndex: 'buyCount', width: 90},
            { text: '库存', dataIndex: 'repoNum', width: 90},
            { text: '成交金额', dataIndex: 'actualFee', width: 90},
            { text: '货款', dataIndex: 'goodsFee', width: 90},
            { text: '价格描述', dataIndex: 'priceDescription', width: 180},
            {
                xtype: 'actioncolumn',
                text: '增加',
                itemId: 'addRow',
                menuDisabled: true,
                width: 50,
                iconCls: 'icon-add'
            }
        ];
        this.callParent(arguments);
    }
});
