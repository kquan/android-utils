/*
 * Copyright 2014 Kevin Quan (kevin.quan@gmail.com)
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

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.kevinquan.utils.IOUtils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
    
    public static final int UNKNOWN_VERSION_CODE = -1;
    protected static final String CLASSES_FILE = "classes.dex";
    
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
            Log.w(TAG, "Could not return version code: "+re.getMessage(), re);
        }
        return UNKNOWN_VERSION_CODE;
    }
    
    /**
     * Retrieves the version name of the app whose context is provided
     * @param context The context of the app whose version name we wish to get
     * @return The version name or name if it could not be retrieved.
     */
    public static String getVersionName(Context context) {
        if (context == null) return null;;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo == null) {
                return null;
            }
            return packageInfo.versionName;
        } catch (NameNotFoundException nnfe) {
            Log.w("Could not return version name as could not get package name.", nnfe);
        } catch (RuntimeException re) {
            Log.w(TAG, "Could not return version name: "+re.getMessage(), re);
        }
        return null;
    }
    
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
     * Returns the time when the source for the provided context is built.  Note that the timestamp may
     * not be consistent with the time on the device.
     * @param context The context whose build time to check
     * @return the build time, or 0 if it could not be retrieved.
     */
    public static long getBuildTime(Context context) {
        if (context == null) return 0;
        ApplicationInfo info = null;
        try {
        	info = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException nnfe) {
        	Log.w(TAG, "Could not find info about package: "+context.getPackageName(), nnfe);
        	return 0;
        }
        if (info == null) {
        	Log.w(TAG, "No info returned about package: "+context.getPackageName());
        	return 0;
        }
        ZipFile source = null;
        try {
        	source = new ZipFile(info.sourceDir);
        	ZipEntry classes = source.getEntry(CLASSES_FILE);
        	if (classes != null) {
        		return classes.getTime();
        	}
        	return 0;
        } catch (IOException e) {
        	Log.e(TAG, "Could not open source for package: "+context.getPackageName(), e);
        	return 0;
		} finally {
        	IOUtils.safeClose(source);
        }
    }
    
    /**
     * Checks whether the current environment is Gingerbread or higher 
     * @return True if it is
     */
    public static boolean isGingerbreadOrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
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
    
    /**
     * Checks whether the current environment is Jelly Bean or higher 
     * @return True if it is
     */
    public static boolean isJellyBeanOrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
    
    /**
     * Checks whether the current environment is Jelly Bean MR1 (4.2) or higher 
     * @return True if it is
     */
    public static boolean isJellyBeanMR1OrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
    
    /**
     * Checks whether the current environment is KitKat (4.4) or higher 
     * @return True if it is
     */
    public static boolean isKitKatOrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}