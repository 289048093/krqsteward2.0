/**
 * 模块列表
 * Created by Lein.xu
 */

//定义模块数据模型
Ext.define('EBDesktop.ModuleListModel', {
    extend: 'Ext.data.Model',
    fields: ["id", 'name', 'url', 'resourceName'],
    idProperty: "id"
});

//定义模块列表类
Ext.define('EBDesktop.permission.moduleList', {
    extend: 'Ext.container.Container',
    alias: 'widget.moduleList',
    id: 'moduleList',
    title: "模块管理",
    layout: "fit",
    fixed: true,
    initComponent: function () {
        // 刷新moduleListGrid
        function refreshModuleListGrid() {
            Ext.getCmp('moduleListGrid').getStore().load();
        }

        // 模块数据集
        var moduleListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.ModuleListModel',
            groupField: 'resourceName',
            proxy: {
                type: 'ajax',
                api: {
                    read: '/operation/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'message'
                }
            },
            autoSync: true,
            autoLoad: true
        });

        //模块列表容器
        var moduleListGrid = Ext.create("Ext.grid.Panel", {
                region: 'center',
                id: 'moduleListGrid',
                loadMask: true,
                forceFit: true,
                store: moduleListStore,
                disableSelection: false,
                columns: [
                    {
                        header: '模块ID',
                        dataIndex: 'id'
                    },
                    {
                        header: '模块内容',
                        dataIndex: 'name'

                    },
                    {
                        header: '模块路径',
                        dataIndex: 'url'
                    }
                ],
                tbar: [
                    {
                        text: '刷新',
                        iconCls: 'icon-refresh',
                        handler: refreshModuleListGrid
                    }
                ],
                features: [
                    {
                        id: 'group',
                        ftype: 'grouping',
                        groupHeaderTpl: '{name}({rows.length})',
                        startCollapsed: true
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: moduleListStore,
                    displayInfo: true
                })
            }
        );

        this.items = [moduleListGrid];
        this.callParent(arguments);

    }

});
