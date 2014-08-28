/**
 * Created by Lein xu
 */
Ext.define('StorageFlowLog.controller.LogList', {
    extend: 'Ext.app.Controller',
    views: ['LogList'],
    stores: ['LogList', 'BusinessLog'],
    models: ['LogList', 'BusinessLog'],
    init: function () {
        var start = "", end = "" , type;

        this.control({
            '#search': {
                click: function (btn) {
                    Espide.Common.doSearch('LogListGrid','search',false);
                }
            },

            "#LogListGrid": {
                //order表格有 选择项则把底部商品表 格显示
                selectionchange: function (sm, records) {

                    var record = records[records.length - 1];
                    var businessGrid = Ext.getCmp('BusinessGrid');
                    if (record) {
                        var store =businessGrid.getStore();
                        store.load({
                            params: {
                                sku: record.get('sku'),
                                minDate: start,
                                maxDate: end,
                                repositoryId: record.get('repositoryId'),
                                type: type
                            }
                        });

                        businessGrid.show();

                    } else {
                        Ext.getCmp('BusinessGrid').hide();
                    }

                },
                afterrender: function (grid) {

                    var root = this,
                        store = grid.getStore();

                    start = Ext.util.Format.date(Ext.getCmp('startDate').getRawValue(), 'Y-m-d H:i:s');
                    end = Ext.util.Format.date(Ext.getCmp('endDate').getRawValue(), 'Y-m-d H:i:s');


                    store.getProxy().extraParams = {
                        minDate: start,
                        maxDate: end,
                        repositoryId: null,
                        type: null
                    };
                    Ext.Function.defer(function () {

                    }, 700)
                },
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
            '#BusinessGrid': {
                afterrender: function (grid) {

                    var root = this,
                        store = grid.getStore();

                    store.getProxy().extraParams = {
                        minDate: start,
                        maxDate: end,
                        repositoryId: null,
                        type: null

                    };
                    Ext.Function.defer(function () {

                    }, 700)

                },
            }

        });
    }



});