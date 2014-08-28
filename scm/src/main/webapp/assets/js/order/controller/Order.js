/**
 * Created by king on 13-12-17
 */

Ext.define('Supplier.controller.Order', {
    extend: 'Ext.app.Controller',
    views: ['Order', 'Search', 'List', 'Item', 'Batch', 'AutoNumber', 'ItemTab', 'ExamineList', 'OrderLog'],
    stores: ['OrderList', 'ItemList', 'StorageAll', 'Brand', 'ShopAll', 'Shop', 'ExamineList', 'OrderLog'],
    models: ['OrderList', 'ItemList'],
    init: function () {
        this.control({

            //高级搜索
            '#createChangeBtn': {
                change: function (obj, newValue, oldValue) {
                    if (newValue) {
                        Ext.getCmp('row2').setVisible(true);
                        Ext.getCmp('row2').enable();


                    } else {
                        Ext.getCmp('row2').setVisible(false);
                        Ext.getCmp('row2').disable();
                    }
                }
            },
            //1-表格操作
            "#list": {
                //高级排序
                headerclick: function (ct, column, e) {
                    var listStore = Ext.getCmp('OrderList').getStore(),
                        limit = Ext.getCmp('limit').getValue(),
                        params = Ext.getCmp('search').getForm().getValues();

                    if (column.text == '买家留言') {
                        //整合表单数据
                        var currentParams = Ext.Object.merge(params, {
                            limit: limit,
                            sortParam: 'buyerMessage#ASC'
                        });
                        listStore.load({
                            params: currentParams
                        });
                    } else if (column.text == '备注说明') {

                        //整合表单数据
                        var currentParams = Ext.Object.merge(params, {
                            limit: limit,
                            sortParam: 'remark#ASC'
                        });

                        listStore.load({
                            params: currentParams
                        });

                    }
                },

                //order表格有选择项则把底部商品表格显示
                cellclick: function (grid, td, cellIndex, records) {
                    var root = this;

                    if (records) {
                        root.getOrderGood(records.get('id'));
                        Ext.getCmp('ItemTab').show();
                    } else {
                        Ext.getCmp('ItemTab').hide();
                    }
                },

                //在表格渲染后跟据用户的自定义重新加载列顺序
                afterrender: function (grid) {

                    var root = this,
                        com = Espide.Common,
                        store = grid.getStore();
                    columns = com.getGridColumnData(grid, '/assets/js/order/data/UserConfig.json');

                    store.getProxy().extraParams = Ext.getCmp('search').getValues();
                    columns = columns ? com.sortGridHead(grid, columns) : columns;

                    if (columns) {
                        columns.unshift({xtype: 'rownumberer', text: '行数', width: 'auto', align: 'center'})
                    }

                    columns && columns.length > 0 && grid.reconfigure(store, columns);
                    Ext.Function.defer(function () {
                        //grid.getStore().getProxy().extraParams = {};
                    }, 700)

                },

                //用户移到列顺序时保存数据addNum
                columnmove: function (ct, column, fromIdx, toIdx, eOpts) {

                    Espide.Common.saveGridColumnsData(ct.getGridColumns(), fromIdx, toIdx, '/assets/js/order/data/UserConfig.json', 'OrderList', Espide.Common.getGridHeadText(ct));
                },

                //判断是否可以编辑
                beforeedit: function (editor, e) {
                    //if (e.record.get('orderStatus')['name'] != "WAIT_PROCESS") return false;
                },
                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.reLoadGird('OrderList', 'search', true);
                                    Ext.getCmp('ItemTab').hide();
                                }
                            }
                        ]
                    });
                }
            },

            //清除 订单自定义排序
            '#removeOrderSort': {
                click: function (btn) {
                    Espide.Common.deleteGridColumnsData('OrderListpending');
                    Espide.Common.showGridSelErr('订单数据排序清除，请重新刷新页面');
                    Ext.getCmp('ItemTab').hide();
                }
            },
            //清除 订单自定义排序
            '#removeItemSort': {
                click: function (btn) {
                    Espide.Common.deleteGridColumnsData('orderItempending');
                    Espide.Common.showGridSelErr('订单项数据排序清除，请重新刷新页面');
                    Ext.getCmp('ItemTab').hide();
                }
            },


            "#item": {
                //判断是否可以编辑
                beforeedit: function (editor, e) {
                    if (Espide.Common.getGridSels('OrderList', 'orderStatus')[0] != "WAIT_PROCESS") return false;
                },
                //在表格渲染后跟据用户的自定义重新加载列顺序
                afterrender: function (grid) {

                    var root = this,
                        com = Espide.Common,
                        store = grid.getStore();
                    columns = com.getGridColumnData(grid, '/assets/js/order/data/UserConfig.json');


                    columns = columns ? com.sortGridHead(grid, columns) : columns;
                    columns && columns.length > 0 && grid.reconfigure(store, columns);

                },

                //用户移到列顺序时保存数据addNum
                columnmove: function (ct, column, fromIdx, toIdx, eOpts) {
                    Espide.Common.saveGridColumnsData(ct.getGridColumns(), fromIdx, toIdx, '/assets/js/order/data/UserConfig.json', 'orderItem', Espide.Common.getGridHeadText(ct));
                },

            },

            //1.1-订单对应商品删除
            "#item actioncolumn#goodRemove": {
                click: function (button, rowIndex, colIndex, item, e, selected) {
                    var itemStore = Ext.getCmp('orderItem').getStore();

                    if (itemStore.count() === 1) {
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: '请至少保留一个商品',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                        return;
                    }
                    if (colIndex >= 0) {
                        Ext.Msg.confirm('操作确认', '确定要删除这项商品吗？', function (optional) {
                            if (optional == "yes") {
//                                itemStore.remove(selected);
                                Ext.Ajax.request({
                                    params: {
                                        id: selected.data.id
                                    },

                                    url: "order/deleteItemList",
                                    success: function (response, options) {
                                        var data = Ext.JSON.decode(response.responseText);
                                        if (data.success) {
                                            itemStore.autoSync = false;
                                            itemStore.removeAt(colIndex);

                                            Espide.Common.tipMsg('操作成功', data.msg);
                                        }
                                    },
                                    failure: function (form, action) {
                                        var data = Ext.JSON.decode(form.responseText);
                                        Ext.MessageBox.show({
                                            title: '提示',
                                            msg: data.msg,
                                            buttons: Ext.MessageBox.OK,
                                            icon: 'x-message-box-warning'
                                        });
                                    }
                                });


                            }
                        });
                    }
                }
            },
            //订单导出
            '#outOrder': {
                'click': function (btn) {
                    var totalCount = Ext.getCmp('OrderList').down("#orderConut").getValue(),
                        parms = Ext.getCmp('search').getForm().getValues();
                    /*                if(totalCount>4999){
                     Espide.Common.showGridSelErr('导出订单不得超出5000条');
                     return;
                     }*/
                    window.open('order/reportOrders?' + Ext.Object.toQueryString(parms));
                }
            },
            // 补货换货导出
            '#outOtherOrder': {
                click: function (btn) {
                    var parms = Ext.getCmp('search').getForm().getValues();
                    window.open('order/extractExchangeOrder2Excel?' + Ext.Object.toQueryString(parms));
                }
            },
            //2-表格toobar

            //2.0.1-搜索订单
            "#search #dateType": {
                select: function (combo, record, index) {


                }
            },
            "#search #confirmBtn": {
                click: function () {
                    var com = Espide.Common,
                        startDate = Ext.getCmp('search').down('#startDate').getValue(),
                        endDate = Ext.getCmp('search').down('#endDate').getValue();

                    if (Ext.getCmp('search').down('#dateType').getValue() != 'all' && !!startDate && !!endDate && startDate.getTime && endDate.getTime && endDate.getTime() < startDate.getTime()) {
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: '搜索条件的结束日期必须大于开始日期! ',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                        return;
                    }
                    com.doFormCheck(Ext.getCmp('search').getForm(), function () {
                        com.reLoadGird('OrderList', 'search', true);
                        Ext.getCmp('ItemTab').hide();
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

            //2.0.2-条件搜索
            "#search #querySelect2": {
                change: function (that, newValue, oldValue, eOpts) {
                    var queryType = Ext.getCmp('search').down("#queryType2"),
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

            //2.0.3-条件搜索类型
            "#search #queryType2": {
                beforeselect: function (combo, record, index, eOpts) {
                    var type = record.get('type'),
                        querySelect = Ext.getCmp('search').down("#querySelect2"),
                        querySelectType = querySelect.getStore().getById(querySelect.getValue()).get('type');

                    if (type != 'all' && type != querySelectType) {
                        Ext.Msg.alert('警告', '您请选的查询对象不支持所选值类型，请换一种试试');
                        return false;
                    }
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

            //2.1-拆分订单
            "#splitOrderBtn": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (!root.checkOrderState('OrderList')) return;

                    if (!com.checkGridSel('orderItem', '请至少选择一个订单项'))  return;

                    if (Ext.getCmp('orderItem').getStore().getCount() === 1) {
                        Ext.Msg.alert({
                            title: '警告！',
                            msg: '当前订单只有1个订单项，不能拆分',
                            icon: Ext.Msg.WARNING,
                            buttons: Ext.Msg.YES
                        });
                        return;
                    }

                    //先弹出操作确认警告，再概据用户交互做回调
                    com.commonMsg({
                        msg: '你确定要拆分订单吗，拆分后不可复原!',
                        fn: com.doAction({
//                            url: 'order/orderSeparate',
                            url: 'order/splitOrder',
                            params: {
                                orderId: com.getGridSels('OrderList', 'id')[0],
                                //ids: com.getGridSelsId('orderItem').join(',')
                                orderItemIds: com.getGridSelsId('orderItem').join(',')
                            },
                            successCall: function (data) {
                                root.orderComCallback();
                                Ext.Msg.show({
                                    title: '拆分成功',
                                    msg: "订单编号为:" + data.data.list.join(','),
                                    buttons: Ext.Msg.YES
                                });
                            }
                        })
                    });

                }
            },

            //2.2-合并订单
            "#mergerOrderBtn": {
                click: function () {
                    var root = this,
                        com = Espide.Common,
                        OrderList = Ext.getCmp('OrderList'),
                        sels = com.getGridSelsId(OrderList);

                    if (sels.length < 2) {
                        com.showGridSelErr('请至少选择两个订单来合并');
                        return;
                    }

                    if (!root.checkOrderState('OrderList')) return;


                    //此项过滤条件暂时不用
                    //if (!com.isArrAllEqual(sels)) {
                    //    com.showGridSelErr('要合并的订单的物流编号必须相同');
                    //    return;
                    //}

                    //先弹出操作确认警告，再概据用户交互做回调
                    com.commonMsg({
                        msg: '你确定要合并订单吗，合并后不可复原!',
                        fn: com.doAction({
                            url: 'order/merge',
                            params: {
                                orderIds: sels.join(',')
                            },
                            successTipMsg: '订单合并成功',
                            successCall: root.orderComCallback
                        })
                    });
                }
            },

            //2.2-订单作废
            "#cancelOrder": {
                click: function () {
                    var root = this,
                        flag = false,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;
                    var orderStates = com.getGridSels('OrderList','orderStatus');

                    function checkcancelState(grid, state) {
                        var state = state || 'PRINTED';
                        var com = Espide.Common,

                            msg = ['已打印', '已验货','已确认'],flag = true;


                        Ext.each(orderStates, function (orderStates, key) {
                            console.log(orderStates.value);
                            //判断是否在京东或者天猫平台  才能嘻唰唰
                            if (!Ext.Array.contains(msg, orderStates.value)) {
                                flag = false;
                            }
                        });
                        return flag;
                    }



                    if(orderStates.length>1){  //单条订单判断
                        if(!checkcancelState('OrderList', 'CONFIRMED')){
                            com.commonMsg({
                                msg: '选中的部分订单在仓库已经打单,作废之前请通知仓库不要发货,您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            })

                        }else if(!checkcancelState('OrderList', 'PRINTED')){
                            com.commonMsg({
                                msg: '选中的部分订单在仓库已经打单,作废之前请通知仓库不要发货,您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            })
                        }else if(!checkcancelState('OrderList', 'EXAMINED')){
                            com.commonMsg({
                                msg: '选中的部分订单在仓库已经打单,作废之前请通知仓库不要发货,您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            });
                        }else{
                            com.commonMsg({
                                msg: '您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            })
                        }
                    }else{
                        //多个订单判断
                        if(checkcancelState('OrderList', 'CONFIRMED')){
                            com.commonMsg({
                                msg: '选中的部分订单在仓库已经打单,作废之前请通知仓库不要发货,您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            })

                        }else if(checkcancelState('OrderList', 'PRINTED')){
                            com.commonMsg({
                                msg: '选中的部分订单在仓库已经打单,作废之前请通知仓库不要发货,您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            })
                        }else if(checkcancelState('OrderList', 'EXAMINED')){
                            com.commonMsg({
                                msg: '选中的部分订单在仓库已经打单,作废之前请通知仓库不要发货,您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            });
                        }else{
                            com.commonMsg({
                                msg: '您确定要作废吗?',
                                fn: function (action) {
                                    root.comchangeOrderState('/order/cancellation')(action);
                                }
                            })
                        }
                    }



                }
            },

            //2.2.1-订单恢复
            "#recoverOrder": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;


                    if (!root.checkOrderState('OrderList', 'INVALID')) return;

                    com.commonMsg({
                        msg: '您确定要恢复此订单?',
                        fn: function (action) {
                            root.comchangeOrderState('/order/recover')(action);
                        }
                    })
                }
            },

            //订单刷单
            '#cheatBtn': {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;
                    //判断是否是京东或者天猫平台
                    if (!root.checkPlatform('OrderList')) return;
                    //是否是待处理  订单状态
                    if (!root.checkOrderState('OrderList')) return;
                    //是否为正常订单  订单类型
                    if (!root.checkOrderType('OrderList')) return;

                    com.commonMsg({
                        msg: '您确定要修改为刷单订单?',
                        fn: function (action) {
                            root.comchangeOrderState('/order/cheatOrder')(action);
                        }
                    })

                }
            },

            //2.3-批量删除订单
            "#batDelete": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (!root.checkOrderState('OrderList')) return;


                    //先弹出操作确认警告，再概据用户交互做回调
                    com.commonMsg({
                        msg: '你确定要删除订单吗，删除订单后不可复原!',
                        fn: com.doAction({
                            url: '/order/delete',
                            params: {
                                orderIds: com.getGridSelsId('OrderList').join(',')
                            },
                            successTipMsg: '订单删除成功',
                            successCall: root.orderComCallback
                        })
                    });
                }
            },
            "#batCheck": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    //if (!root.checkOrderState('OrderList')) return;


                    //先弹出操作确认警告，再概据用户交互做回调
                    com.commonMsg({
                        msg: '你是否要执行批量审核?',
                        fn: com.doAction({
                            url: '/order/check',
                            params: {
                                orderIds: com.getGridSelsId('OrderList').join(',')
                            },
                            successTipMsg: '批量审核成功',
                            successCall: root.orderComCallback
                        })
                    });
                }
            },
            //2.4-批量改状态
            "#batEditState": {
                click: function () {

                    if (!Espide.Common.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (!this.checkOrderState('OrderList')) return;


                    //如果"批量改状态"视图已绘制就返回
                    if (Ext.getCmp('batchStateWin')) return false;


                    Ext.widget('batchState').show();
                }
            },

            //2.5-批量改状态确定按钮
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
                            url: '/order/updateStautsByOrder',
                            params: params,
                            successTipMsg: '订单批量修改成功',
                            successCall: function () {
                                com.reLoadGird('OrderList', false, false);
                                //Ext.getCmp('orderItem').hide();
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

            //2.7-弹出联想单号窗口
            "#autoEditNum": {
                click: function () {

                    if (!Espide.Common.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (!root.checkOrderState('OrderList')) return;


                    //如果"联想单号"视图已绘制就返回
                    if (Ext.getCmp('autoNumberWin')) return;

                    Ext.widget('autoNumber').show();
                }
            },

            //2.8-联想单号确定按钮
            "#autoNumberWin #comfirm": {
                click: function (btn) {
                    var com = Espide.Common,
                        params = btn.up('form').getValues();

                    if (!btn.up('form').isValid()) return;

                    if (params.shippingComp === "shunfeng" && !/^\d{12}$/.test(params.intNo)) {
                        Ext.Msg.alert('警告', "顺丰物流单号必须是12位的数字");
                        btn.up('form').down('[name=intNo]').focus();
                        return false;
                    } else if (params.shippingComp === "ems" && !/^\d{13}$/.test(params.intNo)) {
                        Ext.Msg.alert('警告', "EMS物流单号必须是13位的数字或数字");
                        btn.up('form').down('[name=intNo]').focus();
                        return false;
                    }

                    params['orderIds'] = com.getGridSelsId('OrderList').join(',');

                    com.commonMsg({
                        msg: '你确定要联想单号吗，修改订单后不可复原!',
                        fn: com.doAction({
                            url: '/order/updateShipping',
                            params: params,
                            successTipMsg: '联想单号成功',
                            successCall: function () {
                                com.reLoadGird('OrderList', false, true);
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

            //2.10-导入进销存
            "#importOrder": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (!root.checkOrderState('OrderList')) return;

                    if (!com.hasEmpytData(com.getGridSels('OrderList', 'shippingComp'))) {
                        Ext.Msg.alert({
                            title: '警告！',
                            msg: '选中的订单中含有没有指派物流公司的订单，请指定后再导入',
                            icon: Ext.Msg.WARNING,
                            buttons: Ext.Msg.YES
                        });
                        return;
                    }

                    Ext.Msg.confirm('提示', '是否将选中订单导入进销存？', function (btn) {
                        if (btn == 'yes') {
                            root.comchangeOrderState('/order/confirm')('yes');
                        }

                    });


                }
            },

            //2.11-标记打印
            "#signPrinter": {
                click: function () {
                    var flag = true,
                        root = this,
                        com = Espide.Common,
                        shippingNums = com.getGridSels('OrderList', 'shipping_no') || [];

                    if (shippingNums.length === 0) {
                        Ext.Msg.alert('警告', '请至少选择一项订单');
                    }

                    if (!root.checkOrderState('OrderList')) return;


                    Ext.each(shippingNums, function (num, index, nums) {
                        if (Espide.Common.isEmptyData(num)) {
                            return flag = false;
                        }
                    });

                    if (!flag) {
                        Ext.Msg.alert('警告', '请修正没有写入订单编号的订单再提交');
                    } else {
                        com.commonMsg({
                            msg: '你确定要标记打印这些订单吗?',
                            fn: com.doAction({
                                url: '/assets/js/order/data/orderList.json',
                                params: {
                                    orderIds: com.getGridSelsId('OrderList').join(',')
                                },
                                successTipMsg: '订单标记成功',
                                successCall: root.orderComCallback
                            })
                        })
                    }
                }
            },

            //2.12-给订单增加商品
            "#addGood": {
                click: function () {

                    var com = Espide.Common,
                        sels = com.getGridSels('OrderList', 'repoId');

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (sels.length > 1 && !com.isArrAllEqual(sels)) {
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: '选中的订单来自不同的仓库，不能批量加入商品!',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                        return;
                    }

                    if (!this.checkOrderState('OrderList')) return;

                    if (Ext.getCmp('addGoodWin')) return;

                    //显示加产品窗口
                    Ext.widget('addGood').show();

                }
            },
            '#cancel': {
                click: function (btn) {
                    btn.up('window').destroy();
                }
            },
            //2.13-加订单
            "#addOrder": {
                click: function () {
                    if (Ext.getCmp('addOrderWin')) return;
                    Ext.widget('addOrderWin').show(this, function () {

//                        var records = Ext.getCmp('OrderList').getSelectionModel().getSelection();
//
//
//                        if (Espide.Common.isGridSel('OrderList')) {//判断是否选择了订单列表
//                            console.log(records[0].data.orderType);
////                            records[0].set('orderType',)
//                            //Ext.getCmp('AddOrderInfo').getForm().loadRecord(records[0]);
//                        }

                    });
                }
            },
            //加订单 （售后换货订单）
            '#addOrderChange': {
                click: function () {
                    if (Ext.getCmp('addOrderChange')) return;

                    Ext.widget('addOrderChange').show(this, function () {


                        var searchOrderStore = Ext.getCmp('AddOrderChangeSearch').getStore(),
                            AddOrderChangeListStore = Ext.getCmp('AddOrderChangeList').getStore(),
                            AddOrderChangeCartStore = Ext.getCmp('AddOrderChangeCart').getStore();

                        //清空上次数据

                        if (AddOrderChangeListStore.count() >= 0) {
                            AddOrderChangeListStore.removeAll();
                        }

                        if (AddOrderChangeCartStore.count() >= 0) {
                            AddOrderChangeCartStore.removeAll();
                        }


                        //判断是否选择了订单数据
                        if (Espide.Common.isGridSel('OrderList')) {
                            var records = Ext.getCmp('OrderList').getSelectionModel().getSelection();
                            //重新组合record数据
                            delete records[0].data['shopId'];
                            delete records[0].data['shopName'];
                            delete records[0].data['orderType'];
                            records[0].data['platformType'] = records[0].data.outPlatformType.name;

                            Ext.getCmp('AddOrderChangeInfo').getForm().loadRecord(records[0]);
                            Ext.getCmp('keyword').setValue(records[0].get('platformOrderNo'));

                            //联动店铺数据
                            var shopId = Ext.getCmp("shopGo");
                            shopId.reset();
                            shopId.getStore().proxy.url = '/order/shopList?platformType=' + records[0].data['platformType'];
                            shopId.getStore().load();

                            //设置订单类型
                            if (!records[0].data['orderType']) {
                                Ext.getCmp('orderType').setValue('EXCHANGE');
                            }
                        } else {
                            if (searchOrderStore.count() > 0) {
                                searchOrderStore.removeAll();
                            }
                        }


                    });
                }
            },
            //加订单 （补货）
            '#replenishment': {
                click: function () {
                    if (Ext.getCmp('ReplenishmentWin')) return;
                    Ext.widget('ReplenishmentWin').show(this, function () {

                        var ReplenishmentAddGoodListStore = Ext.getCmp('ReplenishmentAddGoodList').getStore(),

                            ReplenishmentAddGoodCartStore = Ext.getCmp('ReplenishmentAddGoodCart').getStore();


                        //判断是否选择了订单数据
                        if (Espide.Common.isGridSel('OrderList')) {
                            var records = Ext.getCmp('OrderList').getSelectionModel().getSelection();

                            //重新组合record数据
                            delete records[0].data['shopId'];
                            delete records[0].data['shopName'];
                            delete records[0].data['orderType'];
                            records[0].data['platformType'] = records[0].data.outPlatformType.name;
                            Ext.getCmp('ReplenishmentOrderInfo').getForm().loadRecord(records[0]);

                            //联动店铺数据
                            var shopId = Ext.getCmp("shopGo");
                            shopId.reset();
                            shopId.getStore().proxy.url = '/order/shopList?platformType=' + records[0].data['platformType'];
                            shopId.getStore().load();

                            //设置订单类型
                            if (!records[0].data['orderType']) {
                                Ext.getCmp('orderType').setValue('REPLENISHMENT');
                            }
                        }

                        //清空上一次数据
                        if (ReplenishmentAddGoodListStore.count() >= 0 || ReplenishmentAddGoodCartStore.count() >= 0) {
                            ReplenishmentAddGoodListStore.removeAll();
                            ReplenishmentAddGoodCartStore.removeAll();
                        }


                    });
                }
            },
            //2.14-复制订单
            "#copyOrder": {
                click: function () {

                    var root = this,
                        com = Espide.Common,
                        records = Ext.getCmp('OrderList').getSelectionModel().getSelection();

                    if (!com.checkGridSel('OrderList', '请至少选择一项订单'))  return;

                    if (records.length > 1) {
                        Ext.Msg.alert({
                            title: '警告！',
                            msg: '只能选中一项订单进行复制作',
                            icon: Ext.Msg.WARNING,
                            buttons: Ext.Msg.YES
                        });
                        return;
                    }

                    if (Ext.getCmp('addOrderWin')) return;
                    Ext.widget('addOrderWin').show();
                    //Ext.getCmp('AddOrderInfo').getForm().loadRecord(records[0]);

                    function loadOrderItem() {
                        var records = Ext.getCmp('orderItem').getStore().data.items;
                        if (records.length > 0) {
                            var newRecords = [];
                            Ext.each(records, function (item, index, items) {
                                newRecords.push(item.data);
                            });
                            Ext.getCmp('goodCart').getStore().loadData(newRecords);
                        }
                    }

                    loadOrderItem();
                }
            },

            //2.15-汇总订单
            "#orderCollect": {
                click: function () {

                }
            },

            //2.16-刷新
            "#orderRefresh": {
                click: function () {
                    Espide.Common.reLoadGird('OrderList', false, false);
                }
            },
            //导入订单
            //导入
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
                                name: "multipartFile",
                                labelWidth: 80,
                                width: 300,
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
                                                location.href = "/static/templet/leadInOrderExcelModel.xlsx";
                                            }
                                        }
                                    }
                                ]}
                        ]


                    });


                    var includeWin = Ext.create("Ext.window.Window", {
                        title: '导入订单',
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
                                            url: "/order/leadInOrders",
                                            waitMsg: "正在导入验证数据",
                                            success: function (fp, o) {
                                                var data = Ext.JSON.decode(o.response.responseText);
                                                if (data.success) {
                                                    Espide.Common.tipMsgIsCloseWindow(data, includeWin, 'OrderList', true);
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
    },

    //订单页通用操作
    /**
     * 根据某一字段取出订单对应商品信息
     * @param fld {String} 字段名
     */
    getOrderGood: function (fld) {
        var store = Ext.getCmp('orderItem').getStore();
        var ExamineListStore = Ext.getCmp('ExamineList').getStore();
        var OrderLogStore = Ext.getCmp('OrderLog').getStore();

        store.load({
            params: {
                orderIds: fld
            }
        });

        ExamineListStore.load({
            params: {
                orderIds: fld
            }
        });

        OrderLogStore.load({
            params: {
                orderIds: fld
            }
        });


        OrderLogStore
    },

    /**
     * 订单页通用操作回调函数
     */
    orderComCallback: function () {
        Espide.Common.reLoadGird('OrderList', false, true);
        Ext.getCmp('orderItem').getStore().reload();
    },
    //平台类型判断
    checkPlatform: function (grid, platform) {
        var com = Espide.Common,
            outPlatformTypes = com.getGridSels(grid, 'outPlatformType'),
            platArray = ['天猫', '京东'], flag = true;


        Ext.each(outPlatformTypes, function (type, key) {
            //判断是否在京东或者天猫平台  才能嘻唰唰
            if (!Ext.Array.contains(platArray, type.value)) {
                Ext.Msg.alert({
                    title: '警告！',
                    msg: '选中的订单中含有非[天猫或者京东]平台的订单，请重新选择',
                    icon: Ext.Msg.WARNING,
                    buttons: Ext.Msg.YES
                });
                flag = false;
            }
        });

        return flag;
    },
    //订单类型判断
    checkOrderType: function (grid, type) {
        type = type || 'NORMAL';
        var com = Espide.Common,
            orderTypes = com.getGridSels(grid, 'orderType'),
            msg = {
                NORMAL: '正常订单'
            };

        if (!(com.isArrAllEqual(orderTypes) && orderTypes[0].name === type )) {
            Ext.Msg.alert({
                title: '警告！',
                msg: '选中的订单中含有非' + msg[type] + '类型的订单，请重新选择',
                icon: Ext.Msg.WARNING,
                buttons: Ext.Msg.YES
            });
            return false;
        }
        return true;
    },
    //订单状态判断
    checkOrderState: function (grid, state) {
        state = state || 'WAIT_PROCESS';
        var com = Espide.Common,
            orderStates = com.getGridSels(grid, 'orderStatus'),
            msg = {
                WAIT_PROCESS: '待处理',
                INVALID: '已做废',
                PRINTED: '已打印',
                EXAMINED: '已验货',
            };

        if (!(com.isArrAllEqual(orderStates) && orderStates[0].name === state )) {

            Ext.Msg.alert({
                title: '警告！',
                msg: '选中的订单中含有非' + msg[state] + '状态的订单，请重新选择',
                icon: Ext.Msg.WARNING,
                buttons: Ext.Msg.YES
            });

            return false;
        }
        return true;
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
});