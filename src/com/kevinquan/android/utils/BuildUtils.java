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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

/**
 * Utilities relating to the current build of the app.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class BuildUtils {

    private static final String TAG = BuildUtils.class.getSimpleName();
    
    /**
     * Checks whether the current binary is debuggable
     * @param context The context of the current binary
     * @return True if the current binary is debuggable.
     */
    public static boolean isDebuggable(Context context) {
        if (context == null || context.getPackageManager() == null) return false;
        try {
            return 0 == (context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags &= ApplicationInfo.FLAG_DEBUGGABLE );
        } catch (NameNotFoundException nnfe) {
            Log.w(TAG, "Could not retrieve package info for package: "+context.getPackageName(), nnfe);
            return false;
        }
    }
    
    /**
     * Checks whether the current environment is Honeycomb MR1 or higher 
     * @return True if it is
     */
    public static boolean isHoneycombMR1OrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    
    /**
     * Checks whether the current environment is Honeycomb MR2 or higher 
     * @return True if it is
     */
    public static boolean isHoneycombMR2OrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }
    
    /**
     * Checks whether the current environment is Honeycomb or higher 
     * @return True if it is
     */
    public static boolean isHoneycombOrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    
    /**
     * Checks whether the current environment is Ice Cream Sandwich or higher 
     * @return True if it is
     */
    public static boolean isIceCreamSandwhichOrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
}
