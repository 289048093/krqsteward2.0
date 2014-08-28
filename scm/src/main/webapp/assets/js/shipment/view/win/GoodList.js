/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.view.win.GoodList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    alias: 'widget.goodList',
    id: 'goodList',
    height: 200,
    forceFit: true,
    store: 'GoodList',
    selType: 'checkboxmodel',
    initComponent: function () {
        this.tbar = [
            {xtype: 'numberfield', value: 1, minValue: 1, width: 50, name: 'addGiftNum', itemId: 'addNum'},
            {xtype: 'button', text: '增加', iconCls: 'icon-add', disabled: false, itemId: 'addBtn'},
            {xtype: 'button', text: '清除', iconCls: 'icon-remove', disabled: false, itemId: 'removeBtn'},
            '->',
            Ext.create('Ext.form.Panel', {
                layout: 'hbox',
                border: false,
                items: [
                    {
                        xtype: 'textfield',
                        emptyText: '请输入关键字',
                        name: 'keywords',
                        width: 120,
                        margin: '0 5 0 0'
                    },
                    {
                        xtype: 'combo',
                        hideLabel: true,
                        name: 'type',
                        width: 80,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        value: '0',
                        store: [
                            ['0', '全部'],
                            ['1', '其它']
                        ],
                        margin: '0 5 0 0'
                    },
                    {
                        xtype: 'button',
                        text: '搜索',
                        margin: '0 5 0 0',
                        itemId: 'searchBtn'
                    },
                    {
                        xtype: 'button',
                        text: '重置'
                    }
                ]
            })
        ];
        this.columns = [
            {text: '商品编号', dataIndex: 'prodId'},
            {text: '品牌', dataIndex: 'brandName'},
            {text: '品名', dataIndex: 'prodName'},
            {text: '厂家货号', dataIndex: 'skuCode'},
            //{text: '规格', dataIndex: 'sellPropsStr'},
            {
                xtype: 'actioncolumn',
                text: '操作',
                itemId: 'addRow',
                menuDisabled: true,
                width: 50,
                iconCls: 'icon-add'
            }
        ];
        this.callParent(arguments);
    }
});
