/*
 * Created by king on 13-12-19
 */

Ext.define('ShopProduct.model.List',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            {name: 'id', type: 'int'},
            'allNum',
            'synProportion',
            'autoPutaway',
            'productNo',
            'chaining',
            'name',
            'platformName',
            'price',
            'putaway',
            'shopName',
            'sku',
            'storageNum',
            'synNum',
            'synProportion',
            'synStatus',
            'storagePercent'
        ],
        idProperty: 'id'
    }
)

