/**
 * 产品分类列表
 * Created by Lein.xu
 */

//定义品牌列表数据模型
Ext.define('EBDesktop.productCategoryModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});
//定义品牌列表容器
Ext.define('EBDesktop.goods.productCategoryList', {
    extend: 'Ext.container.Container',
    alias: 'widget.productCategoryList',
    id: 'productCategoryList',
    title: "产品分类管理",
    fixed: true,
    layout: 'fit',
    initComponent: function () {

        // 移除产品数据
        function removeProductCategory() {
            var grid = Ext.getCmp('productCategoryListGrid'),
                records = grid.getSelectionModel().getSelection();

            if (records.length < 1) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '请先选择要删除品牌。',
                    model: false,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
            } else {

                Ext.Msg.confirm("提示", "您确定要删除所选的品牌吗？", function (optional) {
                    if (optional == "yes") {
                        var ids = [];
                        Ext.Array.each(records, function (record) {
                            ids.push(record.get("id"));
                        });

                        Ext.Ajax.request({
                            url: "/prodCategory/delete",
                            method: "get",
                            params: {
                                idArray: ids.join(",")
                            },
                            success: function (response, options) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    Ext.Msg.alert("提示", Ext.JSON.decode(response.responseText).msg);
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: '删除成功',
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-info'
                                    });
                                    Ext.getCmp('productCategoryListGrid').getStore().load();
                                } else {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: data.msg,
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-error'
                                    });
                                }
                            },
                            failure: function (response, options) {
                                Ext.Msg.alert("提示", Ext.JSON.decode(response.responseText).msg);
                            }
                        });
                    }
                });

            }

        }

        // 刷新productCategoryListGrid
        function refreshProductGrid() {
            Ext.getCmp('productCategoryListGrid').getStore().load();
        }


        // 产品分类数据集
        var productCategoryListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.productCategoryModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/prodCategory/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'message',
                    totalProperty: 'data.obj.totalCount'
                }
            },
            autoSync: true,
            autoLoad: {start: 0, limit: 20},
            pageSize: 20
        });


        //创建角色数据表格容器
        var productCategoryListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'productCategoryListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: productCategoryListStore,
                columns: [
                    {
                        header: 'ID',
                        dataIndex: 'id',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }

                    },
                    {
                        header: '品牌分类',
                        dataIndex: 'name',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    }

                ],
                tbar: [
                    {
                        xtype: 'button',
                        text: '添加产品分类',
                        iconCls: 'icon-add',
                        handler: function () {//添加用户
                            var formItems = [];
                            formItems.push({
                                    fieldLabel: '产品分类',
                                    name: 'name'

                                }
                            );

                            //创建用户添加表单
                            var productAddForm = Ext.create('Ext.form.Panel', {
                                baseCls: 'x-plain',
                                labelWidth: 80,
                                defaults: {
                                    width: 380
                                },
                                id: 'productAddForm',
                                forceFit: true,
                                border: false,
                                layout: 'form',
                                header: false,
                                frame: false,
                                bodyPadding: '5 5 0',
                                requires: ['Ext.form.field.Text'],
                                fieldDefaults: {
                                    msgTarget: 'qtip',
                                    blankText: '不能为空',
                                    allowBlank: false,
                                    labelAlign: "left",
                                    labelSeparator: "：",
                                    labelWidth: 75
                                },
                                defaultType: 'textfield',
                                items: formItems

                            });
                            //创建一个弹窗容器，把表单面板放进弹窗容器
                            var addWin = Ext.create("Ext.window.Window", {
                                title: '添加品牌分类',
                                width: 500,
                                height: 150,
                                modal: true,
                                autoHeight: true,
                                layout: 'fit',
                                plain: true,
                                buttonAlign: 'center',
                                bodyStyle: 'padding:5px;',
                                items: productAddForm,
                                buttons: [
                                    {
                                        text: '确认添加',
                                        itemId: 'addBtn',
                                        handler: function () {
                                            var addForm = productAddForm.getForm();
                                            datas = addForm.getValues();

                                            if (addForm.isValid()) {

                                                addForm.submit({
                                                    clientValidation: true, //对客户端进行验证
                                                    url: "/prodCategory/add",
                                                    method: "post",
                                                    params: {name: datas["name"]},
                                                    waitMsg: "添加成功...",
                                                    success: function (form, action) {

                                                        addWin.close();
                                                        Ext.Msg.alert("表单提交成功", Ext.JSON.decode(action.response.responseText).msg);
                                                        Ext.getCmp('productCategoryListGrid').getStore().load();

                                                    },
                                                    failure: function (form, action) {
                                                        Ext.Msg.alert("表单提交成功", Ext.JSON.decode(action.response.responseText).msg);
                                                    }
                                                });

                                            }
                                        }


                                    },
                                    {
                                        text: '重置',
                                        itemId: 'resetBtn',
                                        handler: function () {
                                            productAddForm.getForm().reset();
                                        }
                                    }
                                ]
                            });

                            //显示弹窗
                            addWin.show();


                        }}/*,
                     {
                     xtype: 'button',
                     text: '删除',
                     iconCls: 'icon-remove',
                     handler: removeProductCategory
                     }*/,
                    {
                        xtype: 'button',
                        text: '刷新',
                        iconCls: 'icon-refresh',
                        handler: refreshProductGrid
                    }

                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: productCategoryListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件

                        var formItems = [];
                        formItems.push({
                                fieldLabel: '商品名称',
                                name: 'name',
                                value: record.get('name')
                            }
                        );

                        var productCategoryUpdateForm = Ext.create('Ext.form.Panel', {
                            baseCls: 'x-plain',
                            labelWidth: 80,
                            id: 'productCategoryUpdateForm',
                            forceFit: true,
                            border: false,
                            layout: 'form',
                            header: false,
                            frame: false,
                            bodyPadding: '5 5 0',
                            requires: ['Ext.form.field.Text'],
                            fieldDefaults: {
                                blankText: '不能为空',
                                allowBlank: false,
                                msgTarget: 'side',
                                labelWidth: 75
                            },
                            defaultType: 'textfield',
                            items: formItems

                        });


                        var winUpdate = Ext.create("Ext.window.Window", {
                            title: "修改产品分类",
                            width: 400,
                            height: 150,
                            items: productCategoryUpdateForm,
                            buttonAlign: "center",
                            modal: true,
                            buttons: [
                                {
                                    text: '确认修改',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        if (productCategoryUpdateForm.isValid()) {
                                            productCategoryUpdateForm.getForm().submit({
                                                url: "/prodCategory/update",
                                                params: {id: record.get("id"), name: record.get('name')},
                                                waitMsg: "保存成功...",
                                                success: function (form, action) {
                                                    winUpdate.close();
                                                    Ext.Msg.alert("提示", Ext.JSON.decode(action.response.responseText).msg);
                                                    Ext.getCmp('productCategoryListGrid').getStore().load();
                                                },
                                                failure: function (form, action) {
                                                    Ext.Msg.alert("提示", Ext.JSON.decode(action.response.responseText).msg);
                                                }
                                            });
                                        }

                                    }
                                },
                                {
                                    text: '重置',
                                    itemId: 'resetBtn',
                                    handler: function () {
                                        productCategoryUpdateForm.getForm().reset();
                                    }
                                }
                            ]
                        });
                        //更新窗口
                        winUpdate.show();

                    },
                    failure: function (response, options) {

                    }

                }
            }
        );


        this.items = [productCategoryListGrid];
        this.callParent(arguments);
    }

});
