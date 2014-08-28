/*
 * Created by king on 13-12-17
 */

Ext.define('ShopProduct.view.showDetails.showProductInfo', {
    extend: 'Ext.form.Panel',
    id: 'showProductInfo',
    region: 'north',
    alias: 'widget.showProductInfo',
    bodyPadding: 10,
    layout: {
        type: 'hbox',
        align: 'left'
    },
    split: 'true',
    height: 'auto',
    defaultType: 'fieldcontainer',
    defaults: {
        defaults: {
            xtype: 'textfield',
            margin: '0 0 5 0',
            labelWidth: 70,
            labelAlign: 'right',
            width: 280,
            queryMode: 'local'
        }

    },

    initComponent: function () {
        this.items = [
            {
                items: [
                    {
                        name: 'id',
                        hidden: true,

                    },
                    {
                        name: 'picUrl',
                        fieldLabel: '产品图片',
                        hidden: true,

                    },
                    {
                        name: 'name',
                        fieldLabel: '产品名称',
                        allowBlank:false,
                        maxLength: 32,

                    },
                    {   name: 'brandName',
                        id: 'brandName',
                        fieldLabel: '产品品牌',
                    },
                    {
                        name: 'productNo',
                        fieldLabel: '商品编号',
                        allowBlank:false,
                        maxLength: 32,

                    },
                    {
                        name: 'sku',
                        fieldLabel: '产品sku',
                        maxLength: 32,
                    },
                    {
                            labelWidth: 70,
                            width: 280,
                            name: 'prodCategoryName',
                            id:'prodCategoryName',
                            fieldLabel: '产品分类',
                    },
                    {
                        name: 'description',
                        fieldLabel: '产品描述',
                        allowBlank: true,
                        emptyText: '请输入产品描述(可为空)',
                        xtype: 'textareafield',
                        maxLength: 512,
                        height: 26,

                    },
                    {
                        name: 'marketPrice',
                        id: "shopPriceStr",
                        xtype: 'numberfield',
                        editable: true,
                        fieldLabel: '市场价(元)',
                        minValue: 0,
                        negativeText: '不得输入负数',
                        allowBlank:false,

                    },
                    {
                        name: 'minimumPrice',
                        id: "minimumPrice",
                        xtype: 'numberfield',
                        editable: true,
                        fieldLabel: '最低价(元)',
                        minValue: 0,
                        negativeText: '不得输入负数',
                        allowBlank:false,

                    },
                    {
                        name: 'color',
                        fieldLabel: '颜色',
                        allowBlank: true,
                        emptyText: '请输入产品颜色(可为空)',
                        maxLength: 5,

//                                regex: /[\u4e00-\u9fa5]+/,
//                                regexText: '颜色必须是中文'
                    },

                ]
            },
            {
                items: [

                    {
                        name: 'weight',
                        fieldLabel: '重量(kg)',
                        allowBlank: true,
                        emptyText: '请输入产品重量(可为空)',
                        maxLength: 20,

                    },
                    {
                        name: 'boxSize',
                        fieldLabel: '尺寸(厘米)',
                        allowBlank: true,
                        emptyText: '请输入产品尺寸(可为空)',
                        maxLength: 20,
                    },
                    {
                        name: 'speci',
                        fieldLabel: '规格',
                        allowBlank: true,
                        emptyText: '请输入产品规格(可为空)',
                    },
                    {
                        name: 'orgin',
                        fieldLabel: '产地',
                        allowBlank:false,
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: '产品类型',
                        id: 'style',
                        allowBlank:false,
                        name: 'style',


                    },
                    {
                        xtype: 'combo',
                        id: 'location',
                        name: 'location',
                        fieldLabel: '库位',
                        allowBlank:false,

                    },
                    ,
                    {
                        name: 'repositoryNum',
                        fieldLabel: '总库存',
                        xtype: 'numberfield',
                        allowBlank:false,
                        editable: true,
                        minValue: 0,
                        allowDecimals:false,
                        negativeText: '不得输入负数'
                    },
                    {
                        name: 'repositoryName',
                        id: 'repositoryName',
                        fieldLabel: '仓库',
                    },
                    {
                        name: 'outerProductNo',
                        fieldLabel: '外部平台商品编号',
                        allowBlank:true,
                        labelWidth:110,
                        maxLength:32
                    },
                ]
            }
        ];
        this.callParent(arguments);
    }
});

