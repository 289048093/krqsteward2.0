/**
 * 商品优惠活动 - 修改
 * Created by HuaLei.Du on 14-1-7.
 */
Ext.define('EBDesktop.gift.GoodsActivityEdit', {
    extend: 'Ext.window.Window',
    alias: 'widget.giftGoodsActivityEdit',
    title: '修改商品活动',
    id: 'giftGoodsActivityEdit',
    modal: true,
    closeAction: 'destroy',
    autoShow: false,
    constrain: true,
    layout: 'border',
    width: 740,
    height: 480,
    initComponent: function () {

        Ext.tip.QuickTipManager.init();
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
                            params ={},
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

                        if(form.isValid()){
                            params = form.getValues();
                            Ext.isArray(params['shopIds']) ? params['shopIds'] = params['shopIds'].join(',') : params['shopIds'];
                            params['productId'] = ids.join('');
                            params['proId'] = giftProdId.join(',');
                            params['amout'] = giftProdCount.join(',');
                            params['type'] = 'PRODUCT';



                            Ext.Ajax.request({
                                params: params,
                                url: "activity/update",
                                success: function (response, options) {
                                    var data = Ext.JSON.decode(response.responseText);
                                    if (data.success) {
                                        Espide.Common.tipMsgIsCloseWindow(data, Ext.getCmp('giftGoodsActivityEdit'), 'giftGoodsActivityGrid', true);
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
                        Ext.getCmp('giftGoodsActivityEdit').close();
                    }
                }

            ]
        };
        this.callParent(arguments);
    }
});