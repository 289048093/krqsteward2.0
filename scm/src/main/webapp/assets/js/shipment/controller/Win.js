/**
 * Created by king on 13-12-23
 */

Ext.define('Supplier.controller.Win', {
        extend: 'Ext.app.Controller',
        views: ['win.ExamineGood'],
        init: function () {
            this.control({
                //验货窗口
                //验单
                "#examineGoodWin #orderSearch": {
                    click: function (button) {
                        var win = button.up("#examineGoodWin"),
                            orderGrid = button.up("#order"),
                            goodGrid = win.down("#good"),
                            msgTip = orderGrid.down('#displayMsg'),
                            OrderArr = [],
                            waybillNumber = button.up("#order").down("textfield[name=order_num]").getValue(),
                            examineGoodArr = (window.examineGoodArr = []);

                        waybillNumber = waybillNumber.trim();
                        if (waybillNumber && waybillNumber.length > 0) {
                            Ext.Ajax.request({
                                url: '/invoice/inspection',
                                params: {
                                    shippingNos: waybillNumber
                                },
                                success: function (response) {
                                    var responseData = Ext.JSON.decode(response.responseText),
                                        data = responseData.data;
                                    if (data && data.list && responseData.success && data.list.length > 0) {

                                        for (var i = 0, j = data.list.length; i < j; i++) {
                                            OrderArr.push(data.list[i]['id']);
                                        }

                                        goodGrid.down("#orderNum").setValue(OrderArr.join(','));

//                                        for (var m = 0, n = data.list[0].orderItemInspectionVos.length; m < n; m++) {
//                                            examineGoodArr.push(data.list[0].orderItemInspectionVos[m]['skuCode']);
//                                        }

                                        for (var m = 0, n = data.list[0].orderItemInspectionVoMapList.length; m < n; m++) {
                                            examineGoodArr.push(data.list[0].orderItemInspectionVoMapList[m]['skuCode']);
                                        }


                                        msgTip.hide();
                                        orderGrid.getStore().removeAll();
                                        goodGrid.show();
                                        //orderGrid.getStore().add(data.list);
                                        orderGrid.getStore().loadRawData(data.list);
                                        goodGrid.getStore().removeAll();

                                        goodGrid.getStore().loadRawData(data.list[0].orderItemInspectionVoMapList);


                                        //console.log(data.list);
                                        //goodGrid.getStore().loadRawData(data.list);

                                    } else {
                                        orderGrid.getStore().removeAll();
                                        goodGrid.getStore().removeAll();
                                        goodGrid.hide();
                                        msgTip.show();
                                    }
                                },
                                failure: function () {
                                    //console.log('failure');
                                }
                            })
                        }
                    }
                },

                //验货
                "#examineGoodWin #goodSearch": {
                    click: function (button) {
                        var goodGrid = button.up("#good"),
                            goodGridStore = goodGrid.getStore(),
                            goodNum = goodGrid.down("#goodNum").getValue(),
                            trueMsg = goodGrid.down("#trueMsg"),
                            falseMsg = goodGrid.down("#falseMsg"),
                            goodArr = window.examineGoodArr;

                        goodNum = goodNum.trim();
                        if (goodNum) {
                            var index = goodGridStore.find('skuCode', goodNum, 0, false, false, true),



                                flag = Ext.Array.contains(goodArr, goodNum) || Ext.Array.contains(goodArr, goodNum.toUpperCase());

                                var returnArray = goodGridStore.queryBy(function(record){
                                      return record.get('skuCode')==goodNum;
                                });



                           //获取全部的 internalId
                            var indexGood = [];
                            goodGridStore.each(function(record){
                                indexGood.push(record.internalId);
                            });


                           //获取过滤后的    internalId
                            var indexArray = [];
                            returnArray.each(function(record){
                                indexArray.push(record.internalId);
                            })

                            //过滤后在全部的索引值
                            var indexCollection = [];

                           Ext.Array.each(indexGood,function(indexGood,i){
                               Ext.Array.each(indexArray,function(indexArray,j){
                                   if(indexGood == indexArray){
                                       indexCollection.push(i);
                                   }
                               })
                           });




                            if (index !=-1){
                                if (!flag){
                                    trueMsg.hide();
                                    falseMsg.setValue('<span style="color:#E47113">该商品已验证通过!</span>');
                                    falseMsg.show();
                                }else{
                                    trueMsg.show();
                                    falseMsg.hide();

                                    Ext.Array.each(indexCollection,function(indexCollection,index){
                                        var currentNode = goodGrid.getView().getNode(parseInt(indexCollection));
                                        var currentElement = Ext.get(currentNode);
                                        currentElement.animate({
                                            duration: 1000,
                                            keyframes: {
                                                40: {
                                                    backgroundColor: "#669900"
                                                },
                                                60: {
                                                    opacity: 0.8
                                                }
                                            }
                                        });
                                    });



                                    Ext.Array.remove(goodArr, goodNum);
                                    Ext.Array.remove(goodArr, goodNum.toUpperCase());
                                    if (goodArr.length === 0) {
                                        Ext.Ajax.request({
                                            url: '/invoice/batch_examine',
                                            params: {
                                                orderIds: goodGrid.down('#orderNum').getValue()
                                            },
                                            success: function (response) {
                                                var data = Ext.decode(response.responseText);
                                                if (data.success) {
                                                    Ext.Function.defer(function () {
                                                        var nodes = goodGrid.getView().getNodes();
                                                        for (var i = 0, j = nodes.length; i < j; i++) {
                                                            if (i === j - 1) {
                                                                Ext.get(nodes[i]).animate({
                                                                    duration: 1000 * (j - i),
                                                                    to: {
                                                                        opacity: 0
                                                                    },
                                                                    listeners: {
                                                                        afteranimate: function () {
                                                                            goodGrid.hide();
                                                                            goodGrid.up("window").down("#order").getStore().removeAll();
                                                                            goodAmount = [];
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                button.up('#examineGoodWin').down('#order').getStore().removeAll();
                                                                Ext.get(nodes[i]).animate({
                                                                    duration: 1000 * (j - i),
                                                                    to: {
                                                                        opacity: 0
                                                                    }
                                                                });
                                                            }

                                                        }
                                                    }, 1500);
                                                    Ext.Function.defer(function () {
                                                        Ext.Msg.show({
                                                            title: '提示',
                                                            msg: '选中物流单号已验货成功',
                                                            buttons: Ext.MessageBox.YES,
                                                            fn: function () {
                                                                trueMsg.hide();
                                                                goodGrid.down("#goodNum").setValue('');
                                                                //root.reloadGridData(true);
                                                                //window.examineGoodArr = [];
                                                            }
                                                        });
                                                    }, 2500);
                                                }
                                            }
                                        });
                                    }
                                }
                            }else{
                                trueMsg.hide();
                                falseMsg.setValue('<span style="color:#E47113">未扫描到该商品!</span>');
                                falseMsg.show();
                            }

                        }
                    }
                },

                //验货完成
                "#examineGoodWin": {
                    close: function () {
                        Espide.Common.reLoadGird('OrderList', 'search',true,function(){

                            //设置按钮
                            var orderGridToolBar = Ext.getCmp('search').getDockedItems('toolbar[dock="top"]')[0];

                            Ext.each(orderGridToolBar.items.items, function (item, index, items) {
                                //按钮替换
                                if ( item.itemId=='PRINTED') {
                                    item.setDisabled(true);
                                } else {
                                    item.enable(true);
                                }
                            });
                        });
                    }
                }
            });
        }
    }
);