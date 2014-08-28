/**
 * author     : 梦龙
 * createTime : 14-5-9 上午10:10
 * description:
 */



Ext.define('Supplier.view.ExchangeOnsaleWin.ExchangeItem', {
    extend: 'Ext.grid.Panel',
    region: 'south',
    alias: 'widget.exchangeItem',
    id: 'ExchangeItem',
    height: 150,
    split: true,
    forceFit: false,
    height: 360,
    store: 'GoodList',
    //selModel: Ext.create('Ext.selection.CheckboxModel', {mode: 'SINGLE', showHeaderCheckbox: false}),
    multiSelect : false,//支持单选
    selModel: {
        selType : 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'SINGLE',
        showHeaderCheckbox: false
    },
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {

        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 1
            })
        ];
        this.tbar = [
            Ext.create('Ext.form.Panel', {
                layout: 'hbox',
                border: false,
                itemId: 'goodSearch',
                items: [
                    {
                        xtype: 'combo',
                        hideLabel: true,
                        name: 'searchType',
                        width: 160,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        value: 'productNo',
                        store: [
                            ['productNo', '商品编号'],
                            ['name', '商品名称']
                        ],
                        margin: '0 5 0 0'
                    },
                    {
                        name: 'searchValue',
                        id:'code',
                        xtype:'textfield',
                        labelWidth: 60,
                    },
                    {
                        xtype: 'button',
                        text: '查询',
                        margin: '0 5 0 0',
                        itemId: 'ExchangeSearchBtn'
                    }
                ]
            })
        ];
        this.columns = [
            {text: '订单类型', dataIndex: 'type', width:130},
            {text: '商品编号', dataIndex: 'productNo', width:150},
            {text: '商品名称', dataIndex: 'name',width:250},
            {text: '仓库名称', dataIndex: 'repositoryName',width:150},
            {text: 'sku', dataIndex: 'sku',width:150},
            {text: '品牌', dataIndex: 'brandName',},
            {text: '类别', dataIndex: 'prodCategoryName'},
            {text: '订货数量', dataIndex: 'buyCount',
                editor: {
                    xtype: 'numberfield',
                    allowDecimals: false,
                    minValue: 1
                },
                renderer: function (value) {
                    return (value);
                }
            },
            {text: '库存', dataIndex: 'repositoryNum',},

        ];
        this.callParent(arguments);
    }
});
