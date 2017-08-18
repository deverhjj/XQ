package com.github.huajianjiang.alphaslidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/4
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AlphaSlidebar extends View {
    public static final int STATE_CAPTURED = 0;
    public static final int STATE_FINISHED = 1;
    private static final String TAG = AlphaSlidebar.class.getSimpleName();
    private static final int DEFAULT_LETTER_TEXT_SIZE = 14;//SP
    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private Paint mLetterPaint;
    private float mLetterSize;
    private float mFixedLetterH;

    private OnLetterTouchListener mListener;

    public AlphaSlidebar(Context context) {
        this(context, null);
    }

    public AlphaSlidebar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaSlidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLetterSize = getResources().getDisplayMetrics().density * DEFAULT_LETTER_TEXT_SIZE;
        mLetterPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mLetterPaint.setTextSize(mLetterSize);
        mLetterPaint.setTypeface(Typeface.DEFAULT);
        mFixedLetterH = mLetterPaint.descent() - mLetterPaint.ascent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int horPadding = ViewCompat.getPaddingStart(this) + ViewCompat.getPaddingEnd(this);
        int verPadding = getPaddingTop() + getPaddingBottom();
        int sugW = (int) Math
                .max(getSuggestedMinimumWidth(), mLetterPaint.getTextSize() + horPadding);
        int sugH = getSuggestedMinimumHeight() + verPadding;
        int measuredW = getSlidebarSize(sugW, widthMeasureSpec);
        int measuredH = getSlidebarSize(sugH, heightMeasureSpec);
        setMeasuredDimension(measuredW, measuredH);
    }

    private int getSlidebarSize(int size, int measureSpec) {
        int result = size;
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(Math.max(size, specSize), Math.min(size, specSize));
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float fixedLetterH = mFixedLetterH;
        float x = 0;
        float y = getPaddingTop();

        for (int i = 0; i < LETTERS.length; i++) {
            String letter = LETTERS[i];
            float letterW = mLetterPaint.measureText(letter);
            x = (getWidth() - letterW) / 2.0f;
            y += fixedLetterH;
            canvas.drawText(letter, x, y, mLetterPaint);
            Logger.e(TAG, "x=" + x + ",y=" + y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        handleTouch(event);
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.onLetterTouch(-1, "", STATE_FINISHED);
                }
                break;
        }
        return true;
    }

    private boolean handleTouch(MotionEvent event) {
        float y = event.getY() - getPaddingTop();
        int index = (int) (y / mFixedLetterH);
        Logger.e(TAG, "y=" + y + ",mFixedLetterH=" + mFixedLetterH);
        if (index >= 0 && index < LETTERS.length) {
            Logger.e(TAG, "touched letter>>" + LETTERS[index]);
            if (mListener != null) {
                mListener.onLetterTouch(index, LETTERS[index], STATE_CAPTURED);
            }
            return true;
        }
        return false;
    }

    public void setOnLetterTouchListener(OnLetterTouchListener listener) {
        mListener = listener;
    }

    public interface OnLetterTouchListener {
        void onLetterTouch(int index, String letter, int state);
    }
}
