/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 库房管理
 */

Ext.define('EBDesktop.Good', {
    extend: 'Ext.ux.desktop.Module',

    id:'good-win',

    init : function(){
        this.launcher = {
            text: '商品管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('storage-win');
        var goodStore = this.self.getStore();
        if(!win){
            win = desktop.createWindow({
                title: '商品管理',
                collapsible: true,
                maximizable: true,
                modal: false,
                layout: 'border',
                width: 700,
                height: 400,
                items:[
                    Ext.create('Ext.form.Panel', {
                            region: 'north',
                            layout: 'hbox',
                            border: false,
                            bodyPadding: 10,
                            defaults: {
                                margin: '0 5 0 0'
                            },
                            items: [
                                {
                                    xtype: 'combo', fieldLabel: '选择库房', value: 'sn_storage', labelWidth: 60, width: 160, name: 'storage',
                                    store: [
                                        ['sn_storage', '尚尼新兴仓'],
                                        ['idix_storage', '爱东爱西仓']
                                    ]
                                },
                                {xtype: 'filefield', width: 200, buttonText: '导入商品', itemId: 'importBtn', msgTarget: 'side', allowBlank: false},
                                {xtype: 'textfield', emptyText: '选输入关键词', name: 'search_text', width: 100},
                                {
                                    xtype: 'combo', value: 'field_1', name: 'search_type', width: 70,
                                    store: [
                                        ['field_1', '字段1'],
                                        ['field_2', '字段2'],
                                        ['field_3', '字段3']
                                    ]
                                },
                                {xtype: 'button', text: '搜索', itemId: 'searchBtn'},
                                {xtype: 'button', text: '重置', itemId: 'resetBtn'}
                            ]
                        }),
                    Ext.create('Ext.grid.Panel', {
                            store: goodStore,
                            forceFit: true,
                            region: 'center',
                            plugins: [
                                {ptype: 'cellediting',clicksToEdit: 2 }
                            ],
                            bbar: Ext.create('Ext.PagingToolbar', {
                                store: goodStore,
                                displayInfo: true,
                                displayMsg: '当前页 {0} - {1} of {2}',
                                emptyMsg: "没有页了"
                            }),
                            columns: [
                                {
                                    text: 'ID', dataIndex: 'id'
                                },
                                {
                                    text: '编码', dataIndex: 'good_number',
                                    editor: {
                                        xtype: 'textfield'
                                    }
                                },
                                {
                                    text: '产品品牌', dataIndex: 'good_brand',
                                    editor: this.self.brand
                                    //renderer: Ext.ux.renderer.ComboRenderer(this.combos.brand)
                                },
                                {
                                    text: '商品名称', dataIndex: 'good_name',
                                    editor: {
                                        xtype: 'textfield'
                                    }
                                }
                            ]
                        })
                ]
            })
        }
        return win;
    },

    statics: {
        getStore: function () {
            Ext.define('Good',
                {
                    //不要忘了继承
                    extend:'Ext.data.Model',
                    fields:[
                        {name: 'id',type: 'int', useNull: true},
                        'good_number','good_brand','good_name'
                    ],
                    idProperty: 'id'
                }
            );

            var store = Ext.create('Ext.data.Store',{
                model: 'Good',
                proxy: {
                    type: 'ajax',
                    extraParams: {
                        orders: 0
                    },
                    api: {
                        read: 'static/js/order/data/goodRead.json',
                        create: 'static/js/order/data/common.json',
                        update: 'static/js/order/data/common.json',
                        destroy: 'static/js/order/data/common.json'
                    },
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data',
                        messageProperty: 'message',
                        totalProperty: 'totalCount'
                    },
                    writer: {
                        type: 'json',
                        encode: true,
                        writeAllFields: true,
                        root: 'data'
                    }
                },
                autoLoad: true,
                autoSync: true,
                pageSize: 50
            });

            return store;
        },
        brand: {
            xtype: 'combo',
            typeAhead: true,
            triggerAction:     'all',
            displayField: 'name',
            valueField: 'value',
            store: Ext.create('Ext.data.Store', {
                fields: ['value', 'name'],
                data : [
                    {value: "brand_1", name: "尚尼"},
                    {value: "brand_2", name: "达服"},
                    {value: "brand_3", name: "小雄"}
                ]
            })
        }
    }
});

