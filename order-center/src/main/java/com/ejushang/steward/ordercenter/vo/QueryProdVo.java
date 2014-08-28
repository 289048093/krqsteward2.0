package com.ejushang.steward.ordercenter.vo;

/**
 * User:moon
 * Date: 14-1-9
 * Time: 下午5:26
 */
public class QueryProdVo {

    private Integer id;

    /**
     * 商品编号
     */
    private String productNo;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品条形码
     */
    private String sku;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 品牌ID
     */
    private Integer brandId;
    /**
     * 品牌对象
     */
    private String brandName;
    /**
     * 仓库ID
     */
    private Integer repositoryId;
    /**
     * 仓库名
     */
    private String repositoryName;

    /**
     * 产品分类ID
     */
    private Integer prodCategoryId;
    /**
     * 产品分类对象
     */
    private String prodCategoryName;

    /**
     * 库存数量
     */
    private Integer repositoryNum;

    /**
     * 价格
     *
     * @return
     */
    private String marketPrice;

    /**
     * 促销价
     */
    private String discountPrice;

    /** 建议使用trade.promotion_details查询系统优惠 系统优惠金额（如打折，VIP，满就送等） */
    private String discountFee;

    /** 分摊折扣 */
    private String sharedDiscountFee;

    /** 分摊邮费 */
    private String sharedPostFee ;

    /** 换货邮费 */
    private String exchangePostFee ;

    /** 换货邮费承担方 */
    private String exchangePostPayer;

    /**
     * 类型
     *
     * @return
     */
    private String orderItemType;

    private  Integer autoId;

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Integer getProdCategoryId() {
        return prodCategoryId;
    }

    public void setProdCategoryId(Integer prodCategoryId) {
        this.prodCategoryId = prodCategoryId;
    }

    public String getProdCategoryName() {
        return prodCategoryName;
    }

    public void setProdCategoryName(String prodCategoryName) {
        this.prodCategoryName = prodCategoryName;
    }

    public Integer getRepositoryNum() {
        return repositoryNum;
    }

    public void setRepositoryNum(Integer repositoryNum) {
        this.repositoryNum = repositoryNum;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getSharedDiscountFee() {
        return sharedDiscountFee;
    }

    public void setSharedDiscountFee(String sharedDiscountFee) {
        this.sharedDiscountFee = sharedDiscountFee;
    }

    public String getSharedPostFee() {
        return sharedPostFee;
    }

    public void setSharedPostFee(String sharedPostFee) {
        this.sharedPostFee = sharedPostFee;
    }

    public String getExchangePostFee() {
        return exchangePostFee;
    }

    public void setExchangePostFee(String exchangePostFee) {
        this.exchangePostFee = exchangePostFee;
    }

    public String getExchangePostPayer() {
        return exchangePostPayer;
    }

    public void setExchangePostPayer(String exchangePostPayer) {
        this.exchangePostPayer = exchangePostPayer;
    }

    public String getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(String orderItemType) {
        this.orderItemType = orderItemType;
    }

    @Override
    public String toString() {
        return "QueryProdVo={" +
                "sku = " + sku + "," +
                "num = " + num + "," +
                "orderItemType = " + orderItemType + "," +
                "}";
    }

}
