/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.model.OrderList',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            {name: 'id', type: 'int'},
            'processed',//解析状态
            'discard',//是否有效
            'platformType',
            'shopName',
            'brandName',
            'outOrderNo',
            'outActualFee',
            'orderType',
            'buyerId',
            {name: 'buyTime', type: 'date', dateFormat: 'time'},
            {name: 'payTime', type: 'date', dateFormat: 'time'},
            'postFee',
            'province',
            'city',
            'receiptTitle',
            'receiptContent',
            'receiverDistrict',
            'address',
            'receiverName',
            'receiverZip',
            'receiverPhone',
            'receiverMobile',
            'shippingComp',
            'shippingNo',
            'orderApproves',
            'orderHandleLogs',
        ],
        idProperty: 'id'
    }
)