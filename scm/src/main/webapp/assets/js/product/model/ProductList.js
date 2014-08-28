//定义产品
Ext.define('Product.model.ProductList', {
    extend: 'Ext.data.Model',
    fields: [
        'id',                   //商品id
        'brandName',
        {name:'brandId',type:'int'},//商品品牌          	//商品品牌
        'name',          	//商品名称
        'productNo',            	//商品编号
        'sku',          	//商品条形码
        'prodCategoryName',
        'description',          //商品描述
        'importPrice',            //现价
        'marketPrice',        //市场价
        'minimumPrice',             //最低价
        'color',                //颜色
        'weight',               //重量
        'boxSize',              //尺寸
        'speci',                //规格
        'orgin', //产地
        'style', //产品类型
        'storageNum',
        'repositoryNum',
        'repositoryName',
        'repositoryId',
        'prodCategoryId',
        'outerProductNo',
        {name:'location',type:'string',mapping:'style.value'}, //库位赠品
        "style",
        'TaoBaoImportPrice',
        'TaoBaoIsPutaway',
        'TaoBaoMarketPrice',
        'TaoBaoProdLinkPrefix',
        'TaoBaoStorageNum',
        'TaoBaoPlatformUrl',
        'TaoBaoStoragePercentReal',
        'JDImportPrice',
        'JDIsPutaway',
        'JDMarketPrice',
        'JDProdLinkPrefix',
        'JDStorageNum',
        'JDPlatformUrl',
        'JDStoragePercentReal',
        'EJSImportPrice',
        'EJSIsPutaway',
        'EJSMarketPrice',
        'EJSProdLinkPrefix',
        'EJSStorageNum',
        'EJSPlatformUrl',
        'EJSStoragePercentReal',
        'EJSProductPlatformId',
        'TaoBaoProductPlatformId',
        'JDProductPlatformId',
        'JDSynStatus',
        'EJSSynStatus',
        'TaoBaoSynStatus'


    ],
    idProperty: 'id'
});