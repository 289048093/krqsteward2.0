package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.EncryptUtil;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.order.OrderGetRequest;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.request.order.OrderSopOutstorageRequest;
import com.jd.open.api.sdk.request.order.OrderSopWaybillUpdateRequest;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;
import com.jd.open.api.sdk.response.order.OrderSopOutstorageResponse;
import com.jd.open.api.sdk.response.order.OrderSopWaybillUpdateResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 京东订单api
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午4:48
 */
public class JdOrderApi {

    private JdClient client;

    public JdOrderApi(String sessionKey){
        client = new DefaultJdClient(ConstantJingDong.JD_API_URL,sessionKey,ConstantJingDong.JD_APP_KEY,
                ConstantJingDong.JD_APP_SECRET);
    }

    /**
     * 根据条件检索订单信息
     * @param argsMap
     * @return
     * @throws JdException
     */
    public OrderSearchResponse orderSearch(Map<String,Object> argsMap) throws Exception {
        OrderSearchRequest request = new OrderSearchRequest();
        ReflectUtils.executeMethods(request, argsMap);
        OrderSearchResponse response = client.execute(request);
        return response;
    }

    /**
     * 根据条件获取单个订单信息
     * {@link class http://help.jd.com/jos/question-569.html#A2}
     * @param argsMap
     * @return
     * @throws Exception
     */
    public OrderGetResponse orderGet(Map<String,Object> argsMap) throws Exception {
        OrderGetRequest request = new OrderGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        OrderGetResponse response=client.execute(request);
        return response;
    }

    /**
     * 根据订单id进行出库操作
     * @param argsMap
     * @return
     */
    public OrderSopOutstorageResponse orderSopOutstorage(Map<String,Object> argsMap) throws Exception {
        OrderSopOutstorageRequest request = new OrderSopOutstorageRequest();
        ReflectUtils.executeMethods(request,argsMap);
        OrderSopOutstorageResponse response = client.execute(request);
        return response;
    }

    /**
     * 根据订单id修改运单号
     * @param argsMap
     * @return
     */
    public OrderSopWaybillUpdateResponse orderSopWaybillUpdate(Map<String,Object> argsMap) throws Exception {
        OrderSopWaybillUpdateRequest request = new OrderSopWaybillUpdateRequest();
        ReflectUtils.executeMethods(request,argsMap);
        OrderSopWaybillUpdateResponse response=client.execute(request);
        return response;
    }

    /**
     * 不使用京东的SDK获取订单信息
     * @param argsMap
     * @return
     * @throws Exception
     */
    public String orderGetBySelf(Map<String,Object> argsMap,String sessionKey) throws Exception {
        String paramJson = JsonUtil.object2Json(argsMap);
        String method = "360buy.order.get";
        String timestamp = EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        String v = "2.0";
        // 构造提交参数
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("method",method));
        nameValuePairList.add(new BasicNameValuePair("access_token",sessionKey));
        nameValuePairList.add(new BasicNameValuePair("app_key",ConstantJingDong.JD_APP_KEY));
        String sign = getSign(ConstantJingDong.JD_APP_SECRET,paramJson,sessionKey,ConstantJingDong.JD_APP_KEY,method,timestamp,v);
        nameValuePairList.add(new BasicNameValuePair("sign",sign));
        nameValuePairList.add(new BasicNameValuePair("timestamp",timestamp));
        nameValuePairList.add(new BasicNameValuePair("v",v));
        nameValuePairList.add(new BasicNameValuePair("360buy_param_json",paramJson));

        UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, Constants.CHARSET_UTF8);
        HttpPost httpPost = new HttpPost(ConstantJingDong.JD_API_URL);
        httpPost.setEntity(encodedFormEntity);

        // post提交获取access token
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 创建保存返回内容的buffer对象
        StringBuffer content = new StringBuffer();
        try{
            HttpEntity httpEntity = response.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
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
        String con = URLDecoder.decode(content.toString(), Constants.CHARSET_UTF8);
        //Map<String,Object> map = JsonUtil.json2Object(con, HashMap.class);
        return con;
    }

    /**
     * ① 所有请求参数按照字母先后顺序排列
     * 例如：将360buy_param_json,access_token,app_key,method,timestamp,v 排序为access_token,app_key,method,timestamp,v
     * ② 把所有参数名和参数值进行拼装
     * 例如：access_tokenxxxapp_keyxxxmethodxxxxxxtimestampxxxxxxvx
     * ③ 把appSecret夹在字符串的两端
     * 例如：appSecret+XXXX+appSecret
     * ④ 使用MD5进行加密，再转化成大写
     *
     * @return
     */
    private String getSign(String appSecret,String paramJson,String sessionKey,String appKey,String method,String timestamp,String v){
        StringBuffer stringBuffer = new StringBuffer(appSecret);
        if(!StringUtils.isBlank(paramJson)){
            stringBuffer.append("360buy_param_json").append(paramJson);
        }
        if(!StringUtils.isBlank(sessionKey)){
            stringBuffer.append("access_token").append(sessionKey);
        }
        if(!StringUtils.isBlank(appKey)){
            stringBuffer.append("app_key").append(appKey);
        }
        if(!StringUtils.isBlank(method)){
            stringBuffer.append("method").append(method);
        }
        if(!StringUtils.isBlank(timestamp)){
            stringBuffer.append("timestamp").append(timestamp);
        }
        if(!StringUtils.isBlank(v)){
            stringBuffer.append("v").append(v);
        }
        stringBuffer.append(appSecret);

        return EncryptUtil.md5(stringBuffer.toString()).toUpperCase();
    }

}
