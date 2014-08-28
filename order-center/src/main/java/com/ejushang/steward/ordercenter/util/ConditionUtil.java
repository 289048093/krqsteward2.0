package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.Repository;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * User: tin
 * Date: 14-4-9
 * Time: 下午1:49
 */
public class ConditionUtil {

    private ConditionUtil() {
    }


    public static Search RepositoryUtil(Repository repository) {
        Search search = new Search(Repository.class);
        Map<String, Object> map = new HashMap<String, Object>();

        search=Condition(search,RepositoryMap(repository));
        return search;
    }

    public static Search Condition(Search search, Map<String, Object> map) {
        Iterator iTer = map.entrySet().iterator();
        while (iTer.hasNext()) {
            Map.Entry entry = (Map.Entry) iTer.next();
            String key = entry.getKey().toString();
            Object val = entry.getValue();
            search=RepositoryConnect(search,val,key);

        }
           return search;
    }

    public static Map<String,Object> RepositoryMap(Repository repository){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", repository.getId());
        map.put("name", repository.getName());
        map.put("code", repository.getCode());
        map.put("address", repository.getAddress());
//        map.put("chargePersonId", repository.getChargePersonId());
        map.put("shippingComp", repository.getShippingComp());
        map.put("chargeMobile", repository.getChargeMobile());
        map.put("chargePhone", repository.getChargePhone());
        map.put("operatorId", repository.getOperatorId());
        map.put("areaId", repository.getAreaId());
        map.put("cityId", repository.getCityId());
        map.put("provinceId", repository.getProvinceId());
        return map;
    }
    public static Search RepositoryConnect(Search search,Object val,String key){
        if (!NumberUtil.isNullOrZero(val)) {
            if (key.equals("name") || key.equals("code") || key.equals("address")) {
                search.addFilterLike(key, "%" + val + "%");
            } else {
                search.addFilterEqual(key, val);
            }
        }
        return search;
    }

}
