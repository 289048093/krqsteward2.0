/**
 * 品牌列表
 */

//定义品牌列表数据模型
Ext.define('EBDesktop.goodsListModel', {
    extend: 'Ext.data.Model',
    fields: [
        'id',                   //商品id
        'brandName',                //商品品牌
        'prodName',          //商品名称
        'prodNo',            //商品编号
        'prodCode',          //商品条形码
        'prodCategoryName',                 //商品分类id
        'description',          //商品描述
        'shopPrice',            //销售价
        'standardPrice',        //市场价
        'buyPrice',             //进货价
        'color',                //颜色
        'weight',               //重量
        'boxSize',              //尺寸
        'speci',                 //规格
        'type'                  //商品类型
    ],
    idProperty: 'id'
});

Ext.define('EBDesktop.brandCategoryModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});

//定义品牌列表数据模型
Ext.define('EBDesktop.brandListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});

//定义套餐下拉列表
Ext.define('EBDesktop.searchLsitModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});





//定义品牌列表容器
Ext.define('EBDesktop.goods.goodsList', {
    extend: 'Ext.container.Container',
    alias: 'widget.goodsList',
    id: 'goodsList',
    title: "商品管理",
    fixed: true,
    layout: 'fit',
    initComponent: function () {

        // 移除用户
        function removegoodsList() {
            var grid = Ext.getCmp('goodsListGrid'),
                records = grid.getSelectionModel().getSelection();

            if (records.length < 1) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '请先选择要删除商品。',
                    model: false,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
            } else {

                Ext.Msg.confirm("提示", "您确定要删除所选的商品吗？", function (btnId) {
                    if (btnId == "yes") {
                        var idArray = [];
                        Ext.Array.each(records, function (record) {
                            idArray.push(record.get("id"));
                        });

                        Ext.Ajax.request({
                            url: "http://172.16.1.161:8080/product/delete",
                            method: "get",
                            params: {
                                idArray: idArray.join(",")
                            },
                            success: function (response, options) {
                                Ext.Msg.alert("提示", Ext.JSON.decode(response.responseText).msg);
                                Ext.getCmp('goodsListGrid').getStore().load();
                            },
                            failure: function (response, options) {
                                Ext.Msg.alert("提示", Ext.JSON.decode(response.responseText).msg);
                            }
                        });
                    }
                });

            }

        }

        // 刷新Grid
        function refreshgoodsGrid() {
            Ext.getCmp('goodsListGrid').getStore().load();
        }


        // 产品导入
        function productsInclude() {


            var uploadForm = Ext.create("Ext.form.Panel", {
                baseCls: 'x-plain',
                labelWidth: 80,
                defaults: {
                    width: 380
                },
                id: 'uploadForm',
                border: false,
                layout: {
                    type: 'vbox',
                    align: 'center'
                },
                header: false,
                frame: false,
                bodyPadding: '20',
                items: [

                    {
                        xtype: 'container',
                        layout: {
                            type: 'hbox',
                            pack: 'center'
                        },
                        items: [
                            {
                                xtype: 'button',
                                cls: 'contactBtn',
                                margin: "20 20 30 20",
                                text: '下载Excel模板',
                                listeners: {
                                    'click': function () {
                                        location.href = "http://172.16.1.161:8080/templet/excelmode.xls";
                                    }
                                }
                            }
                        ]},
                    {
                        xtype: "filefield",
                        name: "multipartFile",
                        fieldLabel: "导入产品Excel",
                        anchor: "100%",
                        id: "multipartFile",
                        allowBlank: false,
                        blankText: 'Excel文件不能为空',
                        buttonText: "选择导入文件",
                        msgTarget: 'under',
                        validator: function (value) {
                            var arr = value.split(".");
                            if (!/xls|xlsx/.test(arr[arr.length - 1])) {
                                return "文件不合法";
                            } else {
                                return true;
                            }
                        }

                    }
                ]


            });


            var includeWin = Ext.create("Ext.window.Window", {
                title: '导入产品',
                width: 500,
                height: 300,
                modal: true,
                autoHeight: true,
                layout: 'fit',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px;',
                items: uploadForm,
                buttons: [
                    {
                        text: "确认导入产品",
                        handler: function () {
                            var form = uploadForm.getForm();
                            if (form.isValid()) {
                                form.submit({
                                    url: "http://172.16.1.161:8080/product/add",
                                    waitMsg: "正在导入验证数据",
                                    success: function (fp, o) {
                                        Ext.Msg.alert("提示", Ext.JSON.decode(o.response.responseText).msg, function () {
                                            if (Ext.JSON.decode(o.response.responseText).data.status == "success") {
                                                //关闭上传窗口
                                                includeWin.close();
                                            }
                                        });
                                    }
                                });
                            }
                        }

                    }
                ]
            });

            includeWin.show();
        }


        // 用户数据源
        var brandCategoryListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.brandCategoryModel',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: 'http://172.16.1.161:8080/prodCategory/list'

                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'message'
                },
                writer: {
                    type: 'json',
                    encode: true,
                    writeAllFields: true,
                    root: 'data.list'
                }
            },
            autoSync: true,
            autoLoad: true
        });

        // 用户数据源
        var goodsListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.goodsListModel',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: 'http://172.16.1.161:8080/product/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'message',
                    totalProperty: 'data.obj.totalCount'
                },
                writer: {
                    type: 'json',
                    encode: true,
                    writeAllFields: true,
                    root: 'data.obj.result'
                }
            },
            autoSync: true,
            autoLoad: {start: 0, limit: 10},
            pageSize: 10
        });


        // 用户数据源
        var brandListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.brandListModel',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: 'http://172.16.1.161:8080/brand/list'

                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'message'
                },
                writer: {
                    type: 'json',
                    encode: true,
                    writeAllFields: true,
                    root: 'data.list'
                }
            },
            autoSync: true,
            autoLoad: true
        });


        //创建角色数据表格容器
        var goodsListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'goodsListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: goodsListStore,
                columns: [
                    {
                        header: '商品id',
                        dataIndex: 'id'
                    },
                    {
                        header: '商品品牌',
                        dataIndex: 'brandName'
                    },
                    {
                        header: '商品名称',
                        dataIndex: 'prodName'
                    },
                    {
                        header: '商品编号',
                        dataIndex: 'prodNo'
                    },
                    {
                        header: '商品条形码',
                        dataIndex: 'prodCode'
                    },
                    {
                        header: '商品分类id',
                        dataIndex: 'prodCategoryName'
                    },
                    {
                        header: '商品描述',
                        dataIndex: 'description'
                    },
                    {
                        header: '销售价',
                        dataIndex: 'standardPrice'
                    },
                    {
                        header: '市场价',
                        dataIndex: 'shopPrice'
                    },
                    {
                        header: '进货价',
                        dataIndex: 'buyPrice'
                    },
                    {
                        header: '颜色',
                        dataIndex: 'color'
                    },
                    {
                        header: '重量',
                        dataIndex: 'weight'
                    },
                    {
                        header: '尺寸',
                        dataIndex: 'boxSize'
                    },
                    {
                        header: '规格',
                        dataIndex: 'speci'
                    },
                    {
                        header: '商品类型',
                        dataIndex: 'type',
                        renderer: function (value) {
                            switch (value) {
                                case "PRODUCT":
                                    return "商品";
                                    break;
                                case "GIFT":
                                    return "赠品";
                                    break;
                            }
                        }
                    }

                ],


                tbar: [
                    {
                        xtype: 'button',
                        text: '添加商品',
                        iconCls: 'icon-add',
                        handler: function () {
                            var addGoodsForm = Ext.create('Ext.form.Panel', {
                                region: 'center',
                                id: 'addGoodsForm',
                                height: 'auto',
                                bodyPadding: 10,
                                layout: 'anchor',
                                border: 0,
                                defaults: {
                                    xtype: 'textfield',
                                    margin: '0 0 5 0',
                                    labelWidth: 100,
                                    labelAlign: 'right',
                                    width: 350,
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false
                                },
                                items: [
                                    { name: 'picUrl', fieldLabel: '商品图片', hidden: true, value: "无"},
                                    {   name: 'type',
                                        id: "type",
                                        xtype: 'combo',
                                        queryMode: 'local',
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        fieldLabel: '产品分类',
                                        value: 'PRODUCT',
                                        store: [
                                            ['GIFT', '赠品'],
                                            ['PRODUCT', '商品']
                                        ],
                                        listeners: {
                                            change: function () {
                                                var form = Ext.getCmp('addGoodsForm');
                                                var upform = form.getForm();
                                                if (Ext.getCmp("type").getRawValue() == "赠品") {
                                                    upform.findField('standardPrice').setValue(0);
                                                    upform.findField('shopPrice').setValue(0);
                                                    upform.findField('buyPrice').setValue(0);
                                                    upform.findField('standardPrice').setReadOnly(true);
                                                    upform.findField('shopPrice').setReadOnly(true);
                                                    upform.findField('buyPrice').setReadOnly(true);

                                                } else {
                                                    upform.findField('standardPrice').setValue("");
                                                    upform.findField('shopPrice').setValue("");
                                                    upform.findField('buyPrice').setValue("");
                                                    upform.findField('standardPrice').setReadOnly(false);
                                                    upform.findField('shopPrice').setReadOnly(false);
                                                    upform.findField('buyPrice').setReadOnly(false);
                                                }
                                            }
                                        }
                                    },
                                    { name: 'prodName', fieldLabel: '商品名称'},
                                    {   name: 'brandId',
                                        fieldLabel: '商品品牌',
                                        xtype: 'combo',
                                        queryMode: "remote",
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        displayField: "name",
                                        valueField: 'id',
                                        blankText: '请选择',
                                        store: brandListStore
                                    },
                                    { name: 'prodNo', fieldLabel: '商品编号'},
                                    { name: 'prodCode', fieldLabel: '商品条形码' },
                                    { name: 'cid',
                                        xtype: 'combo',
                                        queryMode: "remote",
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        displayField: "name",
                                        fieldLabel: '产品类型',
                                        value: 'name',
                                        valueField: 'id',
                                        blankText: '请选择',
                                        store: brandCategoryListStore
                                    },
                                    { name: 'description', fieldLabel: '商品描述', xtype: 'textareafield', height: 50 },
                                    { name: 'standardPrice', id: "standardPrice", fieldLabel: '销售价(元)'},
                                    { name: 'shopPrice', id: "shopPrice", fieldLabel: '市场价(元)'},
                                    { name: 'buyPrice', id: "buyPrice", fieldLabel: '进货价(元)'},
                                    { name: 'color', fieldLabel: '颜色'},
                                    { name: 'weight', fieldLabel: '重量(公斤)'},
                                    { name: 'boxSize', fieldLabel: '尺寸(厘米)'},
                                    { name: 'speci', fieldLabel: '规格'}
                                ]

                            });


                            var addGoodListWin = Ext.create("Ext.window.Window", {
                                title: '添加商品',
                                width: 440,
                                height: 600,
                                modal: true,
                                autoHeight: true,
                                layout: 'fit',
                                buttonAlign: 'center',
                                bodyStyle: 'padding:5px;',
                                items: addGoodsForm,
                                buttons: [
                                    {
                                        text: "保存商品",
                                        handler: function () {
                                            var form = addGoodsForm.getForm();
                                            var data = form.getValues();
                                            if (form.isValid()) {
                                                form.submit({
                                                    url: "http://172.16.1.161:8080/product/add",
                                                    params: form.getValues(),
                                                    waitMsg: "正在导入验证数据",
                                                    success: function (fp, o) {
                                                        Ext.Msg.alert("提示", Ext.JSON.decode(o.response.responseText).msg, function () {
                                                            addGoodListWin.close();
                                                            refreshgoodsGrid();
                                                        });
                                                    }
                                                });
                                            }
                                        }

                                    }
                                ]
                            });
                            addGoodListWin.show();


                        }},
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-remove',
                        handler: removegoodsList
                    },
                    {
                        xtype: 'button',
                        text: '刷新',
                        iconCls: 'icon-refresh',
                        handler: refreshgoodsGrid
                    },
                    "-",
                    {xtype: 'combo', store:[
                     ["prodCode","商品条形码"],
                     ["prodName","商品名称"]

                    ], queryMode: 'local', name:"searchType", value: "prodCode", width: 100, itemId: "type"},
                    {xtype: 'textfield', name: 'shopName', width: 150, itemId: "prodNo"},
                    {xtype: 'button', name: 'submuit', text: '搜索', handler: function (btn) {

                        var prodNo = btn.up('grid').down('#prodNo').getValue();
                        var type = btn.up('grid').down('#type').getValue();
                        btn.up('grid').getStore().reload({
                            params: {
                                searchType: type,
                                searchValue:prodNo
                            }
                        });

                    }},
                    '->',
                    {
                        xtype: 'button',
                        text: '导入产品',
                        iconCls: 'icon-add',
                        handler: productsInclude
                    }

                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 10,
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: goodsListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件

                        var updateGoodsForm = Ext.create('Ext.form.Panel', {
                            region: 'center',
                            id: 'updateGoodsForm',
                            height: 'auto',
                            bodyPadding: 10,
                            layout: 'anchor',
                            border: 0,
                            defaults: {
                                xtype: 'textfield',
                                margin: '0 0 5 0',
                                labelWidth: 100,
                                labelAlign: 'right',
                                width: 350,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false
                            },
                            items: [
                                { name: 'picUrl', fieldLabel: '商品图片', hidden: true, value: "无"},
                                { name: 'id', hidden: true, value: record.get("id")},
                                {   name: 'type',
                                    id: "type",
                                    xtype: 'combo',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    fieldLabel: '产品分类',
                                    value: 'PRODUCT',
                                    store: [
                                        ['PRODUCT', '商品'],
                                        ['GIFT', '赠品']
                                    ],
                                    listeners: {
                                        change: function () {
                                            var form = Ext.getCmp('updateGoodsForm');
                                            var upform = form.getForm();
                                            if (Ext.getCmp("type").getRawValue() == "赠品") {
                                                upform.findField('standardPrice').setValue(0);
                                                upform.findField('shopPrice').setValue(0);
                                                upform.findField('buyPrice').setValue(0);
                                                upform.findField('standardPrice').setReadOnly(true);
                                                upform.findField('shopPrice').setReadOnly(true);
                                                upform.findField('buyPrice').setReadOnly(true);
                                            } else {
                                                upform.findField('standardPrice').setValue(record.get('standardPrice'));
                                                upform.findField('shopPrice').setValue(record.get('shopPrice'));
                                                upform.findField('buyPrice').setValue(record.get('buyPrice'));
                                                upform.findField('standardPrice').setReadOnly(false);
                                                upform.findField('shopPrice').setReadOnly(false);
                                                upform.findField('buyPrice').setReadOnly(false);
                                            }
                                        }
                                    }
                                },
                                { name: 'prodName', fieldLabel: '商品名称', value: record.get("prodName")},
                                {   name: 'brandId',
                                    id: "brandId",
                                    fieldLabel: '商品品牌',
                                    xtype: 'combo',
                                    queryMode: "remote",
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    displayField: "name",
                                    valueField: 'id',
                                    blankText: '请选择',
                                    store: brandListStore
                                },
                                { name: 'prodNo', fieldLabel: '商品编号', value: record.get("prodNo")},
                                { name: 'prodCode', fieldLabel: '商品条形码', value: record.get("prodCode") },
                                { name: 'cid',
                                    id: "cid",
                                    xtype: 'combo',
                                    queryMode: "remote",
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    displayField: "name",
                                    fieldLabel: '产品类型',
                                    value: 'name',
                                    valueField: 'id',
                                    blankText: '请选择',
                                    store: brandCategoryListStore
                                },
                                { name: 'description', fieldLabel: '商品描述', xtype: 'textareafield', height: 50, value: record.get("description")},
                                { name: 'standardPrice', fieldLabel: '销售价(元)', value: record.get("standardPrice")},
                                { name: 'shopPrice', fieldLabel: '市场价(元)', value: record.get("shopPrice")},
                                { name: 'buyPrice', fieldLabel: '进货价(元)', value: record.get("buyPrice")},
                                { name: 'color', fieldLabel: '颜色', value: record.get("color")},
                                { name: 'weight', fieldLabel: '重量(公斤)', value: record.get("weight")},
                                { name: 'boxSize', fieldLabel: '尺寸(厘米)', value: record.get("boxSize")},
                                { name: 'speci', fieldLabel: '规格', value: record.get("speci")}
                            ]


                        });

                        if (record.get("type") == "商品") {
                            Ext.getCmp("type").setValue("PRODUCT");
                        } else {
                            Ext.getCmp("type").setValue("GIFT");
                        }


                        brandCategoryListStore.each(function (recod) {
                            if (recod.get("name") == record.get("prodCategoryName")) {
                                Ext.getCmp('cid').setValue(recod.get("id"));
                            }

                        });

                        brandListStore.each(function (recod) {
                            if (recod.get("name") == record.get("brandName")) {
                                Ext.getCmp('brandId').setValue(recod.get("id"));
                            }
                        });


                        var updateGoodListWin = Ext.create("Ext.window.Window", {
                            title: '修改商品',
                            width: 440,
                            height: 600,
                            modal: true,
                            autoHeight: true,
                            layout: 'fit',
                            buttonAlign: 'center',
                            bodyStyle: 'padding:5px;',
                            items: updateGoodsForm,
                            buttons: [
                                {
                                    text: "保存商品",
                                    handler: function () {
                                        var form = updateGoodsForm.getForm();
                                        var data = form.getValues();
                                        if (form.isValid()) {
                                            form.submit({
                                                url: "http://172.16.1.161:8080/product/update",
                                                params: data,
                                                waitMsg: "正在导入验证数据",
                                                success: function (fp, o) {
                                                    Ext.Msg.alert("提示", Ext.JSON.decode(o.response.responseText).msg, function () {
                                                        updateGoodListWin.close();
                                                        refreshgoodsGrid();
                                                    });
                                                }
                                            });
                                        }
                                    }

                                }
                            ]
                        });
                        updateGoodListWin.show();
                    }
                }
            }
        );


        this.items = [goodsListGrid];
        this.callParent(arguments);
    }

});
