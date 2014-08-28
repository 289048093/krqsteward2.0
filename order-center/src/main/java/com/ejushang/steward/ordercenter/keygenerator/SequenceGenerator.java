package com.ejushang.steward.ordercenter.keygenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改人：Amos.zhou
 * 修改时间:2013-12-26
 * 从生成订单做成通用的
 *
 * User: Json.zhu
 * Date: 13-12-25
 * Time: 下午4:29
 *
 */
public class SequenceGenerator {

    private static final Logger log = LoggerFactory.getLogger(SequenceGenerator.class);

    private static  volatile SequenceGenerator generator = new SequenceGenerator();
    private static Map<String,KeyInfo> keyList = new HashMap<String,KeyInfo>();

    static {
        try{
            keyList.put(SystemConfConstant.NEXT_ORDER_NO,new OrderKeyInfo(SystemConfConstant.NEXT_ORDER_NO,200));
            keyList.put(SystemConfConstant.NEXT_MEAL_SET_NO,new MealSetKeyInfo(SystemConfConstant.NEXT_MEAL_SET_NO,5));
            keyList.put(SystemConfConstant.NEXT_ORDER_ITEM_NO,new OrderItemKeyInfo(SystemConfConstant.NEXT_ORDER_ITEM_NO,200));
            keyList.put(SystemConfConstant.NEXT_RETURN_VISIT_NO,new ReturnVisitKeyInfo(SystemConfConstant.NEXT_RETURN_VISIT_NO,200));
        }catch (Exception e){
            log.error("", e);
        }
    }

    private SequenceGenerator() {
    };

    /**工厂方法*/
    public static SequenceGenerator getInstance() {
        return generator;
    }

    /** 获取下一个value*/
    private   String getNextKey(String keyName) {
        KeyInfo key = keyList.get(keyName);
        if(key==null){
           throw new IllegalArgumentException("不存在配置项相关的序列键生成信息");
        }
        return key.getNextNoWithRandomSuffix();
    }

    /**
     * 获取下一个订单编号
     * @return
     */
    public String getNextOrderNo(){
        return getNextKey(SystemConfConstant.NEXT_ORDER_NO);
    }


    /**
     * 获取下一个订单项编号编号
     * @return
     */
    public String getNextOrderItemNo(){
        return getNextKey(SystemConfConstant.NEXT_ORDER_ITEM_NO);
    }


    /**
     * 获取下一个套餐编号
     * @return
     */
    public String getNextMealSetNo(){
        return getNextKey(SystemConfConstant.NEXT_MEAL_SET_NO);
    }


    /**
     * 获取下一个回访编号
     * @return
     */
    public String getNextReturnVisitNo(){
        return getNextKey(SystemConfConstant.NEXT_RETURN_VISIT_NO);
    }

    /**
     *获取不需要缓存的自增长键
     * @param keyName
     * @return
     */
    public String getNextWithoutCache(String keyName){
        KeyInfo key  = new AutoIncrementKeyInfo(keyName);
        if(key==null){
            throw new IllegalArgumentException("不存在配置项相关的序列键生成信息");
        }
        return key.getNextNo();
    }
}
