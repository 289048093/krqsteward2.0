/**
 * 日志类型列表
 */
Ext.define('StorageFlowLog.model.LogList', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'repository',
        'repositoryId',
        {name: 'repoName', type: 'auto', mapping: 'repository.name'},
        'product',
        {name: 'brandName', type: 'auto', mapping: 'product.brand.name'},
        {name: 'categogryName', type: 'auto', mapping: 'product.category.name'},
        {name: 'productName', type: 'auto', mapping: 'product.name'},
        {name: 'boxSize', type: 'auto', mapping: 'product.boxSize'},
        {name: 'marketPrice', type: 'auto', mapping: 'product.marketPrice'},
        {name: 'sku', type: 'auto', mapping: 'product.sku'},
        'amount',


    ],
    idProperty: 'id'
});