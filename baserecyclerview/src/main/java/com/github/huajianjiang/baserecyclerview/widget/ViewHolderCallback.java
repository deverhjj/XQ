/*
 * Copyright (c) 2017 HuaJian Jiang
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
 *
 */

package com.github.huajianjiang.baserecyclerview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/29.
 */
interface ViewHolderCallback {
    int[] onRegisterClickEvent(RecyclerView rv);

    void onItemClick(RecyclerView rv, View v);

    int[] onRegisterLongClickEvent(RecyclerView rv);

    boolean onItemLongClick(RecyclerView rv, View v);
}
