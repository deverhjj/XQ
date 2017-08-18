package com.binfenjiari.widget.calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/3/29
 */
public class MonthView extends ViewGroup {
    private static final int DEFAULT_DAYS_IN_WEEK = 7;
    final OnClickListener dayClickListener;
    private CalendarCallback callback;
    private OnDateChangedListener mListener;
    private final boolean starsOnSunday;
    private int pagerPosition;
    private ArrayList<DayView> dayViews;
    private ArrayList numbers;
    private CalendarDay calendarDay;
    final DisplayMetrics metrics;
    private int offset;
    public MonthView(Context context, CalendarDay calendarDay, boolean startsOnSunday,
                     @NonNull CalendarCallback callback, OnDateChangedListener listener, int pagerPosition, ArrayList numbers) {
        super(context);
        this.metrics = getResources().getDisplayMetrics();
        this.dayClickListener = new ClickListener();
        this.calendarDay = calendarDay;
        this.callback = callback;
        this.starsOnSunday = startsOnSunday;
        this.mListener=listener;
        this.pagerPosition = pagerPosition;
        this.numbers=numbers;
        this.dayViews = new ArrayList();
        init();
    }

    private void init() {
        this.offset = CalendarUtils.getDayOfWeek(this.calendarDay.getCalendar(), this.starsOnSunday) -1;
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addHeaders();
        int lastDay = CalendarUtils.getEndOfMonth(this.calendarDay.getCalendar());
        Calendar calendarperMonth=Calendar.getInstance();
        calendarperMonth.set(this.calendarDay.getYear(),calendarDay.getMonth(),calendarDay.getDay());
        calendarperMonth.add(Calendar.MONTH,-1);
//        calendarperMonth.set(this.calendarDay.getYear(), calendarDay.getMonth()-1,1);
        int perLastDay=calendarperMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("lastday",perLastDay+"");
//        for (int i = perLastDay; i >perLastDay-offset ; i--) {
//            calendarperMonth.set(this.calendarDay.getYear(), this.calendarDay.getMonth(),i);
//            CalendarDay cur = CalendarDay.from(calendarperMonth);
//            addHintView(cur);
//        }
        for (int i = perLastDay-offset; i <=perLastDay ; i++) {
            calendarperMonth.set(this.calendarDay.getYear(), this.calendarDay.getMonth()-1,i);
            CalendarDay cur = CalendarDay.from(calendarperMonth);
            addHintView(cur);
        }
        for (int i = 1; i <= lastDay; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(this.calendarDay.getYear(), this.calendarDay.getMonth(), i);
            CalendarDay cur = CalendarDay.from(calendar);
            int number= (int) numbers.get(i);
            addDayView(cur,number);
        }
        int endMonthdayoffset;
        if(getChildCount()/7==6){
             endMonthdayoffset=getChildCount()%7==0?7:(7-getChildCount()%7);
        }else {
            endMonthdayoffset=7+(getChildCount()%7==0?0:(7-getChildCount()%7));
        }
        for (int i = 1; i <=endMonthdayoffset ; i++) {
            Calendar calendarEndMonth=Calendar.getInstance();
            calendarEndMonth.set(this.calendarDay.getYear(), this.calendarDay.getMonth(),i);
            CalendarDay cur = CalendarDay.from(calendarEndMonth);
            addHintView(cur);
        }
        refreshEvents();
    }
    private void addDayView(CalendarDay day,int number) {
        DayView dayView = new DayView(getContext(), day,number);
        Calendar p=day.getCalendar();
        Calendar n=Calendar.getInstance();
        if((p.get(Calendar.YEAR)==n.get(Calendar.YEAR))&&(p.get(Calendar.MONTH)==n.get(Calendar.MONTH))&&(p.get(Calendar.DAY_OF_MONTH)==n.get(Calendar.DAY_OF_MONTH))){
            dayView.setSelected(true);
            dayView.isToday=true;
        }
        dayView.setOnClickListener(this.dayClickListener);
        this.dayViews.add(dayView);
        addView(dayView);
    }
    private void addHintView(CalendarDay day) {
        DayView dayView = new DayView(getContext(), day);
        dayView.setEnabled(false);
        addView(dayView);
    }
    public void refreshNumber(ArrayList numbers){
        for (int i = 0; i < 10; i++) {
            numbers.add(0);
        }
        for (int i = 0; i < dayViews.size(); i++) {
            DayView dayView=dayViews.get(i);
            int n= (int) numbers.get(i);
            dayView.setNumber(n);
            if(n==0){
                dayView.setClickable(false);
            }
        }
    }
    public void refreshMoney(ArrayList numbers){
        for (int i = 0; i < 10; i++) {
            numbers.add(0);
        }
        for (int i = 0; i < dayViews.size(); i++) {
            DayView dayView=dayViews.get(i);
            double n= Double.parseDouble(String.valueOf(numbers.get(i))) ;
            dayView.setMoney(n);
            if(n==0){
                dayView.setClickable(false);
            }
        }
    }
    public void refreshEvents() {
        if (this.callback != null && this.callback.getIndicatorsVisible() &&
                this.callback.getEvents() != null && this.callback.getEvents().size() > 0) {
            Iterator it = this.dayViews.iterator();
            int year = this.calendarDay.getYear();
            int month = this.calendarDay.getMonth() + 1;

            while (it.hasNext()) {
                DayView v = (DayView) it.next();
                int day = v.getDay().getDay();
                CalendarDay today = CalendarDay.from(day, month, year);

                for (int i = 0; i < this.callback.getEvents().size(); i++) {
                    CalendarDay event = this.callback.getEvents().get(i);
                    if (event.equals(today)) {
                    }
                }

            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("CalendarPagerView should never be left to decide it's size");
        }
        int measureTileWidth = (specWidthSize - (((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, this.metrics)) * 2)) / DEFAULT_DAYS_IN_WEEK;
        int measureTileHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30.0f, getResources().getDisplayMetrics());
        int specHeightSize= (int) ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60.0f, getResources().getDisplayMetrics())*0.9);
        setMeasuredDimension(specWidthSize, (specHeightSize *7) - ((int)( TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.0f, this.metrics)*1.6)));

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            if(i<7){
                getChildAt(i).measure(MeasureSpec.makeMeasureSpec(measureTileWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(measureTileHeight, MeasureSpec.EXACTLY)/2);
            }else {
                getChildAt(i).measure(MeasureSpec.makeMeasureSpec(measureTileWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(measureTileHeight, MeasureSpec.EXACTLY));
            }

        }

    }


    private void addHeaders() {
        int i = 1;
        TextView tv= new TextView(getContext());
        try {
            tv.setText(new CalendarUtils.Day(Integer.valueOf(7)).getShortName(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        tv.setPadding(0,0,0,100);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(ContextCompat.getColor(this.getContext(), R.color.textColorDark));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f);
        tv.setAllCaps(true);
        addView(tv);
        while (i < DEFAULT_DAYS_IN_WEEK) {
            int actual = this.starsOnSunday ? i == 1 ? DEFAULT_DAYS_IN_WEEK : i - 1 : i;
            TextView textView = new TextView(getContext());

            try {
                textView.setText(new CalendarUtils.Day(Integer.valueOf(actual)).getShortName(getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
//            textView.setPadding(0,0,0,100);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.textColorDark));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f);
            textView.setAllCaps(true);
            addView(textView);
            i++;
        }
    }
    public CalendarDay getCalendarDay() {
        return calendarDay;
    }
    public void setCalendarDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, this.metrics);
        int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, this.metrics);
        int count = getChildCount();
//        int offset = this.offset;
        int offset = 0;
        int headerOffset = 0;
        int childTop = marginTop*2;
        int h=0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = (int) (child.getMeasuredHeight()*1.8);
            int childLeft;
            if (child instanceof TextView) {
                childLeft = (width * headerOffset) + marginLeft;
                child.layout(childLeft, 0, childLeft + width, height);
                headerOffset++;
            } else if (child instanceof DayView) {

                childLeft = (width * offset) + marginLeft;
                child.layout(childLeft,  childTop, childLeft + width, height  + childTop);
                offset++;
                if (offset >= DEFAULT_DAYS_IN_WEEK) {
                    offset = 0;
                    childTop += height;
                }
            }
        }
    }
    public void refreshSelection(int day, int month, int year) {
        boolean select;
        if (this.calendarDay.getMonth() == month && this.calendarDay.getYear() == year) {
            select = true;
        } else {
            select = false;
        }

        Iterator it = this.dayViews.iterator();

        while (it.hasNext()) {
            boolean z;

            DayView v = (DayView) it.next();

            if (select && v.getDay().getDay() == day) {
                z = true;
            } else {
                z = false;
            }
            if(v.isToday){
                TextView tv_number=v.getTv_number();
                tv_number.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorHint));
            }
            v.setSelected(z);
        }
    }
    class ClickListener implements OnClickListener {
        ClickListener() {
        }

        @Override
        public void onClick(View view) {
            if (view instanceof DayView) {
                CalendarDay day = ((DayView) view).getDay();
                if (day != null) {
                    view.setSelected(true);
                    if (MonthView.this.mListener != null) {
                        MonthView.this.mListener.onDateChanged(day.getDate());
                    }
                }
            }
        }
    }
}
