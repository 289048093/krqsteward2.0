/*
 * Created by Lein on 13-12-17
 */

Ext.define('Log.view.LogList', {
    extend: 'Ext.container.Container',
    id: 'LogList',
    alias: 'widget.LogList',
    title: "系统日志管理",
    fixed: true,
    layout: "border",
    initComponent: function () {

//操作日志
        var LogListGrid = Ext.create("Ext.grid.Panel", {
            region: 'center',
            title: "系统日志管理",
            id: 'LogListGrid',
            loadMask: true,
            forceFit: true,
            split: true,
            store: 'LogList',
            viewConfig: {
                enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false
            },
            columns: [
                {
                    header: '日志ID',
                    dataIndex: 'id',
                    hidden:true,
                },
                {
                    header: '操作用户',
                    dataIndex: 'operatorName'
                },
                {
                    header: 'ip地址',
                    dataIndex: 'ip'
                },

                {
                    header: '请求参数',
                    dataIndex: 'params',
                    renderer: function (value, meta, record) {
                        if (value == null) {
                            return '';
                        }
                        return "<span title='" + value + "'>" + value + "</span>";
                    },
                    editor: {
                        xtype: "textfield",
                        allowBlank: false
                    }
                },
                {
                    header: '操作模块',
                    dataIndex: 'resourceName'
                },
                {
                    header: '操作名称',
                    dataIndex: 'operationName'
                },
                {
                    header: '请求链接',
                    dataIndex: 'requestUrl',
                    renderer: function (value, meta, record) {
                        if (value == null) {
                            return '';
                        }
                        return "<span title='" + value + "'>" + value + "</span>";
                    }
                },

                {
                    header: '操作结果',
                    dataIndex: 'operationResult',
                    renderer:function(value){
                        if(value){
                            return '成功';
                        }else{
                            return '失败';
                        }
                    }
                },
                {
                    header: '操作异常描述',
                    dataIndex: 'operationException',
                    renderer: function (value, meta, record) {
                        if (value == null) {
                            return '';
                        }
                        return "<span title='" + value + "'>" + value + "</span>";
                    }
                },
                {
                    header: '操作描述',
                    dataIndex: 'description',
                    renderer: function (value, meta, record) {
                        if (value == null) {
                            return '';
                        }
                        return "<span title='" + value + "'>" + value + "</span>";
                    }
                },
                {
                    header: '执行时间',
                    dataIndex: 'executionTime',
                },
                {
                    header: '创建开始时间',
                    dataIndex: 'createTime',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                }

            ],
            plugins:[
                Ext.create('Ext.grid.plugin.CellEditing',{
                    clicksToEdit:1 //设置单击单元格编辑
                })
            ],
            tbar: [

                Ext.create('Ext.form.Panel', {
                    layout: 'hbox',
                    border: false,
                    itemId: 'search',
                    id: 'search',
                    defaults:{
                        margin:'0 5 0 0'
                    },
                    items: [
                        Ext.create('Log.dateExtend.form.field.DateTime', {
                            fieldLabel: '开始日期',
                            value: new Date(new Date().getTime() - 60 * 60 * 24 * 30000),
                            name: 'startDate',
                            id: 'startDate',
                            format: 'Y-m-d H:i:s',
                            margin: '0 5 5 0',
                            labelWidth: 60,
                            width: 220
                        }),
                        Ext.create('Log.dateExtend.form.field.DateTime', {
                            fieldLabel: '结束日期',
                            value: new Date(),
                            name: 'endDate',
                            id: 'endDate',
                            format: 'Y-m-d H:i:s',
                            margin: '0 5 5 0',
                            labelWidth: 60,
                            width: 220
                        }),
                        {
                            xtype: 'combo',
                            store: [
                                ["operatorName", "操作用户"],
                                ["operationName", "操作名称"]
                            ],
                            queryMode: 'local',
                            name: "paramType",
                            value: "operatorName",
                            width: 100,
                            itemId: "type",
                            editable:false
                        },
                        {
                            xtype: 'textfield',
                            name: 'param',
                            emptyText:'请输入关键字',
                            width: 150,
                            itemId: "inputValue"
                        },
                        {
                            xtype: 'button',
                            name: 'submuit',
                            text: '搜索',
                            handler: function (btn) {
                                Espide.Common.doSearch('LogListGrid','search',false);

                            }}
                    ]
                })

            ],
            bbar: new Ext.PagingToolbar({
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: 'LogList',
                displayInfo: true,
                emptyMsg: '没有记录'
            })
        });
//操作日志
        var BusinessGrid = Ext.create("Ext.grid.Panel", {
            region: 'south',
            id: 'BusinessGrid',
            split: true,
            height: 200,
            loadMask: true,
            hidden: true,
            forceFit: true,
            store: 'BusinessLog',
            viewConfig: {
                enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false
            },
            plugins:[
                Ext.create('Ext.grid.plugin.CellEditing',{
                    clicksToEdit:1 //设置单击单元格编辑
                })
            ],
            columns: [
                {
                    header: '日志ID',
                    width: 50,
                    dataIndex: 'id'
                },
                {
                    header: '业务日志ID',
                    width: 50,
                    dataIndex: 'businessLogId'
                },
                {
                    header: 'SQL语句',
                    width: 800,
                    dataIndex: 'content',
                    editor: {
                        xtype: "textfield",
                        allowBlank: false
                    }
                },
                {
                    header: '操作类型',
                    width: 40,
                    dataIndex: 'operationType'
                },
                {
                    header: '创建时间',
                    dataIndex: 'createTime',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },
                {
                    header: '执行时长',
                    width: 40,
                    dataIndex: 'executionTime'
                }

            ]
        });


        this.items = [LogListGrid, BusinessGrid];
        this.callParent(arguments);

    }
})
;