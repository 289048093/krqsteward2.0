Ext.define('Supplier.model.GoodList',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields:[
            'id',                   //商品id
            'brandName',          	//商品品牌
            'brandId',          	//商品品牌
            'name',          	//商品名称
            'productNo',            	//商品编号
            'sku',          	//商品条形码
            'prodCategoryName',
            'description',          //商品描述
            'importPrice',            //促销价
            'marketPrice',        //市场价
            'minimumPrice',             //最低价
            'discountPrice',           //最低价
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
            {name:'location',type:'string'}, //库位赠品
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
            'TaoBaoSynStatus',
            {name:'type',type:'string',defaultValue:'售前换货'},
            {name:'buyCount',type:'string',defaultValue:'1'},
            {name:'exchangePostFee',type:'number',defaultValue:'0'},//线下换货邮费
            {name:'exchangePostPayer',type:'string',defaultValue:'BUYER'},//线下换货邮费承担{
            {name:'discountFee',type:'number',defaultValue:'0'},  //订单项优惠
            {name:'sharedPostFee',type:'number',defaultValue:'0'},//分摊邮费
            {name:'sharedDiscountFee',type:'number',defaultValue:'0'},//整单优惠分摊
            {name: 'orderItemType',type: 'string',defaultValue:'PRODUCT'},
            {name: 'num',type: 'string',defaultValue:'1'}
        ],
        idProperty: 'skuCode'
    }
)