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
    height:'auto',
    autoScroll :true,
    forceFit: true,
    split: true,
    //selType: 'checkboxmodel',
    viewConfig: {
        enableTextSelection: true,
        forceFit: true
    },
    initComponent: function () {


        this.columns = [

            {text: '订单详细日志', dataIndex: 'log',width:500},



        ];

        this.callParent(arguments);

    }
})