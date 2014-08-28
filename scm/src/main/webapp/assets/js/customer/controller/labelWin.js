/**
 * Created by king on 13-12-23
 */

Ext.define('Customer.controller.labelWin', {
    extend: 'Ext.app.Controller',
    views: ['labelWin.labelManage','labelWin.labelList'],
    init: function () {
        this.control({
            //标签管理
            '#labelManager': {
                'click': function (btn) {
                    Ext.widget('labelManage').show(this, function () {
                            Ext.getCmp('labelGridpanel').getStore().load();
                    });
                }
            },
        })
    }
});