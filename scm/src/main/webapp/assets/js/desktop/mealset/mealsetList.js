/**
 * 套餐列表
 */

//定义品牌列表数据模型
Ext.define('EBDesktop.mealsetListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name', 'code', 'sellDescription', 'sku'],
    idProperty: 'id'
});


//定义品牌列表数据模型
Ext.define('EBDesktop.mealsetProductListModel', {
    extend: 'Ext.data.Model',
    fields: [
        'id',                   //商品id
        'brandId',                   //商品id
        'brand',          	//商品品牌
        'picUrl',
        {name: 'brandName', type: 'string', mapping: 'brand.name'},
        'name',          	//商品名称
        'productNo',            	//商品编号
        'sku',          	//商品条形码
        'category',
        {name: 'prodCategoryName', type: 'string', mapping: 'category.name'},
        'categoryId',
        'cid',   	//商品分类id
        'description',          //商品描述
        'importPrice',            //现价
        'marketPrice',        //市场价
        'minimumPrice',             //最低价
        'color',                //颜色
        'weight',               //重量
        'boxSize',              //尺寸
        'speci',                //规格
        'type',                  //商品类型
        'orgin', //产地
        'style', //产品类型
        'location', //库位赠品
        'mealCount',
        'amount',
        'mealPrice',
        'price'

    ],
    idProperty: 'id'
});


//定义套餐下拉列表
Ext.define('EBDesktop.searchLsitModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});


var searchStore = Ext.create("Ext.data.Store", {
    extend: 'Ext.data.Store',
    model: "EBDesktop.searchLsitModel",
    data: [
        {
            id: 1, name: "商品名称"
        },
        {
            id: 2, name: "商品编号"
        }
    ]
});


//定义品牌列表容器
Ext.define('EBDesktop.mealset.mealsetList', {
    extend: 'Ext.container.Container',
    alias: 'widget.mealsetList',
    id: 'mealsetList',
    title: "套餐管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {

        // 移除套餐
        function removeMealsetList() {

            var url = '/mealset/delete',
                ids = Espide.Common.getGridSels('mealsetListGrid', 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的套餐');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        id: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('mealsetListGrid').getStore().loadPage(1);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });


        }

        // 刷新Grid
        function refreshmealsetGrid() {
            Ext.getCmp('mealsetListGrid').getStore().load();
        }

        // 套餐数据源
        var mealsetListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.mealsetListModel',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: '/mealset/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'message',
                    totalProperty: 'data.obj.totalCount'
                }
            },
            listeners: {
                exception: function (proxy, response, operation) {
                    var data = Ext.decode(response.responseText);
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: data.msg,
                        icon: Ext.MessageBox.ERROR,
                        button: Ext.Msg.OK
                    });
                }
            },
            autoSync: true,
            autoLoad: {start: 0, limit: 13},
            pageSize: 13
        });

        // 套餐产品据源
        var mealsetProcuctListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.mealsetProductListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/product/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result'
                }
            },
            listeners: {
                exception: function (proxy, response, operation) {
                    var data = Ext.decode(response.responseText);
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: data.msg,
                        icon: Ext.MessageBox.ERROR,
                        button: Ext.Msg.OK
                    });
                }
            },
            autoLoad: false
        });


        // 套餐产品据源
        var mealsetProcuctListStore1 = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.mealsetProductListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/product/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'message'
                }
            },
            listeners: {
                exception: function (proxy, response, operation) {
                    var data = Ext.decode(response.responseText);
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: data.msg,
                        icon: Ext.MessageBox.ERROR,
                        button: Ext.Msg.OK
                    });
                }
            },
            autoLoad: false
        });


        var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        });

        //套餐查询
        function searchMealsetGrid(button) {
            Espide.Common.doSearch("mealsetListGrid", "searchMealsetGrid", true);
        }


        //触发上面数据添加到 下面表格数据列表 并且判断是否数据重复
        function onrowclick(grid, addGrid, records, index, e) {
            //分别获得两个gridpanel的store
            var leftStore = grid.getStore();
            //var addWin = Ext.getCmp('procuctRight');
            var rightStore = addGrid.getStore();
            var leftProdCode;
            var flag = false;
            //判断是否含有相同条形码
            function haveProdCode(leftProdCode) {
                rightStore.each(function (record, index) {
                    if (record.get('sku') == leftProdCode) {
                        flag = true;
                    }
                });
                return flag;
            }


            //创建一个新的Ext.data.record 保持与套餐底下一致防止冲突
            var newgood = new EBDesktop.mealsetProductListModel({
                id: records.get('id'),
                sku: records.get('sku'),
                amount: '1',
                price: records.get('marketPrice'),
                productNo: records.get('productNo'),
                name: records.get('name'),
                marketPrice: records.get('marketPrice')

            });

            if (rightStore.data.items.length != 0) {  //判断是否是首次插入数据
                //左边栏的条形码
                Ext.Array.each(records, function (record) {
                    leftProdCode = record.get("sku");
                });


                //如果没有相同的条形码 插入数据
                if (!haveProdCode(leftProdCode)) {


                    rightStore.insert(0, newgood);
                    leftStore.remove(records);
                } else {
                    Ext.Msg.alert('警告', '暂存仓已有选中商品，请先移除已选再添加');
                }
            } else { //如果是首次 顺利插入数据

                rightStore.insert(0, newgood);
                leftStore.remove(records);
            }
        }

        //添加套餐
        function mealsetAdd() {

            //创建上面产品数据表格
            var leftProducgrid = Ext.create('Ext.grid.Panel', {
                region: 'center',
                id: 'procuctLeft',
                width: 'auto',
                border: 5,
                height: 200,
                loadMask: true,
                forceFit: true,
                split: true,
                store: mealsetProcuctListStore,
                tbar: [
                    {xtype: 'combo',
                        store: [
                        ["sku", "产品条形码"],
                        ["name", "产品名称"],
                        ["productNo", "商品编号"]

                    ], queryMode: 'local', name: "searchType", value: "productNo", width: 100, itemId: "type"},
                    {
                        xtype: 'textfield',
                        name: 'shopName',
                        emptyText:'请输入关键字',
                        width: 150,
                        itemId: "value"},
                    {
                        xtype: 'button',
                        name: 'submuit',
                        iconCls: 'icon-search',
                        text: '搜索',
                        handler: function (btn) {
                            var value = btn.up('grid').down('#value').getValue();
                            var type = btn.up('grid').down('#type').getValue();
                            btn.up('grid').getStore().load({
                                params: {
                                    searchType: type,
                                    searchValue: value,
                                    type: "PRODUCT"
                                }
                            });
                        }}
                ],
                columns: [
                    {
                        header: '产品条形码',
                        dataIndex: "sku",
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {
                        header: '商品编号',
                        dataIndex: "productNo",
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {
                        header: '产品名称',
                        dataIndex: 'name',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {
                        header: '市场价格',
                        dataIndex: 'marketPrice',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        },
                        renderer: function (value) {
                            return value;
                        }
                    },
                    {
                        xtype: 'actioncolumn',
                        text: '添加',
                        menuDisabled: true,
                        width: 50,
                        items: [
                            {
                                iconCls: 'icon-add'
                            }
                        ],
                        handler: function (btn, rowIndex, colIndex, item, e, record) {
                            var grid = btn.up('grid');
                            onrowclick(grid, Ext.getCmp('procuctRight'), record);
                        }
                    }

                ]

            });

            //创建下面数据
            var addWin = Ext.create("Ext.grid.Panel", {
                region: 'south',
                id: 'procuctRight',
                width: 'auto',
                height: 250,
                border: 5,
                loadMask: true,
                forceFit: true,
                split: true,
                store: mealsetProcuctListStore1,
                tbar: [
                    Ext.create('Ext.form.Panel', {
                        layout: 'hbox',
                        border: false,
                        itemId: 'mealsetAdd',
                        id: 'mealsetAdd',
                        items: [
                            {
                                xtype: 'textfield',
                                fieldLabel: '套餐名称',
                                labelWidth: 60,
                                id: 'mealName',
                                name: 'mealName',
                                emptyText:'请输入套餐名称',
                                allowBlank: false,
                                maxLength: 20,
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
                            },
                            {
                                xtype: 'textfield',
                                fieldLabel: '套餐sku',
                                labelWidth: 60,
                                width: 320,
                                id: 'sku',
                                name: 'sku',
                                maxLength: 20,
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
                            },
                            {
                                name: 'description',
                                id: "sellDescription",
                                fieldLabel: '套餐描述',
                                emptyText:'请输入套餐描述',
                                xtype: 'textfield',
                                labelWidth: 60,
                                maxLength: 500,
                                allowBlank: false
                            }
                        ]
                    })

                ],
                features: [
                    {
                        ftype: 'summary'
                    }
                ],
                plugins: [cellEditing],
                columns: [
                    {
                        header: '产品条形码',
                        dataIndex: "sku"
                    },
                    {
                        header: '商品编号',
                        dataIndex: "productNo"
                    },
                    {
                        header: '产品名称',
                        dataIndex: 'name'
                    },
                    {
                        header: '市场价格',
                        width: 120,
                        dataIndex: 'marketPrice',
                        renderer: function (value) {
                            return value;
                        },
                        summaryType: function (records) {
                            var i = 0,
                                length = records.length,
                                total = 0,
                                record;

                            for (; i < length; ++i) {
                                record = records[i];
                                total += record.get('marketPrice') * record.get('amount');
                            }
                            return total;
                        },
                        summaryRenderer: function (value) {
                            return "产品总价(元):" + value;
                        }
                    },
                    {
                        header: '套餐价格',
                        dataIndex: 'price',
                        width: 120,
                        field: {
                            xtype: 'numberfield'
                        },
                        renderer: function (value) {
                            return value;
                        },
                        summaryType: function (records) {
                            var i = 0,
                                length = records.length,
                                total = 0,
                                record;

                            for (; i < length; ++i) {
                                record = records[i];
                                total += record.get('price') * record.get('amount');
                            }
                            return total;
                        },
                        summaryRenderer: function (value) {
                            return "套餐总价(元):" + value;
                        }

                    },
                    {
                        header: '数量',
                        width: 50,
                        dataIndex: 'amount',
                        editor: {
                            xtype: "numberfield",
                            allowBlank: false,
                            minValue: 1
                        }

                    },
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

                ]

            });

            var winContainer = Ext.create("Ext.window.Window", {
                title: '添加套餐',
                collapsible: true,
                maximizable: true,
                modal: true,
                layout: 'border',
                width: 820,
                height: 600,
                animateTarget: Ext.getBody(),
                buttonAlign: 'right',
                items: [
                    leftProducgrid, addWin
                ],
                buttons: [
                    {
                        text: "保存",
                        handler: function () {
                            var addStore = addWin.getStore();
                            var shopPriceStr = [], prodCode = [] , idArray = [], mealCount = [];
                            addStore.each(function (record, index) {
                                shopPriceStr.push(record.get("price"));
                                prodCode.push(record.get("productNo"));
                                idArray.push(record.get("id"));
                                mealCount.push(record.get('amount'))
                            });

                            var mealName = Ext.getCmp("mealName").getValue();
                            var sellDescription = Ext.getCmp("sellDescription").getValue();
                            var sku = Ext.getCmp("sku").getValue();


                            if (Ext.getCmp('mealsetAdd').getForm().isValid()) {
                                Ext.Ajax.request({
                                    url: '/mealset/save',
                                    params: {
                                        name: mealName,
                                        sku: sku,
                                        sellDescription: sellDescription,
                                        mealPrice: shopPriceStr.join(","),
                                        prodId: idArray.join(","),
                                        mealCount: mealCount.join(",")
                                    },
                                    success: function (response, options) { //成功返回回调函数
                                        var data = Ext.JSON.decode(response.responseText);
                                        if (data.success) {

                                            Espide.Common.tipMsg('保存成功', data.msg);
                                            winContainer.close();
                                            Ext.getCmp('mealsetListGrid').getStore().load();

                                        }
                                    },
                                    failure: function (response, options) {

                                        var data = Ext.JSON.decode(response.responseText);
                                        Ext.MessageBox.show({
                                            title: '提示',
                                            msg: data.msg,
                                            buttons: Ext.MessageBox.OK,
                                            icon: 'x-message-box-warning'
                                        });

                                    }

                                });
                            }

                        }
                    }
                ]
            });
            //显示弹窗
            Ext.getCmp("procuctLeft").getStore().removeAll();
            Ext.getCmp("procuctRight").getStore().removeAll();
            winContainer.show();
        }


        //顶栏表单
        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'searchMealsetGrid',
            defaults: {
                xtype: 'combo',
                labelWidth: 90,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'combo',
                    width:120,
                    name: 'searchType',
                    itemId: 'searchType',
                    editable: false,
                    queryMode: 'local',
                    value: 'sku',
                    store: [
                        ["sku", "套餐sku"],
                        ["name", "套餐名称"],
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'searchValue',
                    emptyText:'请输入套餐名称',
                    listeners: {
                        change: function () {
                            Espide.Common.reLoadGird('mealsetListGrid', 'searchMealsetGrid', true);
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchMealsetGrid
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: mealsetAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选',
                    itemId: 'deleteBtn',
                    handler: removeMealsetList
                }
            ]
        });


        //创建角色数据表格容器
        var mealsetListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'mealsetListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: mealsetListStore,
                viewConfig: {
                    enableTextSelection: true
                },
                columns: [
                    {
                        header: '套餐名称',
                        dataIndex: 'name',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {
                        header: '套餐sku',
                        dataIndex: 'sku',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }

                    }

                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: mealsetListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'afterrender': function (grid) {
                        grid.getStore().getProxy().extraParams = Ext.getCmp('searchMealsetGrid').getValues();
                    },
                    'render': function (input) {
                        var map = new Ext.util.KeyMap({
                            target: 'searchMealsetGrid',    //target可以是组建的id  加单引号
                            binding: [
                                {                       //绑定键盘事件
                                    key: Ext.EventObject.ENTER,
                                    fn: function () {
                                        Espide.Common.reLoadGird('mealsetListGrid', 'searchMealsetGrid', true);
                                    }
                                }
                            ]
                        });
                    },
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件

                        var mealsetId = record.get("id");
                        var mealsetSku = record.get('sku');
                        Ext.Ajax.request({
                            url: '/mealSetItem/list',
                            params: {mealSetId: record.get('id')},
                            success: function (response, options) { //成功返回回调函数
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) { // 有套餐
                                    //创建修改
//                                    var updateProductgrid = Ext.create('Ext.grid.Panel', {
//                                        region: 'center',
//                                        id: 'updateProductgrid',
//                                        border: 5,
//                                        height: 200,
//                                        loadMask: true,
//                                        forceFit: true,
//                                        split: true,
//                                        store: mealsetProcuctListStore,
//                                        tbar: {
//                                            items: [
//                                                {
//                                                    xtype: 'combo',
//                                                    store: [
//                                                        ["sku", "产品条形码"],
//                                                        ["name", "产品名称"],
//                                                        ["productNo", "产品编号"]
//                                                    ],
//                                                    queryMode: 'local',
//                                                    name: "searchType",
//                                                    value: "sku",
//                                                    width: 100,
//                                                    itemId: "type"
//                                                },
//                                                {
//                                                    xtype: 'textfield',
//                                                    name: 'shopName',
//                                                    width: 150,
//                                                    itemId: "value"},
//                                                {
//                                                    xtype: 'button',
//                                                    name: 'submuit',
//                                                    text: '搜索',
//                                                    handler: function (btn) {
//                                                        var value = btn.up('grid').down('#value').getValue();
//                                                        var type = btn.up('grid').down('#type').getValue();
//                                                        btn.up('grid').getStore().load({
//                                                            params: {
//                                                                searchType: type,
//                                                                searchValue: value,
//                                                                type: "PRODUCT"
//                                                            }
//                                                        });
//                                                    }}
//                                            ]
//                                        },
//                                        columns: [
//
//                                            {
//                                                header: '产品条形码',
//                                                dataIndex: "sku",
//                                                editor: {
//                                                    xtype: "textfield",
//                                                    allowBlank: false
//                                                }
//                                            },
//                                            {
//                                                header: '产品编号',
//                                                dataIndex: "productNo",
//                                                editor: {
//                                                    xtype: "textfield",
//                                                    allowBlank: false
//                                                }
//                                            },
//                                            {
//                                                header: '产品名称',
//                                                dataIndex: 'name',
//                                                editor: {
//                                                    xtype: "textfield",
//                                                    allowBlank: false
//                                                }
//                                            },
//                                            {
//                                                header: '市场价格',
//                                                dataIndex: 'marketPrice',
//                                                editor: {
//                                                    xtype: "textfield",
//                                                    allowBlank: false
//                                                },
//                                                renderer: function (value) {
//                                                    return parseInt(value);
//                                                }
//                                            },
//                                            {
//                                                xtype: 'actioncolumn',
//                                                text: '添加',
//                                                menuDisabled: true,
//                                                width: 50,
//                                                items: [
//                                                    {
//                                                        iconCls: 'icon-add'
//                                                    }
//                                                ],
//                                                handler: function (btn, rowIndex, colIndex, item, e, record) {
//                                                    var grid = btn.up('grid');
//                                                    onrowclick(grid, Ext.getCmp('addUpdateWin'), record);
//                                                }
//                                            }
//
//
//                                        ]
//
//                                    });
                                    //创建右边数据
                                    var addUpdateWin = Ext.create("Ext.grid.Panel", {
                                        region: 'south',
                                        id: 'addUpdateWin',
                                        width: 'auto',
                                        height: 200,
                                        border: 5,
                                        forceFit: true,
                                        split: true,
                                        autoScroll: true,
                                        store: {
                                            fields: [
                                                {name: 'id', type: 'auto'},
                                                'product',
                                                {name: 'sku', type: 'auto', mapping: 'product.sku'},
                                                {name: 'id', type: 'auto', mapping: 'product.id'},
                                                {name: 'productNo', type: 'auto', mapping: 'product.productNo'},
                                                {name: 'name', type: 'auto', mapping: 'product.name'},
                                                {name: 'marketPrice', type: 'auto', mapping: 'product.marketPrice'},
                                                {name: 'price', type: 'auto'},
                                                {name: 'amount', type: 'auto'}
                                            ],
                                            data: data.data.obj,
                                            autoLoad: true
                                        },
                                        tbar: {
                                            layout: {
                                                type: 'hbox',
                                                align: 'left'
                                            },
                                            items: [
                                                Ext.create('Ext.form.Panel', {
                                                    layout: 'hbox',
                                                    border: false,
                                                    itemId: 'mealsetEdit',
                                                    id: 'mealsetEdit',
                                                    items: [
                                                        {
                                                            xtype: 'textfield',
                                                            fieldLabel: '套餐名称',
                                                            id: 'mealName',
                                                            name: 'mealName',
                                                            allowBlank: false,
                                                            maxLength: 20,
                                                            labelWidth: 60,
                                                            value: record.get('name')
                                                        },
                                                        {
                                                            xtype: 'textfield',
                                                            fieldLabel: '套餐sku',
                                                            id: 'sku',
                                                            name: 'sku',
                                                            maxLength: 20,
                                                            allowBlank: false,
                                                            width:320,
                                                            labelWidth: 60,
                                                            value: record.get('sku')
                                                        },
                                                        {
                                                            name: 'description',
                                                            id: "sellDescription",
                                                            fieldLabel: '套餐描述',
                                                            xtype: 'textfield',
                                                            maxLength: 500,
                                                            labelWidth: 60,
                                                            value: record.get('sellDescription')
                                                        }
                                                    ]
                                                })

                                            ]
                                        },
                                        features: [
                                            {
                                                ftype: 'summary'
                                            }
                                        ],
                                        columns: [
                                            {
                                                header: '产品条形码',
                                                dataIndex: "sku"
                                            },
                                            {
                                                header: '商品编号',
                                                dataIndex: "productNo"
                                            },
                                            {
                                                header: '产品名称',
                                                dataIndex: 'name'
                                            },
                                            {
                                                header: '市场价格',
                                                width: 120,
                                                dataIndex: 'marketPrice',
                                                renderer: function (value) {
                                                    return value;
                                                },
                                                summaryType: function (records) {
                                                    var i = 0,
                                                        length = records.length,
                                                        total = 0,
                                                        record;

                                                    for (; i < length; ++i) {
                                                        record = records[i];
                                                        total += record.get('marketPrice') * record.get('amount');
                                                    }
                                                    return total;
                                                },
                                                summaryRenderer: function (value) {
                                                    return "产品总价(元):" + value;
                                                }

                                            },
                                            {
                                                header: '套餐价格',
                                                dataIndex: 'price',
                                                width: 120,
                                                field: {
                                                    xtype: 'numberfield'
                                                },
                                                renderer: function (value) {
                                                    return value;
                                                },
                                                summaryType: function (records) {
                                                    var i = 0,
                                                        length = records.length,
                                                        total = 0,
                                                        record;

                                                    for (; i < length; ++i) {
                                                        record = records[i];
                                                        total += record.get('price') * record.get('amount');
                                                    }
                                                    return total;
                                                },
                                                summaryRenderer: function (value) {
                                                    return "套餐总价(元):" + value;
                                                }

                                            },
                                            {
                                                header: '数量',
                                                dataIndex: 'amount',
                                                width: 50
//                                                editor: {
//                                                    xtype: "numberfield",
//                                                    allowBlank: false
//                                                }
                                            }


                                        ]

                                    });


                                    var winContainer = Ext.create("Ext.window.Window", {
                                        title: '查看套餐',
                                        collapsible: true,
                                        maximizable: true,
                                        // modal: true,
                                        layout: 'border',
                                        resizable: false,
                                        width: 820,
                                        height: 290,
                                        animateTarget: Ext.getBody(),
                                        buttonAlign: 'right',
                                        items: [
                                            addUpdateWin
                                        ],
                                        buttons: [
                                            {
                                                text: "保存",
                                                handler: function () {
                                                    var addStore = addUpdateWin.getStore();
                                                    var mealPriceStr = [], prodCode = [] , idArray = [], mealCount = [];


                                                    var mealName = Ext.getCmp("mealName").getValue();
                                                    var sellDescription = Ext.getCmp("sellDescription").getValue();
                                                    var sku = Ext.getCmp("sku").getValue();


                                                    if (Ext.getCmp('mealsetEdit').getForm().isValid()) {
                                                        Ext.Ajax.request({
                                                            url: '/meal_set/update',
                                                            params: {
                                                                id: mealsetId,
                                                                name: mealName,
                                                                sellDescription: sellDescription,
                                                                sku: sku

                                                            },
                                                            success: function (response, options) { //成功返回回调函数
                                                                var data = Ext.JSON.decode(response.responseText);
                                                                if (data.success) {
                                                                    Espide.Common.tipMsg('保存成功', data.msg);
                                                                    winContainer.close();
                                                                    Ext.getCmp('mealsetListGrid').getStore().load();
                                                                }
                                                            },
                                                            failure: function (response, options) {

                                                                var data = Ext.JSON.decode(response.responseText);
                                                                Ext.MessageBox.show({
                                                                    title: '提示',
                                                                    msg: data.msg,
                                                                    buttons: Ext.MessageBox.OK,
                                                                    icon: 'x-message-box-warning'
                                                                });

                                                            }

                                                        });

                                                    }

                                                }
                                            }
                                        ]
                                    });
                                    //显示弹窗
                                    // Ext.getCmp("updateProductgrid").getStore().removeAll();
                                    winContainer.show();
                                }
                            },
                            failure: function (response, options) {

                                var data = Ext.JSON.decode(response.responseText);
                                Ext.MessageBox.show({
                                    title: '提示',
                                    msg: data.msg,
                                    buttons: Ext.MessageBox.OK,
                                    icon: 'x-message-box-warning'
                                });

                            }

                        });


                    }
                }

            }
        );


        this.items = [searchForm, mealsetListGrid];
        this.callParent(arguments);
    }

});
