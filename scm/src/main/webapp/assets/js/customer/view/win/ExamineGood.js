/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 验货
 */

Ext.define('Customer.view.win.ExamineGood', {
    extend: 'Ext.window.Window',
    alias: 'widget.examineGood',
    id: 'examineGoodWin',
    itemId: 'examineGoodWin',
    title: '验货',
    iconCls: 'icon-cart',
    collapsible: true,
    maximizable: true,
    width: 1000,
    height: 450,
    fixed: true,
    layout: 'border',
    modal: true,
    initComponent: function () {

        var order = Ext.create('Ext.grid.Panel', {
            region: 'center',
            height: 150,
            itemId: 'order',
            closeAction: 'destroy',
            forceFit: true,
            viewConfig: {
                enableTextSelection: true
            },
            tbar: [
                { xtype: 'textfield', fieldLabel: '输入扫描物流单号', labelWidth: 110, name: 'order_num', id: 'order_num', itemId: 'order_num', allowBlank: false,
                    listeners: {
                        'specialkey': function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {//or e.getKey() == e.ENTER
                                Ext.getCmp('orderSearch').getEl().dom.click();
                            }
                        }
                    }
                },
                { xtype: 'button', itemId: 'orderSearch', id: 'orderSearch', text: '查询'},
                { xtype: 'displayfield', itemId: 'displayMsg', fieldLabel: '提示', value: '<span style="color:#E47113;">抱歉，查询不到该订单信息！</span>', hideLabel: true, hidden: true}
            ],
            store: Ext.create('Ext.data.Store', {
                fields: ["orderId", "orderNo", 'invoice', 'shippingNo', "orderStatus", 'shippingComp', 'receiverName', "confirmUser", "remark", "buyerMessage"],
                proxy: {
                    type: 'ajax',
                    url: '/assets/js/shipment/data/ExaminOrder.json',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.list',
                        messageProperty: 'message'
                    }
                },
                idProperty: 'orderId',
                autoLoad: false
            }),
            columns: [
                {text: 'id', dataIndex: 'orderId', hidden: true},
                {text: '订单编号', dataIndex: 'orderNo', width: 120},
                {text: '物流单号', dataIndex: 'shippingNo'},
                {text: '订单状态', dataIndex: 'orderStatus', renderer: function (value) {
                    return value['value']
                }},
                {text: '配送方式', dataIndex: 'shippingComp', renderer: Espide.Common.getExpress},
                {text: '收货人', dataIndex: 'receiverName'},
                {text: '审单员', dataIndex: 'confirmUser'},
                {text: '备注说明', dataIndex: 'remark'},
                {text: '买家留言', dataIndex: 'buyerMessage'}
            ]
        });

        var good = Ext.create('Ext.grid.Panel', {
            region: 'south',
            itemId: 'good',
            bodyCls: 'colorGrid',
            forceFit: true,
            disableSelection: true,
            split: true,
            hidden: true,
            height: 250,
            autoScroll: true,
            viewConfig: {
                enableTextSelection: true
            },
            tbar: [
                { xtype: 'textfield', itemId: 'goodNum', fieldLabel: '输入商品条形码', allowBlank: false, enableKeyEvents: true,
                    listeners: {
                        'specialkey': function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {//or e.getKey() == e.ENTER
                                Ext.getCmp('goodSearch').getEl().dom.click();
                            }
                        }
                    }
                },
                { xtype: 'button', itemId: 'goodSearch', id:'goodSearch', text: '查询'},
                { xtype: 'displayfield', itemId: 'trueMsg', hideLabel: true, value: '<span style="color:#669900">扫描成功</span>', hidden: true},
                { xtype: 'displayfield', itemId: 'falseMsg', hideLabel: true, value: '<span style="color:#E47113">未扫描到该商品!</span>', hidden: true},
                '->',
                { xtype: 'displayfield', itemId: 'orderNum', value: '', fieldLabel: '订单编号', labelWidth: 70, hiden: true}
            ],
            store: Ext.create('Ext.data.Store', {
                fields: [
                    {name: "prodName", type: 'string'},
                    {name: "skuCode", type: 'string'},
                    {name: 'prodCode', type: 'string'},
                    {name: 'orderNo', type: 'string'},
                    {name: 'prodPrice', type: 'string'},
                    {name: 'categoryName', type: 'string'},
                    {name: 'prodCount', type: 'string'},
                    {name: 'amoumt', type: 'int'},
                    {name: 'brandName', type: 'string'}
                ],
                proxy: {
                    type: 'ajax',
                    url: '/orders',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.list',
                        messageProperty: 'message'
                    }
                },
                idProperty: 'productSku',
                autoLoad: false
            }),
            columns: [
                {text: '序号', xtype: 'rownumberer', storable: false, width: 40},
                {text: '条形码', dataIndex: 'skuCode', width: 180 },
                {text: '商品名称', dataIndex: 'prodName', width: 300},
                {text: '订单编号', dataIndex: 'orderNo'},
                {text: '商品编号', dataIndex: 'prodCode'},
                {text: '类别', dataIndex: 'categoryName'},
                {text: '单价', dataIndex: 'prodPrice'},
                {text: '数量', dataIndex: 'prodCount', width: 60},
                {text: '库存', dataIndex: 'amoumt', width: 60},
                {text: '品牌', dataIndex: 'brandName'}
            ]
        });

        this.items = [order, good];

        this.callParent(arguments);
    }
});
