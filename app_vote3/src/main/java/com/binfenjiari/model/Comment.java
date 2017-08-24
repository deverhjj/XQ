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

package com.binfenjiari.model;

import com.biu.modulebase.binfenjiari.model.Reply;
import com.github.huajianjiang.expandablerecyclerview.widget.Parent;

import java.util.List;

/**
 * Created by Huajian Jiang on 2017/8/24.
 * developer.huajianjiang@gmail.com
 */
public class Comment implements Parent, BaseModel {

    private List<Reply> mReplies;

    @Override
    public List getChildren() {
        return mReplies;
    }

    public void setReplies(List<Reply> replies) {
        mReplies = replies;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return true;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    public static class Reply implements BaseModel {

    }

}
