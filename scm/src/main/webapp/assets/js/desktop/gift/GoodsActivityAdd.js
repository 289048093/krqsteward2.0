/**
 * 商品优惠活动 - 添加
 * Created by HuaLei.Du on 13-12-30.
 */
Ext.define('EBDesktop.gift.GoodsActivityAdd', {
    extend: 'Ext.window.Window',
    alias: 'widget.giftGoodsActivityAdd',
    title: '添加商品活动',
    id: 'giftGoodsActivityAdd',
    modal: true,
    closeAction: 'destroy',
    autoShow: false,
    constrain: true,
    layout: 'border',
    width: 740,
    height: 600,
    initComponent: function () {
        this.items = [
            {
                region: 'north',
                xtype: 'giftGoodsList'
            },
            {
                region: 'south',
                xtype: 'GoodsCart'
            }
        ];

        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {
                    text: '保存',
                    handler: function (btn) {
                        var ids = Espide.Common.getGridSels('giftGoodsListGrid', 'id');
                        var GoodsCartStore = Ext.getCmp('giftGoodsCartGrid').getStore(),
                            giftProdId = [],
                            params = {},
                            form = Ext.getCmp('search').getForm(),
                            giftProdCount = [];


                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('最少需要选择一个商品');
                            return;
                        }

                        //赠品
                        if (GoodsCartStore.data.items.length > 0) {
                            GoodsCartStore.each(function (records, index) {
                                giftProdId.push(records.get('prodId'));
                                giftProdCount.push(records.get('amount'));
                            });
                        }
                        //判断赠品数量
                        if (giftProdId.length < 1) {
                            Ext.Msg.alert('提示', '你还没有添加赠品哦~');
                            return;
                        }

                        if (form.getValues()['shopIds'] == undefined) {
                            Ext.Msg.alert('提示', '必须选择店铺');
                            return;
                        }

                        if (form.isValid()) {

                            params = form.getValues();
                            Ext.isArray(params['shopIds']) ? params['shopIds'] = params['shopIds'].join(',') : params['shopIds'];
                            params['productId'] = ids.join('');
                            params['proId'] = giftProdId.join(',');
                            params['amout'] = giftProdCount.join(',');
                            params['type'] = 'PRODUCT';


                            Ext.Ajax.request({
                                params: params,
                                url: "activity/save",
                                success: function (response, options) {
                                    var data = Ext.JSON.decode(response.responseText);
                                    if (data.success) {
                                        Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('giftGoodsActivityAdd'), 'giftGoodsActivityGrid', true);
                                    }

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

                        }

                    }
                },
                {
                    text: '取消',
                    handler: function () {
                        Ext.getCmp('giftGoodsActivityAdd').close();
                    }
                }

            ]
        };


//        this.buttons = [
//            {
//                text: '保存',
//                itemId: 'saveBtn',
//                handler: function (btn) {
//                    var ids = Espide.Common.getGridSels('giftGoodsListGrid', 'id');
//
//                    if (ids.length < 1) {
//                        Espide.Common.showGridSelErr('最少需要选择一个商品');
//                        return;
//                    }
//
//                    this.up('form').down('[itemId=sellProdIds]').setValue(ids.join());
//                    saveForm(btn);
//                }
//            }
//        ];
        this.callParent(arguments);
    }
});