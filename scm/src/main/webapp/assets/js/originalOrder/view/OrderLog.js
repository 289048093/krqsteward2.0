/*
 * Created by king on 13-12-17
 */

Ext.define('Supplier.view.OrderLog', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    id: 'OrderLog',
    itemId: 'OrderLog',
    alias: 'widget.OrderLog',
    store: 'OrderLog',
    autoHeight: true,
    autoScroll :true,
    forceFit: true,
    split: true,
    foreFit: false,
    //selType: 'checkboxmodel',
    viewConfig: {
        enableTextSelection: true,
        forceFit: true
    },
    initComponent: function () {
        this.columns = [
            {text: '处理结果', dataIndex: 'processed',},
            {text: '解析失败原因', dataIndex: 'message',},
            {text: '创建时间', dataIndex: 'createTime', renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')},
        ];
        this.callParent(arguments);

    }
})