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
Ext.define('EBDesktop.module.moduleList', {
    extend: 'Ext.container.Container',
    alias: 'widget.moduleList',
    id: 'moduleList',
    title: "模块管理",
    layout: "border",
    fixed: true,
    initComponent: function () {
        // 刷新moduleListGrid
        function refreshModuleListGrid() {
            Ext.getCmp('moduleListGrid').getStore().load();
        }


        //顶栏表单
        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'searchModule',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                margin: '0 10 0 0'
            }
        });
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
                        header: '模块内容',
                        dataIndex: 'name'

                    },
                    {
                        header: '模块路径',
                        dataIndex: 'url'
                    }
                ],
                features: [
                    {
                        id: 'group',
                        ftype: 'grouping',
                        groupHeaderTpl: '{name}({rows.length})',
                        startCollapsed: true
                    }
                ]
            }
        );

        this.items = [searchForm, moduleListGrid];
        this.callParent(arguments);

    }

});
