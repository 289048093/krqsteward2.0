/*
 * Created by king on 13-12-17
 */

Ext.define('Customer.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'List',
    itemId: 'list',
    alias: 'widget.customerList',
    store: 'List',
    foreFit: false,
    split: true,
    //selType: 'checkboxmodel',
    selModel: {
        selType: 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'SIMPLE',
        showHeaderCheckbox: true
    },
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {

        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 2
            })
        ];


        this.tbar = {
            items: [

                {
                    xtype: 'button',
                    text: '批量添加标签',
                    iconCls: 'icon-add',
                    itemId: 'addTag'
                },
                { text: '发短信', iconCls: 'icon-batch-delete', },
                { text: '导入', iconCls: 'icon-deliver', itemId: 'outOrder'},
                { text: '导出', iconCls: 'icon-goback', itemId: 'TotalOutOrder'},
                { text: '标签管理', iconCls: 'icon-batch-edit', itemId: 'labelManager'},
                { text: '黑名单管理', iconCls: 'icon-batch-edit', itemId: 'backListManager'},
                Ext.create('Ext.button.Split', {
                    text: '添加黑名单分类',
                    border: '0',
                    iconCls: 'icon-add',
                    menu: {
                        id: 'editOrder',
                        items: [
                            {
                                text: '<b>手机黑名单</b>',
                                iconCls: 'icon-cancel',
                                itemId: 'cancelOrder',
                                handler: function (btn) {
                                     console.log(btn);
//                                    "SMS": "短信黑名单",
//                                        "MAIL": "邮件黑名单",
//                                        "PHONE": "电话黑名单",
                                    if (!Espide.Common.isGridSingleSel('List')) {
                                        Espide.Common.showGridSelErr('必须选择一条会员数据');
                                        return;
                                    }

                                    var ids = Espide.Common.getGridSelsId('List', 'id');
                                    var mobile = Espide.Common.getGridSels('List', 'mobile');

                                    Ext.Ajax.request({
                                        url: "/customer/addCustomerToBlacklist",
                                        params: {type: 'PHONE', id: ids, mobile: mobile},
                                        success: function (response, options) {
                                            var data  = eval('('+response.responseText+')');
                                            if(data.success){
                                                Espide.Common.tipMsg('操作成功', data.msg);
                                            }


                                        }

                                    });
                                }
                            },
                            {
                                text: '<b>邮箱黑名单</b>',
                                iconCls: 'icon-recover',
                                itemId: 'recoverOrder',
                                handler: function (btn) {
                                    console.log(btn);
//                                    "SMS": "短信黑名单",
//                                        "MAIL": "邮件黑名单",
//                                        "PHONE": "电话黑名单",
                                    if (!Espide.Common.isGridSingleSel('List')) {
                                        Espide.Common.showGridSelErr('必须选择一条会员数据');
                                        return;
                                    }

                                    var ids = Espide.Common.getGridSelsId('List', 'id');
                                    var mobile = Espide.Common.getGridSels('List', 'mobile');

                                    Ext.Ajax.request({
                                        url: "/customer/addCustomerToBlacklist",
                                        params: {type: 'MAIL', id: ids, mobile: mobile},
                                        success: function (response, options) {

                                            var data  = eval('('+response.responseText+')');
                                            if(data.success){
                                                Espide.Common.tipMsg('操作成功', data.msg);
                                            }

                                        }

                                    });
                                }
                            },
                            {
                                text: '<b>短信黑名单</b>',
                                iconCls: 'icon-import',
                                itemId: 'batCheck',
                                handler: function (btn) {
                                    console.log(btn);
//                                    "SMS": "短信黑名单",
//                                        "MAIL": "邮件黑名单",
//                                        "PHONE": "电话黑名单",
                                    if (!Espide.Common.isGridSingleSel('List')) {
                                        Espide.Common.showGridSelErr('必须选择一条会员数据');
                                        return;
                                    }

                                    var ids = Espide.Common.getGridSelsId('List', 'id');
                                    var mobile = Espide.Common.getGridSels('List', 'mobile');

                                    Ext.Ajax.request({
                                        url: "/customer/addCustomerToBlacklist",
                                        params: {type: 'SMS', id: ids, mobile: mobile},
                                        success: function (response, options) {
                                             var data  = eval('('+response.responseText+')');
                                            if(data.success){
                                                Espide.Common.tipMsg('操作成功', data.msg);
                                            }

                                        }

                                    });
                                }



                            },
                        ],

                    },
                }),
                { text: '刷新', iconCls: 'icon-refresh', itemId: 'refresh'},
            ]
        };

        this.columns = [
            { text: '会员编号', dataIndex: 'id', width: 50,hidden:true},
            { text: '姓名', dataIndex: 'realName', width: 80,
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
            },
            { text: '会员名称', dataIndex: 'buyerId', width: 150},
            { text: '等级', dataIndex: 'grade', width: 150},
            { text: '当前积分', dataIndex: 'bonusPoint', width: 150},
            {text: '平台', dataIndex: 'platform', sortable: true, width: 120, },
            {text: '电话', dataIndex: 'phone', sortable: true, width: 120,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {text: '手机', width: 150, dataIndex: 'mobile', sortable: true,
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
            },
            {text: '邮箱', width: 150, dataIndex: 'email', sortable: true,
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
            },
            {text: '生日', width: 150, dataIndex: 'birthday', sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d'),
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
            },

            {text: '地址', dataIndex: 'address', sortable: true, width: 250,
//                editor: {
//                    xtype: 'textfield',
//                    allowBlank: true
//                },
            },

            {text: '累计购买金额', width: 150, dataIndex: 'totalTradeFee', sortable: true},
            {text: '最近交易时间', width: 150, dataIndex: 'lastTradeTime', sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')},
            {text: '交易次数', dataIndex: 'tradeCount', width: 160, sortable: true, },
            {text: '平均购买金额', dataIndex: 'avgTradeFee', sortable: true, width: 160, },

            {text: '标签', dataIndex: 'tags', sortable: true, width: 450,
                renderer: function (value) {
                    var tagArr = [];
                    Ext.each(value, function (valueItem, i) {
                        tagArr.push(valueItem.name);
                    });
                    return tagArr.join(',');
                }
            },
            {text: '会员信息', dataIndex: 'tags', sortable: true, width: 70, },
        ];

        this.callParent(arguments);

    }
});