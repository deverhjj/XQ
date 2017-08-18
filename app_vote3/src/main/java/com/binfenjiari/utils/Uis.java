package com.binfenjiari.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/6/7
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Uis {

    public static void toggleSoftInput(Context ctxt) {
        InputMethodManager imm = (InputMethodManager) ctxt
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.showSoftInput(view, 0);
    }

    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
