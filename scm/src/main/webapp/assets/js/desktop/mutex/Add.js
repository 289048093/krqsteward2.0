/**
 * 添加互斥
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.define('EBDesktop.mutex.Add', {
    extend: 'Ext.window.Window',
    alias: 'widget.mutexAdd',
    title: '添加互斥',
    id: 'mutexAdd',
    modal: true,
    closeAction: 'destroy',
    autoShow: false,
    layout: 'fit',
    width: 410,
    height: 230,
    initComponent: function () {

        Ext.tip.QuickTipManager.init();

        this.items = [
            Ext.create('Ext.form.Panel', {
                id: 'mutexAddForm',
                forceFit: true,
                border: false,
                layout: 'form',
                //url: 'save-form.php',
                header: false,
                frame: false,
                bodyPadding: '5 5 0',
                requires: ['Ext.form.field.Text'],
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 75
                },
                defaultType: 'textfield',
                items: [
                    {
                        xtype: 'combo',
                        name: 'category',
                        itemId: 'category',
                        fieldLabel: '选择类别',
                        value: 'has_deal',
                        allowBlank: false,
                        store: [
                            ['has_deal', '锅'],
                            ['has_deal', '碗'],
                            ['has_deal', '水具']
                        ]
                    },
                    {
                        xtype: 'combo',
                        name: 'mutex_category',
                        itemId: 'mutex_category',
                        fieldLabel: '互斥类别',
                        value: 'has_deal',
                        allowBlank: false,
                        store: [
                            ['has_deal', '垃圾桶'],
                            ['has_deal', '衣架'],
                            ['has_deal', '刀具']
                        ]
                    }
                ],
                buttons: [
                    {
                        text: '保存',
                        itemId: 'addBtn',
                        disabled: true,
                        formBind: true
                    }
                ]
            })
        ];

        this.callParent(arguments);
    }
});