package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.SearchResultAudioActivity;
import com.biu.modulebase.binfenjiari.activity.SearchResultCircleActivity;
import com.biu.modulebase.binfenjiari.activity.SearchResultEventActivity;
import com.biu.modulebase.binfenjiari.activity.SearchResultJidiActivity;
import com.biu.modulebase.binfenjiari.activity.SearchResultPostActivity;
import com.biu.modulebase.binfenjiari.adapter.CommonAdapter;
import com.biu.modulebase.binfenjiari.adapter.ViewHolder;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.SearchVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.SearchEditText;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/26
 */
public class SearchFragment extends BaseFragment {

    /** 最大记录数 **/
    private final int MAX_RECODER = 10;

	private SearchEditText searchEditText;
    private ListView listView;
    private CommonAdapter<SearchVO> adapter;
    private ArrayList<SearchVO> searchRecodes = new ArrayList<>();

    private String param = "";
    private View footer;
    private String searchTag =null;

    private String circleId ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_search, container, false);
        return layout;
    }

    @Override
    protected void getIntentData() {
        searchTag =getActivity().getIntent().getStringExtra(Constant.SEARCH_TAG);
        circleId =getActivity().getIntent().getStringExtra(CircleFragment.EXTRA_CIRCLE_ID);
    }

    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
        searchEditText = (SearchEditText) getBaseActivity().setToolBarCustomView(R.layout.search_editview);
		searchEditText.requestFocus();
        if(searchTag.equals(Constant.SEARCH_EVENT)){
            searchEditText.setHint("搜索想要的活动...");
        }else if (searchTag.equals(Constant.SEARCH_JIDI)){
            searchEditText.setHint("搜索想要的基地...");
        }else if (searchTag.equals(Constant.SEARCH_AUDIO)){
            searchEditText.setHint("搜索想要的视听...");
        }else if(searchTag.equals(Constant.SEARCH_CIRCLE_ALL)){
            searchEditText.setHint("搜索想要的圈子...");
        }else if(searchTag.equals(Constant.SEARCH_CIRCLE_MY)){
            searchEditText.setHint("搜索我的圈子...");
        }else if(searchTag.equals(Constant.SEARCH_POST)){
            searchEditText.setHint("搜索想要的帖子...");
        }
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setFooterDividersEnabled(false);
        adapter = new CommonAdapter<SearchVO>(getActivity(), searchRecodes, R.layout.list_item_search_result) {

            @Override
            public void convert(ViewHolder helper, final SearchVO item) {
                helper.setText(R.id.shop_name, item.getName());

                helper.getView(R.id.img_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        adapter.removeData(Collections.singletonList(item));
//                        adapter.removeDataOne(item);
                        searchRecodes.remove(item);
                        refreshListView();
//                        saveSeachRecode();
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                param =searchRecodes.get(position).getName();
                startSearchResult(searchTag);


            }
        });
		searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					/* 隐藏软键盘 */
					showSoftKeyboard();
					search();
					return true;
				}
				return false;
			}
		});

        }

    @Override
    public void loadData() {
        getSeachRecode();
    }

    private void startSearchResult(String tag){
        Intent intent =null;
        if(tag.equals(Constant.SEARCH_EVENT)){
            intent = new Intent(getActivity(), SearchResultEventActivity.class);
        }else if(tag.equals(Constant.SEARCH_JIDI)){
            intent = new Intent(getActivity(), SearchResultJidiActivity.class);
        }else if(tag.equals(Constant.SEARCH_AUDIO)){
            intent = new Intent(getActivity(), SearchResultAudioActivity.class);
        }else if(tag.equals(Constant.SEARCH_CIRCLE_ALL)){
            intent = new Intent(getActivity(), SearchResultCircleActivity.class);
            intent.putExtra(Constant.SEARCH_TAG,Constant.SEARCH_CIRCLE_ALL);
        }else if(tag.equals(Constant.SEARCH_CIRCLE_MY)){
            intent = new Intent(getActivity(), SearchResultCircleActivity.class);
            intent.putExtra(Constant.SEARCH_TAG,Constant.SEARCH_CIRCLE_MY);
        }else if(tag.equals(Constant.SEARCH_POST)){
            intent = new Intent(getActivity(), SearchResultPostActivity.class);
            intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID,circleId);
        }

		intent.putExtra("title", param);
		startActivity(intent);
//        getActivity().finish();
        refreshListView();
    }



    private void search() {
		param = searchEditText.getText().toString();
        if (Utils.isEmpty(param)) {
			getBaseActivity().showTost("请输入关键词",1);
        } else {
            saveSeachRecode();
            startSearchResult(searchTag);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(searchRecodes.size()==0){
            PreferencesUtils.putString(getActivity().getApplicationContext(), "search_recode", "");
        }else {
            String recodeStr = JSONUtil.toJson(searchRecodes);
            PreferencesUtils.putString(getActivity().getApplicationContext(), "search_recode", recodeStr);
        }
    }

    /**
     * 保持搜索记录
     */
    private void saveSeachRecode() {
        int size = searchRecodes.size();

        for (int i = 0; i < size; i++) { // 防止重复数据
            SearchVO recode = searchRecodes.get(i);
            if (recode.getName().equals(param)) {
                recode.setTime(new Date().getTime() + "");
                searchRecodes.set(i, recode);
                return;
            }
        }

//        if(searchRecodes.size()==0){
//            PreferencesUtils.putString(getActivity().getApplicationContext(), "search_recode", "");
//            return;
//        }

        if (size == MAX_RECODER)// 限制历史最大长度
            searchRecodes.remove(size - 1);

        SearchVO recode = new SearchVO();
        recode.setName(param);
        recode.setTime(new Date().getTime() + "");
        searchRecodes.add(recode);
        String recodeStr = JSONUtil.toJson(searchRecodes);
        PreferencesUtils.putString(getActivity().getApplicationContext(), "search_recode", recodeStr);
    }

    /**
     * 获取搜索记录
     */
    private void getSeachRecode() {
        String recodeStr = PreferencesUtils.getString(getActivity().getApplicationContext(), "search_recode");
        if (!Utils.isEmpty(recodeStr)) {
            searchRecodes = JSONUtil.fromJson(recodeStr, new TypeToken<List<SearchVO>>() {
            }.getType());
            refreshListView();
        }

    }

    /**
     * 清除记录
     */
    private void clearRecode() {
        PreferencesUtils.putString(getActivity().getApplicationContext(), "search_recode", "");
        searchRecodes.clear();
        refreshListView();
    }

    /**
     * 刷新listView
     */
    private void refreshListView() {
        sortRecoder();
        adapter.onDateChange(searchRecodes);
        if (searchRecodes.size() > 0) {
            addFooter();
        } else {
            if (footer != null) {
                listView.removeFooterView(footer);
                footer = null;
            }

        }

    }

    private void addFooter() {
        if (footer == null) {
            footer = getActivity().getLayoutInflater().inflate(R.layout.list_footer_search, null);
            footer.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clearRecode();
                }
            });
            listView.addFooterView(footer);
        }

    }

    /**
     * 降序排序
     */
    protected void sortRecoder() {
        Collections.sort(searchRecodes, new Comparator<SearchVO>() {
            public int compare(SearchVO arg0, SearchVO arg1) {
                return -(arg0.getTime().compareTo(arg1.getTime()));
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_text, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//
//        searchView.setIconifiedByDefault(false);
//
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
//                Context.SEARCH_SERVICE);
//
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getActivity().getComponentName()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            search();

        }

        return super.onOptionsItemSelected(item);
    }
}
