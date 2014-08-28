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
Ext.define('EBDesktop.role.roleList', {
    extend: 'Ext.container.Container',
    alias: 'widget.roleList',
    id: 'roleList',
    title: "角色管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {

        // 角色数据集
        var roleListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.RoleListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/role/list'
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
            autoLoad: {start: 0, limit: 13},
            pageSize: 13
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

        // 移除角色
        function removeRoleList() {

            var url = '/role/deleteMore',
                ids = Espide.Common.getGridSels('RoleGrid', 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的角色');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        idArray: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('RoleGrid').getStore().loadPage(1);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });
        }

        // 刷新roleGrid
        function refreshRoleGrid() {
            Ext.getCmp('RoleGrid').getStore().load();
        }


        //添加角色
        function roleAdd() {
            var permissionList;
            var permissionCheckGroup = {};
            //发送ajax请求 获取动态权限模块
            Ext.Ajax.request({
                url: "/role/add",

                success: function (response, options) {
                    var data = Ext.JSON.decode(response.responseText);
                    //假如请求成功
                    if (data.success) {
                        permissionList = Ext.JSON.decode(response.responseText).data.list;
                        Ext.each(permissionList, function (permission) {

                            if (!permissionCheckGroup[permission.name]) {
                                permissionCheckGroup[permission.name] = [];
                            }
                            Ext.each(permission.operationList, function (operationList) {

                                if (operationList.configable) {
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
                                                        id: operationList.id,
                                                        status: obj.checked
                                                    },
                                                    success: function (response, options) {
                                                        var data = Ext.JSON.decode(response.responseText);
                                                        if (data.success) {
                                                            var arr = Ext.JSON.decode(response.responseText).data.list;
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
                                                        id: operationList.id,
                                                        status: obj.checked
                                                    },
                                                    success: function (response, options) {
                                                        var data = Ext.JSON.decode(response.responseText);
                                                        if (data.success) {
                                                            var arr = Ext.JSON.decode(response.responseText).data.list;
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


                        });
                        //创建表单
                        var formItems = [];
                        formItems.push({
                                fieldLabel: '角色名称',
                                name: 'roleName',
                                allowBlank: false,
                                blankText: '角色名称不能为空',
                                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                regexText: '首尾不得包含空格'
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
                            height: 400,
                            items: roleAddForm,
                            buttonAlign: "right",
                            modal: true,
                            autoScroll: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'addBtn',
                                    handler: function () {
                                        if (roleAddForm.isValid()) {
                                            var ids = roleAddForm.getForm().getValues();
                                            if (roleAddForm.getForm().isValid()) {
                                                roleAddForm.getForm().submit({
                                                    clientValidation: true, //对客户端进行验证
                                                    url: "/role/save",
                                                    params: {
                                                        roleName: ids["roleName"],
                                                        permissionIds: Array.isArray(ids["permissionId"]) ? ids["permissionId"].join(",") : ids["permissionId"] },
                                                    waitMsg: "保存成功...",
                                                    success: function (form, action) {
                                                        var data = Ext.JSON.decode(action.response.responseText);
                                                        if (data.success) {
                                                            Espide.Common.tipMsg('保存成功', data.msg);
                                                            roleAddWin.close();
                                                            refreshRoleGrid();

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

                                                    },
                                                    timeout: 5
                                                });
                                            }

                                        }

                                    }
                                },
                                {
                                    text: '重写',
                                    itemId: 'resetBtn',
                                    handler: function () {
                                        roleAddWin.getForm().reset();
                                    }
                                }
                            ]
                        });

                        roleAddWin.show();

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

        //用户查询
        function searchRole(button) {
            Espide.Common.doSearch("RoleGrid", "searchRole", true);
        }

        //顶栏表单
        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'searchRole',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'name',
                    fieldLabel: '角色名称',
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {
                                searchRole();
                            }
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchRole
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: roleAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选',
                    itemId: 'deleteBtn',
                    handler: removeRoleList
                }
            ]
        });

        //角色列表
        var roleGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'RoleGrid',
                loadMask: true,
                forceFit: true,
                store: roleListStore,
                disableSelection: false,
                selType: 'checkboxmodel',
                columns: [
                    {
                        header: '角色名称',
                        dataIndex: 'name'
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    store: roleListStore,
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    displayInfo: true,
                    emptyMsg: '没有记录'
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
                                    }
                                    Ext.each(permission.operationList, function (operationList) {
                                        if (operationList.configable) {
                                            permissionCheckGroup[permission.name].push({boxLabel: operationList.name, name: 'permissionId', inputValue: operationList.id, checked: operationList.op, id: "check" + operationList.id, listeners: {
                                                change: function (obj) {
                                                    Ext.Ajax.request({
                                                        url: "/role/permission/link",
                                                        params: {
                                                            id: operationList.id,
                                                            status: obj.checked
                                                        },
                                                        success: function (response, options) {
                                                            var data = Ext.JSON.decode(response.responseText);
                                                            if (data.success) {
                                                                var arr = Ext.JSON.decode(response.responseText).data.list;
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
                                            }});
                                        } else {
                                            permissionCheckGroup[permission.name].push(
                                                {
                                                    boxLabel: operationList.name,
                                                    name: 'permissionId',
                                                    inputValue: operationList.id,
                                                    checked: operationList.op,
                                                    id: "check" + operationList.id,
                                                    hidden: true,
                                                    listeners: {
                                                        change: function (obj) {
                                                            Ext.Ajax.request({
                                                                url: "/role/permission/link",
                                                                params: {
                                                                    id: operationList.id,
                                                                    status: obj.checked
                                                                },
                                                                success: function (response, options) {
                                                                    var data = Ext.JSON.decode(response.responseText);
                                                                    if (data.success) {
                                                                        var arr = Ext.JSON.decode(response.responseText).data.list;
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
                                                    }});
                                        }


                                    });

                                });

                                var formItems = [];
                                formItems.push({
                                        fieldLabel: '角色名称',
                                        name: 'roleName',
                                        value: record.get('name'),
                                        disabled: true,
                                        regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                                        regexText: '首尾不得包含空格'
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
                                    height: 400,
                                    items: roleUpdateForm,
                                    buttonAlign: "right",
                                    modal: true,
                                    autoScroll: true,
                                    buttons: [
                                        {
                                            text: '保存',
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

                                                            Espide.Common.tipMsg('保存成功', data.msg);
                                                            roleUpdateWin.close();
                                                            refreshRoleGrid();

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

                                roleUpdateWin.show();

                            },
                            failure: function (response, options) {

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

        this.items = [searchForm, roleGrid];
        this.callParent(arguments);
    }
});
