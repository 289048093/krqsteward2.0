/*
 * Created by king on 13-12-17
 */

Ext.define('Comment.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'List',
    itemId: 'List',
    alias: 'widget.CommentList',
    store: 'List',
    foreFit: false,
    split: true,
    //selType: 'checkboxmodel',
    selModel: {
        selType: 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
        mode: 'MULTI',
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
                    text: '归类',
                    iconCls: 'icon-add',
                    itemId: 'commentType'
                },
                {
                    xtype: 'button',
                    text: '添加标签',
                    iconCls: 'icon-add',
                    itemId: 'addTags'
                },
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
                                    //console.log(btn);
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
                                        params: {type: 'PHONE', mobile: mobile[0]},
                                        success: function (form, action) {
                                            var data = Ext.JSON.decode(action.response.responseText);
                                            if (data.success) {
                                                Espide.Common.tipMsg('操作成功!',data.msg);
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
                                    //console.log(btn);
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
                                        params: {type: 'MAIL',  mobile: mobile[0]},
                                        success: function (form, action) {
                                            var data = Ext.JSON.decode(action.response.responseText);
                                            if (data.success) {
                                                Espide.Common.tipMsg('操作成功!',data.msg);
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
                                    //console.log(btn);
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
                                        params: {type: 'SMS',  mobile: mobile[0]},
                                        success: function (form, action) {
                                            var data = Ext.JSON.decode(action.response.responseText);
                                            if (data.success) {
                                                Espide.Common.tipMsg('操作成功!',data.msg);
                                            }


                                        }

                                    });
                                }



                            },
                        ],

                    },
                }),
                { text: '导出', iconCls: 'icon-batch-edit', itemId: 'batUp',   style: 'margin: 0 0 0 15px',},
                { text: '刷新', iconCls: 'icon-refresh', itemId: 'refresh', style: 'margin: 0 0 0 15px',},

            ]
        };

        this.columns = [
            { text: 'id', dataIndex: 'id', width: 50},
            { text: '姓名', dataIndex: 'commenter', width: 80,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            { text: '会员名', dataIndex: 'buyerId', width: 150},
            { text: '标签', dataIndex: 'customerTags', width: 150,renderer:function(value){
                var arr = [];
                Ext.each(value,function(item,i){
                   arr.push(item.name);
                });
                return arr.join(',');
            }},
            { text: '手机', dataIndex: 'mobile', width: 150},
            { text: '平台', dataIndex: 'platformType', width: 150,
                renderer:function(value){
                    if(!!value){
                        return value['value'];
                    }else{
                        return '';
                    }
                }
            },
            {text: '店铺', dataIndex: 'shopName', sortable: true, width: 120, },
            {text: '外部平台订单编号', dataIndex: 'platformOrderNo', sortable: true, width: 120,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {text: '外部平台订单项编号', width: 150, dataIndex: 'platformSubOrderNo', sortable: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {text: '商品名称', width: 150, dataIndex: 'productName', sortable: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {text: 'SKU', width: 150, dataIndex: 'productSku', sortable: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },

            {text: '评价结果', dataIndex: 'result', sortable: true, width: 250,
                renderer:function(value){
                    if(!!value){
                        return value['value'];
                    }else{
                        return '';
                    }
                }
            },
            {text: '评价类型', dataIndex: 'categories', sortable: true, width: 250,
                renderer:function(value){
                    var arr = [];
                    if(Array.isArray(value)){
                        Ext.each(value,function(item,i){
                            arr.push(item.name);
                        });
                        return arr.join(',');
                    }else{
                        return value;
                    }

                }
            },
            {text: '评价内容', dataIndex: 'contents', sortable: true, width: 450,align:'left',
                renderer:function(value){
                    var arr = [];
                    if(Array.isArray(value)){
                        Ext.each(value,function(item,i){
                            arr.push('<b style="color:green">评价:</b>'+item.content+'<br/><b style="color:red">回复:</b>'+item.reply);
                        });
                        return arr.join(',');
                    }else{
                        return value;
                    }
                }
            },


        ];

        this.callParent(arguments);

    }
});


