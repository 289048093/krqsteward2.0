/**
 * 平台列表
 * Created by Lein.xu
 */

//定义平台列表数据模型
Ext.define('EBDesktop.platformListModel', {
    extend: 'Ext.data.Model',
    fields: ['id','code','name'],
    idProperty: 'id'
});
//定义平台列表类
Ext.define('EBDesktop.platform.platformList', {
    extend: 'Ext.container.Container',
    alias: 'widget.platformList',
    id: 'platformList',
    title: "平台管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {


        // 刷新platformListGrid
        function refreshplatformGrid() {
            Ext.getCmp('platformListGrid').getStore().load();
        }

        // 平台数据集
        var platformListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.platformListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/platform/list'
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

        //平台查询
        function searchplatform(button) {
            Espide.Common.doSearch("platformListGrid", "searchplatform", true);
        }

        //平台应商
        function platformAdd() {
            var formItems = [];
            formItems.push({
                    fieldLabel: '平台名称',
                    emptyText:'请输入平台名称',
                    name: 'name',
                    regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                    regexText: '首尾不得包含空格'
                }
            );
            //创建用户添加表单
            var platformAddForm = Ext.create('Ext.form.Panel', {
                baseCls: 'x-plain',
                labelWidth: 100,
                defaults: {
                    width: 380
                },
                id: 'platformAdd',
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
                    labelWidth: 100
                },
                defaultType: 'textfield',
                items: formItems

            });
            //创建一个弹窗容器，把表单面板放进弹窗容器
            var addWin = Ext.create("Ext.window.Window", {
                title: '平台应商',
                width: 330,
                height: 130,
                modal: true,
                autoHeight: true,
                animateTarget: Ext.getBody(),
                layout: 'fit',
                plain: true,
                buttonAlign: 'right',
                bodyStyle: 'padding:5px;',
                items: platformAddForm,
                buttons: [
                    {
                        text: '保存',
                        itemId: 'addBtn',
                        handler: function () {
                            var addForm = platformAddForm.getForm();
                            datas = addForm.getValues();
                            if (addForm.isValid()) {
                                addForm.submit({
                                    clientValidation: true, //对客户端进行验证
                                    url: "platform/save",
                                    method: "post",
                                    params: {name: datas["name"]},
                                    success: function (form, options) {
                                        var data = Ext.JSON.decode(options.response.responseText);
                                        if (data.success) {
                                            Espide.Common.tipMsg('保存成功', data.msg);
                                            addWin.close();
                                            Ext.getCmp('platformListGrid').getStore().load();
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


                    },
//                    {
//                        text: '重写',
//                        itemId: 'resetBtn',
//                        handler: function () {
//                            platformAddForm.getForm().reset();
//                        }
//                    }
                ]
            });

            //显示弹窗
            addWin.show();
        }

        //平台删除
        function platformDel() {
            var url = 'platform/delete',
                ids = Espide.Common.getGridSels('platformListGrid', 'id');

            if (ids.length < 1||ids.length > 1) {
                Espide.Common.showGridSelErr('请先选择要删除的平台,只能单个删除');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        id: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('platformListGrid').getStore().loadPage(1);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });
        }

        //顶栏表单
        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'searchplatform',
            defaults: {
                xtype: 'combo',
                labelWidth: 100,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'name',
                    fieldLabel: '平台名称',
                    emptyText:'请输入平台名称',
                    listeners: {
                        'change':function(){
                            Espide.Common.reLoadGird('platformListGrid', 'searchplatform', true);
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchplatform
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: platformAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选定',
                    itemId: 'deleteBtn',
                    handler: platformDel
                }
            ]

        });


//创建平台数据表格
        var platformListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'platformListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: platformListStore,
                columns: [
                    Ext.create('Ext.grid.RowNumberer',{text : '行号', width : 50}),
                    {
                        header: '平台名称',
                        dataIndex: 'name'
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: platformListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'afterrender':function(grid){
                        grid.getStore().getProxy().extraParams = Ext.getCmp('searchplatform').getValues();
                    },
                    'render': function (input) {
                        var map = new Ext.util.KeyMap({
                            target: 'searchplatform',    //target可以是组建的id  加单引号
                            binding: [{                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function(){
                                    Espide.Common.reLoadGird('platformListGrid', 'searchplatform', true);
                                }
                            }]
                        });
                    },
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件
                        var formItems = [];
                        formItems.push({
                                fieldLabel: '平台名称',
                                name: 'name',
                                value: record.get('name')
                            }
                        );
                        var platformUpdateForm = Ext.create('Ext.form.Panel', {
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
                            title: "修改平台",
                            width: 330,
                            height: 130,
                            animateTarget: Ext.getBody(),
                            items: platformUpdateForm,
                            buttonAlign: "right",
                            modal: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        if (platformUpdateForm.isValid()) {
                                            platformUpdateForm.getForm().submit({
                                                url: "/platform/update",
                                                params: {id: record.get("id"), name: record.get('name')},
                                                success: function (form, action) {
                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    if (data.success) {

                                                        Espide.Common.tipMsg('保存成功', data.msg);
                                                        winUpdate.close();
                                                        Ext.getCmp('platformListGrid').getStore().load();

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
                            ]
                        });
                        //更新窗口
                        winUpdate.show();
                    }
                }
            }
        );


        this.items = [searchForm, platformListGrid];
        this.callParent(arguments);
    }

})
;
