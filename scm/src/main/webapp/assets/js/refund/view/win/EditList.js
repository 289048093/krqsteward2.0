/**
 * author     : 梦龙
 * createTime : 14-5-8 下午2:37
 * description:
 */


Ext.define('Refund.view.win.EditList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    alias: 'widget.editList',
    id: 'editList',
    height: 200,
    forceFit: false,
    store: 'ItemList',
    //selType: 'checkboxmodel',
    //viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {

        this.columns = [
            { text: '智库城订单项编号', dataIndex: 'id', width: 170},
            { text: '外部平台订单项编号', dataIndex: 'platformSubOrderNo', width: 170},
            { text: '订单项类型', dataIndex: 'type', width: 90,},
            { text: '订单项状态', dataIndex: 'status', width: 90, scope: Espide.Common.orderItemStatus, renderer: Espide.Common.orderState.getData},
            { text: '线上退货状态', dataIndex: 'returnStatus', width: 170,
                renderer: function (value) {
                    return value['value'];
                }
            },
            { text: '线下退货状态', dataIndex: 'offlineReturnStatus', width: 170,
                renderer: function (value) {
                    return value['value'];
                }
            },
            { text: '换货状态', dataIndex: 'exchanged', width: 90, renderer: function (value) {
                if (value) {
                    return "已换货"
                } else {
                    return '未换货'
                }
            }},
            { text: '商品编号', dataIndex: 'productCode', width: 90},
            { text: '商品名称', dataIndex: 'productName', width: 120, editor: {xtype: 'textfield', allowBlank: true}},
            { text: '商品规格', dataIndex: 'specInfo', width: 120,},
            { text: 'sku', dataIndex: 'productSku', width: 90},
            { text: '品牌', dataIndex: 'brandName', width: 70},
            { text: '类别', dataIndex: 'cateName', width: 90},
            { text: '原价（一口价）', dataIndex: 'price', width: 170},
            { text: '促销价', dataIndex: 'discountPrice', width: 90},
            { text: '订货数量', dataIndex: 'buyCount', width: 90},
            { text: '库存', dataIndex: 'repoNum', width: 90},
            { text: '订单项优惠金额', dataIndex: 'discountFee', width: 170},
            { text: '分摊优惠金额', dataIndex: 'sharedDiscountFee', width: 90},
            { text: '分摊邮费', dataIndex: 'sharedPostFee', width: 90},
            { text: '成交金额', dataIndex: 'actualFee', width: 90},
            { text: '邮费补差金额', dataIndex: 'postCoverFee', width: 170},
            { text: '邮费补差退款金额', dataIndex: 'postCoverRefundFee', width: 170},
            { text: '服务补差金额', dataIndex: 'serviceCoverFee', width: 135},
            { text: '服务补差退款金额', dataIndex: 'serviceCoverRefundFee', width: 170},
            { text: '线上退款金额', dataIndex: 'refundFee', width: 170},
            { text: '线下退款金额', dataIndex: 'offlineRefundFee', width: 170},
            { text: '线上退货邮费', dataIndex: 'returnPostFee', width: 180},
            { text: '线上退货邮费承担方', dataIndex: 'returnPostPayer', width: 180,

                renderer: function (value) {
                    if (value != null) {
                        return value['value'];
                    } else {
                        return "";
                    }
                }
            },
            { text: '线下退货邮费', dataIndex: 'offlineReturnPostFee', width: 135},
            {
                text: '线下退货邮费承担方',
                dataIndex: 'offlineReturnPostPayer',
                width: 220,
                renderer: function (value) {
                    if (value != null) {
                        return value['value'];
                    } else {
                        return "";
                    }
                }
            },
            { text: '线下换货邮费', dataIndex: 'exchangePostFee', width: 135},
            { text: '线下换货邮费承担方', dataIndex: 'exchangePostPayer', width: 180,
                renderer: function (value) {
                    if (value != null) {
                        return value['value'];
                    } else {
                        return "";
                    }
                }
            },
            { text: '货款', dataIndex: 'orderItemGoodsFee', width: 90},
            { text: '价格描述', dataIndex: 'priceDescription', width: 180},

        ];
        this.callParent(arguments);
    }
});
