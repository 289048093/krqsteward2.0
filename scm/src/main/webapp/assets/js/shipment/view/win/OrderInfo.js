/*
* Created by king on 13-12-17
*/

Ext.define('Supplier.view.win.OrderInfo', {
    extend: 'Ext.form.Panel',
    id: 'orderInfo',
    region: 'north',
    alias: 'widget.orderInfo',
    bodyPadding: 10,
    layout:'anchor',
    height: 'auto',
    defaults: {
        margin: '0 0 10 0',
        layout: 'hbox',
        border: 0,
        defaults: {
            xtype: 'textfield',
            margin: '0 10 0 0',
            labelWidth: 60,
            width: 160,
            queryMode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            editable: false
        }
    },
    initComponent: function (){
        this.items = [
            {
                items: [
                    {
                        name: 'buyerId',
                        fieldLabel: '买家Id'
                    },
                    {
                        name: 'receiverPhone',
                        fieldLabel: '收货电话'
                    },
                    {
                        name: 'receiverMobile',
                        fieldLabel: '收货手机'
                    },
                    {
                        name: 'orderType',
                        fieldLabel:'订单类型',
                        xtype: 'combo',
                        value: 'normalOrder',
                        store: [
                            ['normalOrder','正常订单'],
                            ['returnOrder','退货订单'],
                            ['exchangeOrder','换货订单'],
                            ['addOrder','补发订单'],
                            ['invoiceOrder','需开票订单'],
                            ['internalOrder','刷单订单']
                        ]
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'receiverState',
                        fieldLabel:'收货省',
                        width: 140,
                        xtype: 'combo',
                        value: 'normalOrder',
                        store: [
                            ['normalOrder','正常订单'],
                            ['returnOrder','退货订单'],
                            ['exchangeOrder','换货订单'],
                            ['addOrder','补发订单'],
                            ['invoiceOrder','需开票订单'],
                            ['internalOrder','刷单订单']
                        ]
                    },
                    {
                        name: 'receiverCity',
                        fieldLabel:'收货市',
                        width: 140,
                        xtype: 'combo',
                        value: 'normalOrder',
                        store: [
                            ['normalOrder','正常订单'],
                            ['returnOrder','退货订单'],
                            ['exchangeOrder','换货订单'],
                            ['addOrder','补发订单'],
                            ['invoiceOrder','需开票订单'],
                            ['internalOrder','刷单订单']
                        ]
                    },
                    {
                        name: 'receiverDistrict',
                        fieldLabel:'收货区',
                        width: 140,
                        xtype: 'combo',
                        value: 'normalOrder',
                        store: [
                            ['normalOrder','正常订单'],
                            ['returnOrder','退货订单'],
                            ['exchangeOrder','换货订单'],
                            ['addOrder','补发订单'],
                            ['invoiceOrder','需开票订单'],
                            ['internalOrder','刷单订单']
                        ]
                    },
                    {
                        name: 'receiverAddress',
                        fieldLabel:'收货地址',
                        width: 220
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'receiverName',
                        width: 140,
                        fieldLabel:'收货人'
                    },
                    {
                        name: 'receiverZip',
                        width: 140,
                        fieldLabel:'邮政编码'
                    },
                    {
                        fieldLabel: '快递',
                        labelWidth: 40,
                        width:140,
                        name: 'shippingComp',
                        xtype: 'combo',
                        value: 'default',
                        store: [
                            ['default', '请选择'],
                            ['shunfeng', '顺丰'],
                            ['yunda', '韵达'],
                            ['zhaijisong', '宅急送'],
                            ['ems', 'EMS'],
                            ['yuantong', '圆通'],
                            ['shentong', '申通'],
                            ['zhongtong', '中通'],
                            ['huitong', '汇通'],
                            ['quanritongkuaidi', '全日通'],
                            ['kuaijiesudi', '快捷'],
                            ['guotongkuaidi', '国通'],
                            ['lianbangkuaidi', '联邦'],
                            ['quanfengkuaidi', '全峰'],
                            ['suer', '速尔'],
                            ['tiantian', '天天'],
                            ['youshuwuliu', '优速'],
                            ['unknown', '未知']
                        ]
                    },
                    {
                        fieldLabel: '店铺',
                        labelWidth: 40,
                        width: 220,
                        xtype: 'combo',
                        value: 'ejs',
                        name: 'shopName',
                        store: [
                            ['ejs', '易居尚官方旗舰店']//暂只用这一个店,
//                                    ['all', 'E居尚官方旗舰店（京东店）'],
//                                    ['all', '易居尚天猫C店'],
//                                    ['all', 'TOMMASI官方旗舰店'],
//                                    ['all', '尚尼京东旗舰店'],
//                                    ['all', '域外居家日用专营店'],
//                                    ['all', '赛拉菲诺尚尼官方旗舰店'],
//                                    ['all', '中国光大银行'],
//                                    ['all', '名士商城渠道'],
//                                    ['all', '走秀网'],
//                                    ['all', 'TOMMASI官方旗舰店(京东)'],
//                                    ['all', '京东易居尚旗舰店']
                        ]
                    }
                ]
            },
            {
                items: [
                    {
                        name: 'totalFee',
                        width: 110,
                        fieldLabel: '成交金额'
                    },
                    {
                        name: 'buyerMessage',
                        width: 270,
                        fieldLabel: '买家留言'
                    },
                    {
                        name: 'remark',
                        width: 270,
                        fieldLabel: '备注说明'
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
})