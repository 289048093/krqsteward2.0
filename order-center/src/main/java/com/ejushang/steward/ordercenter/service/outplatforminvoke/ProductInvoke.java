package com.ejushang.steward.ordercenter.service.outplatforminvoke;

import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.taobao.api.ApiException;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-5-29
 * Time: 上午9:52
 */
public interface ProductInvoke {

    /**
     * 获取平台类型
     *
     * @return 平台类型
     */
    PlatformType getType();

    /**
     * 设置当前调用的店铺，每次调用都需要先设置店铺
     * @param shopId
     */
    void setShopId(Integer shopId);

    /**
     * 获取当前调用的店铺
     * @return
     */
    Integer getShopId();

    /**
     * 从外部平台同步数据
     *
     * @param sku
     */
    void downSyncProductInfo(String sku, ShopProduct shopProduct) throws ApiInvokeException;

    /**
     * 商品下架
     *
     * @param sku
     * @throws ApiInvokeException
     */
    void productDelisting( String sku) throws ApiInvokeException;

    /**
     * 商品上架
     *
     * @param sku
     * @throws ApiInvokeException
     */
    void productListing(String sku) throws ApiInvokeException;

    /**
     * 更新淘宝店铺的库存(按产品店铺定义的百分比)，同时更新本地的店铺库存
     * <p/>
     * <b>更新库存前，先判断是否自动上架，如果是没有自动上架，则不会更新库存</b>
     *
     * @param sku
     * @param shopProduct 可选，如果为null，则根据sku和shopId查询
     * @throws ApiInvokeException
     */
    void updateShopStorage(String sku, ShopProduct shopProduct) throws ApiInvokeException;

    /**
     * 更新库存数量
     * @param sku
     * @param shopProduct
     * @param num  要更新的数量
     * @throws ApiInvokeException
     */
    void updateShopStorage(String sku, ShopProduct shopProduct,int num) throws ApiInvokeException;
}
