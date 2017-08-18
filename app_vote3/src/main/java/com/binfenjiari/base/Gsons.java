package com.binfenjiari.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.binfenjiari.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/4/13
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Gsons {
    private static final String TAG = Gsons.class.getSimpleName();
    private static Gsons INSTANCE;
    private Gson mGson;

    private Gsons() {
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static Gsons get() {
        if (INSTANCE == null) {
            INSTANCE = new Gsons();
        }
        return INSTANCE;
    }

    public <T> T fromJson(@Nullable String json, @NonNull Class<T> classOfT) {
        try {
            return mGson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            Logger.e(TAG, e.getMessage());
        }
        return null;
    }

    public <T> T fromJson(@Nullable String json, Type typeOfT) {
        try {
            return mGson.fromJson(json, typeOfT);
        } catch (JsonParseException e) {
            Logger.e(TAG, e.getMessage());
        }
        return null;
    }

    public String toJson(@Nullable Object who) {
        return mGson.toJson(who);
    }

    public String toJson(@Nullable Object who, @NonNull Type typeOfWho) {
        return mGson.toJson(who, typeOfWho);
    }
}
