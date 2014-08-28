/*
 * Created by king on 13-12-19
 */

Ext.define('Supplier.model.OrderList',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            {name: 'id', type: 'int'},
            'orderNo',
            'orderStatus',
            'platformOrderNo',
            'itemName',
            'actualFee',
            'orderType',
            'itemCount','itemNumCount','buyerMessage','remark','buyerId',
            'receiverState','receiverCity','receiverDistrict', 'receiverName',
            'receiverAddress', 'receiverZip', 'receiverPhone', 'receiverMobile',
            {name:'shippingNo',type:'string'},
            'shippingComp', 'repoName',
            'buyTime',
            'offlineRemark',
            'postFee',
            'payTime',
            'goodsFee',
            'repoId',
            'outPlatformType',
            'printedTime',
            'confirmedTime',
            {name:'platformType',type:'string',mapping:'outPlatformType.name'},
//            {name: 'buyTime', type: 'date', dateFormat: 'time'},
//            {name: 'payTime', type: 'date', dateFormat: 'time'},
            //{name: 'confirmTime', type: 'date', dateFormat: 'time'},

            'confirmUser','printUser',
            'shopName','receiptTitle','receiptContent'
        ],
        idProperty: 'id'
    }
)