package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AddFriendActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.common.base.BaseFragment;

import java.util.ArrayList;

/**
 * @author Lee
 * @Title: {新的好友}
 * @Description:{描述}
 * @date 2016/5/4
 */
public class NewFriendFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
        recyclerView =(RecyclerView)rootView.findViewById(R.id.recyclerView);

        ArrayList<String> datas = new ArrayList<>();
        datas.add(""); datas.add(""); datas.add("");
        BaseAdapter adapter = new BaseAdapter(getActivity(),datas) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_add_friend, parent, false), new BaseViewHolder.Callbacks() {
                    @Override
                    public void bind(BaseViewHolder holder, Object data) {

                    }

                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }

        };
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,1);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(search);
        mSearchView.setQueryHint(Html.fromHtml( "手机号/QQ号/微信/微博" ));
        mSearchView.onActionViewExpanded();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_add) {
            startActivity(new Intent(getActivity(), AddFriendActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

}
