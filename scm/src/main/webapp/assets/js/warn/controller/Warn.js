/**
 * Created by HuaLei.Du on 14-2-18.
 */

Ext.define('Warn.controller.Warn', {
    extend: 'Ext.app.Controller',
    views: ['NoSend', 'NoLogistics', 'StayTimeout', 'NoSign', 'OrderDetail', 'GoodsList'],
    stores: ['NoSend', 'NoLogistics', 'StayTimeout', 'NoSign', 'GoodsList'],
    models: ['NoSend', 'NoLogistics', 'StayTimeout', 'NoSign', 'GoodsList'],
    init: function () {
        this.control({
            'viewport': {
                afterrender: this.reLoadGrid
            },
            '#noSendGrid, #noLogisticsGrid, #stayTimeoutGrid, #noSignGrid': {
                itemdblclick: this.showOrderDetail
            }
        });
    },
    reLoadGrid: function () {
        setInterval(function () {
            Ext.getCmp('noSendGrid').getStore().reload();
            Ext.getCmp('noLogisticsGrid').getStore().reload();
            Ext.getCmp('stayTimeoutGrid').getStore().reload();
            Ext.getCmp('noSignGrid').getStore().reload();
        }, 300000);
    },
    orderInfoTpl: function () {
        return new Ext.XTemplate(
            '<div class="warn-order-detail">',
            '<table>',
            '   <tr>',
            '       <th>订单号：</th>',
            '       <td>{orderNo}</td>',
            '       <th>订单状态：</th>',
            '       <td>{orderStatus}</td>',
            '   </tr>',
            '   <tr>',
            '       <th>付款时间：</th>',
            '       <td>{payTime}</td>',
            '       <th>买家ID：</th>',
            '       <td>{buyerId}</td>',
            '   </tr>',
            '</table>',
            '</div>'
        );
    },
    receiverInfoTpl: function () {
        return new Ext.XTemplate(
            '<div class="order-totalFee">订单总额：{totalFee}元</div>',
            '<div class="warn-receiver-info">',
            '<table>',
            '   <tr>',
            '       <th>收货人：</th>',
            '       <td>{receiverName}</td>',
            '   </tr>',
            '   <tr>',
            '       <th>收货人电话：</th>',
            '       <td>{receiverMobile} {receiverPhone}</td>',
            '   </tr>',
            '   <tr>',
            '       <th>收货地址：</th>',
            '       <td>{receiverAddress}</td>',
            '   </tr>',
            '   <tr>',
            '       <th>物流公司：</th>',
            '       <td>{shippingComp}</td>',
            '   </tr>',
            '   <tr>',
            '       <th>物流单号：</th>',
            '       <td>{shippingNo}</td>',
            '   </tr>',
            '   <tr class="last">',
            '       <th valign="top">物流信息：</th>',
            '       <td class="express-info">',
            '       <table>',
            '           <tpl for="expressInfoData">',
            '            <tr><td valign="top" style="width:140px">{time}</td><td>{context}</td></tr>',
            '           </tpl>',
            '       </table>',
            '       </td>',
            '   </tr>',
            '</table>',
            '</div>'
        );
    },
    showOrderDetail: function (t, record) {
        var orderNo = record.raw.orderNo,
            orderDetail,
            self = this;

        Ext.widget('orderDetail').show();
        orderDetail = Ext.getCmp('orderDetail');
        orderDetail.setLoading(true);
        Ext.Ajax.request({
            url: '/warn/singleorderdetail',
            method: 'GET',
            params: {
                orderNo: orderNo
            },
            success: function (response) {
                var data = Ext.JSON.decode(response.responseText),
                    order = data.data.order,
                    orderInfoHTML,
                    receiverInfoHTML;

                if (data.success) {
                    orderInfoHTML = self.orderInfoTpl().apply(order);
                    receiverInfoHTML = self.receiverInfoTpl().apply(order);
                    Ext.getDom('orderInfo').innerHTML = orderInfoHTML;
                    Ext.getDom('receiverInfo').innerHTML = receiverInfoHTML;

                    Ext.getCmp('goodsList').getStore().load({
                        params: {
                            orderNo: orderNo
                        }
                    });

                    Ext.Function.defer(function () {
                        orderDetail.setLoading(false);
                    }, 500);
                } else {
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: data.msg,
                        buttons: Ext.MessageBox.OK,
                        icon: 'x-message-box-error',
                        fn: function () {
                            window.location.reload();
                        }
                    });
                }

            },
            failure: function () {
                Ext.Msg.show({
                    title: '错误',
                    msg: '系统发生错误,请联系管理员',
                    buttons: Ext.MessageBox.OK,
                    icon: 'x-message-box-error',
                    fn: function () {
                        window.location.reload();
                    }
                });
            }
        });
    }
});
