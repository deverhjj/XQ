package com.github.huajianjiang.baserecyclerview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.util.Logger;
import com.github.huajianjiang.baserecyclerview.util.Predications;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/25
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class BaseAdapter<BVH extends BaseViewHolder> extends RecyclerView.Adapter<BVH> {
    private static final String TAG = "BaseAdapter";
    /**
     * 当前所有监听适配器的 RecyclerView 集合
     */
    private List<RecyclerView> mAttachedRecyclerViews = new ArrayList<>(2);

    /**
     * 内部 itemView 或者 itemView 的子 view 交互事件监听器
     */
    private ViewEventWatcher mViewEventWatcher;
    private OnItemClickListener mClickListener;
    private Integer[] mClickTargets;
    private OnItemLongClickListener mLongClickListener;
    private Integer[] mLongClickTargets;

    private Context mCtxt;
    private LayoutInflater mInflater;

    public BaseAdapter(Context ctxt) {
        mCtxt = ctxt;
        mInflater = LayoutInflater.from(ctxt);
    }

    public Context getContext() {
        return mCtxt;
    }

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    public abstract BVH onGenerateViewHolder(ViewGroup parent, int viewType);

    public abstract void onPopulateViewHolder(BVH holder, int position);

    @Override
    public BVH onCreateViewHolder(ViewGroup parent, int viewType) {
        BVH bvh = onGenerateViewHolder(parent, viewType);
        if (!Predications.isNullOrEmpty(mClickTargets)) {
            for (Integer id : mClickTargets) {
                View view = bvh.getView(id);
                if (view != null) {
                    view.setOnClickListener(getViewEventWatcher());
                }
            }
        }

        if (!Predications.isNullOrEmpty(mLongClickTargets)) {
            for (Integer id : mLongClickTargets) {
                View view = bvh.getView(id);
                if (view != null) {
                    view.setOnLongClickListener(getViewEventWatcher());
                }
            }
        }

        return bvh;
    }

    @Override
    public void onBindViewHolder(BVH holder, int position) {
        onPopulateViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerViews.add(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclerViews.remove(recyclerView);
    }

    public RecyclerView[] getAttachRecyclerViews() {
        return mAttachedRecyclerViews.toArray(new RecyclerView[mAttachedRecyclerViews.size()]);
    }

    private ViewEventWatcher getViewEventWatcher() {
        if (mViewEventWatcher == null) {
            mViewEventWatcher = new ViewEventWatcher();
        }
        return mViewEventWatcher;
    }

    private class ViewEventWatcher implements View.OnClickListener, View.OnLongClickListener {
        @Override
        public void onClick(View v) {
            for (RecyclerView parent : mAttachedRecyclerViews) {
                View itemView = parent.findContainingItemView(v);
                if (itemView != null && mClickListener != null) {
                    mClickListener.onItemClick(parent, v);
                    break;
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            for (RecyclerView parent : mAttachedRecyclerViews) {
                View itemView = parent.findContainingItemView(v);
                if (itemView != null && mLongClickListener != null) {
                    return mLongClickListener.onItemLongClick(parent, v);
                }
            }
            return false;
        }
    }

    public BaseAdapter clickTargets(Integer... viewIds) {
        mClickTargets = viewIds;
        return this;
    }

    public BaseAdapter longClickTargets(Integer... viewIds) {
        mLongClickTargets = viewIds;
        return this;
    }

    public BaseAdapter listenClickEvent(OnItemClickListener clickListener)
    {
        this.mClickListener = clickListener;
        return this;
    }

    public BaseAdapter listenLongClickEvent(OnItemLongClickListener clickListener)
    {
        mLongClickListener = clickListener;
        return this;
    }

    public OnItemClickListener getClickListener() {
        return mClickListener;
    }

    public OnItemLongClickListener getLongClickListener() {
        return mLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView parent, View view);
    }

}
