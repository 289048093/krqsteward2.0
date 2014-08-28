/**
 * Created by king on 13-12-23
 */

Ext.define('ShopProduct.view.addProduct.addProductList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    alias: 'widget.addProductList',
    id: 'addProductList',
    height: 150,
    forceFit: true,
    split:true,
    store: 'GoodList',
    selModel: {
        selType : 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'SIMPLE',
        showHeaderCheckbox: true
    },
    viewConfig: Espide.Common.getEmptyText(),

    initComponent: function () {
        /**
         *  data.list  生成store
         * @param url
         * @param insertAll
         * @param fields
         * @returns {Ext.data.Store}
         */
        function createComboStore(url, insertAll, fields) {
            var comboStore = new Ext.data.Store({
                    singleton: true,
                    proxy: {
                        type: 'ajax',
                        url: url,
                        actionMethods: 'get',
                        reader: {
                            type: 'json',
                            root: 'data.list'
                        }
                    },
                    fields: fields || [
                        'id',
                        'name',
                    ],
                    autoLoad: false,
                    pageSize: 300000
                }),
                options = {};

            if (insertAll) {
                options = {
                    callback: function () {
                        comboStore.insert(0, { name: '全部', id: null });
                    }
                };
            }
            comboStore.load(options);

            return comboStore;
        }

        var brandListStore = createComboStore('/brand/list_brand_id_name', true);
        this.tbar = [
            Ext.create('Ext.form.Panel', {
                layout: 'hbox',
                border: false,
                itemId: 'goodSearch',
                id: 'goodSearch',
                items: [
                    {
                        xtype:'combo',
                        name: 'brandId',
                        itemId: 'brandId',
                        fieldLabel: '品牌',
                        editable: false,
                        queryMode: 'local',
                        valueField: 'id',
                        displayField: 'name',
                        emptyText: '不限',
                        store: brandListStore
                    },
                    {
                        style:'margin:0 0 0 15px',
                        xtype: 'combo',
                        name: 'searchType',
                        itemId: 'searchType',
                        editable: false,
                        queryMode: 'local',
                        value: 'productNo',
                        store: [
                            ["sku", "sku"],
                            ["name", "产品名称"],
                            ["productNo", "商品编号"]
                        ]
                    },
                    {
                        xtype:'textfield',
                        name: 'searchValue',
                        id: 'code',
                        emptyText:'请输入查询关键字',
                        labelWidth: 85,
                        width:150,
                        style:'margin:0 0 0 15px',
                    },
                    {
                        style:'margin:0 0 0 15px',
                        xtype: 'button',
                        text: '查询',
                        width: 55,
                        itemId: 'searchBtn'
                    }
                ]
            }),
            '->',
            {xtype: 'numberfield', hidden:true,value: 1, minValue: 1, width: 110, name: 'addGiftNum', itemId: 'addNum', fieldLabel: '增加数量', labelWidth: 60},
            {xtype: 'button',hidden:true, text: '增加', iconCls: 'icon-add', disabled: false, itemId: 'addBtn'}
        ];
        this.columns = [
            {text: '商品名称', dataIndex: 'name'},
            {text: '品牌', dataIndex: 'brandName',},
            {text: 'sku', dataIndex: 'sku'},
            {text: '商家编号', dataIndex: 'productNo'},



            {text: '颜色', dataIndex: 'color',},
            {text: '规格', dataIndex: 'speci',},
            {text: '总库存', dataIndex: 'repositoryNum',},
//            {
//                xtype: 'actioncolumn',
//                text: '操作',
//                itemId: 'addRow',
//                menuDisabled: true,
//                width: 50,
//                iconCls: 'icon-add'
//            }
        ];

        this.callParent(arguments);
    }
});


