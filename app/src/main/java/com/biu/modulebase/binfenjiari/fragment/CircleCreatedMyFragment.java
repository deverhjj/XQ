package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.CircleTypeVO;
import com.biu.modulebase.binfenjiari.util.FragmentSwitcher;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.TestCenter;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * @Title: {我的圈子}
 * @Description:{}
 * @date 2016/4/13
 */
public class CircleCreatedMyFragment extends BaseFragment implements
        CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "CircleCreatedMyFragment";

    private RadioGroup mTabGroup;

    private CircleFragmentSwitcher mCircleFragmentSwitcher;

    private List<CircleTypeVO> types;

    private String[] mTypeNames= TestCenter.tempTabs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class CircleFragmentSwitcher extends FragmentSwitcher {

        public CircleFragmentSwitcher(FragmentManager fm, int containerId) {
            super(fm, containerId);
        }

        @Override
        public Fragment getItem(int position) {
            String typeId = "-1";
            if (types != null) {
                CircleTypeVO item = types.get(position);
                typeId = item.getId();
            }
            return CircleItemFragment.newInstance(CircleItemFragment.TYPE_MY,typeId,-1);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int selectedPos = mTabGroup.indexOfChild(buttonView);
        if (isChecked) {
            mCircleFragmentSwitcher.toggle(selectedPos);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_internal, container, false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }

    private void populateTab(String tabName) {
        if (getActivity()==null) {
            return;
        }
        RadioButton tab = (RadioButton) getActivity().getLayoutInflater().inflate(
                R.layout.item_tab_vertical, mTabGroup,false);
        tab.setText(tabName);
        tab.setOnCheckedChangeListener(this);
        mTabGroup.addView(tab);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        setHasOptionsMenu(true);
        mTabGroup = (RadioGroup) rootView.findViewById(R.id.group_circle_tab);

//        for (String string : mTypeNames) {
//            Log.e(TAG,string);
//            RadioButton tab = (RadioButton) getActivity().getLayoutInflater().inflate(
//                    R.layout.item_tab_vertical, mTabGroup,false);
//            tab.setText(string);
//            tab.setOnCheckedChangeListener(this);
//            mTabGroup.addView(tab);
//        }
//        ((RadioButton)mTabGroup.getChildAt(0)).toggle();
    }

    @Override
    public void loadData() {
        getMyCircleType();
    }

    private  void getMyCircleType(){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"model",Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params,"action",Constant.ACTION_MY_CIRCLE_FIRST_CLASS);
        jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogE(TAG, "response---->" + jsonString);
                //移除所有旧的 Tab
                mTabGroup.removeAllViews();
                types =JSONUtil.fromJson(jsonString,new TypeToken<List<CircleTypeVO>>(){}.getType());
                int tabCount= types.size();
                mTypeNames=new String[tabCount];
                //添加新的 Tab
                for (int i = 0; i < tabCount; i++) {
                    CircleTypeVO item= types.get(i);
                    String tabName=item.getName();
                    populateTab(tabName);
                    mTypeNames[i]=tabName;
                    LogUtil.LogE(TAG, "name====>" + tabName);
                }
                mCircleFragmentSwitcher = new CircleFragmentSwitcher(getActivity()
                        .getSupportFragmentManager(), R.id
                        .fragment_circle_container);
                //默认选中第一个 Tab
                RadioButton tab0=(RadioButton) mTabGroup.getChildAt(0);
                if (tab0!=null) {
                    tab0.toggle();
                }
                inVisibleLoading();
            }

            @Override
            public void onCodeError(int key, String message) {
                visibleNoData();
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Communications.cancelRequest(TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
//        final MenuItem item = menu.findItem(R.id.action_search);
//        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(item);
//        mSearchView.setQueryHint(Html.fromHtml(
//                "<font color = #999999 style=\"font-size: 10sp\">" + "搜索我的圈子..." + "</font>"));
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Intent intent =new Intent(getActivity(),SearchResultCircleActivity.class);
//                intent.putExtra("intent",query);
//                startActivity(intent);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            startSearchIntent(Constant.SEARCH_CIRCLE_MY);

        }
        return super.onOptionsItemSelected(item);
    }
}
