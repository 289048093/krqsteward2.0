/**
 * Created by Lein xu
 */
Ext.define('Product.controller.Product', {
    extend: 'Ext.app.Controller',
    views: ['ProductList'],
    stores: [ 'BrandCategoryList', 'BrandList', 'RepoList'],
    models: ['BrandCategoryList', 'BrandList', 'RepoList'],
    init: function () {
        this.control({
            '#ProductList':{
                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.doSearch('productListGrid', 'search', true);
                                }
                            }
                        ]
                    });
                }
            },
            //查询按钮
            '#searchBtn': {
                'click': function () {
                    Espide.Common.doSearch('productListGrid', 'search', true);
                }
            },
            //添加
//            "#add": {
//                'click': function (button) {
//
//
//                    function createComboStore(url, insertAll, fields) {
//                        var comboStore = new Ext.data.Store({
//                                singleton: true,
//                                proxy: {
//                                    type: 'ajax',
//                                    url: url,
//                                    actionMethods: 'get',
//                                    reader: {
//                                        type: 'json',
//                                        root: 'data.obj.result'
//                                    }
//                                },
//                                fields: fields || [
//                                    'id',
//                                    'name',
//                                    'type',
//                                    {name: 'importPrice', type: 'sting', defaultValue: 0},
//                                    {name: 'isPutaway', type: 'auto', defaultValue: false},
//                                    {name: 'synStatus', type: 'auto', defaultValue: false},
//                                    {name: 'marketPrice', type: 'sting', defaultValue: 0},
//                                    'prodLinkPrefix',
//                                    {name: 'storageNum', type: 'int', defaultValue: 0},
//                                    'PlatformUrl',
//                                    {name: 'storagePercentReal', type: 'int', defaultValue: 0}
//
//                                ],
//                                autoLoad: true
//                            }),
//                            options = {};
//
//                        if (insertAll) {
//                            options = {
//                                callback: function () {
//                                    comboStore.insert(0, { name: '全部', id: null });
//                                }
//                            };
//                        }
//                        comboStore.load(options);
//
//                        return comboStore;
//                    }
//
//
//                    var platformStore = createComboStore('platform/list', false);
//
//
//                    /*!
//                     * @author caizhiping
//                     * 下拉树
//                     */
//                    Ext.define('dataAddOrg.TreeComboBox', {
//                        extend: 'Ext.form.field.ComboBox',
//                        alias: 'widget.keeltreecombo',
//                        store: new Ext.data.ArrayStore({
//                            fields: [],
//                            data: [
//                                []
//                            ]
//                        }),
//                        editable: false,
//                        allowBlank: false,
//                        _idValue: null,
//                        _txtValue: null,
//                        initComponent: function () {
//                            this.treeRenderId = Ext.id();
//                            this.tpl = "<tpl><div id='" + this.treeRenderId + "'></div></tpl>";
//                            this.callParent(arguments);
//                            this.on({
//                                'expand': function () {
//                                    if (!this.treeObj.rendered && this.treeObj
//                                        && !this.readOnly) {
//                                        Ext.defer(function () {
//                                            this.treeObj.render(this.treeRenderId);
//                                        }, 280, this);
//                                    }
//                                }
//                            });
//                            this.treeObj = new Ext.tree.Panel({
//                                border: true,
//                                id: 'technicalTreePanel',
//                                height: 250,
//                                width: 280,
//                                split: true,
//                                autoScroll: true,
//                                root: {
//                                    id: '0',
//                                    name: '全部分类',
//                                    expanded: true
//                                },
//                                rootVisible:false,
//                                store: Ext.create('Ext.data.TreeStore', {
//                                    fields: ['id', 'name'],
//                                    proxy: {
//                                        type: 'ajax',
//                                        url: 'productCategory/list',
//                                        extractResponseData: function (response) {
//                                            var json = Ext.JSON.decode(response.responseText).data.obj;
//                                            return json;
//
//                                        }
//                                    }
//                                }),
//                                columns: [
//                                    {
//                                        width: '100%',
//                                        xtype: 'treecolumn',
//                                        dataIndex: 'name'
//                                    }
//                                ]
//                            });
//                            this.treeObj.on('itemclick', function (view, rec) {
//                                if (rec) {
//                                    this.setValue(this._txtValue = rec.get('name'));
//                                    this._idValue = rec.get('id');
//                                    if (Ext.getCmp('parentId')) {
//                                        Ext.getCmp('parentId').setValue(this._idValue);
//                                    }
//                                    if (Ext.getCmp('departmentNameTrue')) {
//                                        Ext.getCmp('departmentNameTrue').setValue(rec.get('name'));
//                                    }
//                                    this.collapse();
//                                }
//                            }, this);
//                        },
//                        getValue: function () {// 获取id值
//                            return this._idValue;
//                        },
//                        getTextValue: function () {// 获取text值
//                            return this._txtValue;
//                        },
//                        setLocalValue: function (txt, id) {// 设值
//                            this._idValue = id;
//                            this.setValue(this._txtValue = txt);
//                        }
//                    });
//
//
//                    var platformListGrid = Ext.create("Ext.grid.Panel", {
//                            region: 'center',
//                            id: 'platformListGrid',
//                            loadMask: true,
//                            forceFit: true,
//                            border: '0',
//                            store: platformStore,
//                            plugins: new Ext.grid.plugin.CellEditing({
//                                pluginId: 'cellEdit',
//                                clicksToEdit: 1
//                            }),
//                            columns: [
//                                {
//                                    header: 'id',
//                                    hidden:true,
//                                    dataIndex: 'id'
//                                },
//                                {
//                                    header: '平台名称',
//                                    dataIndex: 'name'
//                                },
//                                {
//                                    header: '一口价',
//                                    dataIndex: 'marketPrice',
//                                    editor: {
//                                        xtype: "numberfield",
//                                        minValue: 0
//                                    }
//
//                                },
//                                {
//                                    header: '促销价',
//                                    dataIndex: 'importPrice',
//                                    editor: {
//                                        xtype: "textfield"
//                                    }
//                                },
//                                {
//                                    header: '是否上架',
//                                    dataIndex: 'isPutaway',
//                                    xtype: 'checkcolumn'
//                                },
//                                {
//                                    header: '是否同步',
//                                    dataIndex: 'synStatus',
//                                    xtype: 'checkcolumn'
//                                },
//                                {
//                                    header: '库存',
//                                    dataIndex: 'storageNum',
//                                    editor: {
//                                        xtype: "numberfield",
//                                        minValue: 0,
//                                        allowDecimals:false
//
//                                    }
//                                },
//                                {
//                                    header: '库存占比',
//                                    dataIndex: 'storagePercentReal',
//                                    editor: {
//                                        xtype: "numberfield",
//                                        minValue: 0,
//                                        allowDecimals:false
//                                    }
//                                },
//                                {
//                                    header: '链接',
//                                    dataIndex: 'PlatformUrl',
//                                    editor: {
//                                        xtype: "textfield"
//                                    }
//                                }
//                            ],
//                            listeners:{
//                                edit:function(editor,e){
//                                    /*                                  grid - 当前gird
//                                     record - 当前要编辑的record
//                                     field - 要编辑的字段名称
//                                     value - 当前值
//                                     row - grid的行序号
//                                     column - gird上定义的要编辑的Column.
//                                     rowIdx - 正在编辑的行索引
//                                     colIdx - 正在编辑的列索引
//                                     originalValue - 字段编辑前的原始值(只有配置CellEditing的时候有效)
//                                     originalValues - 字段编辑前该行的原始值(只有配置RowEditing的时候有效)
//                                     newValues - 要设置的新值(只有配置RowEditing的时候有效)
//                                     view - 这个grid的视图 (只有配置RowEditing的时候有效)
//                                     store - 当前grid的store (只有配置RowEditing的时候有效
//                                     */
//
//                                    if(e.field == 'storagePercentReal'){
//                                        e.record.set('storageNum',Math.ceil(Ext.getCmp('repositoryNum').getValue()*e.record.get('storagePercentReal')/100));
//                                    }
//
//                                    if(e.field == 'storageNum'){
//                                        e.record.set('storagePercentReal',Math.floor(e.record.get('storageNum')/Ext.getCmp('repositoryNum').getValue()*100));
//                                    }
//
//                                }
//                            }
//                        }
//                    );
//
//                    var addProductForm = Ext.create('Ext.form.Panel', {
//                        region: 'north',
//                        id: 'addProductForm',
//                        height: 'auto',
//                        bodyPadding: 10,
//                        layout: {
//                            type: 'hbox',
//                            align: 'left'
//                        },
//                        border: 0,
//                        defaultType: 'fieldcontainer',
//                        defaults: {
//
//                            defaults: {
//                                xtype: 'textfield',
//                                margin: '0 0 5 0',
//                                labelWidth: 70,
//                                labelAlign: 'right',
//                                width: 280,
//                                queryMode: 'local',
//                                triggerAction: 'all',
//                                allowBlank: false,
//                                forceSelection: true,
//                                editable: false,
//                                minValue: 1,
//                            }
//                        },
//                        items: [
//                            {
//                                items: [
//                                    {
//                                        name: 'picUrl',
//                                        fieldLabel: '产品图片',
//                                        hidden: true,
//                                        value: "null"
//                                    },
//                                    {
//                                        name: 'name',
//                                        fieldLabel: '产品名称',
//                                        emptyText:'请输入产品名称',
//                                        maxLength: 32,
//                                        allowBlank:false,
//                                        vtype: 'RegularString'
//
//                                    },
//                                    {   name: 'brandId',
//                                        fieldLabel: '产品品牌',
//                                        xtype: 'combo',
//                                        queryMode: "remote",
//                                        triggerAction: 'all',
//                                        forceSelection: true,
//                                        editable: false,
//                                        displayField: "name",
//                                        valueField: 'id',
//                                        blankText: '请选择',
//                                        emptyText: '请选择',
//                                        allowBlank:false,
//                                        store: 'BrandList'
//                                    },
//                                    {
//                                        name: 'productNo',
//                                        fieldLabel: '商品编号',
//                                        emptyText: '请输入商品编号',
//                                        allowBlank:false,
//                                        maxLength: 32
//                                    },
//                                    {
//                                        name: 'sku',
//                                        fieldLabel: 'sku',
//                                        emptyText: '请输入产品sku',
//                                        allowBlank:false,
//                                        maxLength: 32
//                                    },
//                                    {
//                                        xtype: new dataAddOrg.TreeComboBox({
//                                            labelWidth: 70,
//                                            width: 280,
//                                            labelAlign: 'right',
//                                            name: 'categoryId',
//                                            allowBlank:false,
//                                            fieldLabel: '产品分类',
//                                            //name: 'parentId',
//                                            emptyText:'请选择',
//                                            anchor: '88%'
//                                        })
//                                    },
//                                    {
//                                        name: 'description',
//                                        fieldLabel: '产品描述',
//                                        xtype: 'textareafield',
//                                        allowBlank: true,
//                                        emptyText:'请输入产品描述(可为空)',
//                                        maxLength: 512,
//                                        height: 26
//                                    },
//                                    {
//                                        name: 'marketPrice',
//                                        id: "shopPriceStr",
//                                        xtype: 'numberfield',
//                                        allowBlank:false,
//                                        editable: true,
//                                        fieldLabel: '市场价(元)',
//                                        emptyText: '请输入市场价',
//                                        minValue: 0,
//                                        negativeText: '不得输入负数'
//                                    },
//                                    {
//                                        name: 'minimumPrice',
//                                        id: "minimumPrice",
//                                        xtype: 'numberfield',
//                                        allowBlank:false,
//                                        editable: true,
//                                        fieldLabel: '最低价(元)',
//                                        emptyText: '请输入最低价',
//                                        minValue: 0,
//                                        negativeText: '不得输入负数'
//                                    },
//                                    {
//                                        name: 'color',
//                                        fieldLabel: '颜色',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品颜色(可为空)',
//                                        maxLength: 5
////                                regex: /[\u4e00-\u9fa5]+/,
////                                regexText: '颜色必须是中文'
//                                    },
//
//                                ]
//                            },
//                            {
//                                items: [
//
//                                    {
//                                        name: 'weight',
//                                        fieldLabel: '重量(kg)',
//                                        emptyText: '请输入产品重量(可为空)',
//                                        allowBlank: true,
//                                        maxLength: 20
//                                    },
//                                    {
//                                        name: 'boxSize',
//                                        fieldLabel: '尺寸(厘米)',
//                                        emptyText: '请输入产品尺寸(可为空)',
//                                        allowBlank: true,
//                                        maxLength: 20
//                                    },
//                                    {
//                                        name: 'speci',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品规格(可为空)',
//                                        fieldLabel: '规格'
//                                    },
//                                    {
//                                        name: 'orgin',
//                                        emptyText: '请输入产品产地',
//                                        allowBlank:false,
//                                        fieldLabel: '产地'
//                                    },
//                                    {
//                                        xtype: 'combo',
//                                        fieldLabel: '产品类型',
//                                        allowBlank:false,
//                                        emptyText:'请选择',
//                                        name: 'style',
//                                        value: 'A',
//                                        store: [
//                                            ['A', 'A'],
//                                            ['B', 'B'],
//                                            ['C', 'C']
//                                        ]
//
//                                    },
//                                    {
//                                        xtype: 'combo',
//                                        name: 'location',
//                                        emptyText:'请选择',
//                                        allowBlank:false,
//                                        fieldLabel: '库位',
//                                        value: 'NORMAL',
//                                        store: [
//                                            ['NORMAL', '正常产品'],
//                                            ['GIFT', '赠品'],
//                                            ['DEFECTIVE', '缺件商品']
//                                        ]
//                                    },
//                                    {
//                                        name: 'repositoryNum',
//                                        fieldLabel: '总库存',
//                                        emptyText:'请输入库存',
//                                        allowBlank:false,
//                                        xtype: 'numberfield',
//                                        id: 'repositoryNum',
//                                        editable: true,
//                                        allowDecimals:false,
//                                        minValue: 0,
//                                        negativeText: '不得输入负数',
//                                        listeners:{
//                                            change:function(com,value){
//                                                var platStore = Ext.getCmp('platformListGrid').getStore();
//                                                platStore.each(function (record) {
//                                                   record.set('storageNum',Math.ceil(value*record.get('storagePercentReal')/100));
//                                                })
//                                            }
//                                        }
//                                    },
//                                    {
//                                        xtype: 'combo',
//                                        name: 'repositoryId',
//                                        id: 'repoId',
//                                        fieldLabel: '选择仓库',
//                                        allowBlank:false,
//                                        queryMode: "remote",
//                                        triggerAction: 'all',
//                                        forceSelection: true,
//                                        editable: false,
//                                        displayField: "name",
//                                        valueField: 'id',
//                                        emptyText: '请选择',
//                                        store: 'RepoList'
//                                    },
//                                    {
//                                        name: 'outerProductNo',
//                                        fieldLabel: '外部平台商品编号',
//                                        allowBlank:true,
//                                        emptyText: '请输入外部平台商品编号(可为空)',
//                                        labelWidth:110,
//                                        maxLength:32
//                                    },
//                                ]
//                            }
//                        ]
//
//                    });
//
//
//                    var addProductWin = Ext.create("Ext.window.Window", {
//                        title: '添加产品',
//                        width: 630,
//                        height: 600,
//                        modal: true,
//                        animateTarget: Ext.getBody(),
//                        autoHeight: true,
//                        layout: 'border',
//                        buttonAlign: 'right',
//                        bodyStyle: 'padding:5px;',
//                        items: [addProductForm, platformListGrid],
//                        buttons: [
//                            {
//                                text: "保存",
//                                handler: function () {
//                                    var form = addProductForm.getForm();
//                                    var data = form.getValues();
//                                    if (form.isValid()) {
//                                        //获取表单store platformListGrid
//
//                                        var plarformData = platformListGrid.getStore(),
//                                            platformIdArray = [],
//                                            marketPriceArray = [],
//                                            importPriceArray = [],
//                                            isPutawayArray = [],
//                                            storageNumArray = [],
//                                            prodLinkPrefixArray = [],
//                                            storagePercentRealArray = [],
//                                            synStatusArray = [];
//
//
//                                        platformStore.each(function (record) {
//                                            platformIdArray.push(record.get('id'));
//                                            marketPriceArray.push(record.get('marketPrice'));
//                                            importPriceArray.push(record.get('importPrice'));
//                                            isPutawayArray.push(record.get('isPutaway'));
//                                            storageNumArray.push(record.get('storageNum'));
//                                            prodLinkPrefixArray.push(record.get('PlatformUrl'));
//                                            storagePercentRealArray.push(record.get('storagePercentReal'));
//                                            synStatusArray.push(record.get('synStatus'));
//                                        })
//
//                                        form.submit({
//                                            submitEmptyText:false,
//                                            url: "/product/save",
//                                            params: {
//                                                boxSize: data['boxSize'],
//                                                picUrl: data['picUrl'],
//                                                name: data['name'],
//                                                brandId: data['brandId'],
//                                                productNo: data['productNo'],
//                                                sku: data['sku'],
//                                                categoryId: data['categoryId'],
//                                                description: data['description'],
//                                                marketPrice: data['marketPrice'],
//                                                importPrice: data['importPrice'],
//                                                minimumPrice: data['minimumPrice'],
//                                                color: data['color'],
//                                                weight: data['weight'],
//                                                speci: data['speci'],
//                                                orgin: data['orgin'],
//                                                style: data['style'],
//                                                location: data['location'],
//                                                repositoryNum: data['repositoryNum'],
//                                                repositoryId: data['repositoryId'],
//                                                outerProductNo: data['outerProductNo'],
//                                                platformIdArray: platformIdArray.join(','),
//                                                marketPriceArray: marketPriceArray.join(','),
//                                                importPriceArray: importPriceArray.join(','),
//                                                isPutawayArray: isPutawayArray.join(','),
//                                                synStatusArray: synStatusArray.join(','),
//                                                storageNumArray: storageNumArray.join(','),
//                                                platformUrlArray: prodLinkPrefixArray.join(','),
//                                                storagePercentRealArray: storagePercentRealArray.join(',')
//
//
//                                            },
//                                            success: function (response, options) {
//                                                var data = Ext.JSON.decode(options.response.responseText);
//                                                if (data.success) {
//                                                    addProductWin.close();
//                                                    Espide.Common.tipMsg('产品添加成功', data.msg);
//                                                    Ext.getCmp('productListGrid').getStore().load();
//                                                }
//                                            },
//                                            failure: function (response, options) {
//
//                                                var data = Ext.JSON.decode(options.response.responseText);
//
//                                                Ext.MessageBox.show({
//                                                    title: '提示',
//                                                    msg: data.msg,
//                                                    buttons: Ext.MessageBox.OK,
//                                                    icon: 'x-message-box-warning'
//                                                });
//
//                                            }
//                                        });
//                                    }
//                                }
//
//                            }
//                        ]
//                    });
//                    addProductWin.show();
//                }
//            },
//            //修改
//            "#productListGrid": {
//                'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件
//
//
//                    var recordData = record;
//
//                    var platformUpdateStore = new Ext.data.Store({
//                        proxy: {
//                            type: 'ajax',
//                            url: 'platform/list',
//                            reader: {
//                                type: 'json',
//                                root: 'data.obj.result',
//                                idProperty: 'id'
//                            }
//                        },
//                        fields: [
//                            'id',
//                            'name',
//                            'type',
//                            'importPrice',
//                            'isPutaway',
//                            'marketPrice',
//                            'prodLinkPrefix',
//                            'storagePercentReal',
//                            'storageNum'
//                        ]
//
//                    });
//
//
//                    /*!
//                     * @author caizhiping
//                     * 下拉树
//                     */
//                    Ext.define('dataAddOrg.TreeComboBox', {
//                        extend: 'Ext.form.field.ComboBox',
//                        alias: 'widget.keeltreecombo',
//                        store: new Ext.data.ArrayStore({
//                            fields: [],
//                            data: [
//                                []
//                            ]
//                        }),
//                        editable: false,
//                        allowBlank: false,
//                        _idValue: null,
//                        _txtValue: null,
//                        initComponent: function () {
//                            this.treeRenderId = Ext.id();
//                            this.tpl = "<tpl><div id='" + this.treeRenderId + "'></div></tpl>";
//                            this.callParent(arguments);
//                            this.on({
//                                'expand': function () {
//                                    if (!this.treeObj.rendered && this.treeObj
//                                        && !this.readOnly) {
//                                        Ext.defer(function () {
//                                            this.treeObj.render(this.treeRenderId);
//                                        }, 300, this);
//                                    }
//                                }
//                            });
//                            this.treeObj = new Ext.tree.Panel({
//                                border: true,
//                                id: 'technicalTreePanel',
//                                height: 250,
//                                width: 300,
//                                split: true,
//                                autoScroll: true,
//                                root: {
//                                    id: '0',
//                                    name: '全部分类',
//                                    expanded: true
//                                },
//                                rootVisible:false,
//                                store: Ext.create('Ext.data.TreeStore', {
//                                    fields: ['id', 'name'],
//                                    proxy: {
//                                        type: 'ajax',
//                                        url: 'productCategory/list',
//                                        extractResponseData: function (response) {
//                                            var json = Ext.JSON.decode(response.responseText).data.obj;
//                                            return json;
//
//                                        }
//                                    }
//                                }),
//                                columns: [
//                                    {
//                                        width: '100%',
//                                        xtype: 'treecolumn',
//                                        dataIndex: 'name'
//                                    }
//                                ]
//                            });
//                            this.treeObj.on('itemclick', function (view, rec) {
//                                if (rec) {
//                                    this.setValue(this._txtValue = rec.get('name'));
//                                    this._idValue = rec.get('id');
//
//                                    this.collapse();
//                                }
//                            }, this);
//                        },
//                        getValue: function () {// 获取id值
//                            return this._idValue;
//                        },
//                        getTextValue: function () {// 获取text值
//                            return this._txtValue;
//                        },
//                        setLocalValue: function (txt, id) {// 设值
//                            this._idValue = id;
//                            this.setValue(this._txtValue = txt);
//                        }
//                    });
//
//
//                    var updateGoodsForm = Ext.create('Ext.form.Panel', {
//                        region: 'north',
//                        id: 'updateGoodsForm',
//                        height: 'auto',
//                        bodyPadding: 10,
//                        layout: {
//                            type: 'hbox',
//                            align: 'left'
//                        },
//                        border: 0,
//                        msgTarget: 'title',
//
//                        defaultType: 'fieldcontainer',
//                        defaults: {
//                            defaults: {
//                                xtype: 'textfield',
//                                margin: '0 0 5 0',
//                                labelWidth: 70,
//                                labelAlign: 'right',
//                                width: 280,
//                                queryMode: 'local'
//                            }
//
//                        },
//                        items: [
//                            {
//                                items: [
//                                    {
//                                        name: 'id',
//                                        hidden: true,
//                                        value: record.get('id')
//                                    },
//                                    {
//                                        name: 'picUrl',
//                                        fieldLabel: '产品图片',
//                                        hidden: true,
//                                        value: "null"
//                                    },
//                                    {
//                                        name: 'name',
//                                        fieldLabel: '产品名称',
//                                        allowBlank:false,
//                                        maxLength: 32,
//                                        value: record.get('name')
//                                    },
//                                    {   name: 'brandId',
//                                        id: 'brandId',
//                                        fieldLabel: '产品品牌',
//                                        xtype: 'combo',
//                                        allowBlank:false,
//                                        queryMode: "remote",
//                                        triggerAction: 'all',
//                                        forceSelection: true,
//                                        editable: false,
//                                        displayField: "name",
//                                        valueField: 'id',
//                                        blankText: '请选择',
//                                        store: 'BrandList'
//                                    },
//                                    {
//                                        name: 'productNo',
//                                        fieldLabel: '商品编号',
//                                        allowBlank:false,
//                                        maxLength: 32,
//                                        value: record.get('productNo')
//                                    },
//                                    {
//                                        name: 'sku',
//                                        fieldLabel: '产品sku',
//                                        maxLength: 32,
//                                        disabled:true,
//                                        value: record.get('sku')
//                                    },
//                                    {
//                                        xtype: new dataAddOrg.TreeComboBox({
//                                            labelWidth: 70,
//                                            width: 280,
//                                            name: 'categoryId',
//                                            id:'categoryId',
//                                            labelAlign: 'right',
//                                            fieldLabel: '产品分类',
//                                            anchor: '90%'
//                                        })
//                                    },
//                                    {
//                                        name: 'description',
//                                        fieldLabel: '产品描述',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品描述(可为空)',
//                                        xtype: 'textareafield',
//                                        maxLength: 512,
//                                        height: 26,
//                                        value: record.get('description')
//                                    },
//                                    {
//                                        name: 'marketPrice',
//                                        id: "shopPriceStr",
//                                        xtype: 'numberfield',
//                                        editable: true,
//                                        fieldLabel: '市场价(元)',
//                                        minValue: 0,
//                                        negativeText: '不得输入负数',
//                                        allowBlank:false,
//                                        value: record.get('marketPrice')
//                                    },
//                                    {
//                                        name: 'minimumPrice',
//                                        id: "minimumPrice",
//                                        xtype: 'numberfield',
//                                        editable: true,
//                                        fieldLabel: '最低价(元)',
//                                        minValue: 0,
//                                        negativeText: '不得输入负数',
//                                        allowBlank:false,
//                                        value: record.get('minimumPrice')
//                                    },
//                                    {
//                                        name: 'color',
//                                        fieldLabel: '颜色',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品颜色(可为空)',
//                                        maxLength: 5,
//                                        value: record.get('color')
////                                regex: /[\u4e00-\u9fa5]+/,
////                                regexText: '颜色必须是中文'
//                                    },
//
//                                ]
//                            },
//                            {
//                                items: [
//
//                                    {
//                                        name: 'weight',
//                                        fieldLabel: '重量(kg)',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品重量(可为空)',
//                                        maxLength: 20,
//                                        value: record.get('weight')
//                                    },
//                                    {
//                                        name: 'boxSize',
//                                        fieldLabel: '尺寸(厘米)',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品尺寸(可为空)',
//                                        maxLength: 20,
//                                        value: record.get('boxSize')
//                                    },
//                                    {
//                                        name: 'speci',
//                                        fieldLabel: '规格',
//                                        allowBlank: true,
//                                        emptyText: '请输入产品规格(可为空)',
//                                        value: record.get('speci')
//                                    },
//                                    {
//                                        name: 'orgin',
//                                        fieldLabel: '产地',
//                                        allowBlank:false,
//                                        value: record.get('orgin')
//                                    },
//                                    {
//                                        xtype: 'combo',
//                                        fieldLabel: '产品类型',
//                                        id: 'style',
//                                        allowBlank:false,
//                                        name: 'style',
//                                        value: 'A',
//                                        store: [
//                                            ['A', 'A'],
//                                            ['B', 'B'],
//                                            ['C', 'C']
//                                        ]
//
//                                    },
//                                    {
//                                        xtype: 'combo',
//                                        id: 'location',
//                                        name: 'location',
//                                        fieldLabel: '库位',
//                                        allowBlank:false,
//                                        value: 'NORMAL',
//                                        store: [
//                                            ['NORMAL', '正常产品'],
//                                            ['GIFT', '赠品'],
//                                            ['DEFECTIVE', '缺件商品']
//                                        ]
//                                    },
//                                    ,
//                                    {
//                                        name: 'repositoryNum',
//                                        fieldLabel: '总库存',
//                                        xtype: 'numberfield',
//                                        allowBlank:false,
//                                        editable: true,
//                                        minValue: 0,
//                                        disabled:true,
//                                        allowDecimals:false,
//                                        value: record.get('repositoryNum'),
//                                        negativeText: '不得输入负数'
//                                    },
//                                    {
//                                        xtype: 'combo',
//                                        name: 'repositoryId',
//                                        id: 'repoId',
//                                        fieldLabel: '选择仓库',
//                                        allowBlank:false,
//                                        xtype: 'combo',
//                                        queryMode: "remote",
//                                        triggerAction: 'all',
//                                        forceSelection: true,
//                                        editable: false,
//                                        displayField: "name",
//                                        valueField: 'id',
//                                        emptyText: '请选择',
//                                        store: 'RepoList'
//                                    },
//                                    {
//                                        name: 'outerProductNo',
//                                        fieldLabel: '外部平台商品编号',
//                                        allowBlank:true,
//                                        value:record.get('outerProductNo'),
//                                        labelWidth:110,
//                                        maxLength:32
//                                    },
//                                ]
//                            }
//
//                        ]
//
//
//                    });
//
//                    Ext.getCmp('brandId').setValue(recordData.get("brandId"));
//                    Ext.getCmp('repoId').setValue(recordData.get("repositoryId"));
//                    //设置下拉树
//                    Ext.getCmp('categoryId').setLocalValue(record.get('prodCategoryName'),record.get('prodCategoryId'));
//
//                    Ext.getCmp('style').setValue(record.get("style"));
//                    switch (record.get("location")) {
//                        case '正常产品':
//                            Ext.getCmp('location').setValue("NORMAL");
//                            break;
//                        case '赠品':
//                            Ext.getCmp('location').setValue("GIFT");
//                            break;
//                        case '缺件商品':
//                            Ext.getCmp('location').setValue("DEFECTIVE");
//                            break;
//                    }
//
//
//
//                    platformUpdateStore.load(function () {
//
//                        var cmData;
//
//                        //创建角色数据表格容器
//                        Ext.Ajax.request({
//                            url: 'product/detail',
//                            params: {
//                                id: recordData.get('id')
//                            },
//                            async: false,
//                            success: function (response, opts) {
//                                var obj = Ext.decode(response.responseText);
//                                cmData = obj.data.obj.platforms;
//                            },
//                            failure: function (response, opts) {
//                                console.log('server-side failure with status code ' + response.status);
//                            }
//                        });
//
//
//                        var store = new Ext.data.ArrayStore({
//                            //data: [row1, row2, row3],
//                            data: cmData,
//                            fields: [
//                                'id',
//                                'name',
//                                'marketPrice',
//                                'importPrice',
//                                'isPutaway',
//                                'synStatus',
//                                'storageNum',
//                                'storagePercentReal',
//                                'prodLinkPrefix',
//                                'productPlatformId',
//                            ]
//
//                        });
//
//
//                        var platformListGrid = Ext.create("Ext.grid.Panel", {
//                            region: 'center',
//                            id: 'platformListGrid',
//                            loadMask: true,
//                            forceFit: true,
//                            border: '0',
//                            store: store,
//                            plugins: new Ext.grid.plugin.CellEditing({
//                                pluginId: 'cellEdit',
//                                clicksToEdit: 1
//                            }),
//                            columns: [
//                                {
//                                    header: 'id',
//                                    hidden:true,
//                                    dataIndex: 'id'
//                                },
//                                {
//                                    header: '平台名称',
//                                    dataIndex: 'name'
//                                },
//                                {
//                                    header: '一口价',
//                                    dataIndex: 'marketPrice',
//                                    editor: {
//                                        xtype: "textfield"
//                                    }
//                                },
//                                {
//                                    header: '促销价',
//                                    dataIndex: 'importPrice',
//                                    editor: {
//                                        xtype: "textfield"
//                                    }
//                                },
//                                {
//                                    header: '是否上架',
//                                    dataIndex: 'isPutaway',
//                                    xtype: 'checkcolumn'
//
//                                },
//                                {
//                                    header: '是否同步',
//                                    dataIndex: 'synStatus',
//                                    xtype: 'checkcolumn'
//
//                                },
//                                {
//                                    header: '库存',
//                                    dataIndex: 'storageNum',
//                                    editor: {
//                                        xtype: "numberfield",
//                                        minValue: 0,
//                                        allowDecimals:false
//                                    }
//                                },
//                                {
//                                    header: '库存占比',
//                                    dataIndex: 'storagePercentReal',
//                                    editor: {
//                                        xtype: "numberfield",
//                                        minValue: 0,
//                                        allowDecimals:false
//                                        //maxValue: 100
//                                    }
//                                },
//                                {
//                                    header: '链接',
//                                    dataIndex: 'prodLinkPrefix',
//                                    editor: {
//                                        xtype: "textfield"
//                                    }
//                                }
//                            ],
//                            listeners:{
//                                edit:function(editor,e){
///*                                  grid - 当前gird
//                                    record - 当前要编辑的record
//                                    field - 要编辑的字段名称
//                                    value - 当前值
//                                    row - grid的行序号
//                                    column - gird上定义的要编辑的Column.
//                                        rowIdx - 正在编辑的行索引
//                                    colIdx - 正在编辑的列索引
//                                    originalValue - 字段编辑前的原始值(只有配置CellEditing的时候有效)
//                                    originalValues - 字段编辑前该行的原始值(只有配置RowEditing的时候有效)
//                                    newValues - 要设置的新值(只有配置RowEditing的时候有效)
//                                    view - 这个grid的视图 (只有配置RowEditing的时候有效)
//                                    store - 当前grid的store (只有配置RowEditing的时候有效
//                                    */
//
//                                    if(e.field == 'storagePercentReal'){
//                                        e.record.set('storageNum',Math.ceil(record.get('repositoryNum')*e.record.get('storagePercentReal')/100));
//                                    }
//
//                                    if(e.field == 'storageNum'){
//                                        e.record.set('storagePercentReal',Math.floor(e.record.get('storageNum')/record.get('repositoryNum')*100));
//                                    }
//
//                                }
//                            }
//
//                        });
//
//
//                        var updateGoodListWin = Ext.create("Ext.window.Window", {
//                            title: '修改产品',
//                            width: 630,
//                            height: 600,
//                            modal: true,
//                            autoHeight: true,
//                            layout: 'border',
//                            animateTarget: Ext.getBody(),
//                            buttonAlign: 'right',
//                            bodyStyle: 'padding:5px;',
//                            items: [updateGoodsForm, platformListGrid],
//                            buttons: [
//                                {
//                                    text: "保存",
//                                    handler: function () {
//                                        var form = updateGoodsForm.getForm();
//                                        var data = form.getValues();
//
//
//                                        var plarformData = platformListGrid.getStore(),
//                                            platformIdArray = [],
//                                            marketPriceArray = [],
//                                            importPriceArray = [],
//                                            isPutawayArray = [],
//                                            storageNumArray = [],
//                                            prodLinkPrefixArray = [],
//                                            storagePercentRealArray = [],
//                                            productplatformIdArray = [],
//                                            synStatusArray = [];
//
//
//                                        store.each(function (record) {
//
//                                            platformIdArray.push(record.get('id'));
//                                            marketPriceArray.push(record.get('marketPrice'));
//                                            importPriceArray.push(record.get('importPrice'));
//                                            isPutawayArray.push(record.get('isPutaway'));
//                                            storageNumArray.push(record.get('storageNum'));
//                                            prodLinkPrefixArray.push(record.get('prodLinkPrefix'));
//                                            storagePercentRealArray.push(record.get('storagePercentReal'));
//                                            synStatusArray.push(record.get('synStatus'));
//                                            productplatformIdArray.push(record.get('productPlatformId'));
//
//                                        });
//
//                                        if (form.isValid()) {
//                                            form.submit({
//                                                submitEmptyText:false,
//                                                url: "/product/update",
//                                                params: {
//                                                    boxSize: data['boxSize'],
//                                                    picUrl: data['picUrl'],
//                                                    name: data['name'],
//                                                    brandId: data['brandId'],
//                                                    productNo: data['productNo'],
//                                                    //sku: data['sku'],
//                                                    categoryId: data['categoryId'],
//                                                    description: data['description'],
//                                                    marketPrice: data['marketPrice'],
//                                                    importPrice: data['importPrice'],
//                                                    minimumPrice: data['minimumPrice'],
//                                                    color: data['color'],
//                                                    weight: data['weight'],
//                                                    speci: data['speci'],
//                                                    orgin: data['orgin'],
//                                                    style: data['style'],
//                                                    location: data['location'],
//                                                    repositoryNum: data['repositoryNum'],
//                                                    repositoryId: data['repositoryId'],
//                                                    outerProductNo: data['outerProductNo'],
//                                                    platformIdArray: platformIdArray.join(','),
//                                                    marketPriceArray: marketPriceArray.join(','),
//                                                    importPriceArray: importPriceArray.join(','),
//                                                    isPutawayArray: isPutawayArray.join(','),
//                                                    synStatusArray: synStatusArray.join(','),
//                                                    storageNumArray: storageNumArray.join(','),
//                                                    platformUrlArray: prodLinkPrefixArray.join(','),
//                                                    productPlatformIdArray: productplatformIdArray.join(','),
//                                                    storagePercentRealArray: storagePercentRealArray.join(',')
//
//
//
//                                                },
//                                                success: function (response, options) {
//                                                    var data = Ext.JSON.decode(options.response.responseText);
//                                                    if (data.success) {//修改成功
//                                                        updateGoodListWin.close();
//                                                        Espide.Common.tipMsg('修改成功', data.msg);
//                                                        Ext.getCmp('productListGrid').getStore().reload();
//                                                    }
//                                                },
//                                                failure: function (response, options) {
//
//                                                    var data = Ext.JSON.decode(options.response.responseText);
//                                                    Ext.MessageBox.show({
//                                                        title: '提示',
//                                                        msg: data.msg,
//                                                        buttons: Ext.MessageBox.OK,
//                                                        icon: 'x-message-box-warning'
//                                                    });
//
//                                                }
//                                            });
//                                        }
//                                    }
//
//                                }
//                            ]
//                        });
//                        updateGoodListWin.show();
//
//
//                    });
//
//
//                }
//            },
//            //删除
//            '#del': {
//                'click': function (button) {
//
//                    var url = '/product/delete',
//                        ids = Espide.Common.getGridSels('productListGrid', 'id');
//
//                    if (ids.length < 1) {
//                        Espide.Common.showGridSelErr('请先选择要删除的产品');
//                        return;
//                    }
//
//                    Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
//                        Espide.Common.doAction({
//                            url: url,
//                            params: {
//                                idArray: ids.join()
//                            },
//                            successCall: function () {
//                                Ext.getCmp('productListGrid').getStore().loadPage(1);
//                            },
//                            successTipMsg: '删除成功'
//                        })(optional);
//                    });
//                }
//            },
//            //刷新
//            "#refresh": {
//                'click': function (button) {
//                    Ext.getCmp('productListGrid').getStore().load();
//                }
//            },
//            //导入
//            "#include": {
//                'click': function (button) {
//                    var uploadForm = Ext.create("Ext.form.Panel", {
//                        baseCls: 'x-plain',
//                        labelWidth: 80,
//                        defaults: {
//                            width: 380
//                        },
//                        id: 'uploadForm',
//                        border: false,
//                        layout: {
//                            type: 'hbox',
//                            align: 'center'
//                        },
//                        header: false,
//                        frame: false,
//                        bodyPadding: '20',
//                        items: [
//
//                            {
//                                xtype: "filefield",
//                                name: "multipartFile",
//                                labelWidth: 80,
//                                width: 300,
//                                fieldLabel: "导入文件",
//                                anchor: "100%",
//                                id: "multipartFile",
//                                allowBlank: false,
//                                blankText: 'Excel文件不能为空',
//                                buttonText: "选择文件",
//                                msgTarget: 'under',
//                                validator: function (value) {
//                                    var arr = value.split(".");
//                                    if (!/xls|xlsx/.test(arr[arr.length - 1])) {
//                                        return "文件不合法";
//                                    } else {
//                                        return true;
//                                    }
//                                }
//
//                            },
//                            {
//                                xtype: 'container',
//                                layout: {
//                                    type: 'hbox',
//                                    pack: 'left'
//                                },
//                                items: [
//                                    {
//                                        xtype: 'button',
//                                        cls: 'contactBtn',
//                                        margin: "0 0 0 20",
//                                        text: '下载模板',
//                                        listeners: {
//                                            'click': function () {
//                                                location.href = "/static/templet/productExcelModel.xls";
//                                            }
//                                        }
//                                    }
//                                ]}
//                        ]
//
//
//                    });
//
//
//                    var includeWin = Ext.create("Ext.window.Window", {
//                        title: '导入产品',
//                        width: 500,
//                        height: 150,
//                        modal: true,
//                        autoHeight: true,
//                        layout: 'fit',
//                        buttonAlign: 'right',
//                        bodyStyle: 'padding:5px;',
//                        animateTarget: Ext.getBody(),
//                        items: uploadForm,
//                        buttons: [
//                            {
//                                text: "确认导入",
//                                handler: function () {
//                                    var form = uploadForm.getForm();
//                                    if (form.isValid()) {
//                                        form.submit({
//                                            url: "/product/leadingIn",
//                                            waitMsg: "正在导入验证数据",
//                                            success: function (fp, o) {
//                                                var data = Ext.JSON.decode(o.response.responseText);
//                                                if (data.success) {
//                                                    Espide.Common.tipMsgIsCloseWindow(data, includeWin, 'productListGrid', true);
//                                                }
//                                            },
//                                            failure: function (fp, o) {
//
//                                                var data = Ext.JSON.decode(o.response.responseText);
//                                                Ext.MessageBox.show({
//                                                    title: '提示',
//                                                    msg: data.msg,
//                                                    buttons: Ext.MessageBox.OK,
//                                                    icon: 'x-message-box-warning'
//                                                });
//
//                                            }
//                                        });
//                                    }
//                                }
//
//                            }
//                        ]
//                    });
//
//                    includeWin.show();
//                }
//            }
        });
    }



});