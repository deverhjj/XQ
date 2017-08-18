package com.github.huajianjiang.baserecyclerview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.util.Packager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/24
 * <br>Email: developer.huajianjiang@gmail.com
 */
class InnerHeaderAdapter extends RecyclerView.Adapter {
    private RecyclerView.Adapter mWrappedAdapter;

    private List<BaseRecyclerView.FixedHeaderInfo> mHeaders;
    private List<BaseRecyclerView.FixedFooterInfo> mFooters;

    public InnerHeaderAdapter(RecyclerView.Adapter wrappedAdapter) {
        mWrappedAdapter = wrappedAdapter;
        mHeaders = new ArrayList<>();
        mFooters = new ArrayList<>();
    }

    void invalidate(List<BaseRecyclerView.FixedHeaderInfo> headers,
            List<BaseRecyclerView.FixedFooterInfo> footers, boolean autoNotify)
    {
        mHeaders.addAll(headers);
        mFooters.addAll(footers);
        if (autoNotify) notifyDataSetChanged();
    }

    void addHeader(BaseRecyclerView.FixedHeaderInfo header, boolean autoNotify) {
        mHeaders.add(header);
        if (autoNotify) notifyItemInserted(mHeaders.size() - 1);
    }

    void addFooter(BaseRecyclerView.FixedFooterInfo header, boolean autoNotify) {
        mFooters.add(header);
        if (autoNotify) notifyItemInserted(mFooters.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int localType = Packager.getLocalViewType(viewType);
        final int clientType = Packager.getClientViewType(viewType);
        if (localType == Packager.ITEM_VIEW_TYPE_HEADER )
        {
            return new RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(clientType, parent, false))
            {

            };
        } else {
            return mWrappedAdapter.onCreateViewHolder(parent, clientType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 客户端自己判断 type
        mWrappedAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mWrappedAdapter != null) {
            mWrappedAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (mWrappedAdapter != null) {
            mWrappedAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    int getHeaderCount() {
        return mHeaders.size();
    }

    int getWrappedItemCount() {
        return mWrappedAdapter != null ? mWrappedAdapter.getItemCount() : 0;
    }

    int getFooterCount() {
        return mFooters.size();
    }

    boolean isHeaderView(int position) {
        return getHeaderCount() > 0 && position < getHeaderCount();
    }

    boolean isItemView(int position) {
        return position >= getHeaderCount() && position < getItemCount() - getFooterCount();
    }

    boolean isFooterView(int position) {
        return getFooterCount() > 0 && position >= getHeaderCount() + getWrappedItemCount() &&
               position < getItemCount();
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getWrappedItemCount() + getFooterCount();
    }

    /**
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
//        int clientType;
//        int localType;
//        if (isHeaderView(position)) {
//            // header
//            localType = Packager.ITEM_VIEW_TYPE_HEADER;
//            clientType = mHeaders.get(position).layoutResId;
//        } else if (isFooterView(position)) {
//
//        } else {
//            final int adjPos = position - getHeaderCount();
//            localType = Packager.ITEM_VIEW_TYPE_CLIENT;
//            clientType = mWrappedAdapter.getItemViewType(adjPos);
//        }
        return 0;
    }

}
