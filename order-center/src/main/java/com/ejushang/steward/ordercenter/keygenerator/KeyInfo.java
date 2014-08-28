package com.ejushang.steward.ordercenter.keygenerator;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * User:Json.zhu
 * Date: 13-12-25
 * Time: 上午11:09
 */
public abstract class KeyInfo {

    /**缓存的key的名字*/
    protected String keyName;
    /**最大的缓存值 */
    protected Integer maxCacheValue;
    /**最小的缓存值*/
    protected Integer minCacheValue;
    /**缓存的个数 = maxCacheValue - minCacheValue*/
    protected Integer poolSize;
    /**下一个值，每次update过后，第一次nextValue=minCacheValue*/
    protected Integer nextValue;


    public KeyInfo(String keyName,int poolSize) {
        this.keyName = keyName;
        this.poolSize = poolSize;
        updateCache();
    }

    protected abstract String   getPrefix();
    protected abstract void updateNexValue(Integer currValue);

    /**
     * 生成订单号
     *
     * @return
     */
    public String getNextNoWithRandomSuffix() {
        //得到3位随机数
        String randomStr = RandomStringUtils.random(3, SystemConfConstant.ALL_NUMBER);
        return getPrefix() + getFromCache() + randomStr;
    }

    public String getNextNo(){
        return getPrefix() + getFromCache();
    }

    /**
     * 得到数据库中的自增的值
     *
     * @return
     */
    private void updateCache() {
        GeneralDAO generalDAO = (GeneralDAO) Application.getBean("generalDAO");
        Search search = new Search(Conf.class).addFilterEqual("name",keyName);
        Conf conf = (Conf) generalDAO.searchUnique(search);


        //如果忘记初始化配置表中订单号的配置
        if (conf == null) {
           throw new  IllegalArgumentException("配置表中没有配置相关订单项，请自己添加,keyName:" + keyName) ;
        }
        String confValue = conf.getValue();
        Integer value = Integer.parseInt(confValue);
        //更新缓存中的minValue MaxValue和 nextValue
        updateNexValue(value);
        //更新配置表中订单的值，先更新，再使用，可以避免先使用，然后更新失败，导致重复值的情况
        conf.setValue(String.valueOf(maxCacheValue));
        generalDAO.saveOrUpdate(conf);
        //return value;
    }


    private synchronized int  getFromCache() {
        if (nextValue > maxCacheValue) {
            updateCache();
        }
        return nextValue++;
    }
}
