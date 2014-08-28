/**
 * 角色列表
 * Created by Lein.xu
 */
//定义角色 数据模型
Ext.define('EBDesktop.RoleListModel', {
    extend: 'Ext.data.Model',
    fields: ["id", 'name'],
    idProperty: "id"
});

//定义角色类
Ext.define('EBDesktop.permission.roleList', {
    extend: 'Ext.container.Container',
    alias: 'widget.roleList',
    id: 'roleList',
    title: "角色管理",
    fixed: true,
    layout: 'fit',
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

        // 移除角色
        function removeRoleList() {
            var records = getGridSelected('RoleGrid'),
                ids = getJsonAttr(records, 'id');

            if (records.length < 1) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '请先选择要删除角色。',
                    model: false,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
            } else {
                Ext.Msg.confirm("提示", "您确定要删除所选的角色吗？", function (optional) {
                    if (optional == "yes") {
                        Ext.Ajax.request({
                            url: "/role/deleteMore",
                            method: "get",
                            params: {
                                idArray: ids.join(",")
                            },
                            success: function (response) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: '删除成功',
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-info'
                                    });
                                    Ext.getCmp('RoleGrid').getStore().load();
                                } else {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: data.msg,
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-error'
                                    });
                                }
                            },
                            failure: function (data) {
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

        // 刷新roleGrid
        function refreshRoleGrid() {
            Ext.getCmp('RoleGrid').getStore().load();
        }

        // 角色数据集
        var roleListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.RoleListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/role/list'
                    //read: "assets/js/desktop/permission/roleList.json"
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

        //角色列表
        var roleGrid = Ext.create("Ext.grid.Panel", {
                id: 'RoleGrid',
                loadMask: true,
                forceFit: true,
                store: roleListStore,
                disableSelection: false,
                selType: 'checkboxmodel',
                columns: [
                    {
                        header: '角色ID',
                        dataIndex: 'id'
                    },
                    {
                        header: '角色名称',
                        dataIndex: 'name'
                    }
                ],
                tbar: [
                    {
                        text: '添加角色',
                        iconCls: 'icon-add',
                        handler: function () {
                            var permissionList;
                            var permissionCheckGroup = {};
                            //发送ajax请求 获取动态权限模块
                            Ext.Ajax.request({
                                //url: "/role/permission/get/",
                                url: "/role/add",
//                                params: {
//                                    roleId: 0 //获取所有的模块
//                                },
                                success: function (response, options) {
                                    var data = Ext.JSON.decode(response.responseText);
                                    //假如请求成功
                                    if (data.success) {
                                        permissionList = Ext.JSON.decode(response.responseText).data.list;
                                        Ext.each(permissionList, function (permission) {

                                            if (!permissionCheckGroup[permission.name]) {
                                                permissionCheckGroup[permission.name] = [];
                                                //接收operationList.configable 为false的数组
                                                var configableFalse = [];
                                            }
                                            Ext.each(permission.operationList, function (operationList) {

                                                if (operationList.configable) {
                                                    console.log(operationList.name);
                                                    permissionCheckGroup[permission.name].push({
                                                        boxLabel: operationList.name,
                                                        name: 'permissionId',
                                                        inputValue: operationList.id,
                                                        checked: false,
                                                        id: "check" + operationList.id,
                                                        listeners: {
                                                            change: function (obj) {
                                                                Ext.Ajax.request({
                                                                    url: "/role/permission/link",
                                                                    params: {
                                                                        id: operationList.id
                                                                    },
                                                                    success: function (response, options) {
                                                                        var data = Ext.JSON.decode(response.responseText);
                                                                        if (data.success) {
                                                                            var arr = Ext.JSON.decode(response.responseText).data.intArray;
                                                                            if (arr.length == 0) { //当返回是空数组时 不执行以下操作
                                                                                return;
                                                                            } else {
                                                                                if (obj.checked == true) {
                                                                                    for (var i = 0; i < arr.length; i++) {
                                                                                        Ext.getCmp("check" + arr[i]).setValue('true');
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    for (var i = 0; i < arr.length; i++) {
                                                                                        Ext.getCmp("check" + arr[i]).setValue('false');
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                });
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    permissionCheckGroup[permission.name].push({
                                                        boxLabel: operationList.name,
                                                        name: 'permissionId',
                                                        inputValue: operationList.id,
                                                        checked: false,
                                                        id: "check" + operationList.id,
                                                        hidden: true,
                                                        listeners: {
                                                            change: function (obj) {
                                                                Ext.Ajax.request({
                                                                    url: "/role/permission/link",
                                                                    params: {
                                                                        id: operationList.id
                                                                    },
                                                                    success: function (response, options) {
                                                                        var data = Ext.JSON.decode(response.responseText);
                                                                        if (data.success) {
                                                                            var arr = Ext.JSON.decode(response.responseText).data.intArray;
                                                                            if (arr.length == 0) { //当返回是空数组时 不执行以下操作
                                                                                return;
                                                                            } else {
                                                                                if (obj.checked == true) {
                                                                                    for (var i = 0; i < arr.length; i++) {
                                                                                        Ext.getCmp("check" + arr[i]).setValue('true');
                                                                                    }
                                                                                } else {
                                                                                    for (var i = 0; i < arr.length; i++) {
                                                                                        Ext.getCmp("check" + arr[i]).setValue('false');
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            });

                                            //整合 operationList.configable 为false的数组 注明:这样动态渲染不会出现空格
                                            // permissionCheckGroup[permission.name].concat(configableFalse);

                                        });
                                        //创建表单
                                        var formItems = [];
                                        formItems.push({
                                                fieldLabel: '角色名称',
                                                name: 'roleName',
                                                allowBlank: false,
                                                blankText: '角色名称不能为空'
                                            }
                                        );

                                        if (permissionList.length > 0) {
                                            for (var p in permissionCheckGroup) {
                                                formItems.push({
                                                    xtype: 'fieldset',
                                                    title: p,
                                                    collapsible: true,
                                                    collapsed: false,
                                                    autoHeight: true,
                                                    items: {
                                                        xtype: 'checkboxgroup',
                                                        columns: 3,
                                                        items: permissionCheckGroup[p]
                                                    }
                                                });
                                            }
                                        }
                                        //角色添加表格
                                        var roleAddForm = Ext.create('Ext.form.Panel', {
                                            id: 'roleAddForm',
                                            autoScroll: true,
                                            forceFit: true,
                                            border: false,
                                            layout: 'form',
                                            header: false,
                                            frame: false,
                                            width: 500,
                                            bodyPadding: '5 5 0',
                                            requires: ['Ext.form.field.Text'],
                                            fieldDefaults: {
                                                msgTarget: 'qtip',
                                                labelWidth: 75
                                            },
                                            defaultType: 'textfield',
                                            items: formItems
                                        });

                                        //角色添加窗口
                                        var roleAddWin = Ext.create("Ext.window.Window", {
                                            title: "添加角色",
                                            width: 530,
                                            height: 560,
                                            items: roleAddForm,
                                            buttonAlign: "center",
                                            modal: true,
                                            autoScroll: true,
                                            buttons: [
                                                {
                                                    text: '确认添加',
                                                    itemId: 'addBtn',
                                                    handler: function () {
                                                        if (roleAddForm.isValid()) {
                                                            var ids = roleAddForm.getForm().getValues();
                                                            if (roleAddForm.getForm().isValid()) {
                                                                roleAddForm.getForm().submit({
                                                                    clientValidation: true, //对客户端进行验证
                                                                    url: "/role/save",
                                                                    params: {roleName: ids["roleName"], permissionIds: Array.isArray(ids["permissionId"]) ? ids["permissionId"].join(",") : ids["permissionId"] },
                                                                    waitMsg: "保存成功...",
                                                                    success: function (form, action) {
                                                                        var data = Ext.JSON.decode(action.response.responseText);
                                                                        if (data.success) {
                                                                            Ext.MessageBox.show({
                                                                                title: '提示',
                                                                                msg: '保存成功！',
                                                                                buttons: Ext.MessageBox.OK,
                                                                                icon: 'x-message-box-info',
                                                                                fn: function () {
                                                                                    roleAddWin.close();
                                                                                    refreshRoleGrid();
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
                                                                    },
                                                                    timeout: 5
                                                                });
                                                            }

                                                        }

                                                    }
                                                },
                                                {
                                                    text: '关闭窗口',
                                                    itemId: 'closeBtn',
                                                    handler: function () {
                                                        roleAddWin.close();
                                                    }
                                                }
                                            ]
                                        });

                                        roleAddWin.show();

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
                    },
                    {
                        text: '删除',
                        iconCls: 'icon-remove',
                        handler: removeRoleList
                    },
                    {
                        text: '刷新',
                        iconCls: 'icon-refresh',
                        handler: refreshRoleGrid
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: roleListStore,
                    displayInfo: true
                }),
                listeners: {//表格单元添加双击事件
                    'itemdblclick': function (view, record, item, index, e) {
                        var permissionList;
                        var permissionCheckGroup = {};
                        //发送ajax请求
                        Ext.Ajax.request({
                            url: "/role/permission/get",
                            params: {
                                roleId: record.get('id')
                            },
                            success: function (response, options) {
                                permissionList = Ext.JSON.decode(response.responseText).data.list;
                                Ext.each(permissionList, function (permission) {

                                    if (!permissionCheckGroup[permission.name]) {
                                        permissionCheckGroup[permission.name] = [];
                                        //接收operationList.configable 为false的数组
                                        var configableFalse = [];
                                    }
                                    Ext.each(permission.operationList, function (operationList) {
                                        if (operationList.configable) {
                                            permissionCheckGroup[permission.name].push({boxLabel: operationList.name, name: 'permissionId', inputValue: operationList.id, checked: operationList.op, id: "check" + operationList.id, listeners: {
                                                change: function (obj) {
                                                    Ext.Ajax.request({
                                                        url: "/role/permission/link",
                                                        params: {
                                                            id: operationList.id
                                                        },
                                                        success: function (response, options) {
                                                            var data = Ext.JSON.decode(response.responseText);
                                                            if (data.success) {
                                                                var arr = Ext.JSON.decode(response.responseText).data.intArray;
                                                                if (arr.length == 0) { //当返回是空数组时 不执行以下操作
                                                                    return;
                                                                } else {
                                                                    if (obj.checked == true) {
                                                                        for (var i = 0; i < arr.length; i++) {
                                                                            Ext.getCmp("check" + arr[i]).setValue('true');
                                                                        }
                                                                    }
                                                                    /*         else {
                                                                     for (var i = 0; i < arr.length; i++) {
                                                                     Ext.getCmp("check" + arr[i]).setValue('false');
                                                                     }
                                                                     }*/
                                                                }
                                                            }
                                                        }

                                                    });
                                                }
                                            }});
                                        } else {
                                            permissionCheckGroup[permission.name].push({boxLabel: operationList.name, name: 'permissionId', inputValue: operationList.id, checked: operationList.op, id: "check" + operationList.id, hidden: true, listeners: {
                                                change: function (obj) {
                                                    Ext.Ajax.request({
                                                        url: "/role/permission/link",
                                                        params: {
                                                            id: operationList.id
                                                        },
                                                        success: function (response, options) {
                                                            var data = Ext.JSON.decode(response.responseText);
                                                            if (data.success) {
                                                                var arr = Ext.JSON.decode(response.responseText).data.intArray;
                                                                if (arr.length == 0) { //当返回是空数组时 不执行以下操作
                                                                    return;
                                                                } else {
                                                                    if (obj.checked == true) {
                                                                        for (var i = 0; i < arr.length; i++) {
                                                                            Ext.getCmp("check" + arr[i]).setValue('true');
                                                                        }
                                                                    }
                                                                    /*            else {
                                                                     for (var i = 0; i < arr.length; i++) {
                                                                     Ext.getCmp("check" + arr[i]).setValue('false');
                                                                     }
                                                                     }*/
                                                                }
                                                            }
                                                        }

                                                    });
                                                }
                                            }});
                                        }


                                    });
//整合 operationList.configable 为false的数组 注明:这样动态渲染不会出现空格
                                    //permissionCheckGroup[permission.name].concat(configableFalse);
                                    //console.log(permissionCheckGroup[permission.name]);

                                });

                                var formItems = [];
                                formItems.push({
                                        fieldLabel: '角色名称',
                                        name: 'roleName',
                                        value: record.get('name'),
                                        disabled: true
                                    }
                                );

                                formItems.push({
                                    xtype: 'hidden',
                                    name: 'roleId',
                                    value: record.get('id')
                                });


                                if (permissionList.length > 0) {
                                    for (var p in permissionCheckGroup) {

                                        formItems.push({
                                            xtype: 'fieldset',
                                            title: p,
                                            collapsible: true,
                                            collapsed: false,
                                            autoHeight: true,
                                            width: 400,
                                            items: {
                                                xtype: 'checkboxgroup',
                                                columns: 3,
                                                items: permissionCheckGroup[p]
                                            }
                                        });
                                    }
                                }
                                //创建角色更新表格
                                var roleUpdateForm = Ext.create('Ext.form.Panel', {
                                    id: 'roleUpdateForm',
                                    autoScroll: true,
                                    forceFit: true,
                                    border: false,
                                    layout: 'form',
                                    header: false,
                                    frame: false,
                                    width: 500,
                                    bodyPadding: '5 5 0',
                                    requires: ['Ext.form.field.Text'],
                                    fieldDefaults: {
                                        msgTarget: 'side',
                                        labelWidth: 75
                                    },
                                    defaultType: 'textfield',
                                    items: formItems
                                });

                                //角色修改窗口
                                var roleUpdateWin = Ext.create("Ext.window.Window", {
                                    title: "修改用户角色",
                                    width: 530,
                                    height: 560,
                                    items: roleUpdateForm,
                                    buttonAlign: "center",
                                    modal: true,
                                    autoScroll: true,
                                    buttons: [
                                        {
                                            text: '确认修改',
                                            itemId: 'addBtn',
                                            handler: function () {
                                                var ids = roleUpdateForm.getForm().getValues();
                                                roleUpdateForm.getForm().submit({
                                                    clientValidation: true, //对客户端进行验证
                                                    url: "role/permission/save",
                                                    params: { permissionIds: Array.isArray(ids["permissionId"]) ? ids["permissionId"].join(",") : ids["permissionId"] },
                                                    method: "get",
                                                    success: function (form, action) {
                                                        var data = Ext.JSON.decode(action.response.responseText);
                                                        if (data.success) {
                                                            Ext.MessageBox.show({
                                                                title: '提示',
                                                                msg: '修改成功！',
                                                                buttons: Ext.MessageBox.OK,
                                                                icon: 'x-message-box-info',
                                                                fn: function () {
                                                                    roleUpdateWin.close();
                                                                    refreshRoleGrid();
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
                                        },
                                        {
                                            text: '关闭窗口',
                                            itemId: 'resetBtn',
                                            handler: function () {
                                                roleUpdateWin.close();
                                            }
                                        }
                                    ]
                                });

                                roleUpdateWin.show();

                            },
                            failure: function (response, options) {
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

        this.items = [roleGrid];
        this.callParent(arguments);

    }

});
