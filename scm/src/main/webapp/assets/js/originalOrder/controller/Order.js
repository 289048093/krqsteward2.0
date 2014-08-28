/**
 * Created by king on 13-12-17
 */

Ext.define('Supplier.controller.Order', {
    extend: 'Ext.app.Controller',
    views: ['Order', 'Search', 'List', 'Item', 'ItemTab', 'OrderLog'],
    stores: ['OrderList', 'ItemList', 'StorageAll', 'Brand', 'ShopAll', 'Shop','OrderLog'],
    models: ['OrderList', 'ItemList'],
    init: function () {
        this.control({

            //1-表格操作
            "#list": {

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

                    store.getProxy().extraParams = Ext.getCmp('search').getValues();
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


            "#item": {
                //判断是否可以编辑
                beforeedit: function (editor, e) {

                    //判断是否选择了单个订单
                    if(!Espide.Common.isGridSingleSel('OrderList')){
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: '请选择一条订单',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                        return;
                    }

                    //判断是否被解析
                   var processed =  Espide.Common.getGridSels('OrderList','processed').join(',');
                    if(processed == '已被解析'){
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: '已被解析不能编辑sku',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                        return;
                    }

                    //判断是否被解析
                    var discard =  Espide.Common.getGridSels('OrderList','discard').join(',');
                    if(discard == '失效'){
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: '失效订单不能编辑sku',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                        return;
                    }
                },

            },

            //搜索按钮
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

            //2.2-订单作废
            "#cancelOrder": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;
                    //检测是否全部是未被解析 状态
                    if (!root.checkOrderState('OrderList','processed')) return;
                    //检测是否全部是  有效
                    if (!root.checkOrderState('OrderList','discard')) return;

                    com.commonMsg({
                        msg: '您确定要作废此订单?',
                        fn: function (action) {
                            root.comchangeOrderState('/dealOriginalOrder/cancellation')(action);
                        }
                    })
                }
            },

            //2.2.1-订单恢复
            "#recoverOrder": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;

                    //检测是否全部是  无效
                    if (!root.checkOrderState('OrderList','good')) return;

                    com.commonMsg({
                        msg: '您确定要恢复此订单?',
                        fn: function (action) {
                            root.comchangeOrderState('/dealOriginalOrder/recover')(action);
                        }
                    })
                }
            },
            //解析订单
            "#processedOrder": {
                click: function () {
                    var root = this,
                        com = Espide.Common;

                    if (!com.checkGridSel('OrderList')) return;

                    //检测是否全部是未被解析 状态
                    //if (!root.checkOrderState('OrderList','unprocessed')) return;
                    //检测是否全部是  有效
                    if (!root.checkOrderState('OrderList','discard')) return;

                    com.commonMsg({
                        msg: '您确定要解析此订单?',
                        fn: function (action) {

                            var com = Espide.Common,url ='/dealOriginalOrder/analyze';
                            return com.doAction({
                                url:url,
                                params: {
                                    id: com.getGridSelsId('OrderList').join(',')
                                },
                                successTipMsg: '请求成功,请稍后查看解析结果(解析时间最长1分钟)',
                                successCall: function(){
                                    com.showGridSelErr('请求成功,请稍后查看解析结果(解析时间最长1分钟)');
                                    Espide.Common.reLoadGird('OrderList', false, true);
                                    Ext.getCmp('orderItem').getStore().reload();
                                }
                            })(action);
                            //root.comchangeOrderState('/dealOriginalOrder/analyze')(action);
                        }
                    })
                }
            },

            //2.16-刷新
            "#orderRefresh": {
                click: function () {
                    Espide.Common.reLoadGird('OrderList', false, false);
                }
            },

        });
    },

    //订单页通用操作
    /**
     * 根据某一字段取出订单对应商品信息
     * @param fld {String} 字段名
     */
    getOrderGood: function (fld) {
        var store = Ext.getCmp('orderItem').getStore();
        //var ExamineListStore = Ext.getCmp('ExamineList').getStore();
        var OrderLogStore = Ext.getCmp('OrderLog').getStore();

        store.load({
            params: {
                orderIds: fld
            }
        });

        OrderLogStore.load({
            params: {
                orderIds: fld
            }
        });

    },

    /**
     * 订单页通用操作回调函数
     */
    orderComCallback: function () {
        Espide.Common.reLoadGird('OrderList', false, true);
        Ext.getCmp('orderItem').getStore().reload();
    },
    //通用修改订单状态
    comchangeOrderState: function (url) {
        var com = Espide.Common;
        return com.doAction({
            url: url,
            params: {
                id: com.getGridSelsId('OrderList').join(',')
            },
            successTipMsg: '状态修改成功',
            successCall: this.orderComCallback
        })
    },

    checkOrderState: function (grid, state) {
        var com = Espide.Common,
            msg = {
                processed: '已被解析',
                unprocessed: '未被解析',
                discard: '失效',
                good:'正常'
            };

            switch (state){
                case 'good':
                    stateArray = com.getGridSels(grid,'discard');
                    break;
                case 'unprocessed':
                    stateArray = com.getGridSels(grid,'processed');
                    break;
                default :
                    stateArray = com.getGridSels(grid,state);
            }

            if(Ext.Array.contains(stateArray,msg[state])){
                Ext.Msg.alert({
                    title: '警告！',
                    msg: '选中的订单中含有' + msg[state] + '状态的订单，请重新选择',
                    icon: Ext.Msg.WARNING,
                    buttons: Ext.Msg.YES
                });
                return false;
            }

        return true;
    },


});