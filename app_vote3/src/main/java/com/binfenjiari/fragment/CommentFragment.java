package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.binfenjiari.adapter.CommentAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.utils.Views;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableRecyclerView;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class CommentFragment extends AppFragment {
    private ExpandableRecyclerView mCommentList;
    private CommentAdapter mAdapter;

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.expandable_list_view, container, false);
    }

    @Override
    public void onInitView(View root) {
        mCommentList = Views.find(root, R.id.eRv);
        mAdapter = new CommentAdapter(getContext());
        mCommentList.setAdapter(mAdapter);
        mCommentList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}
