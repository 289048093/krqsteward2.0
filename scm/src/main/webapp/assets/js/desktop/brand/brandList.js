/**
 * 品牌列表
 * Created by Lein.xu
 */

//定义品牌列表数据模型
Ext.define('EBDesktop.brandListModel', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'name',
        'code',
        'paymentRule',
        'paymentType',
        'supplier',
        {name: 'supplierName', mapping: 'supplier.name'},
        {name: 'supplierId', mapping: 'supplier.id'},
        {name: 'supplierCode', mapping: 'supplier.code'}
    ],
    idProperty: 'id'
});
//定义品牌列表类
Ext.define('EBDesktop.brand.brandList', {
    extend: 'Ext.container.Container',
    alias: 'widget.brandList',
    id: 'brandList',
    title: "品牌管理",
    fixed: true,
    layout: 'border',
    initComponent: function () {

        // 刷新brandListGrid
        function refreshBrandGrid() {
            Ext.getCmp('brandListGrid').getStore().load();
        }

        //供应商Store
        //var supplierStore = Espide.Common.createComboStore('/supplier/list',true);

        // 品牌数据集
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

        //品牌查询
        function searchBrand(button) {
            Espide.Common.doSearch("brandListGrid", "searchBrand", true);
        }

        //添加品牌
        function brandAdd() {
            var formItems = [];

            formItems.push({
                xtype: 'combo',
                name: 'supplierId',
                itemId: 'supplierId',
                fieldLabel: '供应商',
                width:300,
                editable: false,
                queryMode: 'local',
                valueField: 'id',
                displayField: 'name',
                emptyText: '不限',
                store: Espide.Common.createComboStore('/supplier/list',false)
            });


            formItems.push({
                    fieldLabel: '品牌名称',
                    emptyText:'请选择品牌名称',
                    name: 'name',
                    regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                    regexText: '首尾不得包含空格'
                }
            );

            formItems.push({
                fieldLabel: '品牌编号',
                emptyText:'请选择品牌编号',
                name: 'code',
                allowBlank:true,
                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                regexText: '首尾不得包含空格'
            });

            formItems.push({
                fieldLabel: '结算方式',
                emptyText:'请选择结算方式',
                name: 'paymentType',
                allowBlank:true,
                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                regexText: '首尾不得包含空格'
            });

            formItems.push({
                fieldLabel: '结算规则',
                emptyText:'请选择结算规则',
                allowBlank:true,
                name: 'paymentRule',
                regex: /(^\S+.*\S+$)|(^\S{1}$)/,
                regexText: '首尾不得包含空格'
            });


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
                height: 230,
                modal: true,
                autoHeight: true,
                layout: 'fit',
                animateTarget: Ext.getBody(),
                plain: true,
                buttonAlign: 'right',
                bodyStyle: 'padding:5px;',
                items: brandAddForm,
                buttons: [
                    {
                        text: '保存',
                        itemId: 'addBtn',
                        handler: function () {
                            var addForm = brandAddForm.getForm();
                            datas = addForm.getValues();
                            if (addForm.isValid()) {
                                addForm.submit({
                                    submitEmptyText:false,
                                    clientValidation: true, //对客户端进行验证
                                    url: "brand/save",
                                    method: "post",
                                    params: {name: datas["name"]},
                                    success: function (form, options) {
                                        var data = Ext.JSON.decode(options.response.responseText);
                                        if (data.success) {
                                            Espide.Common.tipMsg('保存成功', data.msg);
                                            addWin.close();
                                            Ext.getCmp('brandListGrid').getStore().load();
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
//                            brandAddForm.getForm().reset();
//                        }
//                    }
                ]
            });

            //显示弹窗
            addWin.show();
        }

        //品牌删除
        function brandDel() {
            var url = 'brand/delete',
                ids = Espide.Common.getGridSels('brandListGrid', 'id');

            if (ids.length < 1) {
                Espide.Common.showGridSelErr('请先选择要删除的品牌');
                return;
            }

            Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                Espide.Common.doAction({
                    url: url,
                    params: {
                        idArray: ids.join()
                    },
                    successCall: function () {
                        Ext.getCmp('brandListGrid').getStore().loadPage(1);
                    },
                    successTipMsg: '删除成功'
                })(optional);
            });
        }

        //导入品牌
        function includeBrand(){

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
                            name: "multipartFile",
                            fieldLabel: "导入文件",
                            labelWidth: 80,
                            width: 300,
                            anchor: "100%",
                            id: "multipartFile",
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
                        {
                            xtype: 'container',
                            layout: {
                                type: 'hbox',
                                pack: 'left'
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    cls: 'contactBtn',
                                    margin: "0 0 0 20",
                                    text: '下载模板',
                                    listeners: {
                                        'click': function () {
                                            location.href = "/static/templet/brandExcelModel.xls";
                                        }
                                    }
                                }
                            ]}
                    ]


                });


                var includeWin = Ext.create("Ext.window.Window", {
                    title: '导入品牌',
                    width: 500,
                    height: 150,
                    modal: true,
                    autoHeight: true,
                    animateTarget: Ext.getBody(),
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

                                        url: "/brand/leadingIn",
                                        waitMsg: "正在导入验证数据",
                                        success: function (fp, o) {
                                            var data = Ext.JSON.decode(o.response.responseText);
                                            if (data.success) {
                                                Espide.Common.tipMsgIsCloseWindow(data,includeWin,'brandListGrid',true);
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



        //顶栏表单
        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'searchBrand',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'combo',
                    name: 'supplierId',
                    width:300,
                    itemId: 'supplierId',
                    fieldLabel: '供应商',
                    editable: false,
                    queryMode: 'local',
                    valueField: 'id',
                    displayField: 'name',
                    emptyText: '不限',
                    store: Espide.Common.createComboStore('/supplier/list',true)
                },
                {
                    name: 'name',
                    xtype: 'textfield',
                    id:'name',
                    width:160,
                    fieldLabel: '品牌名称',
                    emptyText: '输入品牌名称',
                    listeners:{
                        'change':function(){
                            Espide.Common.reLoadGird('brandListGrid', 'searchBrand', true);
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: '查询',
                    itemId: 'searchBtn',
                    handler: searchBrand
                },
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: brandAdd
                },
                {
                    xtype: 'button',
                    text: '删除已选定',
                    itemId: 'deleteBtn',
                    handler: brandDel
                },
                {
                    xtype: 'button',
                    text: '品牌导入',
                    height: 24,
                    width: 80,
                    itemId: 'includeBrand',
                    handler:includeBrand
                }
            ]

        });


//创建品牌数据表格
        var brandListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'brandListGrid',
                loadMask: true,
                forceFit: true,
                selType: 'checkboxmodel',
                store: brandListStore,
                columns: [
                     Ext.create('Ext.grid.RowNumberer',{text : '行号', width : 60}),
                    {
                        header: '品牌名称',
                        dataIndex: 'name'
                    },
                    {
                        header: '品牌编号',
                        dataIndex: 'code'
                    },
                    {
                        header: '供应商',
                        width:250,
                        dataIndex: 'supplierName'
                    },
                    {
                        header: '结算方式',
                        dataIndex: 'paymentType'
                    },
                    {
                        header: '结算规则',
                        dataIndex: 'paymentRule'
                    }

                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: brandListStore,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                }),
                listeners: {
                    'afterrender':function(grid){
                        grid.getStore().getProxy().extraParams = Ext.getCmp('searchBrand').getValues();
                    },
                    'render': function (input) {
                        var map = new Ext.util.KeyMap({
                            target: 'searchBrand',    //target可以是组建的id  加单引号
                            binding: [{                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function(){
                                    Espide.Common.reLoadGird('brandListGrid', 'searchBrand', true);
                                }
                            }]
                        });
                    },
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件
                        var formItems = [];
                        formItems.push({
                                fieldLabel: '品牌名称',
                                name: 'name',
                                value: record.get('name')
                            }
                        );


                        formItems.push({
                            xtype: 'combo',
                            name: 'supplierId',
                            itemId: 'supplierId',
                            id: 'supplierId',
                            fieldLabel: '供应商',
                            editable: false,
                            queryMode: 'local',
                            valueField: 'id',
                            displayField: 'name',
                            emptyText: '不限',
                            store: Espide.Common.createComboStore('/supplier/list',false)
                        });


                        formItems.push({
                            fieldLabel: '品牌编号',
                            name: 'code',
                            allowBlank:true,
                            hidden: false,
                            emptyText: '输入品牌编号',
                            value: record.get('code'),
//                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
//                            regexText: '首尾不得包含空格'
                        });

                        formItems.push({
                            fieldLabel: '结算方式',
                            emptyText: '输入结算方式',
                            name: 'paymentType',
                            allowBlank:true,
                            value: record.get('paymentType'),
//                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
//                            regexText: '首尾不得包含空格'
                        });

                        formItems.push({
                            fieldLabel: '结算规则',
                            emptyText: '输入结算规则',
                            allowBlank:true,
                            name: 'paymentRule',
                            value: record.get('paymentRule'),
//                            regex: /(^\S+.*\S+$)|(^\S{1}$)/,
//                            regexText: '首尾不得包含空格'
                        });





                        var brandUpdateForm = Ext.create('Ext.form.Panel', {
                            baseCls: 'x-plain',
                            labelWidth: 80,
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

                        Ext.getCmp('supplierId').setValue(record.get('supplierId'));

                        var winUpdate = Ext.create("Ext.window.Window", {
                            title: "修改品牌",
                            width: 500,
                            height: 230,
                            items: [brandUpdateForm],
                            buttonAlign: "right",
                            modal: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'updateBtn',
                                    handler: function () {
                                        if (brandUpdateForm.isValid()) {

                                            brandUpdateForm.submit({
                                                submitEmptyText:false,
                                                url: "/brand/update",
                                                params: {id: record.get("id"), name: record.get('name')},
                                                success: function (form, action) {
                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    if (data.success) {
                                                        Espide.Common.tipMsgIsCloseWindow(data, winUpdate,'brandListGrid', true);

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


        this.items = [searchForm, brandListGrid];
        this.callParent(arguments);
    }

})
;
