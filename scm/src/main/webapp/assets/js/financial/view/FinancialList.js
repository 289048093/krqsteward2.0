/*
 * Created by Lein on 13-12-17
 */


Ext.define('Financial.view.FinancialList', {
    extend: 'Ext.container.Container',
    id: 'FinancialList',
    alias: 'widget.FinancialList',
    title: "财务模块",
    fixed: true,
    layout: "border",
    initComponent: function () {


        var platform = Espide.Common.createComboStore('/np/platform/list',true,['id','name','type']);

        var Seach = Ext.create('Ext.form.Panel', {
            region:'north',
            layout: 'hbox',
            title: "财务模块",
            bodyStyle:'padding:4px',
            border: false,
            height: 'auto',
            defaultType: 'fieldcontainer',
            itemId: 'search',
            id: 'search',
            items:[
                {
                    items:[

                        {
                            xtype:'combo',
                            labelWidth: 60,
                            width:150,
                            name: 'searchTimeType',
                            fieldLabel: '日期类型',
                            itemId: 'searchTimeType',
                            id: 'searchTimeType',
                            editable:false,
                            margin: '0 5 5 0',
                            value: 'payTime',
                            store: [
                                ['payTime', '付款日期'],
                                ['printTime', '打印日期'],
                                ['deliveryTime', '发货日期'],
                            ],
                        },
                        {
                            xtype:'combo',
                            labelWidth: 60,
                            width:150,
                            name: 'status',
                            fieldLabel: '订单状态',
                            editable:false,
                            itemId: 'status',
                            id: 'status',
                            margin: '0 5 5 0',
                            value: 'INVOICED',
                            store: [
                                ['INVOICED', '已发货或已签收'],
                                ['SIGNED', '已签收'],
                            ],
                        }
                    ]

                },{

                    items:[
                        Ext.create('Go.form.field.DateTime', {
                            fieldLabel: '开始日期',
                            value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                            name: 'startPayTime',
                            itemId: 'startPayTime',
                            format: 'Y-m-d H:i:s',
                            margin: '0 5 5 0',
                            labelWidth: 90,
                            width: 330
                        }),
                        {
                            xtype: 'fieldcontainer',
                            layout: 'hbox',
                            combineErrors: true,
                            msgTarget: 'side',
                            id:'shopAndGenerrate',
                            labelWidth: 60,
                            width: 330,
                            defaults: {
                                xtype: 'combo',
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                margin: '0 5 0 0'
                            },
                            items:[
                                {
                                    xtype: 'combo',
                                    name: 'platformType',
                                    fieldLabel: '平台类型',
                                    emptyText: '请选择',
                                    labelWidth: 60,
                                    margin: '0 5 5 0',
                                    valueField: 'type',
                                    displayField: 'name',
                                    editable:false,
                                    width: 150,
                                    store: platform,
                                },
                                {
                                    fieldLabel: '库房',
                                    emptyText: '请选择',
                                    labelWidth: 35,
                                    width:175,
                                    name: 'repoId',
                                    valueField: 'id',
                                    displayField: 'name',
                                    queryMode: 'remote',
                                    store: 'StorageAll'
                                },
                            ]

                        },

                    ]
                },
                {
                    items:[
                        Ext.create('Go.form.field.DateTime', {
                            fieldLabel: '结束日期',
                            value: new Date(),
                            name: 'endPayTime',
                            id: 'endPayTime',
                            format: 'Y-m-d H:i:s',
                            margin: '0 5 5 0',
                            labelWidth: 90,
                            width: 243
                        }),
                        {
                            xtype: 'combo',
                            fieldLabel: '店铺名称',
                            emptyText: '请选择',
                            valueField: 'id',
                            displayField: 'nick',
                            margin: '0 5 5 0',
                            labelWidth: 90,
                            editable:false,
                            width: 243,
                            queryMode: 'remote',
                            name: 'shopId',
                            store: 'ShopAll'
                        },
                    ]
                },
                {
                    xtype: 'button',
                    name: 'submuit',
                    style: 'margin: 0 0 0 15px',
                    itemId: 'search',
                    text: '查询',
                    width:55,
                }
            ]
        });



//操作日志
        var FinanceListGrid = Ext.create("Ext.grid.Panel", {
            region: 'center',
            itemId:'FinanceListGrid',
            id:'FinanceListGrid',

            loadMask: true,
            forceFit: false,
            split: true,
            store: 'FinancialList',
            viewConfig: {
                enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false
            },
            columns: [
                {
                    header: '平台类型',
                    width: 80,
                    dataIndex: 'platformType',
                    renderer:function(value){
                        return value['value'];
                    }
                },
                {
                    header: '数据类型',
                    dataIndex: 'financialType',
                },
                {
                    header: '店铺名称',
                    width:150,
                    dataIndex: 'shopName'

                },
                {
                    header: '外部平台订单编号',
                    width:200,
                    dataIndex: 'platformOrderNo'
                },
                {
                    header: '智库城订单编号',
                    width:200,
                    dataIndex: 'orderNo',
                },

                {
                    header: '订单状态',
                    dataIndex: 'status',
                },
                {
                    header: '库房',
                    dataIndex: 'repoName',
                },
                {
                    header: '整单优惠金额',
                    dataIndex: 'orderSharedDiscountFee',
                },
                {
                    header: '邮费',
                    dataIndex: 'postFee',
                },
                {
                    header: '发生日期',
                    dataIndex: 'happenTime',
                    width:200,
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },
                {
                    header: '下单时间',
                    dataIndex: 'buyTime',
                    width:200,
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },
                {
                    header: '付款时间',
                    dataIndex: 'payTime',
                    width:200,
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },
                {
                    header: '打印时间',
                    dataIndex: 'printTime',
                    width:200,
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },
                {
                    header: '买家ID',
                    dataIndex: 'buyerId',
                },
                {
                    header: '收货人',
                    dataIndex: 'receiverName',
                },
                {
                    header: '快递公司',
                    dataIndex: 'shippingComp',
                },
                {
                    header: '快递单号',
                    dataIndex: 'shippingNo',
                },
                {
                    header: '外部平台订单项编号',
                    width:200,
                    dataIndex: 'platformSubOrderNo'
                },
                {
                    header: '智库城订单项编号',
                    width:200,
                    dataIndex: 'orderId',
                },
                {
                    header: '订单项类型',
                    dataIndex: 'type',
                },
                {
                    header: '订单项状态',
                    dataIndex: 'itemStatus',
                },
                {
                    header: '线上退货状态',
                    dataIndex: 'returnStatus',
                    renderer:function(value){
                        return value['value'];
                    }
                },
                {
                    header: '线下退货状态',
                    dataIndex: 'offlineReturnStatus',
                    renderer:function(value){
                        return value['value'];
                    }
                },
                {
                    header: '换货状态',
                    dataIndex: 'exchanged',
                    renderer:function(value){
                        if(value){
                            return "是";
                        }else{
                            return '否';
                        }
                    }
                },
                {
                    header: '商品编号',
                    dataIndex: 'productCode',
                },
                {
                    header: '商品名称',
                    dataIndex: 'productName',
                },
                {
                    header: '商品规格',
                    dataIndex: 'specInfo',
                },
                {
                    header: 'sku',
                    dataIndex: 'sku',
                },
                {
                    header: '外部平台商品条形码（京东）',
                    width:240,
                    dataIndex: 'outerSku',
                },
                {
                    header: '品牌',
                    dataIndex: 'brandName',
                },
                {
                    header: '类别',
                    dataIndex: 'cateName',
                },
                {
                    header: '原价（一口价',
                    dataIndex: 'price',
                },
                {
                    header: '促销价',
                    dataIndex: 'discountPrice',
                },
                {
                    header: '订货数量',
                    dataIndex: 'buyCount',
                },
                {
                    header: '库存',
                    dataIndex: 'repoNum',
                },

                {
                    header: '订单项优惠金额',
                    width:150,
                    dataIndex: 'discountFee',
                },
                {
                    header: '分摊优惠金额',
                    dataIndex: 'sharedDiscountFee',
                },
                {
                    header: '分摊邮费',
                    dataIndex: 'sharedPostFee',
                },
                {
                    header: '平台结算金额',
                    dataIndex: 'actualFee',
                },
                {
                    header: '货款',
                    dataIndex: 'goodsFee',
                },
                {
                    header: '邮费补差金额',
                    width:150,
                    dataIndex: 'postCoverFee',
                },
                {
                    header: '邮费补差退款金额',
                    width:150,
                    dataIndex: 'postCoverRefundFee',
                },
                {
                    header: '服务补差金额',
                    width:150,
                    dataIndex: 'serviceCoverFee',
                },
                {
                    header: '服务补差退款金额',
                    width:150,
                    dataIndex: 'serviceCoverRefundFee',
                },
                {
                    header: '线上退款金额',
                    width:150,
                    dataIndex: 'refundFee',
                },
                {
                    header: '线下退款金额',
                    dataIndex: 'offlineRefundFee',
                },
                {
                    header: '线上退货邮费',
                    dataIndex: 'returnPostFee',
                },
                {
                    header: '线上退货邮费承担方',
                    width:150,
                    dataIndex: 'returnPostPayer',
                    renderer:function(value){
                        if(value == null){
                            return "";
                        }else{
                            return value['value'];
                        }
                    }
                },
                {
                    header: '线下退货邮费',
                    dataIndex: 'offlineReturnPostFee',
                },
                {
                    header: '线下退货邮费承担方',
                    width:150,
                    dataIndex: 'offlineReturnPostPayer',
                    renderer:function(value){
                        if(value == null){
                            return "";
                        }else{
                            return value['value'];
                        }
                    }
                },
                {
                    header: '线下换货邮费',
                    dataIndex: 'exchangePostFee',
                },
                {
                    header: '线下换货邮费承担方',
                    width:150,
                    dataIndex: 'exchangePostPayer',
                    renderer:function(value){
                        if(value == null){
                            return "";
                        }else{
                            return value['value'];
                        }
                    }
                },
                {
                    header: '价格描述',
                    dataIndex: 'priceDescription',
                },

            ],
            plugins: [
                Ext.create('Ext.grid.plugin.CellEditing', {
                    clicksToEdit: 1 //设置单击单元格编辑
                })
            ],
            tbar: [

                {
                    xtype: 'button',
                    text: '财务导出',
                    iconCls: 'icon-deliver',
                    itemId: 'outOrder'
                },
                {
                    xtype: 'button',
                    text: '仓库导出',
                    hidden:true,
                    iconCls: 'icon-deliver',
                    itemId: 'TotalOutOrder'
                }
            ],
            bbar: new Ext.PagingToolbar({
                pageSize: 25,
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: 'FinancialList',
                displayInfo: true,
                emptyMsg: '没有记录'
            })
        });

        this.items = [Seach,FinanceListGrid];
        this.callParent(arguments);

    }
});