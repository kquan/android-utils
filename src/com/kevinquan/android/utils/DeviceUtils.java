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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
    
    public static final String LINE_BREAK = System.getProperty("line.separator");
    
    public static final int UNKNOWN_VERSION_CODE = -1;

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
     * Converts a pixel value to the dp of the device given the provided context
     * @param context The context that defines the pixel to dp mapping
     * @param pixels The pixel value to convert
     * @return The dp value for the device for the provided pixel value
     */
    public static float convertPixelsToDp(Context context, int pixels) {
        if (context == null) {
            Log.w(TAG, "Could not convert pixels to dp as context is null.");
            return pixels;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // 0.5 is added to allow proper rounding when truncating
        return pixels / displayMetrics.density;
    }
    
    /**
     * Retrieves the version code of the app whose context is provided
     * @param context The context of the app whose version code we wish to get
     * @return The version code or {@link UNKNOWN_VERSION_CODE} if it could not be retrieved.
     */
    public static int getVersionCode(Context context) {
        if (context == null) return UNKNOWN_VERSION_CODE;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo == null) {
                return UNKNOWN_VERSION_CODE;
            }
            return packageInfo.versionCode;
        } catch (NameNotFoundException nnfe) {
            Log.w("Could not return version code as could not get package name.", nnfe);
        } catch (RuntimeException re) {
            Log.w(TAG, "Could not return versioncode: "+re.getMessage(), re);
        }
        return UNKNOWN_VERSION_CODE;
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
