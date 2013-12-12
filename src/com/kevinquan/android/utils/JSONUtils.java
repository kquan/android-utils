/*
 * Copyright 2013 Kevin Quan (kevin.quan@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevinquan.android.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

/**
 * Collection of utilities to work with JSON objects.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class JSONUtils {

    public static final String TAG = JSONUtils.class.getSimpleName();
    
    /**
     * Retrieve a JSON Array object stored at the provided key from the provided JSON object
     * @param obj The JSON object to retrieve from
     * @param key The key to retrieve
     * @return the JSON Array object stored in the key, or null if the key doesn't exist
     */
    public static JSONArray safeGetArray(JSONObject obj, String key) {
        if (obj == null || TextUtils.isEmpty(key)) return null;
        if (obj.has(key)) {
            try {
                return obj.getJSONArray(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get JSONArray from key "+key,e);
            }
        }
        return null;
    }

    /**
     * Retrieve a boolean stored at the provided key from the provided JSON object
     * @param obj The JSON object to retrieve from
     * @param key The key to retrieve
     * @return the boolean stored in the key, or the default value if the key doesn't exist
     */
    public static boolean safeGetBoolean(JSONObject obj, String key, boolean defaultValue) {
        if (obj == null || TextUtils.isEmpty(key)) return defaultValue;
        if (obj.has(key)) {
            try {
                return obj.getBoolean(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get boolean from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    /**
     * Retrieve a double stored at the provided key from the provided JSON object
     * @param obj The JSON object to retrieve from
     * @param key The key to retrieve
     * @return the double stored in the key, or the default value if the key doesn't exist
     */
    public static double safeGetDouble(JSONObject obj, String key, double defaultValue) {
        if (obj == null || TextUtils.isEmpty(key)) return defaultValue;
        if (obj.has(key)) {
            try {
                return obj.getDouble(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get double from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    /**
     * Retrieve a JSON object stored at the provided index from the provided JSON Array object
     * @param obj The JSON Array object to retrieve from
     * @param key The index to retrieve
     * @return the JSON object stored at the index, or null if the index doesn't exist
     */
    public static JSONObject safeGetJSONObjectFromArray(JSONArray obj, int index) {
        if (obj == null || index < 0) return null;
        if (obj.length() > index) {
            try {
                return obj.getJSONObject(index);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get object from JSONArray from at index "+index,e);
            }
        }
        return null;
    }
    
    /**
     * Retrieve a long stored at the provided key from the provided JSON object
     * @param obj The JSON object to retrieve from
     * @param key The key to retrieve
     * @return the long stored in the key, or the default value if the key doesn't exist
     */
    public static long safeGetLong(JSONObject obj, String key, long defaultValue) {
        if (obj == null || TextUtils.isEmpty(key)) return defaultValue;
        if (obj.has(key)) {
            try {
                return obj.getLong(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get long from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    /**
     * Retrieve another JSON object stored at the provided key from the provided JSON object
     * @param obj The JSON object to retrieve from
     * @param key The key to retrieve
     * @return the JSON object stored in the key, or null if the key doesn't exist
     */
    public static JSONObject safeGetObject(JSONObject obj, String key) {
        if (obj == null || TextUtils.isEmpty(key)) return null;
        if (obj.has(key)) {
            try {
                return obj.getJSONObject(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get JSONObject from key "+key,e);
            }
        }
        return null;
    }

    /**
     * Retrieve a string stored at the provided key from the provided JSON object
     * @param obj The JSON object to retrieve from
     * @param key The key to retrieve
     * @return the string stored in the key, or null if the key doesn't exist
     */
    public static String safeGetString(JSONObject obj, String key) {
        if (obj == null || TextUtils.isEmpty(key)) return null;
        if (obj.has(key)) {
            try {
                return obj.getString(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get string from key "+key,e);
            }
        }
        return null;
    }
    
    /**
     * Inserts a long in the provided JSON object at the specified key.  Will overwrite any existing value for the key
     * @param obj The JSON object to write to
     * @param key The key to place the value
     * @param value The value to add
     */
    public static void safePutLong(JSONObject obj, String key, long value) {
        if (obj == null || TextUtils.isEmpty(key)) return;
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put long with key "+key);
        }
    }
    
    /**
     * Inserts a string in the provided JSON object at the specified key.  Will overwrite any existing value for the key
     * @param obj The JSON object to write to
     * @param key The key to place the value
     * @param value The value to add
     */
    public static void safePutString(JSONObject obj, String key, String value) {
        if (obj == null || TextUtils.isEmpty(key)) return;
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put string with key "+key);
        }
    }
}
