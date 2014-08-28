package com.ejushang.steward.ordercenter.service.outplatforminvoke;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 产品店铺外部平台（淘宝，京东等）接口调用
 * </p>
 * 用法：new  ProductInvokeWrapper(88,68).syncProductInfo() ,同步商品id为88，店铺id为68 的店铺商品信息
 * <p/>
 * <p/>
 * User:  Sed.Lee(李朝)
 * Date: 14-8-7
 * Time: 上午9:48
 */
public class ProductInvokeWrapper {

    private static Map<PlatformType, ProductInvoke> invokeMap = new HashMap<PlatformType, ProductInvoke>();

    private ProductInvoke productInvoke;

    private ShopProduct shopProduct;

    private Product product;

    static {
        ApplicationContext ac = Application.getInstance().getApplicationContext();
        Map<String, ProductInvoke> map = ac.getBeansOfType(ProductInvoke.class);
        for (ProductInvoke pi : map.values()) {
            invokeMap.put(pi.getType(), pi);
        }
    }


    public ProductInvokeWrapper(String productSku, Integer shopId) throws ApiInvokeException {
        assertNull(productSku, "产品id不能为空");
        assertNull(shopId, "店铺id不能为空");
        GeneralDAO generalDAO = (GeneralDAO) Application.getBean("generalDAO");
        Search search = new Search(ShopProduct.class)
                .addFilterEqual("product.sku", productSku)
                .addFilterEqual("shopId", shopId);
        shopProduct = (ShopProduct) generalDAO.searchUnique(search);
        Shop shop = shopProduct.getShop();
        initProductInvoke(shop.getPlatformType());
        product = shopProduct.getProduct();
        productInvoke.setShopId(shopId);
    }

    public ProductInvokeWrapper(Integer productId, Integer shopId) throws ApiInvokeException {
        assertNull(productId, "产品id不能为空");
        assertNull(shopId, "店铺id不能为空");
        GeneralDAO generalDAO = (GeneralDAO) Application.getBean("generalDAO");
        Search search = new Search(ShopProduct.class)
                .addFilterEqual("prodId", productId)
                .addFilterEqual("shopId", shopId);
        shopProduct = (ShopProduct) generalDAO.searchUnique(search);
        Shop shop = shopProduct.getShop();
        initProductInvoke(shop.getPlatformType());
        product = shopProduct.getProduct();
        productInvoke.setShopId(shopId);
    }

    /**
     * @param shopProduct 必须为持久状态实体
     */
    public ProductInvokeWrapper(ShopProduct shopProduct) throws ApiInvokeException {
        assertNull(shopProduct, "产品同步-店铺信息不能为空");
        this.shopProduct = shopProduct;
        Shop shop = shopProduct.getShop();
        initProductInvoke(shop.getPlatformType());
        product = shopProduct.getProduct();
        productInvoke.setShopId(shop.getId());
    }

    /**
     * 初始化bean
     */
    private void initProductInvoke(PlatformType platformType) throws ApiInvokeException {
        productInvoke = invokeMap.get(platformType);
        if(productInvoke==null){
            throw new ApiInvokeException(platformType.getValue()+"不能同步");
        }
    }

    private void assertNull(Object obj, String errorMsg) {
        if (obj == null) {
            throw new NullPointerException(errorMsg);
        }
    }

    /**
     * 同步产品信息到智库诚（一口价，链接，上下架状态，店铺库存数量）
     *
     * @throws ApiInvokeException
     */
    public void syncProductInfo() throws ApiInvokeException {
        if (productInvoke == null) return;
        productInvoke.downSyncProductInfo(product.getSku(), shopProduct);
    }

    /**
     * 商品上架
     *
     * @throws ApiInvokeException
     */
    public void listing() throws ApiInvokeException {
        if (productInvoke == null) return;
        productInvoke.productListing(product.getSku());
    }

    /**
     * 商品下架
     *
     * @throws ApiInvokeException
     */
    public void delisting() throws ApiInvokeException {
        if (productInvoke == null) return;
        productInvoke.productDelisting(product.getSku());
    }

    /**
     * 库存更新
     *
     * @throws ApiInvokeException
     */
    public void updateShopStorage() throws ApiInvokeException {
        if (productInvoke == null) return;
        productInvoke.updateShopStorage(product.getSku(), shopProduct);
    }

    /**
     * 库存更新，不按百分比，而是按指定num更新
     * @param num
     * @throws ApiInvokeException
     */
    public void updateShopStorage(int num) throws ApiInvokeException {
        if (productInvoke == null) return;
        productInvoke.updateShopStorage(product.getSku(), shopProduct, num);
    }

    /**
     * 是否可以同步
     *
     * @return
     */
    public boolean canSync() {
        return productInvoke != null;
    }

}
