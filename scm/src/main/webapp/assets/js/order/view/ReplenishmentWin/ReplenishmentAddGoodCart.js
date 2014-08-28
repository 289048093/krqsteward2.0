/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.ReplenishmentWin.ReplenishmentAddGoodCart', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.ReplenishmentAddGoodCart',
    region: 'south',
    height: 160,
    id: 'ReplenishmentAddGoodCart',
    forceFit: false,
    split: true,
    store: 'GoodCart',
    selType: 'checkboxmodel',
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {
        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 1
            })
        ];
        this.columns = [
//            {text: '自增号', dataIndex: 'autoId', hidden: true},
            {text: '品牌Id', dataIndex: 'brandId',hidden:true},
            {text: '仓库名称', dataIndex: 'repositoryName',hidden:true},
            {text: '仓库Id', dataIndex: 'repositoryId',hidden:true},
            {text: '产品分类Id', dataIndex: 'prodCategoryId',hidden:true},
            {text: '订单类型', dataIndex: 'orderItemType',
                editor: new Ext.form.ComboBox({
                    id: 'OrderItemType',
                    name: 'OrderItemType',
                    store: Ext.create('Ext.data.Store',{
                        fields: ['value', 'orderItemType'],
                        data:[
                            {value: 'GIFT',orderItemType:'赠品'},
                            {value: 'REPLENISHMENT',orderItemType:'补货'},
                        ]
                    }),
                    queryMode: 'local',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false,
                    displayField:'orderItemType',
                    valueField: 'value'
                }),
                renderer: function (value, cellmeta, record) {
                    var orderItemStore = Ext.getCmp('OrderItemType').getStore();
                    //通过匹配value取得ds索引
                    var index = orderItemStore.find(Ext.getCmp('OrderItemType').valueField, value);
                    //通过索引取得记录ds中的记录集

                    var record = orderItemStore.getAt(index);
                    //返回记录集中的value字段的值
                    var returnvalue = "";
                    if (record) {
                        returnvalue = record.data.orderItemType;
                    }
                    return returnvalue;//注意这个地方的value是上面displayField中的value
                }

            },
            {text: '商品编号', dataIndex: 'productNo',width:120},
            {text: '商品名称', dataIndex: 'name',width:150},
            {text: 'sku', dataIndex: 'sku',width:120},
            {text: '品牌', dataIndex: 'brandName'},
            {text: '类别', dataIndex: 'prodCategoryName'},
            {text: '价格', dataIndex: 'marketPrice', xtype: 'numbercolumn', format: '0.00', width: 130},
            {text: '促销价', dataIndex: 'discountPrice', xtype: 'numbercolumn', format: '0.00'},
            {text: '订货数量', dataIndex: 'num',
                editor: {
                    xtype: 'numberfield',
                    allowDecimals: false,
                    minValue: 1
                },
                renderer: function (value) {
                    return (value + ' 件');
                }
            },
            {text: '库存', dataIndex: 'repositoryNum'},
            {text: '订单项优惠', dataIndex: 'discountFee', width: 130},
            {text: '整单优惠分摊', dataIndex: 'sharedDiscountFee', width: 130},
            {text: '线下换货邮费', dataIndex: 'exchangePostFee', width: 130},
            {text: '线下换货邮费承担方', dataIndex: 'exchangePostPayer', width: 165,
                editor: new Ext.form.ComboBox({
                    id: 'exchangePostPayerCombox',
                    name: 'exchangePostPayer',
                    store: Ext.create('Ext.data.Store',{
                        fields: ['value', 'exchangePostPayer'],
                        data:[
                            {value: 'BUYER',exchangePostPayer:'顾客'},
                            {value: 'SELLER',exchangePostPayer:'商家'},
                            {value: 'SUPPLIER',exchangePostPayer:'供应商'},
                        ]
                    }),
                    queryMode: 'local',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false,
                    displayField:'exchangePostPayer',
                    valueField: 'value'
                }),
                renderer: function (value, cellmeta, record) {
                    var exchangePostPayerStore = Ext.getCmp('exchangePostPayerCombox').getStore();

                    //通过匹配value取得ds索引
                    var index = exchangePostPayerStore.find(Ext.getCmp('exchangePostPayerCombox').valueField, value);
                    //通过索引取得记录ds中的记录集

                    var record = exchangePostPayerStore.getAt(index);
                    //返回记录集中的value字段的值
                    var returnvalue = "";
                    if (record) {
                        returnvalue = record.data.exchangePostPayer;
                    }
                    return returnvalue;//注意这个地方的value是上面displayField中的value
                }
            },

            {
                xtype: 'actioncolumn',
                text: '操作',
                menuDisabled: true,
                width: 50,
                items: [
                    {
                        iconCls: 'icon-remove'
                    }
                ],
                handler: function (view, rowIndex, colIndex, item, e, record) {
                    view.up('grid').getStore().remove(record);
                }
            }
        ];
        this.callParent(arguments);
    }
})