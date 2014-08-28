/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'OrderList',
    itemId: 'list',
    alias: 'widget.orderList',
    store: 'OrderList',
    foreFit: false,
    split: true,
    selModel: {
        selType: 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'SIMPLE',
        showHeaderCheckbox: true
    },
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {

        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 2
            })
        ];

        this.tbar = {
            items: [
                {
                    xtype: 'button',
                    text: '拆分',
                    iconCls: 'icon-split',
                    itemId: 'splitOrderBtn'
                },
                //创建下拉菜单
                Ext.create('Ext.button.Split', {
                    text: '变更订单状态',
                    border: '0',
                    iconCls: 'icon-add',
                    menu: {

                        id: 'editOrder',
                        items: [
                            {
                                text: '<b>订单作废</b>',
                                iconCls: 'icon-cancel',
                                itemId: 'cancelOrder'
                            },
                            {
                                text: '<b>订单恢复</b>',
                                iconCls: 'icon-recover',
                                itemId: 'recoverOrder'
                            },
                            {
                                text: '<b>批量审核</b>',
                                iconCls: 'icon-import',
                                itemId: 'batCheck'
                            },
                        ],

                    },
                }),
                {
                    xtype: 'button',
                    text: '刷单',
                    iconCls: 'icon-batch-edit',
                    itemId: 'cheatBtn'
                },
                {
                    xtype: 'button',
                    text: '批量改物流',
                    iconCls: 'icon-batch-edit',
                    itemId: 'batEditState'
                },
                {
                    xtype: 'button',
                    text: '导入进销存',
                    iconCls: 'icon-import',
                    itemId: 'importOrder'
                },
                {
                    xtype: 'button',
                    text: '联想物流单号',
                    hidden: true,
                    iconCls: 'icon-edit',
                    itemId: 'autoEditNum'
                },
                {
                    xtype: 'button',
                    text: '标记打印',
                    hidden: true,
                    iconCls: 'icon-import',
                    itemId: 'signPrinter'
                },
                {
                    xtype: 'button',
                    text: '加产品',
                    iconCls: 'icon-add',
                    itemId: 'addGood'
                },
                //创建下拉菜单
                Ext.create('Ext.button.Split', {
                    text: '加订单',
                    border: '0',
                    iconCls: 'icon-add',
                    menu: {

                        id: 'addOrderMenu',
                        items: [
                            {
                                text: '<b>正常订单</b>',
                                iconCls: 'icon-add',
                                itemId: 'addOrder'
                            },
                            {
                                text: '<b>售后换货订单</b>',
                                iconCls: 'icon-add',
                                itemId: 'addOrderChange'
                            },
                            {
                                text: '<b>补货订单</b>',
                                iconCls: 'icon-add',
                                itemId: 'replenishment'
                            },
                        ],

                    },
                }),

                {
                    xtype: 'button',
                    text: '售前换货',
                    iconCls: 'icon-join',
                    itemId: 'exchangeOnsale'
                },
                {
                    xtype: 'button',
                    text: '汇总',
                    iconCls: 'icon-add',
                    itemId: 'orderCollect',
                    hidden: true
                },
                {
                    xtype: 'button',
                    text: '刷新',
                    iconCls: 'icon-refresh',
                    itemId: 'orderRefresh'
                },
                //创建下拉菜单
                Ext.create('Ext.button.Split', {
                    text: '导出',
                    border: '0',
                    iconCls: 'icon-add',
                    menu: {

                        id: 'orderOut',
                        items: [
                            {
                                text: '<b>订单导出</b>',
                                iconCls: 'icon-import',
                                itemId: 'outOrder'
                            },
                            {
                                text: '<b>导出补货换货订单</b>',
                                iconCls: 'icon-deliver',
                                itemId: 'outOtherOrder'
                            },
                        ],

                    },
                }),
                {
                    xtype: 'button',
                    text: '导入订单',
                    iconCls: 'icon-import',
                    itemId: 'include'
                },
                //创建下拉菜单
                Ext.create('Ext.button.Split', {
                    text: '清除排序',
                    border: '0',
                    iconCls: 'icon-remove',
                    menu: {

                        id: 'removeSort',
                        items: [
                            {
                                text: '<b>清除订单排序</b>',
                                iconCls: 'icon-remove',
                                itemId: 'removeOrderSort'
                            },
                            {
                                text: '<b>清除订单项排序</b>',
                                iconCls: 'icon-remove',
                                itemId: 'removeItemSort'
                            },
                        ],

                    },
                }),
                '->',
                { xtype: 'displayfield', itemId: 'orderConut', value: '0', fieldLabel: '订单总条数', labelWidth: 70, hiden: true}
            ]
        };

        this.columns = [
            {xtype: 'rownumberer',text:'行数',width:'auto',align:'center'},
            {
                xtype: 'actioncolumn',
                width: 5,
                text: '操作',
                sortable: false,
                menuDisabled: true,

                items: [
                    {
                        iconCls: 'icon-remove',
                        tooltip: '删除当前条'
                    }
                ]
            },

            {text: '生成类型', dataIndex: 'generateType', sortable: true, width: 70,
                renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '订单类型', dataIndex: 'orderType', sortable: true, width: 70,
                renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '订单状态', dataIndex: 'orderStatus', sortable: true, width: 70,
                renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '退款状态', dataIndex: 'refunding', sortable: true, width: 70,
            },
            {text: '退货状态', dataIndex: 'orderReturnStatus', sortable: true, width: 70,
                renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '自增号', dataIndex: 'id', sortable: false, width: 100, hidden: true},
            {text: '智库城订单编号', dataIndex: 'orderNo', sortable: true, width: 150},
            {text: '平台类型', dataIndex: 'outPlatformType', sortable: true, width: 70,
                renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '店铺名称', dataIndex: 'shopName', sortable: true, menuDisabled: true, width: 70, },
            {text: '外部平台订单编号', dataIndex: 'platformOrderNo', sortable: true, width: 130, },
            {text: '外部平台订单金额', dataIndex: 'outActualFee', sortable: true, width: 130, xtype: 'numbercolumn', format: '0.00', },
            {text: '平台结算金额', dataIndex: 'actualFee', sortable: true, width: 120, xtype: 'numbercolumn', format: '0.00', },
            {text: '整单优惠金额', dataIndex: 'sharedDiscountFee', sortable: true, width: 100, xtype: 'numbercolumn', format: '0.00'},
            {text: '邮费', dataIndex: 'postFee', sortable: true, width: 60, xtype: 'numbercolumn', format: '0.00', },
            {text: '货款', dataIndex: 'goodsFee', sortable: true, width: 60, xtype: 'numbercolumn', format: '0.00', },
//            {text: '订单状态', width: 80, dataIndex: 'orderStatus', sortable: true, scope: Espide.Common.orderState, renderer: Espide.Common.orderState.getData },
            {text: '商品名称', dataIndex: 'itemName', sortable: true, width: 150,
                renderer: function (value, meta, record) {
                    return "<span title='" + value + "'>" + value + "</span>";

                },
            },
            //{text: '商品规格', dataIndex: 'specInfo', sortable: true, width: 150,},
            //{text: '成交金额', dataIndex: 'totalFee', sortable: true, width: 80, xtype: 'numbercolumn', format:'0.00'},
            {text: '总条数', width: 60, dataIndex: 'itemCount', sortable: true},
            {text: '总件数', dataIndex: 'itemNumCount', sortable: true, width: 60},
            {text: '买家留言', dataIndex: 'buyerMessage', sortable: true, width: 80,
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
                renderer: function (value, meta, record) {
                    if (value == null) {
                        return '';
                    }
                    return "<span title='" + value + "'>" + value + "</span>";
                }



            },
            {text: '备注说明', dataIndex: 'remark', sortable: true, width: 80,
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
                renderer: function (value, meta, record) {
                    if (value == null) {
                        return '';
                    }
                    return "<span title='" + value + "'>" + value + "</span>";

                },
            },
            {text: '线下备注', dataIndex: 'offlineRemark', sortable: true, width: 80,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
                renderer: function (value, meta, record) {
                    if (value == null) {
                        return '';
                    }
                    return "<span title='" + value + "'>" + value + "</span>";

                },
            },
            {text: '买家ID', dataIndex: 'buyerId', sortable: true, width: 150},
            {text: '收货省', width: 100, dataIndex: 'receiverState', sortable: true, editor: {
                xtype: 'combo',
                triggerAction: 'all',
                listeners: {
                    change: function (btn, newValue) {
                        btn.hasChange = true;
                    },
                    blur: function (btn) {
                        //Ext.getCmp('OrderList').getStore().autoSync = false;
                        //Ext.getCmp('OrderList').getSelectionModel().getSelection()[0].set('receiverCity', '请选择');
                        //Ext.getCmp('OrderList').getSelectionModel().getSelection()[0].set('receiverDistrict', '请选择');

                        btn.hasChange = false;
                    }
                },
                store: Espide.City.getProvinces()
            }},
            {text: '收货市', width: 100, dataIndex: 'receiverCity', sortable: true, editor: {
                xtype: 'combo',
                triggerAction: 'all',
                listeners: {
                    change: function () {
                        //Ext.getCmp('OrderList').getStore().autoSync = false;
                        //Ext.getCmp('OrderList').getSelectionModel().getSelection()[0].set('receiverDistrict', '请选择');

                    },
                    focus: function (combo) {
                        var province = Ext.getCmp('OrderList').getSelectionModel().getSelection()[0].get('receiverState'),
                            cities = Espide.City.getCities(province);
                        combo.getStore().loadData(cities);
                    }
                },
                store: Espide.City.getCities()
            }},
            {text: '收货区（县）', width: 100, dataIndex: 'receiverDistrict', sortable: true, editor: {
                xtype: 'combo',
                triggerAction: 'all',
                listeners: {
                    focus: function (combo) {
                        var record = Ext.getCmp('OrderList').getSelectionModel().getSelection()[0],
                            province = record.get('receiverState'),
                            city = record.get('receiverCity'),
                            areas = Espide.City.getAreas(province, city);
                        combo.getStore().loadData(areas);
                    }
                },
                store: Espide.City.getAreas()
            }},
            {text: '收货地址', dataIndex: 'receiverAddress', sortable: true, width: 250, editor: {xtype: 'textfield', allowBlank: false},

                renderer: function (value, meta, record) {
                    return "<span title='" + value + "'>" + value + "</span>";

                },
            },
            {text: '收货人', width: 90, dataIndex: 'receiverName', sortable: true, editor: {xtype: 'textfield', allowBlank: false}},
            {text: '邮政编码', dataIndex: 'receiverZip', sortable: true, width: 100, editor: {xtype: 'textfield', allowBlank: false}},
            {text: '收货电话', dataIndex: 'receiverPhone', sortable: true, width: 110, editor: {xtype: 'textfield', vtype: 'Phone', allowBlank: true}},
            {text: '收货手机', dataIndex: 'receiverMobile', sortable: true, width: 110, editor: {xtype: 'textfield', vtype: 'Mobile', allowBlank: false}},
            {text: '快递单号', dataIndex: 'shippingNo', sortable: true, width: 120},
            {text: '快递公司ID', dataIndex: 'repoId', sortable: true, hidden: true},
            {text: '快递公司', dataIndex: 'shippingComp', sortable: true, width: 70, renderer: Espide.Common.getExpress, editor: {
                xtype: 'combo',
                triggerAction: 'all',
                store: Espide.Common.expressStore()
            } },
            {text: '发票抬头', dataIndex: 'receiptTitle', sortable: true, width: 120, editor: {xtype: 'textfield', allowBlank: false}},
            {text: '发票内容', dataIndex: 'receiptContent', sortable: true, width: 150, editor: {xtype: 'textfield', allowBlank: false},
                renderer: function (value, meta, record) {
                    if (value == null) {
                        return '';
                    }
                    return "<span title='" + value + "'>" + value + "</span>";

                },
            },
            {text: '库房', dataIndex: 'repoName', sortable: true, width: 60},
            {text: '下单时间', dataIndex: 'buyTime', width: 160, sortable: true,
            },
            {text: '付款时间', dataIndex: 'payTime', sortable: true, width: 160,
            },

        ];

        this.callParent(arguments);

    }
})