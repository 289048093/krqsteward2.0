Ext.define('Supplier.model.GoodList',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields:['id','prodCode', 'prodName', 'skuCode', 'itemType', 'prodPrice', 'prodCount', 'theoryNumber', 'brandName', 'outOrderNo'],
        idProperty: 'id'
    }
)