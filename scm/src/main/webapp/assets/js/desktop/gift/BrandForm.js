/**
 * Created by HuaLei.Du on 14-2-20.
 */

Ext.define('EBDesktop.gift.BrandForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.giftBrandForm',
    id: 'giftBrandForm',
    forceFit: true,
    layout: 'form',
    bodyPadding: '5 5 0',
    border: '0 0 1 0',
    requires: ['Ext.form.field.Text'],
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 90
    },
    height: 'auto',
    defaultType: 'fieldcontainer',
    initComponent: function () {

        Ext.tip.QuickTipManager.init();

        var brandListStore = Espide.Common.createComboStore('/brand/list', false);

        //动态生成店铺checkbox
        ///order/shopList
        var shopArray = [];
        Ext.Ajax.request({
            url: "np/findOnlineShop",
            async:false,
            success: function (response, options) {
               var  shopList = Ext.JSON.decode(response.responseText).data.list;
                Ext.each(shopList, function (shopList) {
                    shopArray.push({
                        boxLabel: shopList.platformType['value']+':'+shopList.nick,
                        name: 'shopIds',
                        inputValue: shopList.id,
                        checked: false,
                        width:210,
                        id: "check" + shopList.id,
                    });
                })
            },
            failure: function (form, action) {
                var data = Ext.JSON.decode(action.response.responseText);
                Ext.MessageBox.show({
                    title: '提示',
                    msg: data.msg,
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-warning'
                });

            }
        });

        this.items = [
            {
                layout: 'anchor',
                defaults: {
                    anchor: '100%'
                },
                items: [
                    {
                        xtype: 'textfield',
                        name: 'id',
                        itemId: 'id',
                        hidden: true
                    },
                    {
                        xtype: 'textfield',
                        name: 'action',
                        itemId: 'action',
                        hidden: true
                    },
                    {
                        xtype: 'textfield',
                        name: 'type',
                        itemId: 'type',
                        value:'BRAND',
                        hidden: true
                    },
                    {
                        xtype: 'textfield',
                        name: 'proId',
                        itemId: 'giftProdId',
                        hidden: true
                    },
                    {
                        xtype: 'textfield',
                        name: 'amout',
                        itemId: 'giftProdCount',
                        hidden: true
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'combo',
                        items: [
                            {
                                flex: 5,
                                name: 'brandId',
                                itemId: 'brandId',
                                fieldLabel: '品牌',
                                editable: false,
                                queryMode: 'local',
                                valueField: 'id',
                                displayField: 'name',
                                emptyText: '请选择',
                                allowBlank: false,
                                margin:'0 8 0 0',
                                store: brandListStore
                            },
                            {
                                flex: 5,
                                xtype: 'textfield',
                                fieldLabel: '活动名称',
                                name: 'remark',
                                emptyText: '活动名称',
                                allowBlank: false,
                            }
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'textfield',
                        itemId: 'c_money',
                        items: [
                            {
                                xtype: 'textfield',
                                fieldLabel: '满足条件',
                                name: 'actualFeeBegin',
                                itemId: 'priceBegin',
                                allowBlank: false,
                                emptyText: '开始金额',
                                maxLength: 10,
                                flex: 5
                            },
                            {
                                xtype: 'container',
                                flex: 3,
                                html: '<span class="x-form-item-label" style="text-align:center">&lt;=订单总额&lt;=</span>'
                            },
                            {
                                xtype: 'textfield',
                                name: 'actualFeeEnd',
                                itemId: 'priceEnd',
                                allowBlank: false,
                                //vtype: 'Number',
                                emptyText: '结束金额',
                                maxLength: 10,
                                flex: 4,
                                validator: function (v) {
                                    var priceBeginVal = this.up('form').down('[itemId=priceBegin]').getValue();
                                    if (Number(v) > Number(priceBeginVal)) {
                                        return true;
                                    }
                                    return '结束金额必须大于开始金额';
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'radiofield',
                        fieldLabel: '是否启用',
                        msgTarget: 'top',
                        defaults: {
                            flex: 1
                        },
                        items: [
                            {
                                boxLabel: '是',
                                name: 'inUse',
                                inputValue: 1,
                                itemId: 'inUseYes',
                                checked: true
                            },
                            {
                                boxLabel: '否',
                                name: 'inUse',
                                itemId: 'inUseNo',
                                inputValue: 0
                            }
                        ]
                    },
                    {
                        xtype: 'checkboxgroup',
                        fieldLabel: '选择店铺',
                        allowBlank:false,
                        blankText:'至少选择一个店铺',
                        columns: 3,
                        vertical: true,
                        items:shopArray
                    }
                ]
            }
        ];

        this.callParent(arguments);
    }
});
