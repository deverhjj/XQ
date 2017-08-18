package com.github.huajianjiang.alphaslidebar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import static com.github.huajianjiang.alphaslidebar.TextViewDrawableHelper.DrawableType.RIGHT;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/6/25
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class SimpleSearchView extends AppCompatEditText {

    private static final String TAG = SimpleSearchView.class.getSimpleName();

    public SimpleSearchView(Context context) {
        this(context, null);
    }

    public SimpleSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public SimpleSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TextViewDrawableHelper helper = TextViewDrawableHelper.with(this).listenDrawableClick(
                new TextViewDrawableHelper.OnDrawableClickListener() {
                    @Override public void onClick(TextView container,
                                                  TextViewDrawableHelper.DrawableType type,
                                                  Drawable drawable)
                    {
                        if (type == RIGHT) {
                            setText("");//清空输入的内容
                        }
                    }
                });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                final boolean hasChar = s.length() > 0;
                Log.e(TAG, "hasChar=" + hasChar);
                if (!hasChar) {
                    getCompoundDrawables()[2].setBounds(0, 0, 0, 0);
                } else {
                    getCompoundDrawables()[2]
                            .setBounds(0, 0, getCompoundDrawables()[2].getIntrinsicWidth(),
                                       getCompoundDrawables()[2].getIntrinsicHeight());
                }
            }
        });

        setText("");
    }

}
