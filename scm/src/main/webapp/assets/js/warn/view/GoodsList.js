/**
 * 商品清单
 * Created by HuaLei.Du on 14-2-19.
 */

Ext.define('Warn.view.GoodsList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.goodsList',
    region: 'center',
    forceFit: true,
    id: 'goodsList',
    store: 'GoodsList',
    style: {
        paddingTop: '5px'
    },
    viewConfig: {
        enableTextSelection: true,
        emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
        markDirty: false
    },
    initComponent: function () {

        this.columns = [
            {
                text: '商品名称',
                dataIndex: 'prodName',
                width: 360
            },
            {
                text: '数量',
                dataIndex: 'prodCount',
                width: 100
            },
            {
                text: '单价（元）',
                dataIndex: 'prodPrice',
                width: 100
            },
            {
                text: '小计（元）',
                dataIndex: 'totalFee',
                width: 100
            }
        ];

        this.callParent(arguments);
    }
});
