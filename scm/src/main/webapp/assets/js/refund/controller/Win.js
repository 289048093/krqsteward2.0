/**
 * Created by Lein xu
 */
Ext.define('Refund.controller.Win', {
    extend: 'Ext.app.Controller',
    views: ['win.AddRefund', 'win.CreateList', 'win.CreateForm', 'win.EditRefund', 'win.EditList', 'win.EditForm'],
    stores: ['RefundList', 'ItemList'],
    models: ['RefundList', 'ItemList'],
    init: function () {
        var start = "", end = "" , type;

        this.control({
            '#search': {
                click: function (btn) {
                    type = btn.up('grid').down('#type').getValue();
                    var repoId = btn.up('grid').down('#repoId').getValue();
                    start = Ext.util.Format.date(Ext.getCmp('startDate').getRawValue(), 'Y-m-d H:i:s');
                    end = Ext.util.Format.date(Ext.getCmp('endDate').getRawValue(), 'Y-m-d H:i:s');

                    var store = btn.up('grid').getStore();
                    var new_params = {
                        type: type,
                        repositoryId: repoId,
                        minDate: start,
                        maxDate: end

                    };
                    store.getProxy().extraParams = new_params;
                    store.loadPage(1);
                }
            },

            //根据checkbox状态转变 表单
            '#createChangeBtn': {
                change: function (obj, newValue, oldValue) {
                    if (newValue) {
                        Ext.getCmp('ctreateActualRefundFee').show();
                        Ext.getCmp('ctreatePostFee').show();
                        Ext.getCmp('createPostPayer').show();
                        Ext.getCmp('shippingNo').show();
                        Ext.getCmp('shippingComp').show();

                        Ext.getCmp('ctreateActualRefundFee').enable();
                        Ext.getCmp('ctreatePostFee').enable();
                        Ext.getCmp('createPostPayer').enable();
                        Ext.getCmp('shippingNo').enable();
                        Ext.getCmp('shippingComp').enable();
                    } else {
                        Ext.getCmp('ctreateActualRefundFee').disable();
                        Ext.getCmp('ctreatePostFee').disable();
                        Ext.getCmp('createPostPayer').disable();
                        Ext.getCmp('shippingNo').disable();
                        Ext.getCmp('shippingComp').disable();
                    }
                }
            },
            //编辑按钮checkbox
            '#alsoReturn': {
                change: function (obj, newValue, oldValue) {
                    if (newValue) {
                        Ext.getCmp('actualRefundFee').show();
                        Ext.getCmp('postFee').show();
                        Ext.getCmp('postPayer').show();
                        Ext.getCmp('shippingNo').show();
                        Ext.getCmp('shippingComp').show();

                        Ext.getCmp('actualRefundFee').enable();
                        Ext.getCmp('postFee').enable();
                        Ext.getCmp('postPayer').enable();
                        Ext.getCmp('shippingNo').enable();
                        Ext.getCmp('shippingComp').enable();
                    } else {
                        Ext.getCmp('actualRefundFee').disable();
                        Ext.getCmp('postFee').disable();
                        Ext.getCmp('postPayer').disable();
                        Ext.getCmp('shippingNo').disable();
                        Ext.getCmp('shippingComp').disable();
                    }
                }
            },

            //查询的订单
            '#searchBtn': {
                click: function (btn) {
                    var createListStore = Ext.getCmp('createList').getStore();
                    createListStore.load({
                        params: Ext.getCmp('orderSearch').getForm().getValues()
                    });
                }
            },
            '#createList':{
                select:function(model,record,index,obj){
                    Ext.getCmp('buyerAlipayNo').setValue(record.get('buyerAlipayNo'));
                }
            },
            //创建退款提交
            '#Refundcomfirm': {
                click: function (btn) {

                    var params = Ext.getCmp('createForm').getForm().getValues();
                    var OrderListSlc = Ext.getCmp('createList').getSelectionModel().getSelection();

                    //判断是否选择了数据
                    if(!Espide.Common.checkGridSel('createList')){
                        return false;
                    }

                    params['oldItemId'] = OrderListSlc[0].data.id;

                    if(Ext.getCmp('createChangeBtn').getValue()){
                        if(!Ext.getCmp('createForm').getForm().isValid()){
                            Espide.Common.showGridSelErr('请填写完整数据!');
                            return false;
                        }
                    }
                    Ext.Ajax.request({
                        params: params,
                        url: "refund/add",
                        success: function (response, options) {
                            var data = Ext.JSON.decode(response.responseText);
                            if (data.success) {
                                Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('addRefundWin'), 'refundList', true);
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
            },
            '#cancel':{
                click:function(btn){
                    btn.up('window').destroy();
                }
            },
            //打开编辑窗口
            '#editRefund': {
                click: function (btn) {
                    var OrderListSlcRecord = Ext.getCmp('refundList').getSelectionModel().getSelection();


                     //判断是否选择了数据
                    if(!Espide.Common.checkGridSel('refundList')){
                        return false;
                    }
                    var records = Ext.getCmp('refundList').getSelectionModel().getSelection();


                    if(records[0].data.type.value !='订单退款'){
                        Espide.Common.showGridSelErr('退款类型必须是订单退款');
                        return;
                    }

                    if(!records[0].data.online || (records[0].data.online && records[0].data.status.value == '成功')){ //线下 且 退款成功才能修改

                        Ext.widget('editRefund').show(this, function () {


                            var editListStore = Ext.getCmp('editList').getStore();
                            var emptyArray = [];
                            emptyArray.push(OrderListSlcRecord[0].data.orderItemVo);
                            editListStore.loadData(emptyArray);

                            Ext.getCmp('refundFee').setValue(OrderListSlcRecord[0].data.refundFee);
                            Ext.getCmp('refundTime').setValue(OrderListSlcRecord[0].data.refundTime);
                            Ext.getCmp('revisitTime').setValue(OrderListSlcRecord[0].data.revisitTime);
                            Ext.getCmp('reason').setValue(OrderListSlcRecord[0].data.reason);
                            Ext.getCmp('remark').setValue(OrderListSlcRecord[0].data.remark);
                            Ext.getCmp('shippingNo').setValue(OrderListSlcRecord[0].data.shippingNo);
                            Ext.getCmp('shippingComp').setValue(OrderListSlcRecord[0].data.shippingComp);
                            Ext.getCmp('status').setValue(OrderListSlcRecord[0].data.status.name);
                            Ext.getCmp('buyerAlipayNo').setValue(OrderListSlcRecord[0].data.buyerAlipayNo);

                            if(!records[0].data.online){//退款状态可改

                            }else{
                                Ext.getCmp('refundFee').disable();
                                Ext.getCmp('refundTime').disable();
                            }
                            Ext.getCmp('shippingNo').disable();
                            Ext.getCmp('shippingComp').disable();

                            if(records[0].data.status.value != '正在申请'){
                                Ext.getCmp('status').setReadOnly(true);
                            }


                            if (OrderListSlcRecord[0].data.actualRefundFee) {
                                Ext.getCmp('actualRefundFee').setValue(OrderListSlcRecord[0].data.actualRefundFee);
                            }

                            if (OrderListSlcRecord[0].data.postFee) {
                                Ext.getCmp('postFee').setValue(OrderListSlcRecord[0].data.postFee);
                            }

                            if (OrderListSlcRecord[0].data.postPayer) {
                                if (OrderListSlcRecord[0].data.postPayer.value == "顾客") {
                                    Ext.getCmp('postPayer').setValue('BUYER');
                                } else if (OrderListSlcRecord[0].data.postPayer.value == "平台商") {
                                    Ext.getCmp('postPayer').setValue('SELLER');

                                } else if (OrderListSlcRecord[0].data.postPayer.value == "供应商") {
                                    Ext.getCmp('postPayer').setValue('SUPPLIER');
                                }

                            }

                            if (OrderListSlcRecord[0].data.alsoReturn) {
                                Ext.getCmp('alsoReturn').setValue(true);
                            } else {
                                Ext.getCmp('actualRefundFee').disable();
                                Ext.getCmp('postFee').disable();
                                Ext.getCmp('postPayer').disable();
                            }

                            //var params = Ext.getCmp('editForm').getForm().getValues();
                        });

                    }else{
                        Ext.Msg.alert('警告', '线上退款且退款状态必须是成功');
                        return false;
                    }

                }
            },
            //编辑提交
            '#Editcomfirm': {
                click: function () {
                    var params = Ext.getCmp('editForm').getForm().getValues();
                    var refundListSlc = Ext.getCmp('refundList').getSelectionModel().getSelection()[0].data;
                    params['id'] = refundListSlc.id;

                    if(Ext.getCmp('editForm').getForm().isValid()){
                        Ext.Ajax.request({
                            params: params,
                            url: "refund/update",
                            success: function (response, options) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('editRefundWin'), 'refundList', true);
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
                    }else{
                        Ext.Msg.alert('警告', '请把表单信息补全!');
                    }

                }
            }


        });
    }



});