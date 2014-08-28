package com.ejushang.steward.scm.web.api;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.ordercenter.bean.ShopAndAuthBean;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.service.api.ShopBeanService;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongOrderApiService;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongShopApiService;
import com.ejushang.steward.ordercenter.util.BeanUtils;
import com.ejushang.steward.scm.util.DefaultTrustManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 14-4-19
 * Time: 下午1:39
 */
@Controller
@RequestMapping("/jd")
public class JingDongController {

    private static final Logger log = LoggerFactory.getLogger(JingDongController.class);

    private static final String INDEX = "/";

    @Autowired
    private JingDongShopApiService jingDongShopApiService;

    @Autowired
    private JingDongOrderApiService jingDongOrderApiService;

    @Autowired
    private ShopBeanService shopBeanService;

    /**
     * 进入京东授权页面
     * @param request
     * @return
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    @RequestMapping("/authorize")
    public String authorize(HttpServletRequest request) throws IOException, URISyntaxException {
        if(log.isInfoEnabled()){
            log.info("京东授权：进入京东授权页面：：：：：：");
        }

        URI uri = new URIBuilder()
                .setPath(ConstantJingDong.JD_AUTHORIZE_URL)
                .setParameter("client_id",ConstantJingDong.JD_APP_KEY)
                .setParameter("response_type", "code")
                .setParameter("redirect_uri", ConstantJingDong.JD_REDIRECT_URL)
                .build();

        return "redirect:"+uri.toURL();
    }

    /**
     * 供top回调，获取token
     * @param code
     */
    @RequestMapping("/callback")
    public String callback(String code) throws Exception {

        if(log.isInfoEnabled()){
            log.info("京东授权：获取京东用户Access Token：：：：：：");
        }

        if(log.isInfoEnabled()){
            log.info("京东授权：TOP返回CODE：" + code);
        }

        // 使用HttpClient4.3访问https时，进行证书环境设置
        DefaultTrustManager[] defaultTrustManagers = new DefaultTrustManager[1];
        defaultTrustManagers[0] = new DefaultTrustManager();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(new KeyManager[0],defaultTrustManagers,new SecureRandom());
        SSLContext.setDefault(sslContext);

        sslContext.init(null,defaultTrustManagers,null);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // 构造提交参数
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("client_id",ConstantJingDong.JD_APP_KEY));
        nameValuePairList.add(new BasicNameValuePair("client_secret",ConstantJingDong.JD_APP_SECRET));
        nameValuePairList.add(new BasicNameValuePair("grant_type","authorization_code"));
        nameValuePairList.add(new BasicNameValuePair("code",code));
        nameValuePairList.add(new BasicNameValuePair("redirect_uri",ConstantJingDong.JD_REDIRECT_URL));

        UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, Constants.CHARSET_UTF8);
        HttpPost httpPost = new HttpPost(ConstantJingDong.JD_TOKEN_URL);
        httpPost.setEntity(encodedFormEntity);

        // post提交获取access token
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 创建保存返回内容的buffer对象
        StringBuffer content = new StringBuffer();
        try{
            HttpEntity httpEntity = response.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent(),Constants.CHARSET_GBK));
            String line = null;
            while((line=br.readLine()) != null){
                content.append(line);
            }
        }catch (Exception e){
            throw e;
        }finally {
            response.close();
        }

        // 转义content
        String con = URLDecoder.decode(content.toString(), Constants.CHARSET_GBK);
        Map<String,Object> map = JsonUtil.json2Object(con, HashMap.class);
        if(log.isInfoEnabled()){
            log.info("京东授权：授权返回信息解码前{}", content.toString());
            log.info("京东授权：授权返回信息解码后{}，获取accessToken : {}", con, map.get("access_token"));
        }

        ShopAndAuthBean shopAndAuthBean = new ShopAndAuthBean();
        shopAndAuthBean.setAccessToken(String.valueOf(map.get("access_token")));
        shopAndAuthBean.setTokenType(String.valueOf(map.get("token_type")));
        shopAndAuthBean.setExpiresIn(String.valueOf(map.get("expires_in")));
        shopAndAuthBean.setRefreshToken(String.valueOf(map.get("refresh_token")));
        shopAndAuthBean.setReExpiresIn(String.valueOf(map.get("time")));
        shopAndAuthBean.setUserId(String.valueOf(map.get("uid")));
        shopAndAuthBean.setUserNick(String.valueOf(map.get("user_nick")));
        shopAndAuthBean.setPlatformType(PlatformType.JING_DONG);

        jingDongShopApiService.addShopAndAuth(shopAndAuthBean);

        return "redirect:"+INDEX;
    }

    @RequestMapping("/fetchOrder")
    public String fetchOrder(String platformOrderNos,String sid,HttpServletResponse response) throws Exception {
        if(StringUtils.isBlank(platformOrderNos)){
            throw new StewardBusinessException("请求时必须要platformOrderNos不能为空，多个订单号以\",\"分隔。");
        }

        if(StringUtils.isBlank(sid)){
            throw new StewardBusinessException("请求时必须要sid不能为空");
        }

        String[] ids = platformOrderNos.split(",");

        Integer shopId = Integer.valueOf(sid);

        // 获取所有店铺信息
        ShopBean shopBeanQuery = new ShopBean();
        shopBeanQuery.setShopId(shopId);
        List<ShopBean> shopBeanList = shopBeanService.findShopBean(shopBeanQuery);
        ShopBean shopBeanOri = CollectionUtils.isNotEmpty(shopBeanList) ? shopBeanList.get(0) : null;

        if(shopBeanOri == null){
            throw new StewardBusinessException("【shopId="+shopId+"】没有对应的店铺");
        }

        List<OriginalOrder> originalOrderList = jingDongOrderApiService.fetchOrdersByIds(shopBeanOri, Arrays.asList(ids));

        StringBuffer stringBuffer = new StringBuffer(shopBeanOri+"抓取以下订单成功：\r\n");
        for(OriginalOrder originalOrder : originalOrderList){
            stringBuffer.append(originalOrder.getPlatformOrderNo()+"\r\n");
        }

        PrintWriter out = response.getWriter();
        out.println(stringBuffer.toString());
        out.flush();
        return null;
    }
}
