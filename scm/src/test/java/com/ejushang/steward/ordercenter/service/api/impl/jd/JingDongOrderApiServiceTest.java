package com.ejushang.steward.ordercenter.service.api.impl.jd;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * User: Baron.Zhang
 * Date: 2014/7/23
 * Time: 15:45
 */
public class JingDongOrderApiServiceTest extends BaseTest {

    @Test
    public void testMoney(){
        Money itemPaymentFee = Money.valueOfCent(5990L);
        Money orderPaymentFee = Money.valueOfCent(17970L);
        Money paymentPercent = itemPaymentFee.divide(orderPaymentFee);
        Money discountFee = Money.valueOf(5);

        BigDecimal paymentPercentBig = new BigDecimal(itemPaymentFee.getCent()).divide(new BigDecimal(orderPaymentFee.getCent()),4,BigDecimal.ROUND_HALF_UP);
        System.out.println(paymentPercentBig);

        BigDecimal partMjzDiscountFeeBig = paymentPercentBig.multiply(new BigDecimal(discountFee.getAmount()));
        System.out.println(partMjzDiscountFeeBig);

        Money partMjzDiscountFee = Money.valueOf(partMjzDiscountFeeBig.doubleValue());
        System.out.println(partMjzDiscountFee.getAmount() + ":" +partMjzDiscountFee.getCent());

       /* System.out.println(paymentPercent.getCent());

        discountFee = Money.valueOf(5);
        Money  partMjzDiscountFee = paymentPercent.multiply(discountFee.getAmount());
        System.out.println(partMjzDiscountFee.getCent());*/
    }
}
