package com.binfenjiari.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/23
 * <br>Email: developer.huajianjiang@gmail.com
 */

public class Msgs {

    public static void shortToast(@NonNull Context ctxt, int resId) {
        Toast.makeText(ctxt, resId, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(@NonNull Context ctxt, CharSequence txt) {
        Toast.makeText(ctxt, txt, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(@NonNull Context ctxt, int resId) {
        Toast.makeText(ctxt, resId, Toast.LENGTH_LONG).show();
    }

    public static void longToast(@NonNull Context ctxt, CharSequence txt) {
        Toast.makeText(ctxt, txt, Toast.LENGTH_LONG).show();
    }
}
