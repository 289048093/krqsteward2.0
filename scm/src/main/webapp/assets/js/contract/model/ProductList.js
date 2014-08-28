//定义产品
Ext.define('Product.model.ProductList', {
    extend: 'Ext.data.Model',
    fields: [
        'id',                   //商品id
        'brandId',                   //商品id
        'brand',          	//商品品牌
        {name: 'brandName', type: 'string', mapping: 'brand.name'},
        'name',          	//商品名称
        'productNo',            	//商品编号
        'sku',          	//商品条形码
        'category',
        {name: 'prodCategoryName', type: 'string', mapping: 'category.name'},
        'categoryId',
        'cid',   	//商品分类id
        'description',          //商品描述
        'shopPrice',            //销售价
        'standardPrice',        //市场价
        'buyPrice',             //进货价
        'color',                //颜色
        'weight',               //重量
        'boxSize',              //尺寸
        'speci',                //规格
        'type',                  //商品类型
        'storage'
    ],
    idProperty: 'id'
});