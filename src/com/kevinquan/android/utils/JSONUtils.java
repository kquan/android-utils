package com.kevinquan.android.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONUtils {

    public static final String TAG = JSONUtils.class.getSimpleName();
    
    public static JSONObject safeGetObject(JSONObject obj, String key) {
        if (obj.has(key)) {
            try {
                return obj.getJSONObject(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get JSONObject from key "+key,e);
            }
        }
        return null;
    }
    
    public static JSONArray safeGetArray(JSONObject obj, String key) {
        if (obj.has(key)) {
            try {
                return obj.getJSONArray(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get JSONArray from key "+key,e);
            }
        }
        return null;
    }
    
    public static JSONObject safeGetJSONObjectFromArray(JSONArray obj, int index) {
        if (obj.length() > index) {
            try {
                return obj.getJSONObject(index);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get object from JSONArray from at index "+index,e);
            }
        }
        return null;
    }
    
    public static String safeGetString(JSONObject obj, String key) {
        if (obj.has(key)) {
            try {
                return obj.getString(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get string from key "+key,e);
            }
        }
        return null;
    }
    
    public static Double safeGetDouble(JSONObject obj, String key) {
        if (obj.has(key)) {
            try {
                return obj.getDouble(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get double from key "+key,e);
            }
        }
        return null;
    }
    
    public static boolean safeGetBoolean(JSONObject obj, String key, boolean defaultValue) {
        if (obj.has(key)) {
            try {
                return obj.getBoolean(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get boolean from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    public static long safeGetLong(JSONObject obj, String key, long defaultValue) {
        if (obj.has(key)) {
            try {
                return obj.getLong(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get long from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    public static void safePutLong(JSONObject obj, String key, long value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put long with key "+key);
        }
    }
    
    public static void safePutString(JSONObject obj, String key, String value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put string with key "+key);
        }
    }
}
