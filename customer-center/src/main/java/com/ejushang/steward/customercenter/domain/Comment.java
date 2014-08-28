package com.ejushang.steward.customercenter.domain;


import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.customercenter.constant.CommentResult;
import com.ejushang.steward.ordercenter.constant.PlatformType;

import javax.persistence.*;
import java.util.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:33
 */

@Table(name = "t_comment")
@Entity
public class Comment  implements EntityClass<Integer>,OperableData {
    private Integer id;
    private String commenter;
    private String buyerId;
    private String mobile;
    private Integer orderId;
    private Integer orderItemId;
    //外部平台订单编号
    private String platformOrderNo;  //
    /**
     * 外部平台子订单编号
     */
    private String platformSubOrderNo;   //
    private CommentResult result;
    private String productSku;
    private String productName;
    private PlatformType platformType;   //
    private Integer shopId;
    private String shopName;
    private Date createTime;
    private Date updateTime;
    //操作人ID
    private Integer operatorId;
    private Boolean category; //
    private List<CommentCategory> categories=new ArrayList<CommentCategory>(0);
    private List<CommentContent> contents=new ArrayList<CommentContent>(0);


    @OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,targetEntity=CommentContent.class, mappedBy = "comment")
    public List<CommentContent> getContents() {
        return contents;
    }

    public void setContents(List<CommentContent> contents) {
        this.contents = contents;
    }

    @ManyToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY,targetEntity=CommentCategory.class)
    @JoinTable(
            name="t_comment_commentcategory",
            joinColumns={@JoinColumn(name="comment_id")},
            inverseJoinColumns={@JoinColumn(name="category_id")}
    )
    public List<CommentCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CommentCategory> categories) {
        this.categories = categories;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name ="commenter")
    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    @javax.persistence.Column(name ="buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @javax.persistence.Column(name ="mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @javax.persistence.Column(name ="order_item_id")
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    @Enumerated(EnumType.STRING)
    @javax.persistence.Column(name ="result")
    public CommentResult getResult() {
        return result;
    }

    public void setResult(CommentResult result) {
        this.result = result;
    }

    @javax.persistence.Column(name ="product_sku")
    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    @javax.persistence.Column(name ="product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    @javax.persistence.Column(name ="platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name ="shop_id")
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @javax.persistence.Column(name ="shop_name")
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @javax.persistence.Column(name ="create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @javax.persistence.Column(name ="update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @javax.persistence.Column(name ="operator_id")
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name ="is_category")
    public Boolean getCategory() {
        return category;
    }

    public void setCategory(Boolean category) {
        this.category = category;
    }

    @javax.persistence.Column(name ="platform_order_no")
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    @javax.persistence.Column(name ="platform_sub_order_no")
    public String getPlatformSubOrderNo() {
        return platformSubOrderNo;
    }

    public void setPlatformSubOrderNo(String platformSubOrderNo) {
        this.platformSubOrderNo = platformSubOrderNo;
    }

    @Column(name="order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
