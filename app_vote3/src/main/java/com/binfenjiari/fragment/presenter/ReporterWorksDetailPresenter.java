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

package com.binfenjiari.fragment.presenter;

import android.os.Bundle;

import com.binfenjiari.base.AppEcho;
import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.AppManager;
import com.binfenjiari.base.NetCallback;
import com.binfenjiari.base.NetObserver;
import com.binfenjiari.base.NetPresenter;
import com.binfenjiari.base.PostCallback;
import com.binfenjiari.base.PreCallback;
import com.binfenjiari.fragment.contract.AbsPaginationContract;
import com.binfenjiari.fragment.contract.ReporterWorksDetailContract;
import com.binfenjiari.model.Comment;
import com.binfenjiari.model.WorksDetail;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Datas;
import com.binfenjiari.utils.Logger;
import com.binfenjiari.utils.Rxs;

import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.binfenjiari.fragment.contract.AbsPaginationContract.UpdateType.TYPE_DEFAULT;
import static com.binfenjiari.fragment.contract.AbsPaginationContract.UpdateType.TYPE_MORE;
import static com.binfenjiari.fragment.contract.AbsPaginationContract.UpdateType.TYPE_REFRESH;

/**
 * Created by Huajian Jiang on 2017/8/24.
 * developer.huajianjiang@gmail.com
 */
public class ReporterWorksDetailPresenter extends NetPresenter<ReporterWorksDetailContract.IView>
        implements ReporterWorksDetailContract.IPresenter
{
    // 头部详情 是否加载成功，为了避免 评论 加载成功但是详情加载失败情况下，
    // 重试再次请求之前加载好的 详情 出现重复的 详情 情况
    private boolean mDetailSuccess = false;

    private int mPage = 1;

    @Override
    public void start() {
    }

    @Override
    public void loadDetail(final Bundle args) {
        //检查 头部详情 之前是否加载成功过，true 跳过加载 头部详情，直接加载其他数据
        if (mDetailSuccess) {
            loadItems(TYPE_DEFAULT, args);
            return;
        }

        final String id = args.getString(Constants.KEY_ID);
        final String token = AppManager.get().getToken();

        Map<String, String> params =
                Datas.paramsOf("work_id", id, "token", token, "methodName", "app_worksInfo");

        pushTask(Rxs.applyBase(service.reporterWorksDetail(params))
                    .flatMap(
                            new Function<AppEcho<WorksDetail>, ObservableSource<AppEcho<Comment>>>() {
                                @Override
                                public ObservableSource<AppEcho<Comment>> apply(
                                        @NonNull AppEcho<WorksDetail> echo) throws Exception
                                {
                                    mDetailSuccess = true;
                                    view.showDetail(echo.data());
                                    Logger.e(TAG, "show detail");

                                    Map<String, String> params = getCommentParams(args);
                                    return Rxs.applyBase(service.comments(params));
                                }
                            })
                    .subscribeWith(new NetObserver<>(new PreCallback<Comment>(view) {
                        @Override
                        public void onEcho(AppEcho<Comment> echo) {
                            Logger.e(TAG, "show Comments");
                        }

                        @Override
                        public void onFailure(AppExp e) {

                        }
                    })));
    }

    private Map<String, String> getCommentParams(Bundle args) {
        final String id = args.getString(Constants.KEY_ID);
        return Datas.paramsOf("id", id, "page", mPage + "", "pageSize", Constants.PER_PAGE + "",
                              "methodName", "app_worksCommentList");
    }

    @Override
    public void loadItems(final AbsPaginationContract.UpdateType type, Bundle args) {
        if (type == TYPE_DEFAULT || type == TYPE_REFRESH) {
            mPage = 1;
        } else if (type == TYPE_MORE) {
            mPage++;
        }

        Map<String, String> params = getCommentParams(args);
        pushTask(Rxs.applyBase(service.comments(params))
                    .subscribeWith(new NetObserver<>(new NetCallback.SimpleNetCallback<Comment>() {
                        @Override
                        public void onEcho(AppEcho<Comment> echo) {
                            Logger.e(TAG, "loading Comments success !");
                        }

                        @Override
                        public void onFailure(AppExp e) {
                            Logger.e(TAG, "loading Comments failed !");
                            if (type == TYPE_MORE) {
                                mPage--;
                            }
                        }
                    })));
    }


    //    new NetObserver<>(new PreCallback<WorksDetail>(view) {
//
//    @Override
//    public void onEcho(AppEcho<WorksDetail> echo) {
//
//    }
//}))
}
