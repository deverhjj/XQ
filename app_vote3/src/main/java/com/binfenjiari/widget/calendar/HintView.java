package com.binfenjiari.widget.calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binfenjiari.R;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/4/5
 */
public class HintView extends LinearLayout{
    public static final float DEFAULT_HEIGHT = 30.0f;
    private static final float DEFAULT_TEXT_SIZE = 12.0f;
    private static Integer _measuredHeight;

    static {
        _measuredHeight = null;
    }

    private CalendarDay day;
    private boolean selected;
    private DisplayMetrics metrics;
    private TextView tvDay;

    public HintView(Context context,CalendarDay day) {
        super(context);
        this.metrics = getResources().getDisplayMetrics();
        this.day = day;
        init();
    }
    private void init() {
        this.selected = false;
        setOrientation(VERTICAL);
        this.tvDay = new TextView(getContext());
        this.tvDay.setText("   "+String.format("%d", new Object[]{Integer.valueOf(this.day.getDay())}));
        this.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorHint));
        LayoutParams params = new LayoutParams(getDefaultMeasuredHeight()*2, getDefaultMeasuredHeight());
        params.gravity = Gravity.CENTER_HORIZONTAL;
        setClickable(false);
        this.tvDay.setLayoutParams(params);
        this.tvDay.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.text_orange));

    }
    public CalendarDay getDay() {
        return this.day;
    }

    public String getText() {
        return this.tvDay.getText().toString();
    }

    public boolean isSelected() {
        return this.selected;
    }

    @SuppressWarnings("deprecation")
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;

            if (this.selected) {
                this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                setBackground(getResources().getDrawable(R.drawable.bg_dayview_selected));
                return;
            }else {
                setBackground(getResources().getDrawable(R.drawable.bg_dayview_stoken));
                this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorDark));
            }

        }
    }





    private int getDefaultMeasuredHeight() {
        if (_measuredHeight == null) {
            _measuredHeight = Integer.valueOf((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEIGHT, this.metrics));
        }
        return _measuredHeight.intValue();
    }
}
