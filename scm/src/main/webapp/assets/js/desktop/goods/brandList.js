/**
 * 品牌列表
 * Created by Lein.xu
 */

//定义品牌列表数据模型
Ext.define('EBDesktop.brandListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});
//定义品牌列表类
Ext.define('EBDesktop.goods.brandList', {
    extend: 'Ext.container.Container',
    alias: 'widget.brandList',
    id: 'brandList',
    title: "品牌管理",
    fixed: true,
    layout: 'fit',
    initComponent: function () {

        // 移除用户
        function removeBrandList() {
            var grid = Ext.getCmp('brandListGrid'),
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

                Ext.Msg.confirm("提示", "您确定要删除所选的品牌吗？", function (btnId) {
                    if (btnId == "yes") {
                        var ids = [];
                        Ext.Array.each(records, function (record) {
                            ids.push(record.get("id"));
                        });

                        Ext.Ajax.request({
                            url: "/brand/delete",
                            method: "get",
                            params: {
                                idArray: ids.join(",")
                            },
                            success: function (response, options) {
                                Ext.Msg.alert("提示", Ext.JSON.decode(response.responseText).msg);
                                Ext.getCmp('brandListGrid').getStore().load();
                            },
                            failure: function (response, options) {
                                Ext.Msg.alert("提示", Ext.JSON.decode(response.responseText).msg);
                            }
                        });
                    }
                });

            }

        }

        // 刷新brandListGrid
        function refreshBrandGrid() {
            Ext.getCmp('brandListGrid').getStore().load();
        }

        // 用户数据集
        var brandListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.brandListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/brand/list'

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


        //创建角色数据表格
        var brandListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'brandListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: brandListStore,
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
                        header: '品牌名称',
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
                        text: '添加品牌',
                        iconCls: 'icon-add',
                        handler: function () {//添加用户


                            var formItems = [];
                            formItems.push({
                                    fieldLabel: '品牌名称',
                                    name: 'name'

                                }
                            );
                            //创建用户添加表单
                            var brandAddForm = Ext.create('Ext.form.Panel', {
                                baseCls: 'x-plain',
                                labelWidth: 80,
                                defaults: {
                                    width: 380
                                },
                                id: 'brandAdd',
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
                                title: '添加品牌',
                                width: 500,
                                height: 150,
                                modal: true,
                                autoHeight: true,
                                layout: 'fit',
                                plain: true,
                                buttonAlign: 'center',
                                bodyStyle: 'padding:5px;',
                                items: brandAddForm,
                                buttons: [
                                    {
                                        text: '确认添加',
                                        itemId: 'addBtn',
                                        handler: function () {
                                            var addForm = brandAddForm.getForm();
                                            datas = addForm.getValues();

                                            if (addForm.isValid()) {

                                                addForm.submit({
                                                    clientValidation: true, //对客户端进行验证
                                                    url: "/brand/add",
                                                    method: "post",
                                                    params: {name: datas["name"]},
                                                    waitMsg: "添加成功...",
                                                    success: function (form, action) {

                                                        addWin.close();
                                                        Ext.Msg.alert("表单提交成功", Ext.JSON.decode(action.response.responseText).msg);
                                                        Ext.getCmp('brandListGrid').getStore().load();

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
                                            accountAddForm.getForm().reset();
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
                     handler: removeBrandList
                     }*/,
                    {
                        xtype: 'button',
                        text: '刷新',
                        iconCls: 'icon-refresh',
                        handler: refreshBrandGrid
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: brandListStore,
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
                        var brandUpdateForm = Ext.create('Ext.form.Panel', {
                            baseCls: 'x-plain',
                            labelWidth: 80,
                            id: 'accountUpdateForm',
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
                            title: "修改品牌",
                            width: 400,
                            height: 150,
                            items: brandUpdateForm,
                            buttonAlign: "center",
                            modal: true,
                            buttons: [
                                {
                                    text: '确认修改',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        if (brandUpdateForm.isValid()) {
                                            brandUpdateForm.getForm().submit({
                                                url: "/brand/update",
                                                params: {id: record.get("id"), name: record.get('name')},
                                                waitMsg: "保存成功...",
                                                success: function (form, action) {
                                                    winUpdate.close();
                                                    Ext.Msg.alert("提示", Ext.JSON.decode(action.response.responseText).msg);
                                                    Ext.getCmp('brandListGrid').getStore().load();
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
                                        brandUpdateForm.getForm().reset();
                                    }
                                }
                            ]
                        });
                        //更新窗口
                        winUpdate.show();
                    }
                }
            }
        );


        this.items = [brandListGrid];
        this.callParent(arguments);
    }

});
