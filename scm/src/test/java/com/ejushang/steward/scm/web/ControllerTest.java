package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.PrintingResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @Author Channel
 * @Date 2014/8/12
 * @Version: 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:spring-*.xml", "classpath*:mvc-*.xml"})
@TransactionConfiguration(defaultRollback = true)
//@TransactionConfiguration(transactionManager="transactionManager")
//@Transactional
public class ControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private static MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private static MockHttpServletRequestBuilder fill(MockHttpServletRequestBuilder builder, Object obj) throws Exception {
        return fill(builder, obj, "");
    }

    private static MockHttpServletRequestBuilder fill(MockHttpServletRequestBuilder builder, Object obj, String prefix) throws Exception {
        if (obj instanceof Map) {
            Set<Map.Entry> set = ((Map) obj).entrySet();
            for (Map.Entry e : set) {
                Object name = e.getKey();
                Object value = e.getValue();
                if (value == null || value instanceof Class) continue;
                if (value instanceof Collection) {
                    Iterator iterator = ((Collection) value).iterator();
                    int idx = 0;
                    while (iterator.hasNext()) {
                        fill(builder, iterator.next(), prefix + name + "[" + idx + "].");
                        idx++;
                    }
                } else if (value instanceof Map) {
                    Map map = ((Map) value);
                    fill(builder, value, prefix + name + ".");
                } else {
                    builder.param(prefix + name, value.toString());
                }
            }
        } else {
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                Object value = PropertyUtils.getSimpleProperty(obj, name);
                if (value == null || value instanceof Class) continue;
                if (value instanceof Collection) {
                    Iterator iterator = ((Collection) value).iterator();
                    int idx = 0;
                    while (iterator.hasNext()) {
                        fill(builder, iterator.next(), prefix + name + "[" + idx + "].");
                        idx++;
                    }
                } else {
                    String packageName = value.getClass().getPackage().getName();
                    if (packageName.equals("com.ejushang.steward.ordercenter.vo")) {
                        fill(builder, value, prefix + name + ".");
                    } else {
                        builder.param(prefix + name, value.toString());
                    }
                }
            }
        }
        return builder;
    }

    static Object jsonValue(Object data, String key) {
        String[] split = key.split("\\.");
        Object obj = data;
        for (String s : split) {
            if (obj instanceof Map) {
                obj = ((Map) obj).get(s);
            } else {
                return null;
//                throw new RuntimeException("fail!");
            }
        }
        return obj;
    }

    static String jsonString(Object data, String key) {
        return (String) jsonValue(data, key);
    }

    static Double jsonDouble(Object data, String key) {
        return Double.valueOf(jsonValue(data, key).toString());
    }

    static Integer jsonInt(Object data, String key) {
        return (Integer) jsonValue(data, key);
    }

    static Object[] jsonArray(Object data, String key) {
        Object o = jsonValue(data, key);
        if (o == null) {
            return null;
        }
        return (Object[]) ((List) o).toArray();
    }

    static boolean jsonBoolean(Object data, String key) {
        Object o = jsonValue(data, key);
        if (o == null) return false;
        return (Boolean) o;
    }

    static class JsonResultEx extends JsonResult {
        private JsonResultEx() {
            super(false);
        }
    }

    static class MvcResultHandler extends PrintingResultHandler {
        MvcResultHandler() {
            super(new ResultValuePrinter() {

                @Override
                public void printHeading(String heading) {
                    System.out.println();
                    System.out.println(String.format("%20s:", heading));
                }

                @Override
                public void printValue(String label, Object value) {
                    if (value != null && value.getClass().isArray()) {
                        value = CollectionUtils.arrayToList(value);
                    } else if (value instanceof MultiValueMap) {
                        MultiValueMap<String, String> map = (MultiValueMap<String, String>) value;
                        StringBuilder strBuf = new StringBuilder();
                        Iterator<Map.Entry<String, List<String>>> entries = map.entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry<String, List<String>> entry = entries.next();
                            String key = entry.getKey();
                            List<String> val = entry.getValue();
                            strBuf.append("\"" + key + "\"").append(": ").append("\"" + val.get(0) + "\"");
                            if (entries.hasNext()) {
                                strBuf.append(", ");
                            }
                        }
                        System.out.println(String.format("%20s = {%s}", label, strBuf.toString()));
                        return;
                    }
                    System.out.println(String.format("%20s = %s", label, value));
                }
            });
        }
    }

    static class Mvc {
        private String url;
        private MockHttpServletRequestBuilder request;

        private Mvc(String url, HttpMethod method) {
            this.url = url;
            request = MockMvcRequestBuilders.request(method, url);
        }

        private Mvc(String url) {
            this(url, HttpMethod.POST);
        }

        public JsonResult call() throws Exception {
            String content = mockMvc.perform(request).andDo(new MvcResultHandler()).andReturn().getResponse().getContentAsString();
            JsonResult jsonResult = null;
            try {
                jsonResult = JsonUtil.json2Object(content, JsonResultEx.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        public MockHttpServletRequestBuilder request() {
            return request;
        }

        public Mvc param(String name, Object... values) {
            int length = values.length;
            String[] strArr = new String[length];
            for (int i = 0; i < length; i++) {
                strArr[i] = values[i].toString();
            }
            request.param(name, strArr);
            return this;
        }

        public Mvc params(Object obj) throws Exception {
            fill(request, obj);
            return this;
        }

        public Mvc params(Object obj, String prefix) throws Exception {
            fill(request, obj, prefix);
            return this;
        }
    }

    public Mvc mvc(String url, HttpMethod method) {
        return new Mvc(url, method);
    }

    public Mvc mvc(String url) {
        return mvc(url, HttpMethod.POST);
    }


}
