/**
 * Created by HuaLei.Du on 13-12-19.
 */
Ext.define('Stock.model.List', {
    extend: 'Ext.data.Model',
    fields: ['id',
        'repository',
        'product',
        'productId',
        'repositoryId',
        {name: 'repoName',type: 'string',mapping: 'repository.name'},
        {name: 'brandName',type: 'string',mapping: 'product.brand.name'},
        {name: 'prodCaName',type: 'string',mapping: 'product.category.name'},
        {name: 'prodName',type: 'string',mapping: 'product.name'},
        {name: 'prodNo',type: 'string',mapping: 'product.productNo'},
        {name: 'prodCode',type: 'string',mapping: 'product.sku'},
        {name: 'marketPrice',type: 'string',mapping: 'product.marketPrice'},
        {name: 'minimumPrice',type: 'string',mapping: 'product.minimumPrice'},
        'amount',
        {name:'description',type: 'string',mapping: 'product.description'}
    ],
    idProperty: 'id'
});