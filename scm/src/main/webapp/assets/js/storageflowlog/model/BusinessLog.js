/**
 * 日志类型列表
 */
Ext.define('StorageFlowLog.model.BusinessLog', {
    extend: 'Ext.data.Model',
    fields: [
        'order',    //订单对象
        'storage',
        {name: 'productNo', type: 'auto', mapping:'storage.product.productNo'},
        {name: 'speci', type: 'auto', mapping:'storage.product.speci'},
        {name: 'repoName', type: 'auto', mapping:'storage.repository.name'},
        'amount',//更新库存量
        'inOutStockType',//出入库类型
        'inOutStockTypeValue',
        'operator',
        {name: 'operatorName', type: 'auto', mapping:'operator.name'},//经办人姓名
        {name: 'operatorUserName', type: 'auto', mapping:'operator.username'},//经办人账号
        {name: 'createTime', type: 'date', dateFormat: 'time'}, //出入库时间
        'beforeAmount',//更新前库存量
        'afterAmount',//更新后库存量
        'type',
        'desc'
    ],
    idProperty:'id'
});