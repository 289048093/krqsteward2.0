/**
 * 查询抓取的订单
 * Created by Lein xu.
 */
Ext.Loader.setConfig({
    enabled: true,
    paths: {
        'Go': 'assets/js/shipment/dateExtend/'
    }
});

Ext.define('EBDesktop.OrderFetch', {
    extend: 'Ext.ux.desktop.Module',

    id: 'orderfetch-win',

    init: function () {
        this.launcher = {
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            root = this,
            win = desktop.getWindow('orderfetch-win'),
            OrderFetchStore = root.self.getConfigStore();

        if (!win) {
            win = desktop.createWindow({
                title: '订单记录',
                id: 'orderfetch-win',
                collapsible: true,
                maximizable: true,
                modal: false,
                border: 0,
                layout: 'border',
                fixed: true,
                width: 1000,
                height: 602,
                items: [
                    // root.self.orderFetchLogGrid(OrderFetchStore)
                    root.self.orderFetchTab()
                ]
            })
        }
        return win;
    },


    statics: {

        //tab容器
        orderFetchTab: function () {
            return Ext.create('Ext.tab.Panel', {
                extend: 'Ext.tab.Panel',
                region: 'south',
                alias: 'widget.OrderFetchTab',
                id: 'OrderFetchTab',
                itemId: 'OrderFetchTab',
                height: 560,
                border: 0,
                forceFit: false,
                width: 'auto',
                activeTab: 0,
                defaults: {
                    bodyPadding: 0,
                },
                items: [
                    {
                        title: '自动抓取日志',
                        layout: 'fit',
                        items: [

                            this.orderFetchLogGrid(this.getConfigStore()),
                        ]

                    },
//                    {
//                        title: '手动抓单日志',
//                        layout: 'fit',
//                        items: [
//                            {
//                                xtype: 'OrderManual',
//                                autoScroll :true,
//                            }
//                        ]
//
//                    },
                    {
                        title: '手动抓单日志',
                        layout: 'fit',
                        items: [
                            this.OrderManual(this.getOrderManualStore()),
                        ],
                        listeners: {
                            afterrender: function (t, opt) {
                                setTimeout(function () {
                                    Ext.getCmp('OrderManual').getStore().load();
                                }, 10);
                            }
                        }

                    }
                ]

            })
        },


        //自动抓取日志表格
        orderFetchLogGrid: function (store) {
            return Ext.create('Ext.grid.Panel', {
                region: 'west',
                alias: 'widget.OrderFetchList',
                id: 'OrderFetchList',
                itemId: 'OrderFetchList',
                store: store,
                forceFit: true,
                columns: [
                    {
                        text: '抓取时间',
                        dataIndex: 'fetchTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        text: '抓取平台',
                        width: 80,
                        dataIndex: 'platformType',
                        renderer: function (value) {
                            return value['value'];
                        }

                    },
                    {
                        text: '抓取类型',
                        width: 80,
                        dataIndex: 'fetchDataType',
                        scope: Espide.Common.fetchOrderType,
                        renderer: Espide.Common.fetchOrderType.getData

                    },
                    {
                        text: '店铺名称',
                        width: 160,
                        dataIndex: 'title'
                    },
                    {
                        text: '记录创建时间',
                        dataIndex: 'createTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    }

                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: store,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                })
            });
        },

        //自动抓取日志store
        getConfigStore: function () {

            Ext.define('OrderFetchModel', {
                //不要忘了继承
                extend: 'Ext.data.Model',
                fields: [
                    'id',
                    {name: 'fetchTime', type: 'date', dateFormat: 'time'},
                    {name: 'fetchStartTime', type: 'date', dateFormat: 'time'},
                    'platformType',
                    'shop',
                    {name: 'title', type: 'string', mapping: 'shop.title'},
                    'fetchDataType',
                    {name: 'createTime', type: 'date', dateFormat: 'time'}
                ],
                idProperty: 'id'
            });

            return Ext.create('Ext.data.Store', {
                model: 'OrderFetchModel',
                proxy: {
                    type: 'ajax',
                    url: 'orderFetch/list',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.obj.result',
                        messageProperty: 'msg',
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
                autoLoad: {start: 0, limit: 19},
                pageSize: 19
            });
        },

        getShopStore: function (flag) {
            return Ext.create('Ext.data.Store', {
                fields: ['id', 'nick'],
                proxy: {
                    type: 'ajax',
                    url: '/order/shopList',
                    reader: {
                        type: 'json',
                        root: 'data.list'
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
                autoLoad: true,
                listeners: {
                    load: function () {
                        if (flag) {
                            this.insert(0, { nick: '全部店铺', id: null });
                        }
                    }
                }
            });
        },


        /*********************手动抓取订单日志***********************/

        /*********************手动抓取订单***********************/

        //手动抓取订单
        OrderManual: function (store) {
            return Ext.create('Ext.grid.Panel', {
                region: 'west',
                alias: 'widget.OrderManual',
                id: 'OrderManual',
                itemId: 'OrderManual',
                store: store,
                forceFit: true,
                tbar: {
                    items: [
                        Ext.create('Ext.form.Panel', {
                            layout: 'hbox',
                            border: false,
                            itemId: 'ManualSearch',
                            id: 'ManualSearch',
                            layout: {
                                type: 'hbox',
                                align: 'left'
                            },
                            height: 'auto',
                            msgTarget: 'title',
                            defaultType: 'fieldcontainer',
                            defaults: {
                                margin: '0 5 0 0',
                                defaults: {
                                    xtype: 'combo',
                                    labelWidth: 60,
                                    width: 200,
                                    name: 'searchType',
                                    queryMode: 'local',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    msgTarget: 'title',
                                }
                            },
                            items: [
                                {
                                    items: [

                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            combineErrors: true,
                                            msgTarget: 'top',
                                            labelWidth: 60,
                                            width: 780,
                                            defaults: {
                                                xtype: 'combo',
                                                queryMode: 'local',
                                                triggerAction: 'all',
                                                forceSelection: true,
                                                editable: false,
                                                margin: '0 5 0 0'
                                            },
                                            items: [
                                                {
                                                    xtype: 'combo',
                                                    name: 'fetchByTypeName',
                                                    fieldLabel: '抓取方式',
                                                    value: null,
                                                    itemId: 'queryType',
                                                    id: 'queryType',
                                                    labelWidth: 60,
                                                    width: 180,
                                                    value: 'FETCH_BY_DATE',
                                                    valueField: 'id',
                                                    displayField: 'value',
                                                    store: Ext.create('Ext.data.Store', {
                                                        fields: ['id', 'value'],
                                                        data: [
                                                            {id: 'FETCH_BY_DATE', value: '时间段抓取', },
                                                            {id: 'FETCH_BY_PLATFORM_NOS', value: '订单号抓取', },
                                                        ]
                                                    }),
                                                    listeners: {
                                                        select: function (combo, records, opts) {

                                                            if (records[0].get('id') == 'FETCH_BY_PLATFORM_NOS') {
                                                                Ext.getCmp('timeSearch').setVisible(false);
                                                                Ext.getCmp('timeSearch2').setVisible(false);
                                                                Ext.getCmp('timeSearch').disable();
                                                                Ext.getCmp('timeSearch2').disable();


                                                                Ext.getCmp('orderNoSearch').setVisible(true);
                                                                Ext.getCmp('orderNoSearch').enable();

                                                            } else {

                                                                Ext.getCmp('orderNoSearch').setVisible(false);
                                                                Ext.getCmp('orderNoSearch').disable();

                                                                Ext.getCmp('timeSearch').setVisible(true);
                                                                Ext.getCmp('timeSearch2').setVisible(true);
                                                                Ext.getCmp('timeSearch').enable();
                                                                Ext.getCmp('timeSearch2').enable();
                                                            }
                                                        }
                                                    }

                                                },
                                                {
                                                    name: 'fetchDataTypeName',
                                                    fieldLabel: '抓取数据类型',
                                                    value: 'FETCH_ORDER',
                                                    itemId: 'fetchDataTypeName',
                                                    valueField: 'id',
                                                    displayField: 'value',
                                                    labelWidth: 86,
                                                    width: 215,
                                                    store: Ext.create('Ext.data.Store', {
                                                        fields: ['id', 'value'],
                                                        data: [
                                                            {id: 'FETCH_ORDER', value: '获取订单', },
                                                            {id: 'FETCH_REFUND', value: '获取退款', },
                                                            {id: 'FETCH_RETURN', value: '获取退货', },
                                                        ]
                                                    })
                                                },
                                            ]
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            combineErrors: true,
                                            msgTarget: 'side',
                                            id: 'timeSearch',
                                            labelWidth: 60,
                                            width: 600,
                                            defaults: {
                                                xtype: 'combo',
                                                queryMode: 'local',
                                                triggerAction: 'all',
                                                forceSelection: true,
                                                editable: false,
                                                margin: '0 5 0 0'
                                            },
                                            items: [
                                                {
                                                    width: 150,
                                                    labelWidth: 60,
                                                    fieldLabel: '平台',
                                                    name: 'platformId',
                                                    allowBlank: false,
                                                    valueField: 'id',
                                                    displayField: 'name',
                                                    store: Espide.Common.createComboStore('/np/platform/list')
                                                },
                                                {
                                                    name: 'shopId',
                                                    fieldLabel: '店铺',
                                                    allowBlank: false,
                                                    itemId: 'queryType',
                                                    valueField: 'id',
                                                    displayField: 'nick',
                                                    labelWidth: 30,
                                                    width: 215,
                                                    store: this.getShopStore()
                                                },


                                            ]
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            combineErrors: true,
                                            msgTarget: 'side',
                                            id: 'timeSearch2',
                                            labelWidth: 60,
                                            width: 600,
                                            defaults: {
                                                xtype: 'combo',
                                                queryMode: 'local',
                                                triggerAction: 'all',
                                                forceSelection: true,
                                                editable: false,
                                                margin: '0 5 0 0'
                                            },
                                            items: [
                                                {
                                                    name: 'fetchDateTypeName',
                                                    fieldLabel: '时间类型',
                                                    value: 'FETCH_BY_MODIFIED',
                                                    itemId: 'fetchDateTypeName',
                                                    valueField: 'id',
                                                    displayField: 'value',
                                                    labelWidth: 60,
                                                    width: 150,
                                                    store: Ext.create('Ext.data.Store', {
                                                        fields: ['id', 'value'],
                                                        data: [
                                                            {id: 'FETCH_BY_MODIFIED', value: '更新时间', },
                                                            {id: 'FETCH_BY_CREATED', value: '创建时间', },
                                                        ]
                                                    })
                                                },
                                                Ext.create('Go.form.field.DateTime', {
                                                    fieldLabel: '开始日期',
                                                    value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 6),
                                                    name: 'fetchStartDate',
                                                    id: 'fetchStartDate',
                                                    itemId: 'fetchStartDate',
                                                    format: 'Y-m-d H:i:s',
                                                    margin: '0 5 0 0',
                                                    labelWidth: 60,
                                                    width: 215
                                                }),
                                                Ext.create('Go.form.field.DateTime', {
                                                    fieldLabel: '结束日期',
                                                    name: 'fetchEndDate',
                                                    itemId: 'fetchEndDate',
                                                    id: 'fetchEndDate',
                                                    format: 'Y-m-d H:i:s',
                                                    margin: '0 5 0 0',
                                                    labelWidth: 60,
                                                    width: 215
                                                }),

                                            ]
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            id: 'orderNoSearch',
                                            combineErrors: true,
                                            msgTarget: 'top',
                                            disabled: true,
                                            hidden: true,
                                            labelWidth: 60,
                                            width: 780,
                                            defaults: {
                                                xtype: 'combo',
                                                queryMode: 'local',
                                                triggerAction: 'all',
                                                forceSelection: true,
                                                editable: false,
                                                margin: '0 5 0 0'
                                            },
                                            items: [
                                                {
                                                    width: 150,
                                                    labelWidth: 60,
                                                    fieldLabel: '平台',
                                                    name: 'platformId',
                                                    value: null,
                                                    allowBlank: false,
                                                    valueField: 'id',
                                                    displayField: 'name',
                                                    store: Espide.Common.createComboStore('/np/platform/list')
                                                },
                                                {
                                                    name: 'shopId',
                                                    fieldLabel: '店铺',
                                                    value: null,
                                                    allowBlank: false,
                                                    itemId: 'queryType',
                                                    valueField: 'id',
                                                    displayField: 'nick',
                                                    labelWidth: 30,
                                                    width: 130,
                                                    store: this.getShopStore(false)
                                                },
                                                {
                                                    fieldLabel: '外部订单号列表',
                                                    xtype: 'textareafield',
                                                    width: 450,
                                                    allowBlank: false,
                                                    name: 'platformNos',
                                                }
                                            ]
                                        },
                                    ]
                                },
                                {
                                    items: [
                                        {
                                            xtype: 'button', name: 'submuit', text: '查询',
                                            margin: '30 0 0 0',
                                            height: 35,
                                            width: 55,
                                            handler: function (btn) {


                                                var data = btn.up('grid').down('#ManualSearch').getValues()
                                                startDate = Ext.getCmp('ManualSearch').down('#fetchStartDate').getValue(),
                                                    endDate = Ext.getCmp('ManualSearch').down('#fetchEndDate').getValue();


                                                //判断是否为订单号抓取
                                                if (Ext.getCmp('queryType').getValue() != 'FETCH_BY_PLATFORM_NOS') {
                                                    if (endDate == null || startDate == null) {
                                                        Ext.MessageBox.show({
                                                            title: '警告',
                                                            msg: '抓单条件日期时间不能为空！ ',
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: 'x-message-box-error'
                                                        });
                                                        return;
                                                    }


                                                    if (!!startDate && !!endDate && startDate.getTime && endDate.getTime && endDate.getTime() < startDate.getTime()) {
                                                        Ext.MessageBox.show({
                                                            title: '警告',
                                                            msg: '抓单条件的结束日期必须大于开始日期! ',
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: 'x-message-box-error'
                                                        });
                                                        return;
                                                    }

                                                    console.log(startDate.getTime() + '=====' + endDate.getTime());
                                                    if ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) > 7) {
                                                        Ext.MessageBox.show({
                                                            title: '警告',
                                                            msg: '抓单条件的结束日期时间范围不能超过7天! ',
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: 'x-message-box-error'
                                                        });
                                                        return;
                                                    }

                                                }
                                                if (btn.up('grid').down('#ManualSearch').isValid()) {

                                                    Ext.Ajax.request({
                                                        params: data,
                                                        url: "/omc/fetchOrderByManual",
                                                        async: false,
                                                        success: function (response, options) {
                                                            var data = Ext.JSON.decode(response.responseText);
                                                            if (data.success) {
                                                                Ext.MessageBox.show({
                                                                    title: '提示',
                                                                    msg: data.msg,
                                                                    buttons: Ext.MessageBox.OK,
                                                                    icon: 'x-message-box-info'
                                                                });
                                                                btn.up('grid').getStore().reload();

                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'button', text: '刷新',
                                            margin: '30 0 0 10',
                                            height: 35,
                                            width: 55,
                                            handler: function (btn) {
                                                btn.up('grid').getStore().reload();
                                            }
                                        },
                                    ]
                                }
                            ]
                        }),

                    ]
                },
                columns: [
                    {
                        text: '抓单开始时间',
                        dataIndex: 'fetchStartTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        text: '抓单结束时间',
                        dataIndex: 'fetchTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    },
                    {
                        text: '抓取平台',
                        width: 80,
                        dataIndex: 'platformType',
                        renderer: function (value) {
                            return value['value'];
                        }

                    },
                    {
                        text: '抓取类型',
                        width: 80,
                        dataIndex: 'fetchDataType',
                        scope: Espide.Common.fetchOrderType,
                        renderer: Espide.Common.fetchOrderType.getData

                    },
                    {
                        text: '店铺名称',
                        width: 160,
                        dataIndex: 'title'
                    },
                    {
                        text: '记录创建时间',
                        dataIndex: 'createTime',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                    }

                ],
                bbar: new Ext.PagingToolbar({
                    displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                    store: store,
                    displayInfo: true,
                    emptyMsg: '没有记录'
                })
            });
        },

        //自动抓取日志store
        getOrderManualStore: function () {

            Ext.define('OrderManual', {
                //不要忘了继承
                extend: 'Ext.data.Model',
                fields: [
                    'id',
                    {name: 'fetchTime', type: 'date', dateFormat: 'time'},
                    'platformType',
                    'shop',
                    {name: 'title', type: 'string', mapping: 'shop.title'},
                    'fetchDataType',
                    {name: 'createTime', type: 'date', dateFormat: 'time'}
                ],
                idProperty: 'id'
            });

            return Ext.create('Ext.data.Store', {
                model: 'OrderFetchModel',
                proxy: {
                    type: 'ajax',
                    // url: '/omc/fetchOrderByManual',
                    url: '/omc/findOrderFetchByHand',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.obj.result',
                        messageProperty: 'msg',
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

                autoLoad: false,
                pageSize: 19
            });
        }

    }
});