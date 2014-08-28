///**
// * Created by king on 13-12-23
// */
//
//Ext.define('Customer.view.backList.backList', {
//    extend: 'Ext.form.Panel',
//    alias: 'widget.backList',
//    //frame: true,
//    bodyPadding: 10,
//    border: false,
//    forceFit: true,
//    layout: 'column',
//    initComponent: function () {
//        Ext.apply(this, {
//            width: 820,
//            fieldDefaults: {
//                labelAlign: 'left',
//                labelWidth: 90,
//                anchor: '100%',
//                msgTarget: 'side'
//            },
//            items: [
//                {
//                    columnWidth: 0.70,
//                    id: 'backListGridpanel',
//                    xtype: 'gridpanel',
//                    forceFit: true,
//                    store: 'BackList',
//                    height: 400,
//                    selModel: {
//                        selType: 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
//                        mode: 'MULTI',
//                        showHeaderCheckbox: true
//                    },
//                    viewConfig: {
//                        enableTextSelection: true
//                    },
//                    bbar: new Ext.PagingToolbar({
//                        displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
//                        store: 'BackList',
//                        displayInfo: true,
//                        emptyMsg: '没有记录'
//                    }),
//                    tbar: [
//                        Ext.create('Ext.form.Panel', {
//                            layout: 'hbox',
//                            id: 'labelSearch',
//                            border: false,
//                            items: [
//                                {
//                                    xtype: 'combo',
//                                    name: 'searchType',
//                                    itemId: 'searchType',
//                                    editable: false,
//                                    queryMode: 'local',
//                                    margin: '0 15 0 0',
//                                    width: 130,
//                                    value: 'SMS',
//                                    store: Espide.Common.backListType.getStore()
//                                },
//                                {
//                                    //fieldLabel:'标签名称',
//                                    labelWidth: 65,
//                                    xtype: 'textfield',
//                                    emptyText: '请输入关键字',
//                                    name: 'name',
//                                    width: 200,
//                                    margin: '0 15 0 0',
//                                    listeners: {
//                                        'change': function () {
//                                            // Espide.Common.doSearch("labelGridpanel", "labelSearch", true);
//                                        }
//                                    }
//                                },
//                                {
//                                    xtype: 'button',
//                                    text: '搜索',
//                                    margin: '0 5 0 0',
//                                    itemId: 'searchBtn',
//                                    handler: function () {
//                                        Espide.Common.doSearch("labelGridpanel", "labelSearch", true);
//                                    }
//                                },
//                                {
//                                    xtype: 'button',
//                                    text: '删除',
//                                    handler: function () {
//                                        var url = '/customerTag/delete',
//                                            ids = Espide.Common.getGridSels('labelGridpanel', 'id');
//
//                                        if (ids.length < 1) {
//                                            Espide.Common.showGridSelErr('请先选择要删除的品牌');
//                                            return;
//                                        }
//
//                                        Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
//                                            Espide.Common.doAction({
//                                                url: url,
//                                                params: {
//                                                    ids: ids.join()
//                                                },
//                                                successCall: function () {
//                                                    Ext.getCmp('labelGridpanel').getStore().loadPage(1);
//                                                },
//                                                successTipMsg: '删除成功'
//                                            })(optional);
//                                        });
//                                    }
//                                }
//                            ]
//                        }),
//                    ],
//                    columns: [
//                        {
//                            text: '序号',
//                            width: 15,
//                            sortable: true,
//                            dataIndex: 'id',
//                            align: 'center',
//                        },
//
//                        {
//                            text: '标签名称',
//                            sortable: true,
//                            width: 75,
//                            sortable: true,
//                            dataIndex: 'name',
//                            align: 'center',
//                        },
//
//                    ],
//                    listeners: {
//                        scope: this,
//                        selectionchange: this.onSelectionChange
//                    }
//                },
//                {
//                    columnWidth: 0.30,
//                    margin: '0 0 0 10',
//                    border: 0,
//                    xtype: 'form',
//                    id: 'labelForm',
//                    frame: true,
//                    autoHeight: true,
//                    bodyPadding: 15,
//                    border: 0,
//                    //title: 'Company details',
//                    layout: 'anchor',
//                    defaultType: 'textfield',
//                    items: [
//                        {
//                            xtype: 'combo',
//                            name: 'type',
//                            itemId: 'searchType',
//                            editable: false,
//                            queryMode: 'local',
//                            margin: '0 15 0 0',
//                            width: 130,
//                            value: 'SMS',
//                            store: Espide.Common.backListType.getStore()
//                        },
//                        {
//                            fieldLabel: '标签名称',
//                            labelWidth: 65,
//                            name: 'name'
//                        },
//                        {
//                            xtype: 'textfield',
//                            hidden: true,
//                            name: 'id',
//                            value: ''
//                        },
//                    ],
//                    buttonAlign: 'center',
//                    buttons: [
//                        {
//                            text: '添加',
//                            handler: function (btn) {
//                                var data = this.up('form').getForm().getValues(), url = '/customerTag/save';
//                                if (this.up('form').getForm().isValid()) {
//                                    Espide.Common.doAction({
//                                        url: url,
//                                        params: {
//                                            name: data.name
//                                        },
//                                        successCall: function () {
//                                            Ext.getCmp('labelGridpanel').getStore().loadPage(1);
//                                        },
//                                        successTipMsg: '添加成功'
//                                    })('yes');
//                                }
//
//                            }
//                        },
//                        {
//                            text: '修改',
//                            width: 30,
//                            handler: function () {
//                                var data = this.up('form').getForm().getValues(), url = '/customerTag/save';
//
//                                if (this.up('form').getForm().isValid()) {
//                                    Espide.Common.doAction({
//                                        url: url,
//                                        params: data,
//                                        successCall: function () {
//                                            Ext.getCmp('labelGridpanel').getStore().loadPage(1);
//                                        },
//                                        successTipMsg: '修改成功'
//                                    })('yes');
//                                }
//
//                            }
//                        },
//                    ]
//                }
//            ]
//        });
//        this.callParent();
//    },
//    onSelectionChange: function (model, records) {
//        var rec = records[0];
//        if (rec) {
//            Ext.getCmp('labelForm').getForm().loadRecord(rec);
//        }
//    },
//    formReset: function (btn) {
//        this.getForm().reset();
//    }
//});
/**
 * Created by king on 13-12-23
 */

Ext.define('Customer.view.backList.backList', {
    extend: 'Ext.form.Panel',
    alias: 'widget.backList',
    //frame: true,
    bodyPadding: 10,
    border: false,
    forceFit: true,
    layout: 'column',
    initComponent: function () {
        Ext.apply(this, {
            width: 820,
            fieldDefaults: {
                labelAlign: 'left',
                labelWidth: 90,
                anchor: '100%',
                msgTarget: 'side'
            },
            items: [
                {
                    columnWidth: 0.70,
                    id: 'backListGridpanel',
                    xtype: 'gridpanel',
                    forceFit: true,
                    store: 'BackList',
                    height: 400,
                    selModel: {
                        selType: 'checkboxmodel',//复选框选择模式Ext.selection.CheckboxModel
                        mode: 'MULTI',
                        showHeaderCheckbox: true
                    },
                    viewConfig: {
                        enableTextSelection: true
                    },
                    bbar: new Ext.PagingToolbar({
                        displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                        store: 'BackList',
                        displayInfo: true,
                        emptyMsg: '没有记录'
                    }),
                    tbar: [
                        Ext.create('Ext.form.Panel', {
                            layout: 'hbox',
                            id: 'backListSearch',
                            border: false,
                            items: [
                                {
                                    xtype: 'combo',
                                    name: 'type',
                                    itemId: 'type',
                                    editable: false,
                                    queryMode: 'local',
                                    margin: '0 15 0 0',
                                    width: 130,
                                    value: 'SMS',
                                    store: Espide.Common.backListType.getStore(false)
                                },
                                {
                                    //fieldLabel:'标签名称',
                                    labelWidth: 65,
                                    xtype: 'textfield',
                                    emptyText: '请输入关键字',
                                    name: 'value',
                                    width: 200,
                                    margin: '0 15 0 0',
                                    listeners: {
                                        'change': function () {
                                            Espide.Common.doSearch("backListGridpanel", "backListSearch", true);
                                        }
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: '搜索',
                                    margin: '0 5 0 0',
                                    itemId: 'searchBtn',
                                    handler: function () {
                                        Espide.Common.doSearch("backListGridpanel", "backListSearch", true);
                                    }
                                },
                                {
                                    xtype: 'button',
                                    text: '删除',
                                    handler: function () {
                                        var url = '/blacklist/delete',
                                            ids = Espide.Common.getGridSels('backListGridpanel', 'id');

                                        if (ids.length < 1) {
                                            Espide.Common.showGridSelErr('请先选择要删除的品牌');
                                            return;
                                        }

                                        Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                                            Espide.Common.doAction({
                                                url: url,
                                                params: {
                                                    ids: ids.join()
                                                },
                                                successCall: function () {
                                                    Ext.getCmp('backListGridpanel').getStore().loadPage(1);
                                                },
                                                successTipMsg: '删除成功'
                                            })(optional);
                                        });
                                    }
                                }
                            ]
                        }),
                    ],
                    columns: [
                        {
                            text: '序号',
                            width: 15,
                            sortable: true,
                            dataIndex: 'id',
                            align: 'center',
                        },
                        {
                            text: '黑名单类别',
                            sortable: true,
                            width: 75,
                            sortable: true,
                            dataIndex: 'type',
                            align: 'center',
                            renderer: function (value) {
                                if (value) {
                                    return value.value;
                                } else {
                                    return '';
                                }

                            }
                        },
                        {
                            text: '电话/邮箱',
                            sortable: true,
                            width: 75,
                            sortable: true,
                            dataIndex: 'value',
                            align: 'center',
                        },

                    ],
                    listeners: {
                        scope: this,
                        selectionchange: this.onSelectionChange
                    }
                },
                {
                    columnWidth: 0.30,
                    margin: '0 0 0 10',
                    border: 0,
                    xtype: 'form',
                    id: 'labelForm',
                    frame: true,
                    autoHeight: true,
                    bodyPadding: 15,
                    border: 0,
                    //title: 'Company details',
                    layout: 'anchor',
                    defaultType: 'textfield',
                    items: [
                        {
                            fieldLabel: '黑名单类型',
                            xtype: 'combo',
                            name: 'type',
                            itemId: 'type',
                            editable: false,
                            queryMode: 'local',
                            margin: '0 0 15 0',
                            labelWidth: 85,
                            width: 130,
                            value: 'SMS',
                            store: Espide.Common.backListType.getStore()
                        },
                        {
                            fieldLabel: '黑名单内容',
                            labelWidth: 85,
                            name: 'value'
                        },
                        {
                            xtype: 'textfield',
                            hidden: true,
                            name: 'id',
                            value: ''
                        },
                    ],
                    buttonAlign: 'center',
                    buttons: [
                        {
                            text: '添加',
                            handler: function (btn) {
                                var data = this.up('form').getForm().getValues(), url = '/blacklist/save';
                                if (this.up('form').getForm().isValid()) {
                                    Espide.Common.doAction({
                                        url: url,
                                        params: {
                                            value: data.value,
                                            type: data.type
                                        },
                                        successCall: function () {
                                            Ext.getCmp('backListGridpanel').getStore().loadPage(1);
                                        },
                                        successTipMsg: '添加成功'
                                    })('yes');
                                }


                            }
                        },
                        {
                            text: '修改',
                            width: 30,
                            id:'editBtn',
                            disabled:true,
                            handler: function () {
                                var data = this.up('form').getForm().getValues(), url = '/blacklist/save';
                                if (this.up('form').getForm().isValid()) {
                                    Espide.Common.doAction({
                                        url: url,
                                        params: data,
                                        successCall: function () {
                                            Ext.getCmp('backListGridpanel').getStore().loadPage(1);
                                        },
                                        successTipMsg: '修改成功'
                                    })('yes');
                                }
                            }
                        },
                    ]
                }
            ]
        });
        this.callParent();
    },
    onSelectionChange: function (model, records) {
        var rec = records[0];
        var isGridSingleSel = Espide.Common.isGridSingleSel('backListGridpanel');
        if(isGridSingleSel){
            Ext.getCmp('editBtn').enable();
        }else{
            Ext.getCmp('editBtn').disable();
        }
        if (rec) {
            if (rec.get('type').name) {
                rec.data.type = rec.get('type').name;
            }
            Ext.getCmp('labelForm').getForm().loadRecord(rec);
        }
    },
    formReset: function (btn) {
        this.getForm().reset();
    }
});
