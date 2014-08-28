package com.ejushang.steward.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * web层使用的Json结果集
 * User: amos
 * Date: 13-12-22
 * Time: 上午9:46
 */
public class JsonResult {



    /**
     * 返回结果集为数据的key
     */
    private static final String RESULT_TYPE_LIST = "list" ;
    /**
     * 返回结果集的类型（单个对象）的key.
     */
    private static final String RESULT_TYPE_SINGLE_OBJECT = "obj" ;



    /**
     * 是否成功
     */
    private boolean success = false;

    /**
     * 消息
     */
    private String msg = "";


    /**
     * 数据
     */
    private Map<String, Object> data = new HashMap<String, Object>();


    /**
     * 以成功标志来构造
     *
     * @param success
     */
    public JsonResult(boolean success) {
        this.success = success;
    }

    /**
     * 以成功标志和消息来构造
     *
     * @param success
     * @param msg
     */
    public JsonResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    /**
     * 返回json对象
     *
     * @return
     */
    public String toJson() {
        return JsonUtil.object2Json(this);
    }

    /**
     * 返回json对象
     *
     * @return
     */
    public Map toMap() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("success", success);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

    /**
     * 直接写入客户端
     *
     * @param response
     * @throws java.io.IOException
     */
    public void writeToResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(toJson());
    }

    /**
     * 加入数据项
     *
     * @param name
     * @param value
     */
    public JsonResult addData(String name, Object value) {
        data.put(name, value);
        return this;
    }

    /**
     * 加入数据项
     *
     * @param value
     */
    public JsonResult addObject(Object value) {
        data.put(RESULT_TYPE_SINGLE_OBJECT, value);
        return this;
    }

    /**
     * 加入数据项
     *
     * @param list
     */
    public JsonResult addList(Collection<?> list) {
        data.put(RESULT_TYPE_LIST, list);
        return this;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public boolean isSuccess() {
        return success;
    }


    public String getMsg() {
        return msg;
    }


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
