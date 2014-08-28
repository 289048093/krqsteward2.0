package com.ejushang.steward.scm.task;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.ordercenter.constant.ProductCenterScheduleType;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.ProductCenterScheduleData;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.domain.Storage;
import com.ejushang.steward.ordercenter.service.ProductPlatformService;
import com.ejushang.steward.ordercenter.service.StorageService;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvoke;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvokeWrapper;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.incoming.ProductLoadAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-6-3
 * Time: 下午1:18
 */
@Component
public class ProductSyncOutPlatformSchedule {

    private static final Logger log = LoggerFactory.getLogger(ProductSyncOutPlatformSchedule.class);

    @Autowired
    private ProductPlatformService productPlatformService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private ProductLoadAPI productLoadAPI;


    //    @Scheduled(cron = "0 0 0 * * ?")
    public void syncOutPlatformData() {
        List<ShopProduct> shopProducts = generalDAO.search(new Search(ShopProduct.class).addFilterEqual("synStatus", true));
        for (ShopProduct shopProduct : shopProducts) {
            if (shopProduct.getSynStatus()) {
                try {
                    new ProductInvokeWrapper(shopProduct).updateShopStorage();
                } catch (ApiInvokeException e) {
                    log.info("每天定时同步产品库存到外部平台出错:" + e.getMessage(), e);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void loadProductCenterData() {

        ProductCenterScheduleData data = (ProductCenterScheduleData) generalDAO.searchUnique(new Search(ProductCenterScheduleData.class)
                .addFilterEqual("type", ProductCenterScheduleType.LOAD_PRODUCT_TYPE));
        Date prodStartTime = data.getLastLoadDate();
        Date prodEndTime = new Date();
        try {
            productLoadAPI.loadData(prodStartTime, prodEndTime);
        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            if (e.getCause() instanceof SocketTimeoutException) {  //网络异常直接返回，下次重新获取
                return;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        data.setLastLoadDate(prodEndTime);
        generalDAO.saveOrUpdate(data);
    }
}
