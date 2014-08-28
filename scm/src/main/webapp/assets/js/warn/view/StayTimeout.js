/**
 * 物流信息长时间未更新
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.view.StayTimeout', {
    extend: 'Ext.container.Container',
    alias: 'widget.stayTimeout',
    id: 'stayTimeout',
    fixed: true,
    layout: 'border',
    initComponent: function () {

        var stayTimeoutGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            store: 'StayTimeout',
            title: '物流信息长时间未更新',
            id: 'stayTimeoutGrid',
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
                    text: '物流最后更新时间',
                    dataIndex: 'latestTime',
                    width: 160
                }
            ]
        });

        this.items = [stayTimeoutGrid];

        this.callParent(arguments);
    }
});
