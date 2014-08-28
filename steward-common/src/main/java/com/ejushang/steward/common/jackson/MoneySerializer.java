package com.ejushang.steward.common.jackson;

import com.ejushang.steward.common.util.Money;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *
 */
public class MoneySerializer extends JsonSerializer<Money> {
    @Override
    public void serialize(Money value, JsonGenerator jsonGenerator, SerializerProvider provider)
            throws IOException {
        if (value != null) {
            jsonGenerator.writeString(value.toString());
        }
    }
}  