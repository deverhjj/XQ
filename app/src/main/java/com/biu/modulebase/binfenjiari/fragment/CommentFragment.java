package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.adapter.CommQADetailAdapter;
import com.biu.modulebase.binfenjiari.model.CardDetailChildItem;
import com.biu.modulebase.binfenjiari.model.CardDetailParentItem;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class CommentFragment extends BaseFragment {
    private static final String TAG = "CommentFragment";
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
        CommQADetailAdapter adapter=new CommQADetailAdapter(getActivity(),getTestData());

        mRecyclerView.setAdapter(adapter);
        //        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    private List<CardDetailParentItem> getTestData() {
        List<CardDetailParentItem> parentItems = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            CardDetailParentItem cardDetailParentItem = new CardDetailParentItem();
            parentItems.add(cardDetailParentItem);
            if (i % 2 == 0) {
                List<CardDetailChildItem> cardDetailChildItems = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    CardDetailChildItem childItem = new CardDetailChildItem();
                    cardDetailChildItems.add(childItem);
                }
                cardDetailParentItem.setHasChildComment(true);
                //cardDetailParentItem.setCardDetailChildItems(cardDetailChildItems);
            }
        }
        return parentItems;
    }

    @Override
    public void loadData() {

    }
}
