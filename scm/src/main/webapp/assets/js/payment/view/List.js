/**
 * author     : 梦龙
 * createTime : 14-5-9 上午9:06
 * description:
 */



Ext.define('Payment.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'List',
    itemId: 'List',
    alias: 'widget.List',
    store: 'PaymentList',
    foreFit: true,
    split: true,
    //selType: 'checkboxmodel',
    selModel:Ext.create('Ext.selection.CheckboxModel',{mode:'SINGLE',showHeaderCheckbox:false}),
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {


        this.tbar = {
            items: [
                {
                    xtype: 'button',
                    name: 'submuit',
                    style: 'margin: 0 0 0 15px',
                    itemId: 'addPayment',
                    iconCls: 'icon-add',
                    text: '分配预收款'
                },
                {
                    xtype: 'button',
                    text: '导出',
                    iconCls: 'icon-deliver',
                    itemId: 'outOrder'
                }
            ]
        };

        this.columns = [
            {
                header: '预收款类型',
                dataIndex: 'type',
                renderer:function(value){
                    return value['value'];
                }
            },
            {
                header: '分配状态',
                dataIndex: 'allocateStatus',
                renderer:function(value){
                    return value['value'];
                }
            },
            {
                header: '平台类型',
                dataIndex: 'platformType',
                renderer:function(value){
                    return value['value'];
                }
            },
            {
                header: '店铺名称',
                dataIndex: 'shopName',
                width: 180,
            },
            {
                header: '外部平台订单编号',
                width: 180,
                dataIndex: 'platformOrderNo'
            },
            {
                header: '外部平台订单项编号',
                width: 180,
                dataIndex: 'platformSubOrderNo'
            },
            {
                header: '预收款金额',
                dataIndex: 'paymentFee',
            },
            {
                header: '买家ID',
                dataIndex: 'buyerId',
                width: 150,
            },
            {
                header: '买家留言',
                width: 180,
                dataIndex: 'buyerMessage',

            },
            {
                header: '客服备注',
                width: 210,
                dataIndex: 'remark',
            },
            {
                header: '下单时间',
                dataIndex: 'buyTime',
                width: 180,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')

            },
            {
                header: '付款时间',
                dataIndex: 'payTime',
                width: 180,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')

            },
            {
                header: '创建时间',
                dataIndex: 'createTime',
                width: 180,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')

            },
            {
                header: '预收款退款金额',
                width: 180,
                dataIndex: 'refundFee'
            },

        ];

        this.bbar = new Ext.PagingToolbar({
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: 'PaymentList',
                displayInfo: true,
                emptyMsg: '没有记录'
            })

        this.callParent(arguments);
    }
})