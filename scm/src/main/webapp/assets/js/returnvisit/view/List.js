/*
 * Created by king on 13-12-17
 */

Ext.define('Returnvisit.view.List', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'List',
    itemId: 'List',
    alias: 'widget.SmsList',
    store: 'List',

    forceFit: true,
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
                    text: '分配',
                    iconCls: 'icon-add',
                    itemId: 'allocation'
                },



            ]
        };

        this.columns = [
            { text: '回访编号', dataIndex: 'id', },
            { text: '回访状态', dataIndex: 'status',
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
                renderer:function(value){
                    if(!!value){
                        return value.value;
                    }else{
                        return '';
                    }
                }
            },
            { text: '回访类型', dataIndex: 'type',
                renderer:function(value){
                    if(!!value){
                        return value.value;
                    }else{
                        return '';
                    }
                }
            },
            { text: '回访人', dataIndex: 'visitorRealname', },
            { text: '最近回访时间', dataIndex: 'lastVisitTime',},
            {text: '预约时间', dataIndex: 'appointmentTime', sortable: true,  },
            {text: '外部平台订单编号', dataIndex: 'platformOrderNo', sortable: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: true
                },
            },
            {
                text: "操作样式1",
                xtype: 'actioncolumn',
                items: [
                    {
                        test: "手动同步",
                        tooltip: '手动同步',
                        text: '手动同步',
                        iconCls: 'icon-refresh',
                        renderer: function (value) {
                            return Ext.String.format('<button>{1}<span>{0}<span></button>', '手动同步', value);
                        },
                        handler: function (btn, rowIndex, colIndex, item, e, record) {
                            var url = '/shopProduct/updateShopStorage';

                            Espide.Common.doAction({
                                url: url,
                                params: {
                                    id: record.get('id')
                                },
                                successCall: function () {
                                    Ext.getCmp('List').getStore().loadPage(1);
                                },
                                successTipMsg: '手动同步成功'
                            })('yes');

                        }
                    },
                    {
                        test: "浏览商品详情",
                        tooltip: "浏览商品详情",
                        iconCls: 'icon-batch-edit',
                        style: 'margin: 0 0 0 15px',
                        renderer: function (value) {
                            return Ext.String.format('<button>{1}<span>{0}<span></button>', '浏览商品详情', value);
                        },
                        handler: function (btn, rowIndex, colIndex, item, e, record) {

                        }
                    }
                ]
            }

        ];

        this.callParent(arguments);

    }
});


