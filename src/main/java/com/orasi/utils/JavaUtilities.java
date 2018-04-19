package com.orasi.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Collection;
import java.util.Map;

public class JavaUtilities {

    public static boolean isValid(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof String && isEmpty((String) obj)) {
            return false;
        } else if (obj instanceof Collection<?> && ((Collection<?>) obj).isEmpty()) {
            return false;
        } else if (obj instanceof Map<?, ?> && ((Map<?, ?>) obj).isEmpty()) {
            return false;
        }
        return true;
    }
}
