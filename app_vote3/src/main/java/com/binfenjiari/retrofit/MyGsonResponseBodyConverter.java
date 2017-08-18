package com.binfenjiari.retrofit;


import com.binfenjiari.base.AppEcho;
import com.binfenjiari.base.AppExp;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/2/28
 * <br>Email: developer.huajianjiang@gmail.com
 */
final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = MyGsonResponseBodyConverter.class.getSimpleName();
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T obj = adapter.read(jsonReader);
            if (obj instanceof AppEcho) {
                AppEcho echo = (AppEcho) obj;
                if (!echo.isOk()) {
                    throw new AppExp(echo.code(), echo.msg());
                }
            } else if (obj == null) {
                throw new AppExp(AppEcho.ERROR_UNKNOWN, "UNKNOWN ERROR");
            }
            return obj;
        } finally {
            value.close();
        }
    }
}
