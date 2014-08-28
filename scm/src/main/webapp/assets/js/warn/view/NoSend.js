/**
 * 需要确认发货的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.view.NoSend', {
    extend: 'Ext.container.Container',
    alias: 'widget.noSend',
    id: 'noSend',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var noSendGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: 'NoSend',
            title: '需要确认发货的订单',
            id: 'noSendGrid',
            forceFit: true,
            viewConfig: {
                //enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false
            },
            columns: [
                {
                    text: '订单号',
                    dataIndex: 'orderNo',
                    width: 160
                },
                {
                    text: '付款时间',
                    dataIndex: 'payTime',
                    width: 160
                },
                {
                    text: '状态',
                    dataIndex: 'orderStatus',
                    width: 160
                }
            ]
        });

        this.items = [noSendGrid];

        this.callParent(arguments);
    }
});
