/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.ExamineList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'ExamineList',
    itemId: 'ExamineList',
    alias: 'widget.ExamineList',
    store: 'ExamineList',
    forceFit: true,
    split: true,
    //selType: 'checkboxmodel',
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {


        this.columns = [

            {text: '订单状态', dataIndex: 'orderStatus', sortable: true,
                renderer: function (value) {
                    return value['value'];
                }
            },
            {text: '操作人', dataIndex: 'operatorName',
                renderer: function (value) {
                    if (value == null) {
                        return '系统';
                    } else {
                        return value;
                    }
                }
            },
            {text: '状态更新时间', dataIndex: 'updateTime', width: 260, sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')},


        ];

        this.callParent(arguments);

    }
})