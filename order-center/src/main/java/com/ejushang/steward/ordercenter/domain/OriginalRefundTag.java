package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * 原始退款单对应的标签（用于区分退款类型，如运费险、七天无理由退货等）
 * User: Baron.Zhang
 * Date: 2014/5/9
 * Time: 14:22
 */
@Entity
@Table(name = "t_original_refund_tag")
public class OriginalRefundTag implements EntityClass<Integer>{
    private Integer id;
    private Integer originalRefundId;
    private String tagKey;
    private String tagName;
    private String tagType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "original_refund_id")
    public Integer getOriginalRefundId() {
        return originalRefundId;
    }

    public void setOriginalRefundId(Integer originalRefundId) {
        this.originalRefundId = originalRefundId;
    }

    @Column(name = "tag_key")
    public String getTagKey() {
        return tagKey;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    @Column(name = "tag_name")
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Column(name = "tag_type")
    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @Override
    public String toString() {
        return "OriginalRefundTag{" +
                "id=" + id +
                ", originalRefundId=" + originalRefundId +
                ", tagKey='" + tagKey + '\'' +
                ", tagName='" + tagName + '\'' +
                ", tagType='" + tagType + '\'' +
                '}';
    }
}
