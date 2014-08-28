/**
 * Created by Lein xu
 */
Ext.define('Financial.controller.FinancialList', {
    extend: 'Ext.app.Controller',
    models: ['FinancialList'],
    stores: ['FinancialList', 'ShopAll', 'StorageAll'],
    views: ['FinancialList'],
    init: function () {


        this.control({
            '#FinanceListGrid': {
                afterrender: function (grid) {

                    var root = this,
                        com = Espide.Common,
                        store = grid.getStore();

                    store.getProxy().extraParams = Ext.getCmp('search').getValues();

                    Ext.Function.defer(function () {
                        //grid.getStore().getProxy().extraParams = {};
                    }, 700)

                },
                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.doSearch('FinanceListGrid', 'search', true);
                                }
                            }
                        ]
                    });
                },
            },
            '#search': {
                click: function (btn) {
                    Espide.Common.doSearch('FinanceListGrid', 'search', true);
                }
            },
            '#outOrder': {
                click: function (btn) {
                    window.open('/financial/extract2excel?' + Ext.Object.toQueryString(Ext.getCmp('search').getValues()));
                }
            },
            '#TotalOutOrder': {
                click: function (btn) {
                    window.open('/financial/extractMerger2excel?' + Ext.Object.toQueryString(Ext.getCmp('search').getValues()));
                }
            }
        });
    }



});