package com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;


/**
 * Created by jhj_Plus on 2015/12/23.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ParentViewHolder";
    private final SparseArray<View> mViews = new SparseArray<>();
    private ViewHolderCallbacks mViewHolderCallbacks;
    private ExpandableRecyclerViewAdapter mAdapter;
    private OnParentListItemExpandCollapseListener mParentListItemExpandCollapseListener;
    private boolean mExpanded;

    public ParentViewHolder(View itemView) {
        this(itemView,null,null);
    }

    public ParentViewHolder(View itemView, ExpandableRecyclerViewAdapter adapter,
            ViewHolderCallbacks callbacks)
    {
        super(itemView);
        mExpanded=false;
        mViewHolderCallbacks=callbacks;
        mAdapter=adapter;
        if (callbacks!=null) {
            int[] childViewIds = callbacks.getNeedRegisterClickListenerChildViewIds();
            registerChildViewClickListener(itemView,childViewIds);
        }
    }

    public void registerChildViewClickListener(final View itemView, int... childViewIds) {
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

    public View getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }


    public void bindData(Object data){
        if (mViewHolderCallbacks!=null) {
            mViewHolderCallbacks.bindData(this,data);
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

    //    @Override
//    public void setChildViewClickListener(int... childViewIds) {
//        int childCount=childViewIds.length;
//        for (int i = 0; i < childCount; i++) {
//            View child=itemView.findViewById(childViewIds[i]);
//            if (child!=null) {
//                child.setOnClickListener(this);
//            }
//        }
//    }
//
//    @Override
//    public void onViewClick(View view, int position) {
//
//    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }


    public interface OnParentListItemExpandCollapseListener {
        void onParentListItemExpanded(int parentPosition);

        void onParentListItemCollapsed(int parentPosition);
    }

    public OnParentListItemExpandCollapseListener getParentListItemExpandCollapseListener() {
        return mParentListItemExpandCollapseListener;
    }

    public void setParentListItemExpandCollapseListener(
            OnParentListItemExpandCollapseListener parentListItemExpandCollapseListener)
    {
        mParentListItemExpandCollapseListener = parentListItemExpandCollapseListener;
    }

    public void setParentListItemOnClickListener() {
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mExpanded) {
            collapseParent();
        } else {
            expandParent();
        }
        if (mViewHolderCallbacks != null && mAdapter != null) {
            int parentAdapterPosition = getAdapterPosition();
            int parentPosition = mAdapter.getParentPosition(parentAdapterPosition);
            mViewHolderCallbacks.onClick(this,v, parentPosition, parentAdapterPosition,
                    parentPosition,parentAdapterPosition);
        }
    }

    private void expandParent() {
        setExpanded(true);
        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemExpanded(getAdapterPosition());
        }
    }

    private void collapseParent() {
        setExpanded(false);
        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemCollapsed(getAdapterPosition());
        }
    }
}
