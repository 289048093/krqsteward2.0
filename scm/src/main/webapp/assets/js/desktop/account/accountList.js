/**
 * 用户列表
 * Created by Lein.xu
 */

//定义用户列表数据模型
Ext.define('EBDesktop.AccountListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'username', 'realName', 'email', 'telephone'],
    idProperty: 'id'
});

//仓库模型
Ext.define('EBDesktop.WarehouseList', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name'],
    idProperty: 'id'
});


//定义用户列表类
Ext.define('EBDesktop.account.accountList', {
    extend: 'Ext.container.Container',
    alias: 'widget.accountList',
    id: 'accountList',
    title: "用户管理",
    layout: "border",
    fixed: true,
    initComponent: function () {
        // 用户数据源
        var accountListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.AccountListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: "/user/list"
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list.result',
                    messageProperty: 'message',
                    totalProperty: 'data.list.totalCount'
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
            autoLoad: {start: 0, limit: 20},
            pageSize: 20
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

        warehouseStore.load({
            callback: function () {
                warehouseStore.insert(0, { name: '为空', id: 0 });
            }
        });

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

            var url = 'user/deleteMore',
                ids = Espide.Common.getGridSels('accountGrid', 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的用户');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        idArray: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('accountGrid').getStore().loadPage(1);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });

        }


        //添加用户
        function accountAdd() {
            //新建一个空数组 将查询的角色列表 封装成checkbox item 对象成为空数组的元素
            var roleList;
            var getRoleId;
            var roleCheckGroup = [];
            Ext.Ajax.request({
                url: '/user/add',
                success: function (response, options) { //成功返回回调函数
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

                            if (roleList.name == '仓库人员') {
                                getRoleId = roleList.id;
                            }

                        });
                        //添加用户表单项
                        var formItems = [];
                        formItems.push({
                                fieldLabel: '用户名称',
                                name: 'username',
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
                            }
                        );
                        formItems.push({
                                fieldLabel: '真实姓名',
                                name: 'realName',
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
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
                                vtypeText: '不是有效的邮箱地址',
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
                            }
                        );
                        formItems.push({
                                fieldLabel: '手机号码',
                                name: 'telephone',
                                vtype: 'Mobile',
                                vtypeText: '不是有效的手机号码',
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
                            }
                        );

                        formItems.push(
                            {
                                name: 'repoId',
                                id: 'repoIdAdd',
                                xtype: 'combo',
                                queryMode: "local",
                                triggerAction: 'all',
                                forceSelection: true,
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
                            xtype: 'radiogroup',
                            id: 'roleGrounp',
                            fieldLabel: '分配角色',
                            columns: 3,
                            items: roleCheckGroup,
                            listeners: {
                                'change': function (obj) {
                                    if (Ext.getCmp('roleGrounp').getValue().roleId == getRoleId) {
                                        Ext.getCmp('repoIdAdd').enable();
                                    } else {
                                        Ext.getCmp('repoIdAdd').setValue(0);
                                    }
                                }
                            }
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
                            width: 500,
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
                        Ext.getCmp('repoIdAdd').setValue(0);
                        //创建一个弹窗容器
                        var accountAddWin = Ext.create("Ext.window.Window", {
                            title: '添加用户',
                            width: 550,
                            height: 400,
                            modal: true,
                            autoScroll: true,
                            plain: true,
                            buttonAlign: 'right',
                            bodyStyle: 'padding:5px;',
                            items: accountAddForm,
                            buttons: [
                                {
                                    text: '保存',
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
                                                        realName: datas["realName"],
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

                                    }
                                },
                                {
                                    text: '重写',
                                    itemId: 'resetBtn',
                                    handler: function () {
                                        accountAddForm.getForm().reset();
                                    }
                                }
                            ]
                        });

                        //显示弹窗
                        accountAddWin.show();
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

                }});
        }

        //用户查询
        function searchAccount(button) {
            Espide.Common.doSearch("accountGrid", "searchAccount", true);
        }

        // 刷新accountGrid
        function refreshAccountGrid() {
            Ext.getCmp('accountGrid').getStore().load();
        }

        //顶栏表单
        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'searchAccount',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'userName',
                    fieldLabel: '用户名称',
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {
                                searchAccount();
                            }
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchAccount
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: accountAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选',
                    itemId: 'deleteBtn',
                    handler: removeAccountList
                }
            ]
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
                        header: '用户名称',
                        dataIndex: 'username',
                        width: 50
                    },
                    {
                        header: '真实姓名',
                        dataIndex: 'realName',
                        width: 50
                    },
                    {
                        header: '电子邮箱',
                        dataIndex: 'email',
                        width: 100

                    },
                    {
                        header: '手机号码',
                        dataIndex: 'telephone'
                    }

                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: accountListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {//单元格绑定双击事件   修改数据
                    'itemdblclick': function (view, record, item, index, event) {

                        var roleList;
                        var getRoleId;
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

                                        if (roleList.name == '仓库人员') {
                                            getRoleId = roleList.id;
                                        }


                                    });
                                    //修改表单项
                                    var formItems = [];
                                    formItems.push({
                                            fieldLabel: '用户名称',
                                            name: 'username',
                                            value: record.get('username'),
                                            disabled: true,
                                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                            regexText: '首尾不得包含空格'
                                        }
                                    );
                                    formItems.push({
                                            fieldLabel: '真实姓名',
                                            name: 'realName',
                                            value: record.get('realName'),
                                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                            regexText: '首尾不得包含空格'
                                        }
                                    );
                                    formItems.push({
                                            fieldLabel: '电子邮箱',
                                            name: 'email',
                                            value: record.get('email'),
                                            vtype: 'email',
                                            vtypeText: '不是有效的邮箱地址',
                                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                            regexText: '首尾不得包含空格'
                                        }
                                    );
                                    formItems.push({
                                            fieldLabel: '手机号码',
                                            name: 'telephone',
                                            value: record.get('telephone'),
                                            vtype: 'Mobile',
                                            vtypeText: '不是有效的手机号码',
                                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                            regexText: '首尾不得包含空格'
                                        }
                                    );

                                    formItems.push({
                                            fieldLabel: '重置密码',
                                            name: 'password',
                                            inputType: "password",
                                            allowBlank: true
                                        }
                                    );
                                    formItems.push({
                                            fieldLabel: '确认密码',
                                            name: 'repassword',
                                            inputType: "password",
                                            allowBlank: true
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
                                            emptyText: '为空',
                                            allowBlank: true,
                                            store: warehouseStore
                                        }
                                    );


                                    formItems.push({
                                        xtype: 'radiogroup',
                                        fieldLabel: '分配角色',
                                        columns: 3,
                                        items: roleCheckGroup,
                                        listeners: {
                                            'change': function (obj) {
                                                if (Ext.getCmp('repoId').getValue().roleId == getRoleId) {
                                                    Ext.getCmp('repoId').enable();
                                                } else {
                                                    Ext.getCmp('repoId').setValue(0);
                                                }
                                            }
                                        }
                                    });

                                    //创建修改表单
                                    var accountUpdateForm = Ext.create('Ext.form.Panel', {
                                        baseCls: 'x-plain',
                                        labelWidth: 80,
                                        defaults: {
                                            width: 380
                                        },
                                        id: 'accountUpdateForm',
                                        forceFit: true,
                                        border: false,
                                        layout: 'form',
                                        header: false,
                                        frame: false,
                                        bodyPadding: '5 5 0',
                                        width: 500,
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
                                    if (Ext.JSON.decode(response.responseText).data.repoId == 0) {
                                        Ext.getCmp('repoId').setValue(0);
                                    } else {
                                        Ext.getCmp('repoId').setValue(Ext.JSON.decode(response.responseText).data.repoId);
                                    }

                                    //修改用户窗口
                                    var accountUpdateWin = Ext.create("Ext.window.Window", {
                                        title: "修改用户资料",
                                        id: "accountUpdateWin",
                                        width: 530,
                                        height: 400,
                                        items: accountUpdateForm,
                                        buttonAlign: "right",
                                        autoScroll: true,
                                        modal: true,
                                        buttons: [
                                            {
                                                text: '保存',
                                                itemId: 'addBtn',
                                                handler: function () {
                                                    var ids = accountUpdateForm.getForm().getValues()["roleId"],
                                                        password = accountUpdateForm.getForm().getValues()["password"],
                                                        repassword = accountUpdateForm.getForm().getValues()["repassword"];
                                                    if (ids.length > 1) {
                                                        //现在的场景只能一个用户对应一个角色
                                                        Ext.MessageBox.show({
                                                            title: '提示',
                                                            msg: '用户只能对应一个角色',
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: 'x-message-box-info'
                                                        });
                                                    } else {
                                                        if (password === repassword) { //密码必须与确认密码相同
                                                            accountUpdateForm.getForm().submit({
                                                                url: "/user/update",
                                                                method: "post",
                                                                params: {
                                                                    email: record.get('email'),
                                                                    realName: record.get('realName'),
                                                                    telephone: record.get('telephone'),
                                                                    id: record.get('id'),
                                                                    roleId: ids,
                                                                    password: password,
                                                                    repassword: repassword
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

                                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                                    Ext.MessageBox.show({
                                                                        title: '提示',
                                                                        msg: data.msg,
                                                                        buttons: Ext.MessageBox.OK,
                                                                        icon: 'x-message-box-warning'
                                                                    });

                                                                }
                                                            });
                                                        } else {
                                                            Ext.MessageBox.show({
                                                                title: '提示',
                                                                msg: '密码必须与确认密码相同',
                                                                buttons: Ext.MessageBox.OK,
                                                                icon: 'x-message-box-error'
                                                            });
                                                        }

                                                    }


                                                }
                                            }
                                        ]
                                    });
                                    //修改用户窗口
                                    accountUpdateWin.show();
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
            }
        );

        this.items = [searchForm, accountGrid];
        this.callParent(arguments);
    }

});
