package com.biu.modulebase.binfenjiari.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

/**
 * Json处理
 *
 * @author Lee
 */
public class JSONUtil {
    /************************
     * Gson
     **************************************/
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static Gson getGson() {
        return gson;
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        try {
            return gson.fromJson(json, clz);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Type type) {

        return gson.fromJson(json, type);
    }

    public static JsonElement getJsonElement(String jsonString) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonEl = parser.parse(jsonString);
            if (jsonEl != null && !jsonEl.isJsonNull()) {
                return jsonEl;
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonArray getJsonArray(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonArray() &&
                jsonElement.getAsJsonArray().size() != 0)
        {
            return jsonElement.getAsJsonArray();
        }
        return new JsonArray();
    }

    public static JsonObject getJsonObject(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return new JsonObject();
    }

    public static String getJsonString(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsString();
        }
        return ""; // 默认""
    }


    /**
     * 获取JsonObject中的String
     * @param jsonObject 待取值的jsonObject
     * @param name 目标在jsonObject中的键值
     * @return
     */
    public static String getString(JsonObject jsonObject, String name){
        if(!jsonObject.isJsonNull()&& jsonObject.has(name)){
            return jsonObject.get(name).getAsString();
        }
        return null;
    }


    @SuppressWarnings("null")
    public static Integer getJsonInt(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonPrimitive()) {
            boolean isInt = true;
            try {
                int num = jsonElement.getAsInt();
                return num;
            } catch (JsonParseException e) {
                isInt = false;
            } catch (Exception e) {
            }
            if (!isInt) {
                try {
                    String str = jsonElement.getAsString();
                    if (str != null || !str.equals("null") || !str.equals("")) {
                        return Integer.valueOf(str);
                    }
                } catch (JsonParseException e) {
                } catch (Exception e) {
                }
            }
        }
        return 0; // 默认==0
    }

    public static Boolean getJsonBoolean(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsBoolean();
        }
        return false; // 默认false
    }

    /************************ Gson **************************************/

    /**
     * 获取JSONobject中的JSONobject
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的JSONobject，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return jsonObject.getJSONObject(name);
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getJSONObject解析json异常");
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    /**
     * 获取JSONArray中的JSONobject
     *
     * @param jsonArray 待取值的jsonArray
     * @param index     目标在jsonArray中的索引值
     * @return 当键值存在且对应内容非空时返回获取到的JSONobject，否则返回null
     * @throws JSONException JSON解析异常
     */
    public static JSONObject getJSONObject(JSONArray jsonArray, int index) {
        if (index >= 0 && index < jsonArray.length()) {
            try {
                return jsonArray.getJSONObject(index);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    /**
     * 获取JSONobject中的JSONArray
     *
     * @param jsonObject 待取值的JSONObject
     * @param name       目标在JSONObject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的JSONArray，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String name) {
        if (!(jsonObject == null)) {

            if (!jsonObject.isNull(name)) {
                try {
                    return jsonObject.getJSONArray(name);
                } catch (JSONException e) {
                    Log.d("【JsonUtil】", "getJSONArray解析json异常");
                    e.printStackTrace();
                }
            }

            return new JSONArray();
        }
        return new JSONArray();
    }

    /**
     * 获取JSONobject中的boolean
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的boolean，否则返回false
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static boolean getBoolean(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return jsonObject.getBoolean(name);
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getBoolean解析json异常");
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取JSONobject中的int
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的int，否则返回0
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static int getInt(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return jsonObject.getInt(name);
            } catch (JSONException e) {
                Log.d("JsonUtil", "getInt解析json异常");
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取JSONobject中的long
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的long，否则返回0
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static Long getLong(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return jsonObject.getLong(name);
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getLong解析json异常");
                e.printStackTrace();
            }
        }
        return 0l;
    }

    /**
     * 获取JSONobject中的String
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的String，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static String getString(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return jsonObject.getString(name);
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getString解析json异常");
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取JSONobject中去空格后的String
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的String，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static String getTrimString(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return jsonObject.getString(name).trim();
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getTrimString解析json异常");
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取JSONobject中的Timestamp
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的Timestamp，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static Timestamp getTimestamp(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                if (!jsonObject.getJSONObject(name).isNull("timestamp")) {
                    return new Timestamp(jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getLong("time"));
                } else if (!jsonObject.getJSONObject(name).isNull("time")) {
                    return new Timestamp(jsonObject.getJSONObject(name).getLong("time"));
                }
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getTimestamp解析json异常");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取JSONobject中的Date
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static Date getDate(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return new Date(jsonObject.getJSONObject(name).getLong("time"));
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getDate解析json异常");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取JSONobject中的Date 其中Date是以Long型传递过来
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static Date getDateByLong(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return new Date(jsonObject.getLong(name));
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getDateByLong解析json异常");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取JSONobject中的Date,类型为util.Date 其中Date是以Long型传递过来
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static java.util.Date getUtilDateByLong(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                return new java.util.Date(jsonObject.getLong(name));
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getUtilDateByLong解析json异常");
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean[] jsonArrayToBooleanArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        boolean[] booleanArray = new boolean[jsonArray.length()];
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    booleanArray[i] = jsonArray.getBoolean(i);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.d("JSON", "jsonArrayToBooleanArray获取JSONArray中的元素错误");
            }
        }
        return booleanArray;
    }

    /**
     * 获取JSONobject中的Timestamp格式化输出字符串
     *
     * @param jsonObject 待取值的JSONobject
     * @param name       目标在JSONobject中的键值
     * @return 当键值存在且对应内容非空时返回获取到的Timestamp的yyyy-MM-dd HH:mm:ss格式字符串，否则返回null
     * @throws JSONException JSON解析异常
     * @author maxun
     */
    public static String getTimestampString(JSONObject jsonObject, String name) {
        if (!jsonObject.isNull(name)) {
            try {
                if (!jsonObject.getJSONObject(name).isNull("timestamp")) {
                    String timestampString = new String();
                    timestampString = (jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getInt("year") + 1900) + "-";
                    timestampString += (jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getInt("month") + 1) + "-";
                    timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getInt("date") + " ";
                    timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getInt("hours") + ":";
                    timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getInt("minutes") + ":";
                    timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp")
                            .getInt("seconds");
                    return timestampString;
                } else if (!jsonObject.getJSONObject(name).isNull("time")) {
                    String timestampString = new String();
                    timestampString = (jsonObject.getJSONObject(name).getInt("year") + 1900) + "-";
                    timestampString += (jsonObject.getJSONObject(name).getInt("month") + 1) + "-";
                    timestampString += jsonObject.getJSONObject(name).getInt("date") + " ";
                    timestampString += jsonObject.getJSONObject(name).getInt("hours") + ":";
                    timestampString += jsonObject.getJSONObject(name).getInt("minutes") + ":";
                    timestampString += jsonObject.getJSONObject(name).getInt("seconds");
                    return timestampString;
                }
            } catch (JSONException e) {
                Log.d("【JsonUtil】", "getTimestamp解析json异常");
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject updateJSONObject(JSONObject jsonObject, Map<String, Object> member) {
        if (jsonObject == null || member == null) {
            return null;
        }
        Iterator<String> names = member.keySet().iterator();
        Iterator<Object> values = member.values().iterator();
        while (names.hasNext() && values.hasNext()) {
            String name = names.next();
            Object value = values.next();
            if (jsonObject.has(name)) {
                try {
                    jsonObject.put(name, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonObject;
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static String prettyPrintJsonString(int indentSpaces, JSONObject jsonObject) {
        if (jsonObject == null) {
            return "";
        }
        try {
            return jsonObject.toString(indentSpaces);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void put(JSONObject jsonObject, String key, Object value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putInt(JSONObject jsonObject, String key, int value) {
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putDouble(JSONObject jsonObject, String key, double value) {
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putlong(JSONObject jsonObject, String key, long value) {
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putBoolean(JSONObject jsonObject, String key, boolean value) {
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
