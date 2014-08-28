package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.ordercenter.vo.logistics.PutMsg;
import com.ejushang.steward.ordercenter.vo.logistics.ResponseMsg;
import com.ejushang.uams.client.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 请求物流配送信息
 *
 * User: Shiro
 * Date: 14-8-1
 * Time: 上午11:19
 */
public class RequestLogisticsInfo {
    private static final Logger log = LoggerFactory.getLogger(RequestLogisticsInfo.class);


    /** 第三方物流查询的请求地址 */
    private String thirdUrl;

    /** 与第三方物流查询平台协商的用于校验用户的 key */
    private String key;

    /** 回调地址 */
    private String callBackUrl;

    /** 请求及响应的数据模式, json 或是 xml, 默认是 json */
    private String schema = "json";

    /** 请求的超时时间(单位, 毫秒. 1 秒则 1 * 1000) */
    private static final int TIME_OUT = 10 * 1000;

    private static final String ENCODE = "UTF-8";

    private boolean online = false;

    /**
     * 向 第三方物流查询平台(快递100) 发送请求. 响应异步返回数据
     *
     * @param company 快递公司
     * @param number  物流单号
     * @param to      到哪去
     * @return 收到成功信息则返回 true.
     */
    public boolean requestThirdLogistics(String company, String number, String to) {
        if (!online) return false;

       // Assert.notNull(callBackUrl, "第三方物流推送给我方的地址必须设置!");

        PutMsg put = new PutMsg();
        put.setCompany(company);
        put.setNumber(number);
        put.setFrom(StringUtils.EMPTY);
        put.setTo(StringUtils.isBlank(to) ? StringUtils.EMPTY : to);
        put.setKey(key);

        Map<String, String> map = new HashMap<String, String>();
        map.put("callbackurl", callBackUrl);
        put.setParameters(map);

        // post 数据.
        String putMsg;
        try {
            putMsg = JsonUtil.objectToJson(put);
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error("转换请求数据时异常", e);

            return false;
        }

        ResponseMsg res = null;
        try {
            String responseMsg = post(putMsg);
            if (StringUtils.isNotBlank(responseMsg))
                res = JsonUtil.json2Object(responseMsg, ResponseMsg.class);
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error("接收三方物流查询平台时异常", e);

            return false;
        }

        if (res == null) {
            if (log.isWarnEnabled())
                log.warn("第三方物流查询平台未返回数据!");

            return false;
        }
        if (res.getReturnCode() != 200) {
            if (log.isWarnEnabled())
                log.warn("第三方物流查询平台未返回<成功>数据");

            return false;
        }
        return true;
    }

    /**
     * 请求数据.
     *
     * @param msg 请求的 json 数据
     * @return 返回的 json 数据, 若异常则返回空
     */
    private String post(String msg) throws Exception {
        StringBuilder sbd = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(thirdUrl).openConnection();
            // 超时时间
            conn.setConnectTimeout(TIME_OUT);
            // method
            conn.setRequestMethod("POST");
            // 忽略缓存
            conn.setUseCaches(false);
            // 可以输出
            conn.setDoOutput(true);

            conn.connect();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            // 传递数据.
            String content = "schema=" + getSchema() + "&param=" + msg;
            out.write(content.getBytes(ENCODE));
            out.flush();
            out.close();

            if (log.isWarnEnabled())
                log.warn("向第三方物流平台发送请求: [" + thirdUrl + "], 数据是: [" + content + "].");

            // 读取返回数据, 在 getInputStream 时才会真正进行数据的请求
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODE));
            String line;
            while ((line = br.readLine()) != null) {
                sbd.append(line);
            }

            if (log.isWarnEnabled())
                log.warn("第三方物流平台即时返回的数据是: " + sbd.toString());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return sbd.toString();
    }

    public String getThirdUrl() {
        return thirdUrl;
    }

    public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}

