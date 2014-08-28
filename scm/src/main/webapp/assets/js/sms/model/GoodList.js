Ext.define('ShopProduct.model.GoodList',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields:[
            'id','name', 'sku', 'productNo', 'color', 'repositoryNum', 'brandName', 'speci',
            ],
        idProperty: 'id'
    }
)
