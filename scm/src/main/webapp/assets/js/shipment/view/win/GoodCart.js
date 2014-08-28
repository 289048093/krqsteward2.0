/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.win.GoodCart', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.goodCart',
    region: 'south',
    height: 160,
    id: 'goodCart',
    forceFit: true,
    store: 'GoodCart',
    selType: 'checkboxmodel',
    initComponent: function () {
        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 2
            })
        ];
        this.columns = [
            {text: '商品编号', dataIndex: 'prodId'},
            {text: '赠品数量', dataIndex: 'prodCount', editor: {
                xtype: 'numberfield'
            }, renderer: function (value) {
                return (value + ' 件');
            }},
            {text: '品牌', dataIndex: 'brandName'},
            {text: '品名', dataIndex: 'prodName'},
            {text: '厂家货号', dataIndex: 'skuCode'},
            //{text: '规格', dataIndex: 'sellPropsStr'},
            {
                xtype: 'actioncolumn',
                text: '删除',
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