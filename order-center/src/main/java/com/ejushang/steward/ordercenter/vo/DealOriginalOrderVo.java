package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.constant.PlatformType;

import java.util.Date;

/**
 * User: JBoss.WU
 * Date: 14-7-8
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
public class DealOriginalOrderVo {
    //id主键
    private Integer id;
    //解析状态
 private String processed;
    //平台类型
    private PlatformType platformType;
    //店铺名称
    private String shopName;
    //品牌名称
    private String brandName;
    //外部平台订单编号
    private String outOrderNo;
    //外部平台订单金额
    private String outActualFee;
    //邮费
    private String postFee;
   //买家ID
    private String buyerId;
    //省份
    private String province;
    //城市
    private String city;
    //地址
    private String address;
     //收货人
    private String receiverName;
    //邮政编码
    private String receiverZip;
    //收货电话
    private String receiverPhone;
    //收货手机
    private String receiverMobile;
    //区
    private String receiverDistrict;
    //创建时间
    private Date createTime;
    //下单时间
    private Date buyTime;
    //付款时间
    private Date payTime;
    //修改时间
    private Date modifiedTime;
    //是否有效
    private String discard;

    public String getDiscard() {
        return discard;
    }

    public void setDiscard(String discard) {
        this.discard = discard;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getOutActualFee() {
        return outActualFee;
    }

    public void setOutActualFee(String outActualFee) {
        this.outActualFee = outActualFee;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public Date getPayTime() {
        return payTime;
    }
}
