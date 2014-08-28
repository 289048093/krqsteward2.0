package com.ejushang.steward.ordercenter.service.outplatforminvoke.jd;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.openapicenter.jd.api.JdSkuApi;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.service.ProductPlatformService;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.AbstractProductInvoke;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.jd.open.api.sdk.response.ware.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品的京东API调用
 * User:  Sed.Lee(李朝)
 * Date: 14-5-30
 * Time: 下午1:18
 */
@Component
public class JdProductInvoke extends AbstractProductInvoke {

    private static final Logger log = LoggerFactory.getLogger(JdProductInvoke.class);

    @Autowired
    private ProductPlatformService productPlatformService;


    @Override
    public PlatformType getType() {
        return PlatformType.JING_DONG;
    }

    @Override
    public void downSyncProductInfo(String sku, ShopProduct shopProduct) throws ApiInvokeException {
        Sku item = getItemBySku(sku);
        if (item == null) {
            throw new ApiInvokeException(String.format("京东平台没有找到条形码为[%s]的相关产品", sku));
        }
        copyProperties(item, shopProduct);
    }

    /**
     * 拷jd商品属性到ProductPlatform
     * <p/>
     * 拷贝 参考价，平台链接
     *
     * @param sku
     * @param shopProduct
     */
    private void copyProperties(Sku sku, ShopProduct shopProduct) {
        shopProduct.setPrice(Money.valueOf(sku.getMarketPrice()));
        shopProduct.setStorageNum((int) sku.getStockNum());
        shopProduct.setPlatformUrl(generateURL(sku.getSkuId()));
        shopProduct.setPutaway(isOnSale(sku.getStatus()));
    }


    @Override
    public void productDelisting(String sku) throws ApiInvokeException {
        Long wareId = getItemWareIdBySku(sku);
        if (wareId == null) {
            throw new ApiInvokeException("京东查找产品API返回结果错误，ware_id为空，产品sku为" + sku);
        }
        Ware ware = getWareByWareId(wareId);
        if (ware == null) {
            log.warn("京东api调用：获取不到商品，sku为:{},wareId为:{}", sku, wareId);
            throw new ApiInvokeException("京东获取不到商品信息,可能是网络问题，请重试,sku:"+sku) ;
        }
        if (!isOnSale(ware)) {// 如果已经是下架状态就不再操作
            return;
        }
        List<Sku> jdSkus = ware.getSkus();
        try {
            JdSkuApi api = getApi();
            if (jdSkus.size() > 1) {//如果商品下的sku数量大于1，则把库存改为0就下架了
                WareSkuStockUpdateResponse response = api.wareSkuStockUpdateByOuterId(sku, 0L);
                assertSuccess(response);
                ware = getWareByWareId(wareId);
                if (ware.getStockNum() == 0) {//如果更新后总库存为0，则下架商品
                    WareUpdateDelistingResponse wareUpdateDelistingResponse = api.wareUpdateDelisting(wareId.toString(), null);
                    assertSuccess(wareUpdateDelistingResponse);
                }
            } else {
                WareUpdateDelistingResponse wareUpdateDelistingResponse = api.wareUpdateDelisting(wareId.toString(), null);
                assertSuccess(wareUpdateDelistingResponse);
            }
        } catch (JdException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("京东产品下架API调用异常：" + errorMsg, e);
        }

    }
    @Override
    public void productListing(String sku) throws ApiInvokeException {
        Sku item = getItemBySku(sku);
        if (item == null) {
            throw new ApiInvokeException(String.format("京东上没有找到sku为[%s]的产品", sku));
        }
        long wareId = item.getWareId();
        Ware ware = getWareByWareId(wareId);
        if (ware == null) {
            log.warn("京东api调用：获取不到商品，sku为:{},wareId为:{}", sku, wareId);
            throw new ApiInvokeException("京东获取不到商品信息,可能是网络问题，请重试,sku:"+sku) ;
        }
        try {
            if (isOnSale(ware)) {
                ShopProduct shopProduct = productPlatformService.findByProductSkuAndShopId(sku, getShopId());
                updateShopStorage(sku, shopProduct);//如果商品在买，则把库存更新即上架了
            } else {
                WareUpdateListingResponse wareUpdateListingResponse = getApi().wareUpdateListing(String.valueOf(wareId), null);
                assertSuccess(wareUpdateListingResponse);
                List<Sku> items = ware.getSkus(); //多个sku的产品 当Ware已经下架的时候 单独上架一个SKU,则其他SKU得设置库存为0 ,避免其他sku也上架的情况
                if (items != null && !items.isEmpty()) {
                    for (Sku tmp : items) {
                        if (!item.getSkuId().equals(tmp.getSkuId())) {
                            WareSkuStockUpdateResponse wareSkuStockUpdateResponse = getApi().wareSkuStockUpdateBySkuId(tmp.getSkuId().toString(), 0L);
                            assertSuccess(wareSkuStockUpdateResponse);
                        }
                    }
                }
            }
        } catch (JdException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("京东产品上架API调用异常：" + errorMsg, e);
        }
    }

    /**
     * 获取京东商品的Ward_id
     *
     * @param sku
     * @return
     * @throws ApiInvokeException
     */
    private Long getItemWareIdBySku(String sku) throws ApiInvokeException {
        Sku item = getItemBySku(sku);
        if (item == null) {
            throw new ApiInvokeException(String.format("京东上没有找到sku为[%s]的产品", sku));
        }
        return item.getWareId();
    }

    @Override
    @Transactional
    public void updateShopStorage(String sku, ShopProduct shopProduct) throws ApiInvokeException {
        if (shopProduct == null) {
            shopProduct = productPlatformService.findByProductSkuAndShopId(sku, getShopId());
        }
        if (!shopProduct.isAutoPutaway() && !shopProduct.getPutaway()) {//如果不上架，则不修改库存
            return;
        }

        Integer storageNum = productPlatformService.getShopRealStorage(sku, getShopId());

        Long updateNum = storageNum < 0 ? 0L : Long.valueOf(storageNum);//如果库存买超了，则改为0，下架  //TODO 是直接下架 还是 抛出异常信息？
        try {
            WareSkuStockUpdateResponse wareSkuStockUpdateResponse = getApi().wareSkuStockUpdateByOuterId(sku, updateNum);
            assertSuccess(wareSkuStockUpdateResponse);
            if (!storageNum.equals(shopProduct.getStorageNum())) {
                shopProduct.setStorageNum(storageNum);
                generalDAO.saveOrUpdate(shopProduct);
            }
        } catch (JdException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("调用京东更新产品库存API异常：" + errorMsg, e);
        }
    }

    @Override
    public void updateShopStorage(String sku, ShopProduct shopProduct, int storageNum) throws ApiInvokeException {
        if (shopProduct == null) {
            shopProduct = productPlatformService.findByProductSkuAndShopId(sku, getShopId());
        }
        if (!shopProduct.isAutoPutaway() && !shopProduct.getPutaway()) {//如果不上架，则不修改库存
            return;
        }

        Long updateNum = storageNum < 0 ? 0L : (long) storageNum;//如果库存买超了，则改为0，下架  //TODO 是直接下架 还是 抛出异常信息？
        try {
            WareSkuStockUpdateResponse wareSkuStockUpdateResponse = getApi().wareSkuStockUpdateByOuterId(sku, updateNum);
            assertSuccess(wareSkuStockUpdateResponse);
            if (storageNum != shopProduct.getStorageNum()) {
                shopProduct.setStorageNum(storageNum);
                generalDAO.saveOrUpdate(shopProduct);
            }
        } catch (JdException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("调用京东更新产品库存API异常：" + errorMsg, e);
        }
    }

    /**
     * 获取ware下的所有京东SKU
     *
     * @param wareId
     * @return
     * @throws ApiInvokeException
     */
    private Ware getWareByWareId(Long wareId) throws ApiInvokeException {
        try {
            WareGetResponse res = getApi().wareGet(wareId.toString(), "skus,ware_status");
            return res.getWare();
        } catch (JdException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("调用京东查询商品信息API异常：" + errorMsg, e);
        }
    }

    /**
     * 获取京东上的SKU通过产品系统的sku
     *
     * @param sku
     * @return
     * @throws ApiInvokeException
     */
    private Sku getItemBySku(String sku) throws ApiInvokeException {
        JdSkuApi api = getApi();
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("outer_id", sku);
        args.put("fields", "sku_id,ware_id,status,stock_num,jd_price,cost_price,market_price");
        try {
            SkuCustomGetResponse response = api.skuCustomGet(args);
            assertSuccess(response);
            return response.getSku();
        } catch (Exception e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("获取京东商品信息异常：" + errorMsg, e);
        }
    }

    /**
     * 判断响应是否有错误
     *
     * @param response
     * @return
     */
    private void assertSuccess(AbstractResponse response) throws ApiInvokeException {
        if (StringUtils.isNotBlank(response.getCode()) && !"0".equals(response.getCode())) {
            String errorMsg = StringUtils.isNotBlank(response.getZhDesc()) ? response.getZhDesc() : response.getMsg();
            throw new ApiInvokeException("京东产品信息同步异常:" + errorMsg);
        }
    }

    /**
     * 获取API接口
     * <p/>
     * 与session绑定，一个session一个API instance，因为API的sessionKey是有有效期的
     *
     * @return
     */
    private JdSkuApi getApi() {
        String key = "JD_SKU_API_" + getShopId();
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        JdSkuApi api = (JdSkuApi) session.getAttribute(key);
        if (api == null) {
            api = new JdSkuApi(getShopAuth().getSessionKey());
            session.setAttribute(key, api);
        }
        return api;
    }

    /**
     * 获取产品链接地址
     *
     * @param skuId
     * @return
     */
    private String generateURL(Long skuId) {
        return String.format(StringUtils.trimToEmpty(getUriPattern()), skuId);
    }

    /**
     * 获取链接模版
     *
     * @return
     */
    private String getUriPattern() {
        return Application.getInstance().getConfigValue(Application.PropertiesKey.JD_ITEM_URI_PATTERN.value);
    }

    private boolean isOnSale(Ware ware) {
        return JdProductStatus.ON_SALE.name().equals(ware.getWareStatus());
    }

    private boolean isOnSale(String status) {
        return JdProductStatus.ON_SALE.name().equals(status);
    }
}
