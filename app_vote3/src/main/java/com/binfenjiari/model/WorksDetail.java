/*
 * Copyright (C) 2015 Huajian Jiang
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

package com.binfenjiari.model;

import com.github.huajianjiang.expandablerecyclerview.widget.Parent;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/23
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class WorksDetail implements Parent {
    private static final String TAG = WorksDetail.class.getSimpleName();

    public String like_number;
    public String title;
    public String username;
    public String school_name;
    public String description;
    /**
     * 是否点赞 1:为点赞 2:已点赞
     */
    public int is_like;
    public String img;
    public String create_time;
    public String user_pic;
    public String comment_number;

    /**
     * 点赞头像,最新点赞的六个
     */
    @SerializedName("like_list")
    public List<Img> likeItems;

    @Override
    public List getChildren() {
        return null;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return false;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

}
