package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-10
 */

public abstract class BaseAnalyzeTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ConfService confService;

    @Autowired
    private OrderAnalyzeTestService orderAnalyzeTestService;

    protected Conf postageProductSkuConf;

    protected Conf serviceProductSkuConf;

    protected void init() {
        postageProductSkuConf = confService.getConfByName(SystemConfConstant.POSTAGE_PRODUCT_SKU);
        if(StringUtils.isBlank(postageProductSkuConf.getValue())) {
            Product product = orderAnalyzeTestService.initProduct(null, null);
            postageProductSkuConf.setValue(product.getSku());
            confService.save(postageProductSkuConf);
        }

        serviceProductSkuConf = confService.getConfByName(SystemConfConstant.SERVICE_PRODUCT_SKU);
        if(StringUtils.isBlank(serviceProductSkuConf.getValue())) {
            Product product = orderAnalyzeTestService.initProduct(null, null);
            serviceProductSkuConf.setValue(product.getSku());
            confService.save(serviceProductSkuConf);
        }

    }

}
