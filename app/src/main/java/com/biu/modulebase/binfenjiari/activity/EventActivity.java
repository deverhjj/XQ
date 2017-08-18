package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.EventFragment;
import com.biu.modulebase.binfenjiari.model.AreaVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.common.base.BaseActivity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/26.
 */
public class EventActivity extends BaseActivity {

    private static final String TAG = "EventActivity";

    private EventFragment fragment;

    private ViewGroup mFilterLayout;

    private TextView tv_region;
    private TextView tv_date;
    private TextView tv_cost;

    private List<AreaVO> araeList = new ArrayList<>();
    private String areaArray[];
    public String dateArray[] = {"综合排序", "时间从近到远", "时间从远到近"};
    public String costArray[] = {"综合排序", "从低到高", "从高到底"};
    /**
     * 1:降序  2：升序
     **/
    private String orderArray[] = {"2", "1"};
    public int areaCheckedPos = -1;
    public int dateCheckedPos = -1;
    public int costCheckedPos = -1;

    public String districtId;
    public String timeOrder;
    public String moneyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackNavigationIcon();

        AppBarLayout.LayoutParams lp2 = (AppBarLayout.LayoutParams) getToolbar().getLayoutParams();
        lp2.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.layout_app_bar);

        if (appBarLayout != null) {
            mFilterLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.part_filter, appBarLayout, false);
            AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

            mFilterLayout.post(new Runnable() {
                @Override
                public void run() {
                    mFilterLayout.setMinimumHeight(mFilterLayout.getMeasuredHeight());
                }
            });

            mFilterLayout.setVisibility(View.VISIBLE);
            appBarLayout.addView(mFilterLayout, layoutParams);
            tv_region = (TextView) mFilterLayout.findViewById(R.id.tv_region);
            tv_date = (TextView) mFilterLayout.findViewById(R.id.tv_date);
            tv_cost = (TextView) mFilterLayout.findViewById(R.id.tv_cost);
            tv_region.setOnClickListener(this);
            tv_date.setOnClickListener(this);
            tv_cost.setOnClickListener(this);

            mFilterLayout.findViewById(R.id.rl_region).setOnClickListener(this);
            mFilterLayout.findViewById(R.id.rl_date).setOnClickListener(this);

        }
    }

    @Override
    protected Fragment getFragment() {
        return new EventFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "活动";
    }


    private void getAreaList() {
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_COMMON);
            params.put("action", Constant.ACTION_GET_AREA_LIST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                araeList = JSONUtil.fromJson(jsonString, new TypeToken<List<AreaVO>>() {
                }.getType());
                int length = araeList.size();
                areaArray = new String[length + 1];
                areaArray[0] = "全部";
                for (int i = 0; i < length; i++) {
                    areaArray[i + 1] = araeList.get(i).getName();
                }
                showAreaFilter();

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message, 1);
            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (fragment == null)
            fragment = (EventFragment) getSupportFragmentManager().getFragments().get(0);
        int i = v.getId();
        if (i == R.id.tv_region || i == R.id.rl_region) {
            if (areaArray == null) {
                getAreaList();
            } else {
                showAreaFilter();
            }

        } else if (i == R.id.tv_date || i == R.id.rl_date) {
            showTimeFilter();

        } else if (i == R.id.tv_cost) {
            showCostFilter();

        } else {
        }
    }

    private void showAreaFilter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
                .item_menu2, R.id.tv, areaArray);
        OtherUtil.showPopupWin(this, mFilterLayout, adapter, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                areaCheckedPos, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv_region.setText(areaArray[position]);
                        areaCheckedPos = position;
                        if (position == 0) {
                            districtId = "";
                        } else {
                            districtId = araeList.get(position - 1).getId() + "";
                        }
                        fragment.visibleLoading();
                        fragment.getEventList(null, districtId, timeOrder, moneyOrder, Constant.LIST_REFRESH);
//                        if (!v.isSelected()) {
//                            v.setSelected(true);
//                        }
                    }
                });
    }

    private void showTimeFilter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
                .item_menu2, R.id.tv, dateArray);
        OtherUtil.showPopupWin(this, mFilterLayout, adapter, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                dateCheckedPos, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv_date.setText(dateArray[position]);
                        dateCheckedPos = position;
                        if (position == 0) {
                            timeOrder = "";
                        } else {
                            timeOrder = orderArray[position - 1];
                        }
                        fragment.visibleLoading();
                        fragment.getEventList(null, districtId, timeOrder, moneyOrder, Constant.LIST_REFRESH);
//                        if (!v.isSelected()) {
//                            v.setSelected(true);
//                        }
                    }
                });
    }

    private void showCostFilter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
                .item_menu2, R.id.tv, costArray);
        OtherUtil.showPopupWin(this, mFilterLayout, adapter, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                costCheckedPos, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv_cost.setText(costArray[position]);
                        costCheckedPos = position;
                        if (position == 0) {
                            moneyOrder = "";
                        } else {
                            moneyOrder = orderArray[position - 1];
                        }
                        fragment.visibleLoading();
                        fragment.getEventList(null, districtId, timeOrder, moneyOrder, Constant.LIST_REFRESH);
//                        if (!v.isSelected()) {
//                            v.setSelected(true);
//                        }
                    }
                });
    }

}
