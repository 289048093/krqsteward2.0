/*
 * Created by king on 13-12-17
 */

Ext.define('Contract.view.ContractList', {
    extend: 'Ext.container.Container',
    id: 'contractList',
    alias: 'widget.contractList',
    title: "品牌商合同管理",
    fixed: true,
    layout: 'border',
    selType: 'checkboxmodel',
    initComponent: function () {



        //品牌商Store
        var supplierStore = Espide.Common.createComboStore('/supplier/list',true);

        var searchForm = Ext.create('Ext.form.Panel', {
            title: "品牌商合同管理",
            region: 'north',
            layout: {
                type: 'hbox',
                align: 'left'
            },
            height: 'auto',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'search',
            defaultType: 'fieldcontainer',
            defaults: {
                labelWidth: 48,
                style:'margin: 0 10px 0 0',
                xtype: 'textfield'
            },
            items: [
                {
                    xtype: 'combo',
                    name: 'supplierId',
                    itemId: 'supplierId',
                    fieldLabel: '品牌商',
                    width:300,
                    editable: false,
                    queryMode: 'local',
                    valueField: 'id',
                    displayField: 'name',
                    emptyText: '不限',
                    store: supplierStore
                },
                {
                    name: 'code',
                    id:'code',
                    labelWidth: 60,
                    emptyText:'请输入关键字',
                    fieldLabel: '合同编号'
                },
                {
                    xtype: 'button',
                    text: '查询',
                    width: 55,
                    itemId: 'searchBtn'
                }
            ]

        });


//创建角色数据表格容器
        var contractListGrid = Ext.create("Ext.grid.Panel", {
            region: 'center',
            id: 'contractListGrid',
            loadMask: true,
            forceFit: false,
            selType: 'checkboxmodel',
            store: 'ContractList',
            viewConfig: {
                enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false
            },
            columns: [
                new Ext.grid.RowNumberer(),
                {
                    header: '品牌商',
                    width:300,
                    dataIndex: 'supplierName'
                }, {
                    header: '运营商',
                    dataIndex: 'ejsCompName'
                },
                {
                    header: '合同编号',
                    width:130,
                    dataIndex: 'code'
                },
                {
                    header: '合同创建时间',
                    dataIndex: 'beginTime',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },
                {
                    header: '合同结束时间',
                    dataIndex: 'endTime',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },
                {
                    header: '终止时间',
                    dataIndex: 'realEndTime',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },
                {
                    header: '终止原因',
                    dataIndex: 'endReason'
                },
                {
                    header: '滞纳金情况',
                    dataIndex: 'overdueFine'
                },
                {
                    header: '保证金',
                    dataIndex: 'deposit'
                },
                {
                    header: '佣金补差',
                    dataIndex: 'commission'
                },
                {
                    header: '结算规则',
                    dataIndex: 'paymentRule'
                },
                {
                    header: '结算类型',
                    dataIndex: 'paymentType'
                },
                {
                    header: '物流补贴',
                    dataIndex: 'shippingFeeType'
                },
                {
                    header: '拍摄费用',
                    dataIndex: 'shotFeeType'
                },
//                {
//                    header: '第三方平台费用',
//                    width: 180,
//                    dataIndex: 'thirdPlatformFeeType'
//                },
//                {
//                    header: '到易居尚仓库的费用情况',
//                    width: 180,
//                    dataIndex: 'toEJSFeeType'
//                },
                {
                    header: '技术服务费/年',
                    dataIndex: 'serviceFee'
                },
                {
                    header: '开具发票给顾客',
                    dataIndex: 'invoiceEJSTitle'
                },
                {
                    header: '开具发票第三方平台',
                    width: 180,
                    dataIndex: 'invoiceOtherTitle'
                },
                {
                    header: '第三方平台销售是否补开发票给易居尚',
                    width: 250,
                    dataIndex: 'invoiceToEJS'
                },
                {
                    header: '其他条款',
                    dataIndex: 'otherRule'
                },
                {
                    header: '补充协议',
                    dataIndex: 'remark'
                }
            ],
            tbar: [
                {
                    xtype: 'button',
                    text: '删除',
                    iconCls: 'icon-remove',
                    id: "del"
                },
                '-',
                {
                    xtype: 'button',
                    text: '添加',
                    iconCls: 'icon-add',
                    id: "add"
                },
                '-',
                {
                    xtype: 'button',
                    text: '导入合同',
                    iconCls: 'icon-import',
                    id: "include"
                }
            ],
            bbar: new Ext.PagingToolbar({
                pageSize: 10,
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: 'ContractList',
                displayInfo: true,
                emptyMsg: '没有记录'
            })
        });

        this.items = [searchForm, contractListGrid];
        this.callParent(arguments);

    }
});