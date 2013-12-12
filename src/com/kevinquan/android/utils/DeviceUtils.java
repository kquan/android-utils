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

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Utility class for methods related to a device
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class DeviceUtils {

    /**
     * Constants for Android permissions 
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public interface Permissions {
        public static final String INTERNET = "android.permission.INTERNET";
        public static final String NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
        public static final String WRITE_SD = "android.permission.WRITE_EXTERNAL_STORAGE";
    }
    
    private static final String TAG = DeviceUtils.class.getSimpleName();
    
    public static final String SCHEME_FILE = "file://";

    /**
     * Converts a dp value to the actual pixel size given the provided context
     * @param context The context that defines the dp to pixel mapping
     * @param dp The dp to convert
     * @return The pixel size for the device for the provided dp
     */
    public static int convertDpToPixels(Context context, int dp) {
        if (context == null) {
            Log.w(TAG, "Could not convert dp to pixels as context is null.");
            return dp;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // 0.5 is added to allow proper rounding when truncating
        return (int) ((dp * displayMetrics.density) + 0.5);
    }
    
    /**
     * Retrieves the window metrics being used by the provided activity
     * @param activity The activity to check
     * @return the display metrics of the provided activity
     */
    public static DisplayMetrics getWindowMetrics(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (activity == null) return metrics;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
    
    /**
     * Checks whether a context has a particular permission
     * @param context The context to check
     * @param permission The permission to check for
     * @return True if the permission exists for the context
     */
    public static boolean hasPermission(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) return false;
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

}
