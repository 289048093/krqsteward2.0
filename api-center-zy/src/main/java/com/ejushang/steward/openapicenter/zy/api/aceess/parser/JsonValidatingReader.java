package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:52
 */
public class JsonValidatingReader extends JsonReader {
    public static final Object INVALID = new Object();
    private JsonValidator validator;

    public JsonValidatingReader(JsonValidator validator) {
        this.validator = validator;
    }

    public JsonValidatingReader(JsonErrorListener listener) {
        this(new JsonValidator(listener));
    }

    public JsonValidatingReader() {
        this(new StdoutStreamErrorListener());
    }

    public Object read(String string) {
        if (!validator.validate(string)) return INVALID;
        return super.read(string);
    }
}
