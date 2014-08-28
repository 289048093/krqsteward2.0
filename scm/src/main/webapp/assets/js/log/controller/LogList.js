/**
 * Created by Lein xu
 */
Ext.define('Log.controller.LogList', {
    extend: 'Ext.app.Controller',
    views: ['LogList'],
    stores: ['LogList', 'BusinessLog'],
    models: ['LogList', 'BusinessLog'],
    init: function () {
        this.control({
            "#LogListGrid": {
                //order表格有 选择项则把底部商品表 格显示
//                selectionchange: function (sm, records) {
//
//                    var record = records[records.length - 1];
//                    if (record) {
//                        var store = Ext.getCmp('BusinessGrid').getStore();
//                        store.load({
//                            params: {
//                                businessLogId: record.get('id')
//                            }
//                        });
//
//                        Ext.getCmp('BusinessGrid').show();
//                    } else {
//                        Ext.getCmp('BusinessGrid').hide();
//                    }
//
//                },
                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.reLoadGird('LogListGrid', 'search', true);
                                }
                            }
                        ]
                    });
                }


            },
            "#refresh": {
                'click': function (button) {
                    Ext.getCmp('LogListGrid').getStore().load();
                }
            }
        });
    }



});