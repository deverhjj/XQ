package com.github.huajianjiang.alphaslidebar;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/6/25
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class TextViewDrawableHelper {
    private static final String TAG = TextViewDrawableHelper.class.getSimpleName();
    private final Rect mCompoundRect = new Rect();
    private OnDrawableClickListener mDrawableClickListener;
    private WeakReference<TextView> mTextView;

    public TextViewDrawableHelper(final TextView textView) {
        mTextView = new WeakReference<>(textView);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                final int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 如果是 EditText 的话不能拦截该事件,因为 EditText 需要处理文本的操作逻辑
                        return !(textView instanceof EditText);
                    case MotionEvent.ACTION_UP:
                        return checkCompound(event);
                    default:
                        return false;
                }
            }
        });
    }

    public static TextViewDrawableHelper with(@NonNull TextView textView) {
        return new TextViewDrawableHelper(textView);
    }

    //public static TextViewDrawableHelper

    private boolean checkCompound(MotionEvent event) {
        TextView tv = mTextView.get();
        if (tv == null) return false;
        Drawable[] drawables = tv.getCompoundDrawables();
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int left = tv.getLeft();
        final int top = tv.getTop();
        final int right = tv.getRight();
        final int bottom = tv.getBottom();
        final int compoundPaddingLeft = tv.getCompoundPaddingLeft();
        final int compoundPaddingTop = tv.getCompoundPaddingTop();
        final int compoundPaddingRight = tv.getCompoundPaddingRight();
        final int compoundPaddingBottom = tv.getCompoundPaddingBottom();

        int index = 0;
        for (Drawable dr : drawables) {
            if (dr != null) {
                dr.copyBounds(mCompoundRect);

                Log.e(TAG, "mCompoundRect==>" + mCompoundRect.flattenToString());

                int compoundX;
                int compoundY;

                if (index == DrawableType.LEFT.ordinal() || index == DrawableType.RIGHT.ordinal()) {
                    final int vspace = bottom - top - compoundPaddingTop - compoundPaddingBottom;
                    compoundY = (vspace - mCompoundRect.height()) / 2 + compoundPaddingTop;

                    Log.e(TAG, "vspace=" + vspace + ",CompoundH=" + mCompoundRect.height());

                    if (index == DrawableType.LEFT.ordinal()) {
                        compoundX = tv.getPaddingLeft();
                    } else {
                        compoundX = right - left - tv.getPaddingRight() - mCompoundRect.width();
                    }

                } else {
                    final int hspace = right - left - compoundPaddingRight - compoundPaddingLeft;
                    compoundX = (hspace - mCompoundRect.width()) / 2 + compoundPaddingLeft;

                    if (index == DrawableType.TOP.ordinal()) {
                        compoundY = tv.getPaddingTop();
                    } else {
                        compoundY = bottom - top - tv.getPaddingBottom() - mCompoundRect.height();
                    }
                }

                final int checkX = x - compoundX;
                final int checkY = y - compoundY;

                if (mCompoundRect.contains(checkX, checkY)) {
                    Log.e(TAG, "hit drawable==>" + index);
                    tv.playSoundEffect(SoundEffectConstants.CLICK);
                    if (mDrawableClickListener != null) {
                        mDrawableClickListener.onClick(tv, DrawableType.getTypeOrdinal(index), dr);
                    }
                    return true;
                }
            }
            index++;
        }
        return false;
    }

    public TextViewDrawableHelper listenDrawableClick(OnDrawableClickListener listener)
    {
        mDrawableClickListener = listener;
        return this;
    }

    public enum DrawableType {
        LEFT, TOP, RIGHT, BOTTOM;

        static DrawableType getTypeOrdinal(int ordinal) {
            switch (ordinal) {
                case 0:
                    return LEFT;
                case 1:
                    return TOP;
                case 2:
                    return RIGHT;
                case 3:
                    return BOTTOM;
                default:
                    throw new RuntimeException("can not find DrawableType for ordinal: " + ordinal);
            }
        }
    }

    public interface OnDrawableClickListener {
        void onClick(TextView container, DrawableType type, Drawable drawable);
    }
}
