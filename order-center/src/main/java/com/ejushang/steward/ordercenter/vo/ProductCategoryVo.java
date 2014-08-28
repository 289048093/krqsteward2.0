package com.ejushang.steward.ordercenter.vo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-14
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryVo {

    private String id;

    private String code;

    private String name;

    private Date createTime;

    private Date updateTime;

    private String operatorId;

    private List<ProductCategoryVo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public List<ProductCategoryVo> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCategoryVo> children) {
        this.children = children;
    }
}
