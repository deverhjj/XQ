package com.biu.modulebase.binfenjiari.widget.wheeltime;

import android.annotation.SuppressLint;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hour;
    private WheelView wv_minute;
    public int screenheight;
    private int START_YEAR = 1900, END_YEAR = 2500;
    private int START_MONTH = 1, END_MONTH = 12;
    private int START_DAY = 1, END_DAY = 31;
    private int START_HOUR = 0, END_HOUR = 23;
    private int START_MINUTE = 0, END_MINUTE = 59;

    private int curYear = 0;
    private int curMonth = 0;
    private int curDay = 0;
    private int curHour = 0;
    private int curMinute = 0;

    // 定义一些时间选择的类型。
    public static String TYPE_YYMMDD = "YYMMDD";
    public static String TYPE_YYMMDDHHmm = "YYMMDDHHmm";
    public static String TYPE_YYMMDDHHmmSS = "YYMMDDHHmmSS";
    public static String TYPE_MMDDHHmmSS = "MMDDHHmmSS";
    public static String TYPE_MMDDHHmm = "MMDDHHmm";
    public static String TYPE_HHmm = "HHmm";

    // 当前选用的时间类型
    private String timetype = TYPE_YYMMDDHHmmSS;

    private NumericWheelAdapter yearAdapter;
    private NumericWheelAdapter monthAdapter;
    private NumericWheelAdapter dayAdapter;
    private NumericWheelAdapter hourAdapter;
    private NumericWheelAdapter minuteAdapter;

    String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
    String[] months_little = { "4", "6", "9", "11" };
    final List<String> list_big = Arrays.asList(months_big);
    final List<String> list_little = Arrays.asList(months_little);

    private int textsize = 15;

    public WheelMain(View view) {
	super();
	this.view = view;
	this.timetype = TYPE_YYMMDDHHmmSS;
	setView(view);
    }

    public WheelMain(View view, String type) {
	super();
	this.view = view;
	this.timetype = type;
	setView(view);
    }

    /***
     * 初始化时间
     * 
     * @param starttime
     *            限制开始时间
     * @param endtime
     *            限制结束时间
     * @param defaluttime
     *            默认选中时间
     */
    @SuppressLint("SimpleDateFormat")
    private void parseTime(String starttime, String endtime, String defaluttime) {
	SimpleDateFormat dateFormatYYYY = new SimpleDateFormat("yyyy");
	SimpleDateFormat dateFormatMM = new SimpleDateFormat("MM");
	SimpleDateFormat dateFormatDD = new SimpleDateFormat("dd");
	SimpleDateFormat dateFormatHH = new SimpleDateFormat("HH");
	SimpleDateFormat dateFormatmm = new SimpleDateFormat("mm");
	if (!Utils.isEmpty(starttime)) {
	    this.START_YEAR = Utils.isInteger(dateFormatYYYY.format(Utils
		    .StrToDate2(starttime)));
	    this.START_MONTH = Utils.isInteger(dateFormatMM.format(Utils
		    .StrToDate2(starttime)));
	    this.START_DAY = Utils.isInteger(dateFormatDD.format(Utils
		    .StrToDate2(starttime)));
	    this.START_HOUR = Utils.isInteger(dateFormatHH.format(Utils
		    .StrToDate2(starttime)));
	    this.START_MINUTE = Utils.isInteger(dateFormatmm.format(Utils
		    .StrToDate2(starttime)));
	}
	if (!Utils.isEmpty(endtime)) {
	    this.END_YEAR = Utils.isInteger(dateFormatYYYY.format(Utils
		    .StrToDate2(endtime)));
	    this.END_MONTH = Utils.isInteger(dateFormatMM.format(Utils
		    .StrToDate2(endtime)));
	    this.END_DAY = Utils.isInteger(dateFormatDD.format(Utils
		    .StrToDate2(endtime)));
	    this.END_HOUR = Utils.isInteger(dateFormatHH.format(Utils
		    .StrToDate2(endtime)));
	    this.END_MINUTE = Utils.isInteger(dateFormatmm.format(Utils
		    .StrToDate2(endtime)));
	}
	if (!Utils.isEmpty(defaluttime)) {
	    this.curYear = Utils.isInteger(dateFormatYYYY.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curMonth = Utils.isInteger(dateFormatMM.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curDay = Utils.isInteger(dateFormatDD.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curHour = Utils.isInteger(dateFormatHH.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curMinute = Utils.isInteger(dateFormatmm.format(Utils
		    .StrToDate2(defaluttime)));
	} else {
	    defaluttime = Utils.getCurrentDate2();
	    this.curYear = Utils.isInteger(dateFormatYYYY.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curMonth = Utils.isInteger(dateFormatMM.format(Utils
		    .StrToDate2(defaluttime))) - 1;
	    this.curDay = Utils.isInteger(dateFormatDD.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curHour = Utils.isInteger(dateFormatHH.format(Utils
		    .StrToDate2(defaluttime)));
	    this.curMinute = Utils.isInteger(dateFormatmm.format(Utils
		    .StrToDate2(defaluttime)));
	}
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void initDateTimePicker(String starttime, String endtime,
	    String defaluttime) {

	parseTime(starttime, endtime, defaluttime);

	// 年
	wv_year = (WheelView) view.findViewById(R.id.biu_select_tiem_year);
	wv_year.setLabel("年");// 添加文字
	initWheelYear();
	// 月
	wv_month = (WheelView) view.findViewById(R.id.biu_select_tiem_month);
	wv_month.setLabel("月");// 添加文字
	initWheelMonth();
	// 日
	wv_day = (WheelView) view.findViewById(R.id.biu_select_tiem_day);
	wv_day.setLabel("日");// 添加文字
	initWheelDay();
	// 时
	wv_hour = (WheelView) view.findViewById(R.id.biu_select_tiem_hour);
	wv_hour.setLabel("时");// 添加文字
	initWheelHour();
	// 分
	wv_minute = (WheelView) view.findViewById(R.id.biu_select_tiem_min);
	wv_minute.setLabel("分");// 添加文字
	initWheelMinute();

	// 设置字体大小
	setTextSize();

	if (timetype.equals(TYPE_YYMMDD)) {
	    wv_hour.setVisibility(View.GONE);
	    wv_minute.setVisibility(View.GONE);
	} else if (timetype.equals(TYPE_MMDDHHmm)) {
	    wv_year.setVisibility(View.GONE);
	} else if (timetype.equals(TYPE_HHmm)) {
	    wv_year.setVisibility(View.GONE);
	    wv_month.setVisibleItems(View.GONE);
	    wv_month.setVisibility(View.GONE);
	    wv_day.setVisibleItems(View.GONE);
	    wv_day.setVisibility(View.GONE);
	}

	wv_year.addChangingListener(wheelListener_year);
	wv_month.addChangingListener(wheelListener_month);
	wv_day.addChangingListener(wheelListener_day);
	wv_hour.addChangingListener(wheelListener_hour);

    }

    /**
     * 初始化年
     */
    public void initWheelYear() {
	yearAdapter = new NumericWheelAdapter(START_YEAR, END_YEAR);
	wv_year.setAdapter(yearAdapter);// 设置"年"的显示数据
	wv_year.setCyclic(false);// 不可循环滚动
	if (curYear >= START_YEAR && curYear <= END_YEAR) {// 当前年份大于限定时
	    wv_year.setCurrentItem(curYear - START_YEAR);// 应该默认是当前年份，所以默认下标是year
							 // - START_YEAR
	} else if (curYear < START_YEAR) {
	    wv_year.setCurrentItem(0);// 应该默认是限定年份，所以默认下标是0
	} else {
	    // 限定时间在当前年份之前
	    wv_year.setCurrentItem(END_YEAR - START_YEAR);
	}
    }

    /**
     * 初始化月份
     */
    public void initWheelMonth() {
	// year_num为选中的年
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	if (year_num == START_YEAR && year_num == END_YEAR) {
	    // 进入限制年，设定月份（默认START_MONTH 小于 END_MONTH ，恶意情况不考虑）
	    monthAdapter = new NumericWheelAdapter(START_MONTH, END_MONTH);
	    wv_month.setAdapter(monthAdapter);
	    wv_month.setCyclic(false);
	    if (curMonth + 1 < START_MONTH) {
		// 当前月份小于开始限定月，默认限定月
		wv_month.setCurrentItem(0);
	    } else if (START_MONTH <= curMonth + 1 && curMonth + 1 <= END_MONTH) {
		// 当前月份大于限定月 默认当前月
		wv_month.setCurrentItem(curMonth + 1 - START_MONTH);
	    } else {
		// 只剩下一种情况了 END_MONTH < curMonth + 1
		wv_month.setCurrentItem(END_MONTH - START_MONTH);
	    }
	} else if (year_num == START_YEAR && year_num != END_YEAR) {
	    // 进入限制年，设定月份（默认START_MONTH 小于 END_MONTH ）
	    monthAdapter = new NumericWheelAdapter(START_MONTH, 12);
	    wv_month.setAdapter(monthAdapter);
	    wv_month.setCyclic(false);
	    if (curMonth + 1 <= START_MONTH) {
		// 当前月份小于开始限定月，默认限定月
		wv_month.setCurrentItem(0);
	    } else {
		// 当前月份大于限定月 默认当前月
		wv_month.setCurrentItem(curMonth + 1 - START_MONTH);
	    }
	} else if (year_num == END_YEAR && year_num != START_YEAR) {
	    // 进入限制年，设定月份（默认START_MONTH 小于 END_MONTH ）
	    monthAdapter = new NumericWheelAdapter(1, END_YEAR);
	    wv_month.setAdapter(monthAdapter);
	    wv_month.setCyclic(false);
	    if (curMonth + 1 > END_MONTH) {
		// 当前月份小于开始限定月，默认限定月
		wv_month.setCurrentItem(0);
	    } else {
		// 当前月份大于限定月 默认当前月
		wv_month.setCurrentItem(END_MONTH - curMonth + 1);
	    }
	} else {
	    // 不是限制年，不需要判断
	    monthAdapter = new NumericWheelAdapter(1, 12);
	    wv_month.setAdapter(monthAdapter);
	    wv_month.setCurrentItem(curMonth);
	    wv_month.setCyclic(true);
	}
    }

    /**
     * 初始化天数
     */
    public void initWheelDay() {
	// 如果等于限制年和限制月，判断限制时间和实际时间
	// year_num为选中的年
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	if ((year_num == START_YEAR && year_num == END_YEAR)
		&& (month_num == START_MONTH && month_num == END_MONTH)) {
	    // 进入限制年限制月中
	    setDayAdapter(START_DAY, month_num, true);
	    wv_day.setCyclic(false);
	    if (curDay < START_DAY) {
		wv_day.setCurrentItem(0);
	    } else if (START_DAY <= curDay && curDay <= END_DAY) {
		// 当前日大于限制日，默认当前日
		wv_day.setCurrentItem(curDay - START_DAY);
	    } else {
		// 只剩下一种情况END_DAY < curDay
		wv_day.setCurrentItem(END_DAY - START_DAY);
	    }
	} else if (year_num == START_YEAR && month_num == START_MONTH) {
	    setDayAdapter(START_DAY, month_num, false);
	    wv_day.setCyclic(false);
	    if (curDay < START_DAY) {
		wv_day.setCurrentItem(0);
	    } else {
		wv_day.setCurrentItem(curDay - START_DAY);
	    }
	} else if (year_num == END_YEAR && month_num == END_MONTH) {
	    setDayAdapter(1, month_num, true);
	    wv_day.setCyclic(false);
	    if (curDay < END_DAY) {
		wv_day.setCurrentItem(curDay - 1);
	    } else {
		wv_day.setCurrentItem(curDay - END_DAY);
	    }
	} else {
	    setDayAdapter(1, month_num, false);
	    wv_day.setCurrentItem(curDay - 1);
	    wv_day.setCyclic(true);
	}
    }

    /**
     * 初始化小时
     */
    public void initWheelHour() {
	// year_num为选中的年
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		.getCurrentItem()));
	if (year_num == START_YEAR && year_num == END_YEAR
		&& month_num == START_MONTH && month_num == END_MONTH
		&& day_num == START_DAY && day_num == END_DAY) {
	    // 进入限制年限制月限制天数中
	    hourAdapter = new NumericWheelAdapter(START_HOUR, END_HOUR);
	    wv_hour.setAdapter(hourAdapter);
	    wv_hour.setCyclic(false);
	    if (curHour < START_HOUR) {
		wv_hour.setCurrentItem(0);
	    } else if (START_HOUR <= curHour && curHour <= END_HOUR) {
		// 当前日大于限制日，默认当前日
		wv_hour.setCurrentItem(curHour - START_HOUR);
	    } else {
		// 只剩下一种情况END_DAY < curDay
		wv_hour.setCurrentItem(END_HOUR - START_HOUR);
	    }
	} else if (year_num == START_YEAR && month_num == START_MONTH
		&& day_num == START_DAY) {
	    hourAdapter = new NumericWheelAdapter(START_HOUR, 23);
	    wv_hour.setAdapter(hourAdapter);
	    wv_hour.setCyclic(false);
	    if (curHour < START_HOUR) {
		wv_hour.setCurrentItem(0);
	    } else {
		wv_hour.setCurrentItem(curHour - START_HOUR);
	    }
	} else if (year_num == END_YEAR && month_num == END_MONTH
		&& day_num == END_DAY) {
	    hourAdapter = new NumericWheelAdapter(0, END_HOUR);
	    wv_hour.setAdapter(hourAdapter);
	    wv_hour.setCyclic(false);
	    if (curHour < END_HOUR) {
		wv_hour.setCurrentItem(curHour);
	    } else {
		wv_hour.setCurrentItem(END_HOUR);
	    }
	} else {
	    hourAdapter = new NumericWheelAdapter(0, 23);
	    wv_hour.setAdapter(hourAdapter);
	    wv_hour.setCurrentItem(curHour);
	    wv_hour.setCyclic(true);
	}
    }

    /**
     * 初始化分钟
     */
    public void initWheelMinute() {
	// year_num为选中的年
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		.getCurrentItem()));
	int hour_num = Utils.isInteger(hourAdapter.getItem(wv_hour
		.getCurrentItem()));
	if (year_num == START_YEAR && year_num == END_YEAR
		&& month_num == START_MONTH && month_num == END_MONTH
		&& day_num == START_DAY && day_num == END_DAY
		&& hour_num == START_HOUR && hour_num == END_HOUR) {
	    // 进入限制年限制月限制天数限制小时中
	    minuteAdapter = new NumericWheelAdapter(START_MINUTE, END_MINUTE);
	    wv_minute.setAdapter(minuteAdapter);
	    wv_minute.setCyclic(false);
	    if (curMinute < START_MINUTE) {
		wv_minute.setCurrentItem(0);
	    } else if (START_MINUTE <= curMinute && curMinute <= END_MINUTE) {
		// 当前日大于限制日，默认当前日
		wv_minute.setCurrentItem(curMinute - START_MINUTE);
	    } else {
		// 只剩下一种情况END_MINUTE < curMinute
		wv_minute.setCurrentItem(END_MINUTE - START_MINUTE);
	    }
	} else if (year_num == START_YEAR && month_num == START_MONTH
		&& day_num == START_DAY && hour_num == START_HOUR) {
	    minuteAdapter = new NumericWheelAdapter(START_MINUTE, 59);
	    wv_minute.setAdapter(minuteAdapter);
	    wv_minute.setCyclic(false);
	    if (curMinute < START_MINUTE) {
		wv_minute.setCurrentItem(0);
	    } else {
		wv_minute.setCurrentItem(curMinute - START_MINUTE);
	    }
	} else if (year_num == END_YEAR && month_num == END_MONTH
		&& day_num == END_DAY && hour_num == END_HOUR) {
	    minuteAdapter = new NumericWheelAdapter(0, END_MINUTE);
	    wv_minute.setAdapter(minuteAdapter);
	    wv_minute.setCyclic(false);
	    if (curMinute > END_MINUTE) {
		wv_minute.setCurrentItem(0);
	    } else {
		wv_minute.setCurrentItem(curMinute);
	    }

	} else {
	    minuteAdapter = new NumericWheelAdapter(0, 59);
	    wv_minute.setAdapter(minuteAdapter);
	    wv_minute.setCurrentItem(curMinute);
	    wv_minute.setCyclic(true);
	}
    }

    /**
     * 添加"年"监听 设置月份
     */
    OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
	    int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		    .getCurrentItem()));
	    int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		    .getCurrentItem()));
	    if (year_num == START_YEAR && year_num == END_YEAR) {
		monthAdapter = new NumericWheelAdapter(START_MONTH, END_MONTH);
		wv_month.setAdapter(monthAdapter);
		wv_month.setCyclic(false);
		if (START_MONTH > month_num) {
		    // 当限定月大于当前月，默认限定月
		    wv_month.setCurrentItem(0);
		} else if (START_MONTH <= month_num && month_num <= END_MONTH) {
		    wv_month.setCurrentItem(month_num - START_MONTH);
		} else {
		    // 当限定月小于当前月，默认当前月
		    wv_month.setCurrentItem(END_MONTH - START_MONTH);
		}
	    } else if (year_num == START_YEAR) {
		monthAdapter = new NumericWheelAdapter(START_MONTH, 12);
		wv_month.setAdapter(monthAdapter);
		wv_month.setCyclic(false);
		if (START_MONTH >= month_num) {
		    // 当限定月大于当前月，默认限定月
		    wv_month.setCurrentItem(0);
		} else {
		    // 当限定月小于当前月，默认当前月
		    wv_month.setCurrentItem(month_num - START_MONTH);
		}
	    } else if (year_num == END_YEAR) {
		monthAdapter = new NumericWheelAdapter(1, END_MONTH);
		wv_month.setAdapter(monthAdapter);
		wv_month.setCyclic(false);
		if (END_MONTH >= month_num) {
		    // 当限定月大于当前月，默认限定月
		    wv_month.setCurrentItem(month_num - 1);
		} else {
		    // 当限定月小于当前月，默认当前月
		    wv_month.setCurrentItem(END_MONTH - 1);
		}
		// }else if(Utils.isInteger(yearAdapter.getItem(oldValue)) ==
		// START_YEAR
		// || Utils.isInteger(yearAdapter.getItem(oldValue)) ==
		// END_YEAR){
		// monthAdapter = new NumericWheelAdapter(1, 12);
		// wv_month.setAdapter(monthAdapter);
		// wv_month.setCyclic(true);
		// wv_month.setCurrentItem(month_num-1);
	    } else {
		monthAdapter = new NumericWheelAdapter(1, 12);
		wv_month.setAdapter(monthAdapter);
		wv_month.setCyclic(true);
		// wv_month.setCurrentItem(wv_month.getCurrentItem()+1);
		wv_month.setCurrentItem(month_num);
		wv_month.setCurrentItem(month_num - 1);
	    }
	    // if(year_num == START_YEAR || year_num == END_YEAR){
	    // monthAdapter = new NumericWheelAdapter(START_MONTH, END_MONTH);
	    // wv_month.setAdapter(monthAdapter);
	    // wv_month.setCyclic(false);
	    // if(START_MONTH >= month_num ){
	    // //当限定月大于当前月，默认限定月
	    // wv_month.setCurrentItem(0);
	    // }else if(month_num > END_MONTH){
	    // //当限定月小于当前月，默认当前月
	    // wv_month.setCurrentItem(END_MONTH - START_MONTH);
	    // }else{
	    // //当限定月小于当前月，默认当前月
	    // wv_month.setCurrentItem(month_num-START_MONTH);
	    // }
	    //
	    // }else if(oldValue == 0){
	    // monthAdapter = new NumericWheelAdapter(1, 12);
	    // wv_month.setAdapter(monthAdapter);
	    // wv_month.setCyclic(true);
	    // wv_month.setCurrentItem(month_num-1);
	    // }else{
	    // monthAdapter = new NumericWheelAdapter(1, 12);
	    // wv_month.setAdapter(monthAdapter);
	    // wv_month.setCyclic(true);
	    // wv_month.setCurrentItem(wv_month.getCurrentItem()+1);
	    // wv_month.setCurrentItem(wv_month.getCurrentItem()-1);
	    // }

	}
    };
    /**
     * 添加"月"监听 来设置日
     */
    OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
	    int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		    .getCurrentItem()));
	    int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		    .getCurrentItem()));
	    int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		    .getCurrentItem()));
	    // if(monthAdapter.getItemsCount() == 12){
	    // newmonth_num = Utils.isInteger(monthAdapter.getItem(newValue));
	    // }else{
	    // newmonth_num = newValue +START_MONTH;
	    // }
	    if (year_num == START_YEAR && year_num == END_YEAR
		    && month_num == START_MONTH && month_num == END_MONTH) {
		setDayAdapter(START_DAY, month_num, true);
		wv_day.setCyclic(false);
		if (day_num < START_DAY) {
		    wv_day.setCurrentItem(0);
		} else if (START_DAY <= day_num && day_num <= END_DAY) {
		    wv_day.setCurrentItem(day_num - START_DAY);
		} else {
		    wv_day.setCurrentItem(END_DAY - START_DAY);
		}
	    } else if (year_num == START_YEAR && month_num == START_MONTH) {
		setDayAdapter(START_DAY, month_num, false);
		wv_day.setCyclic(false);
		if (day_num < START_DAY) {
		    wv_day.setCurrentItem(0);
		} else {
		    wv_day.setCurrentItem(day_num - START_DAY);
		}
	    } else if (year_num == END_YEAR && month_num == END_MONTH) {
		setDayAdapter(1, month_num, true);
		wv_day.setCyclic(false);
		if (day_num < END_DAY) {
		    wv_day.setCurrentItem(day_num - 1);
		} else {
		    wv_day.setCurrentItem(END_DAY - 1);
		}
	    } else {
		setDayAdapter(1, month_num, false);
		wv_day.setCurrentItem(wv_day.getCurrentItem() - 1);
		wv_day.setCurrentItem(wv_day.getCurrentItem() + 1);
		wv_day.setCyclic(true);
	    }

	    // int oldmonth_num =
	    // Utils.isInteger(monthAdapter.getItem(oldValue));
	    // int currentday =
	    // Utils.isInteger(dayAdapter.getItem(wv_day.getCurrentItem()));
	    // // 是否在限制年中
	    // if(year_num == START_YEAR || year_num == END_YEAR){
	    // //是否在限制月中
	    // if(month_num == START_MONTH || month_num == END_MONTH){
	    // //进入限制月，那么就要限制日，从其他月份进来，
	    // setDayAdapter(START_DAY, month_num , true);
	    // if(currentday >= START_DAY && currentday <= END_DAY)
	    // wv_day.setCurrentItem(currentday - START_DAY);
	    // else if(currentday > END_DAY)
	    // wv_day.setCurrentItem(END_DAY - START_DAY);
	    // else{
	    // wv_day.setCurrentItem(0);
	    // }
	    // wv_day.setCyclic(false);
	    //
	    // }else if(oldmonth_num == START_MONTH){
	    // //以前是限制月中的
	    // setDayAdapter(1, month_num, false);
	    // wv_day.setCurrentItem(currentday-1);
	    // wv_day.setCyclic(true);
	    // }else{
	    // //出限制月
	    // setDayAdapter(1, month_num , false);
	    // wv_day.setCurrentItem(currentday-1);
	    // wv_day.setCyclic(true);
	    // }
	    // }else{
	    // setDayAdapter(1, month_num , false);
	    // wv_day.setCurrentItem(wv_day.getCurrentItem());
	    // wv_day.setCyclic(true);
	    // }
	    //
	}
    };
    /**
     * 添加"日"监听 来设置小时
     */
    OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
	    int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		    .getCurrentItem()));
	    int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		    .getCurrentItem()));
	    int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		    .getCurrentItem()));
	    int hour_num = Utils.isInteger(hourAdapter.getItem(wv_hour
		    .getCurrentItem()));
	    // 在限制年中
	    if (year_num == START_YEAR && year_num == END_YEAR
		    && month_num == START_MONTH && month_num == END_MONTH
		    && day_num == START_DAY && day_num == END_DAY) {
		hourAdapter = new NumericWheelAdapter(START_HOUR, END_HOUR);
		wv_hour.setAdapter(hourAdapter);
		wv_hour.setCyclic(false);
		if (hour_num < START_HOUR) {
		    wv_hour.setCurrentItem(0);
		} else if (START_HOUR <= hour_num && hour_num == END_HOUR) {
		    wv_hour.setCurrentItem(hour_num - START_HOUR);
		} else {
		    wv_hour.setCurrentItem(END_HOUR - START_HOUR);
		}
	    } else if (year_num == START_YEAR && month_num == START_MONTH
		    && day_num == START_DAY) {
		hourAdapter = new NumericWheelAdapter(START_HOUR, 23);
		wv_hour.setAdapter(hourAdapter);
		wv_hour.setCyclic(false);
		if (hour_num < START_HOUR) {
		    wv_hour.setCurrentItem(0);
		} else {
		    wv_hour.setCurrentItem(hour_num - START_HOUR);
		}
	    } else if (year_num == END_YEAR && month_num == END_MONTH
		    && day_num == END_DAY) {
		hourAdapter = new NumericWheelAdapter(0, END_HOUR);
		wv_hour.setAdapter(hourAdapter);
		wv_hour.setCyclic(false);
		if (hour_num < END_HOUR) {
		    wv_hour.setCurrentItem(hour_num);
		} else {
		    wv_hour.setCurrentItem(END_HOUR);
		}
	    } else {
		hourAdapter = new NumericWheelAdapter(0, 23);
		wv_hour.setAdapter(hourAdapter);
		wv_hour.setCyclic(true);
		wv_hour.setCurrentItem(hour_num);
	    }
	    // 是否在限制月中
	    // if(month_num == START_MONTH || month_num == END_MONTH){}
	    // //进入限制月，那么就要限制日，从其他月份进来，
	    // setDayAdapter(START_DAY, month_num , true);
	    // if(currentday >= START_DAY && currentday <= END_DAY)
	    // wv_day.setCurrentItem(currentday - START_DAY);
	    // else if(currentday > END_DAY)
	    // wv_day.setCurrentItem(END_DAY - START_DAY);
	    // else{
	    // wv_day.setCurrentItem(0);
	    // }
	    // wv_day.setCyclic(false);

	    // }else if(oldmonth_num == START_MONTH){
	    // //以前是限制月中的
	    // setDayAdapter(1, month_num, false);
	    // wv_day.setCurrentItem(currentday-1);
	    // wv_day.setCyclic(true);
	    // }else{
	    // //出限制月
	    // setDayAdapter(1, month_num , false);
	    // wv_day.setCurrentItem(currentday-1);
	    // wv_day.setCyclic(true);
	    // }
	    // }else{
	    // setDayAdapter(1, month_num , false);
	    // wv_day.setCurrentItem(wv_day.getCurrentItem());
	    // wv_day.setCyclic(true);
	    // }
	}
    };
    /**
     * 添加"小时"监听 来设置小时
     */
    OnWheelChangedListener wheelListener_hour = new OnWheelChangedListener() {

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
	    int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		    .getCurrentItem()));
	    int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		    .getCurrentItem()));
	    int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		    .getCurrentItem()));
	    int hour_num = Utils.isInteger(hourAdapter.getItem(wv_hour
		    .getCurrentItem()));
	    int minute_num = Utils.isInteger(minuteAdapter.getItem(wv_minute
		    .getCurrentItem()));
	    // 在限制年中
	    if (year_num == START_YEAR && year_num == END_YEAR
		    && month_num == START_MONTH && month_num == END_MONTH
		    && day_num == START_DAY && day_num == END_DAY
		    && hour_num == START_HOUR && hour_num == END_HOUR) {
		minuteAdapter = new NumericWheelAdapter(START_MINUTE,
			END_MINUTE);
		wv_minute.setAdapter(minuteAdapter);
		wv_minute.setCyclic(false);
		if (minute_num < START_MINUTE) {
		    wv_minute.setCurrentItem(0);
		} else if (START_MINUTE <= minute_num
			&& minute_num == END_MINUTE) {
		    wv_minute.setCurrentItem(minute_num - START_MINUTE);
		} else {
		    wv_minute.setCurrentItem(END_MINUTE - START_MINUTE);
		}
	    } else if (year_num == START_YEAR && month_num == START_MONTH
		    && day_num == START_DAY && hour_num == START_HOUR) {
		minuteAdapter = new NumericWheelAdapter(START_MINUTE, 59);
		wv_minute.setAdapter(minuteAdapter);
		wv_minute.setCyclic(false);
		if (minute_num < START_MINUTE) {
		    wv_minute.setCurrentItem(0);
		} else {
		    wv_minute.setCurrentItem(minute_num - START_MINUTE);
		}
	    } else if (year_num == END_YEAR && month_num == END_MONTH
		    && day_num == END_DAY && hour_num == END_HOUR) {
		minuteAdapter = new NumericWheelAdapter(0, END_MINUTE);
		wv_minute.setAdapter(minuteAdapter);
		wv_minute.setCyclic(false);
		if (minute_num < END_MINUTE) {
		    wv_minute.setCurrentItem(minute_num);
		} else {
		    wv_minute.setCurrentItem(END_MINUTE);
		}
	    } else {
		minuteAdapter = new NumericWheelAdapter(0, 59);
		wv_minute.setAdapter(minuteAdapter);
		wv_minute.setCyclic(true);
		wv_minute.setCurrentItem(minute_num);
	    }
	}
    };

    /**
     * 设置日期adapter
     * 
     * @param _startday
     *            开始日期
     * @param _month
     *            所在月份
     * @param flag
     *            是否限定结束天
     */
    private void setDayAdapter(int _startday, int _month, boolean flag) {
	dayAdapter = new NumericWheelAdapter(_startday, getEndDays(_month));
	if (flag) {
	    dayAdapter = new NumericWheelAdapter(_startday, END_DAY);
	    wv_day.setAdapter(dayAdapter);
	} else {
	    dayAdapter = new NumericWheelAdapter(_startday, getEndDays(_month));
	    wv_day.setAdapter(dayAdapter);
	}
    }

    /**
     * 根据月份判断天数
     * 
     * @param month
     *            月份
     * @return days 有多少天
     */
    private int getEndDays(int month) {

	if (list_big.contains(String.valueOf(month))) {

	    return 31;

	} else if (list_little.contains(String.valueOf(month))) {

	    return 30;

	} else {
	    // 判断是不是闰年
	    if (isLeapYear(wv_year.getCurrentItem() + START_YEAR)) {
		return 29;
	    } else {
		return 28;
	    }
	}
    }

    /**
     * 判断是不是闰年
     * 
     * @param year
     * @return
     */
    private boolean isLeapYear(int year) {

	return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);

    }

    /**
     * 设置字体大小
     */
    public void setTextSize() {
	if (Utils.getScreenHeight(view.getContext()) < 800) {
	    textsize = (int) view.getResources().getDimension(
		    R.dimen.text_size_18);
	} else {
	    textsize = (int) view.getResources().getDimension(
		    R.dimen.text_size_20);
	}
	// 根据dimen 来获得大小
	wv_day.TEXT_SIZE = textsize;
	wv_month.TEXT_SIZE = textsize;
	wv_year.TEXT_SIZE = textsize;
	wv_hour.TEXT_SIZE = textsize;
	wv_minute.TEXT_SIZE = textsize;
    }

    /**
     * YYYY-MM-DD HH-mm-ss
     * 
     * @return
     */
    public String getTime() {

	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		.getCurrentItem()));
	int hour_num = Utils.isInteger(hourAdapter.getItem(wv_hour
		.getCurrentItem()));
	int minute_num = Utils.isInteger(minuteAdapter.getItem(wv_minute
		.getCurrentItem()));
	StringBuffer sb = new StringBuffer();
	if (timetype.equals(TYPE_YYMMDDHHmmSS))
	    sb.append(year_num).append("-").append(AddZero(month_num))
		    .append("-").append(AddZero(day_num)).append(" ")
		    .append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num)).append(":").append("00");
	else if (timetype.equals(TYPE_YYMMDDHHmm))
	    sb.append(year_num).append("-").append(AddZero(month_num))
		    .append("-").append(AddZero(day_num)).append(" ")
		    .append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num));
	else if (timetype.equals(TYPE_YYMMDD)) {
	    sb.append(year_num).append("-").append(AddZero(month_num))
		    .append("-").append(AddZero(day_num));
	} else if (timetype.equals(TYPE_MMDDHHmmSS)) {
	    sb.append(AddZero(month_num)).append("-").append(AddZero(day_num))
		    .append(" ").append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num)).append(":").append("00");
	} else if (timetype.equals(TYPE_MMDDHHmm)) {
	    sb.append(AddZero(month_num)).append("-").append(AddZero(day_num))
		    .append(" ").append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num));
	} else if (timetype.equals(TYPE_HHmm)) {
	    sb.append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num));
	}

	return sb.toString();
    }

    /**
     * 得到 yyyy-MM-dd HH-mm
     * 
     * @return
     */
    public String getTimeWithoutss() {
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		.getCurrentItem()));
	int hour_num = Utils.isInteger(hourAdapter.getItem(wv_hour
		.getCurrentItem()));
	int minute_num = Utils.isInteger(minuteAdapter.getItem(wv_minute
		.getCurrentItem()));
	StringBuffer sb = new StringBuffer();
	sb.append(year_num).append("-").append(AddZero(month_num)).append("-")
		.append(AddZero(day_num)).append(" ").append(AddZero(hour_num))
		.append(":").append(AddZero(minute_num));

	return sb.toString();
    }

    /**
     * 根据类型得到时间
     * 
     * @return
     */
    public String getTime(String timetype) {
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		.getCurrentItem()));
	int hour_num = Utils.isInteger(hourAdapter.getItem(wv_hour
		.getCurrentItem()));
	int minute_num = Utils.isInteger(minuteAdapter.getItem(wv_minute
		.getCurrentItem()));
	StringBuffer sb = new StringBuffer();
	if (timetype.equals(TYPE_YYMMDDHHmmSS))
	    sb.append(year_num).append("-").append(AddZero(month_num))
		    .append("-").append(AddZero(day_num)).append(" ")
		    .append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num)).append(":").append("00");
	else if (timetype.equals(TYPE_YYMMDDHHmm))
	    sb.append(year_num).append("-").append(AddZero(month_num))
		    .append("-").append(AddZero(day_num)).append(" ")
		    .append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num));
	else if (timetype.equals(TYPE_YYMMDD)) {
	    sb.append(year_num).append("-").append(AddZero(month_num))
		    .append("-").append(AddZero(day_num));
	} else if (timetype.equals(TYPE_MMDDHHmmSS)) {
	    sb.append(AddZero(month_num)).append("-").append(AddZero(day_num))
		    .append(" ").append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num)).append(":").append("00");
	} else if (timetype.equals(TYPE_MMDDHHmm)) {
	    sb.append(AddZero(month_num)).append("-").append(AddZero(day_num))
		    .append(" ").append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num));
	} else if (timetype.equals(TYPE_HHmm)) {
	    sb.append(AddZero(hour_num)).append(":")
		    .append(AddZero(minute_num));
	}

	return sb.toString();
    }

    /**
     * 
     * @return yyyy-MM-dd
     */
    public String getShortTime() {
	int year_num = Utils.isInteger(yearAdapter.getItem(wv_year
		.getCurrentItem()));
	int month_num = Utils.isInteger(monthAdapter.getItem(wv_month
		.getCurrentItem()));
	int day_num = Utils.isInteger(dayAdapter.getItem(wv_day
		.getCurrentItem()));
	StringBuffer sb = new StringBuffer();
	sb.append(year_num).append("-").append(AddZero(month_num)).append("-")
		.append(AddZero(day_num));

	return sb.toString();
    }

    /**
     * 判断是否要补0 例如 9 = 09
     * 
     * @param number
     * @return
     */
    private String AddZero(int number) {
	if (number < 10) {
	    return "0" + number;
	}
	return "" + number;
    }

    /****************************** get set 方法 ******************************/
    public View getView() {
	return view;
    }

    public void setView(View view) {
	this.view = view;
    }

    public void setSTART_YEAR(int sTART_YEAR) {
	START_YEAR = sTART_YEAR;
	this.yearAdapter = new NumericWheelAdapter(START_YEAR, END_YEAR);
    }

    public void setEND_YEAR(int eND_YEAR) {
	END_YEAR = eND_YEAR;
	this.yearAdapter = new NumericWheelAdapter(START_YEAR, END_YEAR);
    }

    public void setSTART_MONTH(int sTART_MONTH) {
	START_MONTH = sTART_MONTH;
    }

    public void setEND_MONTH(int eND_MONTH) {
	END_MONTH = eND_MONTH;
    }

    public void setSTART_DAY(int sTART_DAY) {
	START_DAY = sTART_DAY;
    }

    public void setEND_DAY(int eND_DAY) {
	END_DAY = eND_DAY;
    }

    public void setEND_HOUR(int eND_HOUR) {
	END_HOUR = eND_HOUR;
    }

    public void setSTART_HOUR(int sTART_HOUR) {
	START_HOUR = sTART_HOUR;
    }

    public void setSTART_MIN(int sTART_MINUTE) {
	START_MINUTE = sTART_MINUTE;
    }

    public void setEND_MIN(int eND_MINUTE) {
	END_MINUTE = eND_MINUTE;
    }

    public int getTextsize() {
	return textsize;
    }

    public void setTextsize(int textsize) {
	this.textsize = textsize;
    }
}
