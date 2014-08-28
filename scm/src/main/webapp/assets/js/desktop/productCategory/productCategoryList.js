/**
 * 产品分类列表
 * Created by Lein.xu
 */

//定义品牌列表数据模型

Ext.define('EBDesktop.productCategoryModel', {
    extend: 'Ext.data.Model',
    fields: [
        "id",
        'name',
        //'code'
    ],
    idProperty: "id"
});
//定义品牌列表容器
Ext.define('EBDesktop.productCategory.productCategoryList', {
    extend: 'Ext.container.Container',
    alias: 'widget.productCategoryList',
    id: 'productCategoryList',
    title: "产品分类管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {
        // 产品分类数据集


        var productCategoryListStore = Ext.create('Ext.data.TreeStore', {
            model: 'EBDesktop.productCategoryModel',
            proxy: {
                type: 'ajax',
                url: 'productCategory/list',
                extractResponseData: function (response) {
                    var json = Ext.JSON.decode(response.responseText).data.obj;
                    return json;

                }
            },
            root: {
                id: "0",
                name: '全部分类'
            }
        });



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
                            }, 300, this);
                        }
                    }
                });
                this.treeObj = new Ext.tree.Panel({
                    border: true,
                    id: 'technicalTreePanel',
                    height: 250,
                    width: 400,
                    split: true,
                    autoScroll: true,
                    tbar: new Ext.Toolbar({
                        style: 'border-top:0px;border-left:0px',
                        items: [
                            {
                                iconCls: 'expand',
                                text: '展开',
                                handler: function () {
                                    this.treeObj.getRootNode().expand(true);
                                },
                                scope: this
                            },
                            '-',
                            {
                                iconCls: 'collapse',
                                text: '折叠',
                                handler: function () {
                                    this.treeObj.getRootNode().collapse(true);
                                },
                                scope: this
                            }
                        ]
                    }),
                    root: {
                        id: '0',
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
        
        
        
        var categoryGrid = Ext.create("Ext.tree.Panel", {
            region: 'center',
            id: 'categoryGrid',
            store: productCategoryListStore,
            useArrows: true,
            tbar:[
                {
                    xtype: 'button',
                    name: 'submuit',
                    text: '导入产品分类',
                    iconCls: 'icon-import',
                    handler: function (btn) {

                        var uploadForm = Ext.create("Ext.form.Panel", {
                            baseCls: 'x-plain',
                            labelWidth: 80,
                            defaults: {
                                width: 380
                            },
                            id: 'uploadForm',
                            border: false,
                            layout: {
                                type: 'hbox',
                                align: 'center'
                            },
                            header: false,
                            frame: false,
                            bodyPadding: '20',
                            items: [
                                {
                                    xtype: "filefield",
                                    name: "uploadFile",
                                    fieldLabel: "导入文件",
                                    labelWidth: 80,
                                    width: 300,
                                    anchor: "100%",
                                    id: "uploadFile",
                                    allowBlank: false,
                                    blankText: 'Excel文件不能为空',
                                    buttonText: "选择文件",
                                    msgTarget: 'under',
                                    validator: function (value) {
                                        var arr = value.split(".");
                                        if (!/xls|xlsx/.test(arr[arr.length - 1])) {
                                            return "文件不合法";
                                        } else {
                                            return true;
                                        }
                                    }

                                },
//                                {
//                                    xtype: 'container',
//                                    layout: {
//                                        type: 'hbox',
//                                        pack: 'left'
//                                    },
//                                    items: [
//                                        {
//                                            xtype: 'button',
//                                            cls: 'contactBtn',
//                                            margin: "0 0 0 20",
//                                            text: '下载模板',
//                                            listeners: {
//                                                'click': function () {
//                                                    location.href = "/static/templet/brandExcelModel.xls";
//                                                }
//                                            }
//                                        }
//                                    ]}
                            ]


                        });


                        var includeWin = Ext.create("Ext.window.Window", {
                            title: '导入产品分类',
                            width: 500,
                            height: 150,
                            modal: true,
                            animateTarget: Ext.getBody(),
                            autoHeight: true,
                            layout: 'fit',
                            buttonAlign: 'right',
                            bodyStyle: 'padding:5px;',
                            items: uploadForm,
                            buttons: [
                                {
                                    text: "确认导入",
                                    handler: function () {
                                        var form = uploadForm.getForm();
                                        if (form.isValid()) {
                                            form.submit({
                                                url: "product_category/upload_excel",
                                                waitMsg: "正在导入验证数据",
                                                success: function (fp, o) {
                                                    var data = Ext.JSON.decode(o.response.responseText);
                                                    if (data.success) {
                                                        Espide.Common.tipMsgIsCloseWindow(data,includeWin,'categoryGrid',true);
                                                    }
                                                },
                                                failure: function (fp, o) {

                                                    var data = Ext.JSON.decode(o.response.responseText);
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

                        includeWin.show();
                    }
                }
            ],
            columns: [
                {
                    xtype: 'treecolumn',
                    text: '产品分类名称',
                    flex: 2,
                    align:'left',
                    dataIndex: 'name'
                }

            ],
            listeners: {
                "itemcontextmenu": function (view, record, item, index, e, eOpts) {
                    //禁用浏览器的右键相应事件
                    e.preventDefault();
                    e.stopEvent();
                    var myContextMenu = new Ext.menu.Menu({
                        shadow: 'frame',
                        float: true,
                        items: [
                            {
                                iconCls: "button-add",
                                text: "添加",
                                scope: this,
                                handler: function () {
                                    myContextMenu.hide();
                                    var formItems = [];


                                    formItems.push({
                                            fieldLabel: '产品分类名称',
                                            name: 'name',
                                            allowBlank: false,
                                            blankText: '产品分类名称不能为空',
                                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                            regexText: '首尾不得包含空格'
                                        }
                                    );



                                    //创建产品分类更新表格
                                    var departAddForm = Ext.create('Ext.form.Panel', {
                                        id: 'departAddForm',
                                        autoScroll: true,
                                        forceFit: true,
                                        border: false,
                                        layout: 'form',
                                        header: false,
                                        frame: false,
                                        width: 300,
                                        bodyPadding: '5 5 0',
                                        requires: ['Ext.form.field.Text'],
                                        fieldDefaults: {
                                            msgTarget: 'side',
                                            labelWidth: 90
                                        },
                                        defaultType: 'textfield',
                                        items: formItems
                                    });

                                    var departUpdateWin = Ext.create("Ext.window.Window", {
                                        title: "添加产品分类",
                                        width: 330,
                                        height: 150,
                                        items: departAddForm,
                                        buttonAlign: "right",
                                        modal: true,
                                        autoScroll: true,
                                        buttons: [
                                            {
                                                text: '保存',
                                                itemId: 'addBtn',
                                                handler: function () {
                                                    var ids = departAddForm.getForm().getValues();
                                                    departAddForm.getForm().submit({
                                                        method: 'post',
                                                        clientValidation: true, //对客户端进行验证
                                                        url: "productCategory/save",
                                                        params: {
                                                            name: ids["name"],
                                                            parentId: record.get('id'),

                                                        },
                                                        success: function (form, action) {
                                                            var data = Ext.JSON.decode(action.response.responseText);
                                                            if (data.success) {
                                                                Espide.Common.tipMsg('保存成功', data.msg);
                                                                departUpdateWin.close();
                                                                record.appendChild({
                                                                    name: data.data.obj.name,
                                                                    code: ids["code"],
                                                                    id: data.data.obj.id,
                                                                    draggable: false,
                                                                    leaf: true,
                                                                    expanded: true
                                                                });
                                                            }
                                                        },
                                                        failure: function (form, action) {

                                                            var data = Ext.JSON.decode(action.response.responseText);
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
                                        ]
                                    });

                                    departUpdateWin.show();


                                }
                            },
                            {
                                iconCls: "button-edit",
                                text: "编辑",
                                handler: function (node) {
                                    myContextMenu.hide();
                                    var pNode = record.parentNode;
                                    var formItems = [];
                                    formItems.push({
                                        xtype: 'hidden',
                                        name: 'code',
                                        value: record.get('code')
                                    });

                                    formItems.push({
                                        xtype: 'hidden',
                                        name: 'id',
                                        value: record.get('id')
                                    });
                                    formItems.push({
                                        xtype: 'hidden',
                                        name: 'parentId',
                                        id: 'parentId',
                                        value: pNode.data.id
                                    });



                                    formItems.push({
                                            fieldLabel: '产品分类名称',
                                            name: 'name',
                                            value: record.get('name'),
                                            allowBlank: false,
                                            blankText: '产品分类名称不能为空',
                                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                            regexText: '首尾不得包含空格'
                                        }
                                    );

//                                    formItems.push(
//                                        new dataAddOrg.TreeComboBox({
//                                            fieldLabel: '产品分类',
//                                            //name: 'parentId',
//                                            anchor: '95%'
//                                        }).setValue(pNode.data.name)
//                                    );


//                                    var idPath = record.getPath('id');


                                    //创建产品分类更新表格
                                    var departUpdateForm = Ext.create('Ext.form.Panel', {
                                        id: 'departUpdateForm',
                                        autoScroll: true,
                                        forceFit: true,
                                        border: false,
                                        layout: 'form',
                                        header: false,
                                        frame: false,
                                        width: 300,
                                        bodyPadding: '5 5 0',
                                        requires: ['Ext.form.field.Text'],
                                        fieldDefaults: {
                                            msgTarget: 'side',
                                            labelWidth: 90
                                        },
                                        defaultType: 'textfield',
                                        items: formItems
                                    });

                                    var departUpdateWin = Ext.create("Ext.window.Window", {
                                        title: "修改产品分类",
                                        width: 330,
                                        height: 150,
                                        items: departUpdateForm,
                                        buttonAlign: "right",
                                        modal: true,
                                        autoScroll: true,
                                        buttons: [
                                            {
                                                text: '保存',
                                                itemId: 'addBtn',
                                                handler: function () {
                                                    var ids = departUpdateForm.getForm().getValues();
                                                    departUpdateForm.getForm().submit({
                                                        method: 'post',
                                                        clientValidation: true, //对客户端进行验证
                                                        url: "productCategory/update",
                                                        params: {
                                                            id: ids['id'],
                                                            code: ids['code'],
                                                            name: ids["name"]
                                                            //parentId: ids['parentId']
                                                        },
                                                        success: function (form, action) {
                                                            var data = Ext.JSON.decode(action.response.responseText);
                                                            if (data.success) {
                                                                Espide.Common.tipMsg('保存成功', data.msg);
                                                                departUpdateWin.close();
                                                                //刷新列表
                                                                Ext.getCmp('categoryGrid').getStore().load({
                                                                    node: Ext.getCmp('categoryGrid').getRootNode(),
                                                                    callback: function(){
                                                                        Ext.getCmp('categoryGrid').expandPath(idPath,ids['id']);
                                                                    }
                                                                })
                                                                record.set('name', ids['name']);
                                                                record.set('code', ids['code']);
                                                            }

                                                        },
                                                        failure: function (form, action) {

                                                            var data = Ext.JSON.decode(action.response.responseText);
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
                                        ]
                                    });

                                    departUpdateWin.show();
                                }
                            },
                            {
                                iconCls: "button-delete",
                                text: "删除",
                                handler: function () {
                                    myContextMenu.hide();
                                    if (record.get('id') == 0) {
                                        Ext.MessageBox.show({
                                            title: '提示',
                                            msg: '根目录不难删除',
                                            buttons: Ext.MessageBox.OK,
                                            icon: 'x-message-box-warning'
                                        });

                                        return;
                                    }
                                    Ext.MessageBox.confirm("确认删除", "是否要删除指定内容？", function (button, text) {
                                        if (button == "yes") {
                                            Ext.Ajax.request({
                                                url: "productCategory/delete",//请求的地址
                                                method: "post",
                                                params: {
                                                    id: record.get('id')
                                                }, //发送的参数
                                                success: function (response, option) {
                                                    data = Ext.JSON.decode(response.responseText);
                                                    if (data.success) {
                                                        Espide.Common.tipMsg('删除成功', data.msg);

                                                        var pNode = record.parentNode;
                                                        record.remove();
                                                    }

                                                },
                                                failure: function (response, option) {
                                                    data = Ext.JSON.decode(response.responseText);
                                                    Ext.MessageBox.show({
                                                        title: '提示',
                                                        msg: data.msg,
                                                        buttons: Ext.MessageBox.OK,
                                                        icon: 'x-message-box-warning'
                                                    });
                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        ]
                    });
//
//
                    myContextMenu.showAt(e.getXY());
                }
            }
        });


        this.items = [categoryGrid];
        this.callParent(arguments);
    }

});
