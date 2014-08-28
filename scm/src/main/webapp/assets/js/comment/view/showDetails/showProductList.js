/**
 * Created by king on 13-12-23
 */

Ext.define('ShopProduct.view.showDetails.showProductList', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    alias: 'widget.showProductList',
    id: 'showProductList',
    height: 150,
    forceFit: true,
    split:true,
    store: 'GoodList',
    viewConfig: Espide.Common.getEmptyText(),
    initComponent: function () {

        this.columns = [
            {text: '商品名称', dataIndex: 'name'},
            {text: '品牌', dataIndex: 'brandName',},
            {text: 'sku', dataIndex: 'sku'},
            {text: '商家编号', dataIndex: 'productNo'},
            {text: '颜色', dataIndex: 'color',},
            {text: '规格', dataIndex: 'speci',},
            {text: '总库存', dataIndex: 'repositoryNum',},
        ];
        this.callParent(arguments);
    }
});


