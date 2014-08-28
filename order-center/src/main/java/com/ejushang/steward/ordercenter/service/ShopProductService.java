package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvokeWrapper;
import com.ejushang.steward.ordercenter.service.transportation.PlatformService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.util.ShopProInputPar;
import com.ejushang.steward.ordercenter.vo.ShopProductSearchVo;
import com.ejushang.steward.ordercenter.vo.ShopProductVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-8
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */

@Service
@Transactional
public class ShopProductService {

    private static final Logger logger = Logger.getLogger(ShopProductService.class);
    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private ProductService productService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StorageService storageService;

    /**
     * 根据条件查询商品（关联店铺）
     *
     * @param shopProInputPar 输入参数，包括商品名、SKU、库存等
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShopProductVo> list(ShopProInputPar shopProInputPar) {
        List<ShopProductVo> shopProductVos = new ArrayList<ShopProductVo>();
        Search search = new Search(ShopProduct.class);
        //设置查询条件
        setSearchCondition(shopProInputPar, search);
        List<ShopProduct> shopProducts = generalDAO.search(search);
        for (ShopProduct shopProduct : shopProducts) {
            Integer prodId = shopProduct.getProdId();
            Integer allAmount = storageService.findAmountByProdId(prodId);
            if (!NumberUtil.isNullOrZero(shopProInputPar.getAllNumMin())) {
                if (shopProInputPar.getAllNumMin() > allAmount) {
                    continue;
                }
            }
            if (!NumberUtil.isNullOrZero(shopProInputPar.getAllNumMax())) {
                if (shopProInputPar.getAllNumMax() < allAmount) {
                    continue;
                }
            }
            //构建vo
            ShopProductVo shopProductVo = buildShopProductVo(shopProduct, allAmount);
            shopProductVos.add(shopProductVo);
        }
        return shopProductVos;
    }

    /**
     * 设置查询条件
     *
     * @param shopProInputPar 输入参数，包括商品名、SKU、库存等
     * @return
     */
    private void setSearchCondition(ShopProInputPar shopProInputPar, Search search) {
        if (!StringUtils.isBlank(shopProInputPar.getProductName())) {
            search.addFilterLike("product.name", "%" + shopProInputPar.getProductName() + "%");
        }
        if (!StringUtils.isBlank(shopProInputPar.getSku())) {
            search.addFilterLike("product.sku", "%" + shopProInputPar.getSku() + "%");
        }
        if (!StringUtils.isBlank(shopProInputPar.getProductNo())) {
            search.addFilterLike("product.productNo", "%" + shopProInputPar.getProductNo() + "%");
        }
        if (shopProInputPar.getPutaway() != null) {
            search.addFilterEqual("putaway", shopProInputPar.getPutaway());
        }
        if (!NumberUtil.isNullOrZero(shopProInputPar.getShopId())) {
            search.addFilterEqual("shopId", shopProInputPar.getShopId());
        }
        if (shopProInputPar.getSynStatus() != null) {
            search.addFilterEqual("synStatus", shopProInputPar.getSynStatus());
        }
        if (!NumberUtil.isNullOrZero(shopProInputPar.getStorageNumMin())) {
            search.addFilterGreaterOrEqual("storageNum", shopProInputPar.getStorageNumMin());
        }
        if (!NumberUtil.isNullOrZero(shopProInputPar.getStorageNumMax())) {
            search.addFilterLessOrEqual("storageNum", shopProInputPar.getStorageNumMax());
        }
    }

    /**
     * 将查询出的结果封装成VO
     *
     * @param shopProduct 商品信息
     * @param allAmount   总库存
     * @return
     */
    private ShopProductVo buildShopProductVo(ShopProduct shopProduct, Integer allAmount) {
        ShopProductVo shopProductVo = new ShopProductVo();
        shopProductVo.setId(shopProduct.getId());
        shopProductVo.setAutoPutaway(shopProduct.isAutoPutaway());
        shopProductVo.setChaining(shopProduct.getPlatformUrl());
        shopProductVo.setName(shopProduct.getProduct().getName());
        shopProductVo.setPlatformName(shopProduct.getShop().getPlatformType().getValue());
        shopProductVo.setPrice(shopProduct.getPrice());
        shopProductVo.setProductNo(shopProduct.getProduct().getProductNo());
        shopProductVo.setShopName(shopProduct.getShop().getNick());
        shopProductVo.setSku(shopProduct.getProduct().getSku());
        shopProductVo.setStorageNum(shopProduct.getStorageNum());
        shopProductVo.setSynProportion(shopProduct.getStoragePercent());
        shopProductVo.setSynStatus(shopProduct.getSynStatus());
        shopProductVo.setPutaway(shopProduct.getPutaway());
        shopProductVo.setBrand(shopProduct.getProduct().getBrand().getName());
        shopProductVo.setAllNum(allAmount);
        double synNum = Math.ceil(allAmount / 100.00 * shopProductVo.getSynProportion());
        shopProductVo.setSynNum((int) synNum);

        return shopProductVo;
    }

    /**
     * 批量上架
     *
     * @param shopProductIds id
     * @return
     */
    public void listing(Integer[] shopProductIds) throws ApiInvokeException {
        for (int i = 0; i < shopProductIds.length; i++) {
            ShopProduct shopProduct = get(shopProductIds[i]);
            if (shopProduct.getPutaway() == true) {
                throw new StewardBusinessException("店铺：" + shopProduct.getShop().getNick() + "的商品：" +
                        shopProduct.getProduct().getName() + "已经上架，请勿重复上架");
            }
            ProductInvokeWrapper productInvokeWrapper = new ProductInvokeWrapper(shopProduct.getProdId(), shopProduct.getShopId());
            productInvokeWrapper.listing();
            shopProduct.setPutaway(true);
        }
    }

    /**
     * 批量下架
     *
     * @param shopProductIds id
     * @return
     */
    public void delisting(Integer[] shopProductIds) throws ApiInvokeException {
        for (int i = 0; i < shopProductIds.length; i++) {
            ShopProduct shopProduct = get(shopProductIds[i]);
            if (shopProduct.getPutaway() == false) {
                throw new StewardBusinessException("店铺：" + shopProduct.getShop().getNick() + "的商品：" +
                        shopProduct.getProduct().getName() + "已经下架，请勿重复下架");
            }
            ProductInvokeWrapper productInvokeWrapper = new ProductInvokeWrapper(shopProduct.getProdId(), shopProduct.getShopId());
            productInvokeWrapper.delisting();
            shopProduct.setPutaway(false);
        }
    }

    /**
     * 批量删除
     *
     * @param shopProductIds id
     * @return
     */
    public void delete(Integer[] shopProductIds) {
        for (int i = 0; i < shopProductIds.length; i++) {
            ShopProduct shopProduct = get(shopProductIds[i]);
            generalDAO.remove(shopProduct);
        }
    }

    /**
     * 根据id，得到指定的shopProduct
     *
     * @param shopProductId id
     * @return
     */
    @Transactional(readOnly = true)
    public ShopProduct get(Integer shopProductId) {
        return generalDAO.get(ShopProduct.class, shopProductId);
    }

    /**
     * 添加一个shopProduct
     *
     * @param productIds 商品id（多个）
     * @param shopId 店铺id
     * @param storagePercent 库存占比
     * @param autoPutaway 自动上架
     * @param synStatys 自动同步
     * @param platformId  平台ID
     * @return
     */
    public void add(Integer[] productIds,Integer shopId,Integer storagePercent,Boolean autoPutaway,Boolean synStatys,Integer platformId) {
        List<ShopProduct> shopProducts = new ArrayList<ShopProduct>();
        for (int prodId : productIds) {
            ShopProduct shopProduct = new ShopProduct();
            setShopProductParameter(shopId, storagePercent, autoPutaway, synStatys, platformId, prodId, shopProduct);
            shopProducts.add(shopProduct);
        }
        generalDAO.saveOrUpdate(shopProducts);
    }

    /**
     * 设置shopProduct参数
     *
     * @param shopId 店铺id
     * @param storagePercent 库存占比
     * @param autoPutaway 自动上架
     * @param synStatus 自动同步
     * @param platformId  平台ID
     * @return
     */
    private void setShopProductParameter(Integer shopId, Integer storagePercent, Boolean autoPutaway, Boolean synStatus, Integer platformId, int prodId, ShopProduct shopProduct) {
        shopProduct.setProdId(prodId);
        shopProduct.setShopId(shopId);
        shopProduct.setStoragePercent(storagePercent);
        shopProduct.setAutoPutaway(autoPutaway);
        shopProduct.setSynStatus(synStatus);
        isAddLegal(shopProduct);
        Product product = productService.get(prodId);
        Platform platform = platformService.getById(platformId);
        shopProduct.setPlatformUrl(platform.getProdLinkPrefix());
        shopProduct.setPrice(product.getMarketPrice());
        Integer allAmount = storageService.findAmountByProdId(prodId);
        //如果自动同步库存，则计算出该店铺所需库存，并同步。
        if (shopProduct.getSynStatus() == true) {
            double storageNum = Math.ceil(allAmount / 100.00 * shopProduct.getStoragePercent());
            shopProduct.setStorageNum((int) storageNum);
        } else {
            shopProduct.setStorageNum(0);
        }
        if (shopProduct.isAutoPutaway() == true && allAmount > 0) {
            shopProduct.setPutaway(true);
        } else {
            shopProduct.setPutaway(false);
        }
    }

    /**
     * 判断添加条件是否合法
     *
     * @param shopProduct shopProduct具体信息
     * @return
     */
    private void isAddLegal(ShopProduct shopProduct) {
        if (shopProduct.getShopId() == null || shopProduct.getProdId() == null) {
            throw new StewardBusinessException("店铺和商品都不能为空，请选择");
        }
        Product product = productService.get(shopProduct.getProdId());
        //有相同平台和商品的不能添加
        if (isRepeat(shopProduct)) {
            throw new StewardBusinessException("该店铺已添加该商品:{"+ product.getName()+"}，请勿重复添加");
        }
        //所有库存占比总和大于100时不能添加
        if (isStoragePercentLegal(shopProduct) > 100) {
            throw new StewardBusinessException("商品:{"+product.getName()+"}总库存占比为:" + isStoragePercentLegal(shopProduct) + "%,超过100%，不能添加");
        }
    }

    /**
     * 判断添是否重复
     *
     * @param shopProduct shopProduct具体信息
     * @return
     */
    @Transactional(readOnly = true)
    private Boolean isRepeat(ShopProduct shopProduct) {
        Search search = new Search(ShopProduct.class);
        search.addFilterEqual("shopId", shopProduct.getShopId());
        search.addFilterEqual("prodId", shopProduct.getProdId());
        int count = generalDAO.count(search);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断新增(修改)商品的库存占比是否合法
     *
     * @param shopProduct shopProduct具体信息
     * @return
     */
    @Transactional(readOnly = true)
    private Integer isStoragePercentLegal(ShopProduct shopProduct) {
        String hql = "select sum(sp.storagePercent) from ShopProduct sp where sp.prodId = ? and sp.shopId != ?";
        List<Long> allStoragePercents = generalDAO.query(hql, null, new Object[]{shopProduct.getProdId(), shopProduct.getShopId()});
        if (allStoragePercents == null || allStoragePercents.size() == 0) {
            return shopProduct.getStoragePercent();
        }
        if (allStoragePercents.get(0) == null) {
            return shopProduct.getStoragePercent();
        }
        return (int) (allStoragePercents.get(0) + shopProduct.getStoragePercent());
    }

    /**
     * 更新shopProduct
     *
     * @param id             id
     * @param storageNum     店铺库存
     * @param storagePercent 库存占比
     * @param synStatus      是否自动同步
     * @param autoPutaway    是否自动上架
     * @return
     */
    public void update(Integer id, Integer storageNum, Integer storagePercent, Boolean synStatus, Boolean autoPutaway) {
        ShopProduct shopProduct = get(id);
        shopProduct.setStoragePercent(storagePercent);
        if (isStoragePercentLegal(shopProduct) > 100) {
            throw new StewardBusinessException("总库存占比为:" + isStoragePercentLegal(shopProduct) + "%,超过100%，不能修改");
        }
        shopProduct.setSynStatus(synStatus);
        shopProduct.setAutoPutaway(autoPutaway);
        shopProduct.setStorageNum(storageNum);
        generalDAO.saveOrUpdate(shopProduct);
    }

    /**
     * 查询指定商品明细
     *
     * @param id id
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, Object> searchDetail(Integer id) {
        ShopProduct shopProduct = get(id);
        Product product = productService.get(shopProduct.getProdId());
        Map<String, Object> res = productService.findDetail(product.getId());
        return res;
    }

//    /**
//     * 构造明细vo
//     *
//     * @param shopProduct ShopProduct信息
//     * @param shop        店铺信息
//     * @param product     商品信息
//     * @return
//     */
//    private ShopProductSearchVo buidSearchVo(ShopProduct shopProduct, Shop shop, Product product) {
//        ShopProductSearchVo shopProductSearchVo = new ShopProductSearchVo();
//        shopProductSearchVo.setId(shopProduct.getId());
//        shopProductSearchVo.setStoragePercent(shopProduct.getStoragePercent());
//        shopProductSearchVo.setAllAmount(storageService.findAmountByProdId(shopProduct.getProdId()));
//        shopProductSearchVo.setAutoPutaway(shopProduct.isAutoPutaway());
//        shopProductSearchVo.setBrand(product.getBrand().getName());
//        shopProductSearchVo.setColor(product.getColor());
//        shopProductSearchVo.setPlatform(shop.getPlatformType().getValue());        /
//        shopProductSearchVo.setProductName(product.getName());
//        shopProductSearchVo.setProductNo(product.getProductNo());
//        shopProductSearchVo.setShop(shop.getNick());
//        shopProductSearchVo.setSku(product.getSku());
//        shopProductSearchVo.setSpecification(product.getSpeci());
//        shopProductSearchVo.setSynStatus(shopProduct.getSynStatus());
//        return shopProductSearchVo;
//    }

    /**
     * 手动同步
     *
     * @param id id
     * @return
     */
    public void updateShopStorage(Integer id) throws ApiInvokeException {
        ShopProduct shopProduct = get(id);
        ProductInvokeWrapper productInvokeWrapper = new ProductInvokeWrapper(shopProduct.getProdId(), shopProduct.getShopId());
        productInvokeWrapper.updateShopStorage();
    }


}
