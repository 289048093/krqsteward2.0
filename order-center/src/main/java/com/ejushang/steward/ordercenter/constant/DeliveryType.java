package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;

/**
 * User: amos.zhou
 * Date: 13-12-23
 * Time: 上午11:59
 * 配送类型（物流公司）
 */
@JsonSerialize(using = EnumSerializer.class)
public enum DeliveryType implements ViewEnum {

    shunfeng("顺丰","505","SF","顺丰速运","^[0-9]{12}$","467","顺丰快递"),
    zhongtong("中通","500","ZTO","中通快递","^((618|680|688|618|828|988|118|888|571|518|010|628|205|880|717|718|728|761|762|701|757)[0-9]{9})$|^((2008|2010|8050|7518)[0-9]{8})$","1499","中通速递"),
    yunda("韵达","102","YUNDA","韵达快递","^[0-9]{13}$","1327","韵达快递"),
    zhaijisong("宅急送","103","ZJS","宅急送","^[0-9]{10}$"),

    ems("ems","2","EMS","EMS","^[A-Z]{2}[0-9]{9}[A-Z]{2}$|[0-9]{13}","465","邮政EMS"),
    yuantong("圆通","101","YTO","圆通速递","^(0|1|2|3|5|6|8|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$","463","圆通快递"),
    shentong("申通","100","STO","申通快递","^(268|888|588|688|368|468|568|668|768|868|968)[0-9]{9}$|^(268|888|588|688|368|468|568|668|768|868|968)[0-9]{10}$|^(STO)[0-9]{10}$","470","申通快递"),
    quanritongkuaidi("全日通"),

    kuaijiesudi("快捷","1204","FAST","快捷快递","^[0-9]{11,13}$|^(P330[0-9]{8})$|^(D[0-9]{11})$","2094","快捷速递"),
    huitongkuaidi("汇通","502","HTKY","百世汇通","^(A|B|C|D|E|H|0)(D|X|[0-9])(A|[0-9])[0-9]{10}$|^(21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39)[0-9]{10}$","1748","汇通快运"),
    guotongkuaidi("国通","1000001163","GTO","国通快递","^(1|2)[0-9]{9}$","2465","国通快递"),
    lianbangkuaidi("联邦"),

    quanfengkuaidi("全峰","1216","QFKD","全峰快递","^[0-9]{12}$","2016","全峰快递"),
    suer("速尔","201174","SURE","速尔","^[0-9]{11}[0-9]{1}$","2105","速尔快递"),
    tiantian("天天","504","TTKDEX","天天快递","^[0-9]{12}$","2009","天天快递"),
    youshuwuliu("优速","1207","UC","优速快递","^VIP[0-9]{9}|V[0-9]{11}|[0-9]{12}$|^LBX[0-9]{15}-[2-9AZ]{1}-[1-9A-Z]{1}$|^(9001)[0-9]{8}$","1747","优速物流"),

    youzhengguonei("邮政国内小包","2000002165","POSTB","邮政国内小包","^[GA]{2}[0-9]{9}([2-5][0-9]|[1][1-9]|[6][0-5])$|^[99]{2}[0-9]{11}$","2170","中国邮政国内小包"),
    zengyi("增益速递","1208","QRT","增益速递","^[0-9]{12}$","3044","增益速递"),
    debang("德邦物流","107","DBL","德邦物流","^[0-9]{8,10}$","2130","德邦物流"),
    changjiazisong("厂家自送","","","","1274","厂家自送"),
    xinbang("新邦物流","1186","XB","新邦物流","[0-9]{8}","2461","新邦物流"),
    debangwuliu("德邦物流","107","DBL","德邦物流","^[0-9]{8,10}$","2130","德邦物流"),
    unknown("未知");



    private String name;
    private String  tmallId;
    private String  tmallCode;
    private String tmallShortName;
    private String  tmallExp;
    private String  jdId;
    private String  jdName;


    DeliveryType(String name){
        this.name = name;
    }

    DeliveryType(String name ,String tmallId , String tmallCode, String  tmallExp) {
        this.name = name;
        this.tmallId = tmallId;
        this.tmallCode = tmallCode;
        this.tmallExp = tmallExp;
    }

    DeliveryType(String name ,String tmallId ,String tmallShortName, String tmallCode, String  tmallExp) {
        this.name = name;
        this.tmallId = tmallId;
        this.tmallCode = tmallCode;
        this.tmallShortName = tmallShortName;
        this.tmallExp = tmallExp;
    }

    DeliveryType(String name ,String tmallId , String tmallCode, String tmallShortName, String tmallExp,String jdId,String jdName) {
        this.name = name;
        this.tmallId = tmallId;
        this.tmallCode = tmallCode;
        this.tmallShortName = tmallShortName;
        this.tmallExp = tmallExp;
        this.jdId = jdId;
        this.jdName = jdName;
    }

    DeliveryType(String name ,String tmallId , String tmallCode, String  tmallExp,String jdId,String jdName) {
        this.name = name;
        this.tmallId = tmallId;
        this.tmallCode = tmallCode;
        this.tmallExp = tmallExp;
        this.jdId = jdId;
        this.jdName = jdName;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getValue() {
        return name;
    }



    /**
     * 获取淘宝上物流公司id
     * @return
     */
    public String getTmallId(){
        return  tmallId;
    }

    /**
     * 获取淘宝上物流公司代码
     * @return
     */
    public String getTmallCode(){
        return tmallCode;
    }

    public String getTmallShortName() {
        return tmallShortName;
    }

    /**
     * 获取淘宝上验证物流单号正则表达式
     * @return
     */
    public String getTmallExp(){
        return tmallExp;
    }

    /**
     * 得到京东物流公司id
     * @return
     */
    public String getJdId() {
        return jdId;
    }

    /**
     * 得到京东物流公司名称
     * @return
     */
    public String getJdName() {
        return jdName;
    }

    /**
     * 得到中文名称
     * @return
     */
    public String toDesc() {
        return name;
    }


    public static DeliveryType getDeliveryTypeByShortName(String tmallShortName){
        for(DeliveryType deliveryType : DeliveryType.values()){
            if(StringUtils.equalsIgnoreCase(deliveryType.getTmallShortName(),tmallShortName)){
                return deliveryType;
            }
        }
        return null;
    }

    /**
     * 根据值取枚举
     * @param value
     * @return
     */
    public static DeliveryType enumValueOf(String value) {
        if(value == null) {
            return null;
        }
        for(DeliveryType enumValue : values()) {
            if(value.equalsIgnoreCase(enumValue.getValue())) {
                return enumValue;
            }
        }
        return null;
    }

}
