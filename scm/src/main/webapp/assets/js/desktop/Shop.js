/**
 * Created by king on 13-12-19.
 */
Ext.define('EBDesktop.Shop', {
    extend: 'Ext.ux.desktop.Module',

    id: 'shop-win',

    init: function () {
        this.launcher = {
            text: '店铺管理',
            iconCls: 'icon-grid'
        };
    },

    createWindow: function () {
        var desktop = this.app.getDesktop(),
            root = this,
            win = desktop.getWindow('shop-win'),
            shopStore = root.self.getShopStore();

        if (!win) {
            win = desktop.createWindow({
                title: '店铺管理',
                id: 'shop-win',
                collapsible: true,
                maximizable: true,
                modal: false,
                layout: 'border',
                width: 900,
                height: 602,
                items: [
                    root.self.createShopGrid(shopStore),
                    root.self.createShopItem()
                ]
            })
        }
        return win;
    },

    statics: {

        //生成店铺管理表格
        createShopGrid: function (store) {
            return Ext.create('Ext.grid.Panel', {
                region: 'west',
                id: 'ShopList',
                store: store,
                forceFit: true,
                width: 500,
                split: true,
                selType: 'checkboxmodel',
                tbar: {
                    items: [

                        Ext.create('Ext.form.Panel', {
                            layout: 'hbox',
                            border: false,
                            itemId: 'search',
                            id: 'search',
                            defaults:{
                                margin:'0 5 0 0'
                            },
                            items: [
                                {xtype: 'textfield', name: 'nick', width: 150, emptyText: '请输入店铺名称', itemId: 'searchText'},
                                {xtype: 'button', name: 'submuit', text: '查询', handler: function (btn) {

                                    var searchText = btn.up('grid').down('#searchText').getValue();
                                    btn.up('grid').getStore().reload({
                                        params: {
                                            nick: searchText
                                        }
                                    });

                                }},
                                {xtype: 'button',  name: 'removeShop', text: '删除', handler: function (btn) {
                                    var com = Espide.Common,
                                        sels = com.getGridSels(btn.up('grid'), 'id');

                                    if (!com.checkGridSel(btn.up('grid'), '请至少选择一项订单'))  return;

                                    com.commonMsg({
                                        title: '操作确认?',
                                        msg: '你确定要删除这个店铺吗，处理后不可复原?',
                                        fn: com.doAction({
                                            url: 'shop/delete',
                                            params: {
                                                id: sels
                                            },
                                            successCall: function () {
                                                com.reLoadGird(btn.up('grid'), false, true);
                                            }
                                        })
                                    });
                                }},

                            ]
                        })

                    ]
                },
                bbar: Ext.create('Ext.PagingToolbar', {
                    store: store,
                    displayInfo: true,
                    displayMsg: '当前页 {0} - {1} of {2}',
                    emptyMsg: "没有页了"
                }),
                columns: [
                    {
                        text: 'ID', dataIndex: 'id', width: 50
                    },
                    {
                        text: '店铺名称', dataIndex: 'nick', width: 220
                    },
                    {
                        text: '开店时间', dataIndex: 'createTime', width: 100, renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        text: '修改时间', dataIndex: 'updateTime', width: 100, renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    }
                ],
                listeners: {
                    selectionchange: EBDesktop.Shop.showShopItem,
                    render: function (input) {
                        var map = new Ext.util.KeyMap({
                            target: 'search',    //target可以是组建的id  加单引号
                            binding: [
                                {                       //绑定键盘事件
                                    key: Ext.EventObject.ENTER,
                                    fn: function () {
                                        Espide.Common.reLoadGird('ShopList', 'search', true);
                                    }
                                }
                            ]
                        });
                    }
                }
            });
        },

        //生成店铺详情
        createShopItem: function () {
            return Ext.create('Ext.form.Panel', {
                region: 'center',
                id: 'shopItem',
                height: 'auto',
                bodyPadding: 10,
                layout: 'anchor',
                border: 0,
                defaults: {
                    xtype: 'textfield',
                    margin: '0 0 5 0',
                    labelWidth: 100,
                    labelAlign: 'right',
                    width: 350,
                    queryMode: 'local',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false
                },
                items: [
                    { name: 'id', fieldLabel: 'id', hidden: true, itemId: 'id' },
                    { name: 'shopId', fieldLabel: '店铺id', hidden: true},
                    { name: 'title', fieldLabel: '店铺名称', minLength: 5, maxLength: 30},
                    { name: 'description', fieldLabel: '店铺描述', xtype: 'textareafield', height: 50, minLength: 10, maxLength: 2000 },
                    { name: 'bulletin', fieldLabel: '店铺公告', xtype: 'textareafield', height: 50, maxLength: 1024, minLength: 10,
                        regex:/(?:^\S+.*\S+$)|(?:^\S{1}$)/, regexText: '首尾不能有空格'
                    },
                    {   name: 'deExpress',
                        xtype: 'combo',
                        queryMode: 'local',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        fieldLabel: '默认快递',
                        store: Espide.Common.expressStore()
                    },

                    { name: 'nick', fieldLabel: '卖家昵称', readOnly: true },
                    { name: 'picPath', fieldLabel: '店标地址', readOnly: true },
                    { name: 'itemScore', fieldLabel: '商品描述评分', readOnly: true },
                    { name: 'serviceScore', fieldLabel: '服务态度评分', readOnly: true},
                    { name: 'deliveryScore', fieldLabel: '发货速度评分', readOnly: true },
//                    {
//                        xtype: 'sliderfield',
//                        fieldLabel: '商品描述评分',
//                        name: 'itemScore',
//                        useTips: false,
//                        value:0,
//                        increment: 5,
//                        minValue: 0,
//                        maxValue: 100,
//                        columnWidth: 50,
//                        readOnly: true,
//                        shrinkWrap: 3
//                    },
//                    {
//                        xtype: 'sliderfield',
//                        fieldLabel: '服务态度评分',
//                        name: 'serviceScore',
//                        useTips: false,
//                        value:0,
//                        increment: 5,
//                        minValue: 0,
//                        maxValue: 100,
//                        readOnly: true
//                    },
//                    {
//                        xtype: 'sliderfield',
//                        fieldLabel: '发货速度评分',
//                        name: 'deliveryScore',
//                        value:0,
//                        increment: 5,
//                        minValue: 0,
//                        maxValue: 100,
//                        readOnly: true,
//                        shrinkWrap: 2,
//                        useTips: true
//                    },
                    {   fieldLabel: '发货短信',
                        xtype: 'radiogroup',
                        itemId: 'enableMsg',
                        items: [
                            {boxLabel: '启用', name: 'enableMsg', inputValue: true},
                            {boxLabel: '禁用', name: 'enableMsg', inputValue: false, checked: true}
                        ],
                        listeners: {
                            change: function (btn, newValue) {
                                changeMsgState(!newValue.enableMsg);
                                function changeMsgState(newValue) {
                                    btn.up('form').down('#msgTemp').setReadOnly(newValue);
                                    btn.up('form').down('#msgSign').setReadOnly(newValue);
                                }
                            }
                        }
                    },
                    { name: 'msgTemp', fieldLabel: '短信模板', xtype: 'textareafield', height: 50, itemId: 'msgTemp', readOnly: true, minLength: 4, maxLength: 100 },
                    { name: 'msgSign', fieldLabel: '短信签名', itemId: 'msgSign', readOnly: true, minLength: 4, maxLength: 100 },
                    { name: 'outPlatformTypeValue', fieldLabel: '店铺平台', readOnly: true,
                        editable: false,
                        value: '平台名称',
                    },
                    { name: 'sessionKey', fieldLabel: 'session key', readOnly: true },
                ],
                buttons: {
                    layout: {
                        pack: 'center'
                    },
                    items: [
                        {text: '更新店铺评分', handler: function (btn) {
                            var com = Espide.Common,
                                form = btn.up('form');

                            if (form.down("#id").getValue().trim() !== ''){
                                com.doAction({
                                    url: '/shop/dynamicGetScore',
                                    params: {
                                        id: form.down('#id').getValue()
                                    },
                                    successCall: function () {
                                        com.reLoadGird('ShopList', false, false);
                                    },
                                    successTipMsg: '获取成功'
                                })('yes');
                            }

                        }},
                        {text: '确定', handler: function (btn) {
                            var com = Espide.Common,
                                form = btn.up('form'),
                                shopItem = Ext.getCmp('shopItem');

                            if (form.down("#id").getValue().trim() !== ''){
                                if (com.getGridSels('ShopList').length>1){
                                    Ext.MessageBox.show({
                                        title: '警告',
                                        msg: '更新信息时，只能选择一个店铺',
                                        buttons: Ext.MessageBox.OK,
                                        icon: 'x-message-box-error'
                                    });
                                    return;
                                }

                                com.doFormCheck(form, function (){
                                    shopItem.setLoading(true);
                                    com.doAction({
                                        url: '/shop/update',
                                        params: form.getForm().getValues(),
                                        commonCall: function () {
                                            com.reLoadGird('ShopList', false, false);
                                            //form.getForm().reset();
                                            shopItem.setLoading(false);
                                        },
                                        successTipMsg: '店铺修改成功'
                                    })('yes');
                                }, '请输入正确的店铺信息!');

                            }

                        }},
                        {text: '取消', handler: function (btn) {
                            btn.up('form').getForm().reset();
                        },hidden: true}
                    ]
                }
            })
        },

        //生成店铺管理store
        getShopStore: function () {

            Ext.define('ShopModel', {
                //不要忘了继承
                extend: 'Ext.data.Model',
                fields: [
                    'id',
                    'shopId', 'catId', 'nick', 'title', 'description', 'bulletin',
                    'picPath', 'itemScore', 'serviceScore', 'deliveryScore',
                    'deExpress', 'enableMsg', 'msgTemp', 'msgSign', 'outPlatformType','outPlatformTypeValue',
                    {name: 'createTime', type: 'date', dateFormat: 'time'},
                    {name: 'updateTime', type: 'date', dateFormat: 'time'},
                    'sessionKey'
                ],
                idProperty: 'id'
            });



            return Ext.create('Ext.data.Store', {
                model: 'ShopModel',
                proxy: {
                    type: 'ajax',
                    url: '/shop/list',
                    reader: {
                        type: 'json',
                        successProperty: 'success',
                        root: 'data.obj.result',
                        //root: 'data.list',
                        totalProperty: 'data.obj.totalCount',
                        //totalProperty: 'data.totalCount',
                        messageProperty: 'msg'
                    },
                    listeners: {
                        exception: function(proxy, response, operation){
                            var data = Ext.decode(response.responseText);
                            Ext.MessageBox.show({
                                title: '警告',
                                msg: data.msg,
                                icon: Ext.MessageBox.ERROR,
                                buttons: Ext.Msg.OK
                            });
                        }
                    }
                },
                autoLoad: true,
                listeners: {
                    refresh: function () {
                        var sels = Ext.getCmp('ShopList').getSelectionModel().selected.items;
                        EBDesktop.Shop.showShopItem('', sels);
                    }
                },
                pageSize: 18
            });
        },


        //展示店铺详细信息
        showShopItem: function (sm, records) {
            var shopItem = Ext.getCmp('shopItem');

            shopItem.setLoading(true);
            Ext.Function.defer(function () {
                shopItem.setLoading(false);
                if (records[0]){
                    shopItem.getForm().loadRecord(records[0]);
                    shopItem.down('#enableMsg').setValue({enableMsg: records[0].get('enableMsg')});
                }else{
                    shopItem.getForm().reset();
                }
            }, 300);
        }
    }
});