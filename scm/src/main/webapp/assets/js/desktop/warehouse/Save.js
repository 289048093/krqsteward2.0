/**
 * 添加仓库
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.define('EBDesktop.warehouse.Save', {
    extend: 'Ext.window.Window',
    alias: 'widget.warehouseSave',
    title: '添加仓库',
    id: 'warehouseSave',
    modal: true,
    closeAction: 'destroy',
    autoShow: false,
    constrain: true,
    animateTarget: Ext.getBody(),
    fixed: true,
    layout: 'border',
    width: 500,
    height: 600,
    initComponent: function () {

        Ext.tip.QuickTipManager.init();

        // 保存表单
        function saveForm(btn) {
//            var formEle = btn.up('form'),
            var formEle = Ext.getCmp('warehouseSaveForm'),
                form = formEle.getForm(),
                com = Espide.Common,
                options = {
                    url: formEle.down('[itemId=action]').getValue()
                };


            //检测是否选择了用户表数据
            if(options == "/repository/add"){
                if (!com.checkGridSel('userGrid', '用户表必须选择一条数据'))  return;
            }





            Espide.Common.submitForm(form, options, function () {
                Ext.getCmp('warehouseSave').close();
                Ext.getCmp('warehouseGrid').getStore().load();
            });
        }


        //搜索用户名




        var  employeeStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            fields:[
                'id',
                'username',
                'name'
            ],
            proxy: {
                type: 'ajax',
                api: {
                    read: 'employee/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'msg'
                }
            },
            autoLoad: true,
            pageSize: 15
        });

        var  AddEmployeeStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            fields:[
                'id',
                'username',
                'name'
            ],
            autoLoad: false
        });


        Ext.ClassManager.setAlias('Ext.selection.CheckboxModel','selection.checkboxmodel');

        var  provinceStore = Ext.create('Ext.data.Store', {
            extend: 'Ext.data.Store',
            fields:[
                'id',
                'name'
            ],
            proxy: {
                type: 'ajax',
                url: 'province/findAll',
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'message'
                }
            },
            autoLoad:true
        });


        this.buttons=[
            {
                text: "保存",
                handler:saveForm
            }
        ];

        this.items = [
            Ext.create('Ext.form.Panel', {
                region: 'north',
                id: 'warehouseSaveForm',
                forceFit: false,
                border: false,
                layout: 'form',
                header: false,
                frame: false,
                split: true,
                fixed: true,
                height:'auto',
                bodyPadding: '5 5 0',
                requires: ['Ext.form.field.Text'],
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 75
                },
                defaultType: 'textfield',
                items: [
                    {
                        fieldLabel: 'id',
                        name: 'id',
                        itemId: 'id',
                        hidden: true
                    },
                    {
                        fieldLabel: 'chargePersonId',
                        name: 'chargePersonId',
                        itemId: 'chargePersonId',
                        id: 'chargePersonId',
                        hidden: true
                    },
                    {
                        fieldLabel: 'chargerIds',
                        name: 'chargerIds',
                        itemId: 'chargerIds',
                        id: 'chargerIds',
                        hidden: true
                    },
                    {
                        fieldLabel: 'action',
                        name: 'action',
                        itemId: 'action',
                        hidden: true
                    },
                    {
                        fieldLabel: '仓库名称',
                        name: 'name',
                        itemId: 'name',
                        emptyText:'请输入仓库名称',
                        allowBlank: false,
                        blankText: '不能为空',
                        validator: function (v) {

                            if (!/(^\S+.*\S+$)|(^\S{1}$)/.test(v)) {
                                return '格式不对，前后不能包含空格';
                            }

                            if (v.replace(/[^\x00-\xff]/g, "rr").length <= 50) {
                                return true;
                            }

                            return '仓库名称不能多于50个字符';
                        }
                    },
                    {
                        fieldLabel: '仓库编码',
                        name: 'code',
                        itemId: 'repoCode',
                        allowBlank: false,
                        emptyText:'请输入仓库编码',
                        blankText: '不能为空',
                        validator: function (v) {

                            if (!/^[A-Za-z0-9]*$/.test(v)) {
                                return '仓库编码只能为字母和数字';
                            }

                            if (v.replace(/[^\x00-\xff]/g, "rr").length <= 16) {
                                return true;
                            }

                            return '仓库编码不能多于16个字符';
                        }
                    },
                    {
                        fieldLabel: '地址',
                        name: 'address',
                        itemId: 'address',
                        allowBlank: false,
                        emptyText:'请输入仓库地址',
                        blankText: '不能为空',
                        validator: function (v) {

                            if (v.replace(/[^\x00-\xff]/g, "rr").length <= 200) {
                                return true;
                            }

                            return '地址不能多于200个字符';
                        }
                    },
//                    {
//                        fieldLabel: '联系人',
//                        name: 'chargePerson',
//                        itemId: 'chargePerson',
//                        emptyText:'请输下面表格中选择联系人',
//                        id: 'chargePerson',
//                        allowBlank: false,
//                        blankText: '不能为空',
//                        validator: function (v) {
//
//                            if (v.replace(/[^\x00-\xff]/g, "rr").length <= 20) {
//                                return true;
//                            }
//
//                            return '联系人不能多于20个字符';
//                        },
//                        listeners:{
//                            focus:function(){
//                                Ext.getCmp('userGrid').show();
//                            }
//                        }
//                    },
                    {
                        xtype: 'combo',
                        fieldLabel: '物流公司',
                        name: 'shippingComp',
                        itemId: 'shippingComp',
                        allowBlank: false,
                        blankText: '不能为空',
                        queryMode: 'local',
                        editable: false,
                        emptyText: '请选择',
                        store: Espide.Common.expressStore()
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: '省份',
                        name: 'provinceId',
                        itemId: 'provinceId',
                        id: 'provinceId',
                        //queryMode: "remote",
                        forceSelection: true,
                        triggerAction: 'all',
                        editable: false,
                        displayField: "name",
                        valueField: 'id',
                        emptyText: '请选择省份',
                        allowBlank: false,
                        store:provinceStore
                    },
                    {
                        fieldLabel: '电话',
                        name: 'chargePhone',
                        itemId: 'chargePhone',
                        emptyText: '格式：0755-88888888',
                        vtype: 'Phone'
                    },
                    {
                        fieldLabel: '手机',
                        name: 'chargeMobile',
                        itemId: 'chargeMobile',
                        emptyText: '格式：13800138000',
                        vtype: 'Mobile'
                    }
                ],

            }),
            //数据展示表格
            Ext.create('Ext.grid.Panel', {
                region: 'center',
                id: 'userGrid',
                loadMask: true,
                forceFit: true,
                split:true,
                border: '0',
                store: employeeStore,
                viewConfig: {
                    enableTextSelection: true
                },
                tbar:[
                    Ext.create('Ext.form.Panel', {
                        layout: 'hbox',
                        border: false,
                        itemId: 'userSearch',
                        id: 'userSearch',
                        items: [

                            {
                                xtype: 'combo',
                                hideLabel: true,
                                name: 'searchType',
                                width: 90,
                                queryMode: 'local',
                                triggerAction: 'all',
                                forceSelection: true,
                                editable: false,
                                value: 'userName',
                                store: [
                                    ['userName', '用户名'],
                                    ['realName', '真实姓名']
                                ],
                                margin: '0 5 0 0'
                            },
                            {
                                xtype: 'textfield',
                                emptyText: '请输入关键字',
                                itemId: 'keyword',
                                allowBlank: false,
                                name: 'searchValue',
                                width: 120,
                                margin: '0 5 0 0',
                                listeners:{
                                    'change':function(){
                                        Espide.Common.reLoadGird('userGrid', 'userSearch', true);
                                    }
                                }
                            },
                            {
                                xtype: 'button',
                                text: '搜索',
                                margin: '0 5 0 0',
                                itemId: 'searchBtn',
                                handler:function(btn){
                                    employeeStore.load({
                                        params:Ext.getCmp('userSearch').getValues()
                                    })
                                }

                            }
                        ]
                    })
                ],
                columns: [
                    {
                        text: '用户名',
                        dataIndex: 'username',
                        width: 100
                    },
                    {
                        text: '真实姓名',
                        dataIndex: 'name',
                        width: 80
                    },
                    {
                        xtype: 'actioncolumn',
                        text: '操作',
                        itemId: 'addRow',
                        menuDisabled: true,
                        width: 50,
                        iconCls: 'icon-add',
                        handler:function(view,rowIndex,colIndex,item,e,record,row){
                            var addUserItems = Ext.getCmp('AddUserGrid').getStore().data.items,username = record.get('username');
                            if(addUserItems.length == 0){
                                Ext.getCmp('AddUserGrid').getStore().add(record);
                                Ext.getCmp('chargerIds').setValue(record.get('id'));
                            }else{
                                //判断是否可以添加
                                function isCanAdded(username){
                                    var flag = false;
                                    Ext.each(addUserItems, function (records, index, root) {
                                        if (records.get('username') == username) {
                                            //Espide.Common.showGridSelErr('用户不能重复添加');
                                            return flag =true;
                                        }
                                    });
                                    return flag;
                                }

                                if(isCanAdded(username)){
                                    Espide.Common.showGridSelErr('用户不能重复添加');
                                    return;
                                }else{
                                    Ext.getCmp('AddUserGrid').getStore().add(record);
                                    var lastestItems = Ext.getCmp('AddUserGrid').getStore().data.items,chargerIds=[];
                                    Ext.each(lastestItems, function (records, index, root) {
                                        chargerIds.push(records.get('id'));
                                    });
                                    Ext.getCmp('chargerIds').setValue(chargerIds.join(','));
                                }
                            }
                        }
                    }
                ],
                listeners:{
//                    select: function (sm, records) {
//
//                       // Ext.getCmp('chargePersonId').setValue(records.get('id'));
//                        Ext.getCmp('chargePerson').setValue(records.get('name'));
//                    }
                }

            }),
            //添加的表格
            Ext.create('Ext.grid.Panel', {
                height:150,
                region: 'south',
                id: 'AddUserGrid',
                loadMask: true,
                forceFit: true,
                split:true,
                border: '0',
                store: AddEmployeeStore,
                multiSelect : false,//支持单选
                viewConfig: {
                    enableTextSelection: true
                },
                columns: [
                    {
                        text: '用户名',
                        dataIndex: 'username',
                        width: 100
                    },
                    {
                        text: '真实姓名',
                        dataIndex: 'name',
                        width: 80
                    },
                    {
                        xtype: 'actioncolumn',
                        text: '操作',
                        menuDisabled: true,
                        width: 50,
                        items: [
                            {
                                iconCls: 'icon-remove'
                            }
                        ],
                        handler: function (view, rowIndex, colIndex, item, e, record) {
                            view.up('grid').getStore().remove(record);
                            var lastestItems = Ext.getCmp('AddUserGrid').getStore().data.items,chargerIds=[];

                            Ext.each(lastestItems, function (records, index, root) {
                                chargerIds.push(records.get('id'));
                            });
                            Ext.getCmp('chargerIds').setValue(chargerIds.join(','));
                        }
                    }
                ]

            })
        ];









        this.callParent(arguments);
    }
});