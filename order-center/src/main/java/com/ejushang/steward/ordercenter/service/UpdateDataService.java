package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.ordercenter.constant.OrderType;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class UpdateDataService {

    private static final Logger log = LoggerFactory.getLogger(UpdateDataService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GeneralDAO generalDAO;

    /**
     *
     */
    public void updateOriginalOrderItemSkuZpLvXingDai() {
        //一些校验
        Map<String, String> productGiftSkuMap = new LinkedHashMap<String, String>();
        productGiftSkuMap.put("4895161311647", "DK012-1");
        productGiftSkuMap.put("4895161311654", "DK013-1");
        productGiftSkuMap.put("4895161311760", "DK014-1");
        String notFoundSku = "ZP旅行袋";
        String sql = "select original_order_id from steward.t_original_order oo join steward.t_original_order_item ooi on ooi.original_order_id = oo.id where processed = false and sku = ?";

        for(Map.Entry<String, String> entry : productGiftSkuMap.entrySet()) {
            String productSku = entry.getKey();
            String newSku = entry.getValue();
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, productSku);
            List<Integer> originalOrderIds = new ArrayList<Integer>(results.size());
            for(Map<String, Object> map : results) {
                originalOrderIds.add((Integer) (map.entrySet().iterator().next().getValue()));
            }
            if(originalOrderIds.isEmpty()) continue;
//            originalOrderIds = originalOrderIds.subList(0,20);

            StringBuilder updateSql = new StringBuilder("update t_original_order_item set sku='" + newSku + "' where sku='" + notFoundSku + "' and original_order_id in (");
            for(Integer originalOrderId : originalOrderIds) {
                updateSql.append("?,");
            }
            updateSql.replace(updateSql.length()-1, updateSql.length(), ")");
            int successCount = jdbcTemplate.update(updateSql.toString(), originalOrderIds.toArray());
            log.info(String.format("sku[%s]对应的赠品的从原sku[%s]改为[%s], 成功数量[%d]", productSku, notFoundSku, newSku, successCount));

        }

    }


    /**
     * 因为online字段是2.0版本才加的,所以要给以前的订单的online字段设值
     */
    public void guessOrderOnlineFieldValue() {
        List<Order> orders = generalDAO.findAll(Order.class);
        int count = 0;
        for(Order order : orders) {
            if(order.getOnline() != null) continue;
            boolean online = false;
            if(PlatformType.TAO_BAO.equals(order.getPlatformType()) || PlatformType.JING_DONG.equals(order.getPlatformType())) {
                if(OrderType.NORMAL.equals(order.getType()) || OrderType.NORMAL.equals(order.getType())) {
                    online = true;
                }
            }
            order.setOnline(online);
            generalDAO.saveOrUpdate(order);
            if(count++ % 100 == 0) {
                generalDAO.getSession().flush();
                generalDAO.getSession().clear();
            }
        }
        log.info(String.format("成功更新%d个订单的online属性", count));
    }
}


