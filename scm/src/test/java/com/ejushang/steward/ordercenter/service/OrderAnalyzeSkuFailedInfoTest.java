package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * User: liubin
 * Date: 14-3-10
 */

public class OrderAnalyzeSkuFailedInfoTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ConfService confService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Autowired
    private GeneralDAO generalDAO;

    @Test
    @Rollback(false)
    @Transactional
    public void test() throws IOException {
        String path = "/tmp/not_found_sku.txt";
        List<String> notFoundSkuList = IOUtils.readLines(new FileReader(new File(path)));
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        for(String skuString : notFoundSkuList) {
            String notFoundSku = skuString.substring(skuString.indexOf("[")+1, skuString.indexOf("]"));

            Search search = new Search(OriginalOrder.class);
            search.addFilterEqual("originalOrderItemList.sku", notFoundSku);
            List<OriginalOrder> originalOrders = generalDAO.search(search);

            Map<String, Object> maps = new LinkedHashMap<String, Object>();
            List<Map<String, String>> orderInfos = new ArrayList<Map<String, String>>();
            maps.put("sku", notFoundSku);
            maps.put("orderInfo", orderInfos);
            for(OriginalOrder originalOrder : originalOrders) {
                Map<String, String> orderInfo = new HashMap<String, String>();
                orderInfo.put("id", originalOrder.getId().toString());
                orderInfo.put("platformType", originalOrder.getPlatformType() == null ? "" : originalOrder.getPlatformType().getValue());
                orderInfo.put("platformOrderNo", originalOrder.getPlatformOrderNo());
                orderInfos.add(orderInfo);
            }

            results.add(maps);
        }
        String json = JsonUtil.object2Json(results);


        System.out.println(json);

    }

    @Test
    @Rollback(false)
    @Transactional
    public void test2() throws IOException {
        String path = "/tmp/not_found_sku.txt";
        List<String> notFoundSkuList = IOUtils.readLines(new FileReader(new File(path)));
        StringBuilder skuQueryParams = new StringBuilder();
        if(notFoundSkuList.isEmpty()) return;
        for(String skuString : notFoundSkuList) {
            String notFoundSku = skuString.substring(skuString.indexOf("[")+1, skuString.indexOf("]"));
            skuQueryParams.append("'" + notFoundSku + "',");
        }
        skuQueryParams.deleteCharAt(skuQueryParams.length() - 1);

        String sql = String.format("select count(t1.original_order_id) from ( " +
                " select ooi2.original_order_id from steward.t_original_order_item ooi2\n" +
                " where ooi2.sku in ( " +
                " select t2.sku from ( " +
                " select sku, count(ooi.id) count_ooi from steward.t_original_order oo join steward.t_original_order_item ooi on ooi.original_order_id = oo.id \n" +
                " where sku in (%s) group by sku " +
                " ) t2 " +
                " where t2.count_ooi >= 3 " +
                " ) " +
                " group by ooi2.original_order_id " +
                ") t1;", skuQueryParams.toString());

//        String sql = String.format("select count(t1.oo_id) from (" +
//                " select oo.id oo_id from t_original_order oo join t_original_order_item ooi on ooi.original_order_id = oo.id " +
//                " where ooi.sku in (%s) " +
//                " group by oo.id" +
//                " ) t1", skuQueryParams.toString());

//        String sql = String.format("select count(t1.original_order_id) from (" +
//                " select ooi.original_order_id from steward.t_original_order_item ooi" +
//                " where ooi.sku in (%s)" +
//                " group by ooi.original_order_id" +
//                ") t1;", skuQueryParams.toString());
        System.out.println(sql);

        System.out.println(generalDAO.getSession().createSQLQuery(sql).uniqueResult());

    }


    @Test
    @Rollback(false)
    @Transactional
    public void test3() throws IOException {
        String path = "/tmp/not_found_sku_duplicate.txt";
        List<String> notFoundSkuList = IOUtils.readLines(new FileReader(new File(path)));
        Map<String, Integer> skuMap = new HashMap<String, Integer>();
        MapComparator mapComparator = new MapComparator(skuMap);
        Map<String, Integer> skuTreeMap = new TreeMap<String, Integer>(Collections.reverseOrder(mapComparator));

        for(String skuString : notFoundSkuList) {
            String notFoundSku = skuString.substring(skuString.indexOf("[")+1, skuString.indexOf("]"));

            Integer count = skuMap.get(notFoundSku);
            if(count == null) {
                count = 0;
            }
            skuMap.put(notFoundSku, ++count);
        }

        skuTreeMap.putAll(skuMap);
        for(Map.Entry<String, Integer> entry : skuTreeMap.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }

    }

    static class MapComparator implements Comparator<String> {

        Map<String, Integer> map;

        MapComparator(Map<String, Integer> map) {
            this.map = map;
        }

        @Override
        public int compare(String o1, String o2) {
            Integer x = map.get(o1);
            Integer y = map.get(o2);
            if (x.equals(y)) {
                return o1.compareTo(o2);
            }
            return x.compareTo(y);
        }
    }

}
