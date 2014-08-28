/*
 * Created by king on 13-12-20
 */

Ext.define('Supplier.view.Batch', {
    extend: 'Ext.window.Window',
    title: '批量改物流',
    collapsible: true,
    closeAction: 'destroy',
    alias: 'widget.batchState',
    id: 'batchStateWin',
    autoShow: false,
    layout: 'fit',
    modal: true,
    width: 400,
    height: 160,
    initComponent: function () {
        this.items = Ext.create('Ext.form.Panel', {
            itemId: 'batchStateForm',
            layout: {
                type: 'vbox',
                align: 'center'
            },
            bodyPadding: 20,
            border: 0,
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                width: 250,
                queryMode: 'local',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                margin: '0 0 15 0',
                emptyText: '请选择'
            },
            items: [
                {
                    fieldLabel: '快递公司',
                    name: 'shippingComp',
                    store: Espide.Common.expressStore(true)
                },
//                {
//                    fieldLabel: '库房',
//                    name: 'repoId',
//                    valueField: 'id',
//                    displayField: 'name',
//                    store: 'StorageAll'
//                },
//                {
//                    fieldLabel: '店铺',
//                    valueField: 'id',
//                    displayField: 'nick',
//                    name: 'shopName',
//                    store: 'ShopAll'
//                }
            ],
            buttons: {
                layout: {
                    pack: 'center'
                },
                items: [
                    { text: '确定', itemId: 'comfirm' },
                    { text: '取消', itemId: 'cancel' }
                ]
            }
        });

        this.callParent(arguments);
    }
});