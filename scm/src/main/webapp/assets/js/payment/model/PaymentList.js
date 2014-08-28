/**
 * 日志类型列表
 */
Ext.define('Payment.model.PaymentList', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'originalOrderItemId',    //订单对象
        'platformSubOrderNo',
        'platformType',
        'platformOrderNo',
        'originalOrderId',
        'payTime',
        'buyerId',
        'buyerMessage',
        'remark',
        'allocateStatus',
        'type',
        'paymentFee',
        'refundFee',
        'shopId',
        'shop',
        {name:'shopName',type: 'string',mapping:'shop.title'},
        {name: 'createTime', type: 'date', dateFormat: 'time'},
        {name: 'updateTime', type: 'date', dateFormat: 'time'},
        {name: 'buyTime', type: 'date', dateFormat: 'time'},
        {name: 'payTime', type: 'date', dateFormat: 'time'},
        'remark',
        'updateTime',
        'operatorId'

    ],
    idProperty:'id'
});