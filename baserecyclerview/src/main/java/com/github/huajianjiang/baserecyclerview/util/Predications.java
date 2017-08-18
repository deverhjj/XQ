package com.github.huajianjiang.baserecyclerview.util;

import java.util.Collection;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/4/28
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Predications {

    public static boolean isNullOrEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNullOrEmpty(Collection c) {
        return c == null || c.size() == 0;
    }

    public static boolean isNullOrEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

}
