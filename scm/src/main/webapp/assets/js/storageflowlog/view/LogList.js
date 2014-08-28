/*
 * Created by Lein on 13-12-17
 */

Ext.define('StorageFlowLog.view.LogList', {
    extend: 'Ext.container.Container',
    id: 'LogList',
    alias: 'widget.LogList',
    title: "库存日志管理",
    fixed: true,
    layout: "border",
    initComponent: function () {

       var repositoryListStore = Espide.Common.createComboStore('/repository/list', true); // 仓库列表Store




//操作日志
        var LogListGrid = Ext.create("Ext.grid.Panel", {
            region: 'center',
            title: "库存日志管理",
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
                { text: '序号' , xtype: 'rownumberer' , width:'auto',},
                {
                    header: '仓库名',
                    width: 50,
                    align:'center',
                    dataIndex: 'repoName'
                },
                {
                    header: '品牌名',
                    dataIndex: 'brandName'

                },
                {
                    header: '商品分类',
                    dataIndex: 'categogryName'
                },
                {
                    header: '商品名称',
                    dataIndex: 'productName'
                },
                {
                    header: '规格',
                    dataIndex: 'boxSize',
                },
                {
                    header: '销售价格',
                    dataIndex: 'marketPrice',
                },
                {
                    header: '条形码',
                    dataIndex: 'sku',
                },
                {
                    header: '库存数量',
                    dataIndex: 'amount',
                }

            ],
            plugins:[
                Ext.create('Ext.grid.plugin.CellEditing',{
                    clicksToEdit:1 //设置单击单元格编辑
                })
            ],
            tbar:[
                Ext.create('Ext.form.Panel', {
                    layout: 'hbox',
                    border: false,
                    itemId: 'search',
                    id: 'search',
                    defaults:{
                      margin:'0 5 0 0'
                    },
                    items: [
                        {
                            xtype:'combo',
                            id:'type',
                            fieldLabel: '入库/出库',
                            labelWidth:60,
                            name: 'type',
                            value: null,
                            editable:false,
                            emptyText: '全部',
                            margin:'0 5 0 0',
                            store: [
                                [null, '全部'],
                                ['OUT_STOCK', '出库时间'],
                                ['IN_STOCK', '入库时间']
                            ]
                        },
                        Ext.create('Go.form.field.DateTime', {
                            fieldLabel: '开始日期',
                            value: new Date(new Date().getTime() - 60 * 60 * 24 * 30000),
                            name: 'startDate',
                            id: 'startDate',
                            format: 'Y-m-d H:i:s',
                            margin: '0 5 5 0',
                            labelWidth: 60,
                            width: 220
                        }),
                        Ext.create('Go.form.field.DateTime', {
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
                            xtype:'combo',
                            name: 'repositoryId',
                            id: 'repositoryId',
                            fieldLabel: '选择仓库',
                            labelWidth:60,
                            editable: false,
                            queryMode: 'local',
                            valueField: 'id',
                            displayField: 'name',
                            margin:'0 5 0 0',
                            emptyText: '全部',
                            store: repositoryListStore
                        },{
                            xtype: 'button',
                            name: 'submuit',
                            style: 'margin: 0 0 0 15px',
                            itemId:'search',
                            text: '搜索'
                        }

                    ]
                })
            ],
            bbar: new Ext.PagingToolbar({
                pageSize: 25,
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
                    header: '商品编号',
                    width: 50,
                    dataIndex: 'productNo'
                },
                {
                    header: '产品规格',
                    width: 50,
                    dataIndex: 'speci'
                },
                {
                    header: '仓库名称',
                    dataIndex: 'repoName',
                },
                {
                    header: '更新库存量',
                    dataIndex: 'amount',
                },
                {
                    header: '更新前库存量',
                    dataIndex: 'beforeAmount',
                },
                {
                    header: '更新后库存量',
                    dataIndex: 'afterAmount',
                },
                {
                    header: '出库/入库类型',
                    dataIndex: 'inOutStockTypeValue',
                },
                {
                    header: '经办人姓名',
                    dataIndex: 'operatorName',
                },
                {
                    header: '经办人账号',
                    dataIndex: 'operatorUserName',
                },
                {
                    header: '入库/出库',
                    dataIndex: 'type',
                    renderer:function(value){
                        return value['value'];
                    }

                },
                {
                    header: '入库/出库时间',
                    dataIndex: 'createTime',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },
                {
                    header: '描述',
                    dataIndex: 'desc'
                }

            ],
            bbar: new Ext.PagingToolbar({
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: 'BusinessLog',
                displayInfo: true,
                emptyMsg: '没有记录'
            })
        });


        this.items = [LogListGrid, BusinessGrid];
        this.callParent(arguments);

    }
})
;