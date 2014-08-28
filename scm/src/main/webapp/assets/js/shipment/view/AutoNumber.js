/*
 * Created by king on 13-12-20
 */

Ext.define('Supplier.view.AutoNumber', {
    extend: 'Ext.window.Window',
    title: '联想单号',
    collapsible: true,
    closeAction: 'destroy',
    alias: 'widget.autoNumber',
    id: 'autoNumberWin',
    autoShow: false,
    layout: 'fit',
    modal: true,
    width: 400,
    height: 240,
    initComponent: function () {
        this.items = Ext.create('Ext.form.Panel', {
            itemId: 'autoNumberForm',
            layout: {
                type: 'vbox',
                align: 'center'
            },
            bodyPadding: 30,
            border: 0,
            defaults: {
                xtype: 'combo',
                labelWidth: 60,
                width: 250,
                queryMode: 'local',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                margin: '0 0 15 0'
            },
            items: [
                {
                    fieldLabel: '快递公司',
                    emptyText: '请选择',
                    name: 'shippingComp',
                    id: 'shippingComp',
                    itemId: "shippingComp",
                    readOnly: true,
                    allowBlank: false,
                    store: Espide.Common.expressStore()
                },
                {
                    xtype: 'textfield',
                    editable: true,
                    fieldLabel: '起始单号',
                    name: 'intNo',
                    allowBlank: false
                },
                {
                    xtype: 'checkbox',
                    fieldLabel: '是否允许覆盖',
                    name: 'isCover',
                    labelWidth: 90,
                    width: 250
                }

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
        this.listeners = {
            show: function (){
                var shippingComp = Espide.Common.getGridSels("OrderList", 'shippingComp')[0];
                Ext.getCmp("autoNumberWin").down("#shippingComp").setValue(shippingComp);
            }
        };
        this.callParent(arguments);
    }
});