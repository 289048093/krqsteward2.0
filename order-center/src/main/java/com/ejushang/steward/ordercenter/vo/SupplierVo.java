package com.ejushang.steward.ordercenter.vo;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-14
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public class SupplierVo {

    private String id;
    /**
     * 商家编号
     */
    private String code;
    /**
     * 商家名
     */
    private String name;

    private String deleted;

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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
