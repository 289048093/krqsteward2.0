/**
 * Created by Lein xu
 */
Ext.define('Contract.controller.Contract', {
    extend: 'Ext.app.Controller',
    views: ['ContractList'],
    stores: ['SupplierList', 'ContractList'],
    models: ['SupplierList', 'ContractList'],
    init: function () {
        this.control({
            //搜索
            '#searchBtn': {
                'click': function () {
                    var data = Ext.getCmp('search').getForm().getValues(), store = Ext.getCmp('contractListGrid').getStore();

                    var new_params = {
                        supplierId: data.supplierId,
                        code: data.code
                    };
                    store.getProxy().extraParams = new_params;
                    store.reload();
                }
            },
            //添加
            "#contractListGrid #add": {
                'click': function (button) {
                    var addProductForm = Ext.create('Ext.form.Panel', {
                        region: 'center',
                        id: 'addProductForm',
                        height: 'auto',
                        bodyPadding: 10,
                        border: 0,
                        layout: {
                            type: 'hbox',
                            align: 'left'
                        },
                        defaultType: 'fieldcontainer',
                        defaults: {
                            margin: '0 5 5 0',
                            defaults: {
                                xtype: 'textfield',
                                margin: '0 0 5 0',
                                labelWidth: 90,
                                labelAlign: 'right',
                                width: 320,
                                triggerAction: 'all',
                                allowBlank: false,
                                forceSelection: true,
                            }

                        },
                        items: [
                            {
                                items: [
                                    {
                                        name: 'supplierId',
                                        fieldLabel: '品牌商',
                                        xtype: 'combo',
                                        queryMode: "remote",
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        displayField: "name",
                                        valueField: 'id',
                                        emptyText:'请选择',
                                        blankText: '请选择',
                                        store: 'SupplierList'
                                    },
                                    {
                                        name: 'ejsCompName',
                                        emptyText:'请输入运营商',
                                        fieldLabel: '运营商',
                                        maxLength: 32
                                    },
                                    {
                                        name: 'code',
                                        fieldLabel: '合同编号',
                                        emptyText:'请输入合同编号',
                                        maxLength: 32
                                    },
                                    {
                                        xtype: 'datefield',
                                        name: 'beginTime',
                                        fieldLabel: '合同创建时间',
                                        emptyText:'请选择合同创建时间',
                                        format: 'Y-m-d'
                                    },
                                    {
                                        xtype: 'datefield',
                                        name: 'endTime',
                                        fieldLabel: '合同结束时间',
                                        emptyText:'请选择合同结束时间',
                                        editable:true,
                                        format: 'Y-m-d'
                                    },
                                    {
                                        xtype: 'datefield',
                                        name: 'realEndTime',
                                        fieldLabel: '合同终止时间',
                                        emptyText:'请选择合同终止时间',
                                        allowBlank:true,
                                        format: 'Y-m-d'
                                    },
                                    {
                                        name: 'endReason',
                                        fieldLabel: '合同终止原因',
                                        emptyText:'请输入合同终止原因(可为空)',
                                        allowBlank:true,
                                        maxLength: 32
                                    },

                                    {
                                        name: 'overdueFine',
                                        fieldLabel: '滞纳金情况',
                                        emptyText:'请输入滞纳金情况(可为空)',
                                        allowBlank:true,
                                        maxLength: 32
                                    },
                                    {
                                        name: 'serviceFee',
                                        fieldLabel: '技术服务费/年',
                                        emptyText:'请输入技术服务费/年',
                                        xtype: 'numberfield',
                                        minValue: 0,
                                        negativeText: '不得输入负数',
                                        maxLength: 32
                                    },
                                    {
                                        name: 'deposit',
                                        fieldLabel: '保证金',
                                        emptyText:'请输入保证金',
                                        xtype: 'numberfield',
                                        minValue: 0,
                                        negativeText: '不得输入负数',
                                        maxLength: 32
                                    },

                                    {
                                        name: 'commission',
                                        emptyText:'请输入佣金补差',
                                        fieldLabel: '佣金补差',
                                        maxLength: 32
                                    },
                                ]
                            },
                            {
                                items: [


                                    {
                                        name: 'paymentRule',
                                        emptyText:'请输入结算规则',
                                        fieldLabel: '结算规则',
                                        labelWidth: 100,
                                        maxLength: 32
                                    },

                                    {
                                        name: 'paymentType',
                                        emptyText:'请输入结算类型',
                                        fieldLabel: '结算类型',
                                        labelWidth: 100,
                                        maxLength: 32
                                    },
                                    {
                                        name: 'shippingFeeType',
                                        emptyText:'请输入物流补贴',
                                        fieldLabel: '物流补贴',
                                        labelWidth: 100,
                                        maxLength: 32
                                    },
                                    {
                                        name: 'shotFeeType',
                                        emptyText:'请输入拍摄费用',
                                        fieldLabel: '拍摄费用',
                                        labelWidth: 100,
                                        maxLength: 32
                                    },

                                    {
                                        name: 'invoiceEJSTitle',
                                        emptyText:'请输入开具发票给顾客',
                                        fieldLabel: '开具发票给顾客',
                                        labelWidth: 100,
                                        maxLength: 32
                                    },
                                    {
                                        name: 'invoiceOtherTitle',
                                        emptyText:'请输入第三方平台销售发票',
                                        fieldLabel: '第三方平台销售发票',
                                        labelWidth: 130,
                                        maxLength: 32
                                    },
                                    {
                                        name: 'invoiceToEJS',
                                        emptyText:'请输入第三方平台销售是否补开发票给易居尚',
                                        fieldLabel: '第三方平台销售是否补开发票给易居尚',
                                        maxLength: 32
                                    },
                                    {
                                        name: 'otherRule',
                                        fieldLabel: '其他条款',
                                        emptyText:'请输入其他条款(可为空)',
                                        xtype: 'textareafield',
                                        allowBlank:true,
                                        maxLength: 512,
                                        height: 50
                                    },
                                    {
                                        name: 'remark',
                                        fieldLabel: '补充协议',
                                        emptyText:'请输入补充协议(可为空)',
                                        xtype: 'textareafield',
                                        allowBlank:true,
                                        maxLength: 512,
                                        height: 50
                                    }

                                ]
                            }


                        ]

                    });


                    var addProductWin = Ext.create("Ext.window.Window", {
                        title: '添加合同',
                        width: 720,
                        height: 480,
                        modal: true,
                        autoHeight: true,
                        layout: 'fit',
                        buttonAlign: 'right',
                        bodyStyle: 'padding:5px;',
                        animateTarget: Ext.getBody(),
                        items: addProductForm,
                        buttons: [
                            {
                                text: "保存",
                                handler: function () {
                                    var form = addProductForm.getForm();
                                    var data = form.getValues();
                                    if (form.isValid()) {
                                        form.submit({
                                            submitEmptyText:false,
                                            url: "/contract/save",
                                            params: {

                                            },
                                            success: function (response, options) {
                                                var data = Ext.JSON.decode(options.response.responseText);
                                                if (data.success) {
                                                    addProductWin.close();
                                                    Espide.Common.tipMsg('保存成功', data.msg);
                                                    Ext.getCmp('contractListGrid').getStore().load();
                                                }
                                            },
                                            failure: function (response, options) {

                                                var data = Ext.JSON.decode(options.response.responseText);

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
                    addProductWin.show();
                }
            },
            //修改
            "#contractListGrid": {

                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.reLoadGird('contractListGrid', 'search', true);
                                }
                            }
                        ]
                    });
                },
                'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件
                    var updateGoodsForm = Ext.create('Ext.form.Panel', {
                        region: 'center',
                       // id: 'updateGoodsForm',
                        height: 'auto',
                        bodyPadding: 10,
                        layout: {
                            type: 'hbox',
                            align: 'left'
                        },
                        defaultType: 'fieldcontainer',
                        border: 0,
                        defaults: {
                            margin: '0 5 5 0',
                            defaults: {
                                xtype: 'textfield',
                                margin: '0 0 5 0',
                                labelWidth: 90,
                                labelAlign: 'right',
                                width: 320,
                                queryMode: 'local',
                                triggerAction: 'all',
                                allowBlank: false,
                                forceSelection: true
                            }

                        },
                        items: [
                            {
                                items: [
                                    { name: 'id', value: record.get("id"),hidden:true},
                                    {
                                        name: 'supplierId',
                                        id: 'supplierCom',
                                        fieldLabel: '品牌商',
                                        xtype: 'combo',
                                        queryMode: "remote",
                                        triggerAction: 'all',
                                        editable: false,
                                        displayField: "name",
                                        value: 'name',
                                        valueField: 'id',
                                        store: 'SupplierList'
                                    },
                                    { name: 'ejsCompName', fieldLabel: '运营商', value: record.get("ejsCompName")},
                                    { name: 'code', fieldLabel: '合同编号', value: record.get("code")},
                                    { name: 'beginTime', xtype: 'datefield', fieldLabel: '合同创建时间', format: 'Y-m-d', value: record.get("beginTime")},
                                    { name: 'endTime', xtype: 'datefield', fieldLabel: '合同结束时间', format: 'Y-m-d', value: record.get("endTime")},
                                    { name: 'realEndTime', xtype: 'datefield', fieldLabel: '合同终止时间', format: 'Y-m-d', value: record.get("realEndTime"),allowBlank:true},
                                    { name: 'endReason', fieldLabel: '合同终止原因', value: record.get("endReason"),allowBlank:true,
                                        emptyText:'请输入合同终止原因(可为空)',
                                    },
                                    {
                                        name: 'overdueFine',
                                        fieldLabel: '滞纳金情况',
                                        allowBlank:true,
                                        editable: true,
                                        value: record.get("overdueFine"),
                                        emptyText:'请输入滞纳金情况(可为空)',
                                        minValue: 0,
                                        negativeText: '不得输入负数'
                                    },
                                    {
                                        name: 'serviceFee',
                                        fieldLabel: '技术服务费/年',
                                        xtype: 'numberfield',
                                        editable: true,
                                        value: record.get("serviceFee"),
                                        minValue: 0,
                                        negativeText: '不得输入负数'
                                    },
                                    {
                                        name: 'deposit',
                                        fieldLabel: '保证金',
                                        xtype: 'numberfield',
                                        editable: true,
                                        value: record.get("deposit"),
                                        minValue: 0,
                                        negativeText: '不得输入负数'
                                    },
                                    {
                                        name: 'commission',
                                        fieldLabel: '佣金补差',

                                        editable: true,
                                        value: record.get("commission"),
                                        minValue: 0,
                                        negativeText: '不得输入负数'
                                    },


                                ]
                            },
                            {
                                items: [
                                    {
                                        name: 'paymentType',
                                        fieldLabel: '结算类型',
                                        editable: true,
                                        value: record.get("paymentType"),
                                    },
                                    {
                                        name: 'paymentRule',
                                        fieldLabel: '结算规则',

                                        editable: true,
                                        value: record.get("paymentRule"),
                                        minValue: 0,
                                        negativeText: '不得输入负数'
                                    },
                                    {
                                        name: 'shippingFeeType',
                                        fieldLabel: '物流补贴',
                                        editable: true,
                                        value: record.get("shippingFeeType"),
                                    },
                                    {
                                        name: 'shotFeeType',
                                        fieldLabel: '拍摄费用',
                                        editable: true,
                                        value: record.get("shotFeeType"),
                                    },
                                    { name: 'invoiceEJSTitle', fieldLabel: '开具发票给顾客', value: record.get("invoiceEJSTitle")},
                                    { name: 'invoiceOtherTitle', fieldLabel: '第三方平台销售发票', value: record.get("invoiceOtherTitle")},
                                    { name: 'invoiceToEJS', fieldLabel: '第三方平台销售是否补开发票给易居尚', value: record.get("invoiceToEJS")},
                                    { name: 'otherRule', fieldLabel: '其他条款', xtype: 'textareafield',height:50, value: record.get("otherRule"),allowBlank:true,
                                        emptyText:'请输入其他条款(可为空)',
                                    },
                                    { name: 'remark', fieldLabel: '补充协议', xtype: 'textareafield',height:50, value: record.get("remark"),allowBlank:true,
                                        emptyText:'请输入补充协议(可为空)',
                                    }
                                ]
                            }


                        ]
                    });




                    var updateGoodListWin = Ext.create("Ext.window.Window", {
                        title: '修改合同',
                        width: 720,
                        height: 480,
                        modal: true,
                        autoHeight: true,
                        layout: 'fit',
                        buttonAlign: 'right',
                        bodyStyle: 'padding:5px;',
                        animateTarget: Ext.getBody(),
                        items: updateGoodsForm,
                        buttons: [
                            {
                                text: "保存",
                                handler: function () {
                                    var form = updateGoodsForm.getForm();
                                    var data = form.getValues();
                                    if (form.isValid()) {
                                        form.submit({
                                            submitEmptyText:false,
                                            url: "/contract/update",
                                            params: data,
                                            success: function (response, options) {
                                                var data = Ext.JSON.decode(options.response.responseText);
                                                if (data.success) {//修改成功
                                                    updateGoodListWin.close();
                                                    Espide.Common.tipMsg('修改成功', data.msg);
                                                    Ext.getCmp('contractListGrid').getStore().load();
                                                }
                                            },
                                            failure: function (response, options) {

                                                var data = Ext.JSON.decode(options.response.responseText);
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

                    updateGoodListWin.show(this,function(){
                        Ext.getCmp('supplierCom').setValue(record.get('supplierId'));
                    });


                }
            },
            //删除
            '#del': {
                'click': function (button) {

                    var url = '/contract/delete',
                        ids = Espide.Common.getGridSels('contractListGrid', 'id');

                    if (ids.length < 1) {
                        Espide.Common.showGridSelErr('请先选择要删除的合同');
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
            },
            //刷新
            "#refresh": {
                'click': function (button) {
                    Ext.getCmp('contractListGrid').getStore().load();
                }
            },
            //导入合同
            "#include": {
                'click': function (button) {
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
                                labelWidth: 80,
                                width: 300,
                                name: "multipartFile",
                                fieldLabel: "导入文件",
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
                                                location.href = "/static/templet/contractExcelModel.xls";
                                            }
                                        }
                                    }
                                ]}
                        ]


                    });

                    var includeWin = Ext.create("Ext.window.Window", {
                        title: '导入合同',
                        width: 500,
                        height: 150,
                        modal: true,
                        autoHeight: true,
                        layout: 'fit',
                        buttonAlign: 'right',
                        bodyStyle: 'padding:5px;',
                        animateTarget: Ext.getBody(),
                        items: uploadForm,
                        buttons: [
                            {
                                text: "确认导入",
                                handler: function () {
                                    var form = uploadForm.getForm();
                                    if (form.isValid()) {
                                        form.submit({
                                            url: "/contract/leadingIn",
                                            waitMsg: "正在导入验证数据",
                                            success: function (fp, o) {
                                                var data = Ext.JSON.decode(o.response.responseText);
                                                if (data.success) {
                                                    Espide.Common.tipMsgIsCloseWindow(data, includeWin, 'contractListGrid', true);
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
            }
        });
    }



});