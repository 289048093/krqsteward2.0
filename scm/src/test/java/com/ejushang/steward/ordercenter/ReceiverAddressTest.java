package com.ejushang.steward.ordercenter;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.util.Money;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-4-2
 */
public class ReceiverAddressTest {

    /**
     * 修改地址方法已变,直接把省市县加到最前面
     */
    @Test
    @Ignore
    public void test1() {
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "河南省枣县赵庄", "河南省枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "河南省南阳枣县赵庄", "河南省南阳枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "河南枣县赵庄", "河南省南阳市康家沟河南枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳市枣县赵庄", "河南省南阳市枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳市康家沟枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "康家沟枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳市康家沟枣县赵庄", "河南省南阳市康家沟枣县赵庄");

    }


    @Test
    public void test2() {
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "河南省枣县赵庄", "河南省南阳市康家沟河南省枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "河南省南阳枣县赵庄", "河南省南阳市康家沟河南省南阳枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "河南枣县赵庄", "河南省南阳市康家沟河南枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳市枣县赵庄", "河南省南阳市枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳市康家沟枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "康家沟枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳市康家沟枣县赵庄", "河南省南阳市康家沟枣县赵庄");
        assertCopyAdreessCorrect("河南省", "南阳市", "康家沟", "南阳康家枣县赵庄", "河南省南阳市康家沟南阳康家枣县赵庄");

    }


    private void assertCopyAdreessCorrect(String state, String city, String district, String address, String expectedAddress) {

        Receiver receiver = new Receiver();
        receiver.setReceiverState(state);
        receiver.setReceiverCity(city);
        receiver.setReceiverDistrict(district);
        receiver.setReceiverAddress(address);
        receiver.copyAreaToAddress();
        assertThat(receiver.getReceiverAddress(), is(expectedAddress));


    }


}
