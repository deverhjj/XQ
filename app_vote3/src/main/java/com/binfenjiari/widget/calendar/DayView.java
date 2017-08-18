package com.binfenjiari.widget.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.Utils;


public class DayView extends LinearLayout {
    public static final float DEFAULT_HEIGHT = 30.0f;
    private static final float DEFAULT_TEXT_SIZE = 12.0f;
    private static Integer _measuredHeight;
    public  boolean isToday=false;
    private int currColor=getResources().getColor(R.color.textColorDark);
    static {
        _measuredHeight = null;
    }

    private CalendarDay day;
    private boolean selected;
    private DisplayMetrics metrics;
    private TextView tvDay;
    private TextView mTv_number;

    public DayView(Context context) {
        super(context);
    }

    public DayView(Context context, CalendarDay day, int number) {
        super(context);
        this.metrics = getResources().getDisplayMetrics();
        this.day = day;
        init(number);
    }

    public DayView(Context context, CalendarDay day) {
        super(context);
        this.metrics = getResources().getDisplayMetrics();
        this.day = day;
        init();
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private static Drawable generateCircleDrawable(int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        return drawable;
    }

    private void init(int number) {
        this.selected = false;
        setOrientation(VERTICAL);
        this.tvDay = new TextView(getContext());
        this.tvDay.setText("   " + String.format("%d", new Object[]{Integer.valueOf(this.day.getDay())}));
        this.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorDark));
        LayoutParams params = new LayoutParams(getDefaultMeasuredHeight(), getDefaultMeasuredHeight());
        params.gravity = Gravity.CENTER_HORIZONTAL;
        this.tvDay.setLayoutParams(params);
        mTv_number = new TextView(getContext());
        mTv_number.setText("");//场 number +
        mTv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        mTv_number.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorDark));
        currColor= ContextCompat.getColor(getContext(), R.color.textColorDark);
        LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, getDefaultMeasuredHeight());
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        mTv_number.setLayoutParams(params1);
        setBackground(getResources().getDrawable(R.drawable.bg_dayview_stoken));
        addView(this.tvDay);
        addView(mTv_number);
        LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(p);
    }

    private void init() {
        this.selected = false;
        setOrientation(VERTICAL);
        this.tvDay = new TextView(getContext());
//        this.tvDay.setText("   "+String.format("%d", new Object[]{Integer.valueOf(this.day.getDay())}));
        this.tvDay.setText("" + this.day.getDay());
        this.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorHint));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, getDefaultMeasuredHeight());
        params.gravity = Gravity.CENTER_HORIZONTAL;
        this.tvDay.setLayoutParams(params);
        mTv_number = new TextView(getContext());
        mTv_number.setText("");//0场
        mTv_number.setVisibility(INVISIBLE);
        mTv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        mTv_number.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorHint));
        currColor= ContextCompat.getColor(getContext(), R.color.textColorHint);
        LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, getDefaultMeasuredHeight());
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        mTv_number.setLayoutParams(params1);
        setBackground(getResources().getDrawable(R.drawable.bg_dayview_stoken));
        addView(this.tvDay);
        addView(mTv_number);
        LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(p);
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
                mTv_number.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                setBackground(getResources().getDrawable(R.drawable.bg_dayview_selected));
                return;
            } else {
                if(isToday){
                    this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    mTv_number.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorHint));
                    mTv_number.setText("今天");
                    setBackground(getResources().getDrawable(R.drawable.bg_dayview_stoken));
                }else {
                    setBackground(getResources().getDrawable(R.drawable.bg_dayview_stoken));
                    this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorDark));
                    mTv_number.setTextColor(currColor);
                }
            }
        }
    }

    public void setCur() {

        this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mTv_number.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        isToday=true;

    }

    public void setNumber(int number) {
        mTv_number.setText( number + "个活动");//场
        mTv_number.setGravity(Gravity.CENTER);
        if(number==0){
            mTv_number.setText("");
            mTv_number.setTextColor(getResources().getColor(R.color.textColorHint));
            currColor= ContextCompat.getColor(getContext(), R.color.textColorHint);
        }else {
            mTv_number.setTextColor(getResources().getColor(R.color.colorAccent));
            currColor= ContextCompat.getColor(getContext(), R.color.colorAccent);
        }
        if(isSelected()){
            mTv_number.setTextColor(getResources().getColor(R.color.white));
            currColor= ContextCompat.getColor(getContext(), R.color.textColorHint);
        }

    }

    public void setMoney(double number) {
        mTv_number.setText("¥" + Utils.DecimalPoint(number));
        mTv_number.setTextSize(10);
        mTv_number.setGravity(Gravity.CENTER);
        if(number!=0){
            mTv_number.setTextColor(getResources().getColor(R.color.colorAccent));
            if(isToday){
                mTv_number.setTextColor(getResources().getColor(R.color.white));
            }
            currColor= ContextCompat.getColor(getContext(), R.color.colorAccent);
        }else {
            mTv_number.setVisibility(INVISIBLE);
        }
    }
    private int getDefaultMeasuredHeight() {
        if (_measuredHeight == null) {
            _measuredHeight = Integer.valueOf((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEIGHT, this.metrics));
        }
        return _measuredHeight.intValue();
    }
    public TextView getTv_number(){
        return this.mTv_number;
    }
}
