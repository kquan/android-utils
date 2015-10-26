package com.kevinquan.android.model;

import com.google.gson.Gson;

/**
 * Basic POJO that will print out as JSON.
 *
 * @author Kevin Quan (kevin.quan@gmail.com)
 */
public abstract class BaseGsonModel {

    @SuppressWarnings("Unused")
    private static final String TAG = BaseGsonModel.class.getSimpleName();

    @Override public String toString() {
        return new Gson().toJson(this);
    }

}
