Ext.define('Supplier.model.GoodCart',
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
            //'importPrice',
            //'discountPrice',
            'marketPrice',        //市场价
            //'minimumPrice',             //最低价
            'repositoryNum',
            'repositoryName',
            'repositoryId',
            'prodCategoryId',
            {name:'discountPrice',type:'number',defaultValue:'0'},//线下换货邮费
            {name:'exchangePostFee',type:'number',defaultValue:'0'},//线下换货邮费
            {name:'exchangePostPayer',type:'string',defaultValue:'BUYER'},//线下换货邮费承担
            {name:'discountFee',type:'number',defaultValue:'0'},  //订单项优惠
            {name:'sharedPostFee',type:'number',defaultValue:'0'},//分摊邮费
            {name:'sharedDiscountFee',type:'number',defaultValue:'0'},//整单优惠分摊
            {name: 'orderItemType',type: 'string',defaultValue:'PRODUCT'},
            {name: 'num',type: 'string',defaultValue:'1'}
        ],
        idProperty: 'autoId'
    }
)