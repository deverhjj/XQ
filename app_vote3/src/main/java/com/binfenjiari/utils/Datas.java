package com.binfenjiari.utils;

import android.os.Bundle;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Datas {
    public static final String TAG = Datas.class.getSimpleName();

    public static Bundle argsOf(String... argPairs) {
        return argsOf(null, argPairs);
    }

    public static Bundle argsOf(Bundle b, String... argPairs) {
        if (argPairs.length % 2 != 0) {
            throw new IllegalStateException("must be key-value pair");
        }
        if (b == null) {
            b = new Bundle();
        }
        for (int i = 0, j = argPairs.length; i < j; i++) {
            String key = argPairs[i];
            String value = argPairs[++i];
            if (Preconditions.isNullOrEmpty(key) || Preconditions.isNullOrEmpty(value)) {
                continue;
            }
            b.putString(key, value);
        }
        return b;
    }

    public static Map<String, String> paramsOf(String... pairs) {
        if (pairs.length % 2 != 0) {
            throw new IllegalStateException("must be key-value pair");
        }
        Map<String, String> paramMap = new LinkedHashMap<>();
        for (int i = 0, j = pairs.length; i < j; i++) {
            String key = pairs[i];
            String value = pairs[++i];
            if (Preconditions.isNullOrEmpty(key) || Preconditions.isNullOrEmpty(value)) {
                continue;
            }
            paramMap.put(key, value);
        }
        return Collections.unmodifiableMap(paramMap);
    }

    public static Map<String, String> paramsOf(Bundle args) {
        if (Preconditions.isNullOrEmpty(args)) {
            return Collections.unmodifiableMap(Collections.<String, String>emptyMap());
        }
        Map<String, String> paramMap = new LinkedHashMap<>();
        for (String key : args.keySet()) {
            Object value = args.get(key);
            if (value == null) {
                continue;
            }
            if (value instanceof String) {
                paramMap.put(key, (String) value);
            } else {
                paramMap.put(key, value.toString());
            }
        }
        return Collections.unmodifiableMap(paramMap);
    }
}
