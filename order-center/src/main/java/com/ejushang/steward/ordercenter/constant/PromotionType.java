package com.ejushang.steward.ordercenter.constant;

/**
 * User: Baron.Zhang
 * Date: 14-4-23
 * Time: 下午2:54
 */
public enum PromotionType {
    JD_TAOZHUANG("20-套装优惠","20"),
    JD_SHANTUAN("28-闪团优惠","28"),
    JD_TUANGOU("29-团购优惠","29"),
    JD_DANPINCUXIAO("30-单品促销优惠","30"),
    JD_SHOUJIHONGBAO("34-手机红包","34"),
    JD_MANFANMANSONG("35-满返满送(返现)","35"),
    JD_JINGDOU("39-京豆优惠","39"),
    JD_JINGDONGQUAN("41-京东券优惠","41"),
    JD_LIPINKA("52-礼品卡优惠","52"),
    JD_DIANPU("100-店铺优惠","100"),
    ;

    private String desc;
    private String value;

    PromotionType(String desc,String value){
        this.desc = desc;
        this.value = value;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
