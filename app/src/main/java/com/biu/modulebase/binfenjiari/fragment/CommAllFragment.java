package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommAllFragment extends BaseFragment {
    private static final String TAG = "CommAllFragment";

    private static final int TYPE_FRESH=0;

    private static final int TYPE_VOTE=1;

    private static final int TYPE_QA=2;

    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG,"onRefresh******************");
                //refreshData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG,"onLoadMore******************");
                //loadCards();
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(viewType==TYPE_FRESH?R.layout
                        .item_fresh_things:viewType==TYPE_VOTE?R.layout.item_vote:R.layout.item_qa,
                        parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {

                            }
                        });
                return holder;
            }

            @Override
            public int getItemViewType(int position) {
                return position==0?TYPE_FRESH:position==1?TYPE_VOTE:TYPE_QA;
            }
        };

        adapter.testLayoutData(3);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void loadData() {

    }
}
