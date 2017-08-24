/*
 * Copyright (C) 2017 Huajian Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.binfenjiari.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.widget.BaseAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.Parent;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;

import java.util.List;

/**
 * Created by Huajian Jiang on 2017/8/23.
 * developer.huajianjiang@gmail.com
 */
public abstract class ExpandableHeaderAdapter<HVH extends ParentViewHolder, PVH extends ParentViewHolder, CVH extends ChildViewHolder, P extends Parent, C, H extends Parent>
        extends BaseExpandableAdapter<ParentViewHolder, CVH, Parent, C>
{
    private static final String TAG = ExpandableHeaderAdapter.class.getSimpleName();

    private BaseAdapter.OnItemClickListener mInnerClickListener;


    public ExpandableHeaderAdapter(Context ctxt) {
        super(ctxt);
    }

    public ExpandableHeaderAdapter(Context ctxt, @Nullable List<Parent> parents) {
        super(ctxt, parents);
    }

    public BaseAdapter.OnItemClickListener getInnerClickListener() {
        return mInnerClickListener;
    }

    public void setInnerItemClickListener(BaseAdapter.OnItemClickListener clickListener)
    {
        mInnerClickListener = clickListener;
    }

    public abstract HVH onCreateParentHeaderViewHolder(ViewGroup parent, int parentType);

    public abstract void onBindParentHeaderViewHolder(HVH pvh);

    public abstract PVH onCreateParentBodyViewHolder(ViewGroup parent, int parentType);

    public abstract void onBindParentBodyViewHolder(PVH pvh, int parentPosition);

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        if (parentType == getParentHeaderViewType()) {
            return onCreateParentHeaderViewHolder(parent, parentType);
        } else {
            return onCreateParentBodyViewHolder(parent, parentType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindParentViewHolder(ParentViewHolder pvh, int parentPosition) {
        if (parentPosition == 0) {
            onBindParentHeaderViewHolder((HVH) pvh);
        } else {
            onBindParentBodyViewHolder((PVH) pvh, parentPosition);
        }
    }

    public int getParentHeaderViewType() {
        return 0;
    }

    public int getParentBodyViewType(int parentPosition) {
        return 0;
    }

    @Override
    public int getParentType(int parentPosition) {
        if (parentPosition == 0) {
            return getParentHeaderViewType();
        } else {
            return getParentBodyViewType(parentPosition - 1);
        }
    }

    @SuppressWarnings("unchecked")
    public H getHeader() {
        return (H) getParent(0);
    }

    public void insertHeader(H header) {
        insertParent(0, header);
    }

}
