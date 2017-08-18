package com.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.CommonSearchActivity;
import com.binfenjiari.adapter.ActiAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.base.BaseActivity;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Views;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.github.huajianjiang.alphaslidebar.SimpleSearchView;
import com.github.huajianjiang.alphaslidebar.SoftKeyboardHelper;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/16
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ActiSearchResultFragment extends AppFragment
        implements SoftKeyboardHelper.OnSoftKeyboardStateChangeListener
{
    private static final String TAG = ActiSearchResultFragment.class.getSimpleName();
    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mResultList;
    private ActiAdapter mAdapter;

    private SoftKeyboardHelper mKeyboardHelper;
    private SimpleSearchView mSearchView;

    private Bundle mArgs;
    private String mKeyword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
        mKeyword = mArgs != null ? mArgs.getString(Constants.KEY_SEARCH_KEY_WORDS) : "";
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container, false);
    }

    @Override
    public void onInitView(View root) {
        mRefreshLayout = Views.find(root, R.id.swipe);
        mResultList = Views.find(root, R.id.recyclerView);
        mResultList.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new ActiAdapter(getContext());
        mResultList.setAdapter(mAdapter);
        mResultList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mKeyboardHelper = new SoftKeyboardHelper(root);
        mKeyboardHelper.setListener(this);

        mSearchView = ((BaseActivity) getActivity()).getCustomViewForToolbar();
        mSearchView.setText(mKeyword);
    }

    @Override
    public void onKeyboardShow() {
        Logger.e(TAG, "^^^^^^^^^^^^^^^^^^^onKeyboardShow^^^^^^^^^^^^^^^^");
        Intent search = new Intent(getContext(), CommonSearchActivity.class);
        search.putExtra(Constants.KEY_SEARCH_KEY_WORDS, mKeyword);
        startActivity(search);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onKeyboardHide() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mKeyboardHelper.detach();
    }

    @Override
    protected void onFirstShow() {
        showLoadingView();
        mResultList.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingView();
            }
        }, 1500);
    }
}
