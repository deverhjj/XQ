package com.github.huajianjiang.alphaslidebar;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/6/8
 * <br>Email: developer.huajianjiang@gmail.com
 * <p>一个通过监听应用窗口可见高度和屏幕高度的差值来判断分析软键盘的影藏和显示状态
 * 因此需要应用设置软键盘显示模式能看影响应用的布局，否则不起作用
 */
public class SoftKeyboardHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = SoftKeyboardHelper.class.getSimpleName();
    /**
     * 状态栏高度，此处作为一个判断软键盘是否显示的一个差值判断值
     * 软键盘隐藏状态下，应用窗口高度和屏幕高度差值就是一个状态栏的高度
     */
    final int STATUSBAR_HEIGHT;

    private OnSoftKeyboardStateChangeListener mListener;

    /**
     * 目标 View
     */
    private View mTarget;

    /**
     * 用于存储应用当前窗口的显示区域坐标信息
     */
    private Rect windowRect = new Rect();
    /**
     * 用于存储当前软键盘是否处于显示状态
     */
    private boolean isShow;

    /**
     * 软键盘的高度值
     */
    private int mSoftKeyboardHeight;

    public SoftKeyboardHelper(@NonNull View target) {
        mTarget = target;
        target.getViewTreeObserver().addOnGlobalLayoutListener(this);
        STATUSBAR_HEIGHT = Res.getStatusBarHeight(target.getContext());
    }

    public void detach() {
        if (mTarget == null) return;
        ViewTreeObserver observer = mTarget.getViewTreeObserver();
        if (!observer.isAlive()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeOnGlobalLayoutListener(this);
        } else {
            observer.removeGlobalOnLayoutListener(this);
        }
        mTarget = null;
        mListener = null;
    }

    @Override
    public void onGlobalLayout() {
        //界面根布局
        View contentView = mTarget;
        //App 顶级根视图==>DecorView
        View rootView = contentView.getRootView();
        //获得应用当前窗口显示区域的大小
        rootView.getWindowVisibleDisplayFrame(windowRect);

        Logger.e(TAG,
                "rootView.height=" + rootView.getHeight() + ",fr.height=" + windowRect.height());
        // 计算屏幕的高度和当前应用的窗口显示区域高度差来判断软键盘的显示隐藏状态
        // 软键盘隐藏时，该高度差为一个状态栏的高度，显示时为一个状态栏的高度+软键盘的高度,
        // 因此可以从中活动软键盘的高度
        final int heightDiff = rootView.getHeight() - windowRect.height();

        Logger.e(TAG, "heightDiff" + heightDiff);
        //如果该高度差大于正常的高度差(状态栏的高度),说明软键盘当前是显示状态
        final boolean shouldShow = heightDiff > STATUSBAR_HEIGHT;
        if (shouldShow) {
            int dynamicHeightDiff = heightDiff - STATUSBAR_HEIGHT;

            if (isShow) {// 如果之前软键盘是处于显示状态，这里忽略该状态
                Logger.e(TAG, "hasShow");
            } else {
                if (dynamicHeightDiff == mSoftKeyboardHeight) {
                    Logger.w(TAG, "has  full show>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
                // 当前的软键盘的将要显示，开始切换应用 UI
                isShow = true;
                Logger.e(TAG, "startShow");
                if (mListener != null) {
                    mListener.onKeyboardShow();
                }

                if (mSoftKeyboardHeight == 0) {
                    mSoftKeyboardHeight = dynamicHeightDiff;
                }
            }

        } else {
            if (isShow) {
                // 软键盘开始影藏
                isShow = false;
                if (mListener != null) {
                    mListener.onKeyboardHide();
                }
                Logger.e(TAG, "stopShow");
            } else {
                // 软键盘之前已经处于隐藏状态，忽略该状态
                Logger.e(TAG, "hasHide");
            }
        }
    }

    /**
     * 设置一个监听软键盘显示隐藏状态监听器
     * @param listener
     */
    public void setListener(OnSoftKeyboardStateChangeListener listener)
    {
        mListener = listener;
    }

    /**
     * 获得软键盘的高度.
     * <p>
     * <b>必须先前显示过软键盘才能获得真实的软键盘高度值，否则为 0</b>
     *
     * @return 软键盘的高度
     */
    public int getSoftKeyboardHeight() {
        return mSoftKeyboardHeight;
    }

    /**
     * 软键盘状态改变监听器，用于监听软键盘的显示和隐藏状态
     */
    public interface OnSoftKeyboardStateChangeListener {
        /**
         * 软键盘显示时的回调
         */
        void onKeyboardShow();

        /**
         * 软键盘影藏时的回调
         */
        void onKeyboardHide();
    }
}
