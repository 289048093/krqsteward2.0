package com.ejushang.steward.ordercenter;

import com.ejushang.steward.common.util.Money;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-4-2
 */
public class MoneyTest {

    @Test
    public void test() {

//        Money money = Money.valueOf(123.375);
//        assertThat(money.getAmount(), is(123.38));
//        assertThat(money.getCent(), is(12338L));
//        assertThat(money.equals(Money.valueOfCent(12338L)), is(true));
//        assertThat(money.equals(Money.valueOf(123, 38)), is(true));
//
//        money = Money.valueOf(123.374);
//        assertThat(money.getCent(), is(12337L));
//
//        money = Money.valueOf(123.37);
//        assertThat(money.getAmount(), is(123.37));
//        assertThat(money.getCent(), is(12337L));

        Money money = Money.valueOf(123.375);
        Money money2 = Money.valueOf(10);

        assertThat(money.divide(money2).getAmount(), is(12.34d));

        System.out.println(money.toString());
    }

    @Test
    public void testMultiplyWorkCorrect() {
        assertThat(Money.valueOf(0.12d).multiply(0.12d).getAmount(), is(0.01d));
        assertThat(Money.valueOf(12d).multiply(12d).getAmount(), is(144d));
        assertThat(Money.valueOf(12d).multiply(0.12d).getAmount(), is(1.44d));
        assertThat(Money.valueOf(1.2d).multiply(1.2d).getAmount(), is(1.44d));
        assertThat(Money.valueOf(12d).multiply(1d).getAmount(), is(12d));
        assertThat(Money.valueOf(12d).multiply(0.1d).getAmount(), is(1.2d));
        assertThat(Money.valueOf(12d).multiply(0.01d).getAmount(), is(0.12d));
        assertThat(Money.valueOf(12d).multiply(0.001d).getAmount(), is(0.01d));
        assertThat(Money.valueOf(12d).multiply(0.003d).getAmount(), is(0.04d));
    }

    @Test
    public void testAddWorkCorrect() {
        assertThat(Money.valueOf(0.12d).add(Money.valueOf(0.12d)).getAmount(), is(0.24d));
        assertThat(Money.valueOf(12d).add(Money.valueOf(12d)).getAmount(), is(24d));
        assertThat(Money.valueOf(12d).add(Money.valueOf(0.12d)).getAmount(), is(12.12d));
        assertThat(Money.valueOf(1.2d).add(Money.valueOf(1.2d)).getAmount(), is(2.4d));
        assertThat(Money.valueOf(12d).add(Money.valueOf(1d)).getAmount(), is(13d));
        assertThat(Money.valueOf(12d).add(Money.valueOf(0.1d)).getAmount(), is(12.1d));
        assertThat(Money.valueOf(12d).add(Money.valueOf(0.01d)).getAmount(), is(12.01d));
        assertThat(Money.valueOf(12d).add(Money.valueOf(0.001d)).getAmount(), is(12d));
    }


}
