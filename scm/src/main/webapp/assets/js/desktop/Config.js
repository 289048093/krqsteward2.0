/**
 * Created by Lein xu.
 */
Ext.define('EBDesktop.Config', {
    extend: 'Ext.ux.desktop.Module',

    id: 'config-win',

    init: function () {
        this.launcher = {
            text: '系统配置',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            root = this,
            win = desktop.getWindow('config-win'),
            configStore = root.self.getConfigStore();

        if (!win) {
            win = desktop.createWindow({
                title: '系统配置',
                id: 'config-win',
                collapsible: true,
                maximizable: true,
                modal: false,
                layout: 'fit',
                width: 900,
                height: 602,
                items: [
                    root.self.createShopGrid(configStore)
                ]
            })
        }
        return win;
    },

    statics: {

        //生成配置数据表格
        createShopGrid: function (store) {
            return Ext.create('Ext.grid.Panel', {
                region: 'west',
                id: 'ConfigList',
                store: store,
                forceFit: true,
                columns: [
                    {
                        text: '配置key', dataIndex: 'name'
                    },
                    {
                        text: '配置value',
                        dataIndex: 'value',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    },
                    {

                        text: '配置描述',
                        dataIndex: 'description',
                        editor: {
                            xtype: "textfield",
                            allowBlank: false
                        }
                    }


                ],
                listeners: {
                    'itemdblclick': function (view, record, item, index, event) {//单元格绑定双击事件

                        var formItems = [];
                        formItems.push({
                                fieldLabel: '配置id',
                                name: 'id',
                                hidden: true,
                                value: record.get('id')
                            }, {
                                fieldLabel: '配置key',
                                name: 'name',
                                value: record.get('name'),
                                hidden: true
                            }, {
                                fieldLabel: '配置value',
                                name: 'value',
                                value: record.get('value')
                            },
                            {
                                name: 'description',
                                fieldLabel: '配置描述',
                                xtype: 'textareafield',
                                height: 150,
                                value: record.get("description")
                            }

                        );
                        var configUpdateForm = Ext.create('Ext.form.Panel', {
                            baseCls: 'x-plain',
                            labelWidth: 80,
                            id: 'configUpdateForm',
                            forceFit: true,
                            border: false,
                            layout: 'form',
                            header: false,
                            frame: false,
                            bodyPadding: '5 5 0',
                            requires: ['Ext.form.field.Text'],
                            fieldDefaults: {
                                msgTarget: 'qtip',
                                blankText: '不能为空',
                                allowBlank: false,
                                labelAlign: "left",
                                labelSeparator: "：",
                                labelWidth: 75
                            },
                            defaultType: 'textfield',
                            items: formItems

                        });

                        var winUpdate = Ext.create("Ext.window.Window", {
                            title: "修改配置",
                            width: 400,
                            height: 300,
                            items: configUpdateForm,
                            buttonAlign: "right",
                            modal: true,
                            buttons: [
                                {
                                    text: '保存',
                                    itemId: 'updateBtn',
                                    handler: function (btn) {

                                        if (configUpdateForm.getForm().isValid()) {
                                            configUpdateForm.getForm().submit({
                                                url: "/conf/addOrUpdate",
                                                params: Ext.getCmp('configUpdateForm').getForm().getValues(),
                                                success: function (form, action) {
                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    if (data.success) {
                                                        Ext.MessageBox.show({
                                                            title: '提示',
                                                            msg: data.msg,
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: 'x-message-box-info',
                                                            fn: function () {
                                                                winUpdate.close();
                                                                Ext.getCmp('ConfigList').getStore().load();
                                                            }
                                                        });
                                                    }
                                                },
                                                failure: function (form, action) {

                                                    var data = Ext.JSON.decode(action.response.responseText);
                                                    Ext.MessageBox.show({
                                                        title: '提示',
                                                        msg: data.msg,
                                                        buttons: Ext.MessageBox.OK,
                                                        icon: 'x-message-box-warning'
                                                    });

                                                }
                                            });
                                        }
                                    }
                                }
                            ]
                        });
                        //更新窗口
                        winUpdate.show();
                    }
                }
            });
        },

        // refreshConfigGrid
        refreshConfigGrid: function () {
            Ext.getCmp('ConfigList').getStore().load();
        },

        //生成配置store
        getConfigStore: function () {

            Ext.define('ConfigModel', {
                //不要忘了继承
                extend: 'Ext.data.Model',
                fields: [
                    'id',
                    'name',
                    'value',
                    'description'

                ],
                idProperty: 'id'
            });

            return Ext.create('Ext.data.Store', {
                model: 'ConfigModel',
                proxy: {
                    type: 'ajax',
                    url: '/conf/list',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.obj.result',
                        messageProperty: 'msg'
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
                autoLoad: true
            });
        }
    }
});