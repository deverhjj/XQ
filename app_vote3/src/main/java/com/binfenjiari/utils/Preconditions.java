package com.binfenjiari.utils;

import android.os.Bundle;

import java.util.Collection;
import java.util.Map;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/28
 * <br>Email: developer.huajianjiang@gmail.com
 */

public class Preconditions {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean hasNullOrEmpty(String... array) {
        if (isNullOrEmpty(array)) return true;
        for (String who : array) {
            if (isNullOrEmpty(who)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isNullOrEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNullOrEmpty(Bundle b) {
        return b == null || b.isEmpty();
    }

    public static <T> T isNotNull(T ref, Object expMsg) {
        if (ref == null) throw new NullPointerException(String.valueOf(expMsg));
        return ref;
    }
}
