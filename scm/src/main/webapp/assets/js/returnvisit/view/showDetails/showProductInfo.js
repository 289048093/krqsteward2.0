/*
 * Created by king on 13-12-17
 */

Ext.define('ShopProduct.view.showDetails.showProductInfo', {
    extend: 'Ext.form.Panel',
    id: 'showProductInfo',
    region: 'north',
    alias: 'widget.showProductInfo',
    bodyPadding: 10,
    layout: 'anchor',
    split: 'true',
    height: 'auto',
    defaults: {
        margin: '0 0 10 0',
        layout: 'hbox',
        border: 0,
        defaults: {
            xtype: 'textfield',
            margin: '0 10 0 0',
            labelWidth: 60,
            width: 200,
            disabled:true,
            editable: false
        }
    },

    initComponent: function () {
        this.items = [
            {
                items: [
                    {
                        xtype:'textfield',
                        name: 'platformName',
                        id: 'platform',
                        fieldLabel: '平台类型',
                        labelWidth: 60,
                        width: 160,
                    },
                    {
                        xtype:'textfield',
                        fieldLabel: '店铺名称',
                        name:'shopName',
                        valueField: 'id',
                    },
                    {
                        id:'storagePercent',
                        name: 'storagePercent',
                        fieldLabel:'库存占比',
                    },
                    {
                        xtype: 'checkbox',
                        id:'synStatus',
                        name: 'synStatus',
                        fieldLabel:'自动同步库存',
                        labelAlign: 'left',
                        wdith:50,
                        labelWidth: 85,
                        inputValue: true,
                    },
                    {
                        xtype: 'checkbox',
                        name: 'autoPutaway',
                        wdith:200,
                        labelWidth: 140,
                        itemId: 'autoPutaway',
                        id: 'autoPutaway',
                        labelAlign: 'left',
                        value:false,
                        inputValue: true,
                        fieldLabel: '有库存时是否自动上架'
                    }
                ]
            },
        ];
        this.callParent(arguments);
    }
})