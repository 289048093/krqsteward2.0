/**
 * Created by king on 13-12-23
 */

Ext.define('Returnvisit.view.allocationWin.EmployeeList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    alias: 'widget.EmployeeList',
    id: 'EmployeeList',
    height: 150,
    forceFit: true,
    split:true,
    store: Ext.create('Ext.data.Store', {
        //记得设置model
        fields: [
            'id',
            'name',
            'username',
            {name:'apprortionCount',type:'auto',defaultValue:0}
        ],
//        extraParams: {
//            roleName:'回访专员'
//        },
        proxy: {
            type: 'ajax',
            url: 'returnvisit/findVisiter',
            reader: {
                type: 'json',
                successProperty: 'success',
                root: 'data.list',
                messageProperty: 'message'
            },
            listeners: {
                exception: function(proxy, response, operation){
                    var data = Ext.decode(response.responseText);
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: data.msg,
                        icon: Ext.MessageBox.ERROR,
                        buttons: Ext.Msg.OK
                    });
                }
            }
        },
        autoLoad: false
    }),
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {
        this.tbar = [
            Ext.create('Ext.form.Panel', {
                layout: 'hbox',
                border: false,
                itemId: 'goodSearch',
                items: [
                    {
                        xtype:'textfield',
                        hidden: true,
                        id: 'hideOrderId'
                    },
                    {
                        xtype: 'combo',
                        hideLabel: true,
                        name: 'searchType',
                        width: 90,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        value: 'name',
                        store: [
                            ['name', '真实姓名'],
                            ['productNo', '用户名']
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
                        margin: '0 5 0 0'
                    },
                    {
                        xtype: 'button',
                        text: '搜索',
                        margin: '0 5 0 0',
                        itemId: 'searchBtn'
                    }
                ]
            }),
            '->',
            {xtype: 'textfield',  width: 150, id:'total',itemId: 'total', fieldLabel: '待分配', labelWidth: 60,readOnly:true},
            {xtype: 'textfield',  width: 150,id:'allocationTotal', value:0,   itemId: 'allocationTotal', fieldLabel: '已分配条数', labelWidth: 70,readOnly:true},
        ];
        this.columns = [
            {text: '序号', dataIndex: 'id'},
            {text: '姓名', dataIndex: 'name'},
            {
                xtype: 'actioncolumn',
                text: '操作',
                itemId: 'addRow',
                menuDisabled: true,
                width: 50,
                iconCls: 'icon-add'
            }
        ];
        this.callParent(arguments);
    }
});
