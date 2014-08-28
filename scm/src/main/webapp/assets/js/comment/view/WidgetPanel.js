/*
 * Created by king on 13-12-17
 */

Ext.define('Comment.view.WidgetPanel', {
    extend: 'Ext.panel.Panel',
    region: 'south',
    id: 'WidgetPanel',
    itemId: 'WidgetPanel',
    alias: 'widget.WidgetPanel',
    layout: 'fit',
    title:'评价结果',
    initComponent: function () {


        var store = Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'data'],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                api: {
                    read: '/comment/getResultScale',
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'msg'
                },
//        writer: {
//            type: 'json',
//            encode: true,
//            writeAllFields: false,
//            root: 'data'
//        },
                listeners: {
                    exception: function (proxy, response, operation) {
                        var data = Ext.decode(response.responseText);
                        Ext.MessageBox.show({
                            title: '警告',
                            msg: data.msg,
                            icon: Ext.MessageBox.ERROR,
                            buttons: Ext.Msg.OK
                        });
                    }
                }
            },
//            data: [
//                { 'name': '好评',   'data': 1 },
//                { 'name': '中评',   'data':  2 },
//                { 'name': '差评', 'data':  1 },
//            ]
        });

      var chart =   Ext.create('Ext.chart.Chart', {
           // renderTo: Ext.getBody(),
            width: 200,
            height: 200,
            animate: true,
            store: store,
            theme: 'Category1:gradients',
            series: [{
                type: 'pie',
                angleField: 'data',
                showInLegend: true,
                tips: {
                    trackMouse: true,
                    width: 140,
                    height: 28,
                    renderer: function(storeItem, item) {
                        // calculate and display percentage on hover
                        var total = 0;
                        store.each(function(rec) {
                            total += rec.get('data');
                        });
                        this.setTitle(storeItem.get('name') + ': ' + Math.round(storeItem.get('data') / total * 100) + '%'+' (共：'+storeItem.get('data')+'单)');
                    }
                },
                highlight: {
                    segment: {
                        margin: 20
                    }
                },
                label: {
                    field: 'name',
                    display: 'rotate',
                    contrast: true,
                    font: '18px Arial'
                }
            }]
        });



        this.items = [
            chart
        ];

        this.callParent(arguments);

    }
});


