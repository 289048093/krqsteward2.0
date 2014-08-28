package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.domain.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * User:  Sed.Lee(李朝)
 * Date: 14-5-21
 * Time: 下午1:19
 */
@Service
@Transactional(readOnly = true)
public class ProductPlatformService {
    private static final Logger logger = LoggerFactory.getLogger(ProductPlatformService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private StorageService storageService;

    public List<ShopProduct> listByProductId(Integer productId) {
        Search search = new Search(ShopProduct.class);
        search.addFilterEqual("prodId", productId);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 查找平台下有没有产品记录，用于删除平台时判断
     *
     * @param shopId
     * @return
     */
    public List<ShopProduct> listByShopId(Integer shopId) {
        Search search = new Search(ShopProduct.class);
        search.addFilterEqual("shopId", shopId);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 通过产品sku和平台类型查找平台
     *
     * @param productSku
     * @param shopId
     * @return
     */
    public ShopProduct findByProductSkuAndShopId(String productSku, Integer shopId) {
        Search search = new Search(ShopProduct.class)
                .addFilterEqual("product.sku", productSku)
                .addFilterEqual("shop.id", shopId);
        return (ShopProduct) generalDAO.searchUnique(search);
    }

    public List<ShopProduct> listByProductSku(String productSku) {
        Search search = new Search(ShopProduct.class)
                .addFilterEqual("product.sku", productSku);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 获取平台实时应该分配的库存，即总库存*占比
     *
     * @param sku
     * @param shopId
     * @return
     */
    public Integer getShopRealStorage(String sku, Integer shopId) {
        Search search = new Search(ShopProduct.class).addFilterEqual("product.sku", sku).addFilterEqual("shop.id", shopId);
        ShopProduct pp = (ShopProduct) generalDAO.searchUnique(search);
        if (pp == null) {
            throw new StewardBusinessException("没有找到该产品的店铺信息，请确认已经添加");
        }
        Storage storage = storageService.findByProductId(pp.getProdId());
        Integer rate = pp.getStoragePercent();
        if (rate == null) {
            throw new StewardBusinessException(String.format("产品sku[%s]没有设置店铺[%s]的库存占比", pp.getProduct().getSku(), pp.getShop().getNick()));
        }
        return (int) Math.ceil(storage.getAmount() * rate / 100.0);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<ShopProduct> findAll() {
        Search search = new Search(ShopProduct.class).addFilterEqual("product.deleted", false);
        return generalDAO.search(search);
    }

    /**
     * 查询产品平台下的上样店铺
     * @param productId
     * @param platformType
     * @return
     */
    public List<ShopProduct> listByProductIdAndPlatformType(Integer productId, PlatformType platformType) {
        Search search = new Search(ShopProduct.class);
        search.addFilterEqual("prodId", productId);
        search.addFilterEqual("shop.platformType",platformType);
        //noinspection unchecked
        return generalDAO.search(search);
    }
}
