//定义供应商数据模型
Ext.define('Contract.model.ContractList', {
    extend: 'Ext.data.Model',
    fields: ['id', 'code', 'deposit', 'serviceFee', 'invoiceEJSTitle',
        'invoiceOtherTitle', 'otherRule', 'remark', 'overdueFine', 'beginTime',
        'endTime', 'realEndTime', 'endReason', 'supplier', 'ejsCompName', 'invoiceToEJS',
        'commission',           //佣金补差
        'paymentRule',         // 结算规则
        'paymentType',           //结算类型
        'shippingFeeType',       //物流补贴
        'shotFeeType' ,        // 拍摄费用
        'thirdPlatformFeeType',// 第三方平台费用
        'toEJSFeeType',          //到易居尚仓库的费用情况

        {name: 'supplierName', type: 'string', mapping: 'supplier.name'},
        {name: 'supplierId', mapping: 'supplier.id'},
        {name: 'realEndTime', type: 'date', dateFormat: 'time'},
        {name: 'endTime', type: 'date', dateFormat: 'time'},
        {name: 'beginTime', type: 'date', dateFormat: 'time'}
    ],
    idProperty: 'id'
});