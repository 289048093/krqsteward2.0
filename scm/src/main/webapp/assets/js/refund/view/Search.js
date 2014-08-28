/**
 * author     : 梦龙
 * createTime : 14-5-8 下午1:21
 * description:
 */
/*
 * Created by king on 13-12-17
 */

Ext.define('Refund.view.Search', {
    extend: 'Ext.form.Panel',
    id: 'search',
    alias: 'widget.refundSearch',
    region: 'north',
    border: 0,
    bodyPadding: 10,
    layout: {
        type: 'hbox',
        align: 'left'
    },
    height: 'auto',
    defaultType: 'fieldcontainer',
    defaults: {
        margin: '0 0 10 0',
        defaults: {
            xtype: 'combo',
            margin: '0 10 5 0',
            labelWidth: 60,
            width: 150,
            queryMode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            editable: false
        }
    },
    initComponent: function () {


        var platform = Espide.Common.createComboStore('/np/platform/list',true,['id','name','type']);



        this.items = [
            {
                items: [
                    {
                        name: 'dateType',
                        fieldLabel: '日期类型',
                        width: 220,
                        itemId: 'dateType',
                        id: 'dateType',
                        value: 'refundDate',
                        store: [
                            ['all', '全部'],
                            ['refundDate', '退款日期'],
                            ['createDate', '创建日期']
                        ],

                    },
                    {
                        name: 'type',
                        fieldLabel: '退款类型',
                        width: 220,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store:Espide.Common.refundType.getStore(true)

                    },
                    {
                        name: 'platformType',
                        labelWidth: 60,
                        width: 220,
                        fieldLabel: '平台类型',
                        queryMode: 'local',
                        triggerAction: 'all',
                        valueField: 'type',
                        displayField: 'name',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store: platform,
                    },
                ]
            },
            {
                items: [
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '开始日期',
                        value: new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 7),
                        name: 'startDate',
                        itemId: 'startDate',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        labelWidth: 60,
                        width: 220
                    }),

                    {
                        name: 'online',
                        labelWidth:90,
                        width:220,
                        fieldLabel: '是否线上退款',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store: [
                            ['null', '全部'],
                            [true, '线上'],
                            [false, '线下']
                        ]
                    },
                    {
                        fieldLabel: '店铺名称',
                        emptyText: '请选择',
                        valueField: 'id',
                        displayField: 'nick',
                        labelWidth: 60,
                        width: 220,
                        name: 'shopId',
                        store: 'ShopAll'
                    },
                ]
            },{
                items:[
                    Ext.create('Go.form.field.DateTime', {
                        fieldLabel: '结束日期',
                        //value: new Date(),
                        name: 'endDate',
                        itemId: 'endDate',
                        format: 'Y-m-d H:i:s',
                        margin: '0 10 5 0',
                        labelWidth: 60,
                        width: 220
                    }),
                    {
                        name: 'phase',
                        width:220,
                        fieldLabel: '退款阶段',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store: [
                            ['null', '全部'],
                            ['ON_SALE', '售前'],
                            ['AFTER_SALE', '售后']
                        ]
                    },
                    {

                        name: 'brandId',
                        fieldLabel: '品牌',
                        value: 30,
                        labelWidth: 60,
                        emptyText: '请选择',
                        valueField: 'id',
                        displayField: 'name',
                        width: 220,
                        store: 'Brand'

                    },
                ]
            },{
                items:[
                    {
                        name: 'status',
                        fieldLabel: '退款状态',
                        width:220,
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store: [
                            ['null', '全部'],
                            ['IN_PROCESS', '正在申请'],
                            ['SUCCESS', '退款成功'],
                            ['CLOSED', '退款失败']
                        ]
                    },
                    {
                        name: 'alsoReturn',
                        labelWidth:90,
                        width:220,
                        fieldLabel: '是否同时退货',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store: [
                            ['null', '全部'],
                            [true, '是'],
                            [false, '否']
                        ]
                    },
                    {
                        name: 'postPayer',
                        labelWidth:100,
                        width:220,
                        fieldLabel: '退货邮费承担方',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        emptyText: '请选择',
                        store: [
                            ['null', '全部'],
                            ['BUYER', '顾客'],
                            ['SELLER', '商家'],
                            ['SUPPLIER', '供应商'],
                        ]
                    },
                ]
            },
            {
              items:[
                  {
                      xtype: 'fieldcontainer',
                      layout: 'hbox',
                      combineErrors: true,
                      msgTarget: 'side',
                      fieldLabel: '条件1',
                      labelWidth: 40,
                      width: 330,
                      defaults: {
                          xtype: 'combo',
                          queryMode: 'local',
                          triggerAction: 'all',
                          forceSelection: true,
                          editable: false,
                          hideLabel: true,
                          margin: '0 5 0 0'
                      },
                      items: [
                          {
                              width: 120,
                              name: 'conditionQuery1',
                              itemId: 'querySelect',
                              value: 'buyerId',
                              valueField: 'id',
                              displayField: 'value',
                              store: Ext.create('Ext.data.Store', {
                                  fields: ['id', 'value', 'type'],
                                  data: [
                                      {id: 'platformOrderNo', value: '外部平台订单编号', type: 'string'},
                                      {id: 'orderNo', value: '智库城订单编号', type: 'string'},
                                      {id: 'buyerAlipayNo', value: '支付宝账号', type: 'string'},
                                      {id: 'shippingNo', value: '快递单号', type: 'string'},
                                      {id: 'platformRefundNo', value: '外部平台退款单号', type: 'string'},
                                      {id: 'productNo', value: '商品编号', type: 'string'},
                                      {id: 'productName', value: '商品名称', type: 'string'},
                                      {id: 'sku', value: 'sku', type: 'string'},
                                      {id: 'buyerId', value: '退款人ID', type: 'string'},
                                  ]
                              })
                          },
                          {
                              name: 'conditionType1',
                              value: 'has',
                              itemId: 'queryType',
                              valueField: 'id',
                              displayField: 'value',
                              width: 70,
                              store: Ext.create('Ext.data.Store', {
                                  fields: ['id', 'value', 'type'],
                                  data: [
                                      {id: 'has', value: '包含', type: 'string'},
                                      {id: '!', value: '不包含', type: 'string'},
                                      {id: '=', value: '等于', type: 'all'},
                                      {id: '!=', value: '不等于', type: 'number'},
                                      {id: '>=', value: '大于等于', type: 'number'},
                                      {id: '<=', value: '小于等于', type: 'number'}

                                  ]
                              })
                          },
                          {xtype: 'textfield', name: 'conditionValue1', width: 80, margin: '0 10 0 0'}
                      ]
                  },
                  {
                      xtype: 'fieldcontainer',
                      layout: 'hbox',
                      combineErrors: true,
                      msgTarget: 'side',
                      fieldLabel: '条件2',
                      labelWidth: 40,
                      width: 330,
                      defaults: {
                          xtype: 'combo',
                          queryMode: 'local',
                          triggerAction: 'all',
                          forceSelection: true,
                          editable: false,
                          hideLabel: true,
                          margin: '0 5 0 0'
                      },
                      items: [
                          {
                              width: 120,
                              name: 'conditionQuery2',
                              itemId: 'querySelect',
                              value: 'buyerId',
                              valueField: 'id',
                              displayField: 'value',
                              store: Ext.create('Ext.data.Store', {
                                  fields: ['id', 'value', 'type'],
                                  data: [
                                      {id: 'platformOrderNo', value: '外部平台订单编号', type: 'string'},
                                      {id: 'orderNo', value: '智库城订单编号', type: 'string'},
                                      {id: 'buyerAlipayNo', value: '支付宝账号', type: 'string'},
                                      {id: 'shippingNo', value: '快递单号', type: 'string'},
                                      {id: 'platformRefundNo', value: '外部平台退款单号', type: 'string'},
                                      {id: 'productNo', value: '商品编号', type: 'string'},
                                      {id: 'productName', value: '商品名称', type: 'string'},
                                      {id: 'sku', value: 'sku', type: 'string'},
                                      {id: 'buyerId', value: '退款人ID', type: 'string'},
                                  ]
                              })
                          },
                          {
                              name: 'conditionType2',
                              value: 'has',
                              itemId: 'queryType',
                              valueField: 'id',
                              displayField: 'value',
                              width: 70,
                              store: Ext.create('Ext.data.Store', {
                                  fields: ['id', 'value', 'type'],
                                  data: [
                                      {id: 'has', value: '包含', type: 'string'},
                                      {id: '!', value: '不包含', type: 'string'},
                                      {id: '=', value: '等于', type: 'all'},
                                      {id: '!=', value: '不等于', type: 'number'},
                                      {id: '>=', value: '大于等于', type: 'number'},
                                      {id: '<=', value: '小于等于', type: 'number'}

                                  ]
                              })
                          },
                          {xtype: 'textfield', name: 'conditionValue2', width: 80, margin: '0 10 0 0'}
                      ]
                  },
              ]
            },
            {
                xtype: 'button',
                text: '查询',
                itemId: 'confirmBtn',
                width: 55,
                height: 35
            },
            {
                xtype: 'button',
                text: '重置',
                itemId: 'resetBtn',
                margin: '0 0 0 8',
                width: 55,
                height: 35,
                handler: function (btn) {
                    Ext.getCmp('search').getForm().reset();

                }
            },

        ];

        this.callParent(arguments);
    }
});