/**
 * 已经发了货却还没有物流扫描信息的订单
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.view.NoLogistics', {
    extend: 'Ext.container.Container',
    alias: 'widget.noLogistics',
    id: 'noLogistics',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var noLogisticsGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: 'NoLogistics',
            title: '已经发了货却还没有物流扫描信息的订单',
            id: 'noLogisticsGrid',
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
                    text: '发货时间',
                    dataIndex: 'deliveryTime',
                    width: 160
                }
            ]
        });

        this.items = [noLogisticsGrid];

        this.callParent(arguments);
    }
});
