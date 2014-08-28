package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-6
 * Time: 下午5:36
 */
public class Tag extends ZiYouObject {

    private static final long serialVersionUID = 4087216642405786475L;
    /**
     * 退款单编号
     */
    @ApiField("refund_id")
    private String refundId;

    /**
     * 标签key，如service7d
     */
    @ApiField("tag_key")
    private String tagKey;

    /**
     * 标签名，如服务-7天无理由
     */
    @ApiField("tag_name")
    private String tagName;

    /**
     * 签标类型，如service
     */
    @ApiField("tag_type")
    private String tagType;

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getTagKey() {
        return tagKey;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
