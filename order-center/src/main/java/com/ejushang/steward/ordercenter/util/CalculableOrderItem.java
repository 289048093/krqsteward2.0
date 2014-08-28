package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.PlatformType;

/**
 * User: liubin
 * Date: 14-7-30
 */
public interface CalculableOrderItem {

    public void setBuyCount(Integer buyCount);

    public Integer getBuyCount();

    public void setPrice(Money price);

    public Money getPrice();

    public void setActualFee(Money actualFee);

    public Money getActualFee();

    public void setGoodsFee(Money goodsFee);

    public Money getGoodsFee();

    public void setDiscountFee(Money discountFee);

    public Money getDiscountFee();

    public void setSharedDiscountFee(Money sharedDiscountFee);

    public Money getSharedDiscountFee();

    public void setSharedPostFee(Money sharedPostFee);

    public Money getSharedPostFee();

    public void setPlatformType(PlatformType platformType);

    public PlatformType getPlatformType();

    public void setRefundFee(Money refundFee);

    public Money getRefundFee();

    public void setActualRefundFee(Money actualRefundFee);

    public Money getActualRefundFee();

    public void setOfflineRefundFee(Money offlineRefundFee);

    public Money getOfflineRefundFee();

    public void setServiceCoverFee(Money serviceCoverFee);

    public Money getServiceCoverFee();

    public void setServiceCoverRefundFee(Money serviceCoverRefundFee);

    public Money getServiceCoverRefundFee();

}
