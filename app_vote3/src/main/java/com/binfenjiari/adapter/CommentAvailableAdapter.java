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

import com.binfenjiari.R;
import com.binfenjiari.model.Comment;
import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.Parent;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;

import java.util.List;

/**
 * Created by Huajian Jiang on 2017/8/24.
 * developer.huajianjiang@gmail.com
 */
public abstract class CommentAvailableAdapter<H extends Parent> extends
        ExpandableHeaderAdapter<ParentViewHolder, ParentViewHolder, ChildViewHolder, Parent, Comment.Reply, H>
{
    public CommentAvailableAdapter(Context ctxt) {
        super(ctxt);
    }

    public CommentAvailableAdapter(Context ctxt, @Nullable List<Parent> parents) {
        super(ctxt, parents);
    }

    @Override
    public ParentViewHolder onCreateParentHeaderViewHolder(ViewGroup parent, int parentType) {
        return new ParentViewHolder(getLayoutInflater().inflate(parentType, parent, false));
    }

    @Override
    public ParentViewHolder onCreateParentBodyViewHolder(ViewGroup parent, int parentType) {
        return new ParentViewHolder<>(getLayoutInflater().inflate(parentType, parent, false));
    }

    @Override
    public void onBindParentBodyViewHolder(ParentViewHolder hvh, int parentPosition) {

    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        return new ChildViewHolder(getLayoutInflater().inflate(childType, child, false));
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder cvh, int parentPosition, int childPosition) {

    }

    @Override
    public int getParentBodyViewType(int parentPosition) {
        return R.layout.item_general_comment_parent;
    }


}
