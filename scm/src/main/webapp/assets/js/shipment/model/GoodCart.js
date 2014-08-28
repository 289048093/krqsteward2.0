Ext.define('Supplier.model.GoodCart',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields:['prodId','prodName', 'sellPropsStr', 'skuCode', 'itemType', 'prodPrice', 'prodCount', 'theoryNumber', 'outOrderNo', 'brandName', 'outOrderNo'],
        idProperty: 'prodId'
    }
)