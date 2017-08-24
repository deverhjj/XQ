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

package com.binfenjiari.fragment.contract;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/27
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface AbsPaginationContract {
    /**
     * 分页更新类型
     */
    enum UpdateType {
        TYPE_DEFAULT, TYPE_REFRESH, TYPE_MORE
    }

    interface IView<T> {

        void showItems(UpdateType type, List<T> items);

        void onEndOfPage();
    }

    interface IPresenter {

        void loadItems(UpdateType type, Bundle args);
    }

}
