/**
 * Created by king on 13-12-17
 */

Ext.define('Stock.controller.Stock', {
    extend: 'Ext.app.Controller',
    views: ['List'],
    stores: ['List'],
    models: ['List'],
    init: function () {
        this.control({
            '#stockListGrid': {
                edit: this.updateStockNumber,
                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'stockListSearch',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.reLoadGird('stockListGrid', 'stockListSearch', true);
                                }
                            }
                        ]
                    });
                }
            },
            //查询
            '#stockListSearch #searchBtn': {
                'click': function () {
                    Espide.Common.doSearch('stockListGrid', 'stockListSearch', true);
                }
            },
            //入库
            '#stockAdd': {
                'click': function () {
                    var com = Espide.Common;

                    if (!com.checkGridSel('stockListGrid', '请至少选择一项产品'))  return;

                    var productId = com.getGridSels('stockListGrid', 'productId').join(',');
                    var repositoryId = com.getGridSels('stockListGrid', 'repositoryId').join(',');
                    var repositoryId = com.getGridSels('stockListGrid', 'repositoryId').join(',');
                    var repoName = com.getGridSels('stockListGrid', 'repoName').join(',');
                    var prodName = com.getGridSels('stockListGrid', 'prodName').join(',');
                    var amount = com.getGridSels('stockListGrid', 'amount').join(',');
                    //this.getGridSels(gridId, "id");

                    var formItems = [];

                    formItems.push({
                            fieldLabel: '产品名',
                            disabled:true,
                            name: 'prodName',
                            value: prodName,

                        }
                    );
                    formItems.push({
                            fieldLabel: '仓库名',
                            disabled:true,
                            name: 'repoName',
                            value: repoName
                        }
                    );
                    formItems.push({
                            fieldLabel: '当前库存',
                            name: 'amount',
                            disabled:true,
                            value: amount
                        }
                    );
                    formItems.push({
                            fieldLabel: '入库数量',
                            name: 'num',
                            emptyText: '请输入入库数量',
                            xtype: 'numberfield',
                            editable: true,
                            minValue: 1
                        }
                    );
                    formItems.push({
                            xtype: 'combo',
                            fieldLabel: '入库类型',
                            name: 'inStockType',
                            editable:false,
                            value: 'IN_STOCK_TYPE_PROCURE',
                            store: [
                                ['IN_STOCK_TYPE_PROCURE', '采购入库'],
                                ['IN_STOCK_TYPE_ALLOT', '调拨入库'],
                                ['IN_STOCK_TYPE_CHECK', '盘点入库'],
                                ['IN_STOCK_TYPE_RETURNS', '退货入库'],
                                ['IN_STOCK_TYPE_MAINTAIN', '维修入库'],
                                ['IN_STOCK_TYPE_OTHER', '其他']
                            ]

                        }
                    );

                    formItems.push({
                            fieldLabel: '备注',
                            xtype: 'textareafield',
                            allowBlank:true,
                            emptyText:'请填写入库备注(可为空)',
                            name: 'desc',
                            maxLength:255
                        }
                    );
                    formItems.push({
                            xtype: 'hidden',
                            name: 'productId',
                            value: productId
                        }
                    );
                    formItems.push({
                            xtype: 'hidden',
                            name: 'repositoryId',
                            value: repositoryId
                        }
                    );


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
                        width: 300,
                        requires: ['Ext.form.field.Text'],
                        fieldDefaults: {
                            msgTarget: 'qtip',
                            blankText: '不能为空',
                            allowBlank: false,
                            labelAlign: "right",
                            labelSeparator: "：",
                            labelWidth: 75
                        },
                        defaultType: 'textfield',
                        items: formItems

                    });

                    //创建一个弹窗容器
                    var accountAddWin = Ext.create("Ext.window.Window", {
                        title: '入库',
                        width: 320,
                        height: 320,
                        modal: true,
                        autoScroll: true,
                        plain: true,
                        animateTarget: Ext.getBody(),
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

                                        addForm.submit({
                                            submitEmptyText:false,
                                            clientValidation: true, //对客户端进行验证
                                            url: "/storage/storage_increment",
                                            method: "post",
                                            params: {
                                                productId: datas[":productId"],
                                                repositoryId: datas["repositoryId"],
                                                num: datas["num"],
                                                inStockType: datas["inStockType"],
                                                desc: datas['desc']
                                            },
                                            success: function (form, action) {

                                                var data = Ext.JSON.decode(action.response.responseText);
                                                if (data.success) {
                                                    Espide.Common.tipMsg('保存成功', data.msg);
                                                    accountAddWin.close();
                                                    Ext.getCmp('stockListGrid').getStore().load();
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
                    accountAddWin.show();
                }
            },
            //出库
            '#stockOut': {
                'click': function () {

                    var com = Espide.Common;

                    if (!com.checkGridSel('stockListGrid', '请至少选择一项产品'))  return;

                    var productId = com.getGridSels('stockListGrid', 'productId').join(',');
                    var repositoryId = com.getGridSels('stockListGrid', 'repositoryId').join(',');
                    var repositoryId = com.getGridSels('stockListGrid', 'repositoryId').join(',');
                    var repoName = com.getGridSels('stockListGrid', 'repoName').join(',');
                    var prodName = com.getGridSels('stockListGrid', 'prodName').join(',');
                    var amount = com.getGridSels('stockListGrid', 'amount').join(',');
                    //this.getGridSels(gridId, "id");

                    var formItems = [];


                    formItems.push({
                            fieldLabel: '产品名',
                            disabled:true,
                            name: 'prodName',
                            value: prodName
                        }
                    );


                    formItems.push({
                            fieldLabel: '仓库名',
                            disabled:true,
                            name: 'repoName',
                            value: repoName
                        }
                    );
                    formItems.push({
                            fieldLabel: '当前库存',
                            disabled:true,
                            name: 'amount',
                            value: amount
                        }
                    );
                    formItems.push({
                            xtype: 'numberfield',
                            fieldLabel: '出库数量',
                            emptyText:'请输入出库数量',
                            name: 'num',
                            editable: true,
                            maxValue: amount,
                            minValue: 1
                        }
                    );
                    formItems.push({
                            xtype: 'combo',
                            fieldLabel: '入库类型',
                            name: 'outStockType',
                            editable:false,
                            value: 'OUT_STOCK_TYPE_SCRAP',
                            store: [
                                ['OUT_STOCK_TYPE_SCRAP', '报废出库'],
                                ['OUT_STOCK_TYPE_DIFFERENCE', '差错出库'],
                                ['OUT_STOCK_TYPE_ALLOT', '调拨出库'],
                                ['OUT_STOCK_TYPE_RECEIVE', '领用出库'],
                                ['OUT_STOCK_TYPE_CHECK', '盘点出库'],
                                ['OUT_STOCK_TYPE_RETURNS', '退货出库'],
                                ['OUT_STOCK_TYPE_LOGISTICS_BREAK', '物流损坏'],
                                ['OUT_STOCK_TYPE_SELL', '销售出库'],
                                ['OUT_STOCK_TYPE_GIFT', '赠品出库'],
                                ['OUT_STOCK_TYPE_OTHER', '其他'],
                            ]

                        }
                    );

                    formItems.push({
                            fieldLabel: '备注',
                            xtype: 'textareafield',
                            emptyText:'请填写出库备注(可为空)',
                            allowBlank:true,
                            name: 'desc',
                            maxLength:255
                        }
                    );
                    formItems.push({
                            xtype: 'hidden',
                            name: 'productId',
                            value: productId
                        }
                    );
                    formItems.push({
                            xtype: 'hidden',
                            name: 'repositoryId',
                            value: repositoryId
                        }
                    );


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
                        width: 300,
                        requires: ['Ext.form.field.Text'],
                        fieldDefaults: {
                            msgTarget: 'qtip',
                            blankText: '不能为空',
                            allowBlank: false,
                            labelAlign: "right",
                            labelSeparator: "：",
                            labelWidth: 75
                        },
                        defaultType: 'textfield',
                        items: formItems

                    });

                    //创建一个弹窗容器
                    var outAddWin = Ext.create("Ext.window.Window", {
                        title: '出库',
                        width: 320,
                        height: 320,
                        modal: true,
                        autoScroll: true,
                        plain: true,
                        animateTarget: Ext.getBody(),
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

                                        addForm.submit({
                                            submitEmptyText:false,
                                            clientValidation: true, //对客户端进行验证
                                            url: "/storage/storage_reduce",
                                            method: "post",
                                            params: {
                                                productId: datas[":productId"],
                                                repositoryId: datas["repositoryId"],
                                                num: datas["num"],
                                                outStockType: datas["inStockType"],
                                                desc: datas['desc']
                                            },
                                            success: function (form, action) {

                                                var data = Ext.JSON.decode(action.response.responseText);
                                                if (data.success) {

                                                    Espide.Common.tipMsg('保存成功', data.msg);
                                                    outAddWin.close();
                                                    Ext.getCmp('stockListGrid').getStore().load();


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
                    outAddWin.show();
                }

            },
            //导入
            '#stockImport': {
                'click': function () {
                    var uploadForm = Ext.create("Ext.form.Panel", {
                        region: 'north',
                        border: 0,
                        bodyPadding: 10,
                        defaults: {
                            width: 300
                        },
                        layout: {
                            type: 'hbox',
                            align: 'left'
                        },
                        height: 'auto',
                        height: 70,
                        split: true,
                        items: [
                            {
                                xtype: 'button',
                                width: 80,
                                cls: 'contactBtn',
                                margin: "0 20px 0 0",
                                text: '下载模板',
                                listeners: {
                                    'click': function () {
                                        location.href = "storage/download_template";
                                    }
                                }
                            },
                            {
                                xtype: "filefield",
                                name: "uploadFile",
                                fieldLabel: "导入Excel",
                                labelWidth: 80,
                                margin: "0 10px 0 0",
                                anchor: "100%",
                                id: "uploadFile",
                                allowBlank: false,
                                blankText: '文件不能为空',
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
                                xtype: 'button',
                                width: 80,
                                cls: 'contactBtn',
                                margin: "0 10px 0 0",
                                text: '导入文件',
                                handler: function () {
                                    var form = uploadForm.getForm();
                                    if (form.isValid()) {
                                        form.submit({
                                            url: "storage/batch_update",
                                            waitMsg: "正在导入数据",
                                            success: function (fp, o) {
                                                var data = Ext.JSON.decode(o.response.responseText);

                                                if (data.success) {
                                                    Espide.Common.tipMsgIsCloseWindow(data, includeWin, 'stockListGrid', true);
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
                            },
                            {
                                xtype: 'button',
                                width: 80,
                                cls: 'contactBtn',
                                margin: "0 10px 0 0",
                                text: '预览数据',
                                handler: function () {
                                    var form = uploadForm.getForm();
                                    if (form.isValid()) {
                                        form.submit({
                                            url: "storage/preview_batch_update",
                                            waitMsg: "正在导入验证数据",
                                            success: function (fp, o) {
                                                var data = Ext.JSON.decode(o.response.responseText);
                                                if (data.success) {
                                                    Espide.Common.tipMsg('操作成功', data.msg);
                                                    mystore.loadRawData(data.data.list);
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

                            },
                            {
                                xtype: 'button',
                                width: 80,
                                cls: 'contactBtn',
                                margin: "0 10px 0 0",
                                text: '强制更新',
                                handler: function () {
                                    var form = uploadForm.getForm();
                                    if (form.isValid()) {
                                        form.submit({
                                            url: "storage/force_batch_update",
                                            waitMsg: "正在更新数据",
                                            success: function (fp, o) {
                                                var data = Ext.JSON.decode(o.response.responseText);

                                                if (data.success) {
                                                    Espide.Common.tipMsgIsCloseWindow(data, includeWin, 'stockListGrid', true);
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

                    //预览时候的表格
                    Ext.define('EBDesktop.prevGrid', {
                        extend: 'Ext.data.Model',
                        fields: [
                            'id',
                            'repository',
                            'product',
                            {name: 'repoName', type: 'string', mapping: 'repository.name'},
                            {name: 'brandName', type: 'string', mapping: 'product.brand.name'},
                            {name: 'prodCaName', type: 'string', mapping: 'product.category.name'},
                            {name: 'prodName', type: 'string', mapping: 'product.name'},
                            {name: 'prodNo', type: 'string', mapping: 'product.productNo'},
                            {name: 'sku', type: 'string', mapping: 'product.sku'},
                            {name: 'shopPrice', type: 'string', mapping: 'product.shopPrice'},
                            {name: 'standardPrice', type: 'string', mapping: 'product.standardPrice'},
                            {name: 'color', type: 'string', mapping: 'product.color'},
                            {name: 'boxSize', type: 'string', mapping: 'product.boxSize'},
                            'amount',
                            'beforeAmount',
                            'afterAmount',
                            {name: 'description', type: 'string', mapping: 'product.description'},
                            'statusMsg'
                        ],
                        idProperty: 'id'
                    });

                    var mystore = new Ext.data.Store({
                        model: 'EBDesktop.prevGrid',
                        autoLoad: false
                    });

                    var prevGrid = new Ext.grid.Panel({
                        region: 'south',
                        width: 480,
                        height: 170,
                        store: 'List',
                        id: 'grid',
                        selType: 'checkboxmodel',
                        store: mystore,
                        viewConfig: {
                            forceFit: false,
                            getRowClass: function (record, rowIndex, rowParams, store) {
                                if (record.get('statusMsg') != '可以更新') {
                                    return 'x-grid-record-red';
                                }
                            }
                        },
                        columns: [
                            {
                                text: '数据校验',
                                dataIndex: 'statusMsg',
                                width: 300,
                                renderer: function (value, cellmeta) {
                                    if (value == '可以更新') {
                                           return '可以更新'
                                    } else {
                                        return value;
                                    }
                                }
                            },
                            {
                                text: '商品名',
                                dataIndex: 'prodName'
                            },
                            {
                                text: '品牌',
                                dataIndex: 'brandName'
                            },
                            {
                                text: '商品编号',
                                dataIndex: 'prodNo',
                                width: 100
                            },
                            {
                                text: '规格',
                                dataIndex: 'boxSize',
                                width: 120
                            },
                            {
                                text: '颜色',
                                dataIndex: 'color',
                                width: 120
                            },
                            {
                                text: '条形码',
                                dataIndex: 'sku',
                                width: 120
                            },
                            {
                                text: '现库存',
                                dataIndex: 'beforeAmount',
                                width: 120
                            },
                            {
                                text: '导入库存',
                                dataIndex: 'afterAmount',
                                width: 120
                            },
                            {
                                text: '库存差',
                                dataIndex: 'amount',
                                width: 120
                            },
                            {
                                text: '仓库名',
                                dataIndex: 'repoName',
                                width: 120
                            }
                        ]
                    });


                    var includeWin = Ext.create("Ext.window.Window", {
                        title: '批量更新库存',
                        animateTarget: Ext.getBody(),
                        width: 720,
                        height: 300,
                        resizable: false,
                        modal: true,
                        autoHeight: true,
                        fixed: true,
                        layout: "border",
                        buttonAlign: 'right',
                        bodyStyle: 'padding:5px;',
                        items: [uploadForm, prevGrid]
                    });

                    includeWin.show();
                }

            }

        });
    },

// 更新库存数量
    updateStockNumber: function (editor, e) {
        var params = {},
            prodName = e.record.get('prodName'),
            originalValue = e.originalValue,
            msg;

        // 判断库存数量是否有变动
        if (originalValue == e.value) {
            return;
        }

        params.id = e.record.get('id');
        params[e.field] = e.value;

        msg = '您确定要修改商品名为 <strong style="color: red">' + prodName + '</strong> 的库存吗？' +
            '<br>原始库存为 <strong style="color: red">' + originalValue + '</strong> ' +
            '<br>修改后库存将变更为 <strong style="color: green">' + e.value + '</strong> ';

        // 执行更新操作前弹出确认提醒
        Ext.MessageBox.confirm('系统提示', msg, function (optional) {

            if (optional !== 'yes') {
                e.record.data.actuallyNumber = originalValue;
                Ext.getDom(e.row.cells[e.colIdx].firstChild.id).innerHTML = originalValue;
                return;
            }

            // 执行更新操作
            Ext.Ajax.request({
                url: '/storage/update',
                params: params,
                success: function (response) {
                    var data = Ext.decode(response.responseText);
                    if (data.success) {
                        Espide.Common.tipMsg('成功', '修改成功');
                    } else {
                        Ext.MessageBox.show({
                            title: '错误',
                            msg: data.msg,
                            buttons: Ext.Msg.YES,
                            icon: Ext.Msg.WARNING
                        });
                    }
                },
                failure: function () {
                    Ext.Msg.show({
                        title: '错误',
                        msg: '服务器错误，请重新提交!',
                        buttons: Ext.Msg.YES,
                        icon: Ext.Msg.WARNING
                    });
                }
            });
        });
    }

})
;