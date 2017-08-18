package com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2015/12/23.
 */
public abstract class ParentListItem {
    private static final String TAG = "ParentListItem";

    public abstract List<?> getChildItemList(int parentPosition);

    public abstract boolean isInitiallyExpanded();
}
