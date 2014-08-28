/**
 * 需要确认签收的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.view.NoSign', {
    extend: 'Ext.container.Container',
    alias: 'widget.noSign',
    id: 'noSign',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var noSignGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: 'NoSign',
            title: '需要确认签收的订单',
            id: 'noSignGrid',
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
                    text: '物流单号',
                    dataIndex: 'expressNo',
                    width: 160
                },
                {
                    text: '物流公司',
                    dataIndex: 'expressCompany',
                    width: 160
                },
                {
                    text: '物流开始时间',
                    dataIndex: 'firstTime',
                    width: 160
                },
                {
                    text: '收货地区',
                    dataIndex: 'sendTo',
                    width: 160
                }
            ]
        });

        this.items = [noSignGrid];

        this.callParent(arguments);
    }
});
