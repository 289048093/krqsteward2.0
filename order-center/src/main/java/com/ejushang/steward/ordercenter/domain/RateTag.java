package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * User: Shiro
 * Date: 14-8-25
 * Time: 下午3:15
 */
@Table(name = "t_rate_tag")
@Entity
public class RateTag implements EntityClass<Integer> {
    private Integer id;
    private RateInfo rateInfo;
    /**
     *标签的名称
     */
    private String tagName;
    /**
     * 标签的极性，正极true，负极false
     */
    private Boolean posi;
    /**
     * 评价类型（1、原始评价 2、追加评价）
     */
    private Integer rateType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rate_tag_id", insertable = false, updatable = false)
    public RateInfo getRateInfo() {
        return rateInfo;
    }

    public void setRateInfo(RateInfo rateInfo) {
        this.rateInfo = rateInfo;
    }
    @Column(name ="rate_type")
    @Basic
    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name ="tag_name")
    @Basic
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    @Column(name ="posi")
    @Basic
    public Boolean getPosi() {
        return posi;
    }

    public void setPosi(Boolean posi) {
        this.posi = posi;
    }
}
