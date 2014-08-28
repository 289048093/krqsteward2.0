package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.seller.SellerVenderInfoGetRequest;
import com.jd.open.api.sdk.request.seller.VenderShopQueryRequest;
import com.jd.open.api.sdk.response.seller.SellerVenderInfoGetResponse;
import com.jd.open.api.sdk.response.seller.VenderShopQueryResponse;

/**
 * 京东店铺api
 * User: Baron.Zhang
 * Date: 14-4-19
 * Time: 下午2:27
 */
public class JdShopApi {
    private JdClient client;

    public JdShopApi(String sessionKey){
        client = new DefaultJdClient(ConstantJingDong.JD_API_URL,sessionKey,ConstantJingDong.JD_APP_KEY,
                ConstantJingDong.JD_APP_SECRET);
    }

    /**
     *  查询商家基本信息，包括商家编号、商家类型、店铺编号、店铺名称、主营类目编号。
     * @return
     * @throws JdException
     */
    public SellerVenderInfoGetResponse sellerVenderInfoGet() throws JdException {
        SellerVenderInfoGetRequest request=new SellerVenderInfoGetRequest();
        request.setExtJsonParam("");
        SellerVenderInfoGetResponse response=client.execute(request);
        return response;
    }

    /**
     * 查询商家店铺基本信息，包括商家编号，店铺编号，店铺名称，开店时间，logoUrl，店铺简介，主营类目编号，主营类目名称
     * @return
     * @throws JdException
     */
    public VenderShopQueryResponse venderShopQuery() throws JdException {
        VenderShopQueryRequest request=new VenderShopQueryRequest();
        VenderShopQueryResponse response=client.execute(request);
        return response;
    }
}
