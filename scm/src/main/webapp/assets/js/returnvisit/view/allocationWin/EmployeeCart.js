/**
 * Created by king on 13-12-23
 */

Ext.define('Returnvisit.view.allocationWin.EmployeeCart', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.EmployeeCart',
    region: 'south',
    height: 160,
    id: 'EmployeeCart',
    forceFit: true,
    split: true,
    store: 'EmployeeCart',
    selType: 'checkboxmodel',
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {
        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 1
            })
        ];
        this.features =  [
            {
                ftype: 'summary'
            }
        ];
        this.columns = [
            {text: '序号', dataIndex: 'employeeId',},
            {text: '姓名', dataIndex: 'username',},
            {text: '分配数目', dataIndex: 'apportionCount',
                editor: {
                    xtype: 'numberfield',
                    allowDecimals: false,
                    minValue: 1
                },
                summaryType: function (records) {
                    var i = 0,
                        length = records.length,
                        total = 0,
                        record;

                    for (; i < length; ++i) {
                        record = records[i];
                        total +=  record.get('apportionCount');
                    }
                    Ext.getCmp('allocationTotal').setValue(total);

//                    if(total> Ext.getCmp('total').getValue()){
//                        Espide.Common.showGridSelErr('易萌说待分配不能大于分配任务，梦龙你回来智商低了很多！');
//                        return 0;
//                    }


                    return total;

                },

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
                }
            }
        ];
        this.callParent(arguments);
    }
})