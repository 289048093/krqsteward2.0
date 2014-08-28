/**
 * Created by king on 13-12-23
 */

Ext.define('Comment.controller.Comment', {
        extend: 'Ext.app.Controller',
        views: [
            'Comment',
            'Search',
            'List',
            'WidgetPanel',
            'WidgetPanel2',
        ],
        models: ['List'],
        stores: ['List'],
        init: function () {
            this.control({
                '#commentType':{
                    click:function(btn){
//                        /comment/findAllCommentCategory


                        if (!Espide.Common.isGridSingleSel('List')) {
                            Espide.Common.showGridSelErr('必须选择一条会员数据');
                            return;
                        }

                        var id = Espide.Common.getGridSelsId('List', 'id');

                        Ext.Ajax.request({
                                url: " /comment/findAllCommentCategory",
                                success:function(response, options){

                                    var data  = eval('('+response.responseText+')').data.list,items = [];

                                    Ext.each(data,function(dataItem,i){

                                        items.push({boxLabel:dataItem.name,name:'categoryId',inputValue:dataItem.id});

                                    });

                                    var formItems = [];
                                    formItems.push({

                                            name: 'commentId',
                                            hidden:true,
                                            value:id
                                        }
                                    );

                                    formItems.push(
                                        {
                                            xtype: 'checkboxgroup',
                                            fieldLabel: '会员标签',
                                            columns: 2,
                                            vertical: true,
                                            items:items
                                        }
                                    );


                                    formItems.push(
                                        {
                                            xtype: 'checkbox',
                                            name: 'isVisit',
                                            labelWidth: 93,
                                            itemId: 'isVisit',
                                            id: 'isVisit',
                                            labelAlign: 'left',
                                            inputValue: true,
                                            fieldLabel: '是否转入回访'
                                        }
                                    );


                                    var tagForm = Ext.create('Ext.form.Panel', {
                                        baseCls: 'x-plain',
                                        labelWidth: 80,
                                        forceFit: true,
                                        border: false,
                                        layout: 'form',
                                        header: false,
                                        frame: false,
                                        bodyPadding: '5 5 0',
                                        requires: ['Ext.form.field.Text'],
                                        fieldDefaults: {
                                            blankText: '不能为空',
                                            allowBlank: false,
                                            msgTarget: 'side',
                                            labelWidth: 75
                                        },
                                        defaultType: 'textfield',
                                        items: formItems

                                    });



                                    var tagWin = Ext.create("Ext.window.Window", {
                                        title: "批量添加会员标签",
                                        width: 500,
                                        height: 630,
                                        items: [tagForm],
                                        buttonAlign: "right",
                                        modal: true,
                                        buttons: [
                                            {
                                                text: '保存',
                                                itemId: 'updateBtn',
                                                handler: function () {
                                                    if (tagForm.isValid()) {
                                                        var FormData =  tagForm.getForm().getValues();
                                                        FormData.categoryIds =  Array.isArray(FormData.categoryId) ? FormData.categoryId.join(",") : FormData.categoryId;

                                                        tagForm.submit({
                                                            submitEmptyText:false,
                                                            method:'get',
                                                            url: "/comment/addCategoryToComment",
                                                            params: FormData,
                                                            success: function (form, action) {
                                                                var data = Ext.JSON.decode(action.response.responseText);
                                                                if (data.success) {
                                                                    Espide.Common.tipMsgIsCloseWindow(data, tagWin,'List', true);
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
                                            }
                                        ]
                                    });
                                    //更新窗口
                                    tagWin.show();


                             }
                        });

                    }
                },
                //刷新
                "#refresh": {
                    click: function () {
                        Espide.Common.reLoadGird('List', false, true);
                    }
                },
                '#addTags':{
                    click:function(btn){

                        if(!Espide.Common.isGridSel('List')){
                            Espide.Common.showGridSelErr('必须选择一条会员数据');
                            return;
                        }

                        var ids = Espide.Common.getGridSelsId('List','id');
                        var mobile = Espide.Common.getGridSels('List','mobile');
                        console.log(mobile);


                        Ext.Ajax.request({
                            url: "/customerTag/listNoPage",
                            success:function(response, options){

                                var data  = eval('('+response.responseText+')').data.list,items = [];

                                Ext.each(data,function(dataItem,i){

                                    items.push({boxLabel:dataItem.name,name:'tagId',inputValue:dataItem.id});

                                });


                                var formItems = [];
                                formItems.push({

                                        name: 'mobiles',
                                        //hidden:true,
                                        value: mobile
                                    }
                                );

                                formItems.push(
                                    {
                                        xtype: 'checkboxgroup',
                                        fieldLabel: '会员标签',
                                        columns: 2,
                                        vertical: true,
                                        items:items
                                    }
                                );



                                var tagForm = Ext.create('Ext.form.Panel', {
                                    baseCls: 'x-plain',
                                    labelWidth: 80,
                                    forceFit: true,
                                    border: false,
                                    layout: 'form',
                                    header: false,
                                    frame: false,
                                    bodyPadding: '5 5 0',
                                    requires: ['Ext.form.field.Text'],
                                    fieldDefaults: {
                                        blankText: '不能为空',
                                        allowBlank: false,
                                        msgTarget: 'side',
                                        labelWidth: 75
                                    },
                                    defaultType: 'textfield',
                                    items: formItems

                                });



                                var tagWin = Ext.create("Ext.window.Window", {
                                    title: "批量添加会员标签",
                                    width: 500,
                                    height: 630,
                                    items: [tagForm],
                                    buttonAlign: "right",
                                    modal: true,
                                    buttons: [
                                        {
                                            text: '保存',
                                            handler: function () {
                                                if (tagForm.isValid()) {
                                                    var FormData =  tagForm.getForm().getValues();
                                                    FormData.tagIds =  Array.isArray(FormData.tagId) ? FormData.tagId.join(",") : FormData.tagId;

                                                    tagForm.submit({
                                                        submitEmptyText:false,
                                                        method:'get',
                                                        url: "/comment/addTagInCommentToCustomers",
                                                        params: FormData,
                                                        success: function (form, action) {
                                                            var data = Ext.JSON.decode(action.response.responseText);
                                                            if (data.success) {
                                                                Espide.Common.tipMsgIsCloseWindow(data, tagWin,'List', true);
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
                                        }
                                    ]
                                });
                                //更新窗口
                                tagWin.show();
                            }
                        });

                    }
                },
                '#CommentSearch #confirmBtn': {
                    'click': function (btn) {
                        var com = Espide.Common;

                        com.doFormCheck(Ext.getCmp('search').getForm(), function () {
                            com.reLoadGird('List', 'search', true);
                        }, '请正确输入要搜索的信息!')
                    }
                },
                '#addProduct': {
                    'click': function (btn) {
                        Ext.widget('addProductWin').show(this, function () {
                        });
                    }
                },
                //查询按钮
                '#searchBtn': {
                    'click': function () {
                        Espide.Common.doSearch('addProductList', 'addProductInfo', true);
                    }
                },
                '#Addcomfirm': {
                    'click': function (btn) {
                        var data = Ext.getCmp('addProductInfo').getValues(),
                            synStatus = Ext.getCmp('synStatus').getValue(),
                            autoPutaway = Ext.getCmp('autoPutaway').getValue(),
                            ProductId = Espide.Common.getGridSelsId('addProductList');

                        if (!data.synStatus) {
                            data.synStatus = synStatus;
                        }
                        ;
                        if (!data.autoPutaway) {
                            data.autoPutaway = autoPutaway;
                        }

                        data.prodId = ProductId;

                        Espide.Common.doAction({
                            url: '/shopProduct/add',
                            params: data,
                            successCall: function () {
                                Ext.getCmp('addProductWin').close();
                                Ext.getCmp('List').getStore().loadPage(1);
                            },
                            failureCall: function () {
                            },
                            successTipMsg: '产品添加成功'
                        })('yes');


                    }
                },
                '#remove': {
                    click: function (btn) {
                        var url = '/shopProduct/delete',
                            ids = Espide.Common.getGridSels('List', 'id');

                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('请先选择要删除的品牌');
                            return;
                        }

                        Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                            Espide.Common.doAction({
                                url: url,
                                params: {
                                    shopProductIds: ids.join()
                                },
                                successCall: function () {
                                    Ext.getCmp('List').getStore().loadPage(1);
                                },
                                successTipMsg: '删除成功'
                            })(optional);
                        });
                    }
                },
                '#batUp': {
                    click: function (btn) {
                        var url = '/shopProduct/listing',
                            ids = Espide.Common.getGridSels('List', 'id');

                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('请先选择要批量上架产品');
                            return;
                        }

                        // Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                        Espide.Common.doAction({
                            url: url,
                            params: {
                                shopProductIds: ids.join()
                            },
                            successCall: function () {
                                Ext.getCmp('List').getStore().loadPage(1);
                            },
                            successTipMsg: '上架成功'
                        })('yes');
                        // });
                    }
                },
                '#batDown': {
                    click: function (btn) {
                        var url = '/shopProduct/delisting',
                            ids = Espide.Common.getGridSels('List', 'id');

                        if (ids.length < 1) {
                            Espide.Common.showGridSelErr('请先选择要批量下架产品');
                            return;
                        }

                        // Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                        Espide.Common.doAction({
                            url: url,
                            params: {
                                shopProductIds: ids.join()
                            },
                            successCall: function () {
                                Ext.getCmp('List').getStore().loadPage(1);
                            },
                            successTipMsg: '下架成功'
                        })('yes');
                        // });
                    }
                }
            });
        }
    }
);