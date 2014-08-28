/**
 * 快递单查询
 */

Ext.define('EBDesktop.LogisticsQuery', {
    extend: 'Ext.ux.desktop.Module',

    id: 'logisticsquery-win',

    init: function () {
        this.launcher = {
            text: '快递单查询',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            root = this,
            win = desktop.getWindow('logisticsquery-win'),
            configStore = root.self.getExpressStore(),
            getDetailsStore = root.self.getDetailsStore();

        if (!win) {
            win = desktop.createWindow({
                title: '快递单查询',
                id: 'logisticsquery-win',
                collapsible: true,
                maximizable: true,
                modal: false,
                layout: 'border',
                width: 1000,
                height: 602,
                items: [
                    root.self.createLeftform(),
                    root.self.createExpressGrid(configStore),
                    root.self.createExpressDetail(getDetailsStore)
                ]
            })
        }
        return win;
    },
    statics: {
        createLeftform: function () {
            return Ext.create('Ext.form.Panel', {
                region: 'west',
                id: 'expressForm',
                width: 200,
                height: 'auto',
                bodyPadding: 10,
                layout: 'anchor',
                border: 0,
                defaults: {
                    xtype: 'textfield',
                    margin: '0 0 5 0',
                    labelWidth: 100,
                    labelAlign: 'top',
                    width: 180,
                    queryMode: 'local',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false
                },
                items: [
                    { name: 'expressNos', fieldLabel: '填写快递单', xtype: 'textareafield', height: 300, minLength: 4, maxLength: 2000 },
                    {
                        xtype: 'button',
                        text: '查询快递单',
                        margin: '0 8 0 0',
                        width: 80,
                        itemId: 'searchBtn',
                        handler: function () {
                            Espide.Common.doSearch("expresslist", "expressForm", true);
                        }
                    },
                    {
                        xtype: 'button',
                        text: '导出Excel',
                        margin: '0 0 0 0',
                        width: 70,
                        itemId: 'outPut',
                        handler: function () {
                            var formParams = Ext.getCmp('expressForm').getValues();
                            if (formParams.expressNos != "") {
                                window.open('/logistics/exportByExpressNos?expressNos=' + encodeURIComponent(formParams.expressNos));
                            } else {
                                Espide.Common.tipMsg('提示', '快递单不能为空');
                            }

                        }
                    }

                ]
            });
        },
        //生成店铺管理表格
        createExpressGrid: function (store) {
            return Ext.create('Ext.grid.Panel', {
                region: 'east',
                id: 'expresslist',
                store: store,
                forceFit: true,
                width: 780,
                height: 'auto',
                loadMask: true,
                border: 5,
                split: true,
                columns: [
                    {
                        text: '物流单号', dataIndex: 'expressNo', width: 80
                    },
                    {
                        text: '订单号', dataIndex: 'orderNo', width: 80
                    },
                    {
                        text: '物流公司', dataIndex: 'expressCompanyName', width: 70
                    },
                    {
                        text: '收货地址', dataIndex: 'sendTo', width: 100
                    },
                    {
                        text: '物流状态', dataIndex: 'expressStatusName',
                        width: 80
                    },
                    {
                        text: '第三方请求', dataIndex: 'wasRequestName',
                        width: 90
                    },
                    {
                        text: '物流记录时间', dataIndex: 'firstTime', width: 140, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        text: '最新物流记录时间', dataIndex: 'latestTime', width: 150, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    }

                ],
                listeners: {
                    selectionchange: function (sm, records) {
                        var record = records[records.length - 1];
                        if (record) {
                            var store = Ext.getCmp('ExpressDetails').getStore();
                            store.load({
                                params: {
                                    expressNo: record.get('expressNo')
                                }
                            });

                            Ext.getCmp('ExpressDetails').show();
                        } else {
                            Ext.getCmp('ExpressDetails').hide();
                        }
                    }
                }
            });
        },
        createExpressDetail: function (store) {//详细表格
            return Ext.create('Ext.grid.Panel', {
                region: 'south',
                id: 'ExpressDetails',
                store: store,
                height: 200,
                split: true,
                forceFit: true,
                hidden: true,
                loadMask: true,
                border: 5,
                columns: [
                    {
                        text: '物流信息', dataIndex: 'context', width: 450
                    },
                    {
                        text: '物流时间', dataIndex: 'transferTime', width: 450, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    }
                ]
            });
        },


        //物流查询store
        getExpressStore: function () {

            Ext.define('ShopModel', {
                //不要忘了继承
                extend: 'Ext.data.Model',
                fields: [
                    'id',
                    'orderNo',   //订单号
                    'expressNo', //物流单号
                    'expressCompany',//物流公司
                    'expressCompanyName',//物流公司
                    'sendTo', //收货地址
                    'expressInfo',//物流信息
                    'expressStatus',//物流状态  1 表示配送完成  0 未配送完成
                    'expressStatusName',
                    'wasRequestName',
                    {name: 'firstTime', type: 'date', dateFormat: 'time'},
                    {name: 'latestTime', type: 'date', dateFormat: 'time'}

                ],
                idProperty: 'id'
            });

            return Ext.create('Ext.data.Store', {
                model: 'ShopModel',
                proxy: {
                    type: 'ajax',
                    url: '/logistics/findByExpressNo',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.list',
                        messageProperty: 'msg'
                    },
                    listeners: {
                        exception: function (proxy, response, operation) {
                            var data = Ext.decode(response.responseText);
                            Ext.MessageBox.show({
                                title: '警告',
                                msg: data.msg,
                                icon: Ext.MessageBox.ERROR,
                                buttons: Ext.Msg.OK
                            });
                        }
                    }
                },
                autoLoad: false

            });
        },
        //生成店铺管理store
        getDetailsStore: function () {

            Ext.define('DetailsModel', {
                //不要忘了继承
                extend: 'Ext.data.Model',
                fields: [
                    'context',   //物流信息描述
                    {name: 'transferTime', type: 'date', dateFormat: 'time'} //物流时间

                ],
                idProperty: 'context'
            });

            return Ext.create('Ext.data.Store', {
                model: 'DetailsModel',
                proxy: {
                    type: 'ajax',
                    url: '/logistics/detailByExpressNo',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.list',
                        messageProperty: 'msg'
                    },
                    listeners: {
                        exception: function (proxy, response, operation) {
                            var data = Ext.decode(response.responseText);
                            Ext.MessageBox.show({
                                title: '警告',
                                msg: data.msg,
                                icon: Ext.MessageBox.ERROR,
                                buttons: Ext.Msg.OK
                            });
                        }
                    }
                },
                autoLoad: false

            });
        }
    }
});