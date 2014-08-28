/**
 * Created by king on 13-12-17
 */
Ext.define('Customer.controller.Order', {
    extend: 'Ext.app.Controller',
    views: ['Customer', 'Search', 'List', 'Item', 'Batch', 'AutoNumber'],
    stores: ['List', 'ItemList', 'StorageAll','LabelStore','BackList'],
    models: ['List', 'ItemList'],
    init: function () {
        this.control({
            //导出订单
            '#outOrder': {
                click: function (btn) {
                    var params = Ext.getCmp('search').getForm().getValues();
                    window.open('invoice/list_invoice_report_orders?' + Ext.Object.toQueryString(params));
                }
            },
            '#addTag':{
                click:function(btn){

                    if(!Espide.Common.isGridSel('List')){
                        Espide.Common.showGridSelErr('必须选择一条会员数据');
                        return;
                    }

                    var ids = Espide.Common.getGridSelsId('List','id');


                    Ext.Ajax.request({
                        url: "/customerTag/listNoPage",
                        success:function(response, options){

                            var data  = eval('('+response.responseText+')').data.list,items = [];

                            Ext.each(data,function(dataItem,i){

                                    items.push({boxLabel:dataItem.name,name:'tagId',inputValue:dataItem.id});

                            });


                            var formItems = [];
                            formItems.push({

                                    name: 'customerIds',
                                    hidden:true,
                                    value: ids
                                }
                            );

                            formItems.push(
                                {
                                    xtype: 'checkboxgroup',
                                    fieldLabel: '会员标签',
                                    columns: 2,
                                    vertical: true,
                                    items:items
                                }
                            );



                            var tagForm = Ext.create('Ext.form.Panel', {
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



                            var tagWin = Ext.create("Ext.window.Window", {
                                title: "批量添加会员标签",
                                width: 500,
                                height: 630,
                                items: [tagForm],
                                buttonAlign: "right",
                                modal: true,
                                buttons: [
                                    {
                                        text: '保存',
                                        itemId: 'updateBtn',
                                        handler: function () {
                                            if (tagForm.isValid()) {
                                                var FormData =  tagForm.getForm().getValues();
                                                FormData.tagIds =  Array.isArray(FormData.tagId) ? FormData.tagId.join(",") : FormData.tagId;

                                                tagForm.submit({
                                                    submitEmptyText:false,
                                                    method:'get',
                                                    url: "/customer/addTagToCustomers",
                                                    params: FormData,
                                                    success: function (form, action) {
                                                        var data = Ext.JSON.decode(action.response.responseText);
                                                        if (data.success) {
                                                            Espide.Common.tipMsgIsCloseWindow(data, tagWin,'List', true);
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
                            tagWin.show();
                        }
                    });

                }
            },
            '#TotalOutOrder': {
                click: function (btn) {

                    //判断是否选择了订单
                    var selectedOrder = Ext.getCmp('OrderList').getSelectionModel().getSelection();

                    if (selectedOrder.length > 0) {
                        var com = Espide.Common,
                            orderIds = com.getGridSelsId('OrderList').join(',');
                        window.open('/invoice/collect_invoice_order_excel?orderIds=' + orderIds);
                    } else {
//                        Ext.MessageBox.show({
//                            title: '提示',
//                            msg: '导出汇总前请先选择订单！',
//                            buttons: Ext.MessageBox.OK,
//                            icon: 'x-message-box-error'
//                        });
//                        return;
                        var params = Ext.getCmp('search').getForm().getValues();
                        var url = '/invoice/collect_invoice_order_excel?orderIds=null&'+Ext.Object.toQueryString(params);
                        window.open(url);
                    }
                }
            },

            'viewport': {
                afterrender: this.checkIsInstallLODOP
            },
            //0-订单四种状态切换
            "#search button[belong=mainBtn]": {
                click: function (button) {

                    Ext.getCmp('orderItem').hide();

                    var id = button.getItemId(),
                        btnId = button.itemId,
                        root = this,
                        orderGridToolBar = Ext.getCmp('search').getDockedItems('toolbar[dock="top"]')[0];

                    //切换一级菜单状态
                    Ext.each(orderGridToolBar.items.items, function (item, index, items) {
                        //flag = (item === button);
                        //全部按钮 禁用  防止  异步把订单 交叉
                        item.setDisabled(true);
                    });

                    //对应改变form中四个一级菜单影像的状态
                    root.setOrderState(id);

                    //对应切换二级菜单的状态
                    root.changeSubBtn(id);

                    //刷新订单grid数据
                    Espide.Common.reLoadGird('OrderList', 'search', true, function (records, options, success) {
                        //全部按钮 启用
                        Ext.each(orderGridToolBar.items.items, function (item, index, items) {
                            //按钮替换
                            if (btnId == item.itemId) {
                                item.setDisabled(true);
                            } else {
                                item.enable(true);
                            }
                        });

                    });


                }
            },

            //2.4-批量改快递公司
            "#batEditState": {
                click: function () {

                    if (!Espide.Common.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    // if (!this.checkOrderState('OrderList')) return;


                    //如果"批量改状态"视图已绘制就返回
                    if (Ext.getCmp('batchStateWin')) return false;


                    Ext.widget('batchState').show();
                }
            },


            //2.5-批量改快递公司确定按钮
            "#batchStateWin #comfirm": {
                click: function (btn) {
                    var com = Espide.Common,
                        params = btn.up('form').getValues();

                    if (com.isEmptyObj(params)) {
                        Ext.Msg.show({
                            title: '错误',
                            msg: "请至少选择一项状态",
                            buttons: Ext.Msg.YES,
                            icon: Ext.Msg.WARNING
                        });
                        return;
                    }

                    params['orderIds'] = com.getGridSelsId('OrderList').join(',');
                    com.commonMsg({
                        msg: '你确定要批量修改订单吗，修改订单后不可复原!',
                        fn: com.doAction({
                            //url: '/order/updateStautsByOrder',
                            url: '/order/invoiceUpdateStautsByOrder',
                            params: params,
                            successTipMsg: '订单批量修改成功',
                            successCall: function () {
                                //tbar 物流公司与 联想单号物流同步
                                Ext.getCmp('shippingCp').setValue(Ext.getCmp('shippingComp').getValue());
                                com.reLoadGird('OrderList', 'search', true,function(){
                                    //重新选择
                                    var task = new Ext.util.DelayedTask(function(){
                                        Ext.getCmp('OrderList').selModel.selectAll(true);
                                    });
                                    task.delay(2);
                                });

                                Ext.getCmp('orderItem').hide();
                                Ext.getCmp('batchStateWin').destroy();
                            }
                        })
                    });
                }
            },

            //2.6-批量改状态取消按钮
            "#batchStateWin #cancel": {
                click: function () {
                    Ext.getCmp('batchStateWin').destroy();
                }
            },

            //1-表格操作
            "#list": {
                itemdblclick: function (grid,record) {


                    ///customerTag/listNoPage

                    Ext.Ajax.request({
                        url: "/customerTag/listNoPage",
                        success:function(response, options){
                            console.log(record.get('tags'));
                            var data  = eval('('+response.responseText+')').data.list,TagItems = [],items = [];

                            Ext.each(record.get('tags'),function(TagItem,j){
                                TagItems.push(TagItem.name);

                            });


                            Ext.each(data,function(dataItem,i){
                                if(Ext.Array.contains(TagItems,dataItem.name)){
                                    items.push({boxLabel:dataItem.name, name:'tagId',inputValue:dataItem.id,checked: true});
                                }else{
                                    items.push({boxLabel:dataItem.name,name:'tagId',inputValue:dataItem.id});
                                }
                            });


                            var formItems = [];
                            formItems.push({

                                    name: 'id',
                                    hidden:true,
                                    value: record.get('id')
                                }
                            );
                            formItems.push({
                                    fieldLabel: '真实姓名',
                                    name: 'realName',
                                    value: record.get('realName')
                                }
                            );


                            formItems.push({
                                    fieldLabel: '会员ID',
                                    disabled: true,
                                    name: 'buyerId',
                                    value: Ext.util.Format.stripTags(record.get('buyerId'))
                                }
                            );
                            formItems.push({
                                    fieldLabel: '会员等级',
                                    disabled: true,
                                    name: 'grade',
                                    value: record.get('grade')
                                }
                            );
                            formItems.push({
                                    fieldLabel: '会员积分',
                                    disabled: true,
                                    name: 'bonusPoint',
                                    value: record.get('bonusPoint')
                                }
                            );
                            formItems.push({
                                    fieldLabel: '电话',
                                    name: 'phone',
                                    value: record.get('phone')
                                }
                            );

                            formItems.push({
                                    fieldLabel: '手机',
                                    name: 'mobile',
                                    value: record.get('mobile')
                                }
                            );

                            formItems.push({
                                    fieldLabel: '邮箱',
                                    name: 'email',
                                    value: record.get('email')
                                }
                            );

                            formItems.push({
                                    fieldLabel: '生日',
                                    xtype:'datefield',
                                    name: 'birthday',
                                    format: 'Y-m-d',
                                    value: record.get('birthday')
                                }
                            );

                            formItems.push({
                                    fieldLabel: '地址',
                                    name: 'address',
                                    value: record.get('address')
                                }
                            );


                            formItems.push(
                                {
                                    xtype: 'checkboxgroup',
                                    fieldLabel: '会员标签',
                                    columns: 2,
                                    vertical: true,
                                    items:items
                                }
                            );



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



                            var winUpdate = Ext.create("Ext.window.Window", {
                                title: "修改会员信息",
                                width: 500,
                                height: 630,
                                items: [brandUpdateForm],
                                buttonAlign: "right",
                                modal: true,
                                buttons: [
                                    {
                                        text: '保存',
                                        itemId: 'updateBtn',
                                        handler: function () {
                                            if (brandUpdateForm.isValid()) {
                                                 var FormData =  brandUpdateForm.getForm().getValues();
                                                console.log(Array.isArray(FormData.tagId));
                                                FormData.tagIds =  Array.isArray(FormData.tagId) ? FormData.tagId.join(",") : FormData.tagId;


                                                brandUpdateForm.submit({
                                                    submitEmptyText:false,
                                                    method:'get',
                                                    url: "/customer/update",
                                                    params: FormData,
                                                    success: function (form, action) {
                                                        var data = Ext.JSON.decode(action.response.responseText);
                                                        if (data.success) {
                                                            Espide.Common.tipMsgIsCloseWindow(data, winUpdate,'List', true);

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
                    });






                },
//                //order表格有选择项则把底部商品表格显示
//                selectionchange: function (sm, records) {
//                    var root = this,
//                        record = records[0];
//                    if (record) {
//                        root.getOrderGood(record.get('id'));
//                        Ext.getCmp('orderItem').show();
//                    } else {
//                        Ext.getCmp('orderItem').hide();
//                    }
//                },


                //order表格有选择项则把底部商品表格显示
//                cellclick: function (grid, td, cellIndex, records) {
//                    var root = this;
//
//                    if (records) {
//                        root.getOrderGood(records.get('id'));
//                        Ext.getCmp('orderItem').show();
//                    } else {
//                        Ext.getCmp('orderItem').hide();
//                    }
//                },

                //在表格渲染后跟据用户的自定义重新加载列顺序
//                afterrender: function (grid) {
//                    var root = this,
//                        com = Espide.Common,
//                        store = grid.getStore(),
//
//                    columns = com.getGridColumnData(grid, '/assets/js/order/data/UserConfig.json', 'hasImport');
//
//
//                    store.getProxy().extraParams = Ext.getCmp('search').getValues();
//
//
//                    columns = columns ? com.sortGridHead(grid, columns) : columns;
//                    if(columns){columns.unshift({xtype: 'rownumberer',text:'行数',width:'auto',align:'center'})}
//                    columns && columns.length > 0 && grid.reconfigure(store, columns);
//
//
//                },

                //用户移到列顺序时保存数据
//                columnmove: function (ct, column, fromIdx, toIdx, eOpts) {
//                    Espide.Common.saveGridColumnsData(ct.getGridColumns(), fromIdx, toIdx, '/assets/js/order/data/UserConfig.json', 'OrderList',Espide.Common.getGridHeadText(ct),'hasImport');
//                },

//                //订单物流编号编辑
//                validateedit: function (editor, e) {
//                    if (e.originalValue == e.value) {
//                        return false;
//                    }
//                    return this.checkShippingValue(e.record.get('shippingComp'), e.value)
//                },
//                //判断是否可以编辑
//                beforeedit: function () {
//                    if (this.getOrderState() != "CONFIRMED") return false;
//                },
//                render: function (input) {
//                    var map = new Ext.util.KeyMap({
//                        target: 'search',    //target可以是组建的id  加单引号
//                        binding: [
//                            {                       //绑定键盘事件
//                                key: Ext.EventObject.ENTER,
//                                fn: function () {
//                                    Espide.Common.reLoadGird('OrderList', 'search', true);
//                                }
//                            }
//                        ]
//                    });
//
//
//                }


            },

            //清除 订单自定义排序
            '#removeOrderSort':{
                click:function(btn){
                    Espide.Common.deleteGridColumnsData('OrderListhasImport');
                    Espide.Common.showGridSelErr('订单数据排序清除，请重新刷新页面');
                    Ext.getCmp('orderItem').hide();
                }
            },


            //日期类型
            "#search #dateType": {
                change: function (combo, newValue) {
                    var startDate = combo.up('#search').down("#startDate"),
                        endDate = combo.up('#search').down('#endDate');

                    if (newValue === 'all') {
                        startDate.disable();
                        endDate.disable();
                    } else {
                        startDate.enable();
                        endDate.enable();
                    }
                }
            },

            //2-表格toobar

            //返回客服处理
//            "#goBack_0": {
//                click: function () {
//                    var root = this,
//                        com = Espide.Common;
//
//                    if (!com.checkGridSel('OrderList')) return;
//
//                    root.comchangeOrderState('/invoice/back_to_wait_process')('yes');
//                }
//            },

            //订单汇总按钮
//            "#orderSummary": {
//                click: function () {
//
//                    //判断是否选择了订单
//                    if (com.checkGridSel('OrderList')){
//                        var url = '/orders/summary_report/excel?orderIds=' + Espide.Common.getGridSelsId('OrderList').join(',');
//                        window.open(url);
//                    }else{
//                        var url = '/orders/summary_report/excel?orderIds=null';
//                        window.open(url);
//                    }
//                }
//            },

            //打印物流单
            "#printPreview": {

                click: this.printPreview

            },

            //打印发货单
            "#printInvoice": {
                click: this.printInvoice
            },

            //2.0.1-搜索订单
            "#search #confirmBtn": {
                click: function () {
                    var com = Espide.Common;

//                    var data = Ext.getCmp('search').getForm().getValues(),
//                        btnId = data.orderStatus,
//                        orderGridToolBar = Ext.getCmp('search').getDockedItems('toolbar[dock="top"]')[0];



                    com.doFormCheck(Ext.getCmp('search').getForm(), function () {
                        com.reLoadGird('List', 'search', true);
                    }, '请正确输入要搜索的信息!')
                }
            },

            //2.0.2-条件搜索
            "#search #querySelect": {
                change: function (that, newValue, oldValue, eOpts) {
                    var queryType = Ext.getCmp('search').down("#queryType"),
                        type = that.getStore().getById(newValue).get('type');
                    if (type === 'string') {
                        queryType.setValue('has');
                    } else if (type === "number") {
                        queryType.setValue('=');

                    }
                }
            },

            //2.0.3-条件搜索类型
            "#search #queryType": {
                beforeselect: function (combo, record, index, eOpts) {
                    var type = record.get('type'),
                        querySelect = Ext.getCmp('search').down("#querySelect"),
                        querySelectType = querySelect.getStore().getById(querySelect.getValue()).get('type');

                    if (type != 'all' && type != querySelectType) {
                        Ext.Msg.alert('警告', '您请选的查询对象不支持所选值类型，请换一种试试');
                        return false;
                    }
                }
            },


            //2.7-弹出联想单号窗口
            "#autoEditNum": {
                click: function () {

                    var com = Espide.Common;

                   if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (!com.isArrAllEqual(com.getGridSels('OrderList', 'shippingComp'))) {
                        Ext.Msg.show({
                            title: '错误',
                            msg: "所选的订单对应的物流公司必须相同",
                            buttons: Ext.Msg.YES,
                            icon: Ext.Msg.WARNING
                        });
                        return;
                    }


                    //如果"联想单号"视图已绘制就返回
                    if (Ext.getCmp('autoNumberWin')) return;

                    Ext.widget('autoNumber').show();
                }
            },

            //2.8-联想单号确定按钮
            "#autoNumberWin #comfirm": {
                click: function (btn) {
                    var com = Espide.Common,
                        root = this,
                        params = btn.up('form').getValues();


                    if (!btn.up('form').isValid()) return;

                    if (!root.checkShippingValue(params.shippingComp, params.intNo)) return;

                    params['orderIds'] = com.getGridSelsId('OrderList').join(',');

                    com.commonMsg({
                        msg: '你确定要联想单号吗，修改订单后不可复原!',
                        fn: com.doAction({
                            //url: '/invoice/updateShipping',
                            url: 'invoice/OrderShipping/update',
                            params: params,
                            successTipMsg: '联想单号成功',
                            successCall: function () {

                                //tbar 物流公司与 联想单号物流同步
                                Ext.getCmp('shippingCp').setValue(Ext.getCmp('shippingComp').getValue());

                                com.reLoadGird('OrderList', 'search', true,function(){
                                    //重新选择
                                    var task = new Ext.util.DelayedTask(function(){
                                        Ext.getCmp('OrderList').selModel.selectAll(true);
                                    });
                                    task.delay(2);
                                });

                                Ext.getCmp('orderItem').hide();
                                Ext.getCmp('autoNumberWin').destroy();
                            }
                        })
                    });
                }
            },

            //2.9-联想单号取消按钮
            "#autoNumberWin #cancel": {
                click: function () {
                    Ext.getCmp('autoNumberWin').destroy();
                }
            },

            //确认打印
            "#printLogistics": {
                click: function () {
                    var root = this,
                        com = Espide.Common,
                        selItems = com.getGridSels('OrderList', 'shippingNo');

                    if (!com.checkGridSel('OrderList')) return;

                    if (!com.hasEmpytData(selItems)) {
                        Ext.Msg.alert('警告', '选中订单中有数据还没有加快递单号');
                        return false;
                    }

                    root.comchangeOrderState('/invoice/affirm_print')('yes');

                }
            },

            //选择物流公司
            "#shippingComp": {

                change: function (combo, newValue, oldValue) {
                    Ext.getCmp('OrderList').getStore().getProxy().extraParams.shippingComp = newValue;
                }
            },

            //返回上一级1
            "#goBack_1": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;

                    root.comchangeOrderState('/invoice/back_to_confirm')('yes');

                }
            },


            //批量验货
            "#batchInspection": {
                click: function (btn) {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;
                    btn.disable();
                    btn.setText('正在验货');
                    //root.comchangeOrderState('/invoice/batch_examine')('yes');

                    var com = Espide.Common;
                    return com.doAction({
                        url: '/invoice/batch_examine',
                        params: {
                            orderIds: com.getGridSelsId('OrderList').join(',')
                        },
                        successTipMsg: '状态修改成功',
                        successCall: function(){

                            Espide.Common.reLoadGird('OrderList', false, true);
                            Ext.getCmp('orderItem').hide();
                            btn.enable();
                            btn.setText('批量验货');
                        }
                    })('yes');

                }
            },

            //弹出验货窗口
            "#inspection": {
                click: function () {
                    //if (!Espide.Common.checkGridSel('OrderList', '请至少选择一项订单'))  return;
                    //获取选中的records
                    var records = Ext.getCmp('OrderList').getSelectionModel().getSelection();

                    //如果"验货"视图已绘制就返回
                    //if (Ext.widget('examineGood')) return;

                    Ext.widget('examineGood').show(this, function () {
                        if(records.length>0){
                            Ext.getCmp('order_num').setValue(records[0].data.shippingNo);
                        }
                    });
                }
            },

            //返回上一级2
            "#goBack_2": {
                click: function () {

                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;

                    root.comchangeOrderState('/invoice/back_to_print')('yes');

                }
            },

            //确认发货
            "#confirmationDelivery": {
                click: function () {

                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;

                    root.comchangeOrderState('/invoice/invoice')('yes');

                }
            },

            //确认签收
            "#orderSigned": {
                click: function () {

                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;

                    root.comchangeOrderState('/invoice/signed')('yes');

                }
            },
            //刷新
            "#refresh": {
                click: function () {
                    Espide.Common.reLoadGird('List', false, true);
                }
            },

            //导出表格
            "#orderExport": {
                click: function () {
                    var searchFormVal = Ext.getCmp('search').getValues(),
                        day = new Date(),
                        dayFormat = day.getFullYear() + '-' + day.getMonth() + '-' + day.getDate();

                    window.open('/orders/excel-' + dayFormat + '.xls?' + Espide.Common.parseParam(searchFormVal));
                }
            }

        });
    },

    //订单页通用操作
    /**
     * 根据某一字段取出订单对应商品信息
     * @param fld {String} 字段名
     */
    getOrderGood: function (fld) {
        var store = Ext.getCmp('orderItem').getStore();
        store.load({
            params: {
                orderId: fld
            }
        });
    },

    //获取当前订单管理的四种状态（待处理-已发货-待发货-物流状态）
    getOrderState: function () {
        return Ext.getCmp('orderState').getValue();
    },

    //设置当前订单管理的四种状态 （待处理-已发货-待发货-物流状态）
    setOrderState: function (value) {
        var orderState = Ext.getCmp('orderState');
        orderState.getStore().find('field1', value) == -1 || orderState.select(value);
        Ext.getCmp('OrderList').getStore().getProxy().extraParams = {
            orderStatus: value
        }
    },

    //二级菜单切换:belong:一级菜单id，state(obj):是否为改变状态
    changeSubBtn: function (belong, state) {
        var toolBar = Ext.getCmp('OrderList').getDockedItems('toolbar[dock="top"]')[0],
            buttons = toolBar.items.items;
        if (state == undefined) {
            Ext.each(buttons, function (item, buttons, index) {
                if (!item.belong) {
                    item.show();
                } else if (item.belong == belong) {
                    item.show();
                } else {
                    item.hide();
                }
            });
        } else {
            var flag = state.disabled;
            Ext.each(buttons, function (item, buttons, index) {
                item.belong == belong && !item.initShow && item.setDisabled(flag);
            });
        }

    },

    /**
     * 订单页通用操作回调函数
     */
    orderComCallback: function () {
        Espide.Common.reLoadGird('OrderList', false, true);
        Ext.getCmp('orderItem').hide();
    },

    //通用修改订单状态
    comchangeOrderState: function (url) {
        var com = Espide.Common;
        return com.doAction({
            url: url,
            params: {
                orderIds: com.getGridSelsId('OrderList').join(',')
            },
            successTipMsg: '状态修改成功',
            successCall: this.orderComCallback
        })
    },

    // 打印物流单
    printPreview: function () {
        var parent = this,
            selectedOrder = Ext.getCmp('OrderList').getSelectionModel().getSelection(),
            ids = [],
            isValidate = true,
            isValidateShippingNo = true,
            firstDelivery = '',
            delivery = '',
            shippingNo = '',
            designCode = '',
            firstRepoName = '',
            isValidateRepoName = true,
            printHtml;

        // 检验选中的订单配送方式是否相同,是否设置了物流单号
        Ext.each(selectedOrder, function (name, index) {
            delivery = selectedOrder[index].data.shippingComp;
            shippingNo = selectedOrder[index].data.shippingNo;
            repoName = selectedOrder[index].data.repoName;

            //快递公司不相同
            if (index === 0) {
                firstDelivery = delivery;
            } else {
                if (firstDelivery !== delivery) {
                    isValidate = false;
                    return false;
                }
            }

            //仓库不相同
            if (index === 0) {
                firstRepoName = repoName;
            } else {
                if (firstRepoName !== repoName) {
                    isValidateRepoName = false;
                    return false;
                }
            }

            ids.push(selectedOrder[index].data.id);

//            if (!shippingNo || shippingNo == '') {
//                isValidateShippingNo = false;
//                return false;
//            }

        });

        if (ids.length < 1) {
            Ext.MessageBox.show({
                title: '提示',
                msg: '请先选择订单',
                buttons: Ext.MessageBox.OK,
                icon: 'x-message-box-error'
            });
            return;
        }

        if (!isValidate) {
            Ext.MessageBox.show({
                title: '提示',
                msg: '请选择相同的配送方式',
                buttons: Ext.MessageBox.OK,
                icon: 'x-message-box-error'
            });
            return;
        }

        if (!isValidateRepoName) {
            Ext.MessageBox.show({
                title: '提示',
                msg: '请选择相同的相同仓库',
                buttons: Ext.MessageBox.OK,
                icon: 'x-message-box-error'
            });
            return;
        }

//        if (!isValidateShippingNo) {
//            Ext.MessageBox.show({
//                title: '提示',
//                msg: '所选择订单中有的没有设置物流单号，请先设置物流单号',
//                buttons: Ext.MessageBox.OK,
//                icon: 'x-message-box-error'
//            });
//            return;
//        }

        parent.getLogisticsDeign(delivery, function (data, width, height) { // 获取物流单设计代码
            designCode = data;

            if (data.split(';').length < 5) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '快递公司的物流面单还没有设计',
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
                return;
            }

            var msgText = '服务器错误';
            if (designCode === 'error' || designCode === 500) {

                if (designCode === 'error') {
                    msgText = '所选的配送方式单面设计未找到';
                }

                Ext.MessageBox.show({
                    title: '提示',
                    msg: msgText,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
                return;
            }

            Ext.MessageBox.show({
                title: '提示',
                msg: '打印控件正在运行...',
                width: 300
            });

            parent.getOrderPrintInfo(ids.join(), function (data) { // 获取物流信息列表
                var dataList = data.data.list;

                if (dataList.length > 0) {
                    LODOP = getLodop(document.getElementById('LODOP1'), document.getElementById('LODOP_EM1'));
                    LODOP.PRINT_INIT("打印物流单");
                    LODOP.PRINT_INITA(0, 0, 1000, 600, "初始化打印控件");
                    for (var i = 0; i < dataList.length; i++) {
                        LODOP.NewPage();
                        printHtml = parent.replaceAllHtml(designCode, dataList[i]);
                        eval(printHtml);
                    }
                    //LODOP.SET_PRINT_MODE("POS_BASEON_PAPER", true); // 输出以纸张边缘为基点
                    LODOP.SET_PRINT_STYLE("FontSize", 14);
                    LODOP.SET_PRINT_PAGESIZE(0, width, height, "CreateCustomPage");  //设置纸张大小
                    LODOP.SET_PRINT_MODE("AUTO_CLOSE_PREWINDOW", 1);
                    LODOP.SET_SHOW_MODE("BKIMG_IN_PREVIEW", 1); // 在打印预览时内含背景图（当然实际打印时不输出背景图）
                    LODOP.PREVIEW();
                    Ext.MessageBox.hide();
                }

            });
        }, selectedOrder[0].data.repoId);
    },

    /// 获取到物流单设计
    getLogisticsDeign: function (delivery, callback, repositoryId) {
        var url = 'logisticsprint/get_print_html';
        Ext.Ajax.request({
            url: url,
            method: 'GET',
            params: {
                name: delivery,
                repositoryId: repositoryId

            },
            success: function (response) {
                var data = Ext.JSON.decode(response.responseText);
                if (data.success) {
                    var height = data.data.obj.pageHeight,
                        width = data.data.obj.pageWidth;
                    callback(data.data.obj.printHtml, width, height);
                } else {
                    callback('error');
                }
            },
            failure: function () {
                callback(500);
            }
        });
    },

    // 获取物流信息列表
    getOrderPrintInfo: function (ids, callback) {
        var url = '/invoice/delivery_print';
        Ext.Ajax.request({
            url: url,
            method: 'POST',
            params: {
                orderIds: ids
            },
            success: function (response) {
                var data = Ext.JSON.decode(response.responseText);
                if (typeof callback === 'function') {
                    callback(data);
                }
            }
        });
    },

    // 替换打印配置
    replaceAllHtml: function (html, data) {
        html = html.substr(html.indexOf(";") + 1, html.length);
        if (html.indexOf("收货人联系方式") > 0) {
//            html = html.replace(new RegExp("收货人联系方式", "g"), (data.invoice.receiver.receiverPhone || '') + ' ' + (data.invoice.receiver.receiverPhone || ''));

            var receiverStr;

            if (data.receiverPhone && data.receiverMobile) {
                receiverStr = (data.receiverMobile || '') + '\\n' + (data.receiverPhone || '');
            } else {
                receiverStr = (data.receiverPhone || '') + '' + (data.receiverMobile || '');
            }


            html = html.replace(new RegExp("收货人联系方式", "g"), receiverStr);
        }
        if (html.indexOf("发货人联系方式") > 0) {
            var charge;

            if (data.chargePhone && data.chargeMobile) {
                charge = (data.chargeMobile || '') + '\\n' + (data.chargePhone || '');
            } else {
                charge = (data.chargePhone || '') + '' + (data.chargeMobile || '');
            }

            html = html.replace(new RegExp("发货人联系方式", "g"), charge);
        }
        if (html.indexOf("发货人单位名称") > 0) {
            html = html.replace(new RegExp("发货人单位名称", "g"), data.repoName);
        }
        if (html.indexOf("邮编") > 0) {
            html = html.replace(new RegExp("邮编", "g"), data.receiverZip);
        }
        if (html.indexOf("发货地址") > 0) {
            html = html.replace(new RegExp("发货地址", "g"), data.repoAddress);
        }
        if (html.indexOf("收货人地址") > 0) {
            html = html.replace(new RegExp("收货人地址", "g"), data.receiverAddress);
        }
        if (html.indexOf("收货人") > 0) {
            html = html.replace(new RegExp("收货人", "g"), data.receiverName);
        }
        if (html.indexOf("发货人") > 0) {
            html = html.replace(new RegExp("发货人", "g"), data.chargePerson);
        }
        if (html.indexOf("收货省") > 0) {
            html = html.replace(new RegExp("收货省", "g"), data.receiverState);
        }
        if (html.indexOf("收货市") > 0) {
            html = html.replace(new RegExp("收货市", "g"), data.receiverCity);
        }
        if (html.indexOf("收货区") > 0) {
            html = html.replace(new RegExp("收货区", "g"), data.receiverDistrict);
        }
        if (html.indexOf("发货时间") > 0) {
            var deliveryTime = data.deliveryTime;
            if (data.deliveryTime) {
                deliveryTime = new Date(deliveryTime);
                deliveryTime = Ext.Date.format(deliveryTime, 'Y-m-d H:i:s')
            }
            html = html.replace(new RegExp("发货时间", "g"), deliveryTime || '');
        }


        if (html.indexOf("商品编码及数量") > 0) {
            var prodetail = data.orderItemMapList;
            var data = [];
            Ext.each(prodetail, function (prodetail) {
                //data.push(prodetail.productNo + ':(' + prodetail.buyCount + ')' + "\\n");
                data.push(prodetail.productNo + ':(' + prodetail.buyCount + '),');
            });

            html = html.replace(new RegExp("商品编码及数量", "g"), data.join(''));

        }
        return html;
    },

    printInvoice: function () {
        var parent = this,
            selectedOrder = Ext.getCmp('OrderList').getSelectionModel().getSelection(),
            url = '/invoice/order_print',
            shippingNo,
            isValidateShippingNo = true,
            ids = [];

        // 检验选中的订单是否设置了物流单号
        Ext.each(selectedOrder, function (name, index) {
            shippingNo = selectedOrder[index].data.shippingNo;
            ids.push(selectedOrder[index].data.id);

//            if (!shippingNo || shippingNo == '') {
//                isValidateShippingNo = false;
//                return false;
//            }

        });

        if (ids.length < 1) {
            Ext.MessageBox.show({
                title: '提示',
                msg: '请先选择订单',
                buttons: Ext.MessageBox.OK,
                icon: 'x-message-box-error'
            });
            return;
        }

//        if (!isValidateShippingNo) {
//            Ext.MessageBox.show({
//                title: '提示',
//                msg: '所选择订单中有的没有设置物流单号，请先设置物流单号',
//                buttons: Ext.MessageBox.OK,
//                icon: 'x-message-box-error'
//            });
//            return;
//        }

        Ext.MessageBox.show({
            title: '提示',
            msg: '打印控件正在运行...',
            width: 300
        });

        Ext.Ajax.request({
            url: url,
            method: 'POST',
            params: {
                orderIds: ids.join(',')
            },
            success: function (response) {
                var data = Ext.JSON.decode(response.responseText),
                    style,
                    dataList;

                if (data.success) {
                    LODOP = getLodop(document.getElementById('LODOP1'), document.getElementById('LODOP_EM1'));
                    LODOP.PRINT_INIT("打印发货单");
                    style = parent.invoiceStyle();
                    dataList = data.data.list;
                    for (var i = 0; i < dataList.length; i++) {
                        parent.invoiceHtml(dataList[i], dataList[0]['invoicePostFee'], style, 1);
                    }
                    LODOP.SET_PRINT_PAGESIZE(0, '2100', '1450', 'CreateCustomPage'); // 设定纸张大小
                    LODOP.SET_PRINT_MODE('AUTO_CLOSE_PREWINDOW', 1); // 打印后自动关闭预览窗口
                    //LODOP.SET_PRINT_MODE("POS_BASEON_PAPER", true); // 输出以纸张边缘为基点
                    //加了这个代码后连打出现问题，选4单预览时只显示出来3单，后面2单还没有数据
                    LODOP.PREVIEW();
                    Ext.MessageBox.hide();
                } else {
                    Ext.MessageBox.show({
                        title: '提示',
                        msg: data.msg,
                        buttons: Ext.MessageBox.OK,
                        icon: 'x-message-box-error'
                    });
                }

            }
        });
    },

    // 发货单样式
    invoiceStyle: function () {
        return '<style type="text/css">' +
            '*{margin:0;padding:0}' +
            'table{border-collapse:collapse;border-spacing:0;}' +
            'body{font-size:14px;font-family:SimSun,SimHei,"microsoft yahei";line-height: 1.2em}' +
            '.print-wrapper{position:relative;height:430px;padding: 60px 0 0}' +
            '.table-order td{padding:2px 5px;}' +
            '.table-order th{width:70px;font-weight:400;text-align:left;padding:2px 5px 2px 0;}' +
            '.table-product{margin-top:10px;border-width:1px 0 0 1px;border-color:#999;border-style:solid;}' +
            '.table-product td,.table-product th{border-width:0 1px 1px 0;border-color:#999;' +
            '    border-style:solid;text-align:center;padding:5px;}' +
            '.total{text-align:right;padding:10px 0;}' +
            '.seller{position:absolute;width:90%;left:0;bottom:20px;}' +
            '.seller p{padding:2px 0;font-size:14px;}' +
            '</style>';
    },

    // 发货单模板
    invoiceTpl: function () {
        return new Ext.XTemplate(
            '<!DOCTYPE HTML>',
            '<html>',
            '<head>',
            '<meta charset="utf-8"/>',
            '#* style *#',
            '</head>',
            '<body>',
            '<div class="print-wrapper">',
            '   <table width="100%" class="table-order">',
            '       <tr>',
            '           <th>收 货 人：</th>',
            '           <td>{receiverName}</td>',
            '           <th>快递公司：</th>',
            '           <td style="width: 160px">{shippingComp}</td>',
            '       </tr>',
            '       <tr>',
            '           <th>联系方式：</th>',
            '           <td>{receiverMobile}  {receiverPhone}</td>',
            '           <th>快递单号：</th>',
            '           <td style="width: 160px">{shippingNo}</td>',
            '       </tr>',
            '   </table>',
            '   <table width="100%" class="table-order">',
            '       <tr>',
            '           <th>收货地址：</th>',
            '           <td>{receiverAddress}</td>',
            '           <th>订单邮费：</th>',
            '           <td style="width: 160px">{nvoicePostFee}(元)</td>',
            '       </tr>',
            '       <tr>',
            '           <th>买家留言：</th>',
            '           <td>{buyerMessage}</td>',
            '       </tr>',
            '   </table>',
            '   <table width="100%" class="table-product" style="font-size:11px">',
            '   <thead>',
            '       <tr>',
            '           <th style="width:30p;font-weight:normal;">序号</th>',
            '           <th style="width:70px;font-weight:normal;">商品编号</th>',
            '           <th style="width:330px;font-weight:normal;">商品名称</th>',
            '           <th style="width:75px;font-weight:normal;">产品单价(元)</th>',
            '           <th style="width:40px;font-weight:normal;">数量</th>',
            '           <th style="width:80px;font-weight:normal;">优惠金额(元)</th>',
            '           <th style="width:75px;font-weight:normal;">实付金额(元)</th>',
            '       </tr>',
            '   </thead>',
            '   <tbody>',
            '   #* goodsList *#',
            '   </tbody>',
            '</table>',
            '   <div class="total">合计：￥：{finalTotalFee}元</div>',
            '   <div class="seller">',
            '       <p>订 单 号： {outOrderNo}</p>',
            '       <p>发 货 人： {chargePerson}</p>',
            '       <p>发货地址： {repoAddress}</p>',
            '       <p>卖家留言： {remark}</p>',
            '   </div>',
            '</div>',
            '</body>',
            '</html>'
        );
    },

    // 发货单HTML
    invoiceHtml: function (data, nvoicePostFee, style, status) {
        var goodsList = data.orderItemMapList || [],
            newData = Ext.clone(data),
            index = 0,
            printTime = data.printTime || null,
            strFormHtml,
            goodsListHtml = '',
            shippingComp = data.shippingComp || null,
            nvoicePostFee = nvoicePostFee || null,
            i = (status - 1) * 6;
        if (printTime) {
            printTime = new Date(printTime);
            printTime = Ext.Date.format(printTime, 'Y-m-d H:i:s');
            newData.printTime = printTime;
        }

        if (shippingComp) {
            newData.shippingComp = Espide.Common.getExpress(shippingComp);
        }
        if (nvoicePostFee) {
            newData.nvoicePostFee = nvoicePostFee;
        }

        if (goodsList.length > 6 * status) {
            index = 6 * status;
        } else {
            index = goodsList.length;
        }

        for (; i < index; i++) {

            goodsListHtml += '<tr>' +
                '<td style="width: 50px">' + (i + 1) + '</td>' +
                '<td style="width: 75px">' + goodsList[i].prodCode + '</td>' +
                '<td>' + goodsList[i].prodName +   (goodsList[i].color ==null? '':', 颜色：' + goodsList[i].color) +
                (goodsList[i].speci ==null? '':', 规格：' + goodsList[i].speci)+
                //', 规格：' + (goodsList[i].speci || '') + '</td>' +
                '<td style="width: 75px">' + goodsList[i].prodPrice + '</td>' +
                '<td style="width: 50px">' + goodsList[i].prodCount + '</td>' +
                '<td style="width: 50px">' + goodsList[i].discountFee + '</td>' +
                '<td style="width: 75px">' + goodsList[i].actualFee + '</td>' +
                '</tr>';
        }

        strFormHtml = this.invoiceTpl().apply(newData);
        strFormHtml = strFormHtml.replace('#* style *#', style);
        strFormHtml = strFormHtml.replace('#* goodsList *#', goodsListHtml);

        LODOP.NewPage();
        LODOP.ADD_PRINT_HTM('0.1mm', '10mm', 'RightMargin:10mm', 'BottomMargin:0.1mm', strFormHtml);
        //LODOP.ADD_PRINT_BARCODE(40, 516, 230, 47, '128Auto', data.orderNo);

        if (goodsList.length > 6 * status) {
            status = status + 1;
            this.invoiceHtml(data, nvoicePostFee, style, status);
        }
    },

    // 检测是否已经安装LOGOP
    checkIsInstallLODOP: function () {
        setTimeout(function () {
            try {
                var LODOP = getLodop(document.getElementById('LODOP1'), document.getElementById('LODOP_EM1'));
                if ((LODOP != null) && (typeof(LODOP.VERSION) != "undefined")) {
                    console.log("本机已成功安装过Lodop控件!\n版本号:" + LODOP.VERSION);
                }
            } catch (err) {
                //alert("Error:本机未安装或需要升级!");
            }
        }, 800);
    },

    // 判断物流规则是否正确
    checkShippingValue: function (shipping, value) {
        if (shipping === "shunfeng" && !/^\d{12}$/.test(value)) {
            Ext.Msg.alert('警告', "顺丰物流单号必须是12位的数字，请重新输入");
            return false;
        } else if (shipping === "ems" && value.length != 13) {
            Ext.Msg.alert('警告', "EMS物流单号必须是13位的字符，请重新输入");
            return false;
        }

        return true;
    },


});