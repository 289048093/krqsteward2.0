/**
 * Created by king on 13-12-23
 */

Ext.define('Returnvisit.controller.Returnvisit', {
        extend: 'Ext.app.Controller',
        views: [
            'Returnvisit',
            'Search',
            'List',
            'allocationWin.AllocationWin',
            'allocationWin.EmployeeCart',
            'allocationWin.EmployeeList',
        ],
        models: ['List'],
        stores: ['List', 'EmployeeCart'],
        init: function () {
            this.control({
                '#confirmBtn':{
                    'click': function (btn) {
                        var com = Espide.Common;

                        com.doFormCheck(Ext.getCmp('search').getForm(), function () {
                            com.reLoadGird('List', 'search', true);
                        }, '请正确输入要搜索的信息!')
                    }
                },
                '#allocation': {
                    click: function (btn) {
                        Ext.widget('AllocationWin').show(this, function () {

                            var EmployeeListStore = Ext.getCmp('EmployeeList').getStore();
                            var EmployeeCartStore = Ext.getCmp('EmployeeCart').getStore();


                            if(EmployeeCartStore.count()>= 0){
                                EmployeeCartStore.removeAll();
                            }

                            var ids = Espide.Common.getGridSelsId('List');

                            EmployeeListStore.load({
                                params: {
                                    roleName: '回访专员'
                                }
                            });
                            Ext.getCmp('total').setValue(ids.length);


                        });
                    }
                },
                '#addRow': {
                    click: function (button, rowIndex, colIndex, item, e, selected) {

                        var arr = [];
                        //判断购物车是否已有要添加的商品
                        function isGoodAdded(goodId) {
                            var flag = false,
                                goodCartItems = Ext.getCmp('EmployeeCart').getStore().data.items;
                            Ext.each(goodCartItems, function (record, index, root) {
                                if (record.get('employeeId') == goodId) {
                                    return flag = true;
                                }
                            });
                            return flag;
                        }

                        Ext.each(selected, function (record, index, records) {
                            if (isGoodAdded(record.get('id'))) {
                                Ext.Msg.alert('警告', '回访专员已经分配任务！');
                                return flag = false;
                            }



                            var newdata = Ext.create('Returnvisit.model.EmployeeCart', {
                                'employeeId':record.get('id'),
                                'username':record.get('username'),
                                'employeeName':record.get('name'),
                                'apportionCount':0,
                            });

                            arr.push(newdata);

                        });


                        Ext.getCmp('EmployeeCart').getStore().add(arr);


                    }
                },
                '#allocationTotal':{
                    change:function(textfield, newValue, oldValue, eOpts){
                        if(newValue>Ext.getCmp('total').getValue()){
                            Espide.Common.showGridSelErr('分配任务不得超过待分配数量!');
                            textfield.setValue(oldValue);
                            return;
                        }
                    }
                },
                //加产品提交订单
                "#AllocationWin #submit": {
                    click: function (btn) {
                        var com = Espide.Common,
                            goodCartStore = Ext.getCmp('EmployeeCart').getStore(),
                            ids = com.getGridSelsId('List');
                        console.log(ids);

                        goodCartStore.getProxy().api.create = "/returnvisit/apportionReturnVisitTask";

//                        goodCartStore.getProxy().extraParams = {
//                            orderNos: com.getGridSels('OrderList', 'orderNo').join(','),
//                            orderItemType: Ext.getCmp('goodList').down("#searchType").getValue()
//                        };


                        goodCartStore.getProxy().extraParams['taskIds'] = ids.join(',');

                        goodCartStore.sync();



                    }
                },
            });
        }
    }
);