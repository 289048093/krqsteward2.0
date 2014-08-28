package com.ejushang.steward.common.domain.util;

import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User: liubin
 * Date: 14-5-21
 */
@JsonSerialize(using = EnumSerializer.class)
public interface ViewEnum {

    String getName();

    String getValue();

}
