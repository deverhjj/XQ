package com.binfenjiari.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.biu.modulebase.binfenjiari.util.LogUtil;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/23
 * <br>Email: developer.huajianjiang@gmail.com
 */

public class Res {
    private static final String TAG = Res.class.getSimpleName();

    private Res(){}

    /**
     * @param ctxt
     * @param dp
     * @return
     */
    public static int dp2px(Context ctxt, int dp) {
        DisplayMetrics dm = ctxt.getResources().getDisplayMetrics();
        LogUtil.LogE(TAG, dm.toString() + ",densityDpi=" + dm.densityDpi);
        return (int) (dm.density * dp);
    }

    /**
     * @param ctxt
     * @return
     */
    public static int getStatusBarHeight(Context ctxt) {
        int resId = ctxt.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId == 0) return 0;
        return ctxt.getResources().getDimensionPixelSize(resId);
    }

    /**
     * @param ctxt
     * @return
     */
    public static int getActionBarHeight(Context ctxt) {
        int resId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resId = ctxt.getResources()
                    .getIdentifier("action_bar_default_height_material", "dimen", "android");
        } else {
            resId = ctxt.getResources()
                    .getIdentifier("action_bar_default_height", "dimen", "android");
        }
        if (resId == 0) return 0;
        return ctxt.getResources().getDimensionPixelSize(resId);
    }

    /**
     * @param ctxt
     * @return
     */
    public static int getNavigationBarHeight(Context ctxt) {
        int orientation = ctxt.getResources().getConfiguration().orientation;
        int resId = 0;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            resId = ctxt.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resId = ctxt.getResources()
                    .getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        }
        if (resId == 0) return 0;
        return ctxt.getResources().getDimensionPixelSize(resId);
    }

    public static void setStatusBarColor(Activity ctxt, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = ctxt.getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(color);
        }
    }
}
