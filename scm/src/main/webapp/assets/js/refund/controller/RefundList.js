/**
 * Created by Lein xu
 */
Ext.define('Refund.controller.RefundList', {
    extend: 'Ext.app.Controller',
    views: ['RefundList','List','Search'],
    stores: ['RefundList','Brand','ShopAll'],
    models: ['RefundList'],
    init: function () {
        var start = "", end = "" , type;

        this.control({
            //数据查询
            '#confirmBtn': {
                click: function (btn) {
                    Espide.Common.doSearch('refundList','search',true);
                }
            },
            "#refundList": {
               //初始化数据渲染
                afterrender: function (grid) {
                    var root = this,
                        com = Espide.Common,
                        store = grid.getStore();
                    store.getProxy().extraParams = Ext.getCmp('search').getValues();


                },
                render: function (input) {
                    var map = new Ext.util.KeyMap({
                        target: 'search',    //target可以是组建的id  加单引号
                        binding: [
                            {                       //绑定键盘事件
                                key: Ext.EventObject.ENTER,
                                fn: function () {
                                    Espide.Common.doSearch('refundList','search',true);
                                }
                            }
                        ]
                    });
                }

            },
            //导出数据
            '#outRefundList':{
                click:function(){
                    var parms = Ext.getCmp('search').getForm().getValues();
                    window.open('/refund/extract2excel?'+ Ext.Object.toQueryString(parms));
                }
            },
            '#refundRefresh':{
                click:function(btn){
                    Ext.getCmp('refundList').getStore().reload();
                }
            },
            //创建退款申请
            '#creatRefund':{
                click: function(btn){

                    Ext.widget('addRefund').show(this,function(){
                        var createListStore = Ext.getCmp('createList').getStore()
                        //清空前面数据

                        if(createListStore.count()>0){
                            createListStore.autoSync = false;
                            createListStore.removeAll({silent:true});
                        }

                    });
                }
            },
            //退款作废
            '#cancelRefund':{
                click:function(btn){//删除退款选项
                    var url = 'refund/cancellation',
                        ids = Espide.Common.getGridSels('refundList', 'id'),
                        status = Espide.Common.getGridSels('refundList', 'status');

                    if(status[0].name == 'SUCCESS'){
                        Espide.Common.showGridSelErr('退款成功不能作废!');
                        return;
                    }


                    if(!Espide.Common.isGridSingleSel('refundList')){
                        Espide.Common.showGridSelErr('必须选择一条退款数据！');
                        return;
                    }



                    Ext.MessageBox.confirm('提醒', '您确定要作废退款吗？', function (optional) {
                        Espide.Common.doAction({
                            url: url,
                            params: {
                                id: ids.join()
                            },
                            successCall: function () {
                                Ext.getCmp('refundList').getStore().loadPage(1);
                            },
                            successTipMsg: '作废成功'
                        })(optional);
                    });
                }
            },
            '#remove':{
              click:function(btn){//删除退款选项
                  var url = 'refund/delete',
                      ids = Espide.Common.getGridSels('refundList', 'id');

                  if(!Espide.Common.isGridSingleSel('refundList')){
                      Espide.Common.showGridSelErr('必须选择一条退款数据！');
                      return;
                  }

                  Ext.MessageBox.confirm('提醒', '您确定要删除吗？', function (optional) {
                      Espide.Common.doAction({
                          url: url,
                          params: {
                              id: ids.join()
                          },
                          successCall: function () {
                              Ext.getCmp('refundList').getStore().loadPage(1);
                          },
                          successTipMsg: '删除成功'
                      })(optional);
                  });
              }
            },


        });
    }



});