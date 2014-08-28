package com.ejushang.steward.logisticscenter.numGenerator;


import java.util.List;

/**
 *
 * 物流单号的生成接口
 * User: JBOSS.wu
 * Date: 14-4-23
 * Time: 下午3:02
 *
 */
public interface LogisticsNumGenerator {

    /**
     *生成物流单号
     * @param initNo 生成的起始单号
     * @param law    递增数
     * @param count  生成数量
     * @return
     */
    public List<String> generateNumList(String initNo, int law, int count) throws GenerateException;
}
