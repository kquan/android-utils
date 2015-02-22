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

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/**
 * Utility class for methods related to a device
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
@SuppressLint("NewApi")
public class DeviceUtils {

    /**
     * Constants for Android permissions
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public interface Permissions {

    	public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
        public static final String INTERNET = "android.permission.INTERNET";
        public static final String WIFI_STATE = "android.permission.ACCESS_WIFI_STATE";
        public static final String NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
        public static final String WRITE_SD = "android.permission.WRITE_EXTERNAL_STORAGE";
        public static final String READ_PROFILE = "android.permission.READ_PROFILE";
    }

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
     * Try and return a unique ID for the device
     * @param activity The current context
     * @return A unique ID or null if none could be found
     */
    public static String getDeviceId(Context context) {
        if (context == null) {
            return null;
        }
        String deviceId = null;
        try {
            deviceId = Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e(TAG, "Could not get device id from Settings", e);
        }
        if (TextUtils.isEmpty(deviceId) && BuildUtils.isGingerbreadOrGreater()) {
            return Build.SERIAL;
        }
        return null;
    }

    /**
     * Retrieves the first phone number on the device
     * @param context  The context to use
     * @return The phone number if one exists
     */
    public static String getFirstPhoneNumber(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /**
     * Retrieve the height of the navigation bar
     * @param activity A visual activity to use to get a window
     * @return The height of the navigation bar, or 0 if it could not be computed.
     */
    public static int getNavigationBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        } else {
            // TODO: Try and calculate from display height and display frame; but this would only work in portrait orientation
            return 0;
        }
    }

    /**
     * Determines the screen orientation.  Return values are defined in {@link Configuration}
     * @param activity An activity to get the display from
     * @return A constant for the screen orientation
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getScreenOrientation(Activity activity) {
        if (activity == null) {
            return Configuration.ORIENTATION_UNDEFINED;
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point dimensions = new Point();
        if (BuildUtils.isHoneycombMR2OrGreater()) {
            display.getSize(dimensions);
        } else {
            dimensions.x = display.getWidth();
            dimensions.y = display.getHeight();
        }
        if (dimensions.x == dimensions.y) {
            return Configuration.ORIENTATION_SQUARE;
        } else if (dimensions.y > dimensions.x) {
            return Configuration.ORIENTATION_PORTRAIT;
        } else {
            return Configuration.ORIENTATION_LANDSCAPE;
        }
    }

    /**
     * Retrieve the height of the status bar
     * @param activity A visual activity to use to get a window
     * @return The height of the status bar, or 0 if it could not be computed.
     */
    public static int getStatusBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        } else {
            // Will possibly not work for translucent or immersive UI.
            Rect windowDisplayFrame = new Rect();
            Window window = activity.getWindow();
            if (window == null) {
                return 0;
            }
            window.getDecorView().getWindowVisibleDisplayFrame(windowDisplayFrame);
            return windowDisplayFrame.top;
        }
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
     * Checks whether an intent to open an activity can be handled by anything on this device
     * @param context The context to check with
     * @param intent The intent to check
     * @return True if the intent can be handled
     */
    public static boolean hasActivityResolverFor(Context context, Intent intent) {
    	if (intent == null || context == null) {
    		return false;
    	}
    	List<ResolveInfo> resolvers = context.getPackageManager().queryIntentActivities(intent, 0);
    	return resolvers != null && !resolvers.isEmpty();
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

    /**
     * Hides the keyboard implicitly if it is visible
     * @param context The context from which to get the InputMethodManager
     * @param viewToGetWindowToken The view from which to get a window token.
     */
    public static void hideKeyboard(Context context, View viewToGetWindowToken) {
        if (context == null || viewToGetWindowToken == null) return;
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewToGetWindowToken.getWindowToken(), 0);
    }

    /**
     * Shows the keyboard and sends input to the provided field
     * @param context The context from which to get the InputMethodManager
     * @param viewToSendInput The view for the keyboard to send input to.
     */
    public static void showKeyboard(Context context, View viewToSendInput) {
        if (context == null || viewToSendInput == null) return;
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewToSendInput, 0);
    }

    private static final String TAG = DeviceUtils.class.getSimpleName();

    public static final String SCHEME_FILE = "file://";

    public static final String LINE_BREAK = System.getProperty("line.separator");
}
