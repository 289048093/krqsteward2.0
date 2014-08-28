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
        selType : 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'MULTI',
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
                    text: '订单作废',
                    iconCls: 'icon-cancel',
                    itemId: 'cancelOrder'
                },
                {
                    xtype: 'button',
                    text: '订单恢复',
                    iconCls: 'icon-recover',
                    itemId: 'recoverOrder'
                },
                {
                    xtype: 'button',
                    text: '解析订单',
                    iconCls: 'icon-import',
                    itemId: 'processedOrder'
                },
                {
                    xtype: 'button',
                    text: '刷新',
                    iconCls: 'icon-refresh',
                    itemId: 'orderRefresh'
                },
                '->',
                { xtype: 'displayfield', itemId: 'orderConut', value: '0', fieldLabel: '订单总条数', labelWidth: 70, hiden: true}
            ]
        };

        this.columns = [
            {xtype: 'rownumberer',text:'行数',width:'auto',align:'center'},

            {text: '解析状态', dataIndex: 'processed', sortable: true, width: 100,
                editor: {
                    xtype: 'textfield',
                    allowBlank: false
                }
            },
            {text: '是否有效', dataIndex: 'discard', sortable: true, width: 70,},
            {text: '平台类型', dataIndex: 'platformType', sortable: true, menuDisabled: true, width: 70,
            renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '店铺名称', dataIndex: 'shopName', sortable: true, menuDisabled: true, width: 70, },
            {text: '品牌', dataIndex: 'brandName', sortable: true, menuDisabled: true, width: 70, },
            {text: '外部平台订单编号', dataIndex: 'outOrderNo', sortable: true, menuDisabled: true, width: 130,  },
            {text: '外部平台订单金额', dataIndex: 'outActualFee', sortable: true, menuDisabled: true, width: 130,xtype: 'numbercolumn',  format: '0.00',},
            {text: '邮费', dataIndex: 'postFee', sortable: true, width: 60, xtype: 'numbercolumn', format: '0.00',},
            {text: '买家ID', dataIndex: 'buyerId', sortable: true, width: 150},
            {text: '收货省', width: 100, dataIndex: 'province', sortable: true, editor: {
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
            {text: '收货市', width: 100, dataIndex: 'city', sortable: true, editor: {
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
            {text: '收货地址', dataIndex: 'address', sortable: true, width: 250, editor: {xtype: 'textfield', allowBlank: false},
                renderer:function(value,meta,record){
                    if(value == null){
                        return '';
                    }
                    return "<span title='"+value+"'>"+value+"</span>";
                }
            },
            {text: '收货人', width: 90, dataIndex: 'receiverName', sortable: true, editor: {xtype: 'textfield', allowBlank: false}},
            {text: '邮政编码', dataIndex: 'receiverZip', sortable: true, width: 100, editor: {xtype: 'textfield', allowBlank: false}},
            {text: '收货电话', dataIndex: 'receiverPhone', sortable: true, width: 110, editor: {xtype: 'textfield', vtype: 'Phone', allowBlank: true}},
            {text: '收货手机', dataIndex: 'receiverMobile', sortable: true, width: 110, editor: {xtype: 'textfield', vtype: 'Mobile', allowBlank: false}},
            {text: '下单时间', dataIndex: 'buyTime', width: 160, sortable: true,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
            },
            {text: '付款时间', dataIndex: 'payTime', sortable: true, width: 160,
                renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
            },

        ];

        this.callParent(arguments);

    }
})