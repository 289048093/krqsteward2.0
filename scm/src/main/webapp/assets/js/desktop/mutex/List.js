/**
 * 互斥列表
 * Created by HuaLei.Du on 13-12-18.
 */

Ext.define('EBDesktop.MutexListModel', {
    extend: 'Ext.data.Model',
    fields: ['code', 'category', 'mutex_category'],
    idProperty: 'code'
});

Ext.define('EBDesktop.mutex.List', {
    extend: 'Ext.container.Container',
    alias: 'widget.mutexList',
    id: 'mutexList',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        // 显示添加窗口
        function showAddWin() {
            Ext.getCmp('mutexAdd') ? Ext.getCmp('mutexAdd').show() : Ext.widget('mutexAdd').show();
            Ext.getCmp('mutexAddForm').getForm().reset();
        }

        // 移除互斥
        function removeMutex() {
            var grid = Ext.getCmp('mutexGrid'),
                records = grid.getSelectionModel().getSelection();

            if (records.length < 1) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '请先选择要删除的互斥。',
                    modal: false,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
                return;
            }
            grid.getStore().remove(records);
        }

        // 搜索
        function searchMutex(button) {
            var form = button.up('form'),
                formData = form.getValues();

            if (formData.name.length < 1) {
                Ext.MessageBox.show({
                    title: '提示',
                    msg: '请输入仓库名称',
                    modal: false,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error'
                });
                return;
            }

            Ext.getCmp("mutexGrid").getStore().load({
                params: button.up('form').getValues()
            });

        }

        // 刷新Grid
        function refreshGrid() {
            Ext.getCmp('mutexGrid').getStore().load();
        }

        var mutexListStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            model: 'EBDesktop.MutexListModel',
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: 'assets/js/desktop/mutex/List.json'
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

        var searchForm = Ext.create('Ext.form.Panel', {
            region: 'north',
            layout: 'hbox',
            border: 0,
            bodyPadding: 10,
            id: 'mutexSearch',
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                width: 220,
                margin: '0 10 0 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'name',
                    fieldLabel: '商品名称'
                },
                {
                    xtype: 'button',
                    text: '查询',
                    width: 60,
                    itemId: 'searchBtn',
                    handler: searchMutex
                },
                {
                    xtype: 'button',
                    text: '添加互斥',
                    width: 70,
                    itemId: 'addBtn',
                    handler: showAddWin
                },
                {
                    xtype: 'button',
                    text: '删除已选',
                    width: 70,
                    itemId: 'deleteBtn',
                    handler: removeMutex
                },
                {
                    xtype: 'button',
                    text: '刷新列表',
                    width: 70,
                    itemId: 'reloadBtn',
                    handler: refreshGrid
                }
            ]
        });

        var statementGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: mutexListStore,
            forceFit: true,
            id: 'mutexGrid',
            selType: 'checkboxmodel',
            /*viewConfig: {
                enableTextSelection: true
            },*/
            plugins: [
                Ext.create('Ext.grid.plugin.CellEditing', {
                    clicksToEdit: 2
                })
            ],
            columns: [
                {
                    text: '编码',
                    dataIndex: 'code',
                    width: 40,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    text: '类别名称',
                    dataIndex: 'category',
                    width: 200
                },
                {
                    text: '互斥类别',
                    dataIndex: 'mutex_category',
                    width: 200
                }
            ]
        });

        this.items = [searchForm, statementGrid];

        this.callParent(arguments);
    }
})
;