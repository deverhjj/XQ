package com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;


/**
 * Created by jhj_Plus on 2015/12/23.
 */
public class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ChildViewHolder";
    private ViewHolderCallbacks mViewHolderCallbacks;
    private ExpandableRecyclerViewAdapter mAdapter;
    private final SparseArray<View> mViews = new SparseArray<>();
    private boolean mRegisterClickListener;

    public ChildViewHolder(View itemView) {
        this(itemView, null, null);
    }

    public ChildViewHolder(View itemView, ExpandableRecyclerViewAdapter adapter,
            ViewHolderCallbacks callbacks)
    {
        super(itemView);
        mViewHolderCallbacks = callbacks;
        mAdapter = adapter;
        if (callbacks!=null) {
            int[] childViewIds = callbacks.getNeedRegisterClickListenerChildViewIds();
            registerChildViewClickListener(childViewIds);
        }
    }

    public void registerChildViewClickListener(int... childViewIds) {
        if (childViewIds != null) {
            for (int i = 0; i < childViewIds.length; i++) {
                int childViewId = childViewIds[i];
                if (childViewId != View.NO_ID) {
                    View childView = getView(childViewId);
                    if (childView != null) {
                        childView.setOnClickListener(this);
                    }
                }
            }
        }
    }

    public boolean isRegisterClickListener() {
        return mRegisterClickListener;
    }

    public void setRegisterClickListener(boolean registerClickListener) {
        mRegisterClickListener = registerClickListener;
        if (registerClickListener) {
           itemView.setOnClickListener(this);
        }
    }

    public ViewHolderCallbacks getViewHolderCallbacks() {
        return mViewHolderCallbacks;
    }

    public void setViewHolderCallbacks(ViewHolderCallbacks viewHolderCallbacks)
    {
        mViewHolderCallbacks = viewHolderCallbacks;
    }

    public ExpandableRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ExpandableRecyclerViewAdapter adapter)
    {
        mAdapter = adapter;
    }

    public View getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        if (mViewHolderCallbacks != null && mAdapter != null) {
            int childAdapterPosition = getAdapterPosition();
            int childPosition = mAdapter.getChildPosition(childAdapterPosition);
            int parentPosition = mAdapter.getParentPosition(childAdapterPosition);
            int parentAdapterPosition = mAdapter.getAdapterParentPosition(parentPosition);
            mViewHolderCallbacks.onClick(this,v, childPosition, childAdapterPosition, parentPosition,
                    parentAdapterPosition);
        }
    }

    public void bindData(Object data){
        if (mViewHolderCallbacks!=null) {
            mViewHolderCallbacks.bindData(this,data);
        }
    }
}
