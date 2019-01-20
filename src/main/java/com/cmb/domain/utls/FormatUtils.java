package com.cmb.domain.utls;

public final class FormatUtils {

    private static final String pattern = "-";

    public static String formateClassName(String className) {

        if (className == null) {
            return null;
        }
        int pos = className.indexOf(pattern);
        if (pos == -1) {
            return className;
        }

        String top = String.valueOf(className.charAt(pos + 1)).toUpperCase();
        String newClassName = className.substring(0, pos) + top;
        if (pos < className.length() - 1) {
            newClassName += className.substring(pos + 2);
        }
        return formateClassName(newClassName);
    }

}
