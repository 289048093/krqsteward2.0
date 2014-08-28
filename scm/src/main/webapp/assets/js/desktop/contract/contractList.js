/**
 * 供应商合同列表
 * Created by Lein.xu
 */

//定义供应商合同列表数据模型
Ext.define('EBDesktop.contractListModel', {
    extend: 'Ext.data.Model',
    fields: ['id','code','deposit','serviceFee','invoiceEJSTitle',
    'invoiceOtherTitle','otherRule','remark','overdueFine','beginTime',
        'endTime','realEndTime','endReason','supplier','ejscompName',
        {name:'supplierName',type: 'string',mapping:'supplier.name'},
        {name: 'createTime', type: 'date', dateFormat: 'time'},
        {name: 'realEndTime', type: 'date', dateFormat: 'time'},
        {name: 'endTime', type: 'date', dateFormat: 'time'}

    ],
    idProperty: 'id'
});
//定义供应商合同列表类
Ext.define('EBDesktop.contract.contractList', {
    extend: 'Ext.container.Container',
    alias: 'widget.contractList',
    id: 'contractList',
    title: "供应商合同管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {

        // 刷新contractListGrid
        function refreshcontractGrid() {
            Ext.getCmp('contractListGrid').getStore().load();
        }

        // 供应商合同数据集
        var contractListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.contractListModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/contract/list'
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

        //供应商合同查询
        function searchcontract(button) {
            Espide.Common.doSearch("contractListGrid", "searchcontract", true);
        }

        //添加供应商合同
        function contractAdd() {
            var formItems = [];
            formItems.push({
                    fieldLabel: '供应商合同名称',
                    name: 'name',
                    regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                    regexText: '首尾不得包含空格'
                }
            );
            //创建用户添加表单
            var contractAddForm = Ext.create('Ext.form.Panel', {
                baseCls: 'x-plain',
                labelWidth: 100,
                defaults: {
                    width: 380
                },
                id: 'contractAdd',
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
                title: '添加供应商合同',
                width: 500,
                height: 130,
                modal: true,
                autoHeight: true,
                layout: 'fit',
                plain: true,
                buttonAlign: 'right',
                bodyStyle: 'padding:5px;',
                items: contractAddForm,
                buttons: [
                    {
                        text: '保存',
                        itemId: 'addBtn',
                        handler: function () {
                            var addForm = contractAddForm.getForm();
                            datas = addForm.getValues();
                            if (addForm.isValid()) {
                                addForm.submit({
                                    clientValidation: true, //对客户端进行验证
                                    url: "contract/save",
                                    method: "post",
                                    params: {name: datas["name"]},
                                    success: function (form, options) {
                                        var data = Ext.JSON.decode(options.response.responseText);
                                        if (data.success) {
                                            Espide.Common.tipMsg('保存成功', data.msg);
                                            addWin.close();
                                            Ext.getCmp('contractListGrid').getStore().load();
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

        //供应商合同删除
        function contractDel() {
            var url = 'contract/delete',
                ids = Espide.Common.getGridSels('contractListGrid', 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的供应商合同');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        idArray: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('contractListGrid').getStore().loadPage(1);
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
            id: 'searchcontract',
            defaults: {
                xtype: 'combo',
                labelWidth: 100,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'name',
                    fieldLabel: '供应商合同名称',
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == Ext.EventObject.ENTER) {
                                searchcontract();
                            }
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchcontract
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: contractAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选定',
                    itemId: 'deleteBtn',
                    handler: contractDel
                }
            ]

        });


//创建供应商合同数据表格
        var contractListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'contractListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: contractListStore,
                columns: [
                    new Ext.grid.RowNumberer(),
                    {
                        header: '供应商',
                        dataIndex: 'supplierName'
                    },{
                        header: '采购商',
                        dataIndex: 'ejscompName'
                    },
                    {
                        header: '合同编号',
                        dataIndex: 'code'
                    },
                    {
                        header: '合同创建时间',
                        dataIndex: 'createTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        header: '合同结束期限',
                        dataIndex: 'endTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        header: '终止时间',
                        dataIndex: 'realEndTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        header: '终止原因',
                        dataIndex: 'endReason'
                    },
                    {
                        header: '滞纳金情况',
                        dataIndex: 'overdueFine'
                    },
                    {
                        header: '保证金',
                        dataIndex: 'deposit'
                    },
                    {
                        header: '技术服务费/年',
                        dataIndex: 'serviceFee'
                    },
                    {
                        header: '开具发票给顾客',
                        dataIndex: 'invoiceEJSTitle'
                    },
                    {
                        header: '第三方平台销售是否补开发票给易居尚',
                        dataIndex: 'invoiceOtherTitle'
                    },
                    {
                        header: '其他条款',
                        dataIndex: 'otherRule'
                    },
                    {
                        header: '补充协议',
                        dataIndex: 'remark'
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: contractListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件
                        var formItems = [];
                        formItems.push({
                                fieldLabel: '供应商合同',
                                name: 'name',
                                value: record.get('name')
                            }
                        );
                        var contractUpdateForm = Ext.create('Ext.form.Panel', {
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
                            title: "修改供应商合同",
                            width: 900,
                            height: 130,
                            items: contractUpdateForm,
                            buttonAlign: "right",
                            modal: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        if (contractUpdateForm.isValid()) {
                                            contractUpdateForm.getForm().submit({
                                                url: "/contract/update",
                                                params: {id: record.get("id"), name: record.get('name')},
                                                success: function (form, action) {
                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    if (data.success) {

                                                        Espide.Common.tipMsg('保存成功', data.msg);
                                                        winUpdate.close();
                                                        Ext.getCmp('contractListGrid').getStore().load();

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


        this.items = [searchForm, contractListGrid];
        this.callParent(arguments);
    }

})
;
