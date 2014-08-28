/**
 * author     : 梦龙
 * createTime : 14-5-8 下午1:25
 * description:
 */



Ext.define('Refund.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'refundList',
    itemId: 'refundList',
    alias: 'widget.refundList',
    store: 'RefundList',
    foreFit: false,
    split: true,
    //selType: 'checkboxmodel',
    selModel: {
        selType : 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'SINGLE',
        showHeaderCheckbox: false
    },
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {


        this.tbar = {
            items: [
                {
                    xtype: 'button',
                    name: 'submuit',
                    iconCls: 'icon-add',
                    style: 'margin: 0 0 0 15px',
                    itemId: 'creatRefund',
                    text: '新建线下退款申请'
                },
                {
                    xtype: 'button',
                    name: 'submuit',
                    iconCls: 'icon-batch-edit',
                    style: 'margin: 0 0 0 15px',
                    itemId: 'editRefund',
                    text: '修改退款申请'
                },
                {
                    xtype: 'button',
                    text: '导出',
                    style: 'margin: 0 0 0 15px',
                    iconCls: 'icon-deliver',
                    itemId: 'outRefundList'
                },
                {
                    xtype: 'button',
                    text: '刷新',
                    style: 'margin: 0 0 0 15px',
                    iconCls: 'icon-refresh',
                    itemId: 'refundRefresh'
                },
                {
                    xtype: 'button',
                    text: '删除',
                    style: 'margin: 0 0 0 15px',
                    iconCls: 'icon-remove',
                    itemId: 'remove'
                },
                {
                    xtype: 'button',
                    text: '作废',
                    style: 'margin: 0 0 0 15px',
                    iconCls: 'icon-cancel',
                    itemId: 'cancelRefund'
                }
            ]
        };

//        this.bbar =  Ext.create('Ext.PagingToolbar', {
//            plugins: Ext.create('Ext.ux.PageSizePlugin', {
//                limitWarning: "最大输入",
//                maximumSize: 1001
//            }),
//            store: 'RefundList',
//            displayInfo: true,
//            displayMsg: '当前页 {0} - {1} of {2}',
//            emptyMsg: "没有页了"
//        });


        this.columns = [
            {
                header: '退款类型',
                width: 80,
                dataIndex: 'type',
                renderer: function (value) {
                    return value['value'];
                }
            },
            {
                header: '是否线上退款',
                dataIndex: 'online',
                renderer: function (value) {
                    if (value) {
                        return '是';
                    } else {
                        return '否';
                    }
                }

            },
            {
                header: '退款阶段',
                dataIndex: 'phase',
                renderer: function (value) {
                    return value['value'];
                }
            },
            {
                header: '平台类型',
                dataIndex: 'platformType',
                renderer: function (value) {
                    return value['value'];
                }
            },
            {
                header: '店铺名称',
                dataIndex: 'shopName',
                width: 150
            },
            {
                header: '退款状态',
                dataIndex: 'status',
                renderer: function (value) {
                    return value['value'];
                }
            },
            {
                header: '外部平台订单编号',
                dataIndex: 'platformOrderNo',
                width: 130
            },
            {
                header: '外部平台退款单号',
                dataIndex: 'platformRefundNo',
                width: 150
            },
            {
                header: '商品编号',
                dataIndex: 'productNo',
            },
            {
                header: '商品名称',
                dataIndex: 'productName',
                width: 220
            },
            {
                header: 'sku',
                dataIndex: 'sku',
                width: 150
            },
            {
                header: '外部平台商品条形码（京东）',
                dataIndex: 'outerSku',
                width: 240
            },
            {
                header: '品牌',
                dataIndex: 'brandName',
            },
            {
                header: '支付宝账号',
                dataIndex: 'buyerAlipayNo',
                width: 150
            },
            {
                header: '退款人ID',
                dataIndex: 'buyerId',
            },
            {
                header: '退款人昵称',
                dataIndex: 'buyerName',
                width: 150
            },
            {
                header: '收货电话',
                dataIndex: 'receiverPhone',
                width: 150
            },
            {
                header: '手机号码',
                dataIndex: 'receiverMobile',
                width: 150
            },
            {
                header: '快递单号',
                dataIndex: 'shippingNo',
                width: 150
            },
            {
                header: '快递公司',
                dataIndex: 'shippingComp',
                renderer: Espide.Common.getExpress
            },
            {
                header: '账面退款金额',
                dataIndex: 'refundFee',
            },
            {
                header: '实际退款金额',
                dataIndex: 'actualRefundFee',
            },
            {
                header: '退款时间',
                dataIndex: 'refundTime',
                width: 180,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
            },

            {
                header: '退款原因',
                width: 250,
                dataIndex: 'reason',
            },
            {
                header: '退款描述',
                width: 250,
                dataIndex: 'description',
            },
            {
                header: '备注',
                dataIndex: 'remark',
            },
            {
                header: '是否同时退货',
                dataIndex: 'alsoReturn',
                renderer: function (value) {
                    if (value) {
                        return '是'
                    } else {
                        return '否'
                    }
                }
            },
            {
                header: '退货邮费',
                dataIndex: 'postFee',
            },
            {
                header: '退货邮费承担方',
                dataIndex: 'postPayer',
                width: 130,
                renderer: function (value) {
                    if (value != null) {
                        return value['value'];
                    } else {
                        return '';
                    }

                }
            },
            {
                header: '创建时间',
                dataIndex: 'createTime',
                width: 180,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
            },
            {
                header: '回访时间',
                dataIndex: 'revisitTime',
                width: 180,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
            }
        ];

        this.bbar = new Ext.PagingToolbar({
            displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
            store: 'RefundList',
            displayInfo: true,
            emptyMsg: '没有记录'
        });


        this.callParent(arguments);
    }
})