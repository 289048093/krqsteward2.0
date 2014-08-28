/**
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.define('Stock.view.List', {
    extend: 'Ext.container.Container',
    alias: 'widget.stockList',
    id: 'stockList',
    fixed: true,
    layout: 'border',
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



        var brandListStore = createComboStore('/brand/list_brand_id_name', true), // 品牌列表Store
            repositoryListStore = Espide.Common.createComboStore('/repository/list', true), // 仓库列表Store
            isForceFit = false, // 是否强制Grid的列自适应宽度
            searchForm, // 库存搜索表单
            stockGrid; // 库存Grid

        /*!
         * @author caizhiping
         * 下拉树
         */
        Ext.define('dataAddOrg.TreeComboBox', {
            extend: 'Ext.form.field.ComboBox',
            alias: 'widget.keeltreecombo',
            store: new Ext.data.ArrayStore({
                fields: [],
                data: [
                    []
                ]
            }),
            editable: false,
            allowBlank: false,
            _idValue: null,
            _txtValue: null,
            initComponent: function () {
                this.treeRenderId = Ext.id();
                this.tpl = "<tpl><div id='" + this.treeRenderId + "'></div></tpl>";
                this.callParent(arguments);
                this.on({
                    'expand': function () {
                        if (!this.treeObj.rendered && this.treeObj
                            && !this.readOnly) {
                            Ext.defer(function () {
                                this.treeObj.render(this.treeRenderId);
                            }, 280, this);
                        }
                    }
                });
                this.treeObj = new Ext.tree.Panel({
                    border: true,
                    id: 'technicalTreePanel',
                    height: 250,
                    width: 280,
                    split: true,
                    autoScroll: true,
                    root: {
                        id: null,
                        name: '全部分类',
                        expanded: true
                    },
                    store: Ext.create('Ext.data.TreeStore', {
                        fields: ['id', 'name'],
                        proxy: {
                            type: 'ajax',
                            url: 'productCategory/list',
                            extractResponseData: function (response) {
                                var json = Ext.JSON.decode(response.responseText).data.obj;
                                return json;

                            }
                        }
                    }),
                    columns: [
                        {
                            width: '100%',
                            xtype: 'treecolumn',
                            dataIndex: 'name'
                        }
                    ]
                });
                this.treeObj.on('itemclick', function (view, rec) {
                    if (rec) {
                        this.setValue(this._txtValue = rec.get('name'));
                        this._idValue = rec.get('id');
                        if (Ext.getCmp('parentId')) {
                            Ext.getCmp('parentId').setValue(this._idValue);
                        }
                        if (Ext.getCmp('departmentNameTrue')) {
                            Ext.getCmp('departmentNameTrue').setValue(rec.get('name'));
                        }
                        this.collapse();
                    }
                }, this);
            },
            getValue: function () {// 获取id值
                return this._idValue;
            },
            getTextValue: function () {// 获取text值
                return this._txtValue;
            },
            setLocalValue: function (txt, id) {// 设值
                this._idValue = id;
                this.setValue(this._txtValue = txt);
            }
        });


        searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            title: '库存管理',
            layout: {
                type: 'hbox',
                align: 'left'
            },
            height: 'auto',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'stockListSearch',
            defaultType: 'fieldcontainer',
            defaults: {
                margin: '0 10 0 0',
                defaults: {
                    xtype: 'combo',
                    labelWidth: 60,
                    width: 215,
                    queryMode: 'local',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false
                }
            },
            items: [
                {
                    items: [
                        {
                            name: 'product.brandId',
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
                            xtype: 'textfield',
                            name: 'product.name',
                            fieldLabel: '产品名称',
                            emptyText:'请输入产品名称',
                            maxLength:100,
                            maxLengthText:'文字长度多长'
                        },

                    ]
                },
                {
                    items: [
                        {
                            name: 'repositoryId',
                            fieldLabel: '选择仓库',
                            editable: false,
                            queryMode: 'local',
                            valueField: 'id',
                            displayField: 'name',
                            emptyText: '不限',
                            store: repositoryListStore
                        },

                        {
                            xtype: 'textfield',
                            name: 'product.productNo',
                            fieldLabel: '商品编号',
                            emptyText:'请输入商品编号',
                            maxLength:100,
                            maxLengthText:'文字长度多长'
                        },

                    ]
                },
                {
                    items: [
                        {
                            xtype: new dataAddOrg.TreeComboBox({
                                labelWidth: 60,
                                width: 280,
                                emptyText:'请选择',
                                labelAlign: 'right',
                                name: 'product.categoryId',
                                fieldLabel: '商品分类',
                                anchor: '80%'
                            })
                        },
                        {
                            xtype: 'textfield',
                            name: 'product.sku',
                            width: 280,
                            fieldLabel: '产品条码',
                            emptyText:'请输入产品条码',
                            maxLength:100,
                            maxLengthText:'文字长度多长'
                        }

                    ]
                },
                {
                    items: [
                        {
                            xtype: 'button',
                            text: '查询',
                            height: 55,
                            width: 55,
                            itemId: 'searchBtn'
                        }
                    ]
                }

            ]
        });


        stockGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: 'List',
            itemId:'stockListGrid',
            id: 'stockListGrid',
            viewConfig: {
                enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false,


            },
            plugins: new Ext.grid.plugin.CellEditing({
                pluginId: 'cellEdit',
                clicksToEdit: 2
            }),
            autoScroll:true ,
            forceFit:true,
            layout:'fit',
            tbar:[
                {
                    xtype: 'button',
                    text: '入库',
                    iconCls: 'icon-add',
                    itemId:'stockAdd'
                },
                '-',
                {
                    xtype: 'button',
                    text: '出库',
                    iconCls: 'icon-remove',
                    itemId:'stockOut'
                },
                '-',
                {
                    xtype: 'button',
                    text: '批量更新库存',
                    iconCls: 'icon-batch-edit',
                    itemId:'stockImport'
                }
            ],
            columns: [
                {
                    text: 'id',
                    dataIndex: 'id',
                    width: 80
                },
                {
                    text: '仓库名',
                    dataIndex: 'repoName',
                    width: 180
                },
                {
                    text: '品牌',
                    dataIndex: 'brandName',
                    width: 160
                },
                {
                    text: '商品分类',
                    dataIndex: 'prodCaName',
                    width: 160
                },
                {
                    text: '商品名',
                    dataIndex: 'prodName',
                    width: 260
                },
                {
                    text: '商品编号',
                    dataIndex: 'prodNo',
                    width: 180
                },
                {
                    text: '商品条形码',
                    dataIndex: 'prodCode',
                    width: 180
                },
                {
                    text: '最低价',
                    dataIndex: 'minimumPrice',
                    width: 120
                },
                {
                    text: '市场价',
                    dataIndex: 'marketPrice',
                    width: 120
                },
                {
                    text: '库存',
                    dataIndex: 'amount',
                    width: 120
                },
                {
                    text: '商品描述',
                    dataIndex: 'description',
                    width: 260
                }
            ],
            bbar: Ext.create('Ext.PagingToolbar', {
                store: 'List',
                displayInfo: true
            })
        });

        this.items = [searchForm, stockGrid];

        this.callParent(arguments);
    }
})
;