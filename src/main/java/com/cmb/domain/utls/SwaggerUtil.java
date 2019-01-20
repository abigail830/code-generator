package com.cmb.domain.utls;

import io.swagger.models.properties.*;

public class SwaggerUtil {

    public static String getType(Property value, String suffix) {

        if (value instanceof LongProperty) {
            return "Long";
        }
        if (value instanceof IntegerProperty) {
            return "Integer";
        }
        if (value instanceof StringProperty) {
            return "String";
        }
        if (value instanceof BooleanProperty) {
            return "Boolean";
        }
        if (value instanceof DateTimeProperty) {
            return "java.time.LocalDateTime";
        }
        if (value instanceof RefProperty) {
            return ((RefProperty) value).getSimpleRef() + suffix;
        }
        if (value instanceof ArrayProperty) {
            return String.format("java.util.List<%s>", getType(((ArrayProperty) value).getItems(), suffix));
        }
        return null;
    }

    public static String getType(String value, Property property, String suffix) {
        if (value.equals("integer")) {
            return "Integer";
        }
        if (value.equals("string")) {
            return "String";
        }
        if (value.equals("array")) {
            return String.format("java.util.List<%s>", getType(property, suffix));
        }

        return null;
    }
}
