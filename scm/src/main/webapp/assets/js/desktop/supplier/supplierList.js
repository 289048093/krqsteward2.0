/**
 * 供应商列表
 * Created by Lein.xu
 */

//定义供应商列表数据模型
Ext.define('EBDesktop.supplierListModel', {
    extend: 'Ext.data.Model',
    fields: ['id','code','name'],
    idProperty: 'id'
});
//定义供应商列表类
Ext.define('EBDesktop.supplier.supplierList', {
    extend: 'Ext.container.Container',
    alias: 'widget.supplierList',
    id: 'supplierList',
    title: "供应商管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {


        // 刷新supplierListGrid
        function refreshsupplierGrid() {
            Ext.getCmp('supplierListGrid').getStore().load();
        }

        // 供应商数据集
        var supplierListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.supplierListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/supplier/list'
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

        //供应商查询
        function searchsupplier(button) {
            Espide.Common.doSearch("supplierListGrid", "searchsupplier", true);
        }

        //添加供应商
        function supplierAdd() {
            var formItems = [];
            formItems.push({
                    fieldLabel: '供应商名称',
                    name: 'name',
                    emptyText:'请输入供应商名称',
                    regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                    regexText: '首尾不得包含空格'
                }
            );
            //创建用户添加表单
            var supplierAddForm = Ext.create('Ext.form.Panel', {
                baseCls: 'x-plain',
                labelWidth: 100,
                defaults: {
                    width: 300
                },
                id: 'supplierAdd',
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
                title: '添加供应商',
                width: 330,
                height: 130,
                modal: true,
                animateTarget: Ext.getBody(),
                autoHeight: true,
                layout: 'fit',
                plain: true,
                buttonAlign: 'right',
                bodyStyle: 'padding:5px;',
                items: supplierAddForm,
                buttons: [
                    {
                        text: '保存',
                        itemId: 'addBtn',
                        handler: function () {
                            var addForm = supplierAddForm.getForm();
                            datas = addForm.getValues();
                            if (addForm.isValid()) {
                                addForm.submit({
                                    clientValidation: true, //对客户端进行验证
                                    url: "supplier/save",
                                    method: "post",
                                    params: {name: datas["name"]},
                                    success: function (form, options) {
                                        var data = Ext.JSON.decode(options.response.responseText);
                                        if (data.success) {
                                            Espide.Common.tipMsg('保存成功', data.msg);
                                            addWin.close();
                                            Ext.getCmp('supplierListGrid').getStore().load();
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

            //显示弹窗
            addWin.show();
        }

        //供应商删除
        function supplierDel() {
            var url = 'supplier/delete',
                ids = Espide.Common.getGridSels('supplierListGrid', 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的供应商');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        idArray: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('supplierListGrid').getStore().loadPage(1);
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
            id: 'searchsupplier',
            defaults: {
                xtype: 'combo',
                labelWidth: 100,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'name',
                    fieldLabel: '供应商名称',
                    emptyText:'请输入供应商名称',
                    listeners: {
                        'change':function(){
                            Espide.Common.reLoadGird('supplierListGrid', 'searchsupplier', true);
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchsupplier
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: supplierAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选定',
                    itemId: 'deleteBtn',
                    handler: supplierDel
                }
            ]

        });


//创建供应商数据表格
        var supplierListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'supplierListGrid',
                loadMask: true,
                forceFit: true,
                //selType: 'checkboxmodel',
                selModel:Ext.create('Ext.selection.CheckboxModel',{mode:'SINGLE',showHeaderCheckbox:false}),
                store: supplierListStore,
                columns: [
                    Ext.create('Ext.grid.RowNumberer',{text : '行号', width : 50}),
                    {
                        header: '供应商名称',
                        dataIndex: 'name'
                    },
                    {
                        header: '供应商编号',
                        dataIndex: 'code'
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: supplierListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'afterrender':function(grid){
                        grid.getStore().getProxy().extraParams = Ext.getCmp('searchsupplier').getValues();
                    },
                    'render': function (input) {
                        var map = new Ext.util.KeyMap({
                            target: 'searchsupplier',    //target可以是组建的id  加单引号
                            binding: [{                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function(){
                                    Espide.Common.reLoadGird('supplierListGrid', 'searchsupplier', true);
                                }
                            }]
                        });
                    },
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件
                        var formItems = [];
                        formItems.push({
                                fieldLabel: '供应商名称',
                                name: 'name',
                                value: record.get('name')
                            }
                        );
                        var supplierUpdateForm = Ext.create('Ext.form.Panel', {
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
                            title: "修改供应商",
                            width: 330,
                            height: 130,
                            animateTarget: Ext.getBody(),
                            items: supplierUpdateForm,
                            buttonAlign: "right",
                            modal: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        if (supplierUpdateForm.isValid()) {
                                            supplierUpdateForm.getForm().submit({
                                                url: "/supplier/update",
                                                params: {id: record.get("id"), name: record.get('name')},
                                                success: function (form, action) {
                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    if (data.success) {

                                                        Espide.Common.tipMsg('保存成功', data.msg);
                                                        winUpdate.close();
                                                        Ext.getCmp('supplierListGrid').getStore().load();

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


        this.items = [searchForm, supplierListGrid];
        this.callParent(arguments);
    }

})
;
