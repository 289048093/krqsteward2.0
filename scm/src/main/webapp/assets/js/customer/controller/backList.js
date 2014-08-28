/**
 * Created by king on 13-12-23
 */

Ext.define('Customer.controller.backList', {
    extend: 'Ext.app.Controller',
    views: ['backList.backListManage','backList.backList'],
    init: function () {
        this.control({
            //标签管理
            '#backListManager': {
                'click': function (btn) {
                    Ext.widget('backListManage').show(this, function () {
                            Ext.getCmp('backListGridpanel').getStore().load();
                    });
                }
            },
        })
    }
});