package com.binfenjiari.utils;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/23
 * <br>Email: developer.huajianjiang@gmail.com
 */

public class Views {

    private Views(){}

    @SuppressWarnings("unchecked")
    public static <T extends View> T find(@NonNull View parent, @IdRes int id) {
        return (T) parent.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T find(@NonNull Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T find(@NonNull Dialog dialog, @IdRes int id) {
        return (T) dialog.findViewById(id);
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static boolean isInvisible(View view) {
        return view == null || view.getVisibility() == View.INVISIBLE;
    }

    public static boolean isGone(View view) {
        return view == null || view.getVisibility() == View.GONE;
    }

}
