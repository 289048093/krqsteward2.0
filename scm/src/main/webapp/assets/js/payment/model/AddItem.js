/**
 * 日志类型列表
 */
Ext.define('Payment.model.AddItem', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'platformSubOrderNo',
        'type',
        'status',
      // 'returnStatus',
      //  'offlineReturnStatus',
//        'exchangedGoods',
        'productCode',
        'productName',
        'productSku',
       // 'product',
        'cateName',
        'brandName',
        'price',
        'discountPrice',
        'specInfo',
        'buyCount',
        {name:'repoNum',type:'string',defaultValue:'0'},
        'discountFee',
        'sharedDiscountFee',
        'sharedPostFee',
        'actualFee',
        'postCoverFee',
        'postCoverRefundFee',
        'serviceCoverFee',
        'serviceCoverRefundFee',
        {name:'refundFee',type:'string',defaultValue:'0.00'},
        'returnPostFee',
        //'returnPostPayer',
        {name:'offlineReturnPostFee',type:'string',defaultValue:'0.00'},
        {name:'offlineRefundFee',type:'string',defaultValue:'0.00'},
        //'offlineReturnPostPayer',
        {name:'exchangePostFee',type:'string',defaultValue:'0.00'},
        //'exchangePostPayer',
        'goodsFee',
        'priceDescription',
        {name:'feesString',type:'string',defaultValue:'0.00'},
        {name:'refundFeesString',type:'string',defaultValue:'0.00'},


    ],
    idProperty: 'autoId'
});