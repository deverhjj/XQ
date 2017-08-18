package com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2015/12/23.
 */
public class ParentWrapper {
    private static final String TAG = "ParentWrapper";
    private ParentListItem mParentListItem;
    private boolean mExpanded;

    public ParentWrapper(ParentListItem parentListItem) {
        mParentListItem = parentListItem;
    }

    public ParentListItem getParentListItem() {
        return mParentListItem;
    }

    public void setParentListItem(ParentListItem parentListItem)
    {
        mParentListItem = parentListItem;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public boolean isInitiallyExpanded() {
        return mParentListItem.isInitiallyExpanded();
    }

    public List<?> getChildItemList(int adapterParentPosition) {
        return mParentListItem.getChildItemList(adapterParentPosition);
    }
}
