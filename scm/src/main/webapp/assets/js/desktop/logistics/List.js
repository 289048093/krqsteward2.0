/**
 * 物流公司列表
 * Created by HuaLei.Du on 13-12-18.
 */

Ext.define('EBDesktop.LogisticsListModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name', 'law','repository',
        {name:'repoName',mapping:'repository.name'},
        {name:'repositoryId',mapping:'repository.id'},
    ],
    idProperty: 'id'
});

Ext.define('EBDesktop.logistics.List', {
    extend: 'Ext.container.Container',
    alias: 'widget.logisticsList',
    id: 'logisticsList',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var logisticsListStore,
            searchForm,
            logisticsGrid;


        // 显示添加窗口
        function showAddWin() {
            var saveWin = Ext.getCmp('logisticsSave') || Ext.widget('logisticsSave'),
                saveForm;

            saveWin.show().setTitle('添加物流');
            saveForm = Ext.getCmp('logisticsSaveForm');
            saveForm.getForm().reset();
            saveWin.down('[itemId=action]').setValue('logisticsprint/save');
        }

        // 显示修改窗口
        function showEditWin(t, record) {
            var saveWin = Ext.getCmp('logisticsSave') || Ext.widget('logisticsSave', {
                    title: '修改物流'
                }),
                saveForm = Ext.getCmp('logisticsSaveForm');

            saveForm.getForm().loadRecord(record);
            saveForm.down('[itemId=action]').setValue('logisticsprint/save');
            saveWin.show();
        }

        // 显示设计窗口
        function showDesignWin(view, rowIndex, colIndex, item, e, record) {
            window.open('logistics_design.html?id=' + record.data.id + '&_=' + new Date().getTime());
        }

        logisticsListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.LogisticsListModel',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: 'logisticsprint/list'
                    //read: 'assets/js/desktop/logistics/List.json'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'message',
                    totalProperty: 'data.obj.totalCount'
                }
            },
            autoSync: true,
            autoLoad: true,
            pageSize:15
        });

        searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'logisticsSearch',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'button',
                    text: '添加',
                    itemId: 'addBtn',
                    handler: showAddWin
                },
                {
                    xtype: 'button',
                    text: '刷新',
                    itemId: 'reloadBtn',
                    handler: function () {
                        Espide.Common.reLoadGird('logisticsGrid', false, true);
                    }
                }
            ]
        });

        logisticsGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: logisticsListStore,
            forceFit: true,
            id: 'logisticsGrid',
            selType: 'checkboxmodel',
            bbar: new Ext.PagingToolbar({
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: logisticsListStore,
                displayInfo: true,
                emptyMsg: '没有记录'
            }),
            columns: [
                {
                    text: '编号',
                    dataIndex: 'id',
                    width: 60,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    text: '物流公司',
                    dataIndex: 'name',
                    width: 200,
                    renderer: Espide.Common.getExpress
                },
                {
                    text: '所属仓库',
                    dataIndex: 'repoName',
                    sortable:true,
                    width: 200,
                    renderer:function(value){
                        if(value == null){
                            return '全部';
                        }else{
                            return value;
                        }
                    }
                },
                {
                    text: '递增数',
                    dataIndex: 'law',
                    width: 60,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    xtype: 'actioncolumn',
                    text: '设计',
                    width: 100,
                    borderRight: 0,
                    sortable: false,
                    menuDisabled: true,
                    items: [
                        {
                            icon: 'assets/images/logistics/design.png',
                            tooltip: '设计',
                            handler: showDesignWin
                        }
                    ]
                }
            ],
            listeners: {
                'itemdblclick': showEditWin
            }
        });

        this.items = [searchForm, logisticsGrid];

        this.callParent(arguments);
    }
});
