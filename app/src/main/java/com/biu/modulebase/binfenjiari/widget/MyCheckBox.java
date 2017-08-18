package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by jhj_Plus on 2016/6/1.
 */
public class MyCheckBox extends CheckBox {
    private static final String TAG = "MyCheckBox";

    public MyCheckBox(Context context) {
        super(context);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
    }
}
