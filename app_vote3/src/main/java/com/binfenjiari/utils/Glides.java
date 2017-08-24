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

package com.binfenjiari.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;


import com.binfenjiari.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.huajianjiang.baserecyclerview.util.Predications;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/17
 * <br>Email: developer.huajianjiang@gmail.com
 */

public class Glides {

    public static void loadFromResource(@DrawableRes int resId, @NonNull ImageView iv) {
        Glide.with(iv.getContext())
             .load(resId)
             .asBitmap()
             .dontAnimate()
             .diskCacheStrategy(DiskCacheStrategy.NONE)
             .into(iv);
    }

    public static void loadFromResource(String path, @NonNull ImageView iv) {
        Glide.with(iv.getContext())
             .load(path)
             .asBitmap()
             .dontAnimate()
             .diskCacheStrategy(DiskCacheStrategy.NONE)
             .into(iv);
    }

    public static void loadGifFromResource(@DrawableRes int resId, @NonNull ImageView iv) {
        Glide.with(iv.getContext())
                .load(resId)
                .asGif()
                .dontAnimate()
                .into(iv);
    }

    public static void loadAvatarFormUrl(@NonNull String url, @NonNull ImageView iv) {
        Glide.with(iv.getContext())
             .load(url)
             .asBitmap()
             .placeholder(R.drawable.img_avatar_holder)
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .dontAnimate()
             .into(iv);
    }

    public static void loadFormUrl(@NonNull String url, @NonNull ImageView iv) {
        if (!Predications.isNullOrEmpty(url)) {
            if (!url.startsWith("http") && !url.startsWith("https")) {
                if (url.startsWith("/")) {
                    url = url.substring(1, url.length());
                }
                url = Constants.BASE_IMG_URL + url;
            }
        }
        Glide.with(iv.getContext())
             .load(url)
             .asBitmap()
             .placeholder(R.drawable.img_holder)
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .dontAnimate()
             .into(iv);
    }
}
