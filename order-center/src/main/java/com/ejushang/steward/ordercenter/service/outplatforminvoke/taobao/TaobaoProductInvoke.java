package com.ejushang.steward.ordercenter.service.outplatforminvoke.taobao;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.openapicenter.tb.api.ProductApi;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.service.ProductPlatformService;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.AbstractProductInvoke;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.response.*;
import org.apache.commons.lang3.StringUtils;
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
 * 商品的淘宝API调用类<p/>
 * 淘宝商品Item对应智库诚的同一产品编码的一系列产品，如果一个产品编码（outerProductNo)下有多个条形码(SKU),则以多规格的形式处理，
 * 每个具体的商品对应<a href='http://api.taobao.com/apidoc/dataStruct.htm?path=cid:4-dataStructId:17-apiId:164-invokePath:skus'>淘宝的SKU</a>
 * <br/>
 * 智库诚Product的外部平台商品编码（outerProductNo） 为淘宝的Item的商家编码(outer_id);<br/>
 * 智库诚Product的条形码（sku） 为淘宝Item下的SKU的商家编码(outer_id)。
 * User:  Sed.Lee(李朝)
 * Date: 14-5-29
 * Time: 上午9:48
 */
@Component
public class TaobaoProductInvoke extends AbstractProductInvoke {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductPlatformService productPlatformService;

    @Override
    public PlatformType getType() {
        return PlatformType.TAO_BAO;
    }


    /**
     * 从淘宝同步数据到智库诚
     * <p/>
     * 同步一口价、促销价、产品链接
     * <p/>
     * //     * @param outerProductNo
     *
     * @param sku
     */
    @Override
    public void downSyncProductInfo(String sku, ShopProduct shopProduct) throws ApiInvokeException {
        TbProduct product = getTbProductByProductSku(sku);
        copyProperties(product, shopProduct);
    }


    /**
     * 获取淘宝商品信息
     *
     * @param productSku
     * @return
     * @throws Exception
     */
    private TbProduct getTbProductByProductSku(String productSku) throws ApiInvokeException {
        TbProduct product = new TbProduct();
        try {
            Sku tbSku = findTaobaoSkuByProductSku(productSku);
            Item item = findItemByProductNo(productSku);
            if (tbSku == null) {
                if (item == null) {
                    throw new ApiInvokeException(String.format("淘宝没有找到sku[%s]的商品", productSku));
                }
                product.setNum(item.getNum());
                product.setPrice(item.getPrice());
            } else {
                product.setSkuId(tbSku.getSkuId());
                product.setPrice(tbSku.getPrice());
                product.setNum(tbSku.getQuantity());
            }
            product.setNumIid(item.getNumIid());
            product.setTbSku(tbSku);
            product.setTbItem(item);
            product.setStatus(item.getApproveStatus());
        } catch (Exception e) {
            throw new ApiInvokeException(e.getMessage(), e);
        }
        return product;
    }

    /**
     * 产品下架
     * <p/>
     * 如果产品下没有其他规格，直接下架产品，如果有其他规格的，则把此规格的sku库存数量设置为0
     * <p/>
     *
     * @param sku
     */
    @Override
    public void productDelisting(String sku) throws ApiInvokeException {
        TbProduct product = getTbProductByProductSku(sku);
        if (!isOnSale(product.getStatus())) {
            return;
        }
        if (product.isItem()) {
            ItemUpdateDelistingResponse response = null;
            try {
                response = getProductApi().itemUpdateDelisting(product.getNumIid());
            } catch (ApiException e) {
                throw new ApiInvokeException(e.getMessage(), e);
            }
            assertSuccess(response);
        } else {
            try {
                updateSkuQuantity(product.getNumIid(), product.getSkuId() + ":0");  //把产品库存置为0则自动下架
            } catch (ApiException e) {
                throw new ApiInvokeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 产品上架
     *
     * @param sku
     */
    @Override
    public void productListing(String sku) throws ApiInvokeException {
        final Integer storageNum = productPlatformService.getShopRealStorage(sku, getShopId());
        if (storageNum <= 0) {//库存为0则不做上架操作
            return;     //TODO   是否终止上架操作
        }
        TbProduct product = getTbProductByProductSku(sku);
        if (isOnSale(product.getStatus()) && product.getNum() > 0) {
            return;
        }

        try {
            if (product.isItem()) {
                listingItem(product.getNumIid(), Long.valueOf(storageNum));

            } else {

                String skuidQuantity = product.getSkuId() + ":" + storageNum;
                updateSkuQuantity(product.getNumIid(), skuidQuantity);
                if (!isOnSale(product.getStatus())) {//如果商品未上架，则先上架商品，否则，更新商品数量
                    listingItem(product.getNumIid(), product.getNum());
                    Item tbItem = findItemByProductNo(sku);
                    List<Sku> skus = tbItem.getSkus();   //多个sku的产品 当Ware已经下架的时候 单独上架一个SKU,则其他SKU得设置库存为0 ,避免其他sku也上架的情况
                    for (Sku tbSku : skus) {
                        if (!product.getSkuId().equals(tbSku.getSkuId())) {
                            updateSkuQuantity(tbItem.getNumIid(), tbSku.getSkuId() + ":0");
                        }
                    }
                }
            }
        } catch (Exception e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException(errorMsg, e);
        }
    }

    /**
     * 更新淘宝平台库存
     * <p/>
     * <b>更新库存前，先判断是否上架，如果是没有上架，则不更新库存</b>
     *
     * @param sku
     */
    @Override
    @Transactional
    public void updateShopStorage(String sku, ShopProduct shopProduct) throws ApiInvokeException {
        if (shopProduct == null) {
            shopProduct = productPlatformService.findByProductSkuAndShopId(sku, getShopId());
        }
        Integer storageNum = productPlatformService.getShopRealStorage(sku, getShopId());

        long updateNum = storageNum < 0 ? 0L : Long.valueOf(storageNum);//如果库存买超了，则改为0，下架     //TODO 是直接下架 还是 抛出异常信息？
//        if (storageNum < 0) {
//            throw new ApiInvokeException(String.format("Sku为[%s]的产品库存小于零，不能同步库存到淘宝，建议进行入库操作", sku));
//        }
        shopProduct.setStorageNum(storageNum);
        TbProduct product = getTbProductByProductSku(sku);
        if (!shopProduct.isAutoPutaway() && !isOnSale(product.getStatus())) {//如果不上架，则不修改库存
            return;
        }
        if (product.getNum() != null && product.getNum().equals(updateNum)) {
            return;
        }
        try {
            if (product.isItem()) {
                updateItemNum(product.getNumIid(), updateNum);
            } else {
                updateSkuQuantity(product.getNumIid(), product.getSkuId() + ":" + updateNum);
            }
        } catch (ApiException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException(errorMsg, e);
        }

        generalDAO.saveOrUpdate(shopProduct);
    }

    @Override
    @Transactional
    public void updateShopStorage(String sku, ShopProduct shopProduct, int storageNum) throws ApiInvokeException {
        if (shopProduct == null) {
            shopProduct = productPlatformService.findByProductSkuAndShopId(sku, getShopId());
        }
        long updateNum = storageNum < 0 ? 0L : (long) storageNum;//如果库存买超了，则改为0，下架     //TODO 是直接下架 还是 抛出异常信息？
//        if (storageNum < 0) {
//            throw new ApiInvokeException(String.format("Sku为[%s]的产品库存小于零，不能同步库存到淘宝，建议进行入库操作", sku));
//        }
        shopProduct.setStorageNum(storageNum);
        TbProduct product = getTbProductByProductSku(sku);
        if (!shopProduct.isAutoPutaway() && !isOnSale(product.getStatus())) {//如果不上架，则不修改库存
            return;
        }
        if (product.getNum() != null && product.getNum().equals(updateNum)) {
            return;
        }
        try {
            if (product.isItem()) {
                updateItemNum(product.getNumIid(), updateNum);
            } else {
                updateSkuQuantity(product.getNumIid(), product.getSkuId() + ":" + updateNum);
            }
        } catch (ApiException e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException(errorMsg, e);
        }

        generalDAO.saveOrUpdate(shopProduct);
    }

    /**
     * taobao api更新Item库存
     *
     * @param num_iid
     * @param num
     * @throws ApiException
     */
    private void updateItemNum(Long num_iid, Long num) throws ApiException {
        ProductApi api = getProductApi();
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("num_iid", num_iid);
        args.put("num", num);
        ItemUpdateResponse response = null;
        try {
            response = api.itemUpdate(args);
        } catch (Exception e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiException(errorMsg, e);
        }
        if (response != null) {
            assertSuccess(response);
        }
    }

    /**
     * 淘宝商品上架
     *
     * @param numIid
     * @param num
     * @throws Exception
     */
    private void listingItem(Long numIid, Long num) throws Exception {
        if (num < 0) {
            return;
        }
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("num_iid", numIid);
        args.put("num", num);
        ItemUpdateListingResponse response = getProductApi().itemUpdateListing(args);
        assertSuccess(response);
    }

    /**
     * 是否已经下架
     *
     * @param item
     * @return
     */
    private boolean isOnSale(Item item) {
        return TaobaoProductStatus.onsale.name().equals(item.getApproveStatus());
    }

    /**
     * 是否已经下架
     *
     * @param status
     * @return
     */
    private boolean isOnSale(String status) {
        return TaobaoProductStatus.onsale.name().equals(status);
    }

    /**
     * 淘宝规格商品数量修改
     *
     * @param numIid taobao的Item.numIid
     * @param skuNum taobao的库存properties   skuId:storageNum
     * @return
     * @throws Exception
     */
    private Item updateSkuQuantity(Long numIid, String skuNum) throws ApiException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("num_iid", numIid);
        args.put("skuid_quantities", skuNum);
        SkusQuantityUpdateResponse skusQuantityUpdateResponse = null;
        try {
            skusQuantityUpdateResponse = getProductApi().updateSkusQuantity(args);
        } catch (Exception e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiException(errorMsg, e);
        }
        assertSuccess(skusQuantityUpdateResponse);
        return skusQuantityUpdateResponse.getItem();
    }

    /**
     * 验证参数
     *
     * @param outerProductNo
     * @param sku
     * @return outerProductNo
     */
    private String validParams(String outerProductNo, String sku) throws ApiInvokeException {
        if (StringUtils.isBlank(sku)) {
            throw new ApiInvokeException("参数sku不能为空");
        }
        if (StringUtils.isBlank(outerProductNo)) {
            Product product = productService.findProductBySKU(sku);
            if (product == null) {
                throw new ApiInvokeException("没有找到该sku的产品");
            }
            outerProductNo = product.getOuterProductNo();
        }
        return outerProductNo;
    }

    /**
     * 拷贝淘宝商品属性到系统商品平台
     *
     * @param product
     * @param shopProduct
     */
    private void copyProperties(TbProduct product, ShopProduct shopProduct) {
        shopProduct.setPrice(Money.valueOf(product.getPrice()));
        shopProduct.setStorageNum(product.getNum().intValue());
        shopProduct.setPlatformUrl(generateURL(product.getNumIid()));
        shopProduct.setStorageNum(product.getNum().intValue());
        shopProduct.setPutaway(isOnSale(product.getStatus()));
    }

    /**
     * 通过产品编码查询到天猫的商品
     *
     * @param outerProductNo
     * @return
     */
    private Item findItemByProductNo(String outerProductNo) throws ApiInvokeException {
        ProductApi api = getProductApi();
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("outer_id", outerProductNo);
        args.put("fields", "num_iid,num,price,sku,list_time,delist_time,approve_status");
        ItemsCustomGetResponse response = null;
        try {
            response = api.itemsCustomGet(args);
        } catch (Exception e) {
            String errorMsg = (e.getCause() instanceof SocketTimeoutException) ? "网络连接超时，请重试" : e.getMessage();
            throw new ApiInvokeException("调用淘宝查询商品API出错：" + errorMsg, e);
        }
        List<Item> itemList = response.getItems();
        if (itemList == null || itemList.isEmpty()) {
            throw new ApiInvokeException(String.format("淘宝上没有找到外部平台产品编号为[%s]的相关产品信息,或者存在多个该商家编码的商品", outerProductNo));
        }
        assertSuccess(response);
        return itemList.get(0);
    }

    /**
     * 通过产品条形码（sku)查找淘宝商品sku信息
     * <p/>
     * 当该产品sku下没有其他sku时，查询结果为null，如邮费补差这类没有其他规格的产品
     *
     * @param sku
     * @return
     * @throws Exception
     */
    private Sku findTaobaoSkuByProductSku(String sku) throws Exception {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("outer_id", sku);
        args.put("fields", "sku_id,num_iid,quantity,price,status");
        List<Sku> taobaoSkus = getProductApi().getSkusCustom(args);
        if (taobaoSkus != null && taobaoSkus.size() > 1) {
            throw new ApiException(String.format("淘宝上存在多个相同商家编码[%s]的产品", sku));
        }
        return taobaoSkus != null ? taobaoSkus.get(0) : null;
    }

    /**
     * 获取产品接口
     * <p/>
     * 与session绑定，一个session一个API instance，因为API的sessionKey是有有效期的
     *
     * @return
     */
    private ProductApi getProductApi() {
        String key = "TAOBAO_PRODUCT_API_" + getShopId();
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        ProductApi api = (ProductApi) session.getAttribute(key);
        if (api == null) {
            api = new ProductApi(getShopAuth().getSessionKey());
            session.setAttribute(key, api);
        }
        return api;
    }

    /**
     * 生成链接地址
     *
     * @param numIid
     * @return
     */
    private String generateURL(Long numIid) {
        return String.format(StringUtils.trimToEmpty(getUriPattern()), numIid);
    }

    /**
     * 获取链接模版
     *
     * @return
     */
    private String getUriPattern() {
        return Application.getInstance().getConfigValue(Application.PropertiesKey.TAOBAO_ITEM_URI_PATTERN.value);
    }

    /**
     * 判断请求是否有错误
     *
     * @param response
     * @return
     */
    private void assertSuccess(TaobaoResponse response) {
        if (StringUtils.isNotBlank(response.getErrorCode())) {
            String errorMsg = StringUtils.isBlank(response.getSubMsg()) ? response.getMsg() : response.getSubMsg();
            throw new StewardBusinessException("淘宝产品信息同步异常：" + errorMsg);
        }
    }
}

class TbProduct {
    private Long numIid;

    private Long skuId;

    private Long num;

    private String status;

    private String price;

    private Sku tbSku;

    private Item tbItem;

    Long getSkuId() {
        return skuId;
    }

    void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    Sku getTbSku() {
        return tbSku;
    }

    void setTbSku(Sku tbSku) {
        this.tbSku = tbSku;
    }

    Item getTbItem() {
        return tbItem;
    }

    void setTbItem(Item tbItem) {
        this.tbItem = tbItem;
    }

    Long getNumIid() {
        return numIid;
    }

    void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    Long getNum() {
        return num;
    }

    void setNum(Long num) {
        this.num = num;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    String getPrice() {
        return price;
    }

    void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return 判断是不是Item 否则 是taobao的SKU
     */
    boolean isItem() {
        return tbSku == null;
    }
}
