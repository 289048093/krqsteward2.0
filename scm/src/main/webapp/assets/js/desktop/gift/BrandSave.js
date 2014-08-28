/**
 * 品牌优惠活动 - 保存
 * Created by HuaLei.Du on 13-12-30.
 */
Ext.define('EBDesktop.gift.BrandSave', {
    extend: 'Ext.window.Window',
    alias: 'widget.giftBrandSave',
    title: '添加品牌活动',
    id: 'giftBrandSave',
    modal: true,
    closeAction: 'destroy',
    autoShow: false,
    constrain: true,
    //layout: 'fit',
    width: 740,
    height: 598,
    initComponent: function () {

        Ext.tip.QuickTipManager.init();

        // 保存表单
        function saveForm(btn) {
            var form = Ext.getCmp('giftBrandForm'),
                brandGoodsCartStore = Ext.getCmp('giftBrandGoodsCartGrid').getStore(),
                giftProdId = [],
                giftProdCount = [],
                options = {
                    url: form.down('[itemId=action]').getValue()
                };
            var data = form.getValues();




            if (brandGoodsCartStore.data.items.length > 0) {
                brandGoodsCartStore.each(function (records, index) {
                    giftProdId.push(records.get('prodId'));
                    giftProdCount.push(records.get('amount'));
                });
            }

            if (giftProdId.length < 1) {
                Ext.Msg.alert('提示', '你还没有添加赠品哦~');
                return;
            }

            form.down('[itemId=giftProdId]').setValue(giftProdId);
            form.down('[itemId=giftProdCount]').setValue(giftProdCount);

            data['proId'] = giftProdId.join(',');
            data['amout'] = giftProdCount.join(',');
            Ext.isArray(data['shopIds']) ? data['shopIds'] = data['shopIds'].join(',') : data['shopIds'];


            if(form.isValid()){//验证表单
                Ext.Ajax.request({
                    url: options.url,
                    submitEmptyText: false,
                    waitMsg: '保存中...',
                    url: options.url,
                    params: data,
                    success:function(response){
                        var data = Ext.decode(response.responseText);
                        if (data.success) {
                            Ext.getCmp('giftBrandSave').close();
                            Espide.Common.reLoadGird('giftBrandActivityGrid', false, true);
                        } else {
                            options.failureCall();
                            Ext.Msg.show({
                                title: '错误',
                                msg: data.msg,
                                buttons: Ext.Msg.YES,
                                icon: Ext.Msg.WARNING
                            });
                        }
                    },
                    failure:function(form,action){
                        var data = Ext.JSON.decode(form.responseText);
                        Ext.MessageBox.show({
                            title: '提示',
                            msg:data.msg,
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-warning'
                        });
                    }
                });
            }

        }

        this.items = [
            {
                region: 'north',
                xtype: 'giftBrandForm'
            },
            {
                region: 'center',
                height: 204,
                xtype: 'giftBrandGoods'
            },
            {
                region: 'south',
                height: 180,
                xtype: 'giftBrandGoodsCart'
            }
        ];

        this.buttons = {
            layout: {
                pack: 'center'
            },
            items: [
                {
                    text: '保存',
                    handler: saveForm
                },
                {
                    text: '取消',
                    handler: function () {
                        Ext.getCmp('giftBrandSave').close();
                    }
                }
            ]
        };

        this.callParent(arguments);
    }
});