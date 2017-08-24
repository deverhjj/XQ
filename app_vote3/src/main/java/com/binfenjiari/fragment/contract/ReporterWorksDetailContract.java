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

import com.binfenjiari.base.BaseContract;
import com.binfenjiari.base.PostIView;
import com.binfenjiari.base.PreIView;
import com.binfenjiari.model.WorksDetail;

/**
 * Created by Huajian Jiang on 2017/8/24.
 * developer.huajianjiang@gmail.com
 */
public interface ReporterWorksDetailContract {
    interface IView
            extends CommentContract.ICommentView, BaseContract.BaseIView<IPresenter>, PreIView,
            PostIView
    {
        void showDetail(WorksDetail detail);
    }

    interface IPresenter
            extends CommentContract.ICommentPresenter, BaseContract.BaseIPresenter<IView>
    {
        void loadDetail(Bundle args);
    }
}
