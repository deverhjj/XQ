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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.model.Apps;
import com.binfenjiari.model.Comment;
import com.binfenjiari.model.WorksDetail;
import com.binfenjiari.utils.Glides;

import com.binfenjiari.utils.Logger;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapterHelper;
import com.github.huajianjiang.baserecyclerview.widget.BaseAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.Parent;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;

import java.util.List;

/**
 * Created by Huajian Jiang on 2017/8/23.
 * developer.huajianjiang@gmail.com
 */
public class ReporterCommentAdapter extends CommentAvailableAdapter<WorksDetail> {
    private static final String TAG = ReporterCommentAdapter.class.getSimpleName();

    public ReporterCommentAdapter(Context ctxt) {
        super(ctxt);
    }

    public ReporterCommentAdapter(Context ctxt, @Nullable List<Parent> parents) {
        super(ctxt, parents);
    }

    @Override
    public void onBindParentHeaderViewHolder(ParentViewHolder pvh) {
        Boolean hasBind = (Boolean) pvh.itemView.getTag(R.id.hasBind);
        if (hasBind != null && hasBind) return;
        pvh.itemView.setTag(R.id.hasBind, true);

        WorksDetail detail = (WorksDetail) pvh.getParent();

        ImageView avatar = pvh.getView(R.id.avatar);
        Glides.loadAvatarFormUrl(detail.user_pic, avatar);
        TextView name = pvh.getView(R.id.name);
        name.setText(detail.username);
        TextView school = pvh.getView(R.id.school);
        school.setText(detail.school_name);

        TextView title = pvh.getView(R.id.title);
        title.setText(detail.title);
        TextView content = pvh.getView(R.id.content);
        content.setText(detail.description);

        TextView date = pvh.getView(R.id.date);
        date.setText(detail.create_time);

        TextView commentCount = pvh.getView(R.id.commentCount);
        commentCount.setText(detail.comment_number);
        TextView likeCount = pvh.getView(R.id.likeCount);
        likeCount.setText(detail.like_number);

        RecyclerView imgList = pvh.getView(R.id.imgList);
        ImgAdapter imgAdapter = new ImgAdapter(getContext());
        imgAdapter.invalidate(Apps.getImgs(detail.img));
        imgList.setAdapter(imgAdapter);
        imgAdapter.clickTargets(R.id.imgItem).listenClickEvent(getInnerClickListener());

        RecyclerView avatarList = pvh.getView(R.id.likeList);
        AvatarAdapter avatarAdapter = new AvatarAdapter(getContext());
        avatarList.setAdapter(avatarAdapter);
        avatarList.addItemDecoration(avatarAdapter.new ItemDecor());
        avatarAdapter.invalidate(detail.likeItems);
    }

    @Override
    public int getParentHeaderViewType() {
        return R.layout.header_works_detail;
    }

}
