package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EmployeeUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.ProductLocation;
import com.ejushang.steward.ordercenter.constant.ProductStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_product")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Product implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String name;
    /**
     * 商品编号
     */
    private String productNo;
    /**
     * 商品条形码
     */
    private String sku;

    /**
     * 外部平台产品编码
     */
    private String outerProductNo;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 市场价
     */
    private Money marketPrice = Money.valueOf(0);
//    /**
//     * 现价*
//     */
//    private Money importPrice = Money.valueOf(0);
    /**
     * '最低价'*
     */
    private Money minimumPrice = Money.valueOf(0);
    /**
     * 颜色
     */
    private String color;
    /**
     * 重量
     */
    private String weight;
    /**
     * 包装尺寸
     */
    private String boxSize;
    /**
     * 规格
     */
    private String speci;
    /**
     * 删除标志
     */
    private boolean deleted = false;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 操作人ID
     */
    private Integer operatorId;
    /**
     * 品牌ID
     */
    private Integer brandId;
    /**
     * 品牌对象
     */
    private Brand brand;
    /**
     * 品牌名
     */
    private String brandName;
    /**
     * 产品分类ID
     */
    private Integer categoryId;
    /**
     * 产品分类对象
     */
    private ProductCategory category;

    /**
     * 类目名称
     */
    private String categoryName;
    /**
     * 产地
     */
    private String orgin;
    /**
     * 产品类型 如：爆款等
     */
    private ProductStyle style;
    /**
     * 库位赠品 如：正常.缺位等
     */
    private ProductLocation location;


    @Column(name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //
   @JsonIgnore
    private List<Storage> storage;
     @OneToMany(fetch = FetchType.LAZY,mappedBy = "product")
    public List<Storage> getStorage() {
        return storage;
    }

    public void setStorage(List<Storage> storage) {
        this.storage = storage;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    public Brand getBrand() {
        return brand;
    }

    @Column(name="outer_product_no")
    public String getOuterProductNo() {
        return outerProductNo;
    }

    public void setOuterProductNo(String outerProductNo) {
        this.outerProductNo = outerProductNo;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "brand_id")
    @Basic
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }


    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @javax.persistence.Column(name = "product_no")
    @Basic
    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }


    @javax.persistence.Column(name = "sku")
    @Basic
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @javax.persistence.Column(name = "category_id")
    @Basic
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }


    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @javax.persistence.Column(name = "pic_url")
    @Basic
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }


    @javax.persistence.Column(name = "market_price")
    @Basic
    public Money getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Money marketPrice) {
        this.marketPrice = marketPrice;
    }

//    @javax.persistence.Column(name = "import_price")
//    @Basic
//    public Money getImportPrice() {
//        return importPrice;
//    }
//
//    public void setImportPrice(Money importPrice) {
//        this.importPrice = importPrice;
//    }

    @javax.persistence.Column(name = "minimum_price")
    @Basic


    public Money getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Money minimumPrice) {
        this.minimumPrice = minimumPrice;
    }


    @javax.persistence.Column(name = "color")
    @Basic
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    @javax.persistence.Column(name = "weight")
    @Basic
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    @javax.persistence.Column(name = "box_size")
    @Basic
    public String getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(String boxSize) {
        this.boxSize = boxSize;
    }


    @javax.persistence.Column(name = "speci")
    @Basic
    public String getSpeci() {
        return speci;
    }

    public void setSpeci(String speci) {
        this.speci = speci;
    }

    @javax.persistence.Column(name = "deleted")
    @Basic
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }


    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name = "orgin")
    @Basic
    public String getOrgin() {
        return orgin;
    }

    public void setOrgin(String orgin) {
        this.orgin = orgin;
    }


    @javax.persistence.Column(name = "style")
    @Enumerated(EnumType.STRING)
    public ProductStyle getStyle() {
        return style;
    }

    public void setStyle(ProductStyle style) {
        this.style = style;
    }

    @javax.persistence.Column(name = "location")
    @Enumerated(EnumType.STRING)
    public ProductLocation getLocation() {
        return location;
    }

    public void setLocation(ProductLocation location) {
        this.location = location;
    }

    @Transient
    public String getOperatorName() {
        return EmployeeUtil.getOperatorName(operatorId);
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productNo='" + productNo + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", marketPrice=" + marketPrice +
//                ", importPrice=" + importPrice +
                ", minimumPrice=" + minimumPrice +
                ", color='" + color + '\'' +
                ", weight='" + weight + '\'' +
                ", boxSize='" + boxSize + '\'' +
                ", speci='" + speci + '\'' +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                ", brandId=" + brandId +
                ", brand=" + brand +
                ", categoryId=" + categoryId +
                ", category=" + category +
                ", orgin='" + orgin + '\'' +
                ", style=" + style +
                ", location=" + location +
                '}';
    }
}
