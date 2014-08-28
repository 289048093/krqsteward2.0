package com.ejushang.steward.openapicenter.zy.api.aceess;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.parser.ObjectJsonParser;
import com.ejushang.steward.openapicenter.zy.api.aceess.parser.ObjectXmlParser;
import com.ejushang.steward.openapicenter.zy.api.aceess.parser.ZiYouParser;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.RequestParametersHolder;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.ZiYouRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.ZiYouUploadRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 2014/8/4
 * Time: 15:05
 */
public class DefaultZiYouClient implements ZiYouClient {

   /* private static final String APP_KEY = "app_key";
    private static final String URL = "url";
    private static final String METHOD_NAME = "method_name";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String SIGN = "sign";
    private static final String TIMESTAMP = "timestamp";
    private static final String RANDOM_VAL = "random_val";
    private static final String PARAM_JSON = "param_json";*/

    private static final int MAX_RANDOM_VAL = 99999999;
    private static final int MIN_RANDOM_VAL = 10000;

    private String serverUrl;
    private String appKey;
    private String appSecret;
    private String accessToken;

    private String format = Constants.FORMAT_JSON;
    private String signMethod = Constants.SIGN_METHOD_MD5;



    private int connectTimeout = 3000;//3秒
    private int readTimeout = 15000;//15秒

    private boolean needCheckRequest = true;
    private boolean needEnableParser = true;


    public DefaultZiYouClient(String serverUrl,String appKey,String appSecret,String accessToken){
        if(serverUrl == null || serverUrl.equals("")){
            throw new NullPointerException("参数serverUrl不能为空");
        }
        if(appKey == null || appKey.equals("")){
            throw new NullPointerException("参数appKey不能为空");
        }
        if(appSecret == null || appSecret.equals("")){
            throw new NullPointerException("参数appSecret不能为空");
        }
        if(accessToken == null || accessToken.equals("")){
            throw new NullPointerException("参数accessToken不能为空");
        }
        this.serverUrl = serverUrl;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.accessToken = accessToken;
    }

    public DefaultZiYouClient(String serverUrl,String appKey,String appSecret,String accessToken,String format){
        this(serverUrl,appKey,appSecret,accessToken);
        if(format == null || format.equals("")){
            throw new NullPointerException("参数format不能为空");
        }
        this.format = format;
    }

    public DefaultZiYouClient(String serverUrl,String appKey,String appSecret,String accessToken,String format,int connectTimeout, int readTimeout){
        this(serverUrl,appKey,appSecret,accessToken,format);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public DefaultZiYouClient(String serverUrl,String appKey,String appSecret,String accessToken,String format,int connectTimeout, int readTimeout,String signMethod){
        this(serverUrl,appKey,appSecret,accessToken,format,connectTimeout,readTimeout);
        if(signMethod == null || signMethod.equals("")){
            throw new NullPointerException("参数signMethod不能为空");
        }
        this.signMethod = signMethod;
    }

    /**
     * 执行
     * @param paramJson
     * @return
     */
    public String execute(String methodName,String paramJson) throws Exception {
        String randomVal = getRandomVal();
        String timestamp = getTimestamp();
        String sign = getSign(this.appSecret,paramJson,this.accessToken,this.appKey,methodName,timestamp,randomVal);
        // 构造提交参数
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair(METHOD_NAME,methodName));
        nameValuePairList.add(new BasicNameValuePair(ACCESS_TOKEN,this.accessToken));
        nameValuePairList.add(new BasicNameValuePair(APP_KEY, this.appKey));
        nameValuePairList.add(new BasicNameValuePair(SIGN,sign));
        nameValuePairList.add(new BasicNameValuePair(TIMESTAMP,timestamp));
        nameValuePairList.add(new BasicNameValuePair(RANDOM_VAL,randomVal));
        nameValuePairList.add(new BasicNameValuePair(PARAM_JSON,paramJson));

        UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, Constants.CHARSET_UTF8);
        HttpPost httpPost = new HttpPost(this.serverUrl);
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
     * 获取时间戳
     * @return
     */
    public static String getTimestamp(){
        return DateUtils.formatDate(DateUtils.getCurrentDate(), DateUtils.DateFormatType.DATE_FORMAT_STR);
    }

    /**
     * 获取随机数 10000~99999999
     * @return
     */
    public static String getRandomVal(){
        Random random = new Random();
        Integer randomNum = random.nextInt(MAX_RANDOM_VAL-MIN_RANDOM_VAL) + MIN_RANDOM_VAL;
        return String.valueOf(randomNum);
    }

    /**
     * ① 所有请求参数按照字母先后顺序排列
     * 例如：将param_json,access_token,app_key,url,timestamp,random_val 排序为access_token,app_key,param_json,random_val,timestamp,url
     * ② 把所有参数名和参数值进行拼装
     * 例如：access_tokenxxxapp_keyxxxmethodxxxxxxtimestampxxxxxxvx
     * ③ 把appSecret夹在字符串的两端
     * 例如：appSecret+XXXX+appSecret
     * ④ 使用MD5进行加密，再转化成大写
     *
     * @return
     */
    private String getSign(String appSecret,String paramJson,String accessToken,String appKey,String methodName,String timestamp,String randomVal){
        StringBuffer stringBuffer = new StringBuffer(appSecret);

        if(!StringUtils.isBlank(accessToken)){
            stringBuffer.append(ACCESS_TOKEN).append(accessToken);
        }
        if(!StringUtils.isBlank(appKey)){
            stringBuffer.append(APP_KEY).append(appKey);
        }
        if(!StringUtils.isBlank(methodName)){
            stringBuffer.append(METHOD_NAME).append(methodName);
        }
        if(!StringUtils.isBlank(paramJson)){
            stringBuffer.append(PARAM_JSON).append(paramJson);
        }
        if(!StringUtils.isBlank(randomVal)){
            stringBuffer.append(RANDOM_VAL).append(randomVal);
        }
        if(!StringUtils.isBlank(timestamp)){
            stringBuffer.append(TIMESTAMP).append(timestamp);
        }
        stringBuffer.append(appSecret);

        return EncryptUtil.md5(stringBuffer.toString()).toUpperCase();
    }


    @Override
    public <T extends ZiYouResponse> T excute(ZiYouRequest<T> request) throws ApiException {
        return excute(request,null);
    }

    @Override
    public <T extends ZiYouResponse> T excute(ZiYouRequest<T> request, String session) throws ApiException {
        ZiYouParser<T> parser = null;
        if(this.needEnableParser){
            if(Constants.FORMAT_XML.equalsIgnoreCase(this.format)){
                parser = new ObjectXmlParser<T>(request.getResponseClass());
            }
            else if(Constants.FORMAT_JSON.equalsIgnoreCase(this.format)){
                parser = new ObjectJsonParser<T>(request.getResponseClass());
            }
        }

        return _excute(request,parser,session);
    }

    private <T extends ZiYouResponse> T _excute(ZiYouRequest<T> request, ZiYouParser<T> parser, String session) throws ApiException {
        if(this.needCheckRequest){
            try {
                request.check();
            } catch (ApiRuleException e) {
                T localResponse = null;

                try {
                    localResponse = request.getResponseClass().newInstance();
                } catch (InstantiationException e2) {
                    throw new ApiException(e2);
                } catch (IllegalAccessException e3) {
                    throw new ApiException(e3);
                }

                localResponse.setErrorCode(e.getErrCode());
                localResponse.setMsg(e.getErrMsg());
                return localResponse;
            }
        }

        // 发起请求，并获得返回结果
        Map<String,Object> rt = doPost(request,session);
        if(rt == null){
            return null;
        }

        T tRsp = null;

        if (this.needEnableParser) {
            try {
                tRsp = parser.parse((String) rt.get("rsp"));
                tRsp.setBody((String) rt.get("rsp"));
            } catch (RuntimeException e) {
                ZiYouLogger.logBizError((String) rt.get("rsp"));
                throw e;
            }
        } else {
            try {
                tRsp = request.getResponseClass().newInstance();
                tRsp.setBody((String) rt.get("rsp"));
            } catch (Exception e) {
            }
        }

        tRsp.setParams((ZiYouHashMap) rt.get("textParams"));
        if (!tRsp.isSuccess()) {
            ZiYouLogger.logErrorScene(rt, tRsp, appSecret);
        }
        return tRsp;
    }

    public <T extends ZiYouResponse> Map<String,Object> doPost(ZiYouRequest<T> request,String session) throws ApiException{
        // 保存结果的map
        Map<String,Object> result = new HashMap<String, Object>();

        // 获取随机数
        String randomVal = getRandomVal();
        // 获取时间戳
        String timestamp = getTimestamp();

        ZiYouHashMap appParams = new ZiYouHashMap(request.getTextParams());
        // 获取业务参数
        String paramJson = JsonUtil.object2Json(appParams);
        // 获得sign
        String sign = getSign(this.appSecret,paramJson,session,this.appKey,request.getApiMethodName(),timestamp,randomVal);

        RequestParametersHolder requestParametersHolder = new RequestParametersHolder();
        // 添加业务参数
        requestParametersHolder.setApplicationParams(appParams);

        // 添加协议级请求参数
        ZiYouHashMap protocolMustParams = new ZiYouHashMap();
        protocolMustParams.put(METHOD_NAME,request.getApiMethodName());
        protocolMustParams.put(ACCESS_TOKEN,session);
        protocolMustParams.put(APP_KEY,this.appKey);
        protocolMustParams.put(SIGN,sign);
        protocolMustParams.put(TIMESTAMP,timestamp);
        protocolMustParams.put(RANDOM_VAL,randomVal);
        protocolMustParams.put(PARAM_JSON,paramJson);
        // 添加
        requestParametersHolder.setProtocalMustParams(protocolMustParams);

        ZiYouHashMap protocolOptParams = new ZiYouHashMap();
        // 添加
        requestParametersHolder.setProtocalOptParams(protocolOptParams);

        StringBuffer urlSb = new StringBuffer(serverUrl);

        try {
            String sysMustQuery = WebUtils.buildQuery(requestParametersHolder.getProtocalMustParams(), Constants.CHARSET_UTF8);
            String sysOptQuery = WebUtils.buildQuery(requestParametersHolder.getProtocalOptParams(), Constants.CHARSET_UTF8);

            urlSb.append("?");
            urlSb.append(sysMustQuery);
            if (sysOptQuery != null && sysOptQuery.length() > 0) {
                urlSb.append("&");
                urlSb.append(sysOptQuery);
            }
        } catch (IOException e) {
            throw new ApiException(e);
        }

        String rsp = null;
        try {
            // 是否需要上传文件
            if (request instanceof ZiYouUploadRequest) {
                ZiYouUploadRequest<T> uRequest = (ZiYouUploadRequest<T>) request;
                Map<String, FileItem> fileParams = ZiYouUtils.cleanupMap(uRequest.getFileParams());
                rsp = WebUtils.doPost(urlSb.toString(), appParams, fileParams,Constants.CHARSET_UTF8, connectTimeout, readTimeout,request.getHeaderMap());
            } else {
                rsp = WebUtils.doPost(urlSb.toString(), appParams,Constants.CHARSET_UTF8, connectTimeout, readTimeout,request.getHeaderMap());
            }
        } catch (IOException e) {
            throw new ApiException(e);
        }
        result.put("rsp", rsp);
        result.put("textParams", appParams);
        result.put("protocalMustParams", protocolMustParams);
        result.put("protocalOptParams", protocolOptParams);
        result.put("url", urlSb.toString());

        return result;
    }
}
