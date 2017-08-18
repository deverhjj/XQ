package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.binfenjiari.R;
import com.binfenjiari.activity.MovementListActivity;
import com.binfenjiari.widget.calendar.CalendarView;
import com.binfenjiari.widget.calendar.OnDateChangedListener;
import com.binfenjiari.widget.calendar.OnMonthChangedListener;
import com.biu.modulebase.common.base.BaseFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class MovementCalendlerFragment extends BaseFragment {

    private CalendarView mCalendarView;

    private String nDate = "";

    public static MovementCalendlerFragment newInstance() {
        MovementCalendlerFragment fragment = new MovementCalendlerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_movement_calendler, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        mCalendarView = (CalendarView) rootView.findViewById(R.id.candler);
        TextView yearAdd = (TextView) rootView.findViewById(R.id.year_add);
        TextView yearSub = (TextView) rootView.findViewById(R.id.year_sub);
        final TextView year = (TextView) rootView.findViewById(R.id.year);
        TextView monthAdd = (TextView) rootView.findViewById(R.id.month_add);
        TextView monthSub = (TextView) rootView.findViewById(R.id.month_sub);
        final TextView month = (TextView) rootView.findViewById(R.id.month);
        yearSub.setText("<");
        yearAdd.setText(">");
        monthAdd.setText(">");
        monthSub.setText("<");
        yearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setCurrentItem(mCalendarView.getCurrentItem() + 12);
            }
        });
        yearSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setCurrentItem(mCalendarView.getCurrentItem() - 12);
            }
        });
        monthAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setCurrentItem(mCalendarView.getCurrentItem() + 1);
            }
        });
        monthSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setCurrentItem(mCalendarView.getCurrentItem() - 1);
            }
        });
        Date date = new Date();
        String y = new SimpleDateFormat("yyyy").format(date);
        year.setText(y);
        String m = new SimpleDateFormat("MM").format(date);
        month.setText(m);
//         calendarView.setOnDateChangedListener(new OnDateChangedListener() {
//             @Override
//             public void onDateChanged(Date date) {
//                 String d = new SimpleDateFormat("dd/MM/yyyy").format(date);
//                 time.setText(d);
//             }
//         });
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(Date date) {
                String y = new SimpleDateFormat("yyyy").format(date);
                String m = new SimpleDateFormat("MM").format(date);
                String d = new SimpleDateFormat("yyyy-MM-dd").format(date);
                getNumber(d, mCalendarView);
                year.setText(y);
                month.setText(m);
            }
        });
        mCalendarView.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(Date date) {
                nDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                MovementListActivity.beginActivity(getContext());
//                getCalenderData(Constant.LIST_REFRESH);
            }
        });



    }

    private void getNumber(String date, CalendarView mCalendarView) {

        ArrayList<Integer> data = new ArrayList<>();
        data.add(2);data.add(0);data.add(0);data.add(4);data.add(0);data.add(0);data.add(8);
        for(int i=0;i<30;i++){
            data.add(0);
        }
        mCalendarView.setNumbers(data);
    }

    @Override
    public void loadData() {


    }


}
