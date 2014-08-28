package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import java.util.Date;

/**
 * 仓库
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 上午10:38
 */
public class Repository extends OperateTypeBean{

    @ApiField("name")
    private String name;

    @ApiField("code")
    private String code;

    @ApiField("address")
    private String address;

    @ApiField("shipping_comp")
    private String shippingComp;

    @ApiField("province_name")
    private String provinceName;

    @ApiField("city_name")
    private String cityName;

    @ApiField("area_name")
    private String areaName;

    @ApiField("charge_mobile")
    private String chargeMobile;

    @ApiField("charge_phone")
    private String chargePhone;

    @ApiField("custid")
    private String custid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getChargeMobile() {
        return chargeMobile;
    }

    public void setChargeMobile(String chargeMobile) {
        this.chargeMobile = chargeMobile;
    }

    public String getChargePhone() {
        return chargePhone;
    }

    public void setChargePhone(String chargePhone) {
        this.chargePhone = chargePhone;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(this);
    }
}
