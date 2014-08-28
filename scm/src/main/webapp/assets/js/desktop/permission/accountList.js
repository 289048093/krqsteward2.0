/**
 * 用户列表
 * Created by Lein.xu
 */

//定义用户列表数据模型
Ext.define('EBDesktop.AccountListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'username', 'email', 'telephone'],
    idProperty: 'id'
});

//仓库模型
Ext.define('EBDesktop.WarehouseList', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});

//定义用户列表类
Ext.define('EBDesktop.permission.accountList', {
    extend: 'Ext.container.Container',
    alias: 'widget.accountList',
    id: 'accountList',
    title: "用户管理",
    layout: "fit",
    fixed: true,
    initComponent: function () {
        //获取组件
        function getGridSelected(id) {
            return Ext.getCmp(id).getSelectionModel().getSelection();
        }

        //获取属性
        function getJsonAttr(obj, attr) {
            var arr = [];
            Ext.each(obj, function (sel) {
                sel && arr.push(sel.get(attr));
            });
            return arr;
        }

        // 移除用户
        function removeAccountList() {
            var records = getGridSelected('accountGrid'),
                ids = getJsonAttr(records, 'id');

            if (records.length < 1) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '请先选择要删除用户。',
                    model: false,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
            } else {
                Ext.Msg.confirm("提示", "您确定要删除所选的用户吗？", function (optional) {
                    if (optional == "yes") {
                        Ext.Ajax.request({
                            url: "/user/deleteMore",
                            method: "get",
                            params: {
                                idArray: ids.join(",")
                            },
                            success: function (response, options) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: '删除成功',
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-info'
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: data.msg,
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-error'
                                    });
                                }

                                Ext.getCmp('accountGrid').getStore().load();
                            },
                            failure: function (response, options) {
                                Ext.MessageBox.show({
                                    title: '提示',
                                    msg: '服务器错误',
                                    modal: false,
                                    buttons: Ext.MessageBox.OK,
                                    icon: 'x-message-box-error'
                                });
                            }
                        });
                    }
                });

            }

        }

        // 刷新accountGrid
        function refreshAccountGrid() {
            Ext.getCmp('accountGrid').getStore().load();
        }

        // 用户数据源
        var accountListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.AccountListModel',
            proxy: {
                type: 'ajax',
                api: {
                    //read: 'assets/js/desktop/permission/accountList.json'
                    read: "/user/list"
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'message'
                }
            },
            autoSync: true,
            autoLoad: true
        });


        //仓库数据集
        var warehouseStore = Ext.create('Ext.data.Store', {
            singleton: true,
            extend: 'Ext.data.Store',
            model: 'EBDesktop.WarehouseList',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: '/repository/findAll'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'message'
                }
            },
            autoLoad: false
        });

        warehouseStore.load({
            callback: function () {
                warehouseStore.insert(0, { name: '为空', id: 0 });
            }
        });

        //创建角色数据表格容器
        var accountGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'accountGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: accountListStore,
                columns: [
                    {
                        header: '用户ID',
                        dataIndex: 'id',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }

                    },
                    {
                        header: '用户名称',
                        dataIndex: 'username',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {
                        header: '电子邮箱',
                        dataIndex: 'email',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {
                        header: '电话号码',
                        dataIndex: 'telephone',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    }

                ],
                tbar: [
                    {
                        xtype: 'button',
                        text: '添加用户',
                        iconCls: 'icon-add',
                        handler: function () {//添加用户
                            //新建一个空数组 将查询的角色列表 封装成checkbox item 对象成为空数组的元素
                            var roleList;
                            var roleCheckGroup = [];
                            Ext.Ajax.request({
                                url: '/user/add',
                                success: function (response, options) { //成功返回回调函数
                                    var data = Ext.JSON.decode(response.responseText);
                                    //假如请求成功
                                    if (data.success) {
                                        roleList = Ext.JSON.decode(response.responseText).data.list;
                                        Ext.each(roleList, function (roleList) {
                                            roleCheckGroup.push({boxLabel: roleList.name, name: 'roleId', inputValue: roleList.id, checked: roleList.ur});

                                        });
                                        //添加用户表单项
                                        var formItems = [];
                                        formItems.push({
                                                fieldLabel: '用户名称',
                                                name: 'username'

                                            }
                                        );
                                        formItems.push({
                                                fieldLabel: '用户密码',
                                                name: 'password',
                                                inputType: "password"
                                            }
                                        );

                                        formItems.push({
                                                fieldLabel: '电子邮箱',
                                                name: 'email',
                                                vtype: 'email',
                                                vtypeText: '不是有效的邮箱地址'
                                            }
                                        );
                                        formItems.push({
                                                fieldLabel: '电话号码',
                                                name: 'telephone',
                                                vtype: 'Mobile'
                                            }
                                        );

                                        formItems.push(
                                            { name: 'repoId',
                                                id: 'repoId',
                                                xtype: 'combo',
                                                queryMode: "local",
                                                triggerAction: 'all',
                                                forceSelection: true,
                                                editable: false,
                                                displayField: "name",
                                                fieldLabel: '产品仓库',
                                                value: 'name',
                                                valueField: 'id',
                                                blankText: '请选择',
                                                allowBlank: true,
                                                store: warehouseStore
                                            }
                                        );

                                        formItems.push({
                                            xtype: 'checkboxgroup',
                                            fieldLabel: '分配角色',
                                            columns: 3,
                                            items: roleCheckGroup
                                        });



                                        //创建用户添加表单
                                        var accountAddForm = Ext.create('Ext.form.Panel', {
                                            baseCls: 'x-plain',
                                            labelWidth: 80,
                                            defaults: {
                                                width: 380
                                            },
                                            id: 'accountAddForm',
                                            forceFit: true,
                                            border: false,
                                            layout: 'form',
                                            header: false,
                                            frame: false,
                                            bodyPadding: '5 5 0',
                                            width:500,
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

                                        //设置下拉列表值
                                        Ext.getCmp('repoId').setValue('为空');
                                        //创建一个弹窗容器
                                        var accountAddWin = Ext.create("Ext.window.Window", {
                                            title: '添加用户',
                                            width: 550,
                                            height: 400,
                                            modal: true,
                                            autoScroll: true,
                                            plain: true,
                                            buttonAlign: 'center',
                                            bodyStyle: 'padding:5px;',
                                            items: accountAddForm,
                                            buttons: [
                                                {
                                                    text: '确认添加',
                                                    itemId: 'addBtn',
                                                    handler: function () {
                                                        var addForm = accountAddForm.getForm();
                                                        datas = addForm.getValues();

                                                        if (addForm.isValid()) {//判断表单是否验证

                                                            if (datas["roleId"].length > 1) {
                                                                Ext.Msg.alert("提示", "用户只能对应一个角色");
                                                            } else {
                                                                addForm.submit({
                                                                    clientValidation: true, //对客户端进行验证
                                                                    url: "/user/save",
                                                                    method: "post",
                                                                    params: {
                                                                        username: datas["username"],
                                                                        password: datas["password"],
                                                                        email: datas["email"],
                                                                        telephone: datas["telephone"],
                                                                        roleId: datas["roleId"]
                                                                    },
                                                                    success: function (form, action) {

                                                                        var data = Ext.JSON.decode(action.response.responseText);
                                                                        if (data.success) {
                                                                            Ext.MessageBox.show({
                                                                                title: '提示',
                                                                                msg: '保存成功！',
                                                                                buttons: Ext.MessageBox.OK,
                                                                                icon: 'x-message-box-info',
                                                                                fn: function () {
                                                                                    accountAddWin.close();
                                                                                    Ext.getCmp('accountGrid').getStore().load();
                                                                                }
                                                                            });
                                                                        }

                                                                    },
                                                                    failure: function (form, action) {
                                                                        try {
                                                                            var data = Ext.JSON.decode(action.response.responseText);
                                                                            Ext.MessageBox.show({
                                                                                title: '提示',
                                                                                msg: data.msg,
                                                                                buttons: Ext.MessageBox.OK,
                                                                                icon: 'x-message-box-warning'
                                                                            });
                                                                        } catch (e) {
                                                                            Ext.MessageBox.show({
                                                                                title: '提示',
                                                                                msg: '服务器错误',
                                                                                buttons: Ext.MessageBox.OK,
                                                                                icon: 'x-message-box-error'
                                                                            });
                                                                            console.log(e);
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }

                                                    }
                                                },
                                                {
                                                    text: '关闭窗口',
                                                    itemId: 'closeBtn',
                                                    handler: function () {
                                                        accountAddForm.close();
                                                    }
                                                }
                                            ]
                                        });

                                        //显示弹窗
                                        accountAddWin.show();
                                    }
                                },
                                failure: function () {
                                    try {
                                        var data = Ext.JSON.decode(action.response.responseText);
                                        Ext.MessageBox.show({
                                            title: '提示',
                                            msg: data.msg,
                                            buttons: Ext.MessageBox.OK,
                                            icon: 'x-message-box-warning'
                                        });
                                    } catch (e) {
                                        Ext.MessageBox.show({
                                            title: '提示',
                                            msg: '服务器错误',
                                            buttons: Ext.MessageBox.OK,
                                            icon: 'x-message-box-error'
                                        });
                                        console.log(e);
                                    }
                                }});
                        }
                    },
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-remove',
                        handler: removeAccountList
                    },
                    {
                        xtype: 'button',
                        text: '刷新',
                        iconCls: 'icon-refresh',
                        handler: refreshAccountGrid
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: accountListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {//单元格绑定双击事件   修改数据
                    'itemdblclick': function (view, record, item, index, event) {

                        var roleList;
                        var roleCheckGroup = [];
                        Ext.Ajax.request({
                            url: "/user/view",
                            method: "get",
                            params: {
                                id: record.get('id')
                            },
                            success: function (response, options) {
                                var data = Ext.JSON.decode(response.responseText);
                                //假如请求成功
                                if (data.success) {
                                    roleList = Ext.JSON.decode(response.responseText).data.list;
                                    Ext.each(roleList, function (roleList) {
                                        roleCheckGroup.push({
                                            boxLabel: roleList.name,
                                            name: 'roleId',
                                            inputValue: roleList.id,
                                            checked: roleList.ur
                                        });

                                    });
                                    //修改表单项
                                    var formItems = [];
                                    formItems.push({
                                            fieldLabel: '用户名称',
                                            name: 'username',
                                            value: record.get('username'),
                                            disabled: true
                                        }
                                    );
                                    formItems.push({
                                            fieldLabel: '电子邮箱',
                                            name: 'email',
                                            value: record.get('email')
                                        }
                                    );
                                    formItems.push({
                                            fieldLabel: '电话号码',
                                            name: 'telephone',
                                            value: record.get('telephone')
                                        }
                                    );
                                    formItems.push(
                                        { name: 'repoId',
                                            id:'repoId',
                                            xtype: 'combo',
                                            queryMode: "local",
                                            triggerAction: 'all',
                                            forceSelection: true,
                                            editable: false,
                                            displayField: "name",
                                            fieldLabel: '产品仓库',
                                            value: 'name',
                                            valueField: 'id',
                                            blankText: '请选择',
                                            emptyText: '为空',
                                            allowBlank: true,
                                            store: warehouseStore
                                        }
                                    );

                                    formItems.push({
                                        xtype: 'checkboxgroup',
                                        fieldLabel: '分配角色',
                                        columns: 3,
                                        items: roleCheckGroup
                                    });



                                    //创建修改表单
                                    var accountUpdateForm = Ext.create('Ext.form.Panel', {
                                        baseCls: 'x-plain',
                                        labelWidth: 80,
                                        id: 'accountUpdateForm',
                                        forceFit: true,
                                        border: false,
                                        header: false,
                                        frame: false,
                                        bodyPadding: '5 5 0',
                                        width:500,
                                        requires: ['Ext.form.field.Text'],
                                        fieldDefaults: {
                                            msgTarget: 'side',
                                            labelWidth: 75
                                        },
                                        defaultType: 'textfield',
                                        items: formItems

                                    });


                                    //设置下拉列表值
                                    if(Ext.JSON.decode(response.responseText).data.repoId == null){
                                        Ext.getCmp('repoId').setValue(0);
                                    }else{
                                        Ext.getCmp('repoId').setValue(Ext.JSON.decode(response.responseText).data.repoId);
                                    }


                                    //修改用户窗口
                                    var accountUpdateWin = Ext.create("Ext.window.Window", {
                                        title: "修改用户资料",
                                        id: "accountUpdateWin",
                                        width: 530,
                                        height: 400,
                                        items: accountUpdateForm,
                                        buttonAlign: "center",
                                        autoScroll: true,
                                        modal: true,
                                        buttons: [
                                            {
                                                text: '确认修改',
                                                itemId: 'addBtn',
                                                handler: function () {
                                                    var ids = accountUpdateForm.getForm().getValues()["roleId"];
                                                    if (ids.length > 1) {
                                                        //现在的场景只能一个用户对应一个角色
                                                        Ext.MessageBox.show({
                                                            title: '提示',
                                                            msg: '用户只能对应一个角色',
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: 'x-message-box-info'
                                                        });
                                                    } else {
                                                        accountUpdateForm.getForm().submit({
                                                            url: "/user/update",
                                                            method: "post",
                                                            params: {
                                                                email: record.get('email'),
                                                                telephone: record.get('telephone'),
                                                                id: record.get('id'),
                                                                roleId: ids
                                                            },
                                                            success: function (form, action) {


                                                                var data = Ext.JSON.decode(action.response.responseText);
                                                                if (data.success) {
                                                                    Ext.MessageBox.show({
                                                                        title: '提示',
                                                                        msg: '修改成功！',
                                                                        buttons: Ext.MessageBox.OK,
                                                                        icon: 'x-message-box-info',
                                                                        fn: function () {
                                                                            accountUpdateWin.close();
                                                                            Ext.getCmp('accountGrid').getStore().load();
                                                                        }
                                                                    });
                                                                }
                                                            },
                                                            failure: function (form, action) {
                                                                try {
                                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                                    Ext.MessageBox.show({
                                                                        title: '提示',
                                                                        msg: data.msg,
                                                                        buttons: Ext.MessageBox.OK,
                                                                        icon: 'x-message-box-warning'
                                                                    });
                                                                } catch (e) {
                                                                    Ext.MessageBox.show({
                                                                        title: '提示',
                                                                        msg: '服务器错误',
                                                                        buttons: Ext.MessageBox.OK,
                                                                        icon: 'x-message-box-error'
                                                                    });
                                                                    console.log(e);
                                                                }
                                                            }
                                                        });
                                                    }


                                                }
                                            },
                                            {
                                                text: '重置',
                                                itemId: 'resetBtn',
                                                handler: function () {
                                                    accountUpdateForm.getForm().reset();
                                                }
                                            }
                                        ]
                                    });
                                    //修改用户窗口
                                    accountUpdateWin.show();

                                }
                            },
                            failure: function (form, action) {
                                try {
                                    var data = Ext.JSON.decode(action.response.responseText);
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: data.msg,
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-warning'
                                    });
                                } catch (e) {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: '服务器错误',
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-error'
                                    });
                                    console.log(e);
                                }
                            }
                        });

                    }
                }
            }
        );


        this.items = [accountGrid];
        this.callParent(arguments);
    }

});
