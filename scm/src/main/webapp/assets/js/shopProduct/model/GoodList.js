Ext.define('ShopProduct.model.GoodList',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields:[
            'id','name', 'sku', 'productNo', 'color', 'repositoryNum', 'brandName', 'speci',
        'prodCategoryName',
        'description',          //商品描述
        'importPrice',            //现价
        'marketPrice',        //市场价
        'minimumPrice',             //最低价
        'weight',               //重量
        'boxSize',              //尺寸
        'orgin', //产地
        'style', //产品类型
        'storageNum',
        'repositoryName',
        'repositoryId',
        'prodCategoryId',
        'prodCategoryName',
        'outerProductNo',
    {name:'location',type:'string',mapping:'style.value'}, //库位赠品
        "style",
            ],
        idProperty: 'id'
    }
)
