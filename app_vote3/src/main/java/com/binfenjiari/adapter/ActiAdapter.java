package com.binfenjiari.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.binfenjiari.R;
import com.github.huajianjiang.baserecyclerview.widget.ArrayAdapter;
import com.github.huajianjiang.baserecyclerview.widget.BaseViewHolder;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/16
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ActiAdapter extends ArrayAdapter {

    public ActiAdapter(Context ctxt) {
        super(ctxt);
    }

    @Override
    public BaseViewHolder onGenerateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(getLayoutInflater().inflate(viewType, parent, false)) {

        };
    }

    @Override
    public void onPopulateViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_mine_collection_movement;
    }

    @Override
    public int getItemCount() {
        return 18;
    }
}
