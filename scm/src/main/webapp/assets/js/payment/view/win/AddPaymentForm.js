/**
 * author     : 梦龙
 * createTime : 14-5-8 下午2:55
 * description:
 */



Ext.define('Payment.view.win.AddPaymentForm', {
    extend: 'Ext.form.Panel',
    id: 'createForm',
    region: 'north',
    alias: 'widget.addPaymentForm',
    bodyPadding: 10,
    layout: {
        type: 'hbox',
        align: 'left'
    },
    forceFit: false,
    height: 'auto',
    width:'auto',
    border: 0,
    defaultType: 'fieldcontainer',
    defaults: {
        xtype: 'textfield',
        margin: '0 5 0 0',
    },
    initComponent: function () {
        this.items = [
            {
                xtype: 'combo',
                name: 'conditionQuery',
                width: 150,
                queryMode: 'local',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                value: 'platformOrderNo',
                emptyText: '请选择',
                store: [
                    ['platformOrderNo', '外部平台订单编号'],
                    ['orderNo', '智库城订单编号']
                ],
            },
            {
                name: 'conditionType',
                value: '=',
                hidden: true,
                disabled:true
            },
            {
                xtype: 'textfield',
                id:'conditionValue',
                name: 'conditionValue',
            },
            {
                xtype: 'button',
                text: '查询',
                itemId: 'confirmBtn',
                id: 'searchOrder',
                width: 55,
                height: 25
            }

        ];
        this.callParent(arguments);
    }
})