package com.binfenjiari.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.github.huajianjiang.baserecyclerview.util.Packager;
import com.github.huajianjiang.expandablerecyclerview.widget.BaseViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class CommentAdapter extends BaseExpandableAdapter {

    public CommentAdapter(Context ctxt) {
        super(ctxt);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        return new ParentViewHolder(getLayoutInflater().inflate(parentType, parent, false));
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        return null;
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int parentPosition) {

    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition,
            int childPosition)
    {

    }

    //for test
    @Override
    public int getItemViewType(int position) {
        return R.layout.item_general_comment_parent;
    }

    @Override
    public int getParentType(int parentPosition) {
        return R.layout.item_general_comment_parent;
    }

    //for test
    @Override
    public int getItemCount() {
        return 10;
    }

    //for test
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    //for test
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {

    }

}
