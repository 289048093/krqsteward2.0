package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 附件类型
 * @Author Channel
 * @Date ${date}
*/
@JsonSerialize(using = EnumSerializer.class)
public enum AttachmentType implements ViewEnum {

    IMAGE("图片"),
    DOC("word"),
    EXL("excel"),
    OTH("other");

    public String value;

    AttachmentType(String value) {
    this.value = value;
    }

    @Override
    public String getName() {
    return this.toString();
    }

    @Override
    public String getValue() {
    return value;
    }

}