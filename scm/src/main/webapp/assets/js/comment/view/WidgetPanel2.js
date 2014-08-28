/*
 * Created by king on 13-12-17
 */

Ext.define('Comment.view.WidgetPanel2', {
    extend: 'Ext.panel.Panel',
    region: 'south',
    id: 'WidgetPanel2',
    itemId: 'WidgetPanel2',
    alias: 'widget.WidgetPanel2',
    layout: 'fit',
    height:300,
    title:'评价类别',
    initComponent: function () {


        var store = Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'data'],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                api: {
                    read: '/comment/getCategoryScale',
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.list',
                    messageProperty: 'msg'
                },

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
        });


        var colors = ['url(#v-1)',
            'url(#v-2)',
            'url(#v-3)',
            'url(#v-4)',
            'url(#v-5)'];

        var baseColor = '#eee';

        Ext.define('Ext.chart.theme.Fancy', {
            extend: 'Ext.chart.theme.Base',

            constructor: function(config) {
                this.callParent([Ext.apply({
                    axis: {
                        stroke: baseColor
                    },
                    axisLabelLeft: {
                        fill: baseColor
                    },
                    axisLabelBottom: {
                        fill: baseColor
                    },
                    axisTitleLeft: {
                        fill: baseColor
                    },
                    axisTitleBottom: {
                        fill: baseColor
                    },
                    colors: "#fff"
                }, config)]);
            }
        });

        var chart = Ext.create('Ext.chart.Chart', {
            theme: 'Fancy',
            animate: {
                easing: 'bounceOut',
                duration: 750
            },
            store: store,
            background: {
                fill: 'rgb(60, 60, 60)'
            },
            gradients: [
                {
                    'id': 'v-1',
                    'angle': 0,
                    stops: {
                        0: {
                            color: 'rgb(212, 40, 40)'
                        },
                        100: {
                            color: 'rgb(117, 14, 14)'
                        }
                    }
                },
                {
                    'id': 'v-2',
                    'angle': 0,
                    stops: {
                        0: {
                            color: 'rgb(180, 216, 42)'
                        },
                        100: {
                            color: 'rgb(94, 114, 13)'
                        }
                    }
                },
                {
                    'id': 'v-3',
                    'angle': 0,
                    stops: {
                        0: {
                            color: 'rgb(43, 221, 115)'
                        },
                        100: {
                            color: 'rgb(14, 117, 56)'
                        }
                    }
                },
                {
                    'id': 'v-4',
                    'angle': 0,
                    stops: {
                        0: {
                            color: 'rgb(45, 117, 226)'
                        },
                        100: {
                            color: 'rgb(14, 56, 117)'
                        }
                    }
                },
                {
                    'id': 'v-5',
                    'angle': 0,
                    stops: {
                        0: {
                            color: 'rgb(187, 45, 222)'
                        },
                        100: {
                            color: 'rgb(85, 10, 103)'
                        }
                    }
                }],
            axes: [{
                type: 'Numeric',
                position: 'left',
                fields: ['data'],
                minimum: 0,
                maximum: 100,
                label: {
                    renderer: Ext.util.Format.numberRenderer('0,0')
                },
                title: '类别单数占比',
                grid: {
                    odd: {
                        stroke: '#555'
                    },
                    even: {
                        stroke: '#555'
                    }
                }
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['name'],
                title: '评论分类'
            }],
            series: [{
                type: 'column',
                axis: 'left',
                highlight: true,
                label: {
                    display: 'insideEnd',
                    'text-anchor': 'middle',
                    field: 'data',
                    orientation: 'horizontal',
                    fill: '#fff',
                    font: '17px Arial'
                },
                renderer: function(sprite, storeItem, barAttr, i, store) {
                    barAttr.fill = colors[i % colors.length];
                    return barAttr;

                },
                style: {
                    opacity: 0.95
                },
                xField: 'name',
                yField: 'data'
            }]
        });



        this.items = [
            chart
        ];

        this.callParent(arguments);

    }
});


