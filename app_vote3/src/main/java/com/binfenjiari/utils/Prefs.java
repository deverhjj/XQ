package com.binfenjiari.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Title: 对 SharedPreference 的二次封装
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class Prefs {
    public static final String TAG = Prefs.class.getSimpleName();
    private static final int DEFAULT_INT_VALUE = -1;
    private static final long DEFAULT_LONG_VALUE = -1L;
    private static final float DEFAULT_FLOAT_VALUE = 0.0f;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final String DEFAULT_STRING_VALUE = "";
    private static final Set<String> DEFAULT_STRINGSET_VALUE = null;
    private static Prefs INSTANCE;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private Prefs() {
    }

    private Prefs(@NonNull Context ctxt) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctxt.getApplicationContext());
    }

    private Prefs(@NonNull Context ctxt, @NonNull String name) {
        this(ctxt, name, Context.MODE_PRIVATE);
    }

    private Prefs(@NonNull Context ctxt, @NonNull String name, int mode) {
        mPrefs = ctxt.getApplicationContext().getSharedPreferences(name, mode);
    }

    public static Prefs get(@NonNull Context ctxt) {
        return get(ctxt, false);
    }

    public static Prefs get(@NonNull Context ctxt, boolean newInstance) {
        if (INSTANCE == null || newInstance) {
            INSTANCE = new Prefs(ctxt);
        }
        return INSTANCE;
    }

    public static Prefs get(@NonNull Context ctxt, @NonNull String name) {
        return get(ctxt, name, false);
    }

    public static Prefs get(@NonNull Context ctxt, @NonNull String name, boolean newInstance) {
        if (INSTANCE == null || newInstance) {
            INSTANCE = new Prefs(ctxt, name);
        }
        return INSTANCE;
    }

    public static Prefs get(@NonNull Context ctxt, @NonNull String name, int mode) {
        return get(ctxt, name, mode, false);
    }

    public static Prefs get(@NonNull Context ctxt, @NonNull String name, int mode,
            boolean newInstance)
    {
        if (INSTANCE == null || newInstance) {
            INSTANCE = new Prefs(ctxt, name, mode);
        }
        return INSTANCE;
    }


    public int pullInt(@NonNull String where) {
        return mPrefs.getInt(where, DEFAULT_INT_VALUE);
    }

    public long pullLong(@NonNull String where) {
        return mPrefs.getLong(where, DEFAULT_LONG_VALUE);
    }

    public float pullFloat(@NonNull String where) {
        return mPrefs.getFloat(where, DEFAULT_FLOAT_VALUE);
    }

    public boolean pullBoolean(@NonNull String where) {
        return mPrefs.getBoolean(where, DEFAULT_BOOLEAN_VALUE);
    }

    public String pullString(@NonNull String where) {
        return mPrefs.getString(where, DEFAULT_STRING_VALUE);
    }

    public Set<String> pullStringSet(@NonNull String where) {
        return mPrefs.getStringSet(where, DEFAULT_STRINGSET_VALUE);
    }


    public Prefs pushInt(@NonNull String where, int what) {
        getEditor().putInt(where, what);
        return this;
    }

    public Prefs pushLong(@NonNull String where, long what) {
        getEditor().putLong(where, what);
        return this;
    }

    public Prefs pushFloat(@NonNull String where, float what) {
        getEditor().putFloat(where, what);
        return this;
    }

    public Prefs pushBoolean(@NonNull String where, boolean what) {
        getEditor().putBoolean(where, what);
        return this;
    }

    public Prefs pushString(@NonNull String where, String what) {
        getEditor().putString(where, what);
        return this;
    }

    public Prefs pushStringSet(@NonNull String where, Set<String> what) {
        getEditor().putStringSet(where, what);
        return this;
    }

    public Prefs remove(@NonNull String where) {
        getEditor().remove(where);
        return this;
    }

    public void done() {
        if (mEditor != null) mEditor.apply();
        mEditor = null;
    }

    public void clear() {
        mPrefs.edit().clear().apply();
    }


    private SharedPreferences.Editor getEditor() {
        if (mEditor == null) mEditor = mPrefs.edit();
        return mEditor;
    }

}
